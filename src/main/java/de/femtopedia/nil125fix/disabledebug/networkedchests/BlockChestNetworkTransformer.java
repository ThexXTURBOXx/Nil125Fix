package de.femtopedia.nil125fix.disabledebug.networkedchests;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("BlockChestNetwork")
public class BlockChestNetworkTransformer extends MiniPlusTransformer {

    @Patch.Method.AffectsControlFlow
    @Patch.Method("saveContents(Lnet/minecraft/src/NBTTagList;)Lnet/minecraft/src/NBTTagList;")
    public void skipDebugLines1(PatchContext ctx) {
        removePrintlns(ctx);
    }

    @Patch.Method.AffectsControlFlow
    @Patch.Method("readContents(Lnet/minecraft/src/NBTTagList;)V")
    public void skipDebugLines2(PatchContext ctx) {
        removePrintlns(ctx);
    }

    @Patch.Method.AffectsControlFlow
    @Patch.Method("a(Lnet/minecraft/src/World;IIILnet/minecraft/src/EntityLiving;)V")
    public void skipDebugLines3(PatchContext ctx) {
        removePrintlns(ctx);
    }

}
