package de.femtopedia.nil125fix.reiminimap.fixcmexception;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("reifnsk.minimap.ReiMinimap")
public class ReiMinimapTransformer extends MiniPlusTransformer {

    @Patch.Method("onTickInGame(FLnet/minecraft/client/Minecraft;)V")
    public void copyEntityListBeforeUse(PatchContext ctx) {
        PatchContext.SearchResult res = ctx.search(
                ALOAD(0),
                GETFIELD("reifnsk/minimap/ReiMinimap", "theWorld", "Lnet/minecraft/src/World;"),
                GETFIELD("net/minecraft/src/World", "loadedEntityList", "Ljava/util/List;")
        );

        res.jumpBefore();
        ctx.add(
                NEW("java/util/ArrayList"),
                DUP()
        );

        res.jumpAfter();
        ctx.add(
                INVOKESPECIAL("java/util/ArrayList", "<init>", "(Ljava/util/Collection;)V")
        );
    }

}
