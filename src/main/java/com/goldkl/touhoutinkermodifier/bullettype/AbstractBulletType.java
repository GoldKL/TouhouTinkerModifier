package com.goldkl.touhoutinkermodifier.bullettype;

import com.goldkl.touhoutinkermodifier.hook.MeleeDamagePercentModifierHook;
import com.goldkl.touhoutinkermodifier.registries.ModifierHooksRegistry;
import com.goldkl.touhoutinkermodifier.registries.TTMBulletTypeRegistry;
import dev.xkmc.youkaishomecoming.init.registrate.YHAttributes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.ResourceColorManager;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.utils.Util;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class AbstractBulletType {
    @Nullable
    private ResourceLocation id;
    @Nullable
    private String translationKey;
    @Nullable
    private Component displayName;
    public static UUID uuid = UUID.fromString("5c494360-edf5-4a73-8920-925f30696509");
    abstract public String getTypeName();

    public final TextColor getTextColor() {
        return ResourceColorManager.getTextColor(Objects.requireNonNull(getTranslationKey()));
    }
    public final int getColor() {
        return getTextColor().getValue();
    }
    @Nullable
    public ResourceLocation getId() {
        if(id == null) {
            id = TTMBulletTypeRegistry.REGISTRY.get().getKey(this);
        }
        return id;
    }
    @Nullable
    public String getTranslationKey() {
        if (this.translationKey == null) {
            this.translationKey = Util.makeTranslationKey("bullet_type", Objects.requireNonNull(getId()));
        }
        return this.translationKey;
    }
    public Component getDisplayName() {
        if (displayName == null) {
            displayName = Component.translatable(Objects.requireNonNull(getTranslationKey())).withStyle(style -> style.withColor(getTextColor()));
        }
        return displayName;
    }
    public abstract double getHitboxFix();
    public abstract boolean isShootBulletTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingTicks);
    //子弹开始发射时
    public void startShootBullet(Level level, LivingEntity entity, ItemStack itemStack)
    {
        if(!level.isClientSide())
        {
            AttributeInstance instance = entity.getAttribute(YHAttributes.HITBOX.get());
            if(instance != null)
            {
                instance.removeModifier(uuid);
                instance.addTransientModifier(new AttributeModifier(uuid,"Bullet Type to fix Hitbox",getHitboxFix(), AttributeModifier.Operation.ADDITION));
            }
        }
    }
    //弹幕类型的子弹发射
    abstract public void ShootBulletTick(Level level, LivingEntity entity, ItemStack itemStack, int remainingTicks);
    //子弹发射结束后
    public void stopShootBullet(Level level, LivingEntity entity, ItemStack itemStack,int remainingTicks)
    {
        if(!level.isClientSide())
        {
            AttributeInstance instance = entity.getAttribute(YHAttributes.HITBOX.get());
            if(instance != null)
            {
                instance.removeModifier(uuid);
            }
        }
    }
}
