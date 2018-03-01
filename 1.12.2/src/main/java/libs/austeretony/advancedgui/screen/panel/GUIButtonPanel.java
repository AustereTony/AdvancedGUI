package libs.austeretony.advancedgui.screen.panel;

import java.util.ArrayList;
import java.util.List;

import libs.austeretony.advancedgui.screen.button.GUIButton;
import libs.austeretony.advancedgui.screen.core.GUIElement;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Панель для кнопок GUIButton.
 */
@SideOnly(Side.CLIENT)
public class GUIButtonPanel extends GUIElement {
            
    private final GUIOrientation orientation;
    
    private int buttonsOffset;

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
    	
    	super(scaleFactor);
    	   	
    	this.orientation = orientation;
        this.setPosition(xPosition, yPosition);
        this.setSize(buttonWidth, buttonHeight);
                
        this.setEnabled(true);
        this.setVisible(true);
    }
    
    /**
     * Метод для добавления кнопки на панель.
     * 
     * @param button добавляемая кнопка.
     */
    public void addButton(GUIButton button) {
    	
    	if (!this.buttons.contains(button)) {
    		
    		int size = this.buttons.size();
    		
    		button.setPosition(this.orientation == GUIOrientation.HORIZONTAL ? this.getX() + (int) ((this.getWidth() + this.getButtonsOffset()) * this.getScale() * size) : this.getX(), this.orientation == GUIOrientation.VERTICAL ? this.getY() + (int) ((this.getHeight() + this.getButtonsOffset()) * this.getScale() * size) : this.getY());  		
    		button.setSize(this.getWidth(), this.getHeight());   		
    		button.setScale(this.getScale());
    		    		
    		this.buttons.add(button);
    	}
    }
    
    public void draw() {
    	
        if (this.isVisible()) {      
        	
            GlStateManager.disableRescaleNormal();        
        	
            GlStateManager.pushMatrix();
            
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);
            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
                        
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            
        	if (this.isTextureEnabled()) {
        		
        		this.mc.getTextureManager().bindTexture(this.getTexture());          
                
                this.drawCustomSizedTexturedRect(this.ZERO + (this.getWidth() - this.getTextureWidth()) / 2, this.ZERO + (this.getHeight() - this.getTextureHeight()) / 2, this.getTextureU(), this.getTextureV(), this.getTextureWidth(), this.getTextureHeight(), this.getTextureWidth(), this.getTextureHeight());
        	}
        	                           	
        	if (this.isBackgroundEnabled()) {        		        		        	
                        		
                this.drawRect(this.ZERO, this.ZERO, this.ZERO + (this.orientation == GUIOrientation.HORIZONTAL ? (int) ((this.getWidth() + this.getButtonsOffset()) * this.getScale() * this.buttons.size()) : (int) (this.getWidth() * this.getScale())), this.ZERO + (this.orientation == GUIOrientation.VERTICAL ? (int) ((this.getHeight() + this.getButtonsOffset()) * this.getScale() * this.buttons.size()) : (int) (this.getHeight() * this.getScale())), this.getEnabledBackgroundColor());      	
        	}   
        	
            GlStateManager.popMatrix();

            GlStateManager.enableRescaleNormal();
        	
            for (GUIButton button : this.buttons) {
                	
            	button.draw();          	
            }
        }
    }
    
    public void drawPopup(int mouseX, int mouseY) {
    	
        if (this.isVisible()) {        
        	
            for (GUIButton button : this.buttons) {
                	
            	button.drawPopup(mouseX, mouseY);          	
            }
        }
    }
    
    public void mouseOver(int mouseX, int mouseY) {
    	
    	if (this.isEnabled()) {
    	
    		for (GUIButton button : this.buttons) {

    			button.mouseOver(mouseX, mouseY);
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
    
    public int getButtonsOffset() {
    	
    	return this.buttonsOffset;
    } 
    
    public void setButtonsOffset(int offset) {
    	
    	this.buttonsOffset = offset;
    }
    
    /**
     * Enum для определения ориентации панели.
     */
    public enum GUIOrientation {
    	
    	HORIZONTAL,
    	VERTICAL
    }
}
