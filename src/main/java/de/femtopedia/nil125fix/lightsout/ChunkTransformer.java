package de.femtopedia.nil125fix.lightsout;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import net.minecraft.src.Chunk;
import net.minecraft.src.EnumSkyBlock;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.Chunk")
public class ChunkTransformer extends MiniPlusTransformer {

    @Patch.Method.AffectsControlFlow
    @Patch.Method("getSavedLightValue(Lnet/minecraft/src/EnumSkyBlock;III)I")
    public void skipPlayerCheckLightSection(PatchContext ctx) {
        ctx.search(
                ALOAD(1),
                GETFIELD("net/minecraft/src/EnumSkyBlock", "defaultLightValue", "I")
        ).jumpBefore();

        ctx.add(
                ALOAD(0),
                ALOAD(1),
                ILOAD(2),
                ILOAD(3),
                ILOAD(4),
                INVOKESTATIC(hooks(), "getFixedSavedLightValue",
                        "(Lnet/minecraft/src/Chunk;Lnet/minecraft/src/EnumSkyBlock;III)I"),
                IRETURN()
        );
    }

    public static class Hooks {

        public static int getFixedSavedLightValue(Chunk thiz, EnumSkyBlock enumSkyBlock, int x, int y, int z) {
            return thiz.canBlockSeeTheSky(x, y, z) ? enumSkyBlock.defaultLightValue : 0;
        }

    }

}
