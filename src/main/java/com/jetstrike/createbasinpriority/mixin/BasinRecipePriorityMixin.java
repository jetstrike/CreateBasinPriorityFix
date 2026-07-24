package com.jetstrike.createbasinpriority.mixin;

import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BasinOperatingBlockEntity.class)
public class BasinRecipePriorityMixin {

    /**
     * Intercept the list of matching recipes right before it is returned.
     * Re-sorts the list based on priority:
     * 1. Mixing/Basin recipes (Highest priority)
     * 2. Automated Crafting / Shapeless recipes
     * 3. 3x3 Packing
     * 4. 2x2 Packing (Lowest priority)
     */
    @Inject(method = "getMatchingRecipes", at = @At("RETURN"), remap = false)
    private void adjustRecipePriority(CallbackInfoReturnable<List<Recipe<?>>> cir) {
        List<Recipe<?>> list = cir.getReturnValue();
        if (list == null || list.size() <= 1) {
            return;
        }

        list.sort((r1, r2) -> {
            int score1 = getRecipePriority(r1);
            int score2 = getRecipePriority(r2);
            
            if (score1 != score2) {
                // Lower score means it appears earlier in the list (higher priority)
                return Integer.compare(score1, score2);
            }
            
            // Fallback to Create's original logic (largest amount of total ingredients first)
            return r2.getIngredients().size() - r1.getIngredients().size();
        });
    }

    /**
     * Assigns a sorting score to recipes:
     * 1 = BasinRecipes (Mixing, Compacting, etc.)
     * 2 = Other standard recipes (Shapeless crafting etc.)
     * 3 = 3x3 Packing
     * 4 = 2x2 Packing
     */
    @Unique
    private int getRecipePriority(Recipe<?> recipe) {
        int ingredientCount = recipe.getIngredients().size();
        
        // If it's a packing recipe (4 or 9 identical ingredients)
        if ((ingredientCount == 4 || ingredientCount == 9) && isPackingRecipe(recipe)) {
            return ingredientCount == 4 ? 4 : 3; // 2x2 gets 4, 3x3 gets 3
        }
        
        // If it's a dedicated Create basin recipe
        if (recipe instanceof BasinRecipe) {
            return 1;
        }
        
        // Standard crafting or other types
        return 2;
    }

    /**
     * Determines if a recipe is a standard packing recipe by checking if all 
     * its ingredients are identical.
     */
    @Unique
    private boolean isPackingRecipe(Recipe<?> recipe) {
        List<Ingredient> ingredients = recipe.getIngredients();
        if (ingredients.isEmpty()) return false;
        
        Ingredient first = ingredients.get(0);
        ItemStack[] firstItems = first.getItems();
        
        if (firstItems.length == 0) return false;
        
        for (int i = 1; i < ingredients.size(); i++) {
            Ingredient current = ingredients.get(i);
            ItemStack[] currentItems = current.getItems();
            
            if (currentItems.length != firstItems.length) {
                return false;
            }
            
            for (int j = 0; j < firstItems.length; j++) {
                if (firstItems[j].getItem() != currentItems[j].getItem()) {
                    return false;
                }
            }
        }
        
        return true;
    }
}
