package de.femtopedia.nil125fix.keepmystats.createplaceholders;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import net.minecraft.src.StatBase;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.StatFileWriter")
public class StatFileWriterTransformer extends MiniPlusTransformer {

    @Patch.Method.AffectsControlFlow
    @Patch.Method("func_27177_a(Ljava/lang/String;)Ljava/util/Map;")
    public void createStatPlaceholders(PatchContext ctx) {
        LabelNode Lcontinue = new LabelNode();

        ctx.search(
                LDC(" is not a valid stat")
        ).jumpAfter();
        ctx.add(
                POP(),
                LDC(" is not a valid stat, creating place-holder")
        );

        ctx.search(
                INVOKEVIRTUAL("java/io/PrintStream", "println", "(Ljava/lang/String;)V")
        ).jumpAfter();
        ctx.add(
                ILOAD(10),
                INVOKESTATIC(hooks(), "createPlaceholder", "(I)Lnet/minecraft/src/StatBase;"),
                ASTORE(12),
                GOTO(Lcontinue)
        );

        ctx.search(
                GOTO(null)
        ).jumpAfter();
        ctx.add(
                Lcontinue
        );
    }

    public static class Hooks {

        public static StatBase createPlaceholder(int id) {
            return new StatPlaceholder(id).registerStat();
        }

    }

    public static class StatPlaceholder extends StatBase {

        public StatPlaceholder(int id) {
            super(id, "Unknown stat");
        }

    }

}
