// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.cooking.system;

import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.engine.logic.players.LocalPlayer;
import org.terasology.engine.registry.CoreRegistry;
import org.terasology.inGameHelpAPI.event.OnAddNewCategoryEvent;

/**
 * This system is used to add the CookingCategory help category by sending it to the appropriate handler system over in
 * the InGameHelp module.
 */
@RegisterSystem
public class CookingInGameHelpCommonSystem extends BaseComponentSystem implements UpdateSubscriberSystem {
    /**
     * Flag for checking if the category has been sent.
     */
    boolean hasSent = false;

    /**
     * Update the system. Though in this case, only once.
     *
     * @param delta Time between this and the last update.
     */
    @Override
    public void update(float delta) {
        // Create a new instance of the CookingCategory and send it through an event once.
        if (!hasSent) {
            CoreRegistry.get(LocalPlayer.class).getClientEntity().send(new OnAddNewCategoryEvent(new CookingCategory()));
            hasSent = true;
        }
    }
}
