// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.cooking.system;

import org.terasology.crafting.events.OnRecipeCrafted;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.hunger.component.FoodComponent;

@RegisterSystem(RegisterMode.AUTHORITY)
public class FillingModifier extends BaseComponentSystem {

    /**
     * An event to modify the filling value of cooked produce based on filling value of ingredients
     *
     * @param event The OnRecipeCrafted event
     * @param entity The crafted entity
     * @param foodComponent Filter food component to make sure it is a food produce
     */
    @ReceiveEvent
    public void onRecipeCraftedEvent(OnRecipeCrafted event, EntityRef entity, FoodComponent foodComponent) {
        EntityRef[] ingredients = event.getIngredients();
        int modifyFilling = 0;
        FoodComponent ingredientFoodComponent;

        for (int i = 0; i < ingredients.length; i++) {
            if (ingredients[i].hasComponent(FoodComponent.class)) {
                ingredientFoodComponent = ingredients[i].getComponent(FoodComponent.class);
                modifyFilling += ingredientFoodComponent.filling.getValue() - ingredientFoodComponent.filling.getBaseValue();
            }
        }
        entity.getComponent(FoodComponent.class).filling.preAdd(modifyFilling);
    }
}
