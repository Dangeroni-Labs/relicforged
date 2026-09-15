package dev.dangeroni.relicforged.registry;

import dev.dangeroni.relicforged.Relicforged;
import dev.dangeroni.relicforged.menu.RelicForgeMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Relicforged.MOD_ID);

    public static final RegistryObject<MenuType<RelicForgeMenu>> RELIC_FORGE = MENUS.register("relic_forge", () -> IForgeMenuType.create(RelicForgeMenu::new));

    private ModMenus() {
    }
}
