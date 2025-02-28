package com.paragon.client.ui.taskbar.start

import com.paragon.Paragon
import com.paragon.api.util.Wrapper
import com.paragon.api.util.Wrapper.mc
import com.paragon.api.util.render.ITextRenderer
import com.paragon.api.util.render.RenderUtil
import com.paragon.client.systems.module.impl.client.Colours
import com.paragon.client.ui.configuration.windows.impl.ChangelogWindow
import com.paragon.client.ui.configuration.windows.impl.ConfigWindow
import com.paragon.client.ui.console.ConsoleGUI
import com.paragon.client.ui.util.Click
import com.paragon.client.ui.util.animation.Animation
import com.paragon.client.ui.util.animation.Easing
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper
import java.awt.Color

/**
 * @author Surge
 * @since 26/07/2022
 */
object StartMenu : Wrapper, ITextRenderer {

    var x = 5f
    var y = 5f
    var width = 60f
    var height = 20f

    val expandAnimation = Animation({ 200f }, false, { Easing.LINEAR })

    val elements = arrayOf(
        StartElement("GUI", {
            if (minecraft.currentScreen != Paragon.INSTANCE.configurationGUI) {
                minecraft.displayGuiScreen(Paragon.INSTANCE.configurationGUI)
                expandAnimation.state = false
            }
        }, x + 3, y + 3, 144f, 16f),

        StartElement("Console", {
            if (minecraft.currentScreen !is ConsoleGUI) {
                Paragon.INSTANCE.configurationGUI.windowsList
                minecraft.displayGuiScreen(ConsoleGUI())
                expandAnimation.state = false
            }
        }, x + 3, y + 19, 144f, 16f),

        StartElement("Changelog", {
            if (Paragon.INSTANCE.configurationGUI.windowsList.any { it is ChangelogWindow }) {
                Paragon.INSTANCE.configurationGUI.windowsList.filterIsInstance<ChangelogWindow>().forEach { it.openAnimation.state = false }
            } else {
                Paragon.INSTANCE.configurationGUI.windowsList.add(ChangelogWindow(200f, 200f, 300f, 250f, 16f))
            }

            expandAnimation.state = false
        }, x + 3, y + 35f, 144f, 16f),

        StartElement("Configs", {
            if (Paragon.INSTANCE.configurationGUI.windowsList.any { it is ConfigWindow }) {
                Paragon.INSTANCE.configurationGUI.windowsList.filterIsInstance<ConfigWindow>().forEach { it.openAnimation.state = false }
            } else {
                Paragon.INSTANCE.configurationGUI.windowsList.add(ConfigWindow(200f, 200f, 200f, 150f, 16f))
            }

            expandAnimation.state = false
        }, x + 3, y + 35f, 144f, 16f)
    )

    fun draw(mouseX: Int, mouseY: Int) {
        val scaledResolution = ScaledResolution(minecraft)

        var startY = scaledResolution.scaledHeight - (226 * expandAnimation.getAnimationFactor())

        if (expandAnimation.getAnimationFactor() > 0) {
            RenderUtil.pushScissor(0.0, scaledResolution.scaledHeight - 226.0, 152.0, 201.0)

            RenderUtil.drawRect(0f, startY.toFloat(), 150f, 200f, Color(148, 148, 148).rgb)

            startY += 3f

            elements.forEach {
                it.y = startY.toFloat()
                it.x = 3f

                it.draw(mouseX, mouseY)

                startY += it.height + 2
            }

            RenderUtil.popScissor()
        }

        RenderUtil.drawRect(0f, scaledResolution.scaledHeight - MathHelper.clamp(228f * expandAnimation.getAnimationFactor().toFloat(), (26f * Paragon.INSTANCE.taskbar.expandAnimation.getAnimationFactor().toFloat()) + 2, 228f), 150f, 2f, Colours.mainColour.value.rgb)

        RenderUtil.drawRect(x + 1, y + 1, width, height, Color(100, 100, 100).rgb)
        RenderUtil.drawRect(x, y, width, height, Color(120 - (if (isHovered(mouseX, mouseY)) 10 else 0), 120 - (if (isHovered(mouseX, mouseY)) 10 else 0), 120 - (if (isHovered(mouseX, mouseY)) 10 else 0)).rgb)
        renderText("Start", x + 22, y + 6, -1)

        minecraft.textureManager.bindTexture(ResourceLocation("paragon", "textures/logo.png"))
        RenderUtil.drawModalRectWithCustomSizedTexture(x + 1, y + 1, 0f, 0f, 18f, 18f, 18f, 18f)
    }

    fun mouseClicked(mouseX: Int, mouseY: Int, click: Click): Boolean {
        var callback = false
        if (isHovered(mouseX, mouseY)) {
            expandAnimation.state = !expandAnimation.state
            callback = true
        }

        if (expandAnimation.getAnimationFactor() == 1.0) {
            elements.forEach {
                if (mouseX.toFloat() in it.x..it.x + it.width && mouseY.toFloat() in it.y..it.y + it.height) {
                    callback = true
                    it.clicked()
                }
            }
        }
        val startY = ScaledResolution(mc).scaledHeight - (226 * expandAnimation.getAnimationFactor())
        if (mouseX in 0..150 && mouseY >= startY && mouseY <= startY + 200) {
            callback = true
        }
        return callback
    }

    fun isHovered(mouseX: Int, mouseY: Int) = mouseX.toFloat() in x..x + width && mouseY.toFloat() in y..y + height

}