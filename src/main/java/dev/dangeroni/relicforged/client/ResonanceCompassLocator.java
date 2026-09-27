package dev.dangeroni.relicforged.client;

import dev.dangeroni.relicforged.Relicforged;
import dev.dangeroni.relicforged.registry.ModItems;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Locates the nearest visible geode marker without requesting or generating chunks. */
@Mod.EventBusSubscriber(modid = Relicforged.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ResonanceCompassLocator {
    private static final int SEARCH_RADIUS = 160;
    private static final int SCAN_INTERVAL_TICKS = 40;
    private static final int RESCAN_MOVE_DISTANCE_SQUARED = 24 * 24;
    private static final Predicate<BlockState> IS_BUDDING_AMETHYST = state -> state.is(Blocks.BUDDING_AMETHYST);

    @Nullable
    private static ClientLevel cachedLevel;
    @Nullable
    private static BlockPos cachedTarget;
    @Nullable
    private static BlockPos lastScanPosition;
    private static long lastScanTick = Long.MIN_VALUE;

    private ResonanceCompassLocator() {
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        Player player = minecraft.player;
        if (level == null || player == null || !hasCompass(player) || level.dimension() != Level.OVERWORLD) {
            clear();
            return;
        }

        if (cachedLevel != level) {
            clear();
            cachedLevel = level;
        }

        BlockPos playerPos = player.blockPosition();
        long gameTime = level.getGameTime();
        boolean targetUnloaded = cachedTarget != null
                && level.getChunkSource().getChunk(cachedTarget.getX() >> 4, cachedTarget.getZ() >> 4, ChunkStatus.FULL, false) == null;
        if (!targetUnloaded && lastScanPosition != null
                && gameTime - lastScanTick < SCAN_INTERVAL_TICKS
                && playerPos.distSqr(lastScanPosition) < RESCAN_MOVE_DISTANCE_SQUARED) {
            return;
        }

        BlockPos found = findNearestLoadedGeode(level, playerPos);
        if (!java.util.Objects.equals(cachedTarget, found)) {
            cachedTarget = found;
        }
        lastScanPosition = playerPos;
        lastScanTick = gameTime;
    }

    @Nullable
    public static GlobalPos getTarget(ClientLevel level, Entity entity) {
        return level == cachedLevel && entity == Minecraft.getInstance().player && cachedTarget != null
                ? GlobalPos.of(level.dimension(), cachedTarget)
                : null;
    }

    private static boolean hasCompass(Player player) {
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack stack = player.getInventory().getItem(slot);
            if (stack.is(ModItems.RESONANCE_COMPASS.get())) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    private static BlockPos findNearestLoadedGeode(ClientLevel level, BlockPos origin) {
        BlockPos nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        int radiusSquared = SEARCH_RADIUS * SEARCH_RADIUS;
        int minChunkX = (origin.getX() - SEARCH_RADIUS) >> 4;
        int maxChunkX = (origin.getX() + SEARCH_RADIUS) >> 4;
        int minChunkZ = (origin.getZ() - SEARCH_RADIUS) >> 4;
        int maxChunkZ = (origin.getZ() + SEARCH_RADIUS) >> 4;

        for (int chunkX = minChunkX; chunkX <= maxChunkX; chunkX++) {
            for (int chunkZ = minChunkZ; chunkZ <= maxChunkZ; chunkZ++) {
                LevelChunk chunk = level.getChunkSource().getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);
                if (chunk == null) {
                    continue;
                }

                LevelChunkSection[] sections = chunk.getSections();
                for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
                    LevelChunkSection section = sections[sectionIndex];
                    if (!section.maybeHas(IS_BUDDING_AMETHYST)) {
                        continue;
                    }

                    int baseY = chunk.getSectionYFromSectionIndex(sectionIndex) << 4;
                    for (int localY = 0; localY < 16; localY++) {
                        for (int localZ = 0; localZ < 16; localZ++) {
                            for (int localX = 0; localX < 16; localX++) {
                                if (!section.getBlockState(localX, localY, localZ).is(Blocks.BUDDING_AMETHYST)) {
                                    continue;
                                }

                                int x = (chunkX << 4) + localX;
                                int z = (chunkZ << 4) + localZ;
                                int dx = x - origin.getX();
                                int dz = z - origin.getZ();
                                int horizontalDistance = dx * dx + dz * dz;
                                if (horizontalDistance > radiusSquared) {
                                    continue;
                                }

                                int y = baseY + localY;
                                int dy = y - origin.getY();
                                double distance = horizontalDistance + dy * dy;
                                if (distance < nearestDistance) {
                                    nearestDistance = distance;
                                    nearest = new BlockPos(x, y, z);
                                }
                            }
                        }
                    }
                }
            }
        }
        return nearest;
    }

    private static void clear() {
        cachedLevel = null;
        cachedTarget = null;
        lastScanPosition = null;
        lastScanTick = Long.MIN_VALUE;
    }
}
