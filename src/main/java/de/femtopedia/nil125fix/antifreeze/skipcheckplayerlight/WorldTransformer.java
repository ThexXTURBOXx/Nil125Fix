package de.femtopedia.nil125fix.antifreeze.skipcheckplayerlight;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.World")
public class WorldTransformer extends MiniPlusTransformer {

    @Patch.Method.AffectsControlFlow
    @Patch.Method("func_48461_r()V")
    public void skipPlayerCheckLightSection(PatchContext ctx) {
        LabelNode Lskip = new LabelNode();

        ctx.search(
                LDC("playerCheckLight"),
                INVOKESTATIC("net.minecraft.src.Profiler", "startSection", "(Ljava/lang/String;)V")
        ).jumpAfter();
        ctx.add(
                GOTO(Lskip)
        );

        ctx.search(
                INVOKESTATIC("net.minecraft.src.Profiler", "endSection", "()V")
        ).jumpBefore();
        ctx.add(
                Lskip
        );
    }

}
