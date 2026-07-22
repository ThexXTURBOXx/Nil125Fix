package de.femtopedia.nil125fix.vanilla.spchatcompletecrashfix;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiPlayerInfo;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.GuiChat")
public class GuiChatTransformer extends MiniPlusTransformer {

    @Patch.Method.AffectsControlFlow
    @Patch.Method("completePlayerName()V")
    public void shortCircuitInSP(PatchContext ctx) {
        LabelNode LuseMpList = new LabelNode();
        LabelNode Lcontinue = new LabelNode();

        ctx.search(
                CHECKCAST("net/minecraft/src/EntityClientPlayerMP")
        ).jumpBefore();
        ctx.add(
                INSTANCEOF("net/minecraft/src/EntityClientPlayerMP"),
                IFNE(LuseMpList),
                INVOKESTATIC(hooks(), "getSPNames", "()Ljava/util/List;"),
                GOTO(Lcontinue),
                LuseMpList,
                ALOAD(0),
                GETFIELD("net/minecraft/src/GuiChat", "mc", "Lnet/minecraft/client/Minecraft;"),
                GETFIELD("net/minecraft/client/Minecraft", "thePlayer", "Lnet/minecraft/src/EntityPlayerSP;")
        );


        ctx.search(
                INVOKEINTERFACE("java/util/List", "iterator", "()Ljava/util/Iterator;")
        ).jumpBefore();
        ctx.add(
                Lcontinue
        );
    }

    public static class Hooks {

        public static List<GuiPlayerInfo> getSPNames() {
            Minecraft mc = getMinecraftInstance();
            if (mc == null || mc.thePlayer == null)
                return Collections.emptyList();
            return Collections.singletonList(new GuiPlayerInfo(mc.thePlayer.username));
        }

    }

}
