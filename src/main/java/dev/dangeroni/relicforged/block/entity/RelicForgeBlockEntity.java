package dev.dangeroni.relicforged.block.entity;

import dev.dangeroni.relicforged.menu.RelicForgeMenu;
import dev.dangeroni.relicforged.registry.ModBlockEntities;
import dev.dangeroni.relicforged.recipe.RelicForgingRecipe;
import dev.dangeroni.relicforged.registry.ModRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;

import java.util.Optional;

public final class RelicForgeBlockEntity extends BlockEntity implements MenuProvider {
    public static final int TEMPLATE_SLOT = 0;
    public static final int BASE_SLOT = 1;
    public static final int HANDLE_SLOT = 2;
    public static final int HEAD_SLOT = 3;
    public static final int RESULT_SLOT = 4;
    public static final int SLOT_COUNT = 5;

    private boolean recalculatingResult;
    private boolean consumingResultInputs;
    private final SimpleContainer inventory = new SimpleContainer(SLOT_COUNT) {
        @Override
        public void setItem(int slot, ItemStack stack) {
            super.setItem(slot, stack);
            if (slot != RESULT_SLOT) {
                onInventoryChanged();
            }
        }

        @Override
        public ItemStack removeItem(int slot, int amount) {
            ItemStack removed = super.removeItem(slot, amount);
            if (slot != RESULT_SLOT && !removed.isEmpty()) {
                onInventoryChanged();
            }
            return removed;
        }

        @Override
        public void setChanged() {
            super.setChanged();
        }
    };

    public RelicForgeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RELIC_FORGE.get(), pos, state);
    }

    public Container getInventory() {
        return inventory;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.relicforged.relic_forge");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new RelicForgeMenu(containerId, playerInventory, this);
    }

    public void takeResult() {
        if (level == null || level.isClientSide || getMatchingRecipe().isEmpty()) {
            return;
        }

        consumingResultInputs = true;
        try {
            inventory.removeItem(BASE_SLOT, 1);
            inventory.removeItem(HANDLE_SLOT, 1);
            inventory.removeItem(HEAD_SLOT, 1);
        } finally {
            consumingResultInputs = false;
        }
        recalculateResult();
    }

    public boolean canTakeResult() {
        return getMatchingRecipe().isPresent() && !inventory.getItem(RESULT_SLOT).isEmpty();
    }

    public void dropInputContents() {
        if (level == null || level.isClientSide) {
            return;
        }

        for (int slot = TEMPLATE_SLOT; slot <= HEAD_SLOT; slot++) {
            ItemStack stack = inventory.removeItemNoUpdate(slot);
            if (!stack.isEmpty()) {
                Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), stack);
            }
        }

        inventory.setItem(RESULT_SLOT, ItemStack.EMPTY);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        CompoundTag inventoryTag = new CompoundTag();
        for (int slot = 0; slot < SLOT_COUNT; slot++) {
            ItemStack stack = inventory.getItem(slot);
            if (!stack.isEmpty()) {
                inventoryTag.put("Slot" + slot, stack.save(new CompoundTag()));
            }
        }
        tag.put("Inventory", inventoryTag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        CompoundTag inventoryTag = tag.getCompound("Inventory");
        for (int slot = 0; slot < SLOT_COUNT; slot++) {
            inventory.setItem(slot, inventoryTag.contains("Slot" + slot) ? ItemStack.of(inventoryTag.getCompound("Slot" + slot)) : ItemStack.EMPTY);
        }
        recalculateResult();
    }

    private void onInventoryChanged() {
        if (level != null && !level.isClientSide && !recalculatingResult && !consumingResultInputs) {
            recalculateResult();
        }
        setChanged();
    }

    private void recalculateResult() {
        if (level != null && level.isClientSide) {
            return;
        }

        recalculatingResult = true;
        inventory.setItem(RESULT_SLOT, getMatchingRecipe()
                .map(recipe -> recipe.assemble(inventory, level.registryAccess()))
                .orElse(ItemStack.EMPTY));
        recalculatingResult = false;
    }

    private Optional<RelicForgingRecipe> getMatchingRecipe() {
        if (level == null) {
            return Optional.empty();
        }

        return level.getRecipeManager().getRecipeFor(ModRecipes.relicForgingType(), inventory, level);
    }
}
