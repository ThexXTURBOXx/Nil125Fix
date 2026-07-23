package de.femtopedia.nil125fix.keepmystats.usededicatedusername;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.StatsSyncher")
public class StatsSyncherTransformer extends MiniPlusTransformer {

    @Patch.Method("<init>(Lnet/minecraft/src/Session;Lnet/minecraft/src/StatFileWriter;Ljava/io/File;)V")
    public void useKeepMyStatsUsernameForStats(PatchContext ctx) {
        ctx.jumpToStart();
        ctx.add(
                ALOAD(1),
                GETFIELD("net/minecraft/src/Session", "username", "Ljava/lang/String;"),
                ASTORE(4),
                ALOAD(1),
                LDC("KeepMyStats"),
                PUTFIELD("net/minecraft/src/Session", "username", "Ljava/lang/String;")
        );

        ctx.search(
                ALOAD(0),
                ALOAD(1),
                PUTFIELD("net/minecraft/src/StatsSyncher", "theSession", "Lnet/minecraft/src/Session;")
        ).jumpBefore();
        ctx.add(
                ALOAD(1),
                ALOAD(4),
                PUTFIELD("net/minecraft/src/Session", "username", "Ljava/lang/String;")
        );
    }

}
