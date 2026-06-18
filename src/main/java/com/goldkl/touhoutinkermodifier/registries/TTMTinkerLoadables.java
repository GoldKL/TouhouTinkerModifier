package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.bullettype.AbstractBulletType;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import slimeknights.mantle.data.loadable.Loadables;
import slimeknights.mantle.data.loadable.primitive.StringLoadable;

public class TTMTinkerLoadables {
    public static final StringLoadable<AbstractBulletType> BULLET_TYPE = Loadables.RESOURCE_LOCATION.xmap((id, error) -> {
        AbstractBulletType bulletType = TTMBulletTypeRegistry.getBulletType(id);
        if (bulletType != null) {
            return bulletType;
        }
        throw error.create("Unknown bulletType " + id);
    }, (bulletType, error) -> {
        ResourceLocation id = bulletType.getId();
        if (id != null) {
            return id;
        }
        throw error.create("Attempt to serialize unregistered bulletType " + bulletType);
    });
    public static final StringLoadable<AbstractSpell> SPELL = Loadables.RESOURCE_LOCATION.xmap((id, error) -> {
        AbstractSpell spell = SpellRegistry.getSpell(id);
        if (spell != SpellRegistry.none()) {
            return spell;
        }
        throw error.create("Unknown Spell " + id);
    }, (spell, error) -> {
        ResourceLocation id = spell.getSpellResource();
        if (id != null) {
            return id;
        }
        throw error.create("Attempt to serialize unregistered spell " + spell);
    });
    public static final StringLoadable<SchoolType> SPELL_SCHOOLTYPE = Loadables.RESOURCE_LOCATION.xmap((id, error) -> {
        SchoolType school = SchoolRegistry.getSchool(id);
        if (school != null) {
            return school;
        }
        throw error.create("Unknown School Type " + id);
    }, (spell, error) -> {
        ResourceLocation id = spell.getId();
        if (id != null) {
            return id;
        }
        throw error.create("Attempt to serialize unregistered School Type " + spell);
    });
    public static final StringLoadable<TagKey<AbstractSpell>> SPELL_TAG = Loadables.tagKey(SpellRegistry.SPELL_REGISTRY_KEY);

}
