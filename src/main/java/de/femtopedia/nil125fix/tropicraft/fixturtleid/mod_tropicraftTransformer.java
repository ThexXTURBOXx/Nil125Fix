package de.femtopedia.nil125fix.tropicraft.fixturtleid;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("mod_tropicraft")
public class mod_tropicraftTransformer extends MiniPlusTransformer {

    @Patch.Method("addAllEntities()V")
    public void changeTurtleId(PatchContext ctx) {
        ctx.search(
                LDC("Turtle")
        ).jumpAfter();

        ctx.add(
                POP(),
                LDC("TurtleTC")
        );
    }

}
