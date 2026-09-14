package dev.dangeroni.relicforged.registry;

import dev.dangeroni.relicforged.Relicforged;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Relicforged.MOD_ID);

    private ModItems() {
    }
}
