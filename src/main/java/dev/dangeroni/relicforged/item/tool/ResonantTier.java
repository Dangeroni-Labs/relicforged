package dev.dangeroni.relicforged.item.tool;

import dev.dangeroni.relicforged.registry.ModItems;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public enum ResonantTier implements Tier {
    INSTANCE;

    @Override
    public int getUses() {
        return 610;
    }

    @Override
    public float getSpeed() {
        return 7.0F;
    }

    @Override
    public float getAttackDamageBonus() {
        return 2.0F;
    }

    @Override
    public int getLevel() {
        return 2;
    }

    @Override
    public int getEnchantmentValue() {
        return 16;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(ModItems.RESONANT_ALLOY.get());
    }
}
