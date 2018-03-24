package libs.austeretony.advancedgui.screen.panel;

import java.util.ArrayList;
import java.util.List;

import libs.austeretony.advancedgui.screen.browsing.GUIScroller;
import libs.austeretony.advancedgui.screen.button.GUIButton;
import libs.austeretony.advancedgui.screen.core.GUIAdvancedElement;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Панель для кнопок GUIButton.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUIButtonPanel extends GUIAdvancedElement<GUIButtonPanel> {
            
    public final GUIEnumOrientation orientation;
    
    private int buttonsOffset, buttonWidth, buttonHeight, visibleButtonsAmount, maxButtonsAmount;
    
    private boolean hasScroller;

    public final List<GUIButton> visibleButtons = new ArrayList<GUIButton>();
    
    public final List<GUIButton> buttonsBuffer = new ArrayList<GUIButton>();
    
    private GUIScroller scroller;
    
    /**
     * Панель для кнопок.
     * 
     * @param orientation ориентация панели
     * @param xPosition позиция по x
     * @param yPosition позиция по y
     * @param buttonWidth ширина кнопки
     * @param buttonHeight высота кнопки
     */
    public GUIButtonPanel(GUIEnumOrientation orientation, int xPosition, int yPosition, int buttonWidth, int buttonHeight) {
    	    	   	
    	this.orientation = orientation;
        this.setPosition(xPosition, yPosition);
    	this.buttonWidth = buttonWidth;
    	this.buttonHeight = buttonHeight;
                
        this.setEnabled(true);
        this.setVisible(true);
    }
    
    /**
     * Метод для добавления кнопки на панель.
     * 
     * @param button добавляемая кнопка
     */
    public GUIButtonPanel addButton(GUIButton button) {
    	
		int size;
    	
    	if (!this.visibleButtons.contains(button)) {
    		
    		size = this.visibleButtons.size();
    		
    		if (size < this.visibleButtonsAmount || this.scroller == null) {
    		
    			button.setPosition(this.orientation == GUIEnumOrientation.HORIZONTAL ? this.getX() + (int) ((this.getButtonWidth() + this.getButtonsOffset()) * this.getScale() * size) : this.getX(), 
    					this.orientation == GUIEnumOrientation.VERTICAL ? this.getY() + (int) ((this.getButtonHeight() + this.getButtonsOffset()) * this.getScale() * size) : this.getY());  		
    			button.setSize(this.getButtonWidth(), this.getButtonHeight());   		
    			button.setScale(this.getScale());   		    			
    		    		
    			this.visibleButtons.add(button);
    			
    	    	this.setSize(this.orientation == GUIEnumOrientation.HORIZONTAL ? (int) ((this.getButtonWidth() + this.getButtonsOffset()) * this.getScale()) * (size + 1) : this.getButtonWidth(), 
    	    			this.orientation == GUIEnumOrientation.VERTICAL ? (int) ((this.getButtonHeight() + this.getButtonsOffset()) * this.getScale()) * (size + 1): this.getButtonHeight());
    		}
    	}
    	
    	if (!this.buttonsBuffer.contains(button)) {
    		
    		size = this.buttonsBuffer.size();
    		
			button.setPosition(this.orientation == GUIEnumOrientation.HORIZONTAL ? this.getX() + (int) ((this.getButtonWidth() + this.getButtonsOffset()) * this.getScale() * size) : this.getX(), 
					this.orientation == GUIEnumOrientation.VERTICAL ? this.getY() + (int) ((this.getButtonHeight() + this.getButtonsOffset()) * this.getScale() * size) : this.getY());  		
			button.setSize(this.getButtonWidth(), this.getButtonHeight());   		
			button.setScale(this.getScale());
    		
    		this.buttonsBuffer.add(button);
    	}
    	    	
    	return this;
    }
    
    public boolean hasScroller() {
    	
    	return this.hasScroller;
    }
    
	/**
	 * Инициализирует объект GUIScroller, добавляющий скроллинг для панели кнопок.
	 * 
	 * @param scroller
	 */
	public GUIButtonPanel initScroller(GUIScroller scroller) {
		
		this.scroller = this.scroller == null ? scroller : this.scroller;
		
		this.hasScroller = true;
		
		return this;
	}
	
	public GUIScroller getScroller() {
		
		return this.scroller;
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
                
                this.drawCustomSizedTexturedRect(this.ZERO + (this.getWidth() - this.getTextureWidth()) / 2, this.ZERO + (this.getHeight() - this.getTextureHeight()) / 2, this.getTextureU(), this.getTextureV(), this.getTextureWidth(), this.getTextureHeight(), this.getTextureWidth(), this.getTextureHeight());
        	}
        	                           	
        	if (this.isBackgroundEnabled()) {        		        		        	
                        		
                this.drawRect(this.ZERO, this.ZERO, this.ZERO + this.getWidth(), this.ZERO + this.getHeight(), this.getEnabledBackgroundColor());      	
        	}   
        	
            GlStateManager.popMatrix();

            GlStateManager.enableRescaleNormal();
        	
            for (GUIButton button : this.visibleButtons) {
                	
            	button.draw();          	
            }
        }
    }
    
    @Override
    public void drawPopup(int mouseX, int mouseY) {
    	
        if (this.isVisible()) {        
        	
            for (GUIButton button : this.visibleButtons) {
                	
            	button.drawPopup(mouseX, mouseY);          	
            }
        }
    }
    
    @Override
    public void mouseOver(int mouseX, int mouseY) {
    	
    	if (this.isEnabled()) {
    	
    		for (GUIButton button : this.visibleButtons) {

    			button.mouseOver(mouseX, mouseY);
    		}
    	}
    	
    	this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.getWidth() && mouseY < this.getY() + this.getHeight());   
    }
    
    @Override
    public boolean mouseClicked(int mouseX, int mouseY) {
    	
    	if (this.isEnabled()) {

    		for (GUIButton button : this.visibleButtons) {
        	
    			return button.mouseClicked(mouseX, mouseY);
    		}
    	}
        
		return false;
    }
    
	public int getVisibleButtonsAmount() {
		
		return this.visibleButtonsAmount;
	}
	
	public int getMaxButtonsAmount() {
		
		return this.maxButtonsAmount;
	}
	
	/**
	 * Определяет параметры скроллинга для панели.
	 * 
	 * @param visibleButtonsAmount количество кнопок, которые отображаются
	 * @param maxButtonsAmount максимальное количество кнопок
	 */
	public GUIButtonPanel setScrollingParams(int visibleButtonsAmount, int maxButtonsAmount) {

		this.visibleButtonsAmount = visibleButtonsAmount;
		this.maxButtonsAmount = maxButtonsAmount;
		
		return this;
	}
	
	public int getButtonWidth() {
		
		return this.buttonWidth;
	}
	
	public int getButtonHeight() {
		
		return this.buttonHeight;
	}
    
    public int getButtonsOffset() {
    	
    	return this.buttonsOffset;
    } 
    
    /**
     * Устанавливает расстояние между кнопками.
     * 
     * @param offset
     * 
     * @return
     */
    public GUIButtonPanel setButtonsOffset(int offset) {
    	
    	this.buttonsOffset = offset;
    	
    	return this;
    }
    
    /**
     * Enum для определения ориентации панели.
     */
    @SideOnly(Side.CLIENT)
    public enum GUIEnumOrientation {
    	
    	HORIZONTAL,
    	VERTICAL
    }
}
