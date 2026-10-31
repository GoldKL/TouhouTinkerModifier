package com.goldkl.touhoutinkermodifier.entity.magcislate;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedGeoModel;

public class MagicSlateModel extends DefaultedGeoModel<MagicSlateEntity> {
    public MagicSlateModel(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }
    private static final ResourceLocation ANIMATION_PATH = TouhouTinkerModifier.getResource("animations/entity/magic_slate.animation.json");
    private static final ResourceLocation MODEL_PATH = TouhouTinkerModifier.getResource("geo/entity/magic_slate.geo.json");
    @Override
    public ResourceLocation getAnimationResource(MagicSlateEntity animatable) {
        return ANIMATION_PATH;
    }
    @Override
    public ResourceLocation getModelResource(MagicSlateEntity animatable) {
        return MODEL_PATH;
    }
    @Override
    protected String subtype() {
        return "entity";
    }
}