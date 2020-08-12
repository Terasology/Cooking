// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.cooking.system;

import org.terasology.crafting.events.OnRecipeCrafted;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.hunger.component.FoodComponent;

@RegisterSystem(RegisterMode.AUTHORITY)
public class FillingModifier extends BaseComponentSystem {

    @ReceiveEvent
    public void onRecipeCraftedEvent(OnRecipeCrafted event, EntityRef entity) {
        EntityRef ingredients[] = event.getIngredients();
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
