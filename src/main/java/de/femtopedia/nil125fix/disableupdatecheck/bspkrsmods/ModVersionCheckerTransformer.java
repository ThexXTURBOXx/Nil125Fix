package de.femtopedia.nil125fix.disableupdatecheck.bspkrsmods;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("bspkrs.util.ModVersionChecker")
public class ModVersionCheckerTransformer extends MiniPlusTransformer {

    @Patch.Method.AffectsControlFlow
    @Patch.Method("loadTextFromURL(Ljava/net/URL;)[Ljava/lang/String;")
    public void shortCircuit(PatchContext ctx) {
        ctx.jumpToStart();

        ctx.add(
                ICONST_1(),
                ANEWARRAY("java/lang/String"),
                DUP(),
                ICONST_0(),
                LDC(""),
                AASTORE(),
                ARETURN()
        );
    }

    @Patch.Method.AffectsControlFlow
    @Patch.Method("isCurrentVersion()Z")
    public void iAmAlwaysUpToDateTrustMeBro(PatchContext ctx) {
        ctx.jumpToStart();

        ctx.add(
                ICONST_1(),
                IRETURN()
        );
    }

}
