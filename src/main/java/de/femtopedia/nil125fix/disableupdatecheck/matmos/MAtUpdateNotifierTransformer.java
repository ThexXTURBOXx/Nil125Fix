package de.femtopedia.nil125fix.disableupdatecheck.matmos;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("MAtUpdateNotifier")
public class MAtUpdateNotifierTransformer extends MiniPlusTransformer {

    @Patch.Method.AffectsControlFlow
    @Patch.Method("run()V")
    public void shortCircuit(PatchContext ctx) {
        ctx.jumpToStart();
        ctx.add(RETURN());
    }

}
