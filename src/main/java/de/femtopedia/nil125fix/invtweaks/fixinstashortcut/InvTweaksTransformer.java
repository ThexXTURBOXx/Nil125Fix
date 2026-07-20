package de.femtopedia.nil125fix.invtweaks.fixinstashortcut;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import net.minecraft.src.GuiScreen;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;
import org.lwjgl.input.Mouse;

@Patch.Class("InvTweaks")
public class InvTweaksTransformer extends MiniPlusTransformer {

    @Patch.Method.AffectsControlFlow
    @Patch.Method("handleShortcuts(Lnet/minecraft/src/GuiScreen;)V")
    public void fixInstantShortcutTrigger(PatchContext ctx) {
        ctx.jumpToStart();

        LabelNode LdontReturn = new LabelNode();

        ctx.add(
                ALOAD(1),
                INVOKESTATIC(hooks(), "shouldShortCircuit", "(Lnet/minecraft/src/GuiScreen;)Z"),
                IFEQ(LdontReturn),
                RETURN(),
                LdontReturn
        );
    }

    public static class Hooks {

        private static GuiScreen lastScreen;

        public static boolean shouldShortCircuit(GuiScreen guiScreen) {
            if (lastScreen != guiScreen) {
                if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
                    return true;
                } else {
                    lastScreen = guiScreen;
                }
            }
            return false;
        }

    }

}
