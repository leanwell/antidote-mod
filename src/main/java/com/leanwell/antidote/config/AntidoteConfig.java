package com.leanwell.antidote.config;

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

    public static class CategoryMultiplierConfig {
        public double priceMultiplier = 1.0;
    }

    public AntidotesConfig antidotes = new AntidotesConfig();
    public GodAntidoteConfig godAntidote = new GodAntidoteConfig();
    public CategoryMultiplierConfig ingredients = new CategoryMultiplierConfig();
    public CategoryMultiplierConfig godTierItems = new CategoryMultiplierConfig();

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
        double mult = ingredients != null ? Math.max(0.01, ingredients.priceMultiplier) : 1.0;
        return Math.max(1, (int) Math.round(defaultCost * mult));
    }

    public int calculateGodTierCost(int defaultCost) {
        double mult = godTierItems != null ? Math.max(0.0, godTierItems.priceMultiplier) : 1.0;
        return Math.max(0, (int) Math.round(defaultCost * mult));
    }
}
