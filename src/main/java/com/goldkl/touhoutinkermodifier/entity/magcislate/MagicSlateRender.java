package com.goldkl.touhoutinkermodifier.entity.magcislate;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class MagicSlateRender extends GeoEntityRenderer<MagicSlateEntity> {
    public MagicSlateRender(EntityRendererProvider.Context context, ResourceLocation resourceLocation) {
        super(context, new MagicSlateModel(resourceLocation));
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
    public static MagicSlateRender FireMagicSlateRender(EntityRendererProvider.Context context){
        return new MagicSlateRender(context, TouhouTinkerModifier.getResource("fire_magic_slate"));
    }
    public static MagicSlateRender IceMagicSlateRender(EntityRendererProvider.Context context){
        return new MagicSlateRender(context, TouhouTinkerModifier.getResource("ice_magic_slate"));
    }
    public static MagicSlateRender GeoMagicSlateRender(EntityRendererProvider.Context context){
        return new MagicSlateRender(context, TouhouTinkerModifier.getResource("geo_magic_slate"));
    }
    public static MagicSlateRender NatureMagicSlateRender(EntityRendererProvider.Context context){
        return new MagicSlateRender(context, TouhouTinkerModifier.getResource("nature_magic_slate"));
    }
    public static MagicSlateRender LightningMagicSlateRender(EntityRendererProvider.Context context){
        return new MagicSlateRender(context, TouhouTinkerModifier.getResource("lightning_magic_slate"));
    }

}