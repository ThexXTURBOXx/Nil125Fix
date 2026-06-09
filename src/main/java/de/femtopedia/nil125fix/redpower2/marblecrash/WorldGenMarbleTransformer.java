package de.femtopedia.nil125fix.redpower2.marblecrash;

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
                "addBlockSafe", "(IIIIIII)V", null, null);

        LabelNode Lskip = new LabelNode();
        addBlockSafe.instructions = toInsnList(
                ILOAD(1),
                ILOAD(3),
                ILOAD(5),
                ILOAD(7),
                INVOKESTATIC(hooks(), "isSameChunk", "(IIII)Z"),
                IFNE(Lskip),
                RETURN(),
                Lskip,
                ALOAD(0),
                ILOAD(1),
                ILOAD(2),
                ILOAD(3),
                ILOAD(4),
                INVOKESPECIAL("eloraam/world/WorldGenMarble", "addBlock", "(IIII)V"),
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
                    ILOAD(2),
                    ILOAD(3),
                    ILOAD(4),
                    INVOKESTATIC(hooks(), "getBlockIdSafe", "(Lnet/minecraft/src/World;IIIIII)I"),
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
                    ILOAD(2),
                    ILOAD(3),
                    ILOAD(4),
                    INVOKESPECIAL("eloraam/world/WorldGenMarble", "addBlockSafe", "(IIIIIII)V"),
                    GOTO(Lskip)
            );

            res.jumpAfter();
            ctx.add(
                    Lskip
            );
        }
    }

    public static class Hooks {

        public static boolean isSameChunk(int x1, int z1, int x2, int z2) {
            return x1 << 4 == x2 << 4 && z1 << 4 == z2 << 4;
        }

        public static int getBlockIdSafe(World w, int x, int y, int z, int xOld, int yOld, int zOld) {
            // Do not look into other chunks as they may be unpopulated and cause stack overflows
            if (!isSameChunk(x, z, xOld, zOld)) return -1;
            return w.getBlockId(x, y, z);
        }

    }

}
