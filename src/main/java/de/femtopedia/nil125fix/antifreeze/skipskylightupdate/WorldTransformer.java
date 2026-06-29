package de.femtopedia.nil125fix.antifreeze.skipskylightupdate;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.World")
public class WorldTransformer extends MiniPlusTransformer {

    @Patch.Method.AffectsControlFlow
    @Patch.Method("func_48458_a(IILnet/minecraft/src/Chunk;)V")
    public void skipTickChunkSection(PatchContext ctx) {
        LabelNode Lskip = new LabelNode();

        ctx.search(
                LDC("tickChunk"),
                INVOKESTATIC("net.minecraft.src.Profiler", "endStartSection", "(Ljava/lang/String;)V")
        ).jumpAfter();
        ctx.add(
                GOTO(Lskip)
        );

        ctx.search(
                LDC("moodSound"),
                INVOKESTATIC("net.minecraft.src.Profiler", "endStartSection", "(Ljava/lang/String;)V")
        ).jumpBefore();
        ctx.add(
                Lskip
        );
    }

}
