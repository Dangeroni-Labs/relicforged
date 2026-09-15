package dev.dangeroni.relicforged.registry;

import dev.dangeroni.relicforged.Relicforged;
import dev.dangeroni.relicforged.block.entity.RelicForgeBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Relicforged.MOD_ID);

    public static final RegistryObject<BlockEntityType<RelicForgeBlockEntity>> RELIC_FORGE = BLOCK_ENTITIES.register("relic_forge", () -> BlockEntityType.Builder.of(RelicForgeBlockEntity::new, ModBlocks.RELIC_FORGE.get()).build(null));

    private ModBlockEntities() {
    }
}
