package com.goldkl.touhoutinkermodifier.entity.spell.fire.masterspark;

import com.goldkl.touhoutinkermodifier.TouhouTinkerModifier;
import com.goldkl.touhoutinkermodifier.registries.EntitiesRegistry;
import com.goldkl.touhoutinkermodifier.registries.SpellsRegistry;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;


public class MasterSparkVisualEntity extends Entity implements IEntityAdditionalSpawnData, TraceableEntity {

    public static final int lifetime = 100;

    public MasterSparkVisualEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {

    }
    @Nullable
    public Entity getOwner() {
        if (this.cachedOwner != null && !this.cachedOwner.isRemoved()) {
            return this.cachedOwner;
        } else if (this.ownerUUID != null && this.level() instanceof ServerLevel) {
            this.cachedOwner = ((ServerLevel)this.level()).getEntity(this.ownerUUID);
            return this.cachedOwner;
        } else {
            return null;
        }
    }
    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    public void setOwner(@Nullable Entity p_37263_) {
        if (p_37263_ != null) {
            this.ownerUUID = p_37263_.getUUID();
            this.cachedOwner = p_37263_;
        }

    }
    @Nullable
    private Entity cachedOwner;
    @Nullable
    private UUID ownerUUID;

    public float distance;

    public MasterSparkVisualEntity(Level level, float distance, LivingEntity owner) {
        super(EntitiesRegistry.MasterSparkVisualEntity.get(), level);
        this.distance = distance;
        this.setPos(owner.getEyePosition().subtract(0, .75f, 0));
        this.setRot(owner.getYRot(), owner.getXRot());
        this.setOwner(owner);
    }

    @Override
    public void tick() {
        if(getOwner() == null){
            this.discard();
        }
        else{
            this.setPos(this.getOwner().getEyePosition().subtract(0, .75f, 0));
            this.setRot(this.getOwner().getYRot(), this.getOwner().getXRot());
            /*if (tickCount % 10 == 0) {
                if (level().isClientSide) {
                    var forward = getForward();
                    for (float i = 1; i < distance; i += .5f) {
                        Vec3 pos = position().add(forward.scale(i));
                        level().addParticle(ParticleTypes.ELECTRIC_SPARK, false, pos.x, pos.y + .5, pos.z, 0, 0, 0);
                    }
                }
            }*/
            if (tickCount > lifetime) {
                this.discard();
            }
            if(!this.level().isClientSide)
            {
                if(getOwner() instanceof LivingEntity lv ){
                    SpellData data = MagicData.getPlayerMagicData(lv).getCastingSpell();
                    if(data.getSpell() != SpellsRegistry.masterspark.get()){
                        this.discard();
                    }
                }
                else {
                    this.discard();
                }
            }
        }
    }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }


    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt((int) (distance * 10));
        Entity entity = this.getOwner();
        buffer.writeInt(entity == null ? 0 : entity.getId());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.distance = additionalData.readInt() / 10f;
        Entity entity = this.level().getEntity(additionalData.readInt());
        if (entity != null) {
            this.setOwner(entity);
        }
    }
}
