package com.paragon.client.ui.console

import com.paragon.Paragon
import com.paragon.api.util.Wrapper
import com.paragon.api.util.render.ITextRenderer
import com.paragon.api.util.render.RenderUtil
import com.paragon.client.systems.module.impl.client.Colours
import net.minecraft.client.gui.GuiTextField
import net.minecraft.client.gui.ScaledResolution
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.input.Keyboard
import java.awt.Color
import java.util.*
import kotlin.collections.ArrayList

@SideOnly(Side.CLIENT)
class Console(val title: String, val width: Float, val height: Float) : Wrapper, ITextRenderer {

    // List of lines
    private val lines: MutableList<String> = ArrayList(5)
    private var guiTextField: GuiTextField

    init {
        val scaledResolution = ScaledResolution(minecraft)
        guiTextField = GuiTextField(
            0,
            minecraft.fontRenderer,
            (scaledResolution.scaledWidth / 2 - width / 2).toInt() + 2,
            (scaledResolution.scaledHeight / 2 - height / 2 + height - 11).toInt(),
            width.toInt(),
            11
        )
    }

    fun init() {
        val scaledResolution = ScaledResolution(minecraft)
        guiTextField = GuiTextField(0, minecraft.fontRenderer, (scaledResolution.scaledWidth / 2 - width / 2).toInt() + 3, (scaledResolution.scaledHeight / 2 - height / 2 + height - 13).toInt(), width.toInt() - 6, 11)
    }

    fun draw(mouseX: Int, mouseY: Int) {
        val scaledResolution = ScaledResolution(minecraft)

        /* // Background
        RenderUtil.drawRect((scaledResolution.getScaledWidth() / 2f) - (getWidth() / 2f), (scaledResolution.getScaledHeight() / 2f) - (getHeight() / 2f), getWidth(), getHeight(), 0x90000000);

        RenderUtil.drawRect((scaledResolution.getScaledWidth() / 2f) - (getWidth() / 2f), (scaledResolution.getScaledHeight() / 2f) - (getHeight() / 2f), getWidth(), 14, new Color(23, 23, 23).getRGB());

        // Title
        renderText(getTitle(), ((scaledResolution.getScaledWidth() / 2f) - (getWidth() / 2f)) + 3, ((scaledResolution.getScaledHeight() / 2f) - (getHeight() / 2f)) + 3, -1);

        // Border
        RenderUtil.drawBorder((scaledResolution.getScaledWidth() / 2f) - (getWidth() / 2f), (scaledResolution.getScaledHeight() / 2f) - (getHeight() / 2f), getWidth(), getHeight(), 1, Colours.mainColour.getValue().getRGB());

        // Separator
        RenderUtil.drawRect((scaledResolution.getScaledWidth() / 2f) - (getWidth() / 2f), (scaledResolution.getScaledHeight() / 2f) - (getHeight() / 2f) + 13, getWidth(), 1, Colours.mainColour.getValue().getRGB());

        // Scissor
        RenderUtil.pushScissor((scaledResolution.getScaledWidth() / 2f) - (getWidth() / 2f), (scaledResolution.getScaledHeight() / 2f) - (getHeight() / 2f) + 14.5, getWidth(), getHeight() - 26.5f);

        float lineY = (scaledResolution.getScaledHeight() / 2f) - (getHeight() / 2f) + getHeight() - 26;

        Collections.reverse(lines);

        for (String string : lines) {
            renderText(string, (scaledResolution.getScaledWidth() / 2f) - (getWidth() / 2f) + 2, lineY, -1);
            lineY -= 11;
        }

        Collections.reverse(lines);

        // End scissor
        RenderUtil.popScissor(); */

        val x = (scaledResolution.scaledWidth / 2.0) - (width / 2.0)
        val y = (scaledResolution.scaledHeight / 2.0) - (height / 2.0)

        RenderUtil.drawRoundedRect(x, y, width.toDouble(), height.toDouble(), 5.0, 5.0, 5.0, 5.0, Color(20, 20, 25).rgb)
        RenderUtil.drawRoundedOutline(x, y, width.toDouble(), height.toDouble(), 5.0, 5.0, 5.0, 5.0, 2f, Colours.mainColour.value.rgb)

        renderText(title, (x + 5f).toFloat(), (y + 5f).toFloat(), -1)

        lines.reverse();

        RenderUtil.drawRect(x.toFloat(), (y + 17.5f).toFloat(), width, 1f, Colours.mainColour.value.rgb)

        RenderUtil.pushScissor(
            ((scaledResolution.scaledWidth / 2f) - (width / 2f)).toDouble(), (scaledResolution.scaledHeight / 2f) - (height / 2f) + 20.0,
            width.toDouble(),
            (height - 26.5f).toDouble()
        );

        var lineY = (scaledResolution.scaledHeight / 2f) - (height / 2f) + height - 26;
        for (string in lines) {
            renderText(string, (scaledResolution.scaledWidth / 2f) - (width / 2f) + 5, lineY, -1);
            lineY -= 11;
        }

        lines.reverse();

        RenderUtil.popScissor();

        if (!guiTextField.text.startsWith("> ")) {
            guiTextField.text = "> " + guiTextField.text
        }

        guiTextField.drawTextBox()
    }

    fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        guiTextField.mouseClicked(mouseX, mouseY, mouseButton)
    }

    fun keyTyped(typedChar: Char, keyCode: Int) {
        if (keyCode == Keyboard.KEY_RETURN) {
            Paragon.INSTANCE.commandManager.handleCommands(guiTextField.text.substring(2), true)
            guiTextField.text = ""
            guiTextField.isFocused = false
            return
        }
        guiTextField.textboxKeyTyped(typedChar, keyCode)
    }

    fun addLine(line: String) {
        lines.add(line)
    }
}