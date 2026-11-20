# Changelog

All notable changes to Antidote are documented in this file.

## [1.1.2] - 2026-11-21

### Added
- **Configurable Vending Pricing (`config/antidote.json`)**:
  - Full configuration file support for adjusting pricing percentages and base costs.
  - Dedicated configuration for the God Antidote (separate base Emerald cost, base Diamond cost, and percentage multiplier).
  - Configurable category base prices and percentage multipliers for Antidotes, Raw Ingredients, and Other God Tier items (Nether Star, Totem of Undying).
- **In-Game Price Management Commands**:
  - Added `/antidote price <category> emerald <cost>` and `/antidote price god diamond <cost>` to adjust base prices on the fly.
  - Added `/antidote multiplier <category> <percentage>` to tweak percentage multipliers live.
  - Added `/antidote reload` (requires OP / permission level 2) to reload `config/antidote.json` and sync with all players.
- **Raid Victory Discount (Hero of the Village)**:
  - Players bearing the Hero of the Village effect now receive dynamic discounts across the vending catalog (configurable discount per effect level, up to a configurable cap).
  - Discount is visually applied to both the vending catalog and the Recipe Library buy interface.
- **Multiplayer Network Synchronization**:
  - Server automatically synchronizes pricing configuration to connected clients via a custom Fabric network packet (`antidote:sync_config`).
- **Dynamic Recipe Library Pricing**:
  - The in-game Recipe Library buy button now dynamically displays exact multi-currency costs (e.g. `Buy (200E, 20D)` vs. `Buy (48E)`) and validates both Emerald and Diamond balances.

### Changed
- **Seamless Straight Side Texture**:
  - Redesigned the 2-block side textures (`medical_vending_machine_top_side.png` and `medical_vending_machine_bottom_side.png`) into a single continuous straight panel.
  - Removed the middle dividing seam and repetitive stacked symbols, making the block look like a sleek unified appliance. Front textures remain untouched.

### Fixed
- **Loot Table & Duplication Glitch**: Fixed double drop duplication glitch when broken with an iron pickaxe by migrating loot table condition to Minecraft 26.3 `minecraft:match_block` syntax.
- **Hand Mining Drop Glitch**: Fixed the machine dropping an item entity when broken with bare hands or insufficient tools (wood/stone pickaxes) by checking tool harvest requirements in `playerWillDestroy()`.

---

## [1.1.1] - 2026-11-20

### Fixed
- **Village Church Generation**: Fixed Medical Vending Machine not naturally spawning inside village churches in new chunks. Implemented a structure piece placement mixin (`PoolElementStructurePieceMixin`) that detects village church templates (`plains_temple_3`, `desert_temple_1`, etc.) across all village biomes, locates the brewing stand altar, and intelligently places the 2-block machine on solid floor against the wall with proper orientation.

---

## [1.1.0] - 2026-11-20

### Added
- **2-Block Medical Vending Machine**: A new 2-block tall medical station generating naturally inside village churches across all village biomes (Plains, Desert, Savanna, Taiga, and Snowy). Mineable with Iron Pickaxe or higher.
- **Direct Inventory Checkout**: Replaced coin deposit slots with seamless direct inventory scanning. When purchasing, currency (Emeralds and Diamonds) is automatically deducted from the player's inventory bag.
- **Affordability Indicators**: Real-time visual feedback in the shop grid displaying green borders for affordable items and red borders with subtle tints for items lacking currency.
- **Transaction Confirmation Pane**: Selecting an item from the catalog displays a detailed preview pane showing currency costs, selected item, and an explicit "Buy" button.
- **In-Game Recipe Library**: A dedicated 4th tab inside the vending machine menu showcasing crafting recipes, thematic lore descriptions, and direct purchasing for all 31 antidotes.
- **Audio & Mechanical Feedback**: Sound effects for purchase success and dispensing. If the player's inventory is full, dispensed items drop safely at their feet.
- **Custom Visuals & Iconography**: Dedicated 16x16 pixel-art item sprite, redesigned 2-block front/side/top/bottom textures, and high-resolution mod icon.

### Changed
- Refactored UI typography across all screens to solid black without drop shadows for cleaner readability.
- Renamed "Shapeless Recipe" in the Library view to "Recipe".
- Enabled automatic word-wrapping for multi-line lore descriptions in the Recipe Library panel.

### Fixed
- Fixed unlocalized item name tooltip (`item.antidote.medical_vending_machine`).
- Fixed multi-block break and place synchronization to prevent item duplication.

---

## [1.0.0] - 2026-11-19

### Added
- Initial release for Minecraft 26.3 on Fabric.
- 30 condition-specific antidotes for the Disabilities mod.
- God Antidote endgame cure-all.
- Dynamic color tinting for potion bottles.
- Empty bottle recycling on drink consumption.
- Audio and visual effects upon curing disabilities.
- Dedicated "Antidotes" creative tab.
