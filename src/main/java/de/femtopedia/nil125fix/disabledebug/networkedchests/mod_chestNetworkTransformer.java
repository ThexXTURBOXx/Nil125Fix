package de.femtopedia.nil125fix.disabledebug.networkedchests;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("mod_chestNetwork")
public class mod_chestNetworkTransformer extends MiniPlusTransformer {

    @Patch.Method.AffectsControlFlow
    @Patch.Method("onTickInGame(FLnet/minecraft/client/Minecraft;)Z")
    public void skipDebugLines1(PatchContext ctx) {
        removePrintlns(ctx);
    }

    @Patch.Method.AffectsControlFlow
    @Patch.Method("load()V")
    public void skipDebugLines2(PatchContext ctx) {
        removePrintlns(ctx);
    }

}
