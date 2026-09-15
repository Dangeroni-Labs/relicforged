package dev.dangeroni.relicforged.item.tool;

import dev.dangeroni.relicforged.registry.ModItems;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public enum BlackenedTier implements Tier {
    INSTANCE;

    @Override
    public int getUses() {
        return 2378;
    }

    @Override
    public float getSpeed() {
        return 10.0F;
    }

    @Override
    public float getAttackDamageBonus() {
        return 4.0F;
    }

    @Override
    public int getLevel() {
        return 4;
    }

    @Override
    public int getEnchantmentValue() {
        return 12;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(ModItems.BLACKENED_HANDLE.get());
    }
}
