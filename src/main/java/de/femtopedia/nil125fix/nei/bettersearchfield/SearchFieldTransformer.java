package de.femtopedia.nil125fix.nei.bettersearchfield;

import codechicken.nei.GuiManager;
import codechicken.nei.NEIConfig;
import de.femtopedia.nil125fix.MiniPlusTransformer;
import net.minecraft.src.GuiTextField;
import nilloader.api.lib.asm.Opcodes;
import nilloader.api.lib.asm.tree.ClassNode;
import nilloader.api.lib.asm.tree.MethodNode;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("codechicken.nei.SearchField")
public class SearchFieldTransformer extends MiniPlusTransformer {

    @Override
    protected boolean modifyClassStructure(ClassNode clazz) {
        MethodNode draw = new MethodNode(Opcodes.ASM5, Opcodes.ACC_PUBLIC,
                "draw", "(Lcodechicken/nei/GuiManager;II)V",
                null, null);

        draw.instructions = toInsnList(
                ALOAD(0),
                ALOAD(1),
                ILOAD(2),
                ILOAD(3),
                INVOKESPECIAL("codechicken/nei/TextField", "draw",
                        "(Lcodechicken/nei/GuiManager;II)V"),
                ALOAD(0),
                GETFIELD("codechicken/nei/TextField", "field",
                        "Lnet/minecraft/src/GuiTextField;"),
                ALOAD(1),
                INVOKESTATIC(hooks(), "drawOverlay",
                        "(Lnet/minecraft/src/GuiTextField;Lcodechicken/nei/GuiManager;)V"),
                RETURN()
        );

        clazz.methods.add(draw);

        return true; // don't know if absolutely necessary, let's just do it...
    }

    public static class Hooks {

        public static void drawOverlay(GuiTextField field, GuiManager gui) {
            if (NEIConfig.getBooleanSetting("options.searchinventories")) {
                gui.drawGradientRect(
                        field.xPos - 1,
                        field.yPos - 1,
                        1,
                        field.height + 2,
                        0xFFFFFF00,
                        0xFFC0B000); // Left
                gui.drawGradientRect(
                        field.xPos - 1,
                        field.yPos - 1,
                        field.width + 2,
                        1,
                        0xFFFFFF00,
                        0xFFC0B000); // Top
                gui.drawGradientRect(
                        field.xPos + field.width,
                        field.yPos - 1,
                        1,
                        field.height + 2,
                        0xFFFFFF00,
                        0xFFC0B000); // Left
                gui.drawGradientRect(
                        field.xPos - 1,
                        field.yPos + field.height,
                        field.width + 2,
                        1,
                        0xFFFFFF00,
                        0xFFC0B000); // Bottom
            }
        }

    }

}
