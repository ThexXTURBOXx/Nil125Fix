package de.femtopedia.nil125fix.forge.fixuncraftableorerecipes;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import de.femtopedia.nil125fix.Nil125FixPremain;
import forge.oredict.ShapedOreRecipe;
import forge.oredict.ShapelessOreRecipe;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.src.IRecipe;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.CraftingManager")
public class CraftingManagerTransformer extends MiniPlusTransformer {

    @Patch.Method("getRecipeList()Ljava/util/List;")
    public void fixRecipeList(PatchContext ctx) {
        ctx.jumpToStart();

        ctx.add(
                ALOAD(0),
                GETFIELD("net/minecraft/src/CraftingManager", "recipes", "Ljava/util/List;"),
                INVOKESTATIC(hooks(), "tidyOreRecipes", "(Ljava/util/List;)V")
        );
    }

    public static class Hooks {

        private static boolean wasAlreadyInWorld = false;

        public static void tidyOreRecipes(List<IRecipe> recipes) {
            // We only tidy up this list when we first entered a world.
            // I don't want to let the game run this check everytime and forever as it may get quite heavy.
            if (wasAlreadyInWorld) return;
            Minecraft mc = getMinecraftInstance();
            if (mc != null && mc.theWorld != null) wasAlreadyInWorld = true;
            else return;

            int tidied = 0;
            IRecipe recipe;
            Iterator<IRecipe> iterator = recipes.iterator();
            while (iterator.hasNext()) {
                recipe = iterator.next();
                if (recipe instanceof ShapedOreRecipe) {
                    for (Object input : ((ShapedOreRecipe) recipe).input) {
                        if (input instanceof Collection && ((Collection<?>) input).isEmpty()) {
                            iterator.remove();
                            ++tidied;
                            break;
                        }
                    }
                } else if (recipe instanceof ShapelessOreRecipe) {
                    for (Object input : ((ShapelessOreRecipe) recipe).input) {
                        if (input instanceof Collection && ((Collection<?>) input).isEmpty()) {
                            iterator.remove();
                            ++tidied;
                            break;
                        }
                    }
                }
            }

            Nil125FixPremain.log.info("Removed " + tidied + " invalid ore recipes...");
        }

    }

}
