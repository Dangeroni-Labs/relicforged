package dev.dangeroni.relicforged.menu;

import dev.dangeroni.relicforged.block.entity.RelicForgeBlockEntity;
import dev.dangeroni.relicforged.registry.ModBlocks;
import dev.dangeroni.relicforged.registry.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class RelicForgeMenu extends AbstractContainerMenu {
    private static final int WORKSTATION_SLOT_COUNT = RelicForgeBlockEntity.SLOT_COUNT;
    private static final int PLAYER_INVENTORY_START = WORKSTATION_SLOT_COUNT;
    private static final int PLAYER_INVENTORY_END = PLAYER_INVENTORY_START + 36;

    private final Container workstation;
    private final RelicForgeBlockEntity blockEntity;
    private final ContainerLevelAccess access;

    public RelicForgeMenu(int containerId, Inventory playerInventory, FriendlyByteBuf data) {
        this(containerId, playerInventory, getBlockEntity(playerInventory, data.readBlockPos()));
    }

    public RelicForgeMenu(int containerId, Inventory playerInventory, RelicForgeBlockEntity blockEntity) {
        this(containerId, playerInventory, blockEntity.getInventory(), blockEntity, ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()));
    }

    private static RelicForgeBlockEntity getBlockEntity(Inventory playerInventory, BlockPos pos) {
        if (playerInventory.player.level().getBlockEntity(pos) instanceof RelicForgeBlockEntity relicForge) {
            return relicForge;
        }

        throw new IllegalStateException("Missing Relic Forge block entity at " + pos);
    }

    private RelicForgeMenu(int containerId, Inventory playerInventory, Container workstation, RelicForgeBlockEntity blockEntity, ContainerLevelAccess access) {
        super(ModMenus.RELIC_FORGE.get(), containerId);
        checkContainerSize(workstation, WORKSTATION_SLOT_COUNT);
        this.workstation = workstation;
        this.blockEntity = blockEntity;
        this.access = access;

        addSlot(new Slot(workstation, RelicForgeBlockEntity.TEMPLATE_SLOT, 80, 17));
        addSlot(new Slot(workstation, RelicForgeBlockEntity.BASE_SLOT, 44, 53));
        addSlot(new Slot(workstation, RelicForgeBlockEntity.HANDLE_SLOT, 71, 53));
        addSlot(new Slot(workstation, RelicForgeBlockEntity.HEAD_SLOT, 98, 53));
        addSlot(new ResultSlot(workstation, RelicForgeBlockEntity.RESULT_SLOT, 134, 53));

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 140 + row * 18));
            }
        }

        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInventory, column, 8 + column * 18, 198));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack originalStack = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            originalStack = stack.copy();

            if (index == RelicForgeBlockEntity.RESULT_SLOT) {
                if (!moveItemStackTo(stack, PLAYER_INVENTORY_START, PLAYER_INVENTORY_END, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack, originalStack);
            } else if (index < WORKSTATION_SLOT_COUNT) {
                if (!moveItemStackTo(stack, PLAYER_INVENTORY_START, PLAYER_INVENTORY_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stack, RelicForgeBlockEntity.TEMPLATE_SLOT, RelicForgeBlockEntity.RESULT_SLOT, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == originalStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        return originalStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, ModBlocks.RELIC_FORGE.get());
    }

    private final class ResultSlot extends Slot {
        private ResultSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public boolean mayPickup(Player player) {
            return blockEntity == null || blockEntity.canTakeResult();
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            super.onTake(player, stack);
            if (blockEntity != null) {
                blockEntity.takeResult();
            }
        }
    }
}
