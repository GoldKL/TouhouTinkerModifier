package com.goldkl.touhoutinkermodifier.helper.compat;

import com.goldkl.touhoutinkermodifier.modifiers.DeepbaloneyModifier;
import com.ssakura49.sakuratinker.library.tinkering.tools.STToolStats;
import net.minecraftforge.eventbus.api.IEventBus;

public class SakuraThinkerHelper {
    public static void load(IEventBus modEventBus)
    {
        DeepbaloneyModifier.addBlackStatList(STToolStats.COOLDOWN);
    }
}
