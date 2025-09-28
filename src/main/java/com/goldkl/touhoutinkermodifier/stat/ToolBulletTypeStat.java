package com.goldkl.touhoutinkermodifier.stat;

import com.goldkl.touhoutinkermodifier.bullettype.AbstractBulletType;
import com.goldkl.touhoutinkermodifier.registries.TTMBulletTypeRegistry;
import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.util.JsonHelper;
import slimeknights.mantle.util.RegistryHelper;
import slimeknights.tconstruct.library.tools.stat.IToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.utils.Util;

import java.util.Objects;

public class ToolBulletTypeStat implements IToolStat<AbstractBulletType> {
    private final ToolStatId name;

    public ToolBulletTypeStat(ToolStatId name) {
        this.name = name;
    }
    @Override
    public boolean supports(Item item) {
        return RegistryHelper.contains(TagsRegistry.ItemsTag.GOHEI, item);
    }

    @Override
    public String toString() {
        return "ToolBulletTypeStat{" + name + '}';
    }

    @Override
    @NotNull
    public ToolStatId getName() {
        return name;
    }

    @Override
    @NotNull
    public AbstractBulletType getDefaultValue() {
        return TTMBulletTypeRegistry.DefaultBullet;
    }

    @Override
    @NotNull
    public Object makeBuilder() {
        return new BulletTypeBuilder(getDefaultValue());
    }

    @Override
    @NotNull
    public AbstractBulletType build(ModifierStatsBuilder parent, Object builder) {
        return ((BulletTypeBuilder)builder).value;
    }

    @Override
    public void update(ModifierStatsBuilder builder, @NotNull AbstractBulletType value) {
        builder.<BulletTypeBuilder>updateStat(this, b -> b.value = value);
    }

    @Override
    public @Nullable AbstractBulletType read(Tag tag) {
        if (tag.getId() == Tag.TAG_STRING) {
            ResourceLocation BulletTypeId = ResourceLocation.tryParse(tag.getAsString());
            if (BulletTypeId != null) {
                return TTMBulletTypeRegistry.getBulletType(BulletTypeId);
            }
        }
        return null;
    }

    @Override
    public @Nullable Tag write(AbstractBulletType value) {
        ResourceLocation id = value.getId();
        if (id != null) {
            return StringTag.valueOf(id.toString());
        }
        return null;
    }

    @Override
    public AbstractBulletType deserialize(JsonElement json) {
        ResourceLocation id = JsonHelper.convertToResourceLocation(json, getName().toString());
        return TTMBulletTypeRegistry.getBulletType(id);
    }

    @Override
    public JsonElement serialize(AbstractBulletType value) {
        return new JsonPrimitive(Objects.requireNonNull(value.getId()).toString());
    }

    @Override
    public @NotNull AbstractBulletType fromNetwork(FriendlyByteBuf buffer) {
        return TTMBulletTypeRegistry.getBulletType(buffer.readResourceLocation());
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, AbstractBulletType value) {
        buffer.writeResourceLocation(Objects.requireNonNull(value.getId()));
    }

    @Override
    public @NotNull Component formatValue(AbstractBulletType value) {
        return Component
                .translatable(Util.makeTranslationKey("tool_stat", this.getName()))
                .withStyle((style) -> style.withColor(0xff0000))
                .append(value.getDisplayName());
    }

    private static class BulletTypeBuilder {
        private AbstractBulletType value;
        BulletTypeBuilder(AbstractBulletType value) {
            this.value = value;
        }
    }
}
