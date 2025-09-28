package com.goldkl.touhoutinkermodifier.stat.tools;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.bullettype.AbstractBulletType;
import com.goldkl.touhoutinkermodifier.registries.TTMBulletTypeRegistry;
import com.goldkl.touhoutinkermodifier.registries.TTMTinkerLoadables;
import com.goldkl.touhoutinkermodifier.registries.TTMToolStats;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.materials.stats.IRepairableMaterialStats;
import slimeknights.tconstruct.library.materials.stats.MaterialStatType;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public record GoheiMaterialStats(int durability, AbstractBulletType abstractBulletType) implements IRepairableMaterialStats {
    public static final MaterialStatsId ID = new MaterialStatsId(TouhouTinkerModifier.getResource("gohei"));
    public static final MaterialStatType<GoheiMaterialStats> TYPE = new MaterialStatType<>(ID,
            new GoheiMaterialStats(1,  TTMBulletTypeRegistry.DefaultBullet),
            RecordLoadable.create(
                IRepairableMaterialStats.DURABILITY_FIELD,
                TTMTinkerLoadables.BULLET_TYPE.defaultField("bullet_type", TTMBulletTypeRegistry.DefaultBullet, true, GoheiMaterialStats::abstractBulletType),
                GoheiMaterialStats::new));
    private static final List<Component> DESCRIPTION = ImmutableList.of(
            ToolStats.DURABILITY.getDescription(),
            TTMToolStats.BULLET_TYPE.getDescription());

    @Override
    public MaterialStatType<?> getType() {
        return TYPE;
    }
    @Override
    public List<Component> getLocalizedDescriptions() {
        return DESCRIPTION;
    }
    @Override
    public List<Component> getLocalizedInfo() {
        List<Component> info = Lists.newArrayList();
        info.add(ToolStats.DURABILITY.formatValue(this.durability));
        info.add(TTMToolStats.BULLET_TYPE.formatValue(this.abstractBulletType));
        return info;
    }
    @Override
    public void apply(ModifierStatsBuilder builder, float scale) {
        ToolStats.DURABILITY.update(builder, durability * scale);
        TTMToolStats.BULLET_TYPE.update(builder, abstractBulletType);
    }
}
