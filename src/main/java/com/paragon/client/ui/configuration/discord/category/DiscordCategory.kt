package com.paragon.client.ui.configuration.discord.category

import com.paragon.api.module.Category
import com.paragon.api.util.render.ITextRenderer
import com.paragon.api.util.render.RenderUtil
import com.paragon.client.systems.module.impl.client.ClickGUI
import com.paragon.client.ui.configuration.discord.GuiDiscord
import com.paragon.client.ui.configuration.discord.IRenderable
import com.paragon.client.ui.util.animation.Animation
import net.minecraft.item.ItemStack
import org.lwjgl.opengl.GL11.*
import java.awt.Color
import java.awt.Rectangle

/**
 * @author SooStrator1136
 */
class DiscordCategory(val category: Category) : IRenderable, ITextRenderer {

    val rect = Rectangle()

    private val indicator = ItemStack(category.indicator)
    private val nameAnimation = Animation({ 500F }, false, ClickGUI.easing::value)

    override fun render(mouseX: Int, mouseY: Int) {
        val diff = rect.width / 10
        rect.setBounds(rect.x + diff, rect.y + diff, rect.width - (diff * 2), rect.height - (diff * 2))

        //Render the basic icon with its background
        run {
            @Suppress("IncorrectFormatting")
            RenderUtil.drawRoundedRect(
                rect.x.toDouble(),
                rect.y.toDouble(),
                rect.width.toDouble(),
                rect.height.toDouble(),
                15.0,
                15.0,
                15.0,
                15.0,
                if (rect.contains(mouseX, mouseY)) GuiDiscord.CHANNEL_BAR_BACKGROUND.brighter().rgb else GuiDiscord.CHANNEL_BAR_BACKGROUND.rgb
            )

            glPushMatrix()
            glTranslatef(rect.x.toFloat(), rect.y.toFloat(), 0F)
            val scaleFac = rect.width / 16.0
            glScaled(scaleFac, scaleFac, 1.0)
            glTranslatef(-rect.x.toFloat(), -rect.y.toFloat(), 0F)
            RenderUtil.renderItemStack(indicator, rect.x.toFloat(), rect.y.toFloat(), false)
            glPopMatrix()
        }

        //Render the name tooltip
        run {
            nameAnimation.state = rect.contains(mouseX, mouseY)

            RenderUtil.pushScissor(
                rect.x + rect.width - 2.0,
                rect.y.toDouble(),
                (getStringWidth(category.Name) + 5) * nameAnimation.getAnimationFactor(),
                rect.height.toDouble()
            )

            RenderUtil.drawRoundedRect(
                (rect.x + rect.width).toDouble() - 2.0,
                (rect.centerY - (fontHeight / 2) - 2),
                getStringWidth(category.Name).toDouble() + 4,
                fontHeight.toDouble() + 4,
                5.0,
                5.0,
                5.0,
                5.0,
                GuiDiscord.CATEGORY_TEXT_BACKGROUND.rgb
            )
            renderText(
                category.Name,
                (rect.x + rect.width).toFloat(),
                (rect.centerY - (fontHeight / 2)).toFloat(),
                Color.WHITE.rgb
            )

            RenderUtil.popScissor()
        }
    }

    override fun onClick(mouseX: Int, mouseY: Int, button: Int) {}
    override fun onKey(keyCode: Int) {}

}