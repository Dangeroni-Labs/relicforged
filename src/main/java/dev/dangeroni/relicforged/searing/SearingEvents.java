package dev.dangeroni.relicforged.searing;

import dev.dangeroni.relicforged.Relicforged;
import dev.dangeroni.relicforged.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Relicforged.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class SearingEvents {
    private static final int SEARING_FIRE_SECONDS = 3;
    private static final int TICKS_PER_SECOND = 20;

    private SearingEvents() {
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        toggleSearing(event, event.getItemStack());
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        toggleSearing(event, event.getItemStack());
    }

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (!SearingHelper.isBlackenedTool(stack)) {
            return;
        }

        event.getToolTip().add(SearingHelper.tooltip(SearingHelper.isSearingEnabled(stack)));
        event.getToolTip().add(net.minecraft.network.chat.Component.literal("Shift + Right Click to toggle")
                .withStyle(ChatFormatting.GRAY));
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getEntity().level().isClientSide
                || event.getAmount() <= 0.0F
                || !(event.getSource().getEntity() instanceof Player attacker)
                || event.getSource().getDirectEntity() != attacker) {
            return;
        }

        ItemStack weapon = attacker.getMainHandItem();
        if (!weapon.is(ModItems.BLACKENED_SWORD.get()) || !SearingHelper.isSearingEnabled(weapon)) {
            return;
        }

        int searingFireTicks = SEARING_FIRE_SECONDS * TICKS_PER_SECOND;
        if (event.getEntity().getRemainingFireTicks() < searingFireTicks) {
            event.getEntity().setSecondsOnFire(SEARING_FIRE_SECONDS);
        }
    }

    private static void toggleSearing(PlayerInteractEvent event, ItemStack stack) {
        if (!event.getEntity().isShiftKeyDown() || !SearingHelper.isBlackenedTool(stack)) {
            return;
        }

        if (event.getLevel().isClientSide) {
            return;
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
        boolean enabled = SearingHelper.toggle(stack);
        event.getEntity().getInventory().setChanged();
        event.getEntity().displayClientMessage(SearingHelper.stateMessage(enabled), true);
    }
}
