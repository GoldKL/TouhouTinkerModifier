package com.goldkl.touhoutinkermodifier.utils;

import dev.shadowsoffire.attributeslib.util.AttributesUtil;
import slimeknights.mantle.data.predicate.damage.DamageSourcePredicate;

public class TTMPredicate {
    public static DamageSourcePredicate PHYSICAL_DAMAGE = DamageSourcePredicate.simple(AttributesUtil::isPhysicalDamage);

}
