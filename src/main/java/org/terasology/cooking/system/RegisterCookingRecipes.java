/*
 * Copyright 2016 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.cooking.system;

import org.terasology.assets.ResourceUrn;
import org.terasology.cooking.Cooking;
import org.terasology.entitySystem.prefab.PrefabManager;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.multiBlock.Basic2DSizeFilter;
import org.terasology.multiBlock.BlockUriEntityFilter;
import org.terasology.multiBlock.MultiBlockFormRecipeRegistry;
import org.terasology.multiBlock.recipe.LayeredMultiBlockFormItemRecipe;
import org.terasology.processing.system.AnyActivityFilter;
import org.terasology.processing.system.ToolTypeEntityFilter;
import org.terasology.registry.In;
import org.terasology.workstation.system.WorkstationRegistry;
import org.terasology.workstationCrafting.system.CraftInHandRecipeRegistry;
import org.terasology.workstationCrafting.system.CraftingWorkstationProcessFactory;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.BlockUri;

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
        workstationRegistry.registerProcessFactory(Cooking.COOKING_PROCESS_TYPE, new CraftingWorkstationProcessFactory());
        addWorkstationFormingRecipes();
    }

    /**
     * Add the recipe for building the Cooking Station.
     */
    private void addWorkstationFormingRecipes() {
        LayeredMultiBlockFormItemRecipe cookingStationRecipe = new LayeredMultiBlockFormItemRecipe(
                new ToolTypeEntityFilter("hammer"), new Basic2DSizeFilter(2, 1), new AnyActivityFilter(),
                "Cooking:CookingStation", null);
        cookingStationRecipe.addLayer(1, 1, new BlockUriEntityFilter(new BlockUri("CoreBlocks:Brick")));
        cookingStationRecipe.addLayer(1, 1, new BlockUriEntityFilter(new BlockUri(new ResourceUrn("CoreBlocks:CobbleStone"), new ResourceUrn(("Engine:EighthBlock")))));
        multiBlockFormRecipeRegistry.addMultiBlockFormItemRecipe(cookingStationRecipe);
    }
}
