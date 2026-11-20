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
    double ingredientMultiplier,
    double godTierMultiplier
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
            config.ingredients != null ? config.ingredients.priceMultiplier : 1.0,
            config.godTierItems != null ? config.godTierItems.priceMultiplier : 1.0
        );
    }

    public AntidoteConfig toConfig() {
        AntidoteConfig config = new AntidoteConfig();
        config.antidotes.baseEmeraldCost = this.antidoteBaseCost;
        config.antidotes.priceMultiplier = this.antidoteMultiplier;
        config.godAntidote.baseEmeraldCost = this.godAntidoteBaseCost;
        config.godAntidote.baseDiamondCost = this.godAntidoteDiamondCost;
        config.godAntidote.priceMultiplier = this.godAntidoteMultiplier;
        config.ingredients.priceMultiplier = this.ingredientMultiplier;
        config.godTierItems.priceMultiplier = this.godTierMultiplier;
        return config;
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(antidoteBaseCost);
        buf.writeDouble(antidoteMultiplier);
        buf.writeVarInt(godAntidoteBaseCost);
        buf.writeVarInt(godAntidoteDiamondCost);
        buf.writeDouble(godAntidoteMultiplier);
        buf.writeDouble(ingredientMultiplier);
        buf.writeDouble(godTierMultiplier);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
