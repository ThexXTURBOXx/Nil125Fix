package de.femtopedia.nil125fix.lightsout;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.AnvilChunkLoader")
public class AnvilChunkLoaderTransformer extends MiniPlusTransformer {

    @Patch.Method("func_48445_a(Lnet/minecraft/src/Chunk;Lnet/minecraft/src/World;Lnet/minecraft/src/NBTTagCompound;)V")
    public void alwaysSaveSection(PatchContext ctx) {
        ctx.search(
                INVOKEVIRTUAL("net/minecraft/src/ExtendedBlockStorage", "func_48700_f", "()I")
        ).jumpAfter();

        ctx.add(
                POP(),
                ICONST_1() // Non-zero -> pretend section is non-empty
        );
    }

}
