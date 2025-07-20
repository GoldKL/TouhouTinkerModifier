package com.goldkl.touhoutinkermodifier.utils;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.LLibrary_Boss_Monster;
import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.goldkl.touhoutinkermodifier.mixin.cataclysm.LLibrary_Boss_MonsterAccessor;
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
    public static List<ModifierId>FleshModifiers = List.of(ModifierIds.devourdarkness,ModifierIds.apparitionsstalkthenight);
    public static class BooleanStat {
        boolean booleanstat;
        public BooleanStat(){
            booleanstat = false;
        }
        public BooleanStat(boolean Stat){
            this.booleanstat = Stat;
        }
        public boolean getBooleanstat(){
            return booleanstat;
        }
        public void setBooleanstat(boolean booleanstat){
            this.booleanstat = booleanstat;
        }
    }
    public static class IntStat {
        int intstat;
        public IntStat(){
            intstat = 0;
        }
        public IntStat(int stat){
            this.intstat = stat;
        }
        public int getIntStat(){
            return intstat;
        }
        public void setIntStat(int stat){
            this.intstat = stat;
        }
    }
    public static class LongStat {
        long longstat;
        public LongStat(){
            longstat = 0;
        }
        public LongStat(long stat){
            this.longstat = stat;
        }
        public long getLongStat(){
            return longstat;
        }
        public void setLongStat(long stat){
            this.longstat = stat;
        }
    }
}
