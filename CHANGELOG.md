# Changelog

All notable changes to Antidote are documented in this file.

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
