package com.austinv11.peripheralsplusplus.lua;

import com.austinv11.peripheralsplusplus.mount.EnderNetMount;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.ILuaObject;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IComputerAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LuaObjectServerControl implements ILuaObject {

	public static HashMap<Integer, String> domains = new HashMap<Integer, String>();
	public static HashMap<String,List<String>> subdomains = new HashMap<String,List<String>>();

	private String domain;
	private boolean isSubdomain;
	private IComputerAccess computer;
	private String mountedLocation;

	public LuaObjectServerControl(String domain, boolean isSubdomain, IComputerAccess computer) {
		this.domain = domain;
		this.isSubdomain = isSubdomain;
		this.computer = computer;
		mountedLocation = "servers/"+(isSubdomain ? domain.split(".")[1]+"/"+domain.split(".")[0] : domain);
		computer.mountWritable(mountedLocation, new EnderNetMount(domain, isSubdomain));
	}

	@Override
	public String[] getMethodNames() {
		return new String[]{"open"/*Allows connections*/,
		"close",/*Disallows connections*/
		"openSubdomainSocket"/*Same as modem.openSocket, except for subdomains*/};
	}

	@Override
	public Object[] callMethod(ILuaContext context, int method, Object[] arguments) throws LuaException, InterruptedException {
		switch (method) {
			case 0:
				if (!isSubdomain)
					domains.put(computer.getID(), domain);
				else {
					String[] splitDomain = domain.split(".");
					List<String> subs;
					if (subdomains.containsKey(splitDomain[1]))
						subs = subdomains.get(splitDomain[1]);
					else
						subs = new ArrayList<String>();
					if (subs.isEmpty() || !subs.contains(splitDomain[0]))
						subs.add(splitDomain[0]);
					subdomains.put(splitDomain[1], subs);
				}
				return new Object[0];
			case 1:
				if (!isSubdomain)
					domains.remove(computer.getID());
				else {
					String[] splitDomain = domain.split(".");
					subdomains.remove(splitDomain[0]);
				}
				computer.unmount(mountedLocation);
				return new Object[0];
			case 2:
				if (isSubdomain)
					throw new LuaException("Cannot make a subdomain of a subdomain (SubdomainceptionException)");
				if (arguments.length < 1)
					throw new LuaException("Too few arguments");
				if (!(arguments[0] instanceof String))
					throw new LuaException("Bad argument #1 (expected string)");
				if (LuaObjectServerControl.subdomains.get(domain).contains((String)arguments[0]))
					throw new LuaException("Domain '"+(String)arguments[0]+"' already exists");
				return new Object[]{new LuaObjectServerControl((String) arguments[0]+"."+domain, true, computer)};
		}
		return new Object[0];
	}
}
