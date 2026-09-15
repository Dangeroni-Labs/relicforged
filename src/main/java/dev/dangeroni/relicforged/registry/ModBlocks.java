package dev.dangeroni.relicforged.registry;

import dev.dangeroni.relicforged.Relicforged;
import dev.dangeroni.relicforged.block.RelicForgeBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Relicforged.MOD_ID);

    public static final RegistryObject<Block> RELIC_FORGE = BLOCKS.register("relic_forge", RelicForgeBlock::new);

    private ModBlocks() {
    }
}
