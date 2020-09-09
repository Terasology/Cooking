// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.cooking.system;

import org.terasology.cooking.Cooking;
import org.terasology.engine.entitySystem.prefab.PrefabManager;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.block.BlockManager;
import org.terasology.engine.world.block.BlockUri;
import org.terasology.gestalt.assets.ResourceUrn;
import org.terasology.multiBlock.Basic2DSizeFilter;
import org.terasology.multiBlock.BlockUriEntityFilter;
import org.terasology.multiBlock.MultiBlockFormRecipeRegistry;
import org.terasology.multiBlock.recipe.LayeredMultiBlockFormItemRecipe;
import org.terasology.processing.system.AnyActivityFilter;
import org.terasology.processing.system.ToolTypeEntityFilter;
import org.terasology.workstation.system.WorkstationRegistry;
import org.terasology.workstationCrafting.system.CraftInHandRecipeRegistry;
import org.terasology.workstationCrafting.system.CraftingWorkstationProcessFactory;

/**
 * This system registers all of the Cooking recipes in this module.
 */
@RegisterSystem
public class RegisterCookingRecipes extends BaseComponentSystem {
    @In
    private CraftInHandRecipeRegistry recipeRegistry;
    @In
    private WorkstationRegistry workstationRegistry;
    @In
    private MultiBlockFormRecipeRegistry multiBlockFormRecipeRegistry;
    @In
    private BlockManager blockManager;
    @In
    private PrefabManager prefabManager;

    /**
     * Initialization phase where all of the recipes are added.
     */
    @Override
    public void initialise() {
        // Register the process factory for generic Cooking process recipes. Because there are no custom operations or
        // special traits on the Cooking recipes that require manual registration, all recipes are automatically
        // registered using this base factory.
        workstationRegistry.registerProcessFactory(Cooking.COOKING_PROCESS_TYPE,
                new CraftingWorkstationProcessFactory());
        addWorkstationFormingRecipes();
    }

    /**
     * Add the recipe for building the Cooking Station.
     */
    private void addWorkstationFormingRecipes() {
        LayeredMultiBlockFormItemRecipe cookingStationRecipe = new LayeredMultiBlockFormItemRecipe(
                new ToolTypeEntityFilter("hammer"), new Basic2DSizeFilter(2, 1), new AnyActivityFilter(),
                "Cooking:CookingStation", null);
        cookingStationRecipe.addLayer(1, 1, new BlockUriEntityFilter(new BlockUri("CoreAssets:Brick")));
        cookingStationRecipe.addLayer(1, 1, new BlockUriEntityFilter(new BlockUri(new ResourceUrn("CoreAssets" +
                ":CobbleStone"), new ResourceUrn(("Engine:EighthBlock")))));
        multiBlockFormRecipeRegistry.addMultiBlockFormItemRecipe(cookingStationRecipe);
    }
}
