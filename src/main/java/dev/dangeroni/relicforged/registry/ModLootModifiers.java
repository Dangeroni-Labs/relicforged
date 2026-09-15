package dev.dangeroni.relicforged.registry;

import com.mojang.serialization.Codec;
import dev.dangeroni.relicforged.Relicforged;
import dev.dangeroni.relicforged.searing.SearingBlockToolLootModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(
            ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Relicforged.MOD_ID);

    public static final RegistryObject<Codec<SearingBlockToolLootModifier>> SEARING_BLOCK_TOOLS = LOOT_MODIFIERS.register(
            "searing_block_tools", () -> SearingBlockToolLootModifier.CODEC);

    private ModLootModifiers() {
    }
}
