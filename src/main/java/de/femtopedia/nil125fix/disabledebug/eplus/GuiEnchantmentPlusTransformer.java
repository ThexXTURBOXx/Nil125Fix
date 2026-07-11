package de.femtopedia.nil125fix.disabledebug.eplus;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("eplus.GuiEnchantmentPlus")
public class GuiEnchantmentPlusTransformer extends MiniPlusTransformer {

    @Patch.Method.AffectsControlFlow
    @Patch.Method("a(FII)V")
    public void skipFaultyDebugThing(PatchContext ctx) {
        LabelNode Lskip = new LabelNode();

        ctx.search(
                LDC("Entity")
        ).jumpBefore();
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
