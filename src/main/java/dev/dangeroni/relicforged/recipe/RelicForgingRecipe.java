package dev.dangeroni.relicforged.recipe;

import com.google.gson.JsonObject;
import dev.dangeroni.relicforged.registry.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public final class RelicForgingRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient handle;
    private final Ingredient head;
    private final ItemStack result;

    public RelicForgingRecipe(ResourceLocation id, Ingredient template, Ingredient base, Ingredient handle, Ingredient head, ItemStack result) {
        this.id = id;
        this.template = template;
        this.base = base;
        this.handle = handle;
        this.head = head;
        this.result = result;
    }

    @Override
    public boolean matches(Container container, Level level) {
        return template.test(container.getItem(0))
                && base.test(container.getItem(1))
                && handle.test(container.getItem(2))
                && head.test(container.getItem(3));
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return result.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.RELIC_FORGING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.relicForgingType();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, template, base, handle, head);
    }

    public static final class Serializer implements RecipeSerializer<RelicForgingRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public RelicForgingRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient template = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "template"));
            Ingredient base = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "base"));
            Ingredient handle = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "handle"));
            Ingredient head = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "head"));
            ItemStack result = net.minecraft.world.item.crafting.ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            return new RelicForgingRecipe(id, template, base, handle, head, result);
        }

        @Override
        public RelicForgingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return new RelicForgingRecipe(id, Ingredient.fromNetwork(buffer), Ingredient.fromNetwork(buffer), Ingredient.fromNetwork(buffer), Ingredient.fromNetwork(buffer), buffer.readItem());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RelicForgingRecipe recipe) {
            recipe.template.toNetwork(buffer);
            recipe.base.toNetwork(buffer);
            recipe.handle.toNetwork(buffer);
            recipe.head.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}
