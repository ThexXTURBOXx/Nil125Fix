package de.femtopedia.nil125fix.keepmystats.ignorechecksums;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.StatFileWriter")
public class StatFileWriterTransformer extends MiniPlusTransformer {

    @Patch.Method.AffectsControlFlow
    @Patch.Method("func_27177_a(Ljava/lang/String;)Ljava/util/Map;")
    public void ignoreChecksumMismatches(PatchContext ctx) {
        ctx.search(
                LDC("CHECKSUM MISMATCH"),
                INVOKEVIRTUAL("java/io/PrintStream", "println", "(Ljava/lang/String;)V")
        ).jumpAfter();

        ctx.add(
                GETSTATIC("java/lang/System", "out", "Ljava/io/PrintStream;"),
                LDC("Continuing anyway..."),
                INVOKEVIRTUAL("java/io/PrintStream", "println", "(Ljava/lang/String;)V"),
                ALOAD(1),
                ARETURN()
        );
    }

}
