package de.femtopedia.nil125fix.nei.bettersearchfield;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import java.lang.reflect.Modifier;
import nilloader.api.lib.asm.tree.ClassNode;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.GuiTextField")
public class WidgetTextFieldTransformer extends MiniPlusTransformer {

    @Override
    protected boolean modifyClassStructure(ClassNode clazz) {
        // Make fields public and non-final
        clazz.fields.forEach(field -> {
            field.access &= ~Modifier.PRIVATE & ~Modifier.PROTECTED & ~Modifier.FINAL;
            field.access |= Modifier.PUBLIC;
        });
        return true; // don't know if absolutely necessary, let's just do it...
    }

}
