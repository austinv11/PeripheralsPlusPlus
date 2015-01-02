package miscperipherals.core;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;

import net.minecraft.world.World;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class TickHandler implements ITickHandler {
	private static Map<Integer, LinkedBlockingQueue<FutureTask>> callbacks = Collections.synchronizedMap(new HashMap<Integer, LinkedBlockingQueue<FutureTask>>());
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if (type.contains(TickType.WORLD)) {
			World world = (World)tickData[0];
			if (callbacks.containsKey(world.provider.dimensionId)) {
				Queue<FutureTask> callbackList = callbacks.get(world.provider.dimensionId);
				
				FutureTask callback = callbackList.poll();
				while (callback != null) {
					callback.run();
					callback = callbackList.poll();
				}
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.WORLD);
	}

	@Override
	public String getLabel() {
		return "MiscPeripherals";
	}
	
	public static <T> Future<T> addTickCallback(World world, Callable<T> callback) throws InterruptedException {
		if (!callbacks.containsKey(world.provider.dimensionId)) callbacks.put(world.provider.dimensionId, new LinkedBlockingQueue<FutureTask>());
		
		FutureTask task = new FutureTask<T>(callback) {
			@Override
			protected void done() {
				try {
					if (!isCancelled()) get();
				} catch (Throwable e) {
					MiscPeripherals.log.warning("Exception while executing tick callback! " + this);
					e.printStackTrace();
				}
			}
		};
		callbacks.get(world.provider.dimensionId).put(task);
		return task;
	}
}
