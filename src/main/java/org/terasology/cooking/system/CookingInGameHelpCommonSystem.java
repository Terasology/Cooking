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
import org.terasology.inGameHelp.event.OnAddNewCategoryEvent;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.registry.CoreRegistry;

@RegisterSystem
public class CookingInGameHelpCommonSystem extends BaseComponentSystem implements UpdateSubscriberSystem {
    boolean hasSent = false;

    @Override
    public void update(float delta) {
        if (!hasSent) {
            CoreRegistry.get(LocalPlayer.class).getClientEntity().send(new OnAddNewCategoryEvent(new CookingCategory()));
            hasSent = true;
        }
    }
}
