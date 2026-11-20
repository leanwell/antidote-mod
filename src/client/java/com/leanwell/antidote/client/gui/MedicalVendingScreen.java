package com.leanwell.antidote.client.gui;

import com.leanwell.antidote.screen.MedicalVendingScreenHandler;
import com.leanwell.antidote.store.MedicalLibrary;
import com.leanwell.antidote.store.VendingCatalog;
import com.leanwell.antidote.store.VendingProduct;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class MedicalVendingScreen extends AbstractContainerScreen<MedicalVendingScreenHandler> {
    private enum ActiveTab {
        ANTIDOTES,
        INGREDIENTS,
        GOD_TIER,
        LIBRARY
    }

    private ActiveTab activeTab = ActiveTab.ANTIDOTES;
    private int scrollOffset = 0;
    private int libraryScrollOffset = 0;
    private int selectedLibraryIndex = 0;

    private static final int CATALOG_COLS = 7;
    private static final int CATALOG_ROWS = 4;
    private static final int CELL_SIZE = 22;

    public MedicalVendingScreen(MedicalVendingScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title, 256, 230);
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelX = 48;
        this.inventoryLabelY = 134;
    }

    @Override
    protected void init() {
        super.init();

        int tabY = this.topPos + 18;

        this.addRenderableWidget(Button.builder(Component.literal("Antidotes"), b -> {
            this.activeTab = ActiveTab.ANTIDOTES;
            this.scrollOffset = 0;
        }).bounds(this.leftPos + 8, tabY, 52, 16).build());

        this.addRenderableWidget(Button.builder(Component.literal("Ingredients"), b -> {
            this.activeTab = ActiveTab.INGREDIENTS;
            this.scrollOffset = 0;
        }).bounds(this.leftPos + 62, tabY, 62, 16).build());

        this.addRenderableWidget(Button.builder(Component.literal("God Tier"), b -> {
            this.activeTab = ActiveTab.GOD_TIER;
            this.scrollOffset = 0;
        }).bounds(this.leftPos + 126, tabY, 50, 16).build());

        this.addRenderableWidget(Button.builder(Component.literal("Library"), b -> {
            this.activeTab = ActiveTab.LIBRARY;
        }).bounds(this.leftPos + 178, tabY, 48, 16).build());

        this.addRenderableWidget(Button.builder(Component.literal("Eject Coins"), b -> {
            if (this.minecraft != null && this.minecraft.gameMode != null) {
                this.minecraft.gameMode.handleInventoryButtonClick(
                    this.menu.containerId,
                    MedicalVendingScreenHandler.EJECT_BUTTON_ID
                );
            }
        }).bounds(this.leftPos + 176, this.topPos + 96, 68, 18).build());
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);

        int left = this.leftPos;
        int top = this.topPos;

        // Main Container Background (classic Minecraft stone-gray container palette)
        graphics.fill(RenderPipelines.GUI, left, top, left + this.imageWidth, top + this.imageHeight, 0xFFC6C6C6);
        // Bevel borders
        graphics.fill(RenderPipelines.GUI, left, top, left + this.imageWidth, top + 1, 0xFFFFFFFF);
        graphics.fill(RenderPipelines.GUI, left, top, left + 1, top + this.imageHeight, 0xFFFFFFFF);
        graphics.fill(RenderPipelines.GUI, left + this.imageWidth - 1, top, left + this.imageWidth, top + this.imageHeight, 0xFF555555);
        graphics.fill(RenderPipelines.GUI, left, top + this.imageHeight - 1, left + this.imageWidth, top + this.imageHeight, 0xFF555555);

        // Player Inventory Slots (3 rows x 9 cols) at x=48, y=146
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                drawRecessedSlot(graphics, left + 47 + col * 18, top + 145 + row * 18);
            }
        }

        // Player Hotbar Slots (1 row x 9 cols) at x=48, y=204
        for (int col = 0; col < 9; ++col) {
            drawRecessedSlot(graphics, left + 47 + col * 18, top + 203);
        }

        if (this.activeTab == ActiveTab.LIBRARY) {
            renderLibraryView(graphics, left, top, mouseX, mouseY);
        } else {
            renderStoreView(graphics, left, top, mouseX, mouseY);
        }
    }

    private void renderStoreView(GuiGraphicsExtractor graphics, int left, int top, int mouseX, int mouseY) {
        // Catalog Panel on Left
        int catalogX = left + 8;
        int catalogY = top + 36;
        int catalogWidth = CATALOG_COLS * CELL_SIZE + 4;
        int catalogHeight = CATALOG_ROWS * CELL_SIZE + 4;
        graphics.fill(RenderPipelines.GUI, catalogX, catalogY, catalogX + catalogWidth, catalogY + catalogHeight, 0xFF8B8B8B);
        drawRecessedPanel(graphics, catalogX, catalogY, catalogWidth, catalogHeight);

        // Render catalog items
        VendingProduct.Category cat = switch (this.activeTab) {
            case INGREDIENTS -> VendingProduct.Category.INGREDIENTS;
            case GOD_TIER -> VendingProduct.Category.GOD_TIER;
            default -> VendingProduct.Category.ANTIDOTES;
        };

        List<VendingProduct> products = VendingCatalog.getByCategory(cat);
        int startIndex = this.scrollOffset * CATALOG_COLS;

        int invEm = this.minecraft != null && this.minecraft.player != null
            ? MedicalVendingScreenHandler.countPlayerItem(this.minecraft.player, Items.EMERALD) : 0;
        int invDia = this.minecraft != null && this.minecraft.player != null
            ? MedicalVendingScreenHandler.countPlayerItem(this.minecraft.player, Items.DIAMOND) : 0;
        int totalEm = this.menu.getEmeraldBuffer() + invEm;
        int totalDia = this.menu.getDiamondBuffer() + invDia;
        boolean isCreative = this.minecraft != null && this.minecraft.player != null
            && this.minecraft.player.hasInfiniteMaterials();

        for (int i = 0; i < CATALOG_COLS * CATALOG_ROWS; ++i) {
            int productIndex = startIndex + i;
            if (productIndex >= products.size()) break;

            VendingProduct product = products.get(productIndex);
            int col = i % CATALOG_COLS;
            int row = i / CATALOG_COLS;
            int cellX = catalogX + 2 + col * CELL_SIZE;
            int cellY = catalogY + 2 + row * CELL_SIZE;

            boolean canAfford = isCreative || (totalEm >= product.getEmeraldCost() && totalDia >= product.getDiamondCost());

            // Draw slot box
            drawRecessedSlot(graphics, cellX + 2, cellY + 2);
            if (canAfford) {
                // Subtle green highlight border for affordable items
                graphics.fill(RenderPipelines.GUI, cellX + 1, cellY + 1, cellX + 21, cellY + 2, 0xFF00AA00);
                graphics.fill(RenderPipelines.GUI, cellX + 1, cellY + 20, cellX + 21, cellY + 21, 0xFF00AA00);
                graphics.fill(RenderPipelines.GUI, cellX + 1, cellY + 1, cellX + 2, cellY + 21, 0xFF00AA00);
                graphics.fill(RenderPipelines.GUI, cellX + 20, cellY + 1, cellX + 21, cellY + 21, 0xFF00AA00);
            }

            graphics.item(product.getItemStack(), cellX + 3, cellY + 3);
            graphics.itemDecorations(this.font, product.getItemStack(), cellX + 3, cellY + 3);
        }

        // Right-Side Payment & Output Panel
        drawRecessedSlot(graphics, left + 177, top + 41); // Slot 0: Emeralds (178, 42)
        drawRecessedSlot(graphics, left + 213, top + 41); // Slot 1: Diamonds (214, 42)
        drawRecessedSlot(graphics, left + 195, top + 69); // Slot 2: Output Chute (196, 70)

        // Ghost item previews if slots are empty
        if (this.menu.getSlot(0).getItem().isEmpty()) {
            graphics.item(new ItemStack(Items.EMERALD), left + 178, top + 42);
        }
        if (this.menu.getSlot(1).getItem().isEmpty()) {
            graphics.item(new ItemStack(Items.DIAMOND), left + 214, top + 42);
        }
    }

    private void renderLibraryView(GuiGraphicsExtractor graphics, int left, int top, int mouseX, int mouseY) {
        // Library Left List (antidotes selector)
        int listX = left + 8;
        int listY = top + 36;
        drawRecessedPanel(graphics, listX, listY, 52, 94);

        List<MedicalLibrary.RecipeEntry> entries = MedicalLibrary.getEntries();
        int startIndex = this.libraryScrollOffset * 2;

        for (int i = 0; i < 8; ++i) {
            int entryIndex = startIndex + i;
            if (entryIndex >= entries.size()) break;

            MedicalLibrary.RecipeEntry entry = entries.get(entryIndex);
            int col = i % 2;
            int row = i / 2;
            int slotX = listX + 4 + col * 22;
            int slotY = listY + 4 + row * 22;

            drawRecessedSlot(graphics, slotX, slotY);
            if (entryIndex == this.selectedLibraryIndex) {
                // Gold highlight on selected item
                graphics.fill(RenderPipelines.GUI, slotX, slotY, slotX + 18, slotY + 1, 0xFFFFDF00);
                graphics.fill(RenderPipelines.GUI, slotX, slotY + 17, slotX + 18, slotY + 18, 0xFFFFDF00);
                graphics.fill(RenderPipelines.GUI, slotX, slotY, slotX + 1, slotY + 18, 0xFFFFDF00);
                graphics.fill(RenderPipelines.GUI, slotX + 17, slotY, slotX + 18, slotY + 18, 0xFFFFDF00);
            }

            ItemStack stack = new ItemStack(entry.antidote());
            graphics.item(stack, slotX + 1, slotY + 1);
        }

        // Library Right Display Panel (Recipe & Info)
        int infoX = left + 64;
        int infoY = top + 36;
        int infoW = 184;
        int infoH = 94;
        drawRecessedPanel(graphics, infoX, infoY, infoW, infoH);

        if (this.selectedLibraryIndex >= 0 && this.selectedLibraryIndex < entries.size()) {
            MedicalLibrary.RecipeEntry selected = entries.get(this.selectedLibraryIndex);

            // Display Title & Lore
            graphics.textRenderer().accept(
                infoX + 6,
                infoY + 6,
                Component.literal(selected.conditionName() + " Antidote").withStyle(ChatFormatting.DARK_AQUA, ChatFormatting.BOLD)
            );

            graphics.textRenderer().accept(
                infoX + 6,
                infoY + 18,
                Component.literal(selected.lore()).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)
            );

            // Crafting Grid Title
            graphics.textRenderer().accept(
                infoX + 6,
                infoY + 34,
                Component.literal("Shapeless Recipe:").withStyle(ChatFormatting.GOLD)
            );

            // Visual 2x2 Crafting Display
            int gridX = infoX + 8;
            int gridY = infoY + 46;

            for (int r = 0; r < 2; ++r) {
                for (int c = 0; c < 2; ++c) {
                    drawRecessedSlot(graphics, gridX + c * 18, gridY + r * 18);
                }
            }

            // Slot 0: Water bottle
            if (!selected.ingredients().isEmpty()) {
                graphics.item(new ItemStack(selected.ingredients().get(0)), gridX + 1, gridY + 1);
            }
            // Slot 1: Ingredient 1
            if (selected.ingredients().size() > 1) {
                graphics.item(new ItemStack(selected.ingredients().get(1)), gridX + 19, gridY + 1);
            }
            // Slot 2: Ingredient 2
            if (selected.ingredients().size() > 2) {
                graphics.item(new ItemStack(selected.ingredients().get(2)), gridX + 1, gridY + 19);
            }

            // Crafting Arrow -> Result
            graphics.textRenderer().accept(
                gridX + 44,
                gridY + 14,
                Component.literal("->").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.BOLD)
            );

            int resultX = gridX + 66;
            int resultY = gridY + 10;
            drawRecessedSlot(graphics, resultX, resultY);
            graphics.item(new ItemStack(selected.antidote()), resultX + 1, resultY + 1);

            // Quick Buy Notice
            graphics.textRenderer().accept(
                resultX + 24,
                resultY + 4,
                Component.literal("[Click to Buy]").withStyle(ChatFormatting.DARK_GREEN)
            );
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.textRenderer().accept(
            this.titleLabelX,
            this.titleLabelY,
            this.title.copy().withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
        );

        int invEm = this.minecraft != null && this.minecraft.player != null
            ? MedicalVendingScreenHandler.countPlayerItem(this.minecraft.player, Items.EMERALD) : 0;
        int invDia = this.minecraft != null && this.minecraft.player != null
            ? MedicalVendingScreenHandler.countPlayerItem(this.minecraft.player, Items.DIAMOND) : 0;

        graphics.textRenderer().accept(
            this.inventoryLabelX,
            this.inventoryLabelY,
            Component.literal("Inventory (Bag: " + invEm + "E " + invDia + "D)").withStyle(ChatFormatting.DARK_GRAY)
        );

        if (this.activeTab != ActiveTab.LIBRARY) {
            int emCredit = this.menu.getEmeraldBuffer();
            int diaCredit = this.menu.getDiamondBuffer();

            // Deposit Header
            graphics.textRenderer().accept(
                174,
                26,
                Component.literal("Deposit:").withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.BOLD)
            );

            // Emerald buffer text below slot 0
            graphics.textRenderer().accept(
                174,
                59,
                Component.literal("" + emCredit + "E").withStyle(ChatFormatting.DARK_GREEN, ChatFormatting.BOLD)
            );

            // Diamond buffer text below slot 1
            graphics.textRenderer().accept(
                210,
                59,
                Component.literal("" + diaCredit + "D").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD)
            );

            // Output chute label next to slot 2
            graphics.textRenderer().accept(
                174,
                74,
                Component.literal("Out:").withStyle(ChatFormatting.GOLD)
            );

            // Catalog item tooltips
            renderCatalogTooltips(graphics, mouseX, mouseY);
        } else {
            renderLibraryTooltips(graphics, mouseX, mouseY);
        }
    }

    private void renderCatalogTooltips(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        int left = this.leftPos;
        int top = this.topPos;
        int catalogX = left + 8;
        int catalogY = top + 36;

        VendingProduct.Category cat = switch (this.activeTab) {
            case INGREDIENTS -> VendingProduct.Category.INGREDIENTS;
            case GOD_TIER -> VendingProduct.Category.GOD_TIER;
            default -> VendingProduct.Category.ANTIDOTES;
        };

        List<VendingProduct> products = VendingCatalog.getByCategory(cat);
        int startIndex = this.scrollOffset * CATALOG_COLS;

        int invEm = this.minecraft != null && this.minecraft.player != null
            ? MedicalVendingScreenHandler.countPlayerItem(this.minecraft.player, Items.EMERALD) : 0;
        int invDia = this.minecraft != null && this.minecraft.player != null
            ? MedicalVendingScreenHandler.countPlayerItem(this.minecraft.player, Items.DIAMOND) : 0;
        int totalEm = this.menu.getEmeraldBuffer() + invEm;
        int totalDia = this.menu.getDiamondBuffer() + invDia;
        boolean isCreative = this.minecraft != null && this.minecraft.player != null
            && this.minecraft.player.hasInfiniteMaterials();

        for (int i = 0; i < CATALOG_COLS * CATALOG_ROWS; ++i) {
            int productIndex = startIndex + i;
            if (productIndex >= products.size()) break;

            VendingProduct product = products.get(productIndex);
            int col = i % CATALOG_COLS;
            int row = i / CATALOG_COLS;
            int cellX = catalogX + 2 + col * CELL_SIZE;
            int cellY = catalogY + 2 + row * CELL_SIZE;

            if (mouseX >= cellX && mouseX < cellX + CELL_SIZE && mouseY >= cellY && mouseY < cellY + CELL_SIZE) {
                List<Component> tooltip = new ArrayList<>();
                ItemStack itemStack = product.getItemStack();
                tooltip.add(itemStack.getHoverName());
                tooltip.add(Component.literal(product.getDescription()).withStyle(ChatFormatting.GRAY));

                StringBuilder costStr = new StringBuilder("Price: ");
                if (product.getEmeraldCost() > 0) costStr.append(product.getEmeraldCost()).append(" Emeralds");
                if (product.getDiamondCost() > 0) {
                    if (product.getEmeraldCost() > 0) costStr.append(" + ");
                    costStr.append(product.getDiamondCost()).append(" Diamonds");
                }
                tooltip.add(Component.literal(costStr.toString()).withStyle(ChatFormatting.GOLD));

                if (isCreative) {
                    tooltip.add(Component.literal("[Creative Mode - Click to Dispense]").withStyle(ChatFormatting.LIGHT_PURPLE));
                } else if (totalEm >= product.getEmeraldCost() && totalDia >= product.getDiamondCost()) {
                    tooltip.add(Component.literal("[Click to Purchase]").withStyle(ChatFormatting.GREEN));
                } else {
                    int emShort = Math.max(0, product.getEmeraldCost() - totalEm);
                    int diaShort = Math.max(0, product.getDiamondCost() - totalDia);
                    StringBuilder shortStr = new StringBuilder("Need ");
                    if (emShort > 0) shortStr.append(emShort).append(" more Emeralds");
                    if (diaShort > 0) {
                        if (emShort > 0) shortStr.append(" & ");
                        shortStr.append(diaShort).append(" more Diamonds");
                    }
                    tooltip.add(Component.literal(shortStr.toString()).withStyle(ChatFormatting.RED));
                }

                graphics.setComponentTooltipForNextFrame(this.font, tooltip, mouseX, mouseY);
                break;
            }
        }
    }

    private void renderLibraryTooltips(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        int left = this.leftPos;
        int top = this.topPos;
        int listX = left + 8;
        int listY = top + 36;

        List<MedicalLibrary.RecipeEntry> entries = MedicalLibrary.getEntries();
        int startIndex = this.libraryScrollOffset * 2;

        for (int i = 0; i < 8; ++i) {
            int entryIndex = startIndex + i;
            if (entryIndex >= entries.size()) break;

            int col = i % 2;
            int row = i / 2;
            int slotX = listX + 4 + col * 22;
            int slotY = listY + 4 + row * 22;

            if (mouseX >= slotX && mouseX <= slotX + 18 && mouseY >= slotY && mouseY <= slotY + 18) {
                MedicalLibrary.RecipeEntry entry = entries.get(entryIndex);
                graphics.setTooltipForNextFrame(this.font, new ItemStack(entry.antidote()), mouseX, mouseY);
                return;
            }
        }

        if (this.selectedLibraryIndex >= 0 && this.selectedLibraryIndex < entries.size()) {
            MedicalLibrary.RecipeEntry selected = entries.get(this.selectedLibraryIndex);
            int gridX = left + 64 + 8;
            int gridY = top + 36 + 46;

            if (selected.ingredients().size() > 0 && mouseX >= gridX && mouseX <= gridX + 18 && mouseY >= gridY && mouseY <= gridY + 18) {
                graphics.setTooltipForNextFrame(this.font, new ItemStack(selected.ingredients().get(0)), mouseX, mouseY);
            } else if (selected.ingredients().size() > 1 && mouseX >= gridX + 18 && mouseX <= gridX + 36 && mouseY >= gridY && mouseY <= gridY + 18) {
                graphics.setTooltipForNextFrame(this.font, new ItemStack(selected.ingredients().get(1)), mouseX, mouseY);
            } else if (selected.ingredients().size() > 2 && mouseX >= gridX && mouseX <= gridX + 18 && mouseY >= gridY + 18 && mouseY <= gridY + 36) {
                graphics.setTooltipForNextFrame(this.font, new ItemStack(selected.ingredients().get(2)), mouseX, mouseY);
            } else {
                int resX = gridX + 66;
                int resY = gridY + 10;
                if (mouseX >= resX && mouseX <= resX + 18 && mouseY >= resY && mouseY <= resY + 18) {
                    graphics.setTooltipForNextFrame(this.font, new ItemStack(selected.antidote()), mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        int left = this.leftPos;
        int top = this.topPos;
        double mouseX = event.x();
        double mouseY = event.y();

        if (this.activeTab == ActiveTab.LIBRARY) {
            int listX = left + 8;
            int listY = top + 36;
            List<MedicalLibrary.RecipeEntry> entries = MedicalLibrary.getEntries();
            int startIndex = this.libraryScrollOffset * 2;

            for (int i = 0; i < 8; ++i) {
                int entryIndex = startIndex + i;
                if (entryIndex >= entries.size()) break;

                int col = i % 2;
                int row = i / 2;
                int slotX = listX + 4 + col * 22;
                int slotY = listY + 4 + row * 22;

                if (mouseX >= slotX && mouseX <= slotX + 18 && mouseY >= slotY && mouseY <= slotY + 18) {
                    this.selectedLibraryIndex = entryIndex;
                    if (this.minecraft != null) {
                        AbstractWidget.playButtonClickSound(this.minecraft.getSoundManager());
                    }
                    return true;
                }
            }

            // Click result slot in Library to buy the selected antidote
            if (this.selectedLibraryIndex >= 0 && this.selectedLibraryIndex < entries.size()) {
                MedicalLibrary.RecipeEntry selected = entries.get(this.selectedLibraryIndex);
                int gridX = left + 64 + 8;
                int gridY = top + 36 + 46;
                int resX = gridX + 66;
                int resY = gridY + 10;

                if (mouseX >= resX && mouseX <= resX + 18 && mouseY >= resY && mouseY <= resY + 18) {
                    for (VendingProduct p : VendingCatalog.getAll()) {
                        if (p.getItem() == selected.antidote()) {
                            if (this.minecraft != null && this.minecraft.gameMode != null) {
                                AbstractWidget.playButtonClickSound(this.minecraft.getSoundManager());
                                this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, p.getId());
                                return true;
                            }
                        }
                    }
                }
            }
        } else {
            int catalogX = left + 8;
            int catalogY = top + 36;
            VendingProduct.Category cat = switch (this.activeTab) {
                case INGREDIENTS -> VendingProduct.Category.INGREDIENTS;
                case GOD_TIER -> VendingProduct.Category.GOD_TIER;
                default -> VendingProduct.Category.ANTIDOTES;
            };

            List<VendingProduct> products = VendingCatalog.getByCategory(cat);
            int startIndex = this.scrollOffset * CATALOG_COLS;

            for (int i = 0; i < CATALOG_COLS * CATALOG_ROWS; ++i) {
                int productIndex = startIndex + i;
                if (productIndex >= products.size()) break;

                VendingProduct product = products.get(productIndex);
                int col = i % CATALOG_COLS;
                int row = i / CATALOG_COLS;
                int cellX = catalogX + 2 + col * CELL_SIZE;
                int cellY = catalogY + 2 + row * CELL_SIZE;

                if (mouseX >= cellX && mouseX < cellX + CELL_SIZE && mouseY >= cellY && mouseY < cellY + CELL_SIZE) {
                    if (this.minecraft != null && this.minecraft.gameMode != null) {
                        AbstractWidget.playButtonClickSound(this.minecraft.getSoundManager());
                        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, product.getId());
                        return true;
                    }
                }
            }
        }

        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (this.activeTab == ActiveTab.LIBRARY) {
            List<MedicalLibrary.RecipeEntry> entries = MedicalLibrary.getEntries();
            int maxRows = (int) Math.ceil((double) entries.size() / 2);
            int maxScroll = Math.max(0, maxRows - 4);

            if (scrollY < 0) {
                this.libraryScrollOffset = Math.min(maxScroll, this.libraryScrollOffset + 1);
                return true;
            } else if (scrollY > 0) {
                this.libraryScrollOffset = Math.max(0, this.libraryScrollOffset - 1);
                return true;
            }
        } else {
            VendingProduct.Category cat = switch (this.activeTab) {
                case INGREDIENTS -> VendingProduct.Category.INGREDIENTS;
                case GOD_TIER -> VendingProduct.Category.GOD_TIER;
                default -> VendingProduct.Category.ANTIDOTES;
            };

            List<VendingProduct> products = VendingCatalog.getByCategory(cat);
            int maxRows = (int) Math.ceil((double) products.size() / CATALOG_COLS);
            int maxScroll = Math.max(0, maxRows - CATALOG_ROWS);

            if (scrollY < 0) {
                this.scrollOffset = Math.min(maxScroll, this.scrollOffset + 1);
                return true;
            } else if (scrollY > 0) {
                this.scrollOffset = Math.max(0, this.scrollOffset - 1);
                return true;
            }
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    private void drawRecessedSlot(GuiGraphicsExtractor graphics, int x, int y) {
        // Standard Minecraft 18x18 recessed slot box
        graphics.fill(RenderPipelines.GUI, x, y, x + 18, y + 1, 0xFF373737);
        graphics.fill(RenderPipelines.GUI, x, y, x + 1, y + 18, 0xFF373737);
        graphics.fill(RenderPipelines.GUI, x + 17, y, x + 18, y + 18, 0xFFFFFFFF);
        graphics.fill(RenderPipelines.GUI, x, y + 17, x + 18, y + 18, 0xFFFFFFFF);
        graphics.fill(RenderPipelines.GUI, x + 1, y + 1, x + 17, y + 17, 0xFF8B8B8B);
    }

    private void drawRecessedPanel(GuiGraphicsExtractor graphics, int x, int y, int width, int height) {
        graphics.fill(RenderPipelines.GUI, x, y, x + width, y + 1, 0xFF373737);
        graphics.fill(RenderPipelines.GUI, x, y, x + 1, y + height, 0xFF373737);
        graphics.fill(RenderPipelines.GUI, x + width - 1, y, x + width, y + height, 0xFFFFFFFF);
        graphics.fill(RenderPipelines.GUI, x, y + height - 1, x + width, y + height, 0xFFFFFFFF);
        graphics.fill(RenderPipelines.GUI, x + 1, y + 1, x + width - 1, y + height - 1, 0xFF8B8B8B);
    }
}
