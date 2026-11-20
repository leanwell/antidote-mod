package com.leanwell.antidote.network;

import com.leanwell.antidote.Antidote;
import com.leanwell.antidote.config.AntidoteConfig;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record SyncConfigPayload(
    int antidoteBaseCost,
    double antidoteMultiplier,
    int godAntidoteBaseCost,
    int godAntidoteDiamondCost,
    double godAntidoteMultiplier,
    int ingredientBaseCost,
    double ingredientMultiplier,
    double godTierMultiplier,
    boolean raidDiscountEnabled,
    double discountPercentPerLevel,
    double maxDiscountPercent
) implements CustomPacketPayload {
    public static final Type<SyncConfigPayload> TYPE = new Type<>(Antidote.id("sync_config"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncConfigPayload> CODEC = CustomPacketPayload.codec(
        SyncConfigPayload::write,
        SyncConfigPayload::new
    );

    public SyncConfigPayload(RegistryFriendlyByteBuf buf) {
        this(
            buf.readVarInt(),
            buf.readDouble(),
            buf.readVarInt(),
            buf.readVarInt(),
            buf.readDouble(),
            buf.readVarInt(),
            buf.readDouble(),
            buf.readDouble(),
            buf.readBoolean(),
            buf.readDouble(),
            buf.readDouble()
        );
    }

    public static SyncConfigPayload fromConfig(AntidoteConfig config) {
        return new SyncConfigPayload(
            config.antidotes != null ? config.antidotes.baseEmeraldCost : 48,
            config.antidotes != null ? config.antidotes.priceMultiplier : 1.0,
            config.godAntidote != null ? config.godAntidote.baseEmeraldCost : 200,
            config.godAntidote != null ? config.godAntidote.baseDiamondCost : 20,
            config.godAntidote != null ? config.godAntidote.priceMultiplier : 1.0,
            config.ingredients != null ? config.ingredients.baseEmeraldCost : 32,
            config.ingredients != null ? config.ingredients.priceMultiplier : 1.0,
            config.godTierItems != null ? config.godTierItems.priceMultiplier : 1.0,
            config.raidDiscount != null && config.raidDiscount.enabled,
            config.raidDiscount != null ? config.raidDiscount.discountPercentPerLevel : 20.0,
            config.raidDiscount != null ? config.raidDiscount.maxDiscountPercent : 60.0
        );
    }

    public AntidoteConfig toConfig() {
        AntidoteConfig config = new AntidoteConfig();
        config.antidotes.baseEmeraldCost = this.antidoteBaseCost;
        config.antidotes.priceMultiplier = this.antidoteMultiplier;
        config.godAntidote.baseEmeraldCost = this.godAntidoteBaseCost;
        config.godAntidote.baseDiamondCost = this.godAntidoteDiamondCost;
        config.godAntidote.priceMultiplier = this.godAntidoteMultiplier;
        config.ingredients.baseEmeraldCost = this.ingredientBaseCost;
        config.ingredients.priceMultiplier = this.ingredientMultiplier;
        config.godTierItems.priceMultiplier = this.godTierMultiplier;
        config.raidDiscount.enabled = this.raidDiscountEnabled;
        config.raidDiscount.discountPercentPerLevel = this.discountPercentPerLevel;
        config.raidDiscount.maxDiscountPercent = this.maxDiscountPercent;
        return config;
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(antidoteBaseCost);
        buf.writeDouble(antidoteMultiplier);
        buf.writeVarInt(godAntidoteBaseCost);
        buf.writeVarInt(godAntidoteDiamondCost);
        buf.writeDouble(godAntidoteMultiplier);
        buf.writeVarInt(ingredientBaseCost);
        buf.writeDouble(ingredientMultiplier);
        buf.writeDouble(godTierMultiplier);
        buf.writeBoolean(raidDiscountEnabled);
        buf.writeDouble(discountPercentPerLevel);
        buf.writeDouble(maxDiscountPercent);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
