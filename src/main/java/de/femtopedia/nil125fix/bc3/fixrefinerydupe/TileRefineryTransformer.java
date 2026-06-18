package de.femtopedia.nil125fix.bc3.fixrefinerydupe;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("buildcraft.factory.TileRefinery")
public class TileRefineryTransformer extends MiniPlusTransformer {

    @Patch.Method.Optional // Does not exist in BC 3.1.5
    @Patch.Method.AffectsControlFlow
    @Patch.Method("consumeInput(Lbuildcraft/api/liquids/LiquidStack;)Z")
    public void compareQuantities(PatchContext ctx) {
        LabelNode Lskip = new LabelNode();

        ctx.search(
                INVOKEVIRTUAL("buildcraft/api/liquids/LiquidStack", "isLiquidEqual",
                        "(Lbuildcraft/api/liquids/LiquidStack;)Z"),
                IFEQ(null)
        ).jumpAfter();
        ctx.add(
                ALOAD(0),
                GETFIELD("buildcraft/factory/TileRefinery", "slot1", "Lbuildcraft/factory/TileRefinery$Slot;"),
                GETFIELD("buildcraft/factory/TileRefinery$Slot", "quantity", "I"),
                ALOAD(1),
                GETFIELD("buildcraft/api/liquids/LiquidStack", "amount", "I"),
                IF_ICMPLT(Lskip)
        );

        ctx.search(
                IRETURN()
        ).jumpAfter();
        ctx.add(
                Lskip
        );

        Lskip = new LabelNode();

        ctx.search(
                INVOKEVIRTUAL("buildcraft/api/liquids/LiquidStack", "isLiquidEqual",
                        "(Lbuildcraft/api/liquids/LiquidStack;)Z"),
                IFEQ(null)
        ).jumpAfter();
        ctx.add(
                ALOAD(0),
                GETFIELD("buildcraft/factory/TileRefinery", "slot2", "Lbuildcraft/factory/TileRefinery$Slot;"),
                GETFIELD("buildcraft/factory/TileRefinery$Slot", "quantity", "I"),
                ALOAD(1),
                GETFIELD("buildcraft/api/liquids/LiquidStack", "amount", "I"),
                IF_ICMPLT(Lskip)
        );

        ctx.search(
                IRETURN()
        ).jumpAfter();
        ctx.add(
                Lskip
        );
    }

}
