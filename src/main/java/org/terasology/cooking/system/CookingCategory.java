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

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.inGameHelpAPI.ItemsCategoryInGameHelpRegistry;
import org.terasology.inGameHelpAPI.components.HelpItem;
import org.terasology.inGameHelpAPI.components.ItemHelpComponent;
import org.terasology.inGameHelpAPI.systems.HelpCategory;
import org.terasology.inGameHelpAPI.ui.ItemWidget;
import org.terasology.inGameHelpAPI.ui.WidgetFlowRenderable;
import org.terasology.logic.common.DisplayNameComponent;
import org.terasology.rendering.assets.font.Font;
import org.terasology.rendering.nui.widgets.browser.data.DocumentData;
import org.terasology.rendering.nui.widgets.browser.data.basic.FlowParagraphData;
import org.terasology.rendering.nui.widgets.browser.data.basic.flow.TextFlowRenderable;
import org.terasology.rendering.nui.widgets.browser.data.html.HTMLDocument;
import org.terasology.rendering.nui.widgets.browser.ui.style.ContainerInteger;
import org.terasology.rendering.nui.widgets.browser.ui.style.FixedContainerInteger;
import org.terasology.rendering.nui.widgets.browser.ui.style.ParagraphRenderStyle;
import org.terasology.rendering.nui.widgets.browser.ui.style.TextRenderStyle;
import org.terasology.utilities.Assets;

import java.util.Map;

/**
 * This help category manages how the Cooking tab (or help document) will function in the in game help registry window.
 */
public class CookingCategory implements HelpCategory {
    /** Name of this category. */
    private final String name = "Cooking";

    /** Reference to the InGameHelpRegistry. This will be necessary for determining what prefabs fall under this category. */
    private ItemsCategoryInGameHelpRegistry itemsCategoryInGameHelpRegistry;

    /** Create a mapping of Strings to DocuemntData to store the item help subpages. */
    Map<String, DocumentData> items = Maps.newHashMap();

    /** Reference to the root HTML document. */
    HTMLDocument rootDocument;

    /** Reference to the current document data. This is the current help document that the user is on. */
    DocumentData currentDocument;

    /**
     * Default constructor. Use this if you don't have an ItemsCategoryInGameHelpRegistry instance yet.
     */
    public CookingCategory() {
    }

    /**
     * Create an instance of this help category.
     *
     * @param itemsCategoryInGameHelpRegistry   Reference to the items category in game help registry. This will contain
     *                                          all known items that have an InGameHelp component.
     */
    public CookingCategory(ItemsCategoryInGameHelpRegistry itemsCategoryInGameHelpRegistry) {
        this.itemsCategoryInGameHelpRegistry = itemsCategoryInGameHelpRegistry;
    }

    /**
     * Set the reference to the titular registry.
     *
     * @param reg   Reference to the items category in game help registry. This will contain all known items that have
     *              an InGameHelp component.
     */
    public void setRegistry(ItemsCategoryInGameHelpRegistry reg) {
        this.itemsCategoryInGameHelpRegistry = reg;
    }

    /**
     * Initialize the help document for Alchemy.
     */
    private void initialise() {
        // Set the title render and paragraph styles.
        TextRenderStyle titleRenderStyle = new TextRenderStyle() {
            @Override
            public Font getFont(boolean hyperlink) {
                return Assets.getFont("engine:title").get();
            }
        };
        ParagraphRenderStyle titleParagraphStyle = new ParagraphRenderStyle() {
            @Override
            public ContainerInteger getParagraphPaddingTop() {
                return new FixedContainerInteger(5);
            }
        };

        // Create the root document and add the item list paragraph to it. This paragraph will contain all of the items
        // that fall under the Cooking category. Specifically, links to the individual prefab help documents.
        rootDocument = new HTMLDocument(null);
        FlowParagraphData itemListParagraph = new FlowParagraphData(null);
        rootDocument.addParagraph(itemListParagraph);

        // Iterate through all known prefabs to add all applicable prefabs to this document.
        for (Prefab itemPrefab : itemsCategoryInGameHelpRegistry.getKnownPrefabs()) {
            // Create a new HTML document for this prefab.
            HTMLDocument documentData = new HTMLDocument(null);

            // If this prefab doesn't this have an ItemHelpComponent, add one to it.
            ItemHelpComponent helpComponent = itemPrefab.getComponent(ItemHelpComponent.class);
            if (helpComponent == null) {
                helpComponent = new ItemHelpComponent();
                helpComponent.paragraphText.add("An unknown item.");
            }

            // Make sure this prefab's help category is the same as this one. Otherwise, don't add it to the Alchemy
            // help documents list.
            if (getCategoryName().equalsIgnoreCase(helpComponent.getCategory())) {
                // Get the icon and name of this prefab, and add it to the document.
                FlowParagraphData imageNameParagraph = new FlowParagraphData(null);
                documentData.addParagraph(imageNameParagraph);
                imageNameParagraph.append(new WidgetFlowRenderable(new ItemWidget(itemPrefab.getName()), 48, 48, itemPrefab.getName()));
                DisplayNameComponent displayNameComponent = itemPrefab.getComponent(DisplayNameComponent.class);
                if (displayNameComponent != null) {
                    imageNameParagraph.append(new TextFlowRenderable(displayNameComponent.name, titleRenderStyle, null));
                } else {
                    imageNameParagraph.append(new TextFlowRenderable(itemPrefab.getName(), titleRenderStyle, null));
                }

                // Now, add a section for the related prefabs.
                helpComponent.addHelpItemSection(documentData);

                // Add all the other ones from components.
                for (HelpItem helpItem : Iterables.filter(itemPrefab.iterateComponents(), HelpItem.class)) {
                    if (helpItem != helpComponent) {
                        helpItem.addHelpItemSection(documentData);
                    }
                }

                // Add all the other ones from code registered HelpItems.
                for (HelpItem helpItem : itemsCategoryInGameHelpRegistry.getHelpItems(itemPrefab)) {
                    helpItem.addHelpItemSection(documentData);
                }

                // Add this HTML document to the help documents list.
                items.put(itemPrefab.getName(), documentData);

                // Add this to the root document.
                itemListParagraph.append(new WidgetFlowRenderable(new ItemWidget(itemPrefab.getName()), 48, 48, itemPrefab.getName()));
            }
        }
    }

    /**
     * Get the category name of this help section.
     *
     * @return  The name of this category,
     */
    @Override
    public String getCategoryName() {
        return name;
    }

    /**
     * Get the document data for this category. If this category hasn't been initialized yet, then do so.
     *
     * @return  Either the root document if the current document is null, or the current document.
     */
    @Override
    public DocumentData getDocumentData() {
        initialise();
        if (currentDocument == null) {
            return rootDocument;
        } else {
            return currentDocument;
        }
    }

    /**
     * Reset the current document to null, taking he user ack to the main help page.
     */
    @Override
    public void resetNavigation() {
        currentDocument = null;
    }

    /**
     * Handle navigation between the top and sub-documents.
     *
     * @param hyperlink     What document to navigate to.
     * @return              True if the document linked to exists. False if not.
     */
    @Override
    public boolean handleNavigate(String hyperlink) {
        if (items.size() == 0) {
            // Handle the case where we navigate before we have shown the screen.  There is probably a better way to do this.
            initialise();
        }

        // If the document linked to by the hyperlink exists, navigate to that document page and return true.
        // Otherwise, return false.
        if (items.containsKey(hyperlink)) {
            currentDocument = items.get(hyperlink);
            return true;
        } else {
            return false;
        }
    }
}
