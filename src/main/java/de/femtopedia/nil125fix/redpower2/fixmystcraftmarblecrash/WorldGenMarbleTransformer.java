package de.femtopedia.nil125fix.redpower2.fixmystcraftmarblecrash;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import net.minecraft.src.World;
import nilloader.api.lib.asm.Opcodes;
import nilloader.api.lib.asm.tree.ClassNode;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.asm.tree.MethodNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("eloraam.world.WorldGenMarble")
public class WorldGenMarbleTransformer extends MiniPlusTransformer {

    @Override
    protected boolean modifyClassStructure(ClassNode clazz) {
        MethodNode addBlockSafe = new MethodNode(Opcodes.ASM5, Opcodes.ACC_PRIVATE,
                "addBlockSafe", "(IIIILxd;)V", null, null);

        LabelNode Lskip = new LabelNode();
        addBlockSafe.instructions = toInsnList(
                ALOAD(5),
                ILOAD(1),
                ILOAD(2),
                ILOAD(3),
                INVOKESTATIC(hooks(), "isUnloadedChunk", "(Lnet/minecraft/src/World;III)Z"),
                IFNE(Lskip),
                ALOAD(0),
                ILOAD(1),
                ILOAD(2),
                ILOAD(3),
                ILOAD(4),
                INVOKESPECIAL("eloraam/world/WorldGenMarble", "addBlock", "(IIII)V"),
                Lskip,
                RETURN()
        );

        clazz.methods.add(addBlockSafe);

        return true; // don't know if absolutely necessary, let's just do it...
    }

    @Patch.Method.AffectsControlFlow
    @Patch.Method("searchBlock(Lnet/minecraft/src/World;IIII)V")
    public void shortcutSearchBlock(PatchContext ctx) {
        while (true) {
            PatchContext.SearchResult res = ctx.search(
                    INVOKEVIRTUAL("net/minecraft/src/World", "getBlockId", "(III)I")
            );
            if (!res.isSuccessful()) break;

            LabelNode Lskip = new LabelNode();

            res.jumpBefore();
            ctx.add(
                    INVOKESTATIC(hooks(), "getBlockIdSafe", "(Lnet/minecraft/src/World;III)I"),
                    GOTO(Lskip)
            );

            res.jumpAfter();
            ctx.add(
                    Lskip
            );
        }

        while (true) {
            PatchContext.SearchResult res = ctx.search(
                    INVOKESPECIAL("eloraam/world/WorldGenMarble", "addBlock", "(IIII)V")
            );
            if (!res.isSuccessful()) break;

            LabelNode Lskip = new LabelNode();

            res.jumpBefore();
            ctx.add(
                    ALOAD(1),
                    INVOKESPECIAL("eloraam/world/WorldGenMarble", "addBlockSafe", "(IIIILnet/minecraft/src/World;)V"),
                    GOTO(Lskip)
            );

            res.jumpAfter();
            ctx.add(
                    Lskip
            );
        }
    }

    public static class Hooks {

        public static boolean isUnloadedChunk(World w, int x, int y, int z) {
            return !w.blockExists(x, y, z);
        }

        public static int getBlockIdSafe(World w, int x, int y, int z) {
            // Do not look into unloaded chunks as they may be unpopulated and cause stack overflows
            return isUnloadedChunk(w, x, y, z) ? -1 : w.getBlockId(x, y, z);
        }

    }

}
