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
    private int selectedStoreIndex = 0;

    private static final int CATALOG_COLS = 7;
    private static final int CATALOG_ROWS = 4;
    private static final int CELL_SIZE = 22;

    private Button storeBuyButton;
    private Button libraryBuyButton;

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
            this.selectedStoreIndex = 0;
            updateButtonVisibility();
        }).bounds(this.leftPos + 8, tabY, 52, 16).build());

        this.addRenderableWidget(Button.builder(Component.literal("Ingredients"), b -> {
            this.activeTab = ActiveTab.INGREDIENTS;
            this.scrollOffset = 0;
            this.selectedStoreIndex = 0;
            updateButtonVisibility();
        }).bounds(this.leftPos + 62, tabY, 62, 16).build());

        this.addRenderableWidget(Button.builder(Component.literal("God Tier"), b -> {
            this.activeTab = ActiveTab.GOD_TIER;
            this.scrollOffset = 0;
            this.selectedStoreIndex = 0;
            updateButtonVisibility();
        }).bounds(this.leftPos + 126, tabY, 50, 16).build());

        this.addRenderableWidget(Button.builder(Component.literal("Library"), b -> {
            this.activeTab = ActiveTab.LIBRARY;
            updateButtonVisibility();
        }).bounds(this.leftPos + 178, tabY, 48, 16).build());

        // Store Buy Button in the right-side detail pane (next to selected item slot)
        this.storeBuyButton = this.addRenderableWidget(Button.builder(Component.literal("Buy"), b -> {
            buySelectedStoreItem();
        }).bounds(this.leftPos + 198, this.topPos + 100, 44, 20).build());

        // Library Buy Button in the recipe pane (next to recipe result slot)
        this.libraryBuyButton = this.addRenderableWidget(Button.builder(Component.literal("Buy (48E)"), b -> {
            buySelectedLibraryItem();
        }).bounds(this.leftPos + 168, this.topPos + 92, 66, 20).build());

        updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        boolean isLib = this.activeTab == ActiveTab.LIBRARY;
        if (this.storeBuyButton != null) {
            this.storeBuyButton.visible = !isLib;
        }
        if (this.libraryBuyButton != null) {
            this.libraryBuyButton.visible = isLib;
        }
    }

    private void buySelectedStoreItem() {
        VendingProduct.Category cat = switch (this.activeTab) {
            case INGREDIENTS -> VendingProduct.Category.INGREDIENTS;
            case GOD_TIER -> VendingProduct.Category.GOD_TIER;
            default -> VendingProduct.Category.ANTIDOTES;
        };

        List<VendingProduct> products = VendingCatalog.getByCategory(cat);
        if (this.selectedStoreIndex >= 0 && this.selectedStoreIndex < products.size()) {
            VendingProduct product = products.get(this.selectedStoreIndex);
            if (this.minecraft != null && this.minecraft.gameMode != null) {
                AbstractWidget.playButtonClickSound(this.minecraft.getSoundManager());
                this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, product.getId());
            }
        }
    }

    private void buySelectedLibraryItem() {
        List<MedicalLibrary.RecipeEntry> entries = MedicalLibrary.getEntries();
        if (this.selectedLibraryIndex >= 0 && this.selectedLibraryIndex < entries.size()) {
            MedicalLibrary.RecipeEntry selected = entries.get(this.selectedLibraryIndex);
            for (VendingProduct p : VendingCatalog.getAll()) {
                if (p.getItem() == selected.antidote()) {
                    if (this.minecraft != null && this.minecraft.gameMode != null) {
                        AbstractWidget.playButtonClickSound(this.minecraft.getSoundManager());
                        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, p.getId());
                    }
                    break;
                }
            }
        }
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

            boolean canAfford = isCreative || (invEm >= product.getEmeraldCost() && invDia >= product.getDiamondCost());
            boolean isSelected = (productIndex == this.selectedStoreIndex);

            // Draw base slot box
            drawRecessedSlot(graphics, cellX + 2, cellY + 2);

            if (isSelected) {
                // Gold highlight for the selected item
                graphics.fill(RenderPipelines.GUI, cellX + 1, cellY + 1, cellX + 21, cellY + 2, 0xFFFFDF00);
                graphics.fill(RenderPipelines.GUI, cellX + 1, cellY + 20, cellX + 21, cellY + 21, 0xFFFFDF00);
                graphics.fill(RenderPipelines.GUI, cellX + 1, cellY + 1, cellX + 2, cellY + 21, 0xFFFFDF00);
                graphics.fill(RenderPipelines.GUI, cellX + 20, cellY + 1, cellX + 21, cellY + 21, 0xFFFFDF00);
            } else if (canAfford) {
                // Subtle green highlight border for affordable items
                graphics.fill(RenderPipelines.GUI, cellX + 1, cellY + 1, cellX + 21, cellY + 2, 0xFF00AA00);
                graphics.fill(RenderPipelines.GUI, cellX + 1, cellY + 20, cellX + 21, cellY + 21, 0xFF00AA00);
                graphics.fill(RenderPipelines.GUI, cellX + 1, cellY + 1, cellX + 2, cellY + 21, 0xFF00AA00);
                graphics.fill(RenderPipelines.GUI, cellX + 20, cellY + 1, cellX + 21, cellY + 21, 0xFF00AA00);
            } else {
                // RED highlight for lack of currency
                graphics.fill(RenderPipelines.GUI, cellX + 1, cellY + 1, cellX + 21, cellY + 2, 0xFFAA0000);
                graphics.fill(RenderPipelines.GUI, cellX + 1, cellY + 20, cellX + 21, cellY + 21, 0xFFAA0000);
                graphics.fill(RenderPipelines.GUI, cellX + 1, cellY + 1, cellX + 2, cellY + 21, 0xFFAA0000);
                graphics.fill(RenderPipelines.GUI, cellX + 20, cellY + 1, cellX + 21, cellY + 21, 0xFFAA0000);
                graphics.fill(RenderPipelines.GUI, cellX + 2, cellY + 2, cellX + 20, cellY + 20, 0x28FF0000);
            }

            graphics.item(product.getItemStack(), cellX + 3, cellY + 3);
            graphics.itemDecorations(this.font, product.getItemStack(), cellX + 3, cellY + 3);
        }

        // Right-Side Transaction Pane
        int cardX = left + 170;
        int cardY = top + 36;
        int cardW = 78;
        int cardH = 92;
        drawRecessedPanel(graphics, cardX, cardY, cardW, cardH);

        VendingProduct selectedProduct = null;
        if (this.selectedStoreIndex >= 0 && this.selectedStoreIndex < products.size()) {
            selectedProduct = products.get(this.selectedStoreIndex);
        }

        if (selectedProduct != null) {
            // Row 1: Diamond cost slot
            drawRecessedSlot(graphics, cardX + 6, cardY + 16);
            graphics.item(new ItemStack(Items.DIAMOND), cardX + 7, cardY + 17);

            // Row 2: Emerald cost slot
            drawRecessedSlot(graphics, cardX + 6, cardY + 38);
            graphics.item(new ItemStack(Items.EMERALD), cardX + 7, cardY + 39);

            // Row 3: Selected item slot
            drawRecessedSlot(graphics, cardX + 6, cardY + 64);
            // Gold border around selected preview
            graphics.fill(RenderPipelines.GUI, cardX + 5, cardY + 63, cardX + 25, cardY + 64, 0xFFFFDF00);
            graphics.fill(RenderPipelines.GUI, cardX + 5, cardY + 82, cardX + 25, cardY + 83, 0xFFFFDF00);
            graphics.fill(RenderPipelines.GUI, cardX + 5, cardY + 63, cardX + 6, cardY + 83, 0xFFFFDF00);
            graphics.fill(RenderPipelines.GUI, cardX + 24, cardY + 63, cardX + 25, cardY + 83, 0xFFFFDF00);
            graphics.item(selectedProduct.getItemStack(), cardX + 7, cardY + 65);

            // Update Store Buy Button active status
            boolean canAfford = isCreative || (invEm >= selectedProduct.getEmeraldCost() && invDia >= selectedProduct.getDiamondCost());
            if (this.storeBuyButton != null) {
                this.storeBuyButton.active = canAfford;
            }
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
                graphics.fill(RenderPipelines.GUI, slotX, slotY + 1, slotX + 1, slotY + 17, 0xFFFFDF00);
                graphics.fill(RenderPipelines.GUI, slotX + 17, slotY + 1, slotX + 18, slotY + 17, 0xFFFFDF00);
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

            int resultX = gridX + 66;
            int resultY = gridY + 10;
            drawRecessedSlot(graphics, resultX, resultY);
            graphics.item(new ItemStack(selected.antidote()), resultX + 1, resultY + 1);

            int invEm = this.minecraft != null && this.minecraft.player != null
                ? MedicalVendingScreenHandler.countPlayerItem(this.minecraft.player, Items.EMERALD) : 0;
            int invDia = this.minecraft != null && this.minecraft.player != null
                ? MedicalVendingScreenHandler.countPlayerItem(this.minecraft.player, Items.DIAMOND) : 0;
            boolean isCreative = this.minecraft != null && this.minecraft.player != null
                && this.minecraft.player.hasInfiniteMaterials();

            VendingProduct matchingProduct = null;
            for (VendingProduct p : VendingCatalog.getAll()) {
                if (p.getItem() == selected.antidote()) {
                    matchingProduct = p;
                    break;
                }
            }

            int reqEm = matchingProduct != null ? matchingProduct.getEmeraldCost() : 48;
            int reqDia = matchingProduct != null ? matchingProduct.getDiamondCost() : 0;

            if (this.libraryBuyButton != null) {
                String label = reqDia > 0 ? "Buy (" + reqEm + "E, " + reqDia + "D)" : "Buy (" + reqEm + "E)";
                this.libraryBuyButton.setMessage(Component.literal(label));
                int btnW = Math.max(66, this.font.width(label) + 14);
                this.libraryBuyButton.setWidth(btnW);
                this.libraryBuyButton.setX(this.leftPos + 242 - btnW);
                this.libraryBuyButton.active = isCreative || (invEm >= reqEm && invDia >= reqDia);
            }
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        // Title in black font without shadow
        graphics.text(this.font, "Medical Vending Machine", this.titleLabelX, this.titleLabelY, 0xFF000000, false);

        // Player Inventory title in black font without shadow
        graphics.text(this.font, "Inventory", this.inventoryLabelX, this.inventoryLabelY, 0xFF000000, false);

        int invEm = this.minecraft != null && this.minecraft.player != null
            ? MedicalVendingScreenHandler.countPlayerItem(this.minecraft.player, Items.EMERALD) : 0;
        int invDia = this.minecraft != null && this.minecraft.player != null
            ? MedicalVendingScreenHandler.countPlayerItem(this.minecraft.player, Items.DIAMOND) : 0;

        if (this.activeTab != ActiveTab.LIBRARY) {
            int cardX = 170;
            int cardY = 36;

            VendingProduct.Category cat = switch (this.activeTab) {
                case INGREDIENTS -> VendingProduct.Category.INGREDIENTS;
                case GOD_TIER -> VendingProduct.Category.GOD_TIER;
                default -> VendingProduct.Category.ANTIDOTES;
            };

            List<VendingProduct> products = VendingCatalog.getByCategory(cat);
            VendingProduct selectedProduct = null;
            if (this.selectedStoreIndex >= 0 && this.selectedStoreIndex < products.size()) {
                selectedProduct = products.get(this.selectedStoreIndex);
            }

            if (selectedProduct != null) {
                // Header: item price info in black font without shadow
                graphics.text(this.font, "Cost:", cardX + 6, cardY + 5, 0xFF000000, false);

                // Diamond cost text in black font without shadow
                int diaCost = selectedProduct.getDiamondCost();
                if (diaCost > 0) {
                    graphics.text(this.font, "x " + diaCost, cardX + 28, cardY + 21, 0xFF000000, false);
                } else {
                    graphics.text(this.font, "0 D", cardX + 28, cardY + 21, 0xFF000000, false);
                }

                // Emerald cost text in black font without shadow
                int emCost = selectedProduct.getEmeraldCost();
                if (emCost > 0) {
                    graphics.text(this.font, "x " + emCost, cardX + 28, cardY + 43, 0xFF000000, false);
                } else {
                    graphics.text(this.font, "0 E", cardX + 28, cardY + 43, 0xFF000000, false);
                }
            }

            // Catalog item tooltips
            renderCatalogTooltips(graphics, mouseX, mouseY);
        } else {
            // Library view text in black font without shadow
            int infoX = 64;
            int infoY = 36;
            int infoW = 184;
            List<MedicalLibrary.RecipeEntry> entries = MedicalLibrary.getEntries();

            if (this.selectedLibraryIndex >= 0 && this.selectedLibraryIndex < entries.size()) {
                MedicalLibrary.RecipeEntry selected = entries.get(this.selectedLibraryIndex);

                String name = selected.conditionName();
                String displayName = name.toLowerCase().endsWith("antidote") ? name : name + " Antidote";
                graphics.text(this.font, displayName, infoX + 6, infoY + 6, 0xFF000000, false);

                // Word wrap lore so it never clips out of the UI panel
                graphics.textWithWordWrap(this.font, Component.literal(selected.lore()), infoX + 6, infoY + 17, infoW - 12, 0xFF000000, false);

                graphics.text(this.font, "Recipe:", infoX + 6, infoY + 36, 0xFF000000, false);

                int gridX = infoX + 8;
                int gridY = infoY + 46;
                graphics.text(this.font, "->", gridX + 44, gridY + 14, 0xFF000000, false);
            }

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
                    tooltip.add(Component.literal("[Creative Mode - Free]").withStyle(ChatFormatting.LIGHT_PURPLE));
                } else if (invEm >= product.getEmeraldCost() && invDia >= product.getDiamondCost()) {
                    tooltip.add(Component.literal("[Click to Select]").withStyle(ChatFormatting.GREEN));
                } else {
                    int emShort = Math.max(0, product.getEmeraldCost() - invEm);
                    int diaShort = Math.max(0, product.getDiamondCost() - invDia);
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

                int col = i % CATALOG_COLS;
                int row = i / CATALOG_COLS;
                int cellX = catalogX + 2 + col * CELL_SIZE;
                int cellY = catalogY + 2 + row * CELL_SIZE;

                if (mouseX >= cellX && mouseX < cellX + CELL_SIZE && mouseY >= cellY && mouseY < cellY + CELL_SIZE) {
                    this.selectedStoreIndex = productIndex;
                    if (this.minecraft != null) {
                        AbstractWidget.playButtonClickSound(this.minecraft.getSoundManager());
                    }
                    return true;
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
