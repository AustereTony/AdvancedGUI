package libs.austeretony.advancedgui.screen.button;

import org.lwjgl.input.Mouse;

import libs.austeretony.advancedgui.screen.browsing.GUIScroller;
import libs.austeretony.advancedgui.screen.core.GUIAdvancedElement;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Слайдер для скроллера.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUISlider extends GUIAdvancedElement<GUISlider> {
	
    private GUIScroller scroller;
    
	private ResourceLocation slidebarTexture;
	
	private boolean isSlidebarBackgroundEnabled, isSlidebarTextureEnabled;
    	    
    private int slidebarX, slidebarY, slidebarWidth, slidebarHeight, slidebarTextureU, slidebarTextureV;     
        
    /**
     * Слайдер для скроллера.
     * @param xPosition позиция слайдера по x
     * @param yPosition позиция слайдера по y
     * @param sliderWidth ширина слайдера
     * @param sliderHeight высота слайдера
     */
    public GUISlider(int xPosition, int yPosition, int sliderWidth, int sliderHeight) {
    	
    	this.setPosition(xPosition, yPosition);
    	this.setSize(sliderWidth, sliderHeight);
    	this.setSlidebarPosition(xPosition, yPosition);
    	
        this.setEnabled(true);
        this.setVisible(true);
        this.enableSlidebarBackground();
    }
    
    public void setScroller(GUIScroller scroller) {
    	
    	this.scroller = scroller;
    	
    	this.setSlidebarSize(this.getWidth(), (int) ((float) this.getHeight() / ((float) scroller.rowsAmount / (float) scroller.rowsVisible)) > 10 ? (int) ((float) this.getHeight() / ((float) scroller.rowsAmount / (float) scroller.rowsVisible)) : 10);
    }
    
    @Override
    public void draw() {
    	
        if (this.isVisible()) {
        	
            GlStateManager.disableRescaleNormal();        
        	
            GlStateManager.pushMatrix();
            
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);
            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);                           
        	
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        	
        	if (this.isTextureEnabled()) {
        		
        		this.mc.getTextureManager().bindTexture(this.getTexture());          
                
                this.drawCustomSizedTexturedRect(this.ZERO + (this.getWidth() - this.getTextureWidth()) / 2, this.ZERO + (this.getHeight() - this.getTextureHeight()) / 2, this.getTextureU(), this.getTextureV(), this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
        	}  
        	
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            
        	if (this.isBackgroundEnabled()) {
        		
        		this.drawRect(this.ZERO, this.ZERO, this.ZERO + this.getWidth(), this.ZERO + this.getHeight(), this.getEnabledBackgroundColor());
        	}   
        	
            GlStateManager.popMatrix();  
        	
            GlStateManager.pushMatrix();
            
            GlStateManager.translate(this.getSlidebarX(), this.getSlidebarY(), 0.0F);
            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);          
            
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        	
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
        	
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        	
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
        	
            GlStateManager.popMatrix();

            GlStateManager.enableRescaleNormal();           
        }
    }       
    
    @Override
    public void mouseOver(int mouseX, int mouseY) {
    	
    	if (!this.isDragged()) {
    		
    		this.setHovered(this.isEnabled() && mouseX >= this.getSlidebarX() && mouseY >= this.getSlidebarY() && mouseX < this.getSlidebarX() + (int) (this.getSlidebarWidth() * this.getScale()) && mouseY < this.getSlidebarY() + (int) (this.getSlidebarHeight() * this.getScale()));   		    
    	}
    }
    
    @Override
    public boolean mouseClicked(int mouseX, int mouseY) {
    	    	
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
					
		this.setSlidebarY(this.getY() + (int) ((((float) (this.getHeight() - this.getSlidebarHeight())) / (float) this.scroller.getMaxPosition()) * (float) this.scroller.getPosition()));
	}
	
	public void handleSlidebarViaCursor(int slidebarY) {
				
		this.setSlidebarY(slidebarY >= this.getY() ? (slidebarY <= this.getY() + (this.getHeight() - this.getSlidebarHeight()) ? slidebarY : this.getY() + (this.getHeight() - this.getSlidebarHeight())) : this.getY());
	}
	
	public boolean isSlidebarBackgroundEnabled() {
		
		return this.isSlidebarBackgroundEnabled;
	}
	
	public GUISlider enableSlidebarBackground() {
		
		this.isSlidebarBackgroundEnabled = true;
		
		return this;
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
	
	public GUISlider enableSlidebarTexture() {
		
		this.isSlidebarTextureEnabled = true;
		
		return this;
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
    
    public GUISlider setSlidebarTexture(ResourceLocation texture) {
    	
    	this.slidebarTexture = texture;
    	
    	return this;
    }
    
    public GUISlider setSlidebarTexture(ResourceLocation texture, int u, int v) {
    	
    	this.slidebarTexture = texture;
    	
    	this.slidebarTextureU = u;
    	this.slidebarTextureV = v;
    	
    	return this;	
    }
}