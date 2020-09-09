// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.cooking.ui;

import org.terasology.cooking.Cooking;
import org.terasology.engine.core.Time;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.logic.players.LocalPlayer;
import org.terasology.engine.registry.CoreRegistry;
import org.terasology.engine.rendering.nui.BaseInteractionScreen;
import org.terasology.fluid.component.FluidComponent;
import org.terasology.fluid.component.FluidInventoryComponent;
import org.terasology.fluid.system.FluidRegistry;
import org.terasology.fluid.ui.FluidContainerWidget;
import org.terasology.heat.component.HeatProducerComponent;
import org.terasology.heat.ui.ThermometerWidget;
import org.terasology.inventory.rendering.nui.layers.ingame.InventoryGrid;
import org.terasology.math.TeraMath;
import org.terasology.nui.databinding.Binding;
import org.terasology.nui.databinding.ReadOnlyBinding;
import org.terasology.nui.widgets.UILoadBar;
import org.terasology.processing.ui.VerticalTextureProgressWidget;
import org.terasology.processing.ui.WorkstationScreenUtils;
import org.terasology.workstation.component.WorkstationInventoryComponent;
import org.terasology.workstation.component.WorkstationProcessingComponent;
import org.terasology.workstationCrafting.ui.workstation.StationAvailableRecipesWidget;

import java.util.List;

/**
 * This interaction screen defines how the CookingStation will look and be interacted with.
 */
public class CookingStationWindow extends BaseInteractionScreen {

    // The following will store references to the various UI window widgets.
    private InventoryGrid fluidContainerInput;
    private InventoryGrid fluidContainerOutput;
    private FluidContainerWidget fluidContainer;
    private InventoryGrid ingredientsInventory;
    private InventoryGrid toolsInventory;
    private ThermometerWidget temperature;
    private VerticalTextureProgressWidget burn;
    private InventoryGrid fuelInput;
    private StationAvailableRecipesWidget availableRecipes;
    private InventoryGrid resultInventory;
    private UILoadBar craftingProgress;

    /**
     * Initialize all of the UI widgets of this window.
     */
    @Override
    public void initialise() {
        ingredientsInventory = find("ingredientsInventory", InventoryGrid.class);
        toolsInventory = find("toolsInventory", InventoryGrid.class);

        fluidContainerInput = find("fluidContainerInput", InventoryGrid.class);
        fluidContainer = find("fluidContainer", FluidContainerWidget.class);
        fluidContainerOutput = find("fluidContainerOutput", InventoryGrid.class);

        temperature = find("temperature", ThermometerWidget.class);

        burn = find("burn", VerticalTextureProgressWidget.class);
        burn.setMinY(76);
        burn.setMaxY(4);

        fuelInput = find("fuelInput", InventoryGrid.class);

        availableRecipes = find("availableRecipes", StationAvailableRecipesWidget.class);

        craftingProgress = find("craftingProgress", UILoadBar.class);

        resultInventory = find("resultInventory", InventoryGrid.class);

        InventoryGrid playerInventory = find("playerInventory", InventoryGrid.class);

        // Use the LocalPlayer as the targetted entity, and set the player inventory display to have 30 cells max
        // on the display.
        playerInventory.setTargetEntity(CoreRegistry.get(LocalPlayer.class).getCharacterEntity());
        playerInventory.setCellOffset(10);
        playerInventory.setMaxCellCount(30);
    }

    /**
     * Upon opening the HerbalismStation window, setup the UI widgets.
     *
     * @param station EntityRef of the workstation in question.
     */
    @Override
    protected void initializeWithInteractionTarget(final EntityRef station) {
        // Get the inventory of the target workstation.
        WorkstationInventoryComponent workstationInventory = station.getComponent(WorkstationInventoryComponent.class);

        // Get the slot assignments of this inventory.
        WorkstationInventoryComponent.SlotAssignment fluidInputAssignments =
                workstationInventory.slotAssignments.get("FLUID_INPUT");

        // Setup the inventory grids for the following widgets.
        WorkstationScreenUtils.setupInventoryGrid(station, ingredientsInventory, "INPUT");
        WorkstationScreenUtils.setupInventoryGrid(station, toolsInventory, "TOOL");
        WorkstationScreenUtils.setupInventoryGrid(station, fluidContainerInput, "FLUID_CONTAINER_INPUT");
        WorkstationScreenUtils.setupInventoryGrid(station, fluidContainerOutput, "FLUID_CONTAINER_OUTPUT");
        WorkstationScreenUtils.setupInventoryGrid(station, fuelInput, "FUEL");
        WorkstationScreenUtils.setupInventoryGrid(station, resultInventory, "OUTPUT");

        // Set the min and max size values of the fluid container.
        fluidContainer.setMinX(4);
        fluidContainer.setMaxX(45);
        fluidContainer.setMinY(145);
        fluidContainer.setMaxY(4);

        fluidContainer.setEntity(station);

        // Define the slot used for fluids.
        final int waterSlot = fluidInputAssignments.slotStart;
        fluidContainer.setSlotNo(waterSlot);

        // Bind the tooltip strings for all the widgets.
        ingredientsInventory.bindTooltipString(
                new ReadOnlyBinding<String>() {
                    @Override
                    public String get() {
                        return "Place ingredients here.";
                    }
                }
        );

        toolsInventory.bindTooltipString(
                new ReadOnlyBinding<String>() {
                    @Override
                    public String get() {
                        return "Place tools here.";
                    }
                }
        );

        fuelInput.bindTooltipString(
                new ReadOnlyBinding<String>() {
                    @Override
                    public String get() {
                        return "Place fuel for burner here.";
                    }
                }
        );

        fluidContainerInput.bindTooltipString(
                new ReadOnlyBinding<String>() {
                    @Override
                    public String get() {
                        return "Place fluid container here.";
                    }
                }
        );

        fluidContainerOutput.bindTooltipString(
                new ReadOnlyBinding<String>() {
                    @Override
                    public String get() {
                        return "Fluid container is returned here after use.";
                    }
                }
        );

        resultInventory.bindTooltipString(
                new ReadOnlyBinding<String>() {
                    @Override
                    public String get() {
                        return "Resultant product is sent here.";
                    }
                }
        );

        fluidContainer.bindTooltipString(
                new ReadOnlyBinding<String>() {
                    // Here, details about the fluid inventory's fluid is returned.
                    @Override
                    public String get() {
                        FluidInventoryComponent fluidInventory = station.getComponent(FluidInventoryComponent.class);
                        final FluidComponent fluid =
                                fluidInventory.fluidSlots.get(waterSlot).getComponent(FluidComponent.class);
                        if (fluid == null) {
                            return "0ml";
                        } else {
                            FluidRegistry fluidRegistry = CoreRegistry.get(FluidRegistry.class);
                            return TeraMath.floorToInt(fluid.volume * 1000) + "ml of " + fluidRegistry.getDisplayName(fluid.fluidType);
                        }
                    }
                });

        WorkstationScreenUtils.setupTemperatureWidget(station, temperature, 20f);

        burn.bindValue(
                new Binding<Float>() {
                    // Return a bar graph indicator of the current fuel status of the workstation.
                    @Override
                    public Float get() {
                        HeatProducerComponent heatProducer = station.getComponent(HeatProducerComponent.class);
                        List<HeatProducerComponent.FuelSourceConsume> consumedFuel = heatProducer.fuelConsumed;
                        if (consumedFuel.size() == 0) {
                            return 0f;
                        }
                        long gameTime = CoreRegistry.get(Time.class).getGameTimeInMs();

                        HeatProducerComponent.FuelSourceConsume lastConsumed =
                                consumedFuel.get(consumedFuel.size() - 1);
                        if (gameTime > lastConsumed.startTime + lastConsumed.burnLength) {
                            return 0f;
                        }
                        return 1f - (1f * (gameTime - lastConsumed.startTime) / lastConsumed.burnLength);
                    }

                    @Override
                    public void set(Float value) {
                    }
                });

        availableRecipes.setStation(station);

        craftingProgress.bindVisible(
                new Binding<Boolean>() {
                    // Return a progress bar indicating the current progress of crafting (if currently running).
                    @Override
                    public Boolean get() {
                        WorkstationProcessingComponent processing =
                                station.getComponent(WorkstationProcessingComponent.class);
                        if (processing == null) {
                            return false;
                        }
                        WorkstationProcessingComponent.ProcessDef cookingProcess =
                                processing.processes.get(Cooking.COOKING_PROCESS_TYPE);
                        return cookingProcess != null;
                    }

                    @Override
                    public void set(Boolean value) {
                    }
                }
        );
        craftingProgress.bindValue(
                new Binding<Float>() {
                    // Return the current progress percentage.
                    @Override
                    public Float get() {
                        WorkstationProcessingComponent processing =
                                station.getComponent(WorkstationProcessingComponent.class);
                        if (processing == null) {
                            return 1f;
                        }
                        WorkstationProcessingComponent.ProcessDef cookingProcess =
                                processing.processes.get(Cooking.COOKING_PROCESS_TYPE);
                        if (cookingProcess == null) {
                            return 1f;
                        }

                        long gameTime = CoreRegistry.get(Time.class).getGameTimeInMs();

                        return 1f * (gameTime - cookingProcess.processingStartTime) / (cookingProcess.processingFinishTime - cookingProcess.processingStartTime);
                    }

                    @Override
                    public void set(Float value) {
                    }
                }
        );
    }

    @Override
    public boolean isModal() {
        return false;
    }

    /**
     * Update the available recipes widget on the next tick.
     */
    public void updateAvailableRecipes() {
        availableRecipes.updateNextTick();
    }
}
