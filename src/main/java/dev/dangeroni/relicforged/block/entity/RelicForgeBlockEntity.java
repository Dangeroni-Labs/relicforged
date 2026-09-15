package dev.dangeroni.relicforged.block.entity;

import dev.dangeroni.relicforged.menu.RelicForgeMenu;
import dev.dangeroni.relicforged.registry.ModBlockEntities;
import dev.dangeroni.relicforged.registry.ModItems;
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

public final class RelicForgeBlockEntity extends BlockEntity implements MenuProvider {
    public static final int TEMPLATE_SLOT = 0;
    public static final int BASE_SLOT = 1;
    public static final int HANDLE_SLOT = 2;
    public static final int HEAD_SLOT = 3;
    public static final int RESULT_SLOT = 4;
    public static final int SLOT_COUNT = 5;

    private boolean recalculatingResult;
    private final SimpleContainer inventory = new SimpleContainer(SLOT_COUNT) {
        @Override
        public void setChanged() {
            super.setChanged();
            onInventoryChanged();
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
        if (!matchesTemporaryPickaxeRecipe()) {
            return;
        }

        inventory.removeItem(BASE_SLOT, 1);
        inventory.removeItem(HANDLE_SLOT, 1);
        inventory.removeItem(HEAD_SLOT, 1);
        recalculateResult();
    }

    public boolean canTakeResult() {
        return matchesTemporaryPickaxeRecipe() && !inventory.getItem(RESULT_SLOT).isEmpty();
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
        if (level != null && !level.isClientSide && !recalculatingResult) {
            recalculateResult();
        }
        setChanged();
    }

    private void recalculateResult() {
        if (level != null && level.isClientSide) {
            return;
        }

        recalculatingResult = true;
        inventory.setItem(RESULT_SLOT, matchesTemporaryPickaxeRecipe() ? new ItemStack(ModItems.BLACKENED_PICKAXE.get()) : ItemStack.EMPTY);
        recalculatingResult = false;
    }

    private boolean matchesTemporaryPickaxeRecipe() {
        return inventory.getItem(TEMPLATE_SLOT).is(ModItems.RELIC_FORGING_TEMPLATE.get())
                && inventory.getItem(BASE_SLOT).is(net.minecraft.world.item.Items.DIAMOND_PICKAXE)
                && inventory.getItem(HANDLE_SLOT).is(ModItems.BLACKENED_HANDLE.get())
                && inventory.getItem(HEAD_SLOT).is(ModItems.BLACKENED_PICK_HEAD.get());
    }
}
