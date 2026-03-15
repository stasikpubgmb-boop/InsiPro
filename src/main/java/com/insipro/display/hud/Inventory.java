package com.insipro.display.hud;

import com.insipro.features.impl.render.Hud;
import com.insipro.utils.display.render.post.KawaseBlur;
import com.insipro.utils.interactions.interact.PlayerInteractionHelper;
import com.insipro.utils.theme.ThemeManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import com.insipro.utils.client.managers.api.draggable.AbstractDraggable;
import com.insipro.common.animation.implement.EaseOut;
import com.insipro.utils.display.render.font.FontRenderer;
import com.insipro.utils.display.render.font.Fonts;
import com.insipro.utils.display.render.shape.ShapeProperties;
import com.insipro.utils.display.color.ColorAssist;
import com.insipro.utils.display.render.geometry.Render2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Inventory extends AbstractDraggable {
    List<ItemStack> stacks = new ArrayList<>();

    public Inventory() {
        super("Inventory", 385, 40, 123, 60, true);
        this.scaleAnimation = new EaseOut().setValue(1).setMs(100);
    }

    @Override
    public boolean visible() {
        return !stacks.stream().filter(stack -> !stack.isEmpty()).toList().isEmpty() || PlayerInteractionHelper.isChat(mc.currentScreen);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            FontRenderer essenceFont = Fonts.getSize(15, Fonts.Type.ESSENCE);
            float pX = getX() + getWidth() - essenceFont.getStringWidth("p") - 8;
            float pY = getY() + 9.5f;
            float pWidth = essenceFont.getStringWidth("p");
            float pHeight = essenceFont.getStringHeight("p");
            
            if (mouseX >= pX && mouseX <= pX + pWidth && mouseY >= pY && mouseY <= pY + pHeight) {
                java.util.List<String> selected = new java.util.ArrayList<>(Hud.getInstance().interfaceSettings.getSelected());
                selected.remove("Inventory");
                Hud.getInstance().interfaceSettings.setSelected(selected);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void tick() {
        stacks = IntStream.range(9, 36).mapToObj(i -> mc.player.inventory.getStack(i)).toList();
    }

    @Override
    public void drawDraggable(DrawContext context) {
        MatrixStack matrix = context.getMatrices();
        FontRenderer font = Fonts.getSize(15, Fonts.Type.SuisseIntlMedium);
        FontRenderer items = Fonts.getSize(12, Fonts.Type.SuisseIntlMedium);

        long itemCount = stacks.stream().filter(stack -> !stack.isEmpty()).mapToInt(ItemStack::getCount).sum();
        String itemCountText = String.valueOf(itemCount);
        float textWidth =  items.getStringWidth(itemCountText);
        float boxWidth = textWidth + 6;
        if (Hud.blur.isValue()) {
            Render2D.rectangleWithMask(matrix.peek().getPositionMatrix(), getX(), getY(), getWidth(), getHeight(), 5.5f,0, KawaseBlur.INSTANCE.fbos.getFirst().getColorAttachment());


        }
        rectangle.render(ShapeProperties.create(matrix, getX(), getY(), getWidth(), getHeight())
                .round(5.5f)
                .color(ThemeManager.BackgroundGui.getColor())
                .build());
        
        rectangle.render(ShapeProperties.create(matrix, getX() + 3, getY() + 3, getWidth() - 5, 15.5F)
                .round(4f)
                .outlineColor(new Color(33, 33, 33, 255).getRGB())
                .color(ColorAssist.getClientColor(),ColorAssist.getClientColor(), ColorAssist.getClientColor2(),ColorAssist.getClientColor2())
                .build());




        rectangle.render(ShapeProperties.create(matrix, getX() + 3, getY() + 21.5F, getWidth() - 5, getHeight() - 24.5F)
                .round(4f)
                .outlineColor(new Color(33, 33, 33, 255).getRGB())
                .color(ThemeManager.BackgroundSettings.getColor())
                .build());
        
        Fonts.getSize(15, Fonts.Type.ESSENCE).drawString(matrix, "s", getX() + 8f, getY() + 9.9f, ThemeManager.textColor.getColor());
        font.drawString(matrix, getName(), getX() + 18, getY() + 9.5f, ThemeManager.textColor.getColor());
        FontRenderer essenceFont = Fonts.getSize(15, Fonts.Type.ESSENCE);
        essenceFont.drawString(matrix, "p", getX() + getWidth() - essenceFont.getStringWidth("p") - 8, getY() + 9.9f, ThemeManager.textColor.getColor());

        int offsetY = 20;
        int offsetX = 4;
        for (ItemStack stack : stacks) {
            if (offsetX + 1 + 8 > getWidth() - 3) {
                offsetY += 13;
                offsetX = 4;
            }

            Render2D.defaultDrawStack(context, stack, getX() + offsetX + 1, getY() + offsetY + 1f, false, true, 0.5F);

            offsetX += 13;
        }
    }
}