package dev.dangeroni.relicforged;

import dev.dangeroni.relicforged.registry.ModBlocks;
import dev.dangeroni.relicforged.registry.ModBlockEntities;
import dev.dangeroni.relicforged.registry.ModCreativeTabs;
import dev.dangeroni.relicforged.registry.ModItems;
import dev.dangeroni.relicforged.registry.ModMenus;
import dev.dangeroni.relicforged.registry.ModRecipes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Relicforged.MOD_ID)
public final class Relicforged {
    public static final String MOD_ID = "relicforged";

    public Relicforged(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        ModRecipes.SERIALIZERS.register(modEventBus);
        ModRecipes.TYPES.register(modEventBus);
    }
}
