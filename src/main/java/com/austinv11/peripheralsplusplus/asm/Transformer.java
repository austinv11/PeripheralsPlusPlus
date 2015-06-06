package com.austinv11.peripheralsplusplus.asm;

import com.austinv11.peripheralsplusplus.PeripheralsPlusPlus;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

public class Transformer implements IClassTransformer, Opcodes {
	
	@Override
	public byte[] transform(String className, String newClassName, byte[] byteCode) {
		if (className.equals("dan200.computercraft.shared.pocket.items.ItemPocketComputer")) {
			PeripheralsPlusPlus.LOGGER.info("Adding pocket computer peripherals");
			return transformPocketComputerCreateServerComputer(byteCode);
		}
		return byteCode;
	}
	
	private byte[] transformPocketComputerCreateServerComputer(byte[] byteCode) {
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(byteCode);
		classReader.accept(classNode, 0);
		for (MethodNode m : classNode.methods)
			if (m.name.equals("createServerComputer")) {
				Iterator<AbstractInsnNode> nodes = m.instructions.iterator();
				int i = 0;
				while (nodes.hasNext()) {
					AbstractInsnNode node = nodes.next();
					if (node.getOpcode() == INVOKEVIRTUAL) {
						if (i != 10) {
							i++;
							continue;
						}
						InsnList instructions = new InsnList();
						instructions.add(new VarInsnNode(ALOAD, 7));
						instructions.add(new VarInsnNode(ALOAD, 3));
						instructions.add(new VarInsnNode(ALOAD, 2));
						instructions.add(new MethodInsnNode(INVOKESTATIC, "com/austinv11/peripheralsplusplus/hooks/ComputerCraftHooks", "onPocketComputerCreate", "(Ldan200/computercraft/shared/computer/core/ServerComputer;Lnet/minecraft/item/ItemStack;Lnet/minecraft/inventory/IInventory;)V", false));
						m.instructions.insert(node, instructions);
						break;
					}
				}
			} else if (m.name.equals("func_77653_i") || m.name.equals("getItemStackDisplayName")) {
				Iterator<AbstractInsnNode> nodes = m.instructions.iterator();
				while (nodes.hasNext()) {
					AbstractInsnNode node = nodes.next();
					if (node.getOpcode() == ASTORE) {
						InsnList instructions = new InsnList();
						instructions.add(new VarInsnNode(ALOAD, 2));
						instructions.add(new VarInsnNode(ALOAD, 1));
						instructions.add(new MethodInsnNode(INVOKESTATIC, "com/austinv11/peripheralsplusplus/hooks/ComputerCraftHooks", "getName", "(Ljava/lang/String;Lnet/minecraft/item/ItemStack;)Ljava/lang/String;", false));
						instructions.add(new InsnNode(ARETURN));
						m.instructions.insert(node, instructions);
						break;
					}
				}
			} else if (m.name.equals("func_77663_a") || m.name.equals("onUpdate")) {
				Iterator<AbstractInsnNode> nodes = m.instructions.iterator();
				int i = 0;
				while (nodes.hasNext()) {
					AbstractInsnNode node = nodes.next();
					if (node.getOpcode() == ASTORE) {
						if (i != 1) {
							i++;
							continue;
						}
						InsnList instructions = new InsnList();
						instructions.add(new VarInsnNode(ALOAD, 3));
						instructions.add(new VarInsnNode(ALOAD, 1));
						instructions.add(new VarInsnNode(ALOAD, 7));
						instructions.add(new MethodInsnNode(INVOKESTATIC, "com/austinv11/peripheralsplusplus/hooks/ComputerCraftHooks", "update", "(Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;Ldan200/computercraft/shared/computer/core/ServerComputer;)V", false));
						m.instructions.insert(node, instructions);
						break;
					}
				}
			}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		return writer.toByteArray();
	}
}
