package libs.austeretony.advancedgui.screen.text;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import libs.austeretony.advancedgui.screen.core.GUISimpleElement;

/**
 * Простой графический элемент в виде строки символов.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUITextLabel extends GUISimpleElement<GUITextLabel> {
	
	public GUITextLabel(int xPosition, int yPosition) {
		
		this.setPosition(xPosition, yPosition);
		
        this.setEnabled(true);
        this.setVisible(true);
	}
	
    @Override
    public void draw() {
    	
        if (this.isVisible()) {
        	
        	GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        	
            GL11.glPushMatrix();
            
            GL11.glTranslatef(this.getX(), this.getY(), 0.0F);
            
            GL11.glScalef(this.getScale(), this.getScale(), 0.0F);                           
        	
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        	
        	if (this.isBackgroundEnabled()) {       		 		
                
                this.drawRect(this.ZERO, this.ZERO, this.ZERO + this.getWidth(), this.ZERO + this.getHeight(), this.getEnabledColor());
        	}
        	
        	if (this.getDisplayText().length() > 0) {
                                
                this.mc.fontRenderer.drawStringWithShadow(this.getDisplayText(), this.ZERO, this.ZERO, this.getEnabledTextColor());
        	}
            
            GL11.glPopMatrix();

            GL11.glEnable(GL12.GL_RESCALE_NORMAL); 
        }
    }
    
    /**
     * Установка текста, отображаемого элементом. 
     * 
     * @param displayText
     * 
     * @return
     */
    @Override
    public GUITextLabel setDisplayText(String displayText) {
    	
    	super.setDisplayText(displayText);
    	
    	this.setSize(this.mc.fontRenderer.getStringWidth(displayText), this.FONT_HEIGHT);
    	
    	return this;
    }
}
