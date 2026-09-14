package dev.dangeroni.relicforged.registry;

import dev.dangeroni.relicforged.Relicforged;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Relicforged.MOD_ID);

    private ModBlocks() {
    }
}
