package com.goldkl.touhoutinkermodifier.helper.compat;

import com.goldkl.touhoutinkermodifier.modifiers.DeepbaloneyModifier;
import com.ssakura49.sakuratinker.core.library.tools.stats.STToolStats;
import net.minecraftforge.eventbus.api.IEventBus;

public class SakuraThinkerHelper {
    public static void load(IEventBus modEventBus)
    {
        DeepbaloneyModifier.addBlackStatList(STToolStats.COOLDOWN);
    }
}
