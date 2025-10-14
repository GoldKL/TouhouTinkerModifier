package com.goldkl.touhoutinkermodifier.utils;

import com.goldkl.touhoutinkermodifier.registries.TagsRegistry;
import dev.shadowsoffire.attributeslib.util.AttributesUtil;
import slimeknights.mantle.data.predicate.damage.DamageSourcePredicate;

public class TTMPredicate {
    public static DamageSourcePredicate PHYSICAL_DAMAGE = DamageSourcePredicate.simple(AttributesUtil::isPhysicalDamage);
    public static DamageSourcePredicate NOT_IGNORE_DAMAGE = DamageSourcePredicate.simple(damageSource -> !damageSource.is(TagsRegistry.DamageTypeTag.PASS_PORTION_MODIFIER));


}
