package de.femtopedia.nil125fix.nei.fixusageprune;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("codechicken.nei.FurnaceRecipeHandler$SmeltingPair")
public class FRHSPTransformer extends MiniPlusTransformer {

    @Patch.Method("<init>(Lcodechicken/nei/FurnaceRecipeHandler;Lnet/minecraft/src/ItemStack;"
                  + "Lnet/minecraft/src/ItemStack;)V")
    public void initFRHSP(PatchContext ctx) {
        ctx.search(
                ALOAD(2)
        ).jumpAfter();

        ctx.add(
                INVOKEVIRTUAL("net/minecraft/src/ItemStack", "copy", "()Lnet/minecraft/src/ItemStack;"),
                ASTORE(1),
                ALOAD(1)
        );
    }

}
