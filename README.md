# Antidote

Antidote is an addon for Minecraft 26.3 (Fabric) that introduces craftable medicinal cures for conditions from the Disabilities mod.

## Required Dependency & Credits

Notice: Antidote requires the [Disabilities](https://modrinth.com/mod/disabilities) mod by OddSense. Antidote functions as a companion addon and will not cure anything without the base Disabilities mod installed.

All credit for the Disabilities mod and its mechanics goes to OddSense.

## Features

- 30 Condition-Specific Antidotes: Each condition from the Disabilities mod has an individual antidote crafted from thematic ingredients.
- God Antidote: An endgame cure-all that cures all active disabilities and resets the timer.
- Dynamic Color Tinting: Antidotes use an item color layer system matching the theme of each condition.
- Empty Bottle Recycling: Drinking an antidote returns an empty glass bottle to the player's inventory.
- Visual & Audio Feedback: Consuming an antidote triggers particle bursts, sound cues, and an action bar confirmation message.
- Dedicated Creative Tab: All 31 antidotes are neatly organized under the "Antidotes" creative tab.

## Antidote Reference & Recipes

All recipes are shapeless and can be crafted in any crafting grid or inventory crafting slot.

| Antidote | Item ID | Ingredients (Shapeless) | Effect |
| :--- | :--- | :--- | :--- |
| God Antidote | `antidote:god_antidote` | Water Bottle, Nether Star, Totem of Undying | Cures all active disabilities and resets the timer |
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

## Installation

### Requirements

- Minecraft 26.3
- Fabric Loader 0.20.1 or higher
- Fabric API
- Disabilities 0.5.0+26.3 or higher
- Java 25

### Singleplayer / Client

1. Download `antidote-<version>.jar` from GitHub Releases.
2. Ensure Fabric Loader 26.3 and Fabric API are installed.
3. Ensure the Disabilities mod (`disabilities-<version>.jar`) is placed in your `.minecraft/mods` directory.
4. Place `antidote-<version>.jar` into your `.minecraft/mods` directory.

### Dedicated Server

1. Install Fabric Loader on your dedicated server for Minecraft 26.3.
2. Place Fabric API, the Disabilities mod jar, and `antidote-<version>.jar` into the server's `mods` directory.
3. Note: Antidote must be installed on both the server and connecting clients to provide synchronized item models, translations, and sound/particle effects.

## Building from Source

To compile the mod from source, ensure Java 25 JDK is installed:

```bash
git clone https://github.com/leanwell/antidote-mod.git
cd antidote-mod
./gradlew build
```

The compiled mod JAR will be located in `build/libs/`.

## License

This project is licensed under the terms of the GNU General Public License v3.0 (GPL-3.0). See the [LICENSE](LICENSE) file for details.
