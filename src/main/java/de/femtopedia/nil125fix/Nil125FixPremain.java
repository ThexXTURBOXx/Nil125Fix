package de.femtopedia.nil125fix;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import nilloader.api.ClassTransformer;
import nilloader.api.NilLogger;
import nilloader.api.NilMetadata;
import nilloader.api.NilModList;

public class Nil125FixPremain implements Runnable {

    public static final NilLogger log = NilLogger.get("Nil125Fix");

    public static final Set<String> DISABLED_BY_DEFAULT = new HashSet<>(Arrays.asList(
    ));

    @Override
    public void run() {
        Optional<NilMetadata> meta = NilModList.getById("nil125fix");
        if (!meta.isPresent()) {
            log.error("Nil125Fix cannot find itself! AAAAHHHH!");
            return;
        }

        int pkgLength = getClass().getPackage().getName().length() + 1;
        try (ZipFile zip = new ZipFile(meta.get().source)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();

            Map<Class<?>, String> classes = new HashMap<>();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (!name.endsWith("Transformer.class")) continue;

                name = name.substring(0, name.length() - ".class".length())
                        .replace('\\', '/')
                        .replace('/', '.');

                Class<?> clazz = Class.forName(name);
                if (!ClassTransformer.class.isAssignableFrom(clazz) ||
                    Modifier.isAbstract(clazz.getModifiers())) continue;

                classes.put(clazz, clazz.getPackage().getName().substring(pkgLength));
            }

            Configuration config = new Configuration("config/Nil125Fix.cfg", classes.values());
            config.saveIfChanged();

            for (Map.Entry<Class<?>, String> e : classes.entrySet()) {
                if (e.getValue() != null && !config.isEnabled(e.getValue())) {
                    log.info("Skipping disabled transformer " + e.getKey().getName());
                    continue;
                }
                ClassTransformer.register((ClassTransformer) e.getKey().getDeclaredConstructor().newInstance());
            }

            log.info("Applied a few miscellaneous fixes...");
        } catch (Throwable t) {
            log.error("Transformers could not be registered!", t);
        }
    }

}
