package de.femtopedia.nil125fix.betterdungeons.warniffolderismissing;


import de.femtopedia.nil125fix.MiniPlusTransformer;
import de.femtopedia.nil125fix.Nil125FixPremain;
import java.io.File;
import net.minecraft.client.Minecraft;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("mod_betterDungeons")
public class mod_betterDungeonsTransformer extends MiniPlusTransformer {

    @Patch.Method("loadConfig()V")
    public void warnAboutMissingChocolateFolderUponStart(PatchContext ctx) {
        ctx.jumpToStart();

        ctx.add(
                INVOKESTATIC(hooks(), "warnAboutMissingChocolateFolder", "()V")
        );
    }

    @Patch.Method("generateSurface(Lnet/minecraft/src/World;Ljava/util/Random;II)V")
    public void warnAboutMissingChocolateFolderPeriodically(PatchContext ctx) {
        ctx.jumpToStart();

        ctx.add(
                INVOKESTATIC(hooks(), "warnAboutMissingChocolateFolder", "()V")
        );
    }

    public static class Hooks {

        private static int counter = 0;

        public static void warnAboutMissingChocolateFolder() {
            if (counter++ % 20 != 0) return;

            File configDir = new File(Minecraft.getMinecraftDir(), "Chocolate");
            File buildingDir = new File(Minecraft.getMinecraftDir(), "Building");
            File langDir = new File(Minecraft.getMinecraftDir(), "Lang");
            if (!configDir.exists() || !configDir.isDirectory() ||
                !buildingDir.exists() || !buildingDir.isDirectory() ||
                !langDir.exists() || !langDir.isDirectory()) {
                Nil125FixPremain.log.error("You have installed Better Dungeons, but have not");
                Nil125FixPremain.log.error("extracted the Chocolate folder correctly!");
                Nil125FixPremain.log.error("You will experience various bugs and crashes!");
                Nil125FixPremain.log.error("Please install the mod correctly, i.e., extract");
                Nil125FixPremain.log.error("the \"Chocolate\" folder from the mod zip into");
                Nil125FixPremain.log.error("your Minecraft root directory, which should be:");
                Nil125FixPremain.log.error(Minecraft.getMinecraftDir().toString());
            }

            Minecraft mc = getMinecraftInstance();
            if (mc != null && mc.thePlayer != null) {
                mc.thePlayer.addChatMessage("[Nil125Fix] Check the console!");
            }
        }

    }

}
