package miscperipherals.asm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.IClassTransformer;

public class ImplementIfLoadedTransformer implements IClassTransformer {
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		ClassReader cr = new ClassReader(bytes);
		ClassNode cn = new ClassNode();
		cr.accept(cn, 0);
		
		String[] entries = null;
		if (cn.visibleAnnotations != null) {
			for (AnnotationNode node : (Iterable<AnnotationNode>)cn.visibleAnnotations) {
				if (node.desc.equals("Lmiscperipherals/asm/ImplementIfLoaded;")) {
					for (int i = 0; i < node.values.size(); i += 2) {
						String key = (String)node.values.get(i);
						if (key.equals("value")) {
							entries = (String[])((Collection)node.values.get(i + 1)).toArray(new String[0]);
						}
					}
					break;
				}
			}
		}
		if (entries == null) return bytes;
		if (entries.length % 2 == 1) throw new IllegalArgumentException("@ImplementIfLoaded value must be in pairs of required modid and target interface class name");
		
		List<String> toRemove = new ArrayList<String>((entries.length / 2) + 1);
		for (int i = 0; i < entries.length; i += 2) {
			if (!Loader.isModLoaded(entries[i])) toRemove.add(entries[i + 1]);
		}
		List<String> removingInterfaces = new ArrayList<String>(toRemove.size());
		for (String iface : (Iterable<String>)cn.interfaces) {
			for (String removing : toRemove) {
				if (iface.startsWith(removing.replace('.', '/'))) {
					removingInterfaces.add(iface);
				}
			}
		}
		cn.interfaces.removeAll(removingInterfaces);
		
		List<MethodNode> removingMethods = new ArrayList<MethodNode>();
		for (MethodNode node : (Iterable<MethodNode>)cn.methods) {
			if (node.visibleAnnotations != null) {
				for (AnnotationNode anode : (Iterable<AnnotationNode>)node.visibleAnnotations) {
					if (node.desc.equals("Lmiscperipherals/asm/OwnerInterface;")) {
						for (int i = 0; i < anode.values.size(); i += 2) {
							String key = (String)anode.values.get(i);
							if (key.equals("value")) {
								String iname = ((String)anode.values.get(i + 1)).replace('/', '.');
								for (String removed : removingInterfaces) {
									removingMethods.add(node);
								}
								
								break;
							}
						}
						
						break;
					}
				}
			}
		}
		cn.methods.removeAll(removingMethods);
		
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		cn.accept(cw);
		return cw.toByteArray();
	}
}
