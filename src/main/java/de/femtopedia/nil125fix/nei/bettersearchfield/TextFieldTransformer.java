package de.femtopedia.nil125fix.nei.bettersearchfield;

import codechicken.nei.LayoutManager;
import codechicken.nei.NEIController;
import codechicken.nei.TextField;
import de.femtopedia.nil125fix.MiniPlusTransformer;
import java.lang.reflect.Field;
import net.minecraft.src.GuiTextField;
import nilloader.api.lib.asm.Opcodes;
import nilloader.api.lib.asm.Type;
import nilloader.api.lib.asm.tree.ClassNode;
import nilloader.api.lib.asm.tree.FieldNode;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.asm.tree.MethodNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.PatchContext.SearchResult;
import nilloader.api.lib.mini.annotation.Patch;
import org.lwjgl.input.Keyboard;

@Patch.Class("codechicken.nei.TextField")
public class TextFieldTransformer extends MiniPlusTransformer {

    private static boolean HAS_FOCUS_METHODS = true;

    @Override
    protected boolean modifyClassStructure(ClassNode clazz) {
        clazz.fields.add(new FieldNode(Opcodes.ASM5, Opcodes.ACC_PROTECTED,
                "field", remapFieldDesc("Lnet/minecraft/src/GuiTextField;"),
                null, null));
        clazz.fields.add(new FieldNode(Opcodes.ASM5, Opcodes.ACC_PRIVATE,
                "previousKeyboardRepeatEnabled", Type.BOOLEAN_TYPE.getDescriptor(),
                null, 0));

        if (clazz.methods.stream().noneMatch(m -> m.name.equals("setFocus"))) {
            // NEI 1.2.x does not have this method at all
            MethodNode setFocus = new MethodNode(Opcodes.ASM5, Opcodes.ACC_PUBLIC,
                    "setFocus", "(Z)V", null, null);
            setFocus.instructions = toInsnList(
                    ALOAD(0),
                    ILOAD(1),
                    PUTFIELD("codechicken/nei/TextField", "focused", "Z"),
                    RETURN()
            );
            clazz.methods.add(setFocus);

            HAS_FOCUS_METHODS = false;
        }

        if (clazz.methods.stream().noneMatch(m -> m.name.equals("focused"))) {
            // NEI 1.2.x does not have this method at all
            MethodNode focused = new MethodNode(Opcodes.ASM5, Opcodes.ACC_PUBLIC,
                    "focused", "()Z", null, null);
            focused.instructions = toInsnList(
                    ALOAD(0),
                    GETFIELD("codechicken/nei/TextField", "focused", "Z"),
                    IRETURN()
            );
            clazz.methods.add(focused);

            HAS_FOCUS_METHODS = false;
        }

        return true; // don't know if absolutely necessary, let's just do it...
    }

    @Patch.Method("<init>(Ljava/lang/String;)V")
    public void initInternalTextField(PatchContext ctx) {
        ctx.jumpToLastReturn();

        ctx.add(
                ALOAD(0),
                INVOKESTATIC(hooks(), "initInternalTextField",
                        "()Lnet/minecraft/src/GuiTextField;"),
                PUTFIELD("codechicken/nei/TextField", "field", "Lnet/minecraft/src/GuiTextField;")
        );
    }

    @Patch.Method.AffectsControlFlow
    @Patch.Method("draw(Lcodechicken/nei/GuiManager;II)V")
    public void drawNewField(PatchContext ctx) {
        ctx.jumpToStart();

        ctx.add(
                ALOAD(0),
                ALOAD(0),
                GETFIELD("codechicken/nei/TextField", "field", "Lnet/minecraft/src/GuiTextField;"),
                INVOKESTATIC(hooks(), "drawNewField",
                        "(Lcodechicken/nei/TextField;Lnet/minecraft/src/GuiTextField;)V"),
                RETURN()
        );
    }

    @Patch.Method.AffectsControlFlow
    @Patch.Method("onGuiClick(II)V")
    public void propagateFocusManagement(PatchContext ctx) {
        if (HAS_FOCUS_METHODS) return;

        SearchResult res = ctx.search(
                ALOAD(0),
                ICONST_0(),
                PUTFIELD("codechicken/nei/TextField", "focused", "Z")
        );
        if (!res.isSuccessful()) return; // Most likely (hopefully) on NEI 1.3

        LabelNode Lskip = new LabelNode();

        res.jumpBefore();
        ctx.add(
                ALOAD(0),
                ICONST_0(),
                INVOKEVIRTUAL("codechicken/nei/TextField", "setFocus", "(Z)V"),
                GOTO(Lskip)
        );

        res.jumpAfter();
        ctx.add(
                Lskip
        );
    }

    @Patch.Method.AffectsControlFlow
    @Patch.Method("handleClick(III)Z")
    public void propagateClicksToNewField(PatchContext ctx) {
        ctx.jumpToStart();

        ctx.add(
                ALOAD(0),
                ALOAD(0),
                GETFIELD("codechicken/nei/TextField", "field", "Lnet/minecraft/src/GuiTextField;"),
                ILOAD(1),
                ILOAD(2),
                ILOAD(3),
                INVOKESTATIC(hooks(), "propagateClicks",
                        "(Lcodechicken/nei/TextField;Lnet/minecraft/src/GuiTextField;III)V"),
                ICONST_1(),
                IRETURN()
        );
    }

    @Patch.Method.AffectsControlFlow
    @Patch.Method("handleKeyPress(IC)Z")
    public void propagateKeyPressesToNewField(PatchContext ctx) {
        ctx.jumpToStart();

        ctx.add(
                ALOAD(0),
                ALOAD(0),
                GETFIELD("codechicken/nei/TextField", "field", "Lnet/minecraft/src/GuiTextField;"),
                ILOAD(1),
                ILOAD(2),
                INVOKESTATIC(hooks(), "propagateKeyPress",
                        "(Lcodechicken/nei/TextField;Lnet/minecraft/src/GuiTextField;IC)Z"),
                IRETURN()
        );
    }

    @Patch.Method.AffectsControlFlow
    @Patch.Method("update(Lcodechicken/nei/GuiManager;)V")
    public void shortCutUpdate(PatchContext ctx) {
        ctx.jumpToStart();

        ctx.add(
                RETURN()
        );
    }

    @Patch.Method.AffectsControlFlow
    @Patch.Method("setText(Ljava/lang/String;)V")
    public void propagateSetText(PatchContext ctx) {
        ctx.search(
                PUTFIELD("codechicken/nei/TextField", "text", "Ljava/lang/String;")
        ).jumpAfter();

        ctx.add(
                ALOAD(0),
                ALOAD(0),
                GETFIELD("codechicken/nei/TextField", "field", "Lnet/minecraft/src/GuiTextField;"),
                ALOAD(1),
                INVOKESTATIC(hooks(), "propagateSetText",
                        "(Lcodechicken/nei/TextField;Lnet/minecraft/src/GuiTextField;Ljava/lang/String;)V")
        );
    }

    @Patch.Method("focused()Z")
    @Patch.Method.AffectsControlFlow
    public void propagateFocusedToNewField(PatchContext ctx) {
        ctx.jumpToStart();

        ctx.add(
                ALOAD(0),
                GETFIELD("codechicken/nei/TextField", "field", "Lnet/minecraft/src/GuiTextField;"),
                INVOKEVIRTUAL("net/minecraft/src/GuiTextField", "func_50025_j", "()Z"),
                IRETURN()
        );
    }

    @Patch.Method.AffectsControlFlow
    @Patch.Method("setFocus(Z)V")
    public void propagateSetFocusToNewField(PatchContext ctx) {
        ctx.jumpToStart();

        ctx.add(
                ALOAD(0),
                ALOAD(0),
                ALOAD(0),
                GETFIELD("codechicken/nei/TextField", "field", "Lnet/minecraft/src/GuiTextField;"),
                ALOAD(0),
                GETFIELD("codechicken/nei/TextField", "previousKeyboardRepeatEnabled", "Z"),
                ILOAD(1),
                INVOKESTATIC(hooks(), "propagateSetFocus",
                        "(Lcodechicken/nei/TextField;Lnet/minecraft/src/GuiTextField;ZZ)Z"),
                PUTFIELD("codechicken/nei/TextField", "previousKeyboardRepeatEnabled", "Z")
        );
    }

    public static class Hooks {

        private static Field xPos;
        private static Field yPos;
        private static Field width;
        private static Field height;

        public static GuiTextField initInternalTextField() {
            GuiTextField field = new GuiTextField(getMinecraftInstance().fontRenderer, 0, 0, 0, 0);
            field.setMaxStringLength(256);
            field.field_50042_o = 0;
            field.func_50032_g(0);
            return field;
        }

        public static void setDimensionsAndColor(TextField thiz, GuiTextField field) {
            try {
                if (xPos == null) {
                    xPos = GuiTextField.class.getDeclaredField("b");
                    xPos.setAccessible(true);
                }
                if (yPos == null) {
                    yPos = GuiTextField.class.getDeclaredField("c");
                    yPos.setAccessible(true);
                }
                if (width == null) {
                    width = GuiTextField.class.getDeclaredField("d");
                    width.setAccessible(true);
                }
                if (height == null) {
                    height = GuiTextField.class.getDeclaredField("e");
                    height.setAccessible(true);
                }

                xPos.set(field, thiz.x + 2);
                yPos.set(field, thiz.y + 2);
                width.set(field, thiz.width - 4);
                height.set(field, thiz.height - 4);
                field.field_50047_q = thiz.getTextColour();
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }

        public static void drawNewField(TextField thiz, GuiTextField field) {
            setDimensionsAndColor(thiz, field);
            field.drawTextBox();
        }

        public static void propagateClicks(TextField thiz, GuiTextField field, int mousex, int mousey, int button) {
            thiz.setFocus(true);

            if (button == 1) thiz.setText("");
            else field.mouseClicked(mousex, mousey, button);
        }

        public static boolean propagateKeyPress(TextField thiz, GuiTextField field, int keyID, char keyChar) {
            if (!thiz.focused()) return false;

            boolean handled = field.func_50037_a(keyChar, keyID);
            if (!handled) {
                if (keyID == Keyboard.KEY_RETURN || keyID == Keyboard.KEY_NUMPADENTER || keyID == Keyboard.KEY_ESCAPE) {
                    thiz.setFocus(false);
                    handled = true;
                }
            }

            if (handled) {
                thiz.text = field.getText(); // changed: other classes may expect this...
                NEIController.onTextChange(thiz.text, thiz.identifier);
            }

            return handled;
        }

        public static void propagateSetText(TextField thiz, GuiTextField field, String text) {
            field.setText(text);
            field.field_50042_o = 0;
            field.func_50032_g(0);
        }

        public static boolean propagateSetFocus(TextField thiz, GuiTextField field,
                                                boolean previousKeyboardRepeatEnabled, boolean focus) {
            if (HAS_FOCUS_METHODS) {
                if (focus) {
                    LayoutManager.setInputFocused(thiz);
                } else if (LayoutManager.getInputFocused() == thiz) {
                    LayoutManager.setInputFocused(null);
                }
            }

            final boolean previousFocus = field.func_50025_j();
            field.func_50033_b(focus);

            if (previousFocus != focus) {
                if (focus) {
                    previousKeyboardRepeatEnabled = Keyboard.areRepeatEventsEnabled();
                    Keyboard.enableRepeatEvents(true);
                    if (HAS_FOCUS_METHODS) thiz.gainFocus();
                } else {
                    Keyboard.enableRepeatEvents(previousKeyboardRepeatEnabled);
                    if (HAS_FOCUS_METHODS) thiz.loseFocus();
                }
            }

            return previousKeyboardRepeatEnabled;
        }

    }

}
