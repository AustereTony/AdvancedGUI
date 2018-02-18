package libs.austeretony.advancedgui.guicontainer.button;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import libs.austeretony.advancedgui.guicontainer.utils.GUIUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

/**
 * Кнопка для ГПИ. Может быть автономна или добавлена на панель GUIButtonPanel.
 */
@SideOnly(Side.CLIENT)
public class GUIButton {

	private ResourceLocation texture;
	
	private Minecraft mc;
    
    public int xPosition, yPosition, buttonWidth, buttonHeight;
    
    private int textureWidth, textureHeight; 
    
    private String displayText, popupText;
    
    private boolean isEnabled, isVisible, isHovered, isToggled, isBackgroundEnabled, isVanillaPopupStyle, isTextureEnabled;
    
    private int 
    buttonColor = 0xFFC6C6C6,
    disabledButtonColor = 0xFF7F7F7F,
    hoveredButtonColor = 0xFFE5E5E5,
    enabledTextColor = 0xFFD1D1D1, 
    disabledTextColor = 0xFF707070,
    hoveredTextColor = 0xFFF2F2F2,
    popupTextColor = 0xFFD1D1D1;
    
    public float scaleFactor;
    
    /**
     * Конструктор для создания автономной кнопки (вне панели).
     * 
     * @param xPosition позиция по x.
     * @param yPosition позиция по y.
     * @param buttonWidth ширина кнопки (активной зоны).
     * @param buttonHeight высота кнопки (активной зоны).
     * @param scaleFactor коэффициент скалирования.
     */
    public GUIButton(int xPosition, int yPosition, int buttonWidth, int buttonHeight, float scaleFactor) {
    	
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;
        this.scaleFactor = scaleFactor;
        
        this.mc = Minecraft.getMinecraft();
        this.displayText = "";
        this.popupText = "";
        
        this.isEnabled = true;
        this.isVisible = true;
        this.isTextureEnabled = true;
        this.isVanillaPopupStyle = true;
    }
    
    /**
     * Конструктор для кнопки, добавляемой на панель.
     */
    public GUIButton() {
    	
        this.mc = Minecraft.getMinecraft();
        this.displayText = "";
        this.popupText = "";
        
        this.isEnabled = true;
        this.isVisible = true;
        this.isTextureEnabled = true;
        this.isVanillaPopupStyle = true;
    }
    
    public void draw() {
    	
        if (this.isVisible()) {
        	
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        	        	
        	GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        	
            GL11.glPushMatrix();

            GL11.glTranslatef(this.xPosition, this.yPosition, 0.0F);
            
            GL11.glScalef(this.scaleFactor, this.scaleFactor, 0.0F);
            
            final int zero = 0;                    	
            
        	if (this.isButtonColorEnabled()) {
        		        		
        		int color;
        		
                if (!this.isEnabled()) {
                	
                	color = this.disabledButtonColor;
                }
                
                else if (this.isHovered() || this.isToggled()) {
                	
                	color = this.hoveredButtonColor;
                }
                
                else {
                	
                	color = this.buttonColor;
                }     		
                
                GUIUtils.drawRect(zero, zero, zero + this.buttonWidth, zero + this.buttonHeight, color);
        	}
        	
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        	
        	if (this.isTextureEnabled()) {
        		
        		int u, v;
        		
        		this.mc.getTextureManager().bindTexture(this.texture);          
        		
                if (!this.isEnabled()) {
                	
                	u = 0;
                	v = 0;
                }
                
                else if (this.isHovered() || this.isToggled()) {
                	
                	u = this.textureWidth * 2;
                	v = 0;
                }
                
                else {
                	
                	u = this.textureWidth;
                	v = 0;
                }
                
                GUIUtils.drawCustomSizedTexturedRect(zero + (this.buttonWidth - this.textureWidth) / 2, zero + (this.buttonHeight - this.textureHeight) / 2, u, v, this.textureWidth, this.textureHeight, this.textureWidth * 3, this.textureHeight);
        	}
        	
        	if (this.getDisplayText().length() > 0) {
        		        		
        		int color;
        		
                if (!this.isEnabled()) {
                	
                	color = this.disabledTextColor;
                }
                
                else if (this.isHovered() || this.isToggled()) {
                	                	
                	color = this.hoveredTextColor;
                }
                
                else {
                	
                	color = this.enabledTextColor;
                }
                                
                this.mc.fontRenderer.drawStringWithShadow(StatCollector.translateToLocal(this.getDisplayText()), zero + (this.buttonWidth - this.mc.fontRenderer.getStringWidth(this.getDisplayText()) / 2) / 2, zero + (this.buttonHeight - 8) / 2, color);
        	}
            
            GL11.glPopMatrix();
            	
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }
    
    public void drawPopup(int mouseX, int mouseY, int guiLeft, int guiTop) {
    	
    	if (this.isVisible()) {   	    		
    		
    		if (this.getPopupText().length() > 0) {
    			    		
    			if (this.isHovered()) {
    				
        	        mouseX -= guiLeft;
        	        mouseY -= guiTop;
        		
        	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	        	
                	GL11.glDisable(GL12.GL_RESCALE_NORMAL);
                	
                    GL11.glPushMatrix();

                    GL11.glTranslatef(mouseX, mouseY, 0.0F);
                    
                    GL11.glScalef(this.scaleFactor, this.scaleFactor, 0.0F);
                    
                    final int zero = 0;     
                    
    				String[] popupStrings = this.getPopupText().split("/n");
    				
    				int i, frameWidth, frameHeight, 
    				xStart = zero + (int) (this.buttonWidth * this.scaleFactor),
    				yStart = zero - popupStrings.length * 10;
    			    				
                    if (this.isVanillaPopupStyle()) {
                    	
                    	int 
                    	color1 = - 267386864,
                    	color2 = 1347420415,
                    	color3 = (color2 & 16711422) >> 1 | color2 & - 16777216;
                    	
                    	frameWidth = this.mc.fontRenderer.getStringWidth(StatCollector.translateToLocal(popupStrings[0]));
                    	frameHeight = 8 * popupStrings.length;
                    	                                                
                        GUIUtils.drawGradientRect(xStart - 3, yStart - 4, xStart + frameWidth + 3, yStart - 3, color1, color1, 300.0);
                        GUIUtils.drawGradientRect(xStart - 3, yStart + frameHeight + 3, xStart + frameWidth + 3, yStart + frameHeight + 4, color1, color1, 300.0);
                        GUIUtils.drawGradientRect(xStart - 3, yStart - 3, xStart + frameWidth + 3, yStart + frameHeight + 3, color1, color1, 300.0);
                        GUIUtils.drawGradientRect(xStart - 4, yStart - 3, xStart - 3, yStart + frameHeight + 3, color1, color1, 300.0);
                        GUIUtils.drawGradientRect(xStart + frameWidth + 3, yStart - 3, xStart + frameWidth + 4, yStart + frameHeight + 3, color1, color1, 300.0);

                        GUIUtils.drawGradientRect(xStart - 3, yStart - 3 + 1, xStart - 3 + 1, yStart + frameHeight + 3 - 1, color2, color3, 300.0);
                        GUIUtils.drawGradientRect(xStart + frameWidth + 2, yStart - 3 + 1, xStart + frameWidth + 3, yStart + frameHeight + 3 - 1, color2, color3, 300.0);
                        GUIUtils.drawGradientRect(xStart - 3, yStart - 3, xStart + frameWidth + 3, yStart - 3 + 1, color2, color2, 300.0);
                        GUIUtils.drawGradientRect(xStart - 3, yStart + frameHeight + 2, xStart + frameWidth + 3, yStart + frameHeight + 3, color3, color3, 300.0);
                    }
    			
    				for (i = 0; i < popupStrings.length; i++) {    				
    				
    					this.mc.fontRenderer.drawStringWithShadow(StatCollector.translateToLocal(popupStrings[i]), xStart, yStart + 10 * i, this.popupTextColor);
    				}
    				
                    GL11.glPopMatrix();
                	
                    GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    			}   			
    		}
    	}
    }

    public void mouseOver(int mouseX, int mouseY, int guiLeft, int guiTop) {
    	
        mouseX -= guiLeft;
        mouseY -= guiTop;
    		
    	this.isHovered = this.isEnabled() && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + (int) (this.buttonWidth * this.scaleFactor) && mouseY < this.yPosition + (int) (this.buttonHeight * this.scaleFactor);   
    }

    public boolean mouseClicked() {
    	
        return Mouse.isButtonDown(0) && this.isHovered();
    }

    public void playSound(ResourceLocation location, float loudness) {
    	
        this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(location, loudness));
    }
    
    /**
     * Установка текстуры для кнопки.
     * 
     * @param textureLoctaion путь к текстуре.
     * @param textureWidth ширина иконки для кнопки (не текстуры, а иконки).
     * @param textureHeight высота.
     */
    public void setTexture(ResourceLocation textureLoctaion, int textureWidth, int textureHeight) {
    	
    	this.texture = textureLoctaion;
    	
    	this.textureWidth = textureWidth;
    	this.textureHeight = textureHeight;
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
    
    public boolean isToggled() {
    	
    	return this.isToggled;
    }
    
    public void setToggled(boolean isToggled) {
    	
    	this.isToggled = isToggled;
    }
    
    public String getDisplayText() {
    	
    	return this.displayText;
    }
    
    public void setDisplayText(String displayText) {
    	
    	this.displayText = displayText;
    }
    
    public String getPopupText() {
    	
    	return this.popupText;
    }
    
    public boolean isVanillaPopupStyle() {
    	
    	return this.isVanillaPopupStyle;
    }
    
    public void setCustomPopupStyle() {
    	
    	this.isVanillaPopupStyle = false;
    }
    
    /**
     * Добавляет всплывающий текст при наведении курсора на кнопку.
     */
    public void setPopupText(String popupText) {
    	
    	this.popupText = popupText;
    }
    
    public void setEnabledTextColor(int colorHex) {
    	
    	this.enabledTextColor = colorHex;
    }
    
    public void setDisabledTextColor(int colorHex) {
    	
    	this.disabledTextColor = colorHex;
    }
    
    public void setHoveredTextColor(int colorDec) {
    	
    	this.hoveredTextColor = colorDec;
    }
    
    public void setPopupTextColor(int colorHex) {
    	
    	this.popupTextColor = colorHex;
    }
    
    public boolean isButtonColorEnabled() {
    	
    	return this.isBackgroundEnabled;
    }
    
    public void setBackgroundEnabled(boolean isEnabled) {
    	
    	this.isBackgroundEnabled = isEnabled;
    }
    
    public void setButtonColor(int colorHex) {
    	
    	this.buttonColor = colorHex;
    }
    
    public void setDisabledButtonColor(int colorHex) {
    	
    	this.disabledButtonColor = colorHex;
    }
    
    public void setHoveredButtonColor(int colorHex) {
    	
    	this.hoveredButtonColor = colorHex;
    }
    
    public boolean isTextureEnabled() {
    	
    	return this.isTextureEnabled;
    }
    
    public void setTextureEnabled(boolean isEnabled) {
    	
    	this.isTextureEnabled = isEnabled;	
    }
}
