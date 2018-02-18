package libs.austeretony.advancedgui.guicontainer.contextmenu;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import libs.austeretony.advancedgui.guicontainer.utils.GUIUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.StatCollector;

/**
 * Класс, реализующий работу по отрисовке и обработке действия GUIPresetAction.
 */
@SideOnly(Side.CLIENT)
public class GUIContextAction {
	
	private Minecraft mc;
	
	public Slot slot;
    
    public int xPosition, yPosition, actionWidth, actionHeight;
    
    private boolean isEnabled, isVisible, isHovered;
    
    private String dispalyName;
    
    public float scaleFactor;
    
    private int 
    backgroundColor = 0xFF202020,
    hoveredBackgroundColor = 0xFF595959,
    enabledTextColor = 0xFFD1D1D1, 
    disabledTextColor = 0xFF707070,
    hoveredTextColor = 0xFFF2F2F2;
    
    private GUIPresetAction presetAction;
        
    /**
     * Оболочка для GUIPresetAction, отвечает за рендер и т.д.
     * 
     * @param presetAction ассоциированное действие.
     */
    public GUIContextAction(GUIPresetAction presetAction) {

        this.presetAction = presetAction;
        this.dispalyName = presetAction.dispalyName;

        this.mc = Minecraft.getMinecraft();
    }

    public void draw() {
    	
        if (this.isVisible()) {
        	        	                        
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        	
        	GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        	
            GL11.glPushMatrix();

            GL11.glTranslatef(this.xPosition, this.yPosition, 0.0F);
            
            GL11.glScalef(this.scaleFactor, this.scaleFactor, 0.0F);
            
            final int zero = 0;                    	
        		
        	int color;
        		
            if (!this.isEnabled()) {
                	
                color = this.backgroundColor;
            }
                
            else if (this.isHovered()) {
                	
                color = this.hoveredBackgroundColor;
            }
                
            else {
                	
                color = this.backgroundColor;
            }     		
                
            GUIUtils.drawRect(zero, zero, zero + this.actionWidth, zero + this.actionHeight, color);
        	
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        	
        	if (this.getActionName().length() > 0) {
        		
                if (!this.isEnabled()) {
                	
                	color = this.disabledTextColor;
                }
                
                else if (this.isHovered()) {
                	                	
                	color = this.hoveredTextColor;
                }
                
                else {
                	
                	color = this.enabledTextColor;
                }
                                
                this.mc.fontRenderer.drawStringWithShadow(StatCollector.translateToLocal(this.getActionName()), zero + 4, zero + (this.actionHeight - 8) / 2, color);
        	}
        	
            GL11.glPopMatrix();
        	
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }
    
    public void mouseOver(int mouseX, int mouseY, int guiLeft, int guiTop) {
    	
        mouseX -= guiLeft;
        mouseY -= guiTop;
		
    	this.isHovered = this.isEnabled() && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + (int) (this.actionWidth * this.scaleFactor) && mouseY < this.yPosition + (int) (this.actionHeight * this.scaleFactor);   
    }
    
    public boolean isValid(IInventory inventory, Slot slot) {
    	
    	return this.presetAction.isValidAction(inventory, slot, this.mc.thePlayer);
    }

    public boolean performAction(IInventory inventory) {
    	
    	if (Mouse.isButtonDown(0) && this.isHovered()) {
    		
    		this.presetAction.performAction(inventory, this.slot, this.mc.thePlayer);
    		
    		if (this.presetAction.getSound() != null) {
    			
    	        this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(this.presetAction.getSound().fileLocation, this.presetAction.getSound().volume));
    		}
    		
    		return true;
    	}
    	
    	return false;
    }
    
    public String getActionName() {
    	
        return this.dispalyName;
    }
    
    public boolean isHovered() {
    	
        return this.isHovered;
    }
    
    public boolean isVisible() {
    	
    	return this.isVisible;
    }
    
    public void setVisible(boolean isVisible) {
    	
    	this.isVisible = isVisible;
    }
    
    public boolean isEnabled() {
    	
    	return this.isEnabled;
    }
    
    public void setEnabled(boolean isEnabled) {
    	
    	this.isEnabled = isEnabled;
    }
    
    public void setEnabledTextColor(int colorHex) {
    	
    	this.enabledTextColor = colorHex;
    }
    
    public void setDisabledTextColor(int colorHex) {
    	
    	this.disabledTextColor = colorHex;
    }
    
    public void setHoveredTextColor(int colorHex) {
    	
    	this.hoveredTextColor = colorHex;
    }
    
    public void setBackgroundColor(int colorHex) {
    	
    	this.backgroundColor = colorHex;
    }
    
    public void setHoveredBackgroundColor(int colorHex) {
    	
    	this.hoveredBackgroundColor = colorHex;
    }
}

