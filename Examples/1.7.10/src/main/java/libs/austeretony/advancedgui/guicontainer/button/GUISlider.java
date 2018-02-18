package libs.austeretony.advancedgui.guicontainer.button;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import libs.austeretony.advancedgui.guicontainer.browsing.GUIScroller;
import libs.austeretony.advancedgui.guicontainer.utils.GUIUtils;
import net.minecraft.client.Minecraft;

/**
 * Слайдер для скроллера.
 */
@SideOnly(Side.CLIENT)
public class GUISlider {

	private Minecraft mc;
	
    public final int xPosition, yPosition, sliderWidth, sliderHeight;
    
    private int xSlidebarPos, ySlidebarPos, slidebarWidth, slidebarHeight;
    
    private boolean isEnabled, isVisible, isHovered, isDragged, isBackgroundEnabled;
    
    private int 
    backgroundColor = 0xFF555555,
    slidebarColor = 0xFFA5A5A5,
    disabledSlidebarColor = 0xF666666,
    hoveredSlidebarColor = 0xFFBFBFBF;
    
    private float scaleFactor;
    
    private GUIScroller scroller;
    
    /**
     * Слайдер для скроллера.
     * 
     * @param scroller скроллер, для которого создаётся слайдер.
     * @param xPosition позиция слайдера по x.
     * @param yPosition позиция слайдера по y.
     * @param sliderWidth ширина слайдера.
     * @param sliderHeight высота слайдера.
     * @param scaleFactor коэффициент скалирования.
     */
    public GUISlider(GUIScroller scroller, int xPosition, int yPosition, int sliderWidth, int sliderHeight, float scaleFactor) {
    	
    	this.scroller = scroller;
    	this.xPosition = xPosition;
    	this.yPosition = yPosition;
    	this.sliderWidth = sliderWidth;
    	this.sliderHeight = sliderHeight;
    	this.scaleFactor = scaleFactor;
    	
    	this.slidebarWidth = sliderWidth;
    	
    	this.slidebarHeight = (int) ((float) sliderHeight / ((float) scroller.rowsAmount / (float) scroller.rowsVisible)) > 10 ? (int) ((float) sliderHeight / ((float) scroller.rowsAmount / (float) scroller.rowsVisible)) : 10;
    	this.xSlidebarPos = xPosition;
    	this.ySlidebarPos = yPosition;
    	
        this.mc = Minecraft.getMinecraft();
        
        this.isEnabled = true;
        this.isVisible = true;       
        this.isBackgroundEnabled = true;
    }
    
    public void draw() {
    	
        if (this.isVisible()) {
        	
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        	        	
        	GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        	
            GL11.glPushMatrix();

            GL11.glTranslatef(this.xPosition, this.yPosition, 0.0F);
            
            GL11.glScalef(this.scaleFactor, this.scaleFactor, 0.0F);
            
            final int zero = 0;   
            
        	if (this.isBackgroundEnabled()) {
        		
        		GUIUtils.drawRect(zero, zero, zero + this.sliderWidth, zero + this.sliderHeight, this.backgroundColor);
        	}           
        	
            GL11.glPopMatrix();
            
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        	
            GL11.glPushMatrix();

            GL11.glTranslatef(this.xSlidebarPos, this.ySlidebarPos, 0.0F);
            
            GL11.glScalef(this.scaleFactor, this.scaleFactor, 0.0F);
        	
    		int color;
    		
            if (!this.isEnabled()) {
            	
            	color = this.disabledSlidebarColor;
            }
            
            else if (this.isHovered()) {
            	
            	color = this.hoveredSlidebarColor;
            }
            
            else {
            	
            	color = this.slidebarColor;
            }     		
            
            GUIUtils.drawRect(zero, zero, zero + this.slidebarWidth, zero + this.slidebarHeight, color);
        	
            GL11.glPopMatrix();
        	
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);         
        }
    }       
    
    public void mouseOver(int mouseX, int mouseY, int guiLeft, int guiTop) {
    	
    	if (!this.isDragged()) {
    		
    		mouseX -= guiLeft;
    		mouseY -= guiTop;
    		
    		this.isHovered = this.isEnabled() && mouseX >= this.xSlidebarPos && mouseY >= this.ySlidebarPos && mouseX < this.xSlidebarPos + (int) (this.slidebarWidth * this.scaleFactor) && mouseY < this.ySlidebarPos + (int) (this.slidebarHeight * this.scaleFactor);   		    
    	}
    }
    
    public void mouseClicked() {
    	    	
    	if (Mouse.isButtonDown(0) && this.isHovered()) {
    		
    		this.isDragged = true;
    	}
    }
    
    public void setSlidebarNotDragged() {
    	
		this.isDragged = false;
		this.isHovered = false;
    }
    
    public boolean isSlidebarDragged() {
    	    	
        return this.isDragged();
    }
    
	public void handleSlidebarPosition() {
				
		this.ySlidebarPos = (int) (this.yPosition + (((float) (this.sliderHeight - this.slidebarHeight)) / (float) this.scroller.getMaxPosition()) * (float) this.scroller.getPosition());
	}
	
	public void reset() {
		
        this.scroller.resetPosition();
        
		this.handleSlidebarPosition();
	}
	
	public int getXSlidebarPosition() {
		
		return this.xSlidebarPos;
	}
	
	public int getYSlidebarPosition() {
		
		return this.ySlidebarPos;
	}
	
	public void setYSlidebarPosition(int ySlidebarPos) {
		
		this.ySlidebarPos = ySlidebarPos >= this.yPosition ? (ySlidebarPos <= this.yPosition + (this.sliderHeight - this.slidebarHeight) ? ySlidebarPos : this.yPosition + (this.sliderHeight - this.slidebarHeight)) : this.yPosition;
	}
	
	public int getSlidebarWidth() {
		
		return this.slidebarWidth;
	}
	
	public int getSlidebarHeight() {
		
		return this.slidebarHeight;
	}
    
    public boolean isHovered() {
    	
        return this.isHovered;
    }
    
    public boolean isDragged() {
    	
        return this.isDragged;
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
    
    public void setSlidebarColor(int colorHex) {
    	
    	this.slidebarColor = colorHex;
    }
    
    public void setDisabledSlidebarColor(int colorHex) {
    	
    	this.disabledSlidebarColor = colorHex;
    }
    
    public void setHoveredSlidebarColor(int colorHex) {
    	
    	this.hoveredSlidebarColor = colorHex;
    }
}