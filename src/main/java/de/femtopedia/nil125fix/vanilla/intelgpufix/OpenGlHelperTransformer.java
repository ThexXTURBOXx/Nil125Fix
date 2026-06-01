package de.femtopedia.nil125fix.vanilla.intelgpufix;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.OpenGlHelper")
public class OpenGlHelperTransformer extends MiniPlusTransformer {

    @Patch.Method("setActiveTexture(I)V")
    public void initFRHSP(PatchContext ctx) {
        ctx.search(
                ILOAD(0)
        ).jumpAfter();
        ctx.add(
                INVOKESTATIC("org/lwjgl/opengl/ARBMultitexture", "glClientActiveTextureARB", "(I)V"),
                ILOAD(0)
        );

        ctx.search(
                ILOAD(0)
        ).jumpAfter();
        ctx.add(
                INVOKESTATIC("org/lwjgl/opengl/GL13", "glClientActiveTexture", "(I)V"),
                ILOAD(0)
        );
    }

}
