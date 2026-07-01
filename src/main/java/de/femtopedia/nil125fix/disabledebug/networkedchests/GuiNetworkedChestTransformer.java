package de.femtopedia.nil125fix.disabledebug.networkedchests;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("GuiNetworkedChest")
public class GuiNetworkedChestTransformer extends MiniPlusTransformer {

    @Patch.Method.AffectsControlFlow
    @Patch.Method("e()V")
    public void skipDebugLines(PatchContext ctx) {
        removePrintlns(ctx);
    }

}
