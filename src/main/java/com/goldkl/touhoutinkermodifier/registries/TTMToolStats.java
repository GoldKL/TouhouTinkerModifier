package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.stat.ToolBulletTypeStat;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class TTMToolStats {
    public static final ToolBulletTypeStat BULLET_TYPE = ToolStats.register(new ToolBulletTypeStat(name("bullet_type")));
    private static ToolStatId name(String name) {
        return new ToolStatId(TouhouTinkerModifier.getResource(name));
    }
}
