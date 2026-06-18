package com.goldkl.touhoutinkermodifier.compat.ingameinfoxml.compat.improvedmobs;

import com.goldkl.touhoutinkermodifier.mixin.improvemobs.ClientEventsAccessor;
import df11zomgraves.ingameinfo.tag.Tag;

public class DifficultyTag extends Tag {
    @Override
    public String getCategory() {
        return "im_ttm_difficulty";
    }
    @Override
    public String getValue() {
        return String.format("%.2f", ClientEventsAccessor.getClientDifficulty());
    }
}
