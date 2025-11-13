package com.goldkl.touhoutinkermodifier.utils;

import net.minecraftforge.fml.ModList;

public class TTMModListUtil {
    public static class modName{
        public static String TouhouLittleMaid = "touhou_little_maid";
    }
    public static boolean TouhouLittleMaidLoaded = ModList.get().isLoaded(modName.TouhouLittleMaid);
}
