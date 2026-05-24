package de.femtopedia.nil125fix;

import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import nilloader.api.ClassTransformer;
import nilloader.api.NilLogger;
import nilloader.api.NilMetadata;
import nilloader.api.NilModList;

public class Nil125FixPremain implements Runnable {

    public static final NilLogger log = NilLogger.get("Nil125Fix");

    @Override
    public void run() {
        Optional<NilMetadata> meta = NilModList.getById("nil125fix");
        if (!meta.isPresent()) {
            log.error("Nil125Fix cannot find itself! AAAAHHHH!");
            return;
        }

        try (ZipFile zip = new ZipFile(meta.get().source)) {
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (!name.endsWith("Transformer.class")) continue;

                name = name.substring(0, name.length() - ".class".length())
                        .replace('\\', '/')
                        .replace('/', '.');

                Class<?> clazz = Class.forName(name);
                if (!ClassTransformer.class.isAssignableFrom(clazz) ||
                    Modifier.isAbstract(clazz.getModifiers())) continue;

                ClassTransformer.register((ClassTransformer) clazz.getDeclaredConstructor().newInstance());
            }

            log.info("Applied a few miscellaneous fixes...");
        } catch (Throwable t) {
            log.error("Transformers could not be registered!", t);
        }
    }

}
