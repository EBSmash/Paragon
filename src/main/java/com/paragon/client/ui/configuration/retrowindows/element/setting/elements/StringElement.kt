package com.paragon.client.ui.configuration.retrowindows.element.setting.elements

import com.paragon.api.setting.Setting
import com.paragon.api.util.render.RenderUtil
import com.paragon.client.systems.module.impl.client.ClickGUI
import com.paragon.client.systems.module.impl.client.Colours
import com.paragon.client.ui.configuration.retrowindows.element.module.ModuleElement
import com.paragon.client.ui.configuration.retrowindows.element.setting.SettingElement
import com.paragon.client.ui.util.Click
import com.paragon.client.ui.util.animation.Animation
import net.minecraft.util.ChatAllowedCharacters
import net.minecraft.util.math.MathHelper
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11.glScalef
import java.awt.Color

/**
 * @author Surge
 */
class StringElement(parent: ModuleElement, setting: Setting<String>, x: Float, y: Float, width: Float, height: Float) : SettingElement<String>(parent, setting, x, y, width, height) {

    private val listening = Animation(ClickGUI.animationSpeed::value, false, ClickGUI.easing::value)

    override fun draw(mouseX: Float, mouseY: Float, mouseDelta: Int) {
        super.draw(mouseX, mouseY, mouseDelta)

        RenderUtil.drawRect(x + 3, y + 3, width - 4, height - 4, Color(100, 100, 100).rgb)
        RenderUtil.drawRect(x + 2, y + 2, width - 4, height - 4, Color(130, 130, 130).rgb)

        RenderUtil.drawHorizontalGradientRect(x + 2, y + 2,  ((width - 4) * listening.getAnimationFactor()).toFloat(), height - 4, Colours.mainColour.value.rgb, if (ClickGUI.gradient.value) Colours.mainColour.value.brighter().brighter().rgb else Colours.mainColour.value.rgb)

        glScalef(0.8f, 0.8f, 0.8f)

        val scaleFactor = 1 / 0.8f
        renderText(setting.name, (x + 5) * scaleFactor, (y + 5f) * scaleFactor, -1)

        val valueX: Float = (x + width - getStringWidth(setting.value) * 0.8f - 5) * scaleFactor

        renderText(setting.value, valueX, (y + 5f) * scaleFactor, Color(190, 190, 190).rgb)

        glScalef(scaleFactor, scaleFactor, scaleFactor)

        if (expanded.getAnimationFactor() > 0) {
            var yOffset = 0f

            val scissorY: Double = MathHelper.clamp(y + height, parent.parent.y + parent.parent.height, ((parent.parent.y + parent.parent.scissorHeight) - getSubSettingHeight() * expanded.getAnimationFactor().toFloat()) + height).toDouble()

            val scissorHeight: Double = MathHelper.clamp(getSubSettingHeight().toDouble() * expanded.getAnimationFactor(), 0.0, parent.parent.scissorHeight.toDouble())

            RenderUtil.pushScissor(x.toDouble(), scissorY, width.toDouble(), scissorHeight)

            subSettings.forEach {
                it.x = x + 2
                it.y = y + height + yOffset

                it.draw(mouseX, mouseY, mouseDelta)

                yOffset += it.getTotalHeight()
            }

            RenderUtil.popScissor()
        }
    }

    override fun mouseClicked(mouseX: Float, mouseY: Float, click: Click) {
        super.mouseClicked(mouseX, mouseY, click)

        if (isHovered(mouseX, mouseY) && y in parent.parent.y + parent.parent.height..parent.parent.y + parent.parent.height + parent.parent.scissorHeight) {
            if (click == Click.LEFT) {
                listening.state = !listening.state
            } else if (click == Click.RIGHT) {
                expanded.state = !expanded.state
            }
        }
    }

    override fun keyTyped(character: Char, keyCode: Int) {
        super.keyTyped(character, keyCode)

        if (listening.state) {
            if (keyCode == Keyboard.KEY_BACK) {
                if (setting.value.isNotEmpty()) {
                    setting.setValue(setting.value.substring(0, setting.value.length - 1))
                }
            } else if (keyCode == Keyboard.KEY_RETURN) {
                listening.state = false
            } else if (ChatAllowedCharacters.isAllowedCharacter(character)) {
                setting.setValue(setting.value + character)
            }
        }
    }

}