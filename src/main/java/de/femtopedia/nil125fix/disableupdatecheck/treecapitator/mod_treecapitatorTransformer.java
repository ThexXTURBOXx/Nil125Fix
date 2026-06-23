package de.femtopedia.nil125fix.disableupdatecheck.treecapitator;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("mod_treecapitator")
public class mod_treecapitatorTransformer extends MiniPlusTransformer {

    @Patch.Method.Optional // Does not exist in (some?) non-MLMP version(s)
    @Patch.Method.AffectsControlFlow
    @Patch.Method("isCurrentVersion(Lnet/minecraft/client/Minecraft;)Ljava/lang/Boolean;")
    public void shortCircuit(PatchContext ctx) {
        ctx.jumpToStart();

        ctx.add(
                ICONST_1(),
                INVOKESTATIC("java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;"),
                ARETURN()
        );
    }

    @Patch.Method("load()V")
    public void fixUpdateNotificationLogic(PatchContext ctx) {
        PatchContext.SearchResult res = ctx.search(
                ALOAD(0),
                INVOKESTATIC("ModLoader", "getMinecraftInstance", "()Lnet/minecraft/client/Minecraft;"),
                INVOKESPECIAL("mod_treecapitator", "isCurrentVersion",
                        "(Lnet/minecraft/client/Minecraft;)Ljava/lang/Boolean;"),
                INVOKEVIRTUAL("java/lang/Boolean", "booleanValue", "()Z")
        );
        if (!res.isSuccessful()) return;// Does not exist in (some?) non-MLMP version(s)
        res.jumpAfter();

        // This is a stupid way of adding a NOT
        // I want to do it like this here since I do not need to add another IFNE (it's just easier)
        ctx.add(
                ICONST_1(),
                IXOR()
        );
    }

}
