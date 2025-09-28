package com.goldkl.touhoutinkermodifier.registries;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.bullettype.*;
import com.goldkl.touhoutinkermodifier.bullettype.marisa.*;
import com.goldkl.touhoutinkermodifier.bullettype.reimu.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class TTMBulletTypeRegistry {
    public static final ResourceKey<Registry<AbstractBulletType>> BULLET_TYPE_REGISTRY_KEY = ResourceKey.createRegistryKey(TouhouTinkerModifier.getResource("bullet_type"));
    private static final DeferredRegister<AbstractBulletType> BULLET_TYPES = DeferredRegister.create(BULLET_TYPE_REGISTRY_KEY, TouhouTinkerModifier.MODID);
    public static final Supplier<IForgeRegistry<AbstractBulletType>> REGISTRY = BULLET_TYPES.makeRegistry(() -> new RegistryBuilder<AbstractBulletType>().disableSaving().disableOverrides());
    public static final AbstractBulletType DefaultBullet = new DefaultBulletType();
    public static final RegistryObject<AbstractBulletType> ReimuA = registerBulletType(new ReimuABulletType());
    public static final RegistryObject<AbstractBulletType> ReimuB = registerBulletType(new ReimuBBulletType());
    public static final RegistryObject<AbstractBulletType> MarisaA = registerBulletType(new MarisaABulletType());
    public static final RegistryObject<AbstractBulletType> MarisaB = registerBulletType(new MarisaBBulletType());


    public static RegistryObject<AbstractBulletType> registerBulletType(AbstractBulletType bulletType)
    {
        return BULLET_TYPES.register(bulletType.getTypeName(), () -> bulletType);
    }
    public static void register(IEventBus eventBus)
    {
        BULLET_TYPES.register(eventBus);
    }
    public static AbstractBulletType getBulletType(ResourceLocation resourceLocation) {
        AbstractBulletType bulletType = null;
        if(resourceLocation != null && !resourceLocation.equals(DefaultBulletType.ID))
        {
            bulletType = REGISTRY.get().getValue(resourceLocation);
        }
        return bulletType == null ? DefaultBullet : bulletType;
    }
}
