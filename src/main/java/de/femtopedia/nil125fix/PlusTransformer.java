package de.femtopedia.nil125fix;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import net.minecraft.client.Minecraft;
import nilloader.api.lib.asm.tree.AbstractInsnNode;
import nilloader.api.lib.asm.tree.InsnList;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.asm.tree.MethodInsnNode;
import nilloader.api.lib.mini.InsnBuilder;
import nilloader.api.lib.mini.PatchContext;

public interface PlusTransformer {

    default String hooks() {
        return getClass().getName().replace('.', '/') + "$Hooks";
    }

    void removePrintlns(PatchContext ctx);

    static void removePrintlns(InsnBuilder builder, PatchContext ctx) {
        while (true) {
            PatchContext.SearchResult res = ctx.search(
                    builder.GETSTATIC("java/lang/System", "out", "Ljava/io/PrintStream;")
            );
            if (!res.isSuccessful()) break;

            LabelNode Lskip = new LabelNode();

            res.jumpBefore();
            ctx.add(
                    builder.GOTO(Lskip)
            );

            boolean foundPrintln = false;
            for (int k = ctx.getPointer(); k < ctx.getLength(); ++k) {
                ctx.jumpForward(1);
                AbstractInsnNode node = ctx.get();
                if (!(node instanceof MethodInsnNode)) continue;
                MethodInsnNode mn = (MethodInsnNode) node;
                if (mn.owner.equals("java/io/PrintStream") && mn.name.equals("println")) {
                    foundPrintln = true;
                    break;
                }
            }

            if (!foundPrintln) {
                throw new IllegalArgumentException("Could not find matching println call!");
            }
            ctx.jumpForward(1);
            ctx.add(
                    Lskip
            );
        }
    }

    static InsnList toInsnList(AbstractInsnNode... insns) {
        InsnList li = new InsnList();
        for (AbstractInsnNode ain : insns) li.add(ain);
        return li;
    }

    static Minecraft getMinecraftInstance() {
        try {
            if (Container.getMinecraftInstance == null) {
                Class<?> ModLoader = Class.forName("ModLoader");
                Method m = ModLoader.getMethod("getMinecraftInstance");
                MethodHandles.Lookup lookup = MethodHandles.lookup();
                Container.getMinecraftInstance = lookup.unreflect(m);
            }
            return (Minecraft) Container.getMinecraftInstance.invoke();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    class Container {
        private static MethodHandle getMinecraftInstance;
    }

}
