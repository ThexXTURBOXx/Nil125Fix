package de.femtopedia.nil125fix.ee2.fixbc3addon;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import net.minecraft.src.Item;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("ee.EEAddonBC")
public class EEAddonBCTransformer extends MiniPlusTransformer {

    @Patch.Method("initialize()V")
    public void fixAddonForBC3(PatchContext ctx) {
        LabelNode Lskip = new LabelNode();

        ctx.jumpToStart();
        ctx.add(
                INVOKESTATIC(hooks(), "tryToLoadBC3Item", "()Lnet/minecraft/src/Item;"),
                DUP(),
                IFNONNULL(Lskip),
                POP()
        );

        ctx.search(
                PUTSTATIC("ee/EEAddonBC", "bcItemBucketOil", "Lnet/minecraft/src/Item;")
        ).jumpBefore();
        ctx.add(
                Lskip
        );
    }

    public static class Hooks {

        public static Item tryToLoadBC3Item() {
            try {
                return (Item) Class.forName("buildcraft.BuildCraftEnergy").getField("bucketOil").get(null);
            } catch (Throwable ignored) {
            }
            return null;
        }

    }

}
