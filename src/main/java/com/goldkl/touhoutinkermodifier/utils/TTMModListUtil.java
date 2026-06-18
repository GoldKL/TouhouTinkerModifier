package com.goldkl.touhoutinkermodifier.utils;

import net.minecraftforge.fml.ModList;

public class TTMModListUtil {
    public static class modName{
        public static String TouhouLittleMaid = "touhou_little_maid";
        public static String BetterComBat = "bettercombat";
        public static String SakuraTinker = "sakuratinker";
        public static String Ingameinfoxml = "ingameinfoxml";
        public static String Improvedmobs = "improvedmobs";
        public static String Tinkercuriolib = "tinkercuriolib";
    }
    public static boolean TouhouLittleMaidLoaded = ModList.get().isLoaded(modName.TouhouLittleMaid);
    public static boolean BetterComBatLoaded = ModList.get().isLoaded(modName.BetterComBat);
    public static boolean SakuraTinkerLoaded = ModList.get().isLoaded(modName.SakuraTinker);
    public static boolean IngameinfoxmlLoaded = ModList.get().isLoaded(modName.Ingameinfoxml);
    public static boolean ImprovedMobsLoaded = ModList.get().isLoaded(modName.Improvedmobs);
    public static boolean TinkercuriolibLoaded = ModList.get().isLoaded(modName.Tinkercuriolib);
}
