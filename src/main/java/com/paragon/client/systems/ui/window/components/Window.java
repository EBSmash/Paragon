package com.paragon.client.systems.ui.window.components;

import com.paragon.api.util.render.RenderUtil;
import com.paragon.api.util.render.TextRenderer;
import com.paragon.client.systems.ui.window.WindowGUI;
import com.paragon.client.systems.ui.window.components.impl.CategoryComponent;
import com.paragon.client.systems.ui.window.components.impl.ModuleButtonComponent;
import com.paragon.client.systems.module.ModuleCategory;
import com.paragon.client.systems.module.impl.client.Colours;
import com.paragon.client.systems.module.impl.client.ClickGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wolfsurge
 * @since 29/01/22
 */
public class Window implements TextRenderer {

    // Window Title
    private String title;

    // X and Y of the window
    private float x, y;

    // Width and Height of the window
    private float width, height;

    // List of category buttons
    private List<CategoryComponent> categoryButtons = new ArrayList<>();

    // The current selected category
    // private CategoryComponent selectedCategory;

    /**
     * Create new window
     * @param title Title of window which is displayed at the top
     * @param x Default X of window
     * @param y Default Y of window
     * @param width Width of window
     * @param height Height of window
     */
    public Window(String title, float x, float y, float width, float height) {
        setTitle(title);
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);

        // Add the category buttons
        float categoryX = getX() + 2;
        for(ModuleCategory c : ModuleCategory.values()) {
            CategoryComponent categoryComponent = new CategoryComponent(this, c, categoryX, getY() + 17);
            categoryButtons.add(categoryComponent);
            categoryX += categoryComponent.getWidth() + 2;
        }

        // Defaults the selected category to combat
        setSelectedCategory(categoryButtons.get(0));
    }

    /**
     * Render the window
     * @param mouseX The mouse's X
     * @param mouseY The mouse's Y
     */
    public void render(int mouseX, int mouseY) {
        // Main Background
        RenderUtil.drawRect(getX(), getY(), getWidth(), getHeight(), new Color(17, 17, 17).getRGB());

        // Title
        RenderUtil.drawRect(getX(), getY() + 15, getWidth(), 1, Colours.mainColour.getColour().getRGB());
        renderText(getTitle(), getX() + 3, getY() + 3.5f, -1);

        // Setting Background
        RenderUtil.drawRect(getX() + 201, getY() + 35, 197, 263, new Color(20, 20, 20).getRGB());

        // Render categories
        for(CategoryComponent categoryComponent : categoryButtons)
            categoryComponent.renderCategory(mouseX, mouseY);

        // Separator
        if(ClickGUI.separator.isEnabled())
            RenderUtil.drawRect(getX(), getY() + 32, getWidth(), 1, Colours.mainColour.getColour().getRGB());

        // Window outline
        if(ClickGUI.windowOutline.isEnabled()) {
            RenderUtil.drawRect(getX(), getY(), 1, getHeight(), Colours.mainColour.getColour().getRGB());
            RenderUtil.drawRect(getX() + getWidth() - 1, getY(), 1, getHeight(), Colours.mainColour.getColour().getRGB());

            RenderUtil.drawRect(getX(), getY(), getWidth(), 1, Colours.mainColour.getColour().getRGB());
            RenderUtil.drawRect(getX(), getY() + getHeight() - 1, getWidth(), 1, Colours.mainColour.getColour().getRGB());
        }
    }

    /**
     * Called when the mouse is clicked
     * @param mouseX The mouse's X
     * @param mouseY The mouse's Y
     * @param mouseButton The button which is clicked
     */
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for(CategoryComponent categoryComponent : categoryButtons) {
            categoryComponent.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    /**
     * Triggered when the mouse is released
     * @param mouseX The mouse's X
     * @param mouseY The mouse's Y
     * @param mouseButton The mouse button that is released
     */
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (ModuleButtonComponent moduleButtonComponent : getSelectedCategory().getModuleButtons()) {
            moduleButtonComponent.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    /**
     * Triggered when a key is pressed
     * @param typedChar The character typed
     * @param keyCode The key code of the character
     */
    public void keyTyped(char typedChar, int keyCode) {
        for (ModuleButtonComponent moduleButtonComponent : getSelectedCategory().getModuleButtons()) {
            moduleButtonComponent.keyTyped(typedChar, keyCode);
        }
    }

    /**
     * Gets the title of the window
     * @return The title of the window
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the window
     * @param title The new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the X of the window
     * @return The X of the window
     */
    public float getX() {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

        return scaledResolution.getScaledWidth() / 2f - 200;
    }

    /**
     * Sets the X of the window
     * @param x The new X
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Gets the Y of the window
     * @return The Y of the window
     */
    public float getY() {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

        return scaledResolution.getScaledHeight() / 2f - 150;
    }

    /**
     * Sets the Y of the window
     * @param y The new Y
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Gets the width of the window
     * @return The width of the window
     */
    public float getWidth() {
        return width;
    }

    /**
     * Sets the width of the window
     * @param width The width of the window
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * Gets the height of the window
     * @return The height of the window
     */
    public float getHeight() {
        return height;
    }

    /**
     * Sets the height of the window
     * @param height The new height
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Gets the category buttons
     * @return The category buttons
     */
    public List<CategoryComponent> getCategoryButtons() {
        return categoryButtons;
    }

    /**
     * Gets the selected category
     * @return The selected category
     */
    public CategoryComponent getSelectedCategory() {
        return WindowGUI.selectedCategory;
    }

    /**
     * Sets the selected category
     * @param selectedCategory The new selected category
     */
    public void setSelectedCategory(CategoryComponent selectedCategory) {
        WindowGUI.selectedCategory = selectedCategory;
    }
}
