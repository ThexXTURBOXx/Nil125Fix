package de.femtopedia.nil125fix;

import java.util.Optional;
import nilloader.NilAgent;
import nilloader.api.ASMTransformer;
import nilloader.api.lib.mini.InsnBuilder;
import nilloader.api.lib.mini.PatchContext;

public abstract class AdvPlusTransformer extends InsnBuilder implements PlusTransformer, ASMTransformer {

    public AdvPlusTransformer() {
        super(Optional.ofNullable(NilAgent.getActiveMappings(Nil125FixPremain.MOD_ID)));
    }

    @Override
    public void removePrintlns(PatchContext ctx) {
        PlusTransformer.removePrintlns(this, ctx);
    }

}
