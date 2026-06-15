package de.femtopedia.nil125fix.ml.warnentityidconflicts;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import java.util.Map;
import java.util.Optional;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.EntityList")
public class EntityListTransformer extends MiniPlusTransformer {

    @Patch.Method("addMapping(Ljava/lang/Class;Ljava/lang/String;I)V")
    public void warnIfAlreadyInMap(PatchContext ctx) {
        ctx.jumpToStart();

        ctx.add(
                ALOAD(0),
                ALOAD(1),
                ILOAD(2),
                GETSTATIC("net/minecraft/src/EntityList", "stringToClassMapping", "Ljava/util/Map;"),
                GETSTATIC("net/minecraft/src/EntityList", "IDtoClassMapping", "Ljava/util/Map;"),
                GETSTATIC("net/minecraft/src/EntityList", "stringToIDMapping", "Ljava/util/Map;"),
                INVOKESTATIC(hooks(), "checkDuplicateMapping",
                        "(Ljava/lang/Class;Ljava/lang/String;ILjava/util/Map;Ljava/util/Map;Ljava/util/Map;)V")
        );
    }

    public static class Hooks {

        public static void checkDuplicateMapping(Class<?> newClass, String strId, int id,
                                                 Map<String, Class<?>> stringToClass,
                                                 Map<Integer, Class<?>> idToClass,
                                                 Map<String, Integer> stringToIdMapping) {
            if (stringToClass != null && stringToClass.containsKey(strId)) {
                String debug = "";
                if (stringToIdMapping != null) {
                    Integer debugId = stringToIdMapping.get(strId);
                    if (debugId != null) {
                        debug = " (numerical ID: " + debugId + ")";
                    }
                }

                System.err.println("[WARNING] Duplicate Entity ID registration detected: \"" + strId +
                                   "\" is already mapped to " + stringToClass.get(strId).getName() + debug +
                                   " while trying to register " + newClass.getName() + "!");
            }

            if (idToClass != null && idToClass.containsKey(id)) {
                String debug = "";
                if (stringToIdMapping != null) {
                    Optional<String> debugId = stringToIdMapping.entrySet().stream()
                            .filter(e -> e.getValue() == id)
                            .map(Map.Entry::getKey)
                            .findFirst();
                    if (debugId.isPresent()) {
                        debug = " (string ID: \"" + debugId.get() + "\")";
                    }
                }

                System.err.println("[WARNING] Duplicate Entity ID registration detected: " + id +
                                   " is already mapped to " + idToClass.get(id).getName() + debug +
                                   " while trying to register " + newClass.getName() + "!");
            }
        }

    }

}
