package com.leanwell.antidote.client.gui;

import com.leanwell.antidote.Antidote;
import com.leanwell.antidote.screen.MedicalVendingScreenHandler;
import com.leanwell.antidote.store.VendingCatalog;
import com.leanwell.antidote.store.VendingProduct;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class MedicalVendingScreen extends AbstractContainerScreen<MedicalVendingScreenHandler> {
    private static final Identifier GUI_TEXTURE = Antidote.id("textures/gui/vending_machine_gui.png");

    private VendingProduct.Category activeCategory = VendingProduct.Category.ANTIDOTES;
    private int scrollOffset = 0;
    private static final int ITEMS_PER_ROW = 5;
    private static final int VISIBLE_ROWS = 4;
    private static final int CELL_SIZE = 24;

    public MedicalVendingScreen(MedicalVendingScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title, 252, 224);
        this.titleLabelX = 12;
        this.titleLabelY = 6;
        this.inventoryLabelX = 36;
        this.inventoryLabelY = 128;
    }

    @Override
    protected void init() {
        super.init();

        int tabY = this.topPos + 18;

        this.addRenderableWidget(Button.builder(Component.literal("Antidotes"), b -> {
            this.activeCategory = VendingProduct.Category.ANTIDOTES;
            this.scrollOffset = 0;
        }).bounds(this.leftPos + 10, tabY, 52, 16).build());

        this.addRenderableWidget(Button.builder(Component.literal("Ingredients"), b -> {
            this.activeCategory = VendingProduct.Category.INGREDIENTS;
            this.scrollOffset = 0;
        }).bounds(this.leftPos + 64, tabY, 62, 16).build());

        this.addRenderableWidget(Button.builder(Component.literal("God Tier"), b -> {
            this.activeCategory = VendingProduct.Category.GOD_TIER;
            this.scrollOffset = 0;
        }).bounds(this.leftPos + 128, tabY, 50, 16).build());

        this.addRenderableWidget(Button.builder(Component.literal("Eject Coins"), b -> {
            if (this.minecraft != null && this.minecraft.gameMode != null) {
                this.minecraft.gameMode.handleInventoryButtonClick(
                    this.menu.containerId,
                    MedicalVendingScreenHandler.EJECT_BUTTON_ID
                );
            }
        }).bounds(this.leftPos + 180, this.topPos + 88, 64, 18).build());
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);

        int left = this.leftPos;
        int top = this.topPos;

        // Render main background frame
        graphics.fill(RenderPipelines.GUI, left, top, left + this.imageWidth, top + this.imageHeight, 0xFFC6C6C6);
        graphics.fill(RenderPipelines.GUI, left + 2, top + 2, left + this.imageWidth - 2, top + this.imageHeight - 2, 0xFF383838);
        graphics.fill(RenderPipelines.GUI, left + 4, top + 4, left + this.imageWidth - 4, top + this.imageHeight - 4, 0xFF242424);

        // Catalog background panel
        int catalogX = left + 10;
        int catalogY = top + 36;
        int catalogWidth = ITEMS_PER_ROW * CELL_SIZE + 4;
        int catalogHeight = VISIBLE_ROWS * CELL_SIZE + 4;
        graphics.fill(RenderPipelines.GUI, catalogX, catalogY, catalogX + catalogWidth, catalogY + catalogHeight, 0xFF141414);

        // Draw catalog items
        List<VendingProduct> products = VendingCatalog.getByCategory(this.activeCategory);
        int startIndex = this.scrollOffset * ITEMS_PER_ROW;

        for (int i = 0; i < ITEMS_PER_ROW * VISIBLE_ROWS; ++i) {
            int productIndex = startIndex + i;
            if (productIndex >= products.size()) break;

            VendingProduct product = products.get(productIndex);
            int col = i % ITEMS_PER_ROW;
            int row = i / ITEMS_PER_ROW;
            int cellX = catalogX + 2 + col * CELL_SIZE;
            int cellY = catalogY + 2 + row * CELL_SIZE;

            boolean canAfford = this.menu.getEmeraldBuffer() >= product.getEmeraldCost()
                && this.menu.getDiamondBuffer() >= product.getDiamondCost();

            int cellBg = canAfford ? 0xFF2A4228 : 0xFF1E1E1E;
            graphics.fill(RenderPipelines.GUI, cellX, cellY, cellX + 22, cellY + 22, cellBg);

            graphics.item(product.getItemStack(), cellX + 3, cellY + 3);
            graphics.itemDecorations(this.font, product.getItemStack(), cellX + 3, cellY + 3);
        }

        // Draw Deposit & Output Panel
        int payX = left + 176;
        int payY = top + 18;
        graphics.fill(RenderPipelines.GUI, payX, payY, payX + 70, payY + 92, 0xFF181818);

        // Highlight Emerald Slot (Slot 0 is at 186, 28)
        graphics.fill(RenderPipelines.GUI, left + 185, top + 27, left + 203, top + 45, 0xFF00AA00);

        // Highlight Diamond Slot (Slot 1 is at 186, 64)
        graphics.fill(RenderPipelines.GUI, left + 185, top + 63, left + 203, top + 81, 0xFF00AAAA);

        // Highlight Output Chute (Slot 2 is at 218, 46)
        graphics.fill(RenderPipelines.GUI, left + 217, top + 45, left + 235, top + 63, 0xFFFFAA00);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.textRenderer().accept(
            this.titleLabelX,
            this.titleLabelY,
            this.title.copy().withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)
        );

        graphics.textRenderer().accept(
            this.inventoryLabelX,
            this.inventoryLabelY,
            this.playerInventoryTitle.copy().withStyle(ChatFormatting.GRAY)
        );

        // Balance labels
        graphics.textRenderer().accept(
            180,
            12,
            Component.literal("Deposit & Credit:").withStyle(ChatFormatting.YELLOW)
        );

        graphics.textRenderer().accept(
            206,
            32,
            Component.literal(this.menu.getEmeraldBuffer() + "E").withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD)
        );

        graphics.textRenderer().accept(
            206,
            68,
            Component.literal(this.menu.getDiamondBuffer() + "D").withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD)
        );

        // Hover tooltip over catalog items
        int left = this.leftPos;
        int top = this.topPos;
        int catalogX = left + 10;
        int catalogY = top + 36;
        List<VendingProduct> products = VendingCatalog.getByCategory(this.activeCategory);
        int startIndex = this.scrollOffset * ITEMS_PER_ROW;

        for (int i = 0; i < ITEMS_PER_ROW * VISIBLE_ROWS; ++i) {
            int productIndex = startIndex + i;
            if (productIndex >= products.size()) break;

            VendingProduct product = products.get(productIndex);
            int col = i % ITEMS_PER_ROW;
            int row = i / ITEMS_PER_ROW;
            int cellX = catalogX + 2 + col * CELL_SIZE;
            int cellY = catalogY + 2 + row * CELL_SIZE;

            if (mouseX >= cellX && mouseX <= cellX + 22 && mouseY >= cellY && mouseY <= cellY + 22) {
                ItemStack tooltipStack = product.getItemStack();
                graphics.setTooltipForNextFrame(this.font, tooltipStack, mouseX, mouseY);
                break;
            }
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        int left = this.leftPos;
        int top = this.topPos;
        int catalogX = left + 10;
        int catalogY = top + 36;
        double mouseX = event.x();
        double mouseY = event.y();

        List<VendingProduct> products = VendingCatalog.getByCategory(this.activeCategory);
        int startIndex = this.scrollOffset * ITEMS_PER_ROW;

        for (int i = 0; i < ITEMS_PER_ROW * VISIBLE_ROWS; ++i) {
            int productIndex = startIndex + i;
            if (productIndex >= products.size()) break;

            VendingProduct product = products.get(productIndex);
            int col = i % ITEMS_PER_ROW;
            int row = i / ITEMS_PER_ROW;
            int cellX = catalogX + 2 + col * CELL_SIZE;
            int cellY = catalogY + 2 + row * CELL_SIZE;

            if (mouseX >= cellX && mouseX <= cellX + 22 && mouseY >= cellY && mouseY <= cellY + 22) {
                if (this.minecraft != null && this.minecraft.gameMode != null) {
                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, product.getId());
                    return true;
                }
            }
        }

        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        List<VendingProduct> products = VendingCatalog.getByCategory(this.activeCategory);
        int maxRows = (int) Math.ceil((double) products.size() / ITEMS_PER_ROW);
        int maxScroll = Math.max(0, maxRows - VISIBLE_ROWS);

        if (scrollY < 0) {
            this.scrollOffset = Math.min(maxScroll, this.scrollOffset + 1);
            return true;
        } else if (scrollY > 0) {
            this.scrollOffset = Math.max(0, this.scrollOffset - 1);
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }
}
