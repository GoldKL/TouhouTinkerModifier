package com.goldkl.touhoutinkermodifier.capability;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.communication.TheKindofKillDataMessage;
import com.goldkl.touhoutinkermodifier.tracking.ChannelEventTracker;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Objects;

public class TheKindofKillDataCapability {
    private static final ResourceLocation ID = TouhouTinkerModifier.getResource("thekindofkill");
    public static final Capability<LivingEntityList> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static void register() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.NORMAL, false, RegisterCapabilitiesEvent.class, TheKindofKillDataCapability::register);
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, TheKindofKillDataCapability::attachCapability);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, PlayerEvent.Clone.class, TheKindofKillDataCapability::playerClone);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, PlayerEvent.PlayerRespawnEvent.class, TheKindofKillDataCapability::playerRespawn);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, PlayerEvent.PlayerChangedDimensionEvent.class, TheKindofKillDataCapability::playerChangeDimension);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, PlayerEvent.PlayerLoggedInEvent.class, TheKindofKillDataCapability::playerLoggedIn);
    }
    private static void register(RegisterCapabilitiesEvent event) {
        event.register(Provider.class);
    }
    private static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(ID, new TheKindofKillDataCapability.Provider());
        }
    }

    public static void sync(Player player) {
        if(player instanceof ServerPlayer sp)
        {
            player.getCapability(CAPABILITY).ifPresent((data) -> ChannelEventTracker.sendToPlayer(new TheKindofKillDataMessage(data),sp));
        }
    }

    private static void playerClone(PlayerEvent.Clone event) {
        Player original = event.getOriginal();
        original.reviveCaps();
        original.getCapability(CAPABILITY).ifPresent((oldData) -> {
                event.getEntity().getCapability(CAPABILITY).ifPresent((newData) -> newData.setEntitylist(oldData));
        });
        original.invalidateCaps();
    }

    private static void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        sync(event.getEntity());
    }

    private static void playerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        sync(event.getEntity());
    }

    private static void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        sync(event.getEntity());
    }

    private static class Provider implements ICapabilitySerializable<CompoundTag> {
        private LivingEntityList playerkillList;
        private final LazyOptional<LivingEntityList> capability = LazyOptional.of(() -> playerkillList);

        public Provider() {
            this.playerkillList = new LivingEntityList();
        }
        @Nonnull
        public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
            return TheKindofKillDataCapability.CAPABILITY.orEmpty(cap, this.capability);
        }

        public CompoundTag serializeNBT() {
            var tag = new CompoundTag();
            playerkillList.saveNBTData(tag);
            return tag;
        }

        public void deserializeNBT(CompoundTag nbt) {
            playerkillList.loadNBTData(nbt);
        }
    }
    public static class LivingEntityList{
        private final HashSet<ResourceLocation> entitylist = new HashSet<>();
        private final HashSet<ResourceLocation> bossentitylist = new HashSet<>();
        public static final String NBT_KEY_ENTITY_LIST = "touhoutinkermodifier_entitylist";
        public static final String NBT_KEY_BOSS_LIST = "touhoutinkermodifier_entitylist_boss";
        public HashSet<ResourceLocation> getEntitylist(boolean isBoss) {
            return isBoss?bossentitylist:entitylist;
        }
        public void clearEntitylist(boolean isBoss) {
            if(isBoss) {
                this.bossentitylist.clear();
            }
            else
            {
                this.entitylist.clear();
            }
        }
        public void setEntitylist(LivingEntityList pentitylist) {
            this.entitylist.clear();
            this.bossentitylist.clear();
            this.entitylist.addAll(pentitylist.entitylist);
            this.bossentitylist.addAll(pentitylist.bossentitylist);
        }
        public void addEntity(LivingEntity entity) {
            if(entity.getType().is(Tags.EntityTypes.BOSSES))
            {
                this.bossentitylist.add(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()));
            }
            else
            {
                this.entitylist.add(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()));
            }
        }
        public boolean removeEntity(LivingEntity entity) {
            if(entity.getType().is(Tags.EntityTypes.BOSSES))
            {
                return this.bossentitylist.remove(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()));
            }
            else
            {
                return this.entitylist.remove(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()));
            }
        }
        public boolean containsEntity(LivingEntity entity) {
            if(entity.getType().is(Tags.EntityTypes.BOSSES))
            {
                return this.bossentitylist.contains(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()));
            }
            else
            {
                return this.entitylist.contains(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()));
            }
        }
        public int getCount(boolean isBoss) {
            return isBoss?this.bossentitylist.size():this.entitylist.size();
        }
        public void saveNBTData(CompoundTag tag) {
            var list = new ListTag();
            entitylist.stream()
                    .map(String::valueOf)
                    .filter(Objects::nonNull)
                    .map(StringTag::valueOf)
                    .forEach(list::add);
            tag.put(NBT_KEY_ENTITY_LIST, list);
            var list2 = new ListTag();
            bossentitylist.stream()
                    .map(String::valueOf)
                    .filter(Objects::nonNull)
                    .map(StringTag::valueOf)
                    .forEach(list2::add);
            tag.put(NBT_KEY_BOSS_LIST, list2);
        }
        public void loadNBTData(CompoundTag tag) {
            var list = tag.getList(NBT_KEY_ENTITY_LIST, Tag.TAG_STRING);
            entitylist.clear();
            list.stream()
                    .filter(Objects::nonNull)
                    .map(nbt -> (StringTag) nbt)
                    .map(StringTag::getAsString)
                    .map(ResourceLocation::parse)
                    .forEach(entitylist::add);
            var list2 = tag.getList(NBT_KEY_BOSS_LIST, Tag.TAG_STRING);
            bossentitylist.clear();
            list2.stream()
                    .filter(Objects::nonNull)
                    .map(nbt -> (StringTag) nbt)
                    .map(StringTag::getAsString)
                    .map(ResourceLocation::parse)
                    .forEach(bossentitylist::add);
        }
    }
}
