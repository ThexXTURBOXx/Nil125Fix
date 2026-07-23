package de.femtopedia.nil125fix.keepmystats.createplaceholders;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import java.util.Map;
import net.minecraft.src.AchievementMap;
import net.minecraft.src.StatBase;
import net.minecraft.src.StatList;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.StatBase")
public class StatBaseTransformer extends MiniPlusTransformer {

    @Patch.Method.AffectsControlFlow
    @Patch.Method("registerStat()Lnet/minecraft/src/StatBase;")
    public void createStatPlaceholders(PatchContext ctx) {
        LabelNode Lcontinue = new LabelNode();

        ctx.jumpToStart();

        ctx.add(
                ALOAD(0),
                INVOKESTATIC(hooks(), "tryOverwritePlaceholder", "(Lnet/minecraft/src/StatBase;)Z"),
                IFEQ(Lcontinue),
                ALOAD(0),
                ARETURN(),
                Lcontinue
        );
    }

    public static class Hooks {

        @SuppressWarnings("unchecked")
        public static boolean tryOverwritePlaceholder(StatBase stat) {
            if (StatList.oneShotStats.containsKey(stat.statId)) {
                // If a placeholder tries overwriting some value, short-circuit instantly
                if (stat instanceof StatFileWriterTransformer.StatPlaceholder) return true;

                StatBase placeholder = (StatBase) StatList.oneShotStats.get(stat.statId);
                // Should never happen...
                if (placeholder == null) return false;
                // A "real" conflict happens. Some mod tries to overwrite a non-placeholder stat
                if (!(placeholder instanceof StatFileWriterTransformer.StatPlaceholder)) return false;

                int idx = StatList.allStats.indexOf(placeholder);
                // Something is registered very, very wrongly... Let Vanilla crash
                if (idx < 0) return false;

                // Everything has worked and we can apply our "hack"
                replaceInMap(getMinecraftInstance().statFileWriter.field_25101_b, placeholder, stat);
                replaceInMap(getMinecraftInstance().statFileWriter.field_25102_a, placeholder, stat);
                StatList.allStats.set(idx, stat);
                StatList.oneShotStats.put(stat.statId, stat);
                stat.statGuid = AchievementMap.getGuid(stat.statId);
                return true;
            }
            return false;
        }

        private static void replaceInMap(Map<StatBase, Integer> map, StatBase placeholder, StatBase stat) {
            // Remove placeholder entry and migrate to "real" one
            Integer old = map.remove(placeholder);
            if (old != null) map.put(stat, old);
        }

    }

}
