package dev.dangeroni.relicforged.registry;

import dev.dangeroni.relicforged.Relicforged;
import dev.dangeroni.relicforged.item.tool.BlackenedTier;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Relicforged.MOD_ID);

    public static final RegistryObject<Item> RELIC_FORGE = ITEMS.register("relic_forge", () -> new BlockItem(ModBlocks.RELIC_FORGE.get(), new Item.Properties()));

    public static final RegistryObject<Item> BLACKENED_HANDLE = ITEMS.register("blackened_handle", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLACKENED_PICK_HEAD = ITEMS.register("blackened_pick_head", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLACKENED_AXE_HEAD = ITEMS.register("blackened_axe_head", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLACKENED_SHOVEL_HEAD = ITEMS.register("blackened_shovel_head", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLACKENED_HOE_HEAD = ITEMS.register("blackened_hoe_head", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> BLACKENED_EDGE = ITEMS.register("blackened_edge", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RELIC_FORGING_TEMPLATE = ITEMS.register("relic_forging_template", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> BLACKENED_PICKAXE = ITEMS.register("blackened_pickaxe", () -> new PickaxeItem(BlackenedTier.INSTANCE, 1, -2.8F, new Item.Properties()));
    public static final RegistryObject<Item> BLACKENED_AXE = ITEMS.register("blackened_axe", () -> new AxeItem(BlackenedTier.INSTANCE, 5.0F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> BLACKENED_SHOVEL = ITEMS.register("blackened_shovel", () -> new ShovelItem(BlackenedTier.INSTANCE, 1.5F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> BLACKENED_HOE = ITEMS.register("blackened_hoe", () -> new HoeItem(BlackenedTier.INSTANCE, -4, 0.0F, new Item.Properties()));
    public static final RegistryObject<Item> BLACKENED_SWORD = ITEMS.register("blackened_sword", () -> new SwordItem(BlackenedTier.INSTANCE, 4, -2.6F, new Item.Properties()));

    private ModItems() {
    }
}
