package de.femtopedia.nil125fix;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import nilloader.api.ClassRetransformer;
import nilloader.api.lib.asm.tree.AbstractInsnNode;
import nilloader.api.lib.asm.tree.InsnList;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.asm.tree.MethodInsnNode;
import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;

public abstract class MiniPlusTransformer extends MiniTransformer implements ClassRetransformer {

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
        return FMLClientHandler.instance().getClient();
    }

}
