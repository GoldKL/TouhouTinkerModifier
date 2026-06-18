package com.goldkl.touhoutinkermodifier.compat.ingameinfoxml.tags;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifierConfig;
import com.goldkl.touhoutinkermodifier.compat.ingameinfoxml.compat.improvedmobs.DifficultyTag;
import com.goldkl.touhoutinkermodifier.registries.AttributesRegistry;
import com.goldkl.touhoutinkermodifier.utils.TTMModListUtil;
import df11zomgraves.ingameinfo.tag.Tag;
import df11zomgraves.ingameinfo.tag.TagRegistry;

public abstract class BoundaryPowerTag extends Tag {
    @Override
    public String getCategory() {
        return "boundarypower";
    }
    public static class Player extends BoundaryPowerTag {
        @Override
        public String getValue() {
            if (player.getAttributes().hasAttribute(AttributesRegistry.BOUNDARY_POWER.get()))
                return String.format("%.1f",player.getAttributeValue(AttributesRegistry.BOUNDARY_POWER.get()));
            return "-1";
        }
    }
    public static class World extends BoundaryPowerTag {
        @Override
        public String getValue() {
            return String.format("%.1f",TouhouTinkerModifierConfig.dimension_coefficient.getOrDefault(world.dimension(),0.0));
        }
    }
    public static void register() {
        TagRegistry.INSTANCE.register(new Player().setName("playerboundarypower"));
        TagRegistry.INSTANCE.register(new World().setName("worldboundarypower"));
        if(TTMModListUtil.ImprovedMobsLoaded){
            TagRegistry.INSTANCE.register(new DifficultyTag().setName("im_ttm_difficulty"));
        }
    }
}
