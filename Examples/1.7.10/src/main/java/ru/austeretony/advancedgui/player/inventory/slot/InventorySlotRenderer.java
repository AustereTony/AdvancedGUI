package ru.austeretony.advancedgui.player.inventory.slot;

import org.lwjgl.opengl.GL11;

import libs.austeretony.advancedgui.guicontainer.slot.GUISlotRenderer;
import libs.austeretony.advancedgui.guicontainer.utils.GUIUtils;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.inventory.Slot;

public class InventorySlotRenderer extends GUISlotRenderer {

	public InventorySlotRenderer(int slotWidth, int slotHeight) {
		
		super(slotWidth, slotHeight);
	}

	//Нижний слой оставлен пустым.
	@Override
	public void drawSlotBottomLayer(Slot slot) {}

	//Рендер увеличенного по ширине слота с названием предмета в нём.
	@Override
	public void drawSlot(Slot slot, RenderItem renderItem) {
           	
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        
        renderItem.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), slot.getStack(), slot.xDisplayPosition, slot.yDisplayPosition);
        renderItem.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), slot.getStack(), slot.xDisplayPosition, slot.yDisplayPosition, null);
        
        if (slot.getStack() != null) {
        	
        	this.mc.fontRenderer.drawStringWithShadow(slot.getStack().getDisplayName().length() < 12 ? slot.getStack().getDisplayName() : slot.getStack().getDisplayName().substring(0, 11).concat("..."), slot.xDisplayPosition + 20, slot.yDisplayPosition + 5, 0xFFF2F2F2);
        }       
	}

	//Рендер стандартной подсветки.
	@Override
	public void drawSlotHighlighting(Slot slot) {
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);      
    
		GL11.glColorMask(true, true, true, false);

		GUIUtils.drawGradientRect(slot.xDisplayPosition, slot.yDisplayPosition, slot.xDisplayPosition + this.slotWidth, slot.yDisplayPosition + this.slotHeight, - 2130706433, - 2130706433, 300.0);
	
		GL11.glColorMask(true, true, true, true);

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
}
