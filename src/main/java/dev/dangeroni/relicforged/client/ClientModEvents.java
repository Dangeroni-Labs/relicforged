package dev.dangeroni.relicforged.client;

import dev.dangeroni.relicforged.Relicforged;
import dev.dangeroni.relicforged.registry.ModMenus;
import dev.dangeroni.relicforged.registry.ModItems;
import net.minecraft.client.renderer.item.CompassItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Relicforged.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {
    private ClientModEvents() {
    }

    @SubscribeEvent
    public static void registerScreens(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenus.RELIC_FORGE.get(), RelicForgeScreen::new);
            ItemProperties.register(ModItems.RESONANCE_COMPASS.get(), ResourceLocation.withDefaultNamespace("angle"),
                    new CompassItemPropertyFunction((level, stack, entity) -> ResonanceCompassLocator.getTarget(level, entity)));
        });
    }
}
