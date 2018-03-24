package libs.austeretony.advancedgui.screen.core;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

/**
 * Класс-основа сложных (текстурированных) графических элементов ГПИ.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUIAdvancedElement<T extends GUISimpleElement> extends GUISimpleElement<T> {
		
	private ResourceLocation texture, popupTexture;
    	
    private int textureWidth, textureHeight, textureU, textureV, textureOffsetX, textureOffsetY, popupTextureU, popupTextureV;
                
    private boolean isTextureEnabled, isPopupTextureEabled;
    
    public void draw() {}
    
    public void drawPopup(int mouseX, int mouseY) {
    	
    	if (this.isVisible()) {   	    		
    		
    		if (this.getPopupText().length() > 0) {
    			    		
    			if (this.isHovered()) {
        		
    	        	GL11.glDisable(GL12.GL_RESCALE_NORMAL);
    	        	
    	            GL11.glPushMatrix();
    	            
    	            GL11.glTranslatef(mouseX, mouseY, 0.0F);
    	            
    	            GL11.glScalef(this.getScale(), this.getScale(), 0.0F);                           
    	        	
    	            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                                        
    				String[] popupStrings = this.getPopupText().split("/n");
    				
    				int i, 
    				frameWidth = this.mc.fontRenderer.getStringWidth(popupStrings[0]), 
    				frameHeight = this.FONT_HEIGHT * popupStrings.length, 
    				xStart = this.ZERO + (int) ((this.getXPopupOffset() != 0 ? this.getXPopupOffset() : this.getWidth()) * this.getScale()),
    				yStart = this.ZERO + (int) ((this.getYPopupOffset() != 0 ? this.getYPopupOffset() : 0) * this.getScale()) - popupStrings.length * 10;
    				
    				if (this.isCustomPopup()) {
    					
    					if (this.isPopupTextureEnabled()) {    						    						
    		        		
    		        		this.mc.getTextureManager().bindTexture(this.getPopupTexture());          
    		                
    		                this.drawCustomSizedTexturedRect(xStart, yStart, this.getPopupTextureU(), this.getPopupTextureV(), frameWidth, frameHeight, frameWidth, frameHeight);
    					}
    					
        	            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    					
    					if (this.isPopupBackgroundEnabled()) {
    						
    		            	this.drawRect(xStart, yStart, xStart + frameWidth, yStart + frameHeight, this.getPopupBackgroundColor());
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
    				
    	            GL11.glPopMatrix();

    	            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    			}   			
    		}
    	}
    }
    
    public ResourceLocation getTexture() {
    	
    	return this.texture;
    }
    
    /**
     * Установка текстуры.
     * 
     * @param textureLoctaion путь к текстуре
     * @param textureWidth ширина
     * @param textureHeight высота
     * @param u координата текстуры по x
     * @param v координата текстуры по y
     * 
     * @return вызывающий объект
     */
    public T setTexture(ResourceLocation texture, int textureWidth, int textureHeight, int u, int v) {
    	
    	this.texture = texture;
    	
    	this.textureWidth = textureWidth;
    	this.textureHeight = textureHeight;
    	
    	this.textureU = u;
    	this.textureV = v;
    	
    	this.enableTexture();
    	
    	return (T) this;
    }
    
    /**
     * Установка текстуры.
     * 
     * @param textureLoctaion путь к текстуре
     * @param textureWidth ширина
     * @param textureHeight высота
     * 
     * @return вызывающий объект
     */
    public T setTexture(ResourceLocation texture, int textureWidth, int textureHeight) {
    	
    	this.texture = texture;
    	
    	this.textureWidth = textureWidth;
    	this.textureHeight = textureHeight;
    	
    	this.enableTexture();
    	
    	return (T) this;
    }
    
    /**
     * Установка текстуры.
     * 
     * @param textureLoctaion путь к текстуре
     * 
     * @return вызывающий объект
     */
    public T setTexture(ResourceLocation texture) {
    	
    	this.texture = texture;
    	
    	this.enableTexture();
    	
    	return (T) this;
    }  
    
    public int getTextureWidth() {
    	
    	return this.textureWidth;
    }
    
    public int getTextureHeight() {
    	
    	return this.textureHeight;
    }
    
    /**
     * Установка размера текстуры.
     * 
     * @param textureWidth
     * @param textureHeight
     * 
     * @return вызывающий объект
     */
    public T setTextureSize(int textureWidth, int textureHeight) {
    	
    	this.textureWidth = textureWidth;
    	this.textureHeight = textureHeight;
    	
    	return (T) this;
    }
    
    public int getTextureU() {
    	
    	return this.textureU;
    }
    
    public int getTextureV() {
    	
    	return this.textureV;
    }
    
    /**
     * Установка UV координат для текстуры.
     * 
     * @param u
     * @param v
     * 
     * @return вызывающий объект
     */
    public T setTextureUV(int u, int v) {
    	
    	this.textureU = u;
    	this.textureV = v;
    	
    	return (T) this;
    }
    
    public int getTextureOffsetX() {
    	
    	return this.textureOffsetX;
    }
    
    public int getTextureOffsetY() {
    	
    	return this.textureOffsetY;
    }
    
	/**
	 * Устанавлиает отступ для текстуры (от левого верхнего угла элемента).
	 * 
	 * @param xOffset отступ по горизонтали
	 * @param yOffset отступ по вертикали
	 * 
     * @return вызывающий объект
	 */
	public T setTextureOffset(int offsetX, int offsetY) {
		
		this.textureOffsetX = offsetX;
		this.textureOffsetY = offsetY;
		
		return (T) this;
	}
    
    public boolean isTextureEnabled() {
    	
    	return this.isTextureEnabled;
    }
    
    /**
     * Использовать текстуру для элемента, требует установку текстуры {@link GUIAdvancedElement#setTexture(ResourceLocation)} 
     * (используйте один из перегруженных методов).
     * 
     * @return вызывающий объект
     */
    public T enableTexture() {
    	
    	this.isTextureEnabled = true;
    	
    	return (T) this;
    }
    
    public boolean isPopupTextureEnabled() {
    	
    	return this.isPopupTextureEabled;
    }
    
    /**
     * Разрешает использование текстуры для всплывающего сообщения.
     * 
     * @return вызывающий объект
     */
    public T enablePopupTexture() {
    	
    	this.isPopupTextureEabled = true;
    	
    	return (T) this;
    }
    
	public ResourceLocation getPopupTexture() {
		
		return this.popupTexture;
	}
    
    public int getPopupTextureU() {
    	
    	return this.popupTextureU;
    }
    
    public int getPopupTextureV() {
    	
    	return this.popupTextureV;
    }
    
    /**
     * Установка текстуры для всплывающего сообщения.
     * 
     * @param texture
     * 
     * @return вызывающий объект
     */
    public T setPopupTexture(ResourceLocation texture) {
    	
    	this.popupTexture = texture;  
    	
    	this.isPopupTextureEabled = true;
    	
    	return (T) this;
    }
    
    /**
     * Установка текстуры для всплывающего сообщения.
     * 
     * @param texture
     * @param u
     * @param v
     * 
     * @return вызывающий объект
     */
    public T setPopupTexture(ResourceLocation texture, int u, int v) {
    	
    	this.popupTexture = texture;
    	
    	this.popupTextureU = u;
    	this.popupTextureV = v;
    	
    	return (T) this;
    }
	
    public static void drawCustomSizedTexturedRect(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight) {
    	
        float f = 1.0F / (float) textureWidth;
        float f1 = 1.0F / (float) textureHeight;
        
        Tessellator tessellator = Tessellator.instance;
        
        tessellator.startDrawingQuads();
        
        tessellator.addVertexWithUV((double) (x), (double) (y + height), 0, (double) ((float) (u) * f), (double) ((float) (v + height) * f1));
        tessellator.addVertexWithUV((double) (x + width), (double) (y + height), 0, (double) ((float) (u + width) * f), (double) ((float) (v + height) * f1));
        tessellator.addVertexWithUV((double) (x + width), (double) (y), 0, (double) ((float) (u + width) * f), (double) ((float) (v) * f1));
        tessellator.addVertexWithUV((double) (x), (double) (y), 0, (double) ((float) (u) * f), (double) ((float) (v) * f1));
        
        tessellator.draw();
    }
}

