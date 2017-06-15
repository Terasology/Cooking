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

import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.inGameHelpAPI.event.OnAddNewCategoryEvent;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.registry.CoreRegistry;

/**
 * This system is used to add the CookingCategory help category by sending it to the appropriate handler system over in
 * the InGameHelp module.
 */
@RegisterSystem
public class CookingInGameHelpCommonSystem extends BaseComponentSystem implements UpdateSubscriberSystem {
    /** Flag for checking if the category has been sent. */
    boolean hasSent = false;

    /**
     * Update the system. Though in this case, only once.
     *
     * @param delta     Time between this and the last update.
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
