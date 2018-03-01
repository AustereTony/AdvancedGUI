package libs.austeretony.advancedgui.container.button;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import libs.austeretony.advancedgui.container.browsing.GUIScroller;
import libs.austeretony.advancedgui.screen.core.GUIElement;
import net.minecraft.util.ResourceLocation;

/**
 * Слайдер для скроллера.
 */
@SideOnly(Side.CLIENT)
public class GUISlider extends GUIElement {
	
    public final GUIScroller scroller;
    
	private ResourceLocation slidebarTexture;
	
	private boolean isSlidebarBackgroundEnabled, isSlidebarTextureEnabled;
    	    
    private int slidebarX, slidebarY, slidebarWidth, slidebarHeight, slidebarTextureU, slidebarTextureV;     
        
    /**
     * Слайдер для скроллера.
     * 
     * @param scroller скроллер, для которого создаётся слайдер
     * @param xPosition позиция слайдера по x
     * @param yPosition позиция слайдера по y
     * @param sliderWidth ширина слайдера
     * @param sliderHeight высота слайдера
     * @param scaleFactor коэффициент скалирования
     */
    public GUISlider(GUIScroller scroller, int xPosition, int yPosition, int sliderWidth, int sliderHeight, float scaleFactor) {
    	
    	super(scaleFactor);
    	
    	this.scroller = scroller;
    	this.setPosition(xPosition, yPosition);
    	this.setSize(sliderWidth, sliderHeight);
    	this.setSlidebarPosition(xPosition, yPosition);
    	this.setSlidebarSize(sliderWidth, (int) ((float) sliderHeight / ((float) scroller.rowsAmount / (float) scroller.rowsVisible)) > 10 ? (int) ((float) sliderHeight / ((float) scroller.rowsAmount / (float) scroller.rowsVisible)) : 10);
        
        this.setEnabled(true);
        this.setVisible(true);
        this.setBackgroundEnabled(true);
        this.setSlidebarBackgroundEnabled(true);
    }
    
    public void draw() {
    	
        if (this.isVisible()) {
        	
        	GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        	
            GL11.glPushMatrix();
            
            GL11.glTranslatef(this.getX(), this.getY(), 0.0F);
            
            GL11.glScalef(this.getScale(), this.getScale(), 0.0F);                           
        	
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        	
        	if (this.isTextureEnabled()) {
        		
        		this.mc.getTextureManager().bindTexture(this.getTexture());          
                
                this.drawCustomSizedTexturedRect(this.ZERO + (this.getWidth() - this.getTextureWidth()) / 2, this.ZERO + (this.getHeight() - this.getTextureHeight()) / 2, this.getTextureU(), this.getTextureV(), this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
        	}  
        	
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            
        	if (this.isBackgroundEnabled()) {
        		
        		this.drawRect(this.ZERO, this.ZERO, this.ZERO + this.getWidth(), this.ZERO + this.getHeight(), this.getEnabledBackgroundColor());
        	}   
        	
            GL11.glPopMatrix();
        	
            GL11.glPushMatrix();
            
            GL11.glTranslatef(this.getSlidebarX(), this.getSlidebarY(), 0.0F);
            
            GL11.glScalef(this.getScale(), this.getScale(), 0.0F);          
            
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        	
        	if (this.isSlidebarTextureEnabled()) {
        		
        		int 
        		u = this.getSlidebarTextureU();
        		
        		this.mc.getTextureManager().bindTexture(this.getSlidebarTexture());          
                
                if (this.isHovered() || this.isToggled()) {
                	
                	u += this.getSlidebarWidth() * 2;
                }
                
                else {
                	
                	u += this.getSlidebarWidth();
                }
                
                this.drawCustomSizedTexturedRect(this.ZERO, this.ZERO, u, this.getSlidebarTextureV(), this.getSlidebarWidth(), this.getSlidebarHeight(), this.getSlidebarWidth() * 3, this.getSlidebarHeight());
        	}
        	
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        	
            if (this.isSlidebarBackgroundEnabled()) {
            	
            	int color;
    		
            	if (!this.isEnabled()) {
            	
            		color = this.getDisabledColor();
            	}
            
            	else if (this.isHovered()) {
            	
            		color = this.getHoveredColor();
            	}
            
            	else {
            	
            		color = this.getEnabledColor();
            	}     		
            
            	this.drawRect(this.ZERO, this.ZERO, this.ZERO + this.getSlidebarWidth(), this.ZERO + this.getSlidebarHeight(), color);
            }
        	
            GL11.glPopMatrix();

            GL11.glEnable(GL12.GL_RESCALE_NORMAL);         
        }
    }       
    
    public void mouseOver(int mouseX, int mouseY) {
    	
    	if (!this.isDragged()) {
    		
    		this.setHovered(this.isEnabled() && mouseX >= this.getSlidebarX() && mouseY >= this.getSlidebarY() && mouseX < this.getSlidebarX() + (int) (this.getSlidebarWidth() * this.getScale()) && mouseY < this.getSlidebarY() + (int) (this.getSlidebarHeight() * this.getScale()));   		    
    	}
    }
    
    public boolean mouseClicked() {
    	    	
    	if (Mouse.isButtonDown(0) && this.isHovered()) {
    		
    		this.setDragged(true);
    		
    		return true;
    	}
    	
    	return false;
    }
    
	public void reset() {
		
        this.scroller.resetPosition();
        
		this.handleSlidebarViaScroller();
	}
    
    public void setSlidebarNotDragged() {
    	
		this.setDragged(false);
		this.setHovered(false);
    }
    
	public void handleSlidebarViaScroller() {
				
		this.setSlidebarY((int) (this.getY() + (((float) (this.getHeight() - this.slidebarHeight)) / (float) this.scroller.getMaxPosition()) * (float) this.scroller.getPosition()));
	}
	
	public void handleSlidebarViaCursor(int slidebarY) {
		
		this.setSlidebarY(slidebarY >= this.getY() ? (slidebarY <= this.getY() + (this.getHeight() - this.slidebarHeight) ? slidebarY : this.getY() + (this.getHeight() - this.slidebarHeight)) : this.getY());
	}
	
	public boolean isSlidebarBackgroundEnabled() {
		
		return this.isSlidebarBackgroundEnabled;
	}
	
	public void setSlidebarBackgroundEnabled(boolean isSlidebarBackgroundEnabled) {
		
		this.isSlidebarBackgroundEnabled = isSlidebarBackgroundEnabled;
	}
	
	public int getSlidebarX() {
		
		return this.slidebarX;
	}
	
	public void setSlidebarX(int xPosition) {
		
		this.slidebarX = xPosition;
	}
	
	public int getSlidebarY() {
		
		return this.slidebarY;
	}
	
	public void setSlidebarY(int yPosition) {
		
		this.slidebarY = yPosition;
	}
	
	public void setSlidebarPosition(int xPosition, int yPosition) {
		
		this.slidebarX = xPosition;
		this.slidebarY = yPosition;
	}
	
	public int getSlidebarWidth() {
		
		return this.slidebarWidth;
	}
	
	public void setSlidebarWidth(int slidebarWidth) {
		
		this.slidebarWidth = slidebarWidth;
	}
	
	public int getSlidebarHeight() {
		
		return this.slidebarHeight;
	}
	
	public void setSlidebarHeight(int slidebarHeight) {
		
		this.slidebarHeight = slidebarHeight;
	}
	
	public void setSlidebarSize(int slidebarWidth, int slidebarHeight) {
		
		this.slidebarWidth = slidebarWidth;
		this.slidebarHeight = slidebarHeight;
	}
	
	public boolean isSlidebarTextureEnabled() {
		
		return this.isSlidebarTextureEnabled;
	}
	
	public void setSlidebarTextureEnabled(boolean isSlidebarTextureEnabled) {
		
		this.isSlidebarTextureEnabled = isSlidebarTextureEnabled;
	}
	
    public void setSliderTexture(ResourceLocation texture, int textureWidth, int textureHeight) {
    	
    	this.setTexture(texture, textureWidth, textureHeight);
    }
	
    public void setSliderTexture(ResourceLocation texture, int textureWidth, int textureHeight, int u, int v) {
    	
    	this.setTexture(texture, textureWidth, textureHeight, u, v);
    }
    
	public ResourceLocation getSlidebarTexture() {
		
		return this.slidebarTexture;
	}
    
    public int getSlidebarTextureU() {
    	
    	return this.slidebarTextureU;
    }
    
    public int getSlidebarTextureV() {
    	
    	return this.slidebarTextureV;
    }
    
    public void setSlidebarTexture(ResourceLocation texture) {
    	
    	this.slidebarTexture = texture;
    }
    
    public void setSlidebarTexture(ResourceLocation texture, int u, int v) {
    	
    	this.slidebarTexture = texture;
    	
    	this.slidebarTextureU = u;
    	this.slidebarTextureV = v;
    }
}