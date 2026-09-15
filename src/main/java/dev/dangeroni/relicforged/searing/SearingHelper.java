package dev.dangeroni.relicforged.searing;

import dev.dangeroni.relicforged.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public final class SearingHelper {
    public static final String NBT_KEY = "Searing";

    private SearingHelper() {
    }

    public static boolean isBlackenedTool(ItemStack stack) {
        return isSearingBlockTool(stack)
                || stack.is(ModItems.BLACKENED_HOE.get())
                || stack.is(ModItems.BLACKENED_SWORD.get());
    }

    public static boolean isSearingBlockTool(ItemStack stack) {
        return stack.is(ModItems.BLACKENED_PICKAXE.get())
                || stack.is(ModItems.BLACKENED_AXE.get())
                || stack.is(ModItems.BLACKENED_SHOVEL.get());
    }

    public static boolean isSearingEnabled(ItemStack stack) {
        return !stack.hasTag() || !stack.getTag().contains(NBT_KEY) || stack.getTag().getBoolean(NBT_KEY);
    }

    public static boolean toggle(ItemStack stack) {
        boolean enabled = !isSearingEnabled(stack);
        stack.getOrCreateTag().putBoolean(NBT_KEY, enabled);
        return enabled;
    }

    public static Component stateMessage(boolean enabled) {
        return Component.literal("Searing").withStyle(ChatFormatting.GOLD)
                .append(Component.literal(" is ").withStyle(ChatFormatting.WHITE))
                .append(Component.literal(enabled ? "ON" : "OFF")
                        .withStyle(enabled ? ChatFormatting.GREEN : ChatFormatting.RED));
    }

    public static Component tooltip(boolean enabled) {
        return Component.literal("Searing").withStyle(ChatFormatting.GOLD)
                .append(Component.literal(": ").withStyle(ChatFormatting.WHITE))
                .append(Component.literal(enabled ? "ON" : "OFF")
                        .withStyle(enabled ? ChatFormatting.GREEN : ChatFormatting.RED));
    }
}
