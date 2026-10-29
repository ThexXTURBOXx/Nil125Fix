package de.femtopedia.nil125fix.matmos.usemirror;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import java.net.URL;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("MAtExpansionFetcher")
public class MAtExpansionFetcherTransformer extends MiniPlusTransformer {

    @Patch.Method("getDatabase(Ljava/net/URL;)V")
    public void useMirror(PatchContext ctx) {
        ctx.search(
                PUTFIELD("MAtExpansionFetcher", "url", "Ljava/net/URL;")
        ).jumpAfter();

        ctx.add(
                ALOAD(0),
                ALOAD(0),
                GETFIELD("MAtExpansionFetcher", "url", "Ljava/net/URL;"),
                INVOKESTATIC(hooks(), "useMirror", "(Ljava/net/URL;)Ljava/net/URL;"),
                PUTFIELD("MAtExpansionFetcher", "url", "Ljava/net/URL;")
        );
    }

    public static class Hooks {

        public static URL useMirror(URL url) {
            String s = url.toString();
            if (s.contains("ha3extra.googlecode.com/svn/trunk/matmos/")) {
                s = s.replace("ha3extra.googlecode.com/svn/trunk/matmos/",
                        "raw.githubusercontent.com/ThexXTURBOXx/NilFix/tree/1.2.5/mirror/matmos/");
                s = s.replace("http://", "https://");
            }

            try {
                return new URL(s);
            } catch (Throwable ignored) {
            }
            return url; // Fallback if something real bad happens
        }

    }

}
