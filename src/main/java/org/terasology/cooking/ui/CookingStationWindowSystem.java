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
package org.terasology.cooking.ui;

import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.logic.characters.interactions.InteractionScreenComponent;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.nui.NUIManager;
import org.terasology.module.inventory.events.BeforeItemPutInInventory;
import org.terasology.module.inventory.events.BeforeItemRemovedFromInventory;
import org.terasology.module.inventory.events.InventorySlotStackSizeChangedEvent;
import org.terasology.workstation.component.WorkstationInventoryComponent;
import org.terasology.workstationCrafting.event.CraftingStationUpgraded;

/**
 * This class is designed for handling certain events related to the CookingStation and MixingBowl UIs.
 */
@RegisterSystem
public class CookingStationWindowSystem extends BaseComponentSystem {
    @In
    EntityManager entityManager;

    @In
    private NUIManager nuiManager;

    @Override
    public void initialise() {
    }

    /**
     * Just before an item is placed into one of the cooking station's or mixing bowl's inventory slots, update the UI
     * screen.
     *
     * @param event       Details of this event.
     * @param entity      Entity reference to the workstation. May not be one of the applicable stations though.
     * @param inventory   Inventory of the workstation.
     */
    @ReceiveEvent
    public void itemPutIntoInventorySlot(BeforeItemPutInInventory event, EntityRef entity,
                                         WorkstationInventoryComponent inventory) {
        if (entity.getComponent(InteractionScreenComponent.class).screen.equalsIgnoreCase("Cooking:CookingStation")) {
            CookingStationWindow screen = (CookingStationWindow) nuiManager.getScreen("Cooking:CookingStation");

            if (screen != null) {
                screen.updateAvailableRecipes();
            }
        } else if (entity.getComponent(InteractionScreenComponent.class).screen.equalsIgnoreCase("Cooking:MixingBowl")) {
            MixingBowlWindow screen = (MixingBowlWindow) nuiManager.getScreen("Cooking:MixingBowl");

            if (screen != null) {
                screen.updateAvailableRecipes();
            }
        }
    }

    /**
     * When one of the item stack sizes present in the cooking station's or mixing bowl's inventory slots changes,
     * update the UI screen.
     *
     * @param event       Details of this event.
     * @param entity      Entity reference to the workstation. May not be one of the applicable stations though.
     * @param inventory   Inventory of the workstation.
     */
    @ReceiveEvent
    public void itemPutIntoInventorySlot(InventorySlotStackSizeChangedEvent event, EntityRef entity,
                                         WorkstationInventoryComponent inventory) {
        if (entity.getComponent(InteractionScreenComponent.class).screen.equalsIgnoreCase("Cooking:CookingStation")) {
            CookingStationWindow screen = (CookingStationWindow) nuiManager.getScreen("Cooking:CookingStation");

            if (screen != null) {
                screen.updateAvailableRecipes();
            }
        } else if (entity.getComponent(InteractionScreenComponent.class).screen.equalsIgnoreCase("Cooking:MixingBowl")) {
            MixingBowlWindow screen = (MixingBowlWindow) nuiManager.getScreen("Cooking:MixingBowl");

            if (screen != null) {
                screen.updateAvailableRecipes();
            }
        }
    }

    /**
     * Just before an item is removed from one of the cooking station's or mixing bowl's inventory slots, update the UI
     * screen.
     *
     * @param event       Details of this event.
     * @param entity      Entity reference to the workstation. May not be one of the applicable stations though.
     * @param inventory   Inventory of the workstation.
     */
    @ReceiveEvent
    public void itemRemovedFromInventorySlot(BeforeItemRemovedFromInventory event, EntityRef entity,
                                             WorkstationInventoryComponent inventory) {
        if (entity.getComponent(InteractionScreenComponent.class).screen.equalsIgnoreCase("Cooking:CookingStation")) {
            CookingStationWindow screen = (CookingStationWindow) nuiManager.getScreen("Cooking:CookingStation");

            if (screen != null) {
                screen.updateAvailableRecipes();
            }
        } else if (entity.getComponent(InteractionScreenComponent.class).screen.equalsIgnoreCase("Cooking:MixingBowl")) {
            MixingBowlWindow screen = (MixingBowlWindow) nuiManager.getScreen("Cooking:MixingBowl");

            if (screen != null) {
                screen.updateAvailableRecipes();
            }
        }
    }

    /**
     * When the cooking station or mixing bowl has been upgraded, update the UI screen.
     *
     * @param event       Details of this event.
     * @param entity      Entity reference to the workstation. May not be one of the applicable stations though.
     * @param inventory   Inventory of the workstation.
     */
    @ReceiveEvent
    public void onCraftingStationUpgraded(CraftingStationUpgraded event, EntityRef entity,
                                         WorkstationInventoryComponent inventory) {
        if (entity.getComponent(InteractionScreenComponent.class).screen.equalsIgnoreCase("Cooking:CookingStation")) {
            CookingStationWindow screen = (CookingStationWindow) nuiManager.getScreen("Cooking:CookingStation");

            if (screen != null) {
                screen.updateAvailableRecipes();
            }
        } else if (entity.getComponent(InteractionScreenComponent.class).screen.equalsIgnoreCase("Cooking:MixingBowl")) {
            MixingBowlWindow screen = (MixingBowlWindow) nuiManager.getScreen("Cooking:MixingBowl");

            if (screen != null) {
                screen.updateAvailableRecipes();
            }
        }
    }
}
