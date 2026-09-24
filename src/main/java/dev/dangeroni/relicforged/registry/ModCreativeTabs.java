package dev.dangeroni.relicforged.registry;

import dev.dangeroni.relicforged.Relicforged;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Relicforged.MOD_ID);

    public static final RegistryObject<CreativeModeTab> RELICFORGED = CREATIVE_MODE_TABS.register("relicforged", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.relicforged"))
            .icon(() -> new ItemStack(ModItems.BLACKENED_PICKAXE.get()))
            .displayItems((parameters, output) -> {
                output.accept(ModItems.RELIC_FORGE.get());
                output.accept(ModItems.RESONANT_ALLOY.get());
                output.accept(ModItems.AMETHYST_FRAGMENT.get());
                output.accept(ModItems.RESONANT_PICKAXE.get());
                output.accept(ModItems.RESONANT_AXE.get());
                output.accept(ModItems.RESONANT_SHOVEL.get());
                output.accept(ModItems.RESONANT_HOE.get());
                output.accept(ModItems.RESONANT_SWORD.get());
                output.accept(ModItems.RELIC_FORGING_TEMPLATE.get());
                output.accept(ModItems.BLACKENED_HANDLE.get());
                output.accept(ModItems.BLACKENED_PICK_HEAD.get());
                output.accept(ModItems.BLACKENED_AXE_HEAD.get());
                output.accept(ModItems.BLACKENED_SHOVEL_HEAD.get());
                output.accept(ModItems.BLACKENED_HOE_HEAD.get());
                output.accept(ModItems.BLACKENED_EDGE.get());
                output.accept(ModItems.BLACKENED_PICKAXE.get());
                output.accept(ModItems.BLACKENED_AXE.get());
                output.accept(ModItems.BLACKENED_SHOVEL.get());
                output.accept(ModItems.BLACKENED_HOE.get());
                output.accept(ModItems.BLACKENED_SWORD.get());
            }).build());

    private ModCreativeTabs() {
    }
}
