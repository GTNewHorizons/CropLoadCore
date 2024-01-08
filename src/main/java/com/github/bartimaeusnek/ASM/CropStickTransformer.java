package com.github.bartimaeusnek.ASM;

import java.util.Arrays;
import java.util.List;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class CropStickTransformer implements IClassTransformer {

    private final static String[] classesBeingTransformed = { "ic2.core.crop.BlockCrop",
            "ic2.core.item.tool.ItemWeedingTrowel" };

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        boolean isObfuscated = name.equals(transformedName);
        int index = Arrays.asList(classesBeingTransformed).indexOf(transformedName);
        return index == 0 ? transform(index, 1, transform(index, 0, basicClass, isObfuscated), isObfuscated)
                : index != (-1) ? transform(index, 0, basicClass, isObfuscated) : basicClass;
    }

    private byte[] transform(int index, int method, byte[] basicClass, boolean isObfuscated) {
        try {
            switch (index) {
                case 0: {
                    switch (method) {
                        case 0: {
                            // log what we change
                            CropLoadCoreASM.cppASMlogger.info("Patching CropBlock to be placeable on any soil");
                            // obf and de-obf name of the function
                            final String methodToPatch = "canPlaceBlockAt";
                            final String methodToPatch_obf = "func_149742_c";
                            // if isObfuscated then obfname and descriptor else de-obf name and descriptor
                            String name = !isObfuscated ? methodToPatch : methodToPatch_obf;
                            String desc = !isObfuscated ? "(Lnet/minecraft/world/World;III)Z" : "(Lahb;III)Z";
                            // class reader to read in the class
                            ClassReader reader = new ClassReader(basicClass);
                            // class node to modify the read in class
                            ClassNode classNode = new ClassNode();

                            reader.accept(classNode, 0);
                            // get a list of methods declared for this class
                            final List<MethodNode> methods = classNode.methods;
                            // loop through methods until the method name equals the one defined above
                            for (MethodNode methodNode : methods) {
                                if (methodNode.name.equals(name)) {

                                    // replace the method by a new one
                                    InsnList insnList = new InsnList();
                                    insnList.add(new VarInsnNode(Opcodes.ALOAD, 1)); // load the OBJECT as REFERENCE
                                                                                     // (ALOAD) from the descriptor, why
                                                                                     // so ever it starts at 1...
                                    insnList.add(new VarInsnNode(Opcodes.ILOAD, 2)); // load the Integer from the
                                                                                     // descriptor
                                    insnList.add(new VarInsnNode(Opcodes.ILOAD, 3));
                                    insnList.add(new VarInsnNode(Opcodes.ILOAD, 4));

                                    // invoke the static replacement method, static for convenience
                                    insnList.add(
                                            new MethodInsnNode(
                                                    Opcodes.INVOKESTATIC,
                                                    "com/github/bartimaeusnek/ASM/CropStickTransformerReplaceMethod",
                                                    "patchedcanPlaceBlockAt",
                                                    desc,
                                                    false));
                                    insnList.add(new InsnNode(Opcodes.IRETURN)); // return an integer from a method
                                                                                 // (it's a boolean, which will be 1 or
                                                                                 // 0)
                                    methodNode.instructions = insnList; // copy the new method into the methodNode
                                }
                            }
                            // make a class writer that computes Maxes, Frames and decompresses and recompresses the
                            // method
                            ClassWriter cw = new ClassWriter(
                                    reader,
                                    ClassReader.EXPAND_FRAMES | ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                            // let the class node use the class writer
                            classNode.accept(cw);
                            // return the method as byte array
                            return cw.toByteArray();
                        }

                        case 1: {
                            CropLoadCoreASM.cppASMlogger
                                    .info("Patching CropBlock to not pop off when neighbours change");
                            String methodToPatch = "onNeighborBlockChange";
                            String methodToPatch_obf = "func_149695_a";
                            String name = !isObfuscated ? methodToPatch : methodToPatch_obf;
                            String desc = !isObfuscated ? "(Lnet/minecraft/world/World;IIILnet/minecraft/block/Block;)V"
                                    : "(Lahb;IIILaji;)V";
                            ClassReader reader = new ClassReader(basicClass);
                            ClassNode classNode = new ClassNode();

                            reader.accept(classNode, 0);

                            final List<MethodNode> methods = classNode.methods;

                            for (MethodNode methodNode : methods) {
                                if (methodNode.name.equals(name)) {
                                    InsnList insnList = new InsnList();
                                    insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                                    insnList.add(new VarInsnNode(Opcodes.ILOAD, 2));
                                    insnList.add(new VarInsnNode(Opcodes.ILOAD, 3));
                                    insnList.add(new VarInsnNode(Opcodes.ILOAD, 4));
                                    insnList.add(new VarInsnNode(Opcodes.ALOAD, 5));
                                    insnList.add(
                                            new MethodInsnNode(
                                                    Opcodes.INVOKESTATIC,
                                                    "com/github/bartimaeusnek/ASM/CropStickTransformerReplaceMethod",
                                                    "patchedonNeighborBlockChange",
                                                    desc,
                                                    false));
                                    insnList.add(new InsnNode(Opcodes.RETURN));
                                    methodNode.instructions = insnList;
                                }
                            }

                            ClassWriter cw = new ClassWriter(
                                    reader,
                                    ClassReader.EXPAND_FRAMES | ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                            classNode.accept(cw);
                            return cw.toByteArray();
                        }
                    }
                }
                case 1: {
                    CropLoadCoreASM.cppASMlogger.info("Patching WeedingTrowel to accept custom Weeds");
                    String name = "onItemUseFirst";
                    // String desc = !isObfuscated ?
                    // "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;IIIIFFF)Z"
                    // : "(Ladd;Lyz;Lahb;IIIIFFF)Z";
                    // This is forge and so never obfuscated?????
                    String desc = "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;IIIIFFF)Z";
                    ClassReader reader = new ClassReader(basicClass);
                    ClassNode classNode = new ClassNode();

                    reader.accept(classNode, 0);

                    final List<MethodNode> methods = classNode.methods;

                    for (MethodNode methodNode : methods) {
                        if (methodNode.name.equals(name)) {
                            InsnList insnList = new InsnList();
                            insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                            insnList.add(new VarInsnNode(Opcodes.ALOAD, 2));
                            insnList.add(new VarInsnNode(Opcodes.ALOAD, 3));
                            insnList.add(new VarInsnNode(Opcodes.ILOAD, 4));
                            insnList.add(new VarInsnNode(Opcodes.ILOAD, 5));
                            insnList.add(new VarInsnNode(Opcodes.ILOAD, 6));
                            insnList.add(new VarInsnNode(Opcodes.ILOAD, 7));
                            insnList.add(new VarInsnNode(Opcodes.FLOAD, 8));
                            insnList.add(new VarInsnNode(Opcodes.FLOAD, 9));
                            insnList.add(new VarInsnNode(Opcodes.FLOAD, 10));
                            insnList.add(
                                    new MethodInsnNode(
                                            Opcodes.INVOKESTATIC,
                                            "com/github/bartimaeusnek/ASM/CropStickTransformerReplaceMethod",
                                            "patchedonItemUseFirst",
                                            desc,
                                            false));
                            insnList.add(new InsnNode(Opcodes.IRETURN));
                            methodNode.instructions = insnList;
                        }
                    }

                    ClassWriter cw = new ClassWriter(
                            reader,
                            ClassReader.EXPAND_FRAMES | ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                    classNode.accept(cw);
                    return cw.toByteArray();
                }
            }
            return basicClass;

        } catch (Exception e) {
            e.printStackTrace();
            return basicClass;
        }

    }

}
