package de.femtopedia.nil125fix.disableupdatecheck.forestry;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("forestry.core.config.Version")
public class VersionTransformer extends MiniPlusTransformer {

    @Patch.Method.AffectsControlFlow
    @Patch.Method("versionCheck()V")
    public void shortCircuit(PatchContext ctx) {
        ctx.jumpToStart();

        ctx.add(
                RETURN()
        );
    }

}
