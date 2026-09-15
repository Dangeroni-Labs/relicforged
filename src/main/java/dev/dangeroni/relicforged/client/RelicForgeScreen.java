package dev.dangeroni.relicforged.client;

import dev.dangeroni.relicforged.Relicforged;
import dev.dangeroni.relicforged.menu.RelicForgeMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public final class RelicForgeScreen extends AbstractContainerScreen<RelicForgeMenu> {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Relicforged.MOD_ID, "textures/gui/relic_forge.png");

    public RelicForgeScreen(RelicForgeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 176;
        imageHeight = 166;
        inventoryLabelX = 8;
        inventoryLabelY = 72;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = leftPos;
        int y = topPos;
        graphics.blit(BACKGROUND, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}
