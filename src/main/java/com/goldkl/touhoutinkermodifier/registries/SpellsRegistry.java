package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.spells.blood.*;
import com.goldkl.touhoutinkermodifier.spells.fire.MasterSparkSpell;
import com.goldkl.touhoutinkermodifier.spells.ice.AbsolutezeroSpell;
import com.goldkl.touhoutinkermodifier.spells.ice.ArcticstormSpell;
import com.goldkl.touhoutinkermodifier.spells.lightning.SkysplitterSpell;
import com.goldkl.touhoutinkermodifier.spells.lightning.TinkerchargeSpell;
import com.goldkl.touhoutinkermodifier.spells.nature.MaximumdosageSpell;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SpellsRegistry {
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, TouhouTinkerModifier.MODID);
    //Blood
    public static final RegistryObject<AbstractSpell> gungnir = registerSpell(new GungnirSpell());
    public static final RegistryObject<AbstractSpell> laevatein = registerSpell(new LaevateinSpell());
    public static final RegistryObject<AbstractSpell> crimsonfantasy = registerSpell(new CrimsonfantasySpell());
    public static final RegistryObject<AbstractSpell> worldender = registerSpell(new WorldenderSpell());
    public static final RegistryObject<AbstractSpell> theworld = registerSpell(new TheworldSpell());
    //Ice
    public static final RegistryObject<AbstractSpell> absolutezero = registerSpell(new AbsolutezeroSpell());
    public static final RegistryObject<AbstractSpell> arcticstorm = registerSpell(new ArcticstormSpell());
    //Lightning
    public static final RegistryObject<AbstractSpell> skysplitter = registerSpell(new SkysplitterSpell());
    public static final RegistryObject<AbstractSpell> tinkercharge = registerSpell(new TinkerchargeSpell());
    //Nature
    public static final RegistryObject<AbstractSpell> maximumdosage = registerSpell(new MaximumdosageSpell());
    //Fire
    public static final RegistryObject<AbstractSpell> masterspark = registerSpell(new MasterSparkSpell());
    public static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell)
    {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }
    public static void register(IEventBus eventBus)
    {
        SPELLS.register(eventBus);
    }
}
