# Antidote

Antidote is an addon for Minecraft 26.3 (Fabric) that introduces craftable medicinal cures, alchemical splash potions, curse concoctions, and a village medical economy for conditions from the Disabilities mod.

## Required Dependency & Credits

Notice: Antidote requires the [Disabilities](https://modrinth.com/mod/disabilities) mod by OddSense. Antidote functions as a companion addon and will not cure or inflict anything without the base Disabilities mod installed.

All credit for the Disabilities mod and its mechanics goes to OddSense.

---

## Features

- **30 Condition-Specific Antidotes**: Each condition from the Disabilities mod has an individual drinkable antidote crafted from thematic ingredients.
- **God Antidote**: An endgame cure-all that cures all active disabilities and resets the disability timer.
- **Splash Antidotes (Positive Support)**: 31 throwable area-of-effect potions brewed with Gunpowder that cure all players in a 4-block radius on impact.
- **Corrupt Potions (Curse Alchemistry)**: 31 drinkable curse potions brewed by fermenting Antidotes with a Poisonous Potato. When consumed, permanently inflicts the disability condition on the drinker.
- **Splash Corrupt Potions (Offensive PvP Weapons)**: 31 throwable splash weapons brewed with Corrupt Potions + Gunpowder that inflict disabilities on all target players caught in the splash radius.
- **Medical Vending Machine**: A 2-block tall, coin-operated medical station found naturally in village churches featuring an active healthcare economy, live recipe library, and smart real-time search engine.
- **Vanilla Brewing Stand Integration**: 93 data-driven brewing recipes compatible with vanilla Brewing Stands.
- **Dynamic Color Tinting**: Potion items feature custom tinting matching each disability theme, with corrupted dark-purple shifts for curse variants.
- **Visual & Audio Feedback**: Custom particles, sound effects, and action bar feedback for drinking, throwing, and curing.
- **Organized Creative Tab**: All 124 items neatly grouped under the "Antidotes" creative tab.

---

## Alchemistry & Brewing System

Antidotes can be processed in a standard vanilla Brewing Stand using standard brewing mechanics (powered by Blaze Powder):

```
                       +---------------+
                       |   Antidote    |
                       +-------+-------+
                               |
              +----------------+----------------+
              |                                 |
       + Gunpowder                     + Poisonous Potato
              |                                 |
              v                                 v
   +--------------------+            +--------------------+
   |   Splash Antidote  |            |   Corrupt Potion   |
   | (AoE Positive Cure)|            | (Drinkable Curse)  |
   +--------------------+            +---------+----------+
                                               |
                                          + Gunpowder
                                               |
                                               v
                                     +--------------------+
                                     |Splash Corrupt Potion|
                                     | (AoE Curse Weapon) |
                                     +--------------------+
```

### 1. Splash Antidotes (Positive Medical Support)
- **Brewing Recipe**: Any standard Antidote + Gunpowder.
- **Mechanics**: Throw the potion; upon shattering, it creates an alchemical cloud (4-block radius) that cures that specific condition for all players caught in the splash.
- **Splash God Antidote**: Cures all 30 conditions for all players in range and resets the countdown timer.

### 2. Corrupt Potions (Curse Concoctions / Drinkable)
- **Brewing Recipe**: Any standard Antidote + Poisonous Potato.
- **Mechanics**: Drinking permanently inflicts the corresponding condition on the player via the Disabilities mod.
- **Corrupt God Potion**: Inflicts all 30 conditions simultaneously, playing sinister soul flame particles and wither sound effects.

### 3. Splash Corrupt Potions (Offensive PvP Weapons)
- **Brewing Recipe**: Any Corrupt Potion + Gunpowder.
- **Mechanics**: Throwable offensive weapon. On impact, shatters in a splash cloud that permanently inflicts the condition onto all players in range.

---

## Medical Vending Machine & Economy

The Medical Vending Machine (`antidote:medical_vending_machine`) is an uncraftable 2-block tall medical dispensary found naturally in village churches.

### Features & Capabilities

- **Natural Generation**: Spawns naturally near brewing stands inside Village Churches across Plains, Desert, Savanna, Taiga, and Snowy biomes.
- **Seamless Appliance Design**: Unified 2-block side panel with clean modern vending machine styling.
- **Iron Pickaxe Harvesting**: Requires an Iron Pickaxe or higher to collect. Mining with hand or wood/stone tools safely prevents dropping to preserve balance.
- **Real-Time Smart Search Box**:
  - Located in the top-right header of the interface.
  - Dynamically filters whatever tab is active (Antidotes, Ingredients, God Tier, and Recipe Library).
  - Cross-matches item names, condition names, descriptions/lore, and recipe ingredients (e.g. searching "Carrot" surfaces the Blindness Antidote recipe).
  - Input isolation: typing letters like `E` or `Q` will not accidentally close the inventory or drop held items.
  - Press `Escape` (when text is present) or right-click the search box to instantly clear the query.
- **Inventory Integration & Balance Scanning**: Scans your player bag directly for Emeralds and Diamonds with clear green (affordable) and red (insufficient funds) visual borders.
- **In-Game Recipe Library**: Displays all 31 antidote crafting recipes, thematic lore descriptions, visual 2x2 crafting grids, and direct purchasing.
- **Hero of the Village Raid Discounts**: Players holding the Hero of the Village effect automatically receive dynamic percentage discounts across all catalog and library items.

### Pricing Structure

| Category | Offerings | Price (Default) |
| :--- | :--- | :--- |
| Raw Ingredients | Ghast Tear, Glistering Melon, Spyglass, Honey, Slimeball, etc. | 30 - 36 Emeralds |
| Standard Antidotes | Any of the 30 standard condition antidotes | 48 Emeralds |
| God Tier | Nether Star | 100 Emeralds + 10 Diamonds |
| God Tier | Totem of Undying | 120 Emeralds + 20 Diamonds |
| God Tier | God Antidote (Full Cure-All) | 200 Emeralds + 20 Diamonds |

### Pricing Commands & Configuration (`config/antidote.json`)

Pricing is fully customizable and automatically synchronized across multiplayer networks:

- **Config File**: Located at `.minecraft/config/antidote.json`. Adjust base costs and multipliers for antidotes, ingredients, and the God Antidote.
- **In-Game Commands** (requires OP / permission level 2):
  - `/antidote price antidotes <emeraldCost>`: Set standard antidotes base cost.
  - `/antidote price ingredients <emeraldCost>`: Set raw ingredients base cost.
  - `/antidote price god_antidote <emeralds> [diamonds]`: Set God Antidote base price.
  - `/antidote multiplier <category> <value>`: Adjust percentage multiplier live.
  - `/antidote reload`: Reload configuration from disk and broadcast updates to all online clients.

---

## Antidote Reference & Crafting Recipes

All standard antidotes are shapeless crafting recipes that can be prepared in any 2x2 or 3x3 crafting grid:

| Antidote | Item ID | Ingredients (Shapeless) | Effect |
| :--- | :--- | :--- | :--- |
| God Antidote | `antidote:god_antidote` | Water Bottle, Nether Star, Totem of Undying | Cures all active disabilities & resets timer |
| Blindness Antidote | `antidote:blind_antidote` | Water Bottle, Golden Carrot, Spider Eye | Cures permanent blindness |
| Color Blindness Antidote | `antidote:color_blind_antidote` | Water Bottle, Glowstone Dust, Ink Sac | Restores vibrant color vision |
| Deafness Antidote | `antidote:deaf_antidote` | Water Bottle, Note Block, White Wool | Restores hearing and sound perception |
| Diabetes Antidote | `antidote:diabetes_antidote` | Water Bottle, Honeycomb, Apple | Balances blood sugar and food tolerance |
| Schizophrenia Antidote | `antidote:schizophrenia_antidote` | Water Bottle, Gold Nugget, Fermented Spider Eye | Clears hallucinations and mental noise |
| Muteness Antidote | `antidote:mute_antidote` | Water Bottle, Note Block, Feather | Restores voice and speech in chat |
| Seizures Antidote | `antidote:seizures_antidote` | Water Bottle, Honey Bottle, Glow Berries | Stabilizes nervous reflexes and convulsions |
| Cancer Antidote | `antidote:cancer_antidote` | Water Bottle, Glistering Melon Slice, Ghast Tear | Halts maximum health deterioration |
| OCD Antidote | `antidote:ocd_antidote` | Water Bottle, Clock, Compass | Relieves compulsive urges |
| Amputated Offhand Antidote | `antidote:amputated_offhand_antidote` | Water Bottle, Shield, Slimeball | Regenerates the offhand limb |
| Amputated Leg Antidote | `antidote:amputated_leg_antidote` | Water Bottle, Leather Boots, Slimeball | Restores natural walking and movement |
| Dyslexia Antidote | `antidote:dyslexia_antidote` | Water Bottle, Book, Glow Ink Sac | Clarifies scrambled text and symbols |
| Short Stature Antidote | `antidote:short_antidote` | Water Bottle, Bone Meal, Red Mushroom | Restores standard player height and reach |
| Dementia Antidote | `antidote:dementia_antidote` | Water Bottle, Chest, Book | Prevents inventory scrambling and lost memories |
| Asthma Antidote | `antidote:asthma_antidote` | Water Bottle, Pufferfish, Feather | Restores lung capacity and running endurance |
| Cerebral Palsy Antidote | `antidote:cerebral_palsy_antidote` | Water Bottle, Honey Bottle, Slimeball | Smoothes voluntary muscle coordination |
| Narcolepsy Antidote | `antidote:narcolepsy_antidote` | Water Bottle, Redstone Dust, Cocoa Beans | Prevents sudden sleep episodes and fatigue |
| Parkinson's Antidote | `antidote:parkinsons_antidote` | Water Bottle, Sweet Berries, Iron Ingot | Steadies shaky crosshairs and motor control |
| Pollen Allergy Antidote | `antidote:pollen_allergy_antidote` | Water Bottle, Honeycomb, Dandelion | Removes fatal allergic reaction to pollen |
| Insomnia Antidote | `antidote:insomnia_antidote` | Water Bottle, White Wool, Sweet Berries | Restores the natural ability to sleep |
| Arthritis Antidote | `antidote:arthritis_antidote` | Water Bottle, Magma Cream, Blaze Powder | Eliminates mining and attack fatigue |
| Brittle Bones Antidote | `antidote:brittle_bones_antidote` | Water Bottle, Bone Block, Iron Ingot | Reinforces skeletal durability against fall damage |
| ADHD Antidote | `antidote:adhd_antidote` | Water Bottle, Copper Ingot, Redstone Dust | Prevents involuntary hotbar switching |
| Tourette's Antidote | `antidote:tourettes_antidote` | Water Bottle, Note Block, Sugar | Calms involuntary vocal tics and sudden jerks |
| Vertigo Antidote | `antidote:vertigo_antidote` | Water Bottle, Feather, Compass | Restores balance and stops world spinning |
| Prosopagnosia Antidote | `antidote:prosopagnosia_antidote` | Water Bottle, Carved Pumpkin, Glowstone Dust | Restores facial recognition of all mobs |
| Scoliosis Antidote | `antidote:scoliosis_antidote` | Water Bottle, Bamboo, Iron Chain | Straightens permanently tilted perspective |
| Kleptomania Antidote | `antidote:kleptomania_antidote` | Water Bottle, Chest, Gold Ingot | Restores the ability to discard and drop items |
| Lactose Intolerance Antidote | `antidote:lactose_intolerance_antidote` | Water Bottle, Milk Bucket, Apple | Prevents fatal reaction to dairy and milk |
| Anosognosia Antidote | `antidote:anosognosia_antidote` | Water Bottle, Spyglass, Glowstone Dust | Restores awareness of active conditions |

---

## Installation

### Requirements

- **Minecraft**: 26.3
- **Fabric Loader**: 0.20.1 or higher
- **Fabric API**
- **Disabilities Mod**: 0.5.0+26.3 or higher
- **Java**: 25

### Singleplayer / Client

1. Download `antidote-1.2.0.jar` from GitHub Releases.
2. Ensure Fabric Loader 26.3, Fabric API, and the Disabilities mod are placed in your `.minecraft/mods` directory.
3. Place `antidote-1.2.0.jar` into your `.minecraft/mods` directory.

### Dedicated Server

1. Install Fabric Loader on your dedicated server for Minecraft 26.3.
2. Place Fabric API, Disabilities mod jar, and `antidote-1.2.0.jar` into the server's `mods` directory.
3. Antidote must be installed on both the server and connecting clients for complete synchronization of models, networking, translations, and particles.

---

## Building from Source

To compile the mod from source, ensure Java 25 JDK is installed:

```bash
git clone https://github.com/leanwell/antidote-mod.git
cd antidote-mod
./gradlew build
```

The compiled mod JAR will be located in `build/libs/antidote-1.2.0.jar`.

---

## License

This project is licensed under the terms of the GNU General Public License v3.0 (GPL-3.0). See the [LICENSE](LICENSE) file for details.
