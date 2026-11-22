package com.leanwell.antidote.client.renderer;

import com.leanwell.antidote.Antidote;
import com.leanwell.antidote.entity.WanderingMonkEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.npc.VillagerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.resources.Identifier;

public class WanderingMonkRenderer extends MobRenderer<WanderingMonkEntity, VillagerRenderState, VillagerModel> {
    private static final Identifier MONK_TEXTURE = Antidote.id("textures/entity/wandering_monk.png");

    public WanderingMonkRenderer(EntityRendererProvider.Context context) {
        super(context, new VillagerModel(context.bakeLayer(ModelLayers.WANDERING_TRADER)), 0.5F);
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getPlayerSkinRenderCache()));
        this.addLayer(new CrossedArmsItemLayer<>(this));
        try {
            this.getModel().getHead().getChild("hat").getChild("hat_rim").visible = false;
        } catch (Exception ignored) {
        }
    }

    @Override
    public Identifier getTextureLocation(VillagerRenderState state) {
        return MONK_TEXTURE;
    }

    @Override
    public VillagerRenderState createRenderState() {
        return new VillagerRenderState();
    }

    @Override
    public void extractRenderState(WanderingMonkEntity entity, VillagerRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        HoldingEntityRenderState.extractHoldingEntityRenderState(entity, state, this.itemModelResolver);
        state.isUnhappy = entity.getUnhappyCounter() > 0;
    }
}
