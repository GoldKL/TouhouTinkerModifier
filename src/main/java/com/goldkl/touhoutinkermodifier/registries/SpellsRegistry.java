package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.spells.blood.CrimsonfantasySpell;
import com.goldkl.touhoutinkermodifier.spells.blood.GungnirSpell;
import com.goldkl.touhoutinkermodifier.spells.blood.LaevateinSpell;
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

    public static RegistryObject<AbstractSpell> registerSpell(AbstractSpell spell)
    {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }
    public static void register(IEventBus eventBus)
    {
        SPELLS.register(eventBus);
    }
}
