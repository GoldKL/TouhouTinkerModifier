package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.bullettype.AbstractBulletType;
import net.minecraft.resources.ResourceLocation;
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
}
