# Create Basin Priority Fix

**Create Basin Priority Fix** is a lightweight NeoForge addon for the Create mod that fixes recipe overlap issues in the Basin by intelligently prioritizing recipes. 

If you've ever tried to mix a Blaze Cake Base and accidentally made a Sugar Cube because the basin processed 4 sugar first, this mod is for you!

## What it does
By default, the Create Basin sorts matching recipes based on the total number of ingredients (descending). This causes 2x2 and 3x3 packing recipes (which have 4 and 9 ingredients) to take priority over standard mixing recipes (like Blaze Cake Base, which only has 3).

This mod intercepts the Basin's recipe matching logic and implements a 4-tier priority system:
1. **Basin Recipes (Highest Priority):** Dedicated Create mixing and compacting recipes always get picked first.
2. **Automated Shapeless Crafting:** Standard vanilla shapeless recipes using different items.
3. **3x3 Packing:** Recipes combining 9 of the exact same item.
4. **2x2 Packing (Lowest Priority):** Recipes combining 4 of the exact same item.

This ensures you can safely dump mixed ingredients into a Basin without accidentally triggering unwanted packing/compacting recipes!

## Compatibility
- **Minecraft:** 1.21.1
- **Mod Loader:** NeoForge
- **Required Mods:** Create 6.0.0+

## Installation
Drop the `.jar` into your `mods` folder. It works out of the box and requires zero configuration!
