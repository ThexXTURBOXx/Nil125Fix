package de.femtopedia.nil125fix;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import nilloader.api.ClassRetransformer;
import nilloader.api.lib.asm.tree.AbstractInsnNode;
import nilloader.api.lib.asm.tree.InsnList;
import nilloader.api.lib.mini.MiniTransformer;

public abstract class MiniPlusTransformer extends MiniTransformer implements ClassRetransformer {

    protected final String hooks() {
        return getClass().getName().replace('.', '/') + "$Hooks";
    }

    protected final InsnList toInsnList(AbstractInsnNode... insns) {
        InsnList li = new InsnList();
        for (AbstractInsnNode ain : insns) li.add(ain);
        return li;
    }

    public static Minecraft getMinecraftInstance() {
        return FMLClientHandler.instance().getClient();
    }

}
