package com.goldkl.touhoutinkermodifier.utils;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.LLibrary_Boss_Monster;
import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.mixin.cataclysm.LLibrary_Boss_MonsterAccessor;
import com.goldkl.touhoutinkermodifier.mixin.youkaishomecoming.BossYoukaiEntityAccessor;
import dev.xkmc.youkaishomecoming.content.entity.boss.BossYoukaiEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

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
    }
    public static boolean hasModifier(LivingEntity entity, ModifierId modifier) {
        for(EquipmentSlot equipmentSlot : EquipmentSlot.values())
        {
            if(!(entity.getItemBySlot(equipmentSlot).getItem() instanceof IModifiable))continue;
            ToolStack tool = ToolStack.from(entity.getItemBySlot(equipmentSlot));
            if(tool.getModifier(modifier)!= ModifierEntry.EMPTY)
            {
                return true;
            }
        }
        return false;
    }
    public static boolean hasModifiers(LivingEntity entity, List<ModifierId> modifiers) {
        for(EquipmentSlot equipmentSlot : EquipmentSlot.values())
        {
            if(!(entity.getItemBySlot(equipmentSlot).getItem() instanceof IModifiable))continue;
            ToolStack tool = ToolStack.from(entity.getItemBySlot(equipmentSlot));
            for(ModifierId modifier :modifiers)
            {
                if(tool.getModifier(modifier)!= ModifierEntry.EMPTY)
                {
                    return true;
                }
            }
        }
        return false;
    }
    //这两个词条会修改妖归的怪肉判断，目前是写死的
    public static List<ModifierId>FleshModifiers = List.of(ModifierIds.devourdarkness,ModifierIds.apparitionsstalkthenight);
}
