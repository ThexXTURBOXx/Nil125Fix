package de.femtopedia.nil125fix.factorization.fixbarrelitem;

import de.femtopedia.nil125fix.MiniPlusTransformer;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("factorization.client.render.TileEntityBarrelRenderer")
public class TileEntityBarrelRendererTransformer extends MiniPlusTransformer {

    @Patch.Method("doRenderItem(Lfq;DDDFF)V")
    public void renderCustomRenderedItemBlocksCorrectly(PatchContext ctx) {
        LabelNode Lskip = new LabelNode();
        LabelNode Lcontinue = new LabelNode();

        ctx.search(
                GETFIELD("net/minecraft/src/ItemStack", "itemID", "I"),
                SIPUSH(256)
        ).jumpBefore();
        ctx.add(
                INVOKEVIRTUAL("net/minecraft/src/ItemStack", "getItem", "()Lnet/minecraft/src/Item;"),
                INSTANCEOF("net/minecraft/src/ItemBlock"),
                IFEQ(Lcontinue),
                GOTO(Lskip)
        );

        ctx.search(
                ILOAD(19)
        ).jumpBefore();
        ctx.add(
                Lskip
        );

        ctx.search(
                ALOAD(0),
                GETFIELD("factorization/client/render/TileEntityBarrelRenderer", "itemRender", "Ltw;"),
                ALOAD(10),
                GETFIELD("net/minecraft/src/ItemStack", "itemID", "I"),
                SIPUSH(256)
        ).jumpBefore();
        ctx.add(
                Lcontinue
        );
    }

    @Patch.Method("doRenderItem(Lfq;DDDFF)V")
    public void renderDefaultItemBlocksCorrectly(PatchContext ctx) {
        LabelNode Lskip = new LabelNode();
        LabelNode Lcontinue = new LabelNode();

        ctx.search(
                GETFIELD("net/minecraft/src/ItemStack", "itemID", "I"),
                SIPUSH(256)
        ).next().next().jumpBefore();
        ctx.add(
                INVOKEVIRTUAL("net/minecraft/src/ItemStack", "getItem", "()Lnet/minecraft/src/Item;"),
                INSTANCEOF("net/minecraft/src/ItemBlock"),
                IFEQ(Lcontinue),
                GOTO(Lskip)
        );

        ctx.search(
                GETSTATIC("net/minecraft/src/Block", "blocksList", "[Lnet/minecraft/src/Block;")
        ).jumpBefore();
        ctx.add(
                Lskip
        );

        ctx.search(
                ALOAD(10),
                INVOKEVIRTUAL("net/minecraft/src/ItemStack", "getItem", "()Lnet/minecraft/src/Item;"),
                INVOKEVIRTUAL("net/minecraft/src/Item", "c", "()Z")
        ).jumpBefore();
        ctx.add(
                Lcontinue
        );
    }

}
