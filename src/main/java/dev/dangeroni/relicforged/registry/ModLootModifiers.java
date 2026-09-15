package dev.dangeroni.relicforged.registry;

import com.mojang.serialization.Codec;
import dev.dangeroni.relicforged.Relicforged;
import dev.dangeroni.relicforged.searing.SearingPickaxeLootModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(
            ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Relicforged.MOD_ID);

    public static final RegistryObject<Codec<SearingPickaxeLootModifier>> SEARING_PICKAXE = LOOT_MODIFIERS.register(
            "searing_pickaxe", () -> SearingPickaxeLootModifier.CODEC);

    private ModLootModifiers() {
    }
}
