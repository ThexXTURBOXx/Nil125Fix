package de.femtopedia.nil125fix.disabledebug.neitransfer;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("codechicken.nei.FastTransferManger")
public class FastTransferMangerTransformer extends MiniPlusTransformer {

    @Patch.Method.AffectsControlFlow
    @Patch.Method("transferItem(Lnet/minecraft/src/GuiContainer;I)V")
    public void skipDebugLines(PatchContext ctx) {
        LabelNode Lskip = new LabelNode();

        // Skip "Starting Transfer" as well as initialization of timer
        ctx.jumpToStart();
        ctx.add(
                GOTO(Lskip)
        );

        ctx.search(
                ALOAD(0)
        ).jumpBefore();
        ctx.add(
                Lskip
        );

        // Skip "Transfer Complete" debugs
        while (true) {
            PatchContext.SearchResult res = ctx.search(
                    GETSTATIC("java/lang/System", "out", "Ljava/io/PrintStream;")
            );
            if (!res.isSuccessful()) break;

            Lskip = new LabelNode();

            res.jumpBefore();
            ctx.add(
                    GOTO(Lskip)
            );

            ctx.search(
                    INVOKEVIRTUAL("java/io/PrintStream", "println", "(Ljava/lang/String;)V")
            ).jumpAfter();
            ctx.add(
                    Lskip
            );
        }
    }

}
