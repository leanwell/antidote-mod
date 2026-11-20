package com.leanwell.antidote.config;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class AntidoteConfig {
    public static class AntidotesConfig {
        public int baseEmeraldCost = 48;
        public double priceMultiplier = 1.0;
    }

    public static class GodAntidoteConfig {
        public int baseEmeraldCost = 200;
        public int baseDiamondCost = 20;
        public double priceMultiplier = 1.0;
    }

    public static class IngredientsConfig {
        public int baseEmeraldCost = 32;
        public double priceMultiplier = 1.0;
    }

    public static class CategoryMultiplierConfig {
        public double priceMultiplier = 1.0;
    }

    public static class RaidDiscountConfig {
        public boolean enabled = true;
        public double discountPercentPerLevel = 20.0;
        public double maxDiscountPercent = 60.0;
    }

    public AntidotesConfig antidotes = new AntidotesConfig();
    public GodAntidoteConfig godAntidote = new GodAntidoteConfig();
    public IngredientsConfig ingredients = new IngredientsConfig();
    public CategoryMultiplierConfig godTierItems = new CategoryMultiplierConfig();
    public RaidDiscountConfig raidDiscount = new RaidDiscountConfig();

    public int getAntidoteEmeraldCost() {
        int base = antidotes != null ? antidotes.baseEmeraldCost : 48;
        double mult = antidotes != null ? Math.max(0.01, antidotes.priceMultiplier) : 1.0;
        return Math.max(1, (int) Math.round(base * mult));
    }

    public int getGodAntidoteEmeraldCost() {
        int base = godAntidote != null ? godAntidote.baseEmeraldCost : 200;
        double mult = godAntidote != null ? Math.max(0.01, godAntidote.priceMultiplier) : 1.0;
        return Math.max(1, (int) Math.round(base * mult));
    }

    public int getGodAntidoteDiamondCost() {
        int base = godAntidote != null ? godAntidote.baseDiamondCost : 20;
        double mult = godAntidote != null ? Math.max(0.0, godAntidote.priceMultiplier) : 1.0;
        return Math.max(0, (int) Math.round(base * mult));
    }

    public int calculateIngredientCost(int defaultCost) {
        int base = ingredients != null ? ingredients.baseEmeraldCost : 32;
        double ratio = defaultCost > 32 ? (double) defaultCost / 32.0 : 1.0;
        double mult = ingredients != null ? Math.max(0.01, ingredients.priceMultiplier) : 1.0;
        return Math.max(1, (int) Math.round(base * ratio * mult));
    }

    public int calculateGodTierCost(int defaultCost) {
        double mult = godTierItems != null ? Math.max(0.0, godTierItems.priceMultiplier) : 1.0;
        return Math.max(0, (int) Math.round(defaultCost * mult));
    }

    public double getHeroDiscount(LivingEntity player) {
        if (player == null || raidDiscount == null || !raidDiscount.enabled) {
            return 0.0;
        }
        MobEffectInstance effect = player.getEffect(MobEffects.HERO_OF_THE_VILLAGE);
        if (effect == null) {
            return 0.0;
        }
        int level = effect.getAmplifier() + 1;
        double percent = Math.min(raidDiscount.maxDiscountPercent, level * raidDiscount.discountPercentPerLevel);
        return Math.max(0.0, percent / 100.0);
    }

    public int getDiscountedEmeralds(int originalCost, double discount) {
        if (originalCost <= 0 || discount <= 0.0) return originalCost;
        return Math.max(1, (int) Math.round(originalCost * (1.0 - discount)));
    }

    public int getDiscountedDiamonds(int originalDiamonds, double discount) {
        if (originalDiamonds <= 0 || discount <= 0.0) return originalDiamonds;
        return Math.max(0, (int) Math.round(originalDiamonds * (1.0 - discount)));
    }
}
