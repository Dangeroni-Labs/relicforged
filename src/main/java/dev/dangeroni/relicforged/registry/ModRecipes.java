package dev.dangeroni.relicforged.registry;

import dev.dangeroni.relicforged.Relicforged;
import dev.dangeroni.relicforged.recipe.RelicForgingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Relicforged.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Relicforged.MOD_ID);
    public static final RegistryObject<RecipeSerializer<RelicForgingRecipe>> RELIC_FORGING_SERIALIZER = SERIALIZERS.register("relic_forging", () -> RelicForgingRecipe.Serializer.INSTANCE);
    public static final RegistryObject<RecipeType<?>> RELIC_FORGING = TYPES.register("relic_forging", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return Relicforged.MOD_ID + ":relic_forging";
        }
    });

    @SuppressWarnings("unchecked")
    public static RecipeType<RelicForgingRecipe> relicForgingType() {
        return (RecipeType<RelicForgingRecipe>) RELIC_FORGING.get();
    }

    private ModRecipes() {
    }
}
