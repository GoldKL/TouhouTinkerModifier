package com.goldkl.touhoutinkermodifier.utils;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.LLibrary_Boss_Monster;
import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.mixin.cataclysm.LLibrary_Boss_MonsterAccessor;
import com.goldkl.touhoutinkermodifier.mixin.tconstruct.BasicModifierAccessor;
import com.goldkl.touhoutinkermodifier.mixin.youkaishomecoming.BossYoukaiEntityAccessor;
import dev.xkmc.youkaishomecoming.content.entity.boss.BossYoukaiEntity;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.impl.BasicModifier;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierLevelDisplay;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class TTMEntityUtils {
    public static void clearLivingEntityInvulnerableTime(LivingEntity entity)
    {
        entity.invulnerableTime = 0;
        if(entity instanceof LLibrary_Boss_Monster lLibraryBossMonster)
        {
            ((LLibrary_Boss_MonsterAccessor)lLibraryBossMonster).setreducedDamageTicks(0);
        }
        if(entity instanceof BossYoukaiEntity bossYoukaiEntity)
        {
            ((BossYoukaiEntityAccessor)bossYoukaiEntity).sethurtCD(1000);
        }
        if(entity instanceof Apostle apostle) {
            apostle.moddedInvul = 0;
        }
        /*if (entity instanceof Apostle apostle && apostle instanceof ApollyonAbilityHelper helper && apostle instanceof Apollyon2Interface apollyon2Interface) {
            if (helper.allTitlesApostle_1_20_1$isApollyon())
            {
                if (helper.allTitlesApostle_1_20_1$getHitCooldown() > 0) {
                    helper.allTitlesApostle_1_20_1$setHitCooldown(0);
                }
                if (helper.getApollyonTime() > 0) {
                    helper.setApollyonTime(0);
                }
                if (apollyon2Interface.revelaionfix$getHitCooldown() > 0) {
                    apollyon2Interface.revelaionfix$setHitCooldown(0);
                }
            }
        }*/
    }
    public static void addLivingEntityAbsorptionAmountByMax(LivingEntity entity, float absorb)
    {
        float maxabsorb = entity.getMaxHealth() * 0.5f;
        if(entity.getAbsorptionAmount() < maxabsorb) {
            entity.setAbsorptionAmount(Math.min(maxabsorb, entity.getAbsorptionAmount() + absorb));
        }
    }
    //方便给onInventoryTick里面用
    public static boolean validArmorTool(IToolStackView tool, boolean isCorrectSlot, LivingEntity holder, ItemStack stack)
    {
        return isCorrectSlot && (stack != holder.getMainHandItem() && stack != holder.getOffhandItem() || tool.hasTag(TinkerTags.Items.HELD));
    }
    public static boolean HasMatchModifierTool(LivingEntity entity, Predicate<IToolStackView> predicate) {
        return HasMatchModifierTool(entity,predicate,EquipmentSlot.values());
    }
    public static boolean HasMatchModifierTool(LivingEntity entity, Predicate<IToolStackView> predicate, EquipmentSlot... slots) {
        for(EquipmentSlot equipmentSlot : slots)
        {
            if(!(entity.getItemBySlot(equipmentSlot).getItem() instanceof IModifiable))continue;
            ToolStack tool = ToolStack.from(entity.getItemBySlot(equipmentSlot));
            if(ModifierUtil.validArmorSlot(tool,equipmentSlot) && predicate.test(tool))
            {
                return true;
            }
        }
        return false;
    }
    public static int getModifiertotalLevel(LivingEntity entity, ModifierId modifier) {
        return getModifiertotalLevel(entity,modifier,EquipmentSlot.values());
    }
    public static int getModifiertotalLevel(LivingEntity entity, ModifierId modifier, EquipmentSlot... slots) {
        int level = 0;
        for(EquipmentSlot equipmentSlot : slots)
        {
            if(!(entity.getItemBySlot(equipmentSlot).getItem() instanceof IModifiable))continue;
            ToolStack tool = ToolStack.from(entity.getItemBySlot(equipmentSlot));
            if(ModifierUtil.validArmorSlot(tool,equipmentSlot) && tool.getModifier(modifier)!= ModifierEntry.EMPTY)
            {
                level += getModifierLevel(tool.getModifier(modifier));
            }
        }
        return level;
    }
    public static int getModifiermaxLevel(LivingEntity entity, ModifierId modifier) {
        return getModifiermaxLevel(entity,modifier,EquipmentSlot.values());
    }
    public static int getModifiermaxLevel(LivingEntity entity, ModifierId modifier, EquipmentSlot... slots) {
        int level = 0;
        for(EquipmentSlot equipmentSlot : slots)
        {
            if(!(entity.getItemBySlot(equipmentSlot).getItem() instanceof IModifiable))continue;
            ToolStack tool = ToolStack.from(entity.getItemBySlot(equipmentSlot));
            if(ModifierUtil.validArmorSlot(tool,equipmentSlot) &&tool.getModifier(modifier)!= ModifierEntry.EMPTY)
            {
                level = Math.max(level, getModifierLevel(tool.getModifier(modifier)));
            }
        }
        return level;
    }
    public static boolean hasModifier(LivingEntity entity, ModifierId modifier) {
        return hasModifier(entity,modifier,EquipmentSlot.values());
    }
    public static boolean hasModifier(LivingEntity entity, ModifierId modifier, EquipmentSlot... slots) {
        for(EquipmentSlot equipmentSlot : slots)
        {
            if(!(entity.getItemBySlot(equipmentSlot).getItem() instanceof IModifiable))continue;
            ToolStack tool = ToolStack.from(entity.getItemBySlot(equipmentSlot));
            if(ModifierUtil.validArmorSlot(tool,equipmentSlot) && tool.getModifier(modifier)!= ModifierEntry.EMPTY)
            {
                return true;
            }
        }
        return false;
    }
    public static boolean hasModifiers(LivingEntity entity, List<ModifierId> modifiers) {
        return hasModifiers(entity,modifiers,EquipmentSlot.values());
    }
    public static boolean hasModifiers(LivingEntity entity, List<ModifierId> modifiers, EquipmentSlot... slots) {
        for(EquipmentSlot equipmentSlot : slots)
        {
            if(!(entity.getItemBySlot(equipmentSlot).getItem() instanceof IModifiable))continue;
            ToolStack tool = ToolStack.from(entity.getItemBySlot(equipmentSlot));
            if(ModifierUtil.validArmorSlot(tool,equipmentSlot))
            {
                for(ModifierId modifier :modifiers)
                {
                    if(tool.getModifier(modifier)!= ModifierEntry.EMPTY)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public static int gettotallevelwithtag(IToolContext tool, TagKey<Modifier> tag)
    {
        Iterator<ModifierEntry> it = tool.getModifiers().iterator();
        int level = 0;
        while(it.hasNext())
        {
            ModifierEntry entry = it.next();
            if (entry.matches(tag)) {
                level += getModifierLevel(entry);
            }
        }
        return level;
    }
    private static int getModifierLevel(ModifierEntry entry)
    {
        return TTMEntityUtils.isNolevelModifier(entry)?1:entry.intEffectiveLevel();
    }
    public static boolean isNolevelModifier(ModifierEntry entry)
    {
        return TTMEntityUtils.isNolevelModifier(entry.getModifier());
    }
    public static boolean isNolevelModifier(Modifier modifier)
    {
        if(modifier instanceof NoLevelsModifier)
        {
            return true;
        }
        else if(modifier instanceof BasicModifier)
        {
            return ((BasicModifierAccessor)modifier).getLevelDisplay() == ModifierLevelDisplay.NO_LEVELS;
        }
        return false;
    }
    //这两个词条会修改妖归的怪肉判断，目前是写死的
    public static List<ModifierId>FleshModifiers = List.of(ModifierIds.devourdarkness,ModifierIds.apparitionsstalkthenight);
}
