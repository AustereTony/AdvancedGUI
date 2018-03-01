package ru.austeretony.advancedgui.player.inventory.slot;

import org.lwjgl.opengl.GL11;

import libs.austeretony.advancedgui.container.slot.GUISlotRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
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
           	
        GlStateManager.enableDepth();      
        
        renderItem.renderItemAndEffectIntoGUI(slot.getStack(), slot.xPos, slot.yPos);
        renderItem.renderItemOverlayIntoGUI(this.mc.fontRenderer, slot.getStack(), slot.xPos, slot.yPos, null);
        
        if (slot.getStack() != null) {
        	
        	this.mc.fontRenderer.drawStringWithShadow(slot.getStack().getDisplayName().length() < 12 ? slot.getStack().getDisplayName() : slot.getStack().getDisplayName().substring(0, 11).concat("..."), slot.xPos + 20, slot.yPos + 5, 0xFFF2F2F2);
        }       
	}

	//Рендер стандартной подсветки.
	@Override
	public void drawSlotHighlighting(Slot slot) {
		
        GlStateManager.disableDepth();        
        GlStateManager.disableLighting();     
    
		GL11.glColorMask(true, true, true, false);

		this.drawGradientRect(slot.xPos, slot.yPos, slot.xPos + this.getWidth(), slot.yPos + this.getHeight(), - 2130706433, - 2130706433, 300.0);
	
		GL11.glColorMask(true, true, true, true);

        GlStateManager.enableLighting();        
        GlStateManager.enableDepth();  
	}
}
