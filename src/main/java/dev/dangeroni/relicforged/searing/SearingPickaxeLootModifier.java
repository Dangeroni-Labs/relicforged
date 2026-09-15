package dev.dangeroni.relicforged.searing;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.dangeroni.relicforged.registry.ModItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public final class SearingPickaxeLootModifier extends LootModifier {
    public static final Codec<SearingPickaxeLootModifier> CODEC = RecordCodecBuilder.create(instance -> codecStart(instance)
            .apply(instance, SearingPickaxeLootModifier::new));

    public SearingPickaxeLootModifier(net.minecraft.world.level.storage.loot.predicates.LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (!context.hasParam(LootContextParams.BLOCK_STATE) || !context.hasParam(LootContextParams.TOOL)) {
            return generatedLoot;
        }

        ItemStack tool = context.getParam(LootContextParams.TOOL);
        if (!tool.is(ModItems.BLACKENED_PICKAXE.get())
                || !SearingHelper.isSearingEnabled(tool)
                || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) {
            return generatedLoot;
        }

        ObjectArrayList<ItemStack> transformedLoot = new ObjectArrayList<>();
        for (ItemStack drop : generatedLoot) {
            context.getLevel().getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(drop.copyWithCount(1)), context.getLevel())
                    .ifPresentOrElse(recipe -> addSmeltingResult(transformedLoot, recipe, drop, context),
                            () -> transformedLoot.add(drop));
        }
        return transformedLoot;
    }

    private static void addSmeltingResult(ObjectArrayList<ItemStack> loot, SmeltingRecipe recipe, ItemStack input, LootContext context) {
        ItemStack result = recipe.getResultItem(context.getLevel().registryAccess()).copy();
        if (result.isEmpty()) {
            loot.add(input);
            return;
        }

        long remaining = (long) result.getCount() * input.getCount();
        int maxStackSize = result.getMaxStackSize();
        while (remaining > 0) {
            int count = (int) Math.min(remaining, maxStackSize);
            ItemStack output = result.copy();
            output.setCount(count);
            loot.add(output);
            remaining -= count;
        }
    }
}
