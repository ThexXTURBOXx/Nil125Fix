package de.femtopedia.nil125fix.disabledebug.idresolver;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("IDResolver")
public class IDResolverTransformer extends MiniPlusTransformer {

    @Patch.Method("<clinit>()V")
    public void fixConsoleLogLevel(PatchContext ctx) {
        ctx.search(
                PUTSTATIC("IDResolver", "logger", "Ljava/util/logging/Logger;")
        ).jumpAfter();

        ctx.add(
                GETSTATIC("IDResolver", "logger", "Ljava/util/logging/Logger;"),
                INVOKESTATIC(hooks(), "appendConsoleHandler", "(Ljava/util/logging/Logger;)V")
        );
    }

    public static class Hooks {

        public static void appendConsoleHandler(Logger logger) {
            logger.setUseParentHandlers(false);

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            consoleHandler.setLevel(Level.WARNING);
            logger.addHandler(consoleHandler);
        }

    }

}
