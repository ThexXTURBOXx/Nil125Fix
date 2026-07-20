package de.femtopedia.nil125fix;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import net.minecraft.client.Minecraft;
import nilloader.api.ClassRetransformer;
import nilloader.api.lib.asm.tree.AbstractInsnNode;
import nilloader.api.lib.asm.tree.InsnList;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.asm.tree.MethodInsnNode;
import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;

public abstract class MiniPlusTransformer extends MiniTransformer implements ClassRetransformer {

    private static MethodHandle getMinecraftInstance;

    protected final String hooks() {
        return getClass().getName().replace('.', '/') + "$Hooks";
    }

    protected final InsnList toInsnList(AbstractInsnNode... insns) {
        InsnList li = new InsnList();
        for (AbstractInsnNode ain : insns) li.add(ain);
        return li;
    }

    protected final void removePrintlns(PatchContext ctx) {
        while (true) {
            PatchContext.SearchResult res = ctx.search(
                    GETSTATIC("java/lang/System", "out", "Ljava/io/PrintStream;")
            );
            if (!res.isSuccessful()) break;

            LabelNode Lskip = new LabelNode();

            res.jumpBefore();
            ctx.add(
                    GOTO(Lskip)
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

    public static Minecraft getMinecraftInstance() {
        try {
            if (getMinecraftInstance == null) {
                Class<?> ModLoader = Class.forName("ModLoader");
                Method m = ModLoader.getMethod("getMinecraftInstance");
                MethodHandles.Lookup lookup = MethodHandles.lookup();
                getMinecraftInstance = lookup.unreflect(m);
            }
            return (Minecraft) getMinecraftInstance.invoke();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

}
