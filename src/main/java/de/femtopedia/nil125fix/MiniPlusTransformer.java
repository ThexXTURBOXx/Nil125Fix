package de.femtopedia.nil125fix;

import nilloader.api.ClassRetransformer;
import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;

public abstract class MiniPlusTransformer extends MiniTransformer implements PlusTransformer, ClassRetransformer {

    @Override
    public void removePrintlns(PatchContext ctx) {
        PlusTransformer.removePrintlns(this, ctx);
    }

}
