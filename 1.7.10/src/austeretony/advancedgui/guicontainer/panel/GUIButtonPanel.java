package libs.austeretony.advancedgui.guicontainer.panel;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import libs.austeretony.advancedgui.guicontainer.button.GUIButton;
import libs.austeretony.advancedgui.guicontainer.utils.GUIUtils;
import net.minecraft.client.Minecraft;

/**
 * Панель для кнопок GUIButton.
 */
@SideOnly(Side.CLIENT)
public class GUIButtonPanel {

	private Minecraft mc;
    
    public final int xPosition, yPosition;
    
    public final int buttonWidth, buttonHeight;
    
    private final GUIOrientation orientation;
    
    private boolean isEnabled, isVisible, isBackgroundEnabled;
    
    private int backgroundColor = 0xFF202020;
    
    private final float scaleFactor;   

    public final List<GUIButton> buttons = new ArrayList<GUIButton>();
    
    /**
     * Панель для кнопок.
     * 
     * @param orientation ориентация панели.
     * @param xPosition позиция по x.
     * @param yPosition позиция по y.
     * @param buttonWidth ширина кнопки.
     * @param buttonHeight высота кнопки.
     * @param scaleFactor коэффициент скалирования.
     */
    public GUIButtonPanel(GUIOrientation orientation, int xPosition, int yPosition, int buttonWidth, int buttonHeight, float scaleFactor) {
    	   	
    	this.orientation = orientation;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;
        this.scaleFactor = scaleFactor;
        
        this.mc = Minecraft.getMinecraft();
        
        this.isEnabled = true;
        this.isVisible = true;
    }
    
    /**
     * Метод для добавления кнопки на панель.
     * 
     * @param button добавляемая кнопка.
     */
    public void addButton(GUIButton button) {
    	
    	if (!this.buttons.contains(button)) {
    		
    		int size = this.buttons.size();
    		
    		button.xPosition = this.orientation == GUIOrientation.HORIZONTAL ? this.xPosition + (int) (this.buttonWidth * this.scaleFactor) * size : this.xPosition;
    		button.yPosition = this.orientation == GUIOrientation.VERTICAL ? this.yPosition + (int) (this.buttonHeight * this.scaleFactor) * size : this.yPosition;
    		
    		button.buttonWidth = this.buttonWidth;
    		button.buttonHeight = this.buttonHeight;
    		
    		button.scaleFactor = this.scaleFactor;
    		    		
    		this.buttons.add(button);
    	}
    }
    
    public void draw() {
    	
        if (this.isVisible()) {        
        	                   
            final int zero = 0;
        	
        	if (this.isBackgroundEnabled()) {
        		        		        		
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);     
                
            	GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            	
                GL11.glPushMatrix();

                GL11.glTranslatef(this.xPosition, this.yPosition, 0.0F);
                
                GL11.glScalef(this.scaleFactor, this.scaleFactor, 0.0F);
                        		
                GUIUtils.drawRect(zero, zero, zero + (this.orientation == GUIOrientation.HORIZONTAL ? (int) (this.buttonWidth * this.scaleFactor) * this.buttons.size() : (int) (this.buttonWidth * this.scaleFactor)), zero + (this.orientation == GUIOrientation.VERTICAL ? (int) (this.buttonHeight * this.scaleFactor) * this.buttons.size() + 1 : (int) (this.buttonHeight * this.scaleFactor) + 1), this.backgroundColor);
        	
                GL11.glPopMatrix();
                
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        	}           
        	
            for (GUIButton button : this.buttons) {
                	
            	button.draw();          	
            }
        }
    }
    
    public void drawPopup(int mouseX, int mouseY, int guiLeft, int guiTop) {
    	
        if (this.isVisible()) {        
        	
            for (GUIButton button : this.buttons) {
                	
            	button.drawPopup(mouseX, mouseY, guiLeft, guiTop);          	
            }
        }
    }
    
    public void mouseOver(int mouseX, int mouseY, int guiLeft, int guiTop) {
    	
    	if (this.isEnabled()) {
    	
    		for (GUIButton button : this.buttons) {

    			button.mouseOver(mouseX, mouseY, guiLeft, guiTop);
    		}
    	}
    }
    
    public boolean mouseClicked() {
    	
    	if (this.isEnabled()) {

    		for (GUIButton button : this.buttons) {
        	
    			return button.mouseClicked();
    		}
    	}
        
		return false;
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
    
    public boolean isBackgroundEnabled() {
    	
    	return this.isBackgroundEnabled;
    }
    
    public void setBackgroundEnabled(boolean isEnabled) {
    	
    	this.isBackgroundEnabled = isEnabled;
    }
    
    public void setBackgroundColor(int colorHex) {
    	
    	this.backgroundColor = colorHex;
    }
    
    /**
     * Enum для определения ориентации панели.
     */
    public enum GUIOrientation {
    	
    	HORIZONTAL,
    	VERTICAL
    }
}
