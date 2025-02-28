package com.paragon.client.ui.taskbar

import com.paragon.api.util.Wrapper
import com.paragon.api.util.render.ITextRenderer
import com.paragon.api.util.render.RenderUtil
import com.paragon.client.systems.module.impl.client.Colours
import com.paragon.client.ui.taskbar.start.StartMenu
import com.paragon.client.ui.util.Click
import com.paragon.client.ui.util.animation.Animation
import com.paragon.client.ui.util.animation.Easing
import net.minecraft.client.gui.ScaledResolution
import java.awt.Color

/**
 * @author Surge
 * @since 26/07/2022
 */
object Taskbar : Wrapper, ITextRenderer {

    var tooltip = ""

    val expandAnimation: Animation = Animation({ 500f }, false, { Easing.CUBIC_IN_OUT })
    val timer: Animation = Animation( { 1000f }, false, { Easing.LINEAR })

    fun draw(mouseX: Int, mouseY: Int) {
        val scaledResolution = ScaledResolution(minecraft)
        val hovered = mouseY.toFloat() in scaledResolution.scaledHeight - 80f..scaledResolution.scaledHeight.toFloat()

        var state = false

        if (StartMenu.expandAnimation.state || hovered) {
            state = true
        }

        expandAnimation.state = state

        RenderUtil.drawRect(0f, scaledResolution.scaledHeight - (26f * expandAnimation.getAnimationFactor().toFloat()), scaledResolution.scaledWidth.toFloat(), 26f, Color(148, 148, 148).rgb)

        StartMenu.x = 2f
        StartMenu.y = scaledResolution.scaledHeight - (23f * expandAnimation.getAnimationFactor().toFloat())
        StartMenu.draw(mouseX, mouseY)

        RenderUtil.drawRect(150f, scaledResolution.scaledHeight - (26f * expandAnimation.getAnimationFactor().toFloat()) - 2, scaledResolution.scaledWidth.toFloat() - 150f, 2f, Colours.mainColour.value.rgb)

        if (tooltip != "") {
            RenderUtil.drawRect((scaledResolution.scaledWidth - getStringWidth(tooltip) - 4) + ((getStringWidth(tooltip) + 4) * expandAnimation.getAnimationFactor()).toFloat(), scaledResolution.scaledHeight - 16f, getStringWidth(tooltip) + 4, 13f, 0x90000000.toInt())
            renderText(tooltip, scaledResolution.scaledWidth - getStringWidth(tooltip) - 2, scaledResolution.scaledHeight - 14f, Color.WHITE.rgb)
        }
    }

    fun mouseClicked(mouseX: Int, mouseY: Int, click: Click) {
        if (!StartMenu.mouseClicked(mouseX, mouseY, click)) {
            StartMenu.expandAnimation.state = false
        }
    }

}