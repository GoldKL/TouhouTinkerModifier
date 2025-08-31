package com.goldkl.touhoutinkermodifier.modifiers;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.data.ModifierIds;
import com.ssakura49.sakuratinker.library.tinkering.tools.STToolStats;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.fluid.ToolTankHelper;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.*;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;
;
import java.util.Set;

public class DeepbaloneyModifier extends Modifier implements InventoryTickModifierHook, ToolStatsModifierHook, ModifierRemovalHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.REMOVE, ModifierHooks.INVENTORY_TICK, ModifierHooks.TOOL_STATS);
    }
    @Override
    @Nullable
    public Component onRemoved(IToolStackView tool, Modifier modifier) {
        tool.getPersistentData().remove(ModifierIds.deepbaloney);
        return null;
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        Tag tag = context.getPersistentData().get(ModifierIds.deepbaloney);
        int StatCount = this.GetStatCount(context);
        float[] percent = new float[StatCount];
        if(tag instanceof ListTag list && list.size() == StatCount)
        {
            for (int i = 0; i < StatCount; i++) {
                int tem_num = list.getInt(i);
                for(int j = 0; j < StatCount; j++)
                {
                    percent[j] += i == j ? tem_num * 0.03f : tem_num * (-0.01f);
                }
            }
            Object[] Stats = GetStat(context);
            for(int i = 0; i < StatCount; i++)
            {
                if(Stats[i] instanceof INumericToolStat<?> iNumericToolStat)
                {
                    iNumericToolStat.percent(builder, percent[i]);
                }
            }
        }
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if(world.isClientSide)return;
        Tag tag = tool.getPersistentData().get(ModifierIds.deepbaloney);
        int StatCount = this.GetStatCount(tool);
        int[] num = new int[StatCount];
        int count = 0;
        if(tag instanceof ListTag list && list.size() == StatCount)
        {
            for(int i = 0; i < StatCount; ++i)
            {
                num[i] = list.getInt(i);
                count += num[i];
            }
        }
        int level = modifier.getLevel();
        if(count == level)return;
        if(count > level)
        {
            int cnt = count - level;
            while(cnt > 0)
            {
                cnt--;
                int index = -1;
                int maxnum = 0;
                for(int i = 0; i < StatCount; ++i)
                {
                    if(num[i] > maxnum)
                    {
                        index = i;
                        maxnum = num[i];
                    }
                }
                if(index != -1)
                {
                    num[index]--;
                }
            }
        }
        else
        {
            int cnt = level - count;
            while(cnt > 0)
            {
                cnt--;
                int random = RANDOM.nextInt(StatCount);
                num[random]++;
            }
        }
        ListTag newlist = new ListTag();
        for(int i = 0; i < StatCount; ++i)
        {
            newlist.add(IntTag.valueOf(num[i]));
        }
        tool.getPersistentData().put(ModifierIds.deepbaloney, newlist);
        ToolStack.from(stack).rebuildStats();
    }
    private int GetStatCount(IToolContext tool) {
        return (int) ToolStats.getAllStats().stream()
                .filter((stat)-> stat instanceof INumericToolStat)
                .filter((stat)-> stat.supports(tool.getItem()))
                .filter(stat -> !BLACK_STAT_LIST.contains(stat))
                .filter(stst -> WHITE_MOD_LIST.contains(stst.getName().getNamespace()))
                .count();
    }
    private Object[] GetStat(IToolContext tool) {
        return ToolStats.getAllStats().stream()
                .filter(stat-> stat instanceof INumericToolStat)
                .filter(stat-> stat.supports(tool.getItem()))
                .filter(stat -> !BLACK_STAT_LIST.contains(stat))
                .filter(stst -> WHITE_MOD_LIST.contains(stst.getName().getNamespace()))
                .toArray();
    }
    final static private Set<INumericToolStat<?>>BLACK_STAT_LIST = Set.of(
            ToolTankHelper.CAPACITY_STAT,
            OverslimeModifier.OVERSLIME_STAT,
            STToolStats.COOLDOWN);
    final static private Set<String>WHITE_MOD_LIST = Set.of(
            TConstruct.MOD_ID);
}
