package de.femtopedia.nil125fix.morecreepsandweirdos.fixhealthjitter;

import de.femtopedia.nil125fix.AdvPlusTransformer;
import nilloader.api.lib.asm.Opcodes;
import nilloader.api.lib.asm.tree.AbstractInsnNode;
import nilloader.api.lib.asm.tree.ClassNode;
import nilloader.api.lib.asm.tree.FieldInsnNode;
import nilloader.api.lib.asm.tree.FieldNode;

import static de.femtopedia.nil125fix.PlusTransformer.toInsnList;

public class CREEPSEntityTransformer extends AdvPlusTransformer {

    @Override
    public boolean canTransform(ClassLoader loader, String className) {
        return className.startsWith("CREEPS");
    }

    @Override
    public boolean transform(ClassLoader classLoader, ClassNode classNode) {
        if (!canTransform(classLoader, classNode.name)) return false;

        classNode.fields.add(new FieldNode(Opcodes.ASM5, Opcodes.ACC_PRIVATE,
                "nilfixMaxHealth", "I", null, 0));

        classNode.methods.stream()
                .filter(m -> m.name.equals("<init>"))
                .forEach(c -> {
                    for (AbstractInsnNode i : c.instructions) {
                        if (!(i instanceof FieldInsnNode)) continue;
                        FieldInsnNode fi = (FieldInsnNode) i;
                        if (fi.getOpcode() != Opcodes.PUTFIELD) continue;
                        if (!fi.name.equals(remapField("net/minecraft/src/EntityLiving", "health", "I")))
                            continue;

                        c.instructions.insert(fi, toInsnList(
                                ALOAD(0),
                                ALOAD(0),
                                GETFIELD(fi.owner, fi.name, fi.desc),
                                PUTFIELD(classNode.name, "nilfixMaxHealth", "I")
                        ));
                    }
                });

        classNode.methods.stream()
                .filter(m ->
                        m.name.equals(remapMethod("net/minecraft/src/EntityLiving", "getMaxHealth", "()I")))
                .forEach(m -> m.instructions.insert(toInsnList(
                        ALOAD(0),
                        GETFIELD(classNode.name, "nilfixMaxHealth", "I"),
                        IRETURN()
                )));

        return true;
    }

}
