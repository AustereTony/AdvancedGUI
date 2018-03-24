package libs.austeretony.advancedgui.screen.core;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Класс-основа простых (без текстур) графических элементов ГПИ.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUISimpleElement<T extends GUIBaseElement> extends GUIBaseElement<T> {
			
	public final static int 
	ZERO = 0,
	FONT_HEIGHT = 9;
    	
    private int xPopupOffset, yPopupOffset;
                
    private boolean isDebugMode, isVisible, isBackgroundEnabled, isPopupEnabled, isCustomPopup, isPopupBackgroundEabled;
    
    private String 
    displayText = "", 
    popupText = "";
    
    private int 
    debugColor = 0x64FF00DC,
    enabledColor = 0xFFA5A5A5,
    disabledColor = 0xFF666666,
    hoveredColor = 0xFFBFBFBF,
    enabledBackgroundColor = 0xFF555555,
    disabledBackgroundColor = 0xFF404040,
    hoveredBackgroundColor = 0xFF505050,
    popupBackgroundColor = 0xFF404040,
    displayTextColor = 0xFFD1D1D1,
    enabledTextColor = 0xFFD1D1D1, 
    disabledTextColor = 0xFF707070,
    hoveredTextColor = 0xFFF2F2F2,
    popupTextColor = 0xFFD1D1D1;
    
    private float scaleFactor = 1.0F;
    
    public void draw() {}
    
    public void drawPopup(int mouseX, int mouseY) {
    	
    	if (this.isVisible()) {   	    		
    		
    		if (this.isPopupEnabled()) {
    			    		
    			if (this.isHovered()) {
        		
                    GlStateManager.disableRescaleNormal();        
                	
                    GlStateManager.pushMatrix();
                    
                    GlStateManager.translate(mouseX, mouseY, 0.0F);
                    
                    GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
                                
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                                        
    				String[] popupStrings = this.getPopupText().split("/n");
    				
    				int i, 
    				frameWidth = this.mc.fontRenderer.getStringWidth(popupStrings[0]), 
    				frameHeight = this.FONT_HEIGHT * popupStrings.length, 
    				xStart = this.ZERO + (int) ((this.getXPopupOffset() != 0 ? this.getXPopupOffset() : this.getWidth()) * this.getScale()),
    				yStart = this.ZERO + (int) ((this.getYPopupOffset() != 0 ? this.getYPopupOffset() : 0) * this.getScale()) - popupStrings.length * 10;
    				
    				if (this.isCustomPopup()) {
    					
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    					
    					if (this.isPopupBackgroundEnabled()) {
    						
    		            	this.drawRect(xStart - 1, yStart - 1, xStart + frameWidth + 1, yStart + frameHeight + 1, this.getPopupBackgroundColor());
    					}
    				}
    			    				
    				else {
                    	
                    	int 
                    	color1 = - 267386864,
                    	color2 = 1347420415,
                    	color3 = (color2 & 16711422) >> 1 | color2 & - 16777216;
                    	                                                
                        this.drawGradientRect(xStart - 3, yStart - 4, xStart + frameWidth + 3, yStart - 3, color1, color1, 300.0);
                        this.drawGradientRect(xStart - 3, yStart + frameHeight + 3, xStart + frameWidth + 3, yStart + frameHeight + 4, color1, color1, 300.0);
                        this.drawGradientRect(xStart - 3, yStart - 3, xStart + frameWidth + 3, yStart + frameHeight + 3, color1, color1, 300.0);
                        this.drawGradientRect(xStart - 4, yStart - 3, xStart - 3, yStart + frameHeight + 3, color1, color1, 300.0);
                        this.drawGradientRect(xStart + frameWidth + 3, yStart - 3, xStart + frameWidth + 4, yStart + frameHeight + 3, color1, color1, 300.0);

                        this.drawGradientRect(xStart - 3, yStart - 3 + 1, xStart - 3 + 1, yStart + frameHeight + 3 - 1, color2, color3, 300.0);
                        this.drawGradientRect(xStart + frameWidth + 2, yStart - 3 + 1, xStart + frameWidth + 3, yStart + frameHeight + 3 - 1, color2, color3, 300.0);
                        this.drawGradientRect(xStart - 3, yStart - 3, xStart + frameWidth + 3, yStart - 3 + 1, color2, color2, 300.0);
                        this.drawGradientRect(xStart - 3, yStart + frameHeight + 2, xStart + frameWidth + 3, yStart + frameHeight + 3, color3, color3, 300.0);
                    }
    			
    				for (i = 0; i < popupStrings.length; i++) {    				
    				
    					this.mc.fontRenderer.drawStringWithShadow(popupStrings[i], xStart, yStart + 10 * i, this.getPopupTextColor());
    				}
    				
    	            GlStateManager.popMatrix();

    	            GlStateManager.enableRescaleNormal();
    			}   			
    		}
    	}
    }

	@Override
    public void mouseOver(int mouseX, int mouseY) {  	       	
    	
    	this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + (int) (this.getWidth() * this.getScale()) && mouseY < this.getY() + (int) (this.getHeight() * this.getScale()));   
    }
    
    public boolean isDebugMode() {
    	
    	return this.isDebugMode;
    }
    
	/**
	 * Заливает рабочую область установленным цветом для отображения границ.
	 * 
     * @return вызывающий объект
	 */
    public T enableDebugMode() {
    	
    	this.isDebugMode = true;
    	
    	return (T) this;
    } 
    
    public boolean isVisible() {
    	
		return this.isVisible;
	}
    
    /**
     * Определяет, будет ли отображаться элемент.
     * 
     * @param isVisible
     * 
     * @return вызывающий объект
     */
    public T setVisible(boolean isVisible) {
    	
    	this.isVisible = isVisible;
    	
    	return (T) this;
    }
    
    public boolean isBackgroundEnabled() {
    	
    	return this.isBackgroundEnabled;
    }
    
    /**
     * Использовать заливку для фона элемента. Цвет заливки можно указать {@link GUISimpleElement#setEnabledBackgroundColor(int)}.
     * 
     * @return вызывающий объект
     */
    public T enableBackground() {
    	
    	this.isBackgroundEnabled = true;
    	
    	return (T) this;
    }

    public boolean isPopupEnabled() {
    	
    	return this.isPopupEnabled;
    }
    
    /**
     * Разрешение на использование всплывающего сообщения.
     * 
     * @param isPopupEnabled
     * 
     * @return вызывающий объект
     */
    public T setPopupEnabled(boolean isPopupEnabled) {
    	
    	this.isPopupEnabled = isPopupEnabled;
    	
    	return (T) this;
    }
    
    public boolean isCustomPopup() {
    	
    	return this.isCustomPopup;
    }
    
    /**
     * Разрешает использование собственной заливки фона всплывающего текста.
     * 
     * @return вызывающий объект
     */
    public T enableCustomPopup() {
    	
    	this.isCustomPopup = true;
    	
    	return (T) this;
    }
    
    public String getDisplayText() {
    	
    	return this.displayText;
    }
    
    /**
     * Установка текста, отображаемого элементом. 
     * 
     * @param displayText
     * 
     * @return вызывающий объект
     */
    public T setDisplayText(String displayText) {
    	
    	this.displayText = displayText;
    	
    	return (T) this;
    }
    
    public String getPopupText() {
    	
    	return this.popupText;
    }
    
    /**
     * Добавляет всплывающий текст при наведении курсора.
     * 
     * @return вызывающий объект
     */
    public T setPopupText(String popupText) {
    	
    	this.popupText = popupText;
    	
    	this.isPopupEnabled = true;
    	
    	return (T) this;
    }
    
    public int getDebugColor() {
    	
    	return this.debugColor;
    }
    
    /**
     * Установка цвета debug заливки.
     * 
     * @param colorHex
     * 
     * @return вызывающий объект
     */
    public T setDebugColor(int colorHex) {
    	
    	this.debugColor = colorHex;
    	
    	return (T) this;
    }
    
    public int getEnabledColor() {
    	
    	return this.enabledColor;
    }
    
    public T setEnabledColor(int colorHex) {
    	
    	this.enabledColor = colorHex;
    	
    	return (T) this;
    }
    
    public int getDisabledColor() {
    	
    	return this.disabledColor;
    }
    
    public T setDisabledColor(int colorHex) {
    	
    	this.disabledColor = colorHex;
    	
    	return (T) this;
    }
    
    public int getHoveredColor() {
    	
    	return this.hoveredColor;
    }
    
    public T setHoveredColor(int colorHex) {
    	
    	this.hoveredColor = colorHex;
    	
    	return (T) this;
    }
    
    public int getEnabledBackgroundColor() {
    	
    	return this.enabledBackgroundColor;
    }
    
    public T setEnabledBackgroundColor(int colorHex) {
    	
    	this.enabledBackgroundColor = colorHex;
    	
    	return (T) this;
    }
    
    public int getDisabledBackgroundColor() {
    	
    	return this.disabledBackgroundColor;
    }
    
    public T setDisabledBackgroundColor(int colorHex) {
    	
    	this.disabledBackgroundColor = colorHex;
    	
    	return (T) this;
    }
    
    public int getHoveredBackgroundColor() {
    	
    	return this.hoveredBackgroundColor;
    }
    
    public T setHoveredBackgroundColor(int colorHex) {
    	
    	this.hoveredBackgroundColor = colorHex;
    	
    	return (T) this;
    }
    
    public int getPopupBackgroundColor() {
    	
    	return this.popupBackgroundColor;
    }
    
    public T setPopupBackgroundColor(int colorHex) {
    	
    	this.popupBackgroundColor = colorHex;
    	
    	return (T) this;
    }
    
    public int getDisplayTextColor() {
    	
    	return this.displayTextColor;
    }
    
    public T setDisplayTextColor(int colorHex) {
    	
    	this.displayTextColor = colorHex;
    	
    	return (T) this;
    }
    
    public int getEnabledTextColor() {
    	
    	return this.enabledTextColor;
    }
    
    public T setEnabledTextColor(int colorHex) {
    	
    	this.enabledTextColor = colorHex;
    	
    	return (T) this;
    }
    
    public int getDisabledTextColor() {
    	
    	return this.disabledTextColor;
    }
    
    public T setDisabledTextColor(int colorHex) {
    	
    	this.disabledTextColor = colorHex;
    	
    	return (T) this;
    }
    
    public int getHoveredTextColor() {
    	
    	return this.hoveredTextColor;
    }
    
    public T setHoveredTextColor(int colorHex) {
    	
    	this.hoveredTextColor = colorHex;
    	
    	return (T) this;
    }
    
    public int getPopupTextColor() {
    	
    	return this.popupTextColor;
    }
    
    public T setPopupTextColor(int colorHex) {
    	
    	this.popupTextColor = colorHex;
    	
    	return (T) this;
    }
    
    public boolean isPopupBackgroundEnabled() {
    	
    	return this.isPopupBackgroundEabled;
    }
    
    public T enablePopupBackground() {
    	
    	this.isPopupBackgroundEabled = true;
    	
    	return (T) this;
    }
	
    public int getXPopupOffset() {
    	
    	return this.xPopupOffset;
    }
    
    public int getYPopupOffset() {
    	
    	return this.yPopupOffset;
    }
    
    /**
     * Установка отступа всплывающего текста.
     * 
     * @param xOffset отступ по горизонтали
     * @param yOffset отступ по вертикали
     * 
     * @return вызывающий объект
     */
    public T setPopupAlignment(int xOffset, int yOffset) {
    	
    	this.xPopupOffset = xOffset;
    	this.yPopupOffset = yOffset;
    	
    	return (T) this;
    }

    public float getScale() {
    	
    	return this.scaleFactor;
    }
    
    /**
     * Позволяет быстро задать относительный размер, 1.0F по умолчанию.
     * 
     * @param scaleFactor
     * 
     * @return вызывающий объект
     */
    public T setScale(float scaleFactor) {
    	
    	this.scaleFactor = scaleFactor;
    	
    	return (T) this;
    }
    
    /**
     * Возвращает длину переданной строки.
     * 
     * @param text
     * 
     * @return длина строки в пикселях
     */
    public int width(String text) {
    	
    	return this.mc.fontRenderer.getStringWidth(text);
    }
    
    public static void drawRect(int left, int top, int right, int bottom, int color) {
    	
        if (left < right) {
        	
            int i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
        	
            int j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        
        Tessellator tessellator = Tessellator.getInstance();
        
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        
        bufferbuilder.pos((double) left, (double) bottom, 0.0D).endVertex();
        bufferbuilder.pos((double) right, (double) bottom, 0.0D).endVertex();
        bufferbuilder.pos((double) right, (double) top, 0.0D).endVertex();
        bufferbuilder.pos((double) left, (double) top, 0.0D).endVertex();
        
        tessellator.draw();
        
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor, double zLevel) {
    	
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        
        Tessellator tessellator = Tessellator.getInstance();
        
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        
        bufferbuilder.pos((double) right, (double) top, zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) left, (double) top, zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) left, (double) bottom, zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos((double) right, (double) bottom, zLevel).color(f5, f6, f7, f4).endVertex();
        
        tessellator.draw(); 
        
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}

