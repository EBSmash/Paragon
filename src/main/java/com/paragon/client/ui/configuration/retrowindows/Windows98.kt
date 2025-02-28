package com.paragon.client.ui.configuration.retrowindows

import com.paragon.Paragon
import com.paragon.api.module.Category
import com.paragon.api.util.render.ITextRenderer
import com.paragon.api.util.render.RenderUtil
import com.paragon.api.util.string.StringUtil
import com.paragon.client.systems.module.impl.client.ClickGUI
import com.paragon.client.ui.configuration.GuiImplementation
import com.paragon.client.ui.configuration.retrowindows.window.Window
import com.paragon.client.ui.configuration.retrowindows.window.category.CategoryWindow
import com.paragon.client.ui.util.Click
import java.awt.Color

/**
 * @author Surge
 */
class Windows98 : GuiImplementation(), ITextRenderer {

    private val windows = ArrayList<Window>()

    init {
        var x = 20f

        Category.values().forEach {
            windows.add(CategoryWindow(it, x, 20f, 100f, 16f))

            x += 105f
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, mouseDelta: Int) {
        var tooltipName = ""
        var tooltipContent = ""

        // Render the last window in the list over the others etc
        windows.reverse()

        windows.forEach { window ->
            window.draw(mouseX.toFloat(), mouseY.toFloat(), mouseDelta)

            if (window is CategoryWindow && window.tooltipName.isNotEmpty()) {
                tooltipName = window.tooltipName
                tooltipContent = window.tooltipContent
            }
        }

        windows.reverse()

        if (tooltipName.isNotEmpty() && tooltipContent.isNotEmpty()) {
            val text = StringUtil.wrap(tooltipContent, 30)
            val width = getStringWidth(text) + 6f
            val height = 18f + (text.split(System.lineSeparator()).size * fontHeight)

            RenderUtil.drawRect(mouseX + 8f, mouseY + 8f, width, height, Color(70, 70, 70).rgb)
            RenderUtil.drawRect(mouseX + 7f, mouseY + 7f, width, height, Color(148, 148, 148).rgb)

            renderText(tooltipName, mouseX + 9f, mouseY + 12f, -1)
            renderText(text, mouseX + 9f, mouseY + 12f + fontHeight, -1)
        }

        Paragon.INSTANCE.taskbar.draw(mouseX, mouseY)
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        windows.forEach { it.mouseClicked(mouseX.toFloat(), mouseY.toFloat(), Click.getClick(mouseButton)) }

        Paragon.INSTANCE.taskbar.mouseClicked(mouseX, mouseY, Click.getClick(mouseButton))
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, mouseButton: Int) {
        windows.forEach { it.mouseReleased(mouseX.toFloat(), mouseY.toFloat(), Click.getClick(mouseButton)) }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        windows.forEach { it.keyTyped(typedChar, keyCode) }
    }

    override fun onGuiClosed() {
        Paragon.INSTANCE.storageManager.saveModules("current")
    }

    override fun doesGuiPauseGame() = ClickGUI.pause.value

}