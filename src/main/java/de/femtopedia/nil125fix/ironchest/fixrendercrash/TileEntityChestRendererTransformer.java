package de.femtopedia.nil125fix.ironchest.fixrendercrash;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.TileEntityChestRenderer")
public class TileEntityChestRendererTransformer extends MiniPlusTransformer {

    @Patch.Method.AffectsControlFlow
    @Patch.Method("renderTileEntityChestAt(Lnet/minecraft/src/TileEntityChest;DDDF)V")
    public void checkBlockAndAbort(PatchContext ctx) {
        LabelNode LdontReturn = new LabelNode();

        ctx.search(
                ALOAD(10),
                CHECKCAST("net/minecraft/src/BlockChest")
        ).jumpBefore();
        ctx.add(
                ALOAD(10),
                INSTANCEOF("net/minecraft/src/BlockChest"),
                IFNE(LdontReturn),
                RETURN(),
                LdontReturn
        );
    }

}
