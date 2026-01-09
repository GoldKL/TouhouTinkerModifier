package com.goldkl.touhoutinkermodifier.utils;

import net.minecraftforge.fml.ModList;

public class TTMModListUtil {
    public static class modName{
        public static String TouhouLittleMaid = "touhou_little_maid";
        public static String BetterComBat = "bettercombat";
        public static String SakuraTinker = "sakuratinker";
    }
    public static boolean TouhouLittleMaidLoaded = ModList.get().isLoaded(modName.TouhouLittleMaid);
    public static boolean BetterComBatLoaded = ModList.get().isLoaded(modName.BetterComBat);
    public static boolean SakuraTinkerLoaded = ModList.get().isLoaded(modName.SakuraTinker);


}
