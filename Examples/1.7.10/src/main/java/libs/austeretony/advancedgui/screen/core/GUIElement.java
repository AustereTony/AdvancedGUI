package libs.austeretony.advancedgui.screen.core;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

/**
 * Класс-основа графических элементов ГПИ.
 */
@SideOnly(Side.CLIENT)
public class GUIElement {

	protected final Minecraft mc = Minecraft.getMinecraft();
		
	private ResourceLocation texture;
	
	public final static int ZERO = 0;
	
    private int xPosition, yPosition, width, height, textureWidth, textureHeight, textureU, textureV;
        
    private boolean isDebugMode, isEnabled, isVisible, isHovered, isToggled, isDragged, isBackgroundEnabled, isTextureEnabled, isCustomPopup;
    
    private String displayText, popupText;
    
    private int 
    debugColor = 0x64FF00DC,
    enabledColor = 0xFFA5A5A5,
    disabledColor = 0xFF666666,
    hoveredColor = 0xFFBFBFBF,
    enabledBackgroundColor = 0xFF555555,
    disabledBackgroundColor = 0xFF404040,
    hoveredBackgroundColor = 0xFF505050,
    popupBackgroundColor = 0xFF404040,
    enabledTextColor = 0xFFD1D1D1, 
    disabledTextColor = 0xFF707070,
    hoveredTextColor = 0xFFF2F2F2,
    popupTextColor = 0xFFD1D1D1;
    
    private float scaleFactor;
    
    public GUIElement(float scaleFactor) {
    	
    	this.scaleFactor = scaleFactor;
    	
        this.displayText = "";
        this.popupText = "";
    }
    
    public void draw() {}
    
    public void drawPopup(int mouseX, int mouseY) {}
    
    public void mouseOver(int mouseX, int mouseY) {
		
    	this.setHovered(this.isEnabled() && mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + (int) (this.getWidth() * this.getScale()) && mouseY < this.getY() + (int) (this.getHeight() * this.getScale()));   
    }

    public boolean mouseClicked() {
    	
        return Mouse.isButtonDown(0) && this.isHovered();
    }
    
    public ResourceLocation getTexture() {
    	
    	return this.texture;
    }
    
    /**
     * Установка текстуры.
     * @param textureWidth ширина
     * @param textureHeight высота
     * @param u координата текстуры по x
     * @param v координата текстуры по y
     * @param textureLoctaion путь к текстуре
     */
    public void setTexture(ResourceLocation texture, int textureWidth, int textureHeight, int u, int v) {
    	
    	this.texture = texture;
    	
    	this.textureWidth = textureWidth;
    	this.textureHeight = textureHeight;
    	
    	this.textureU = u;
    	this.textureV = v;
    }
    
    public void setTexture(ResourceLocation texture) {
    	
    	this.texture = texture;
    }  
    
    public void setTexture(ResourceLocation texture, int textureWidth, int textureHeight) {
    	
    	this.texture = texture;
    	
    	this.textureWidth = textureWidth;
    	this.textureHeight = textureHeight;
    }
    
    public int getTextureWidth() {
    	
    	return this.width;
    }
    
    public int getTextureHeight() {
    	
    	return this.height;
    }
    
    public void setTextureSize(int textureWidth, int textureHeight) {
    	
    	this.textureWidth = textureWidth;
    	this.textureHeight = textureHeight;
    }
    
    public int getTextureU() {
    	
    	return this.textureU;
    }
    
    public int getTextureV() {
    	
    	return this.textureV;
    }
    
    public void setTextureUV(int u, int v) {
    	
    	this.textureU = u;
    	this.textureV = v;
    }
    
    public int getX() {
    	
    	return this.xPosition;
    }
    
    public int getY() {
    	
    	return this.yPosition;
    }
    
    public void setPosition(int xPosition, int yPosition) {
    	
    	this.xPosition = xPosition;
    	this.yPosition = yPosition;
    } 
    
    public int getWidth() {
    	
    	return this.width;
    }
    
    public int getHeight() {
    	
    	return this.height;
    }
    
    public void setSize(int width, int height) {
    	
    	this.width = width;
    	this.height = height;
    }
    
    public boolean isDebugMode() {
    	
    	return this.isDebugMode;
    }
    
	/**
	 * Заливает рабочую область установленным цветом для отражения границ и положения.
	 */
    public void setDebugMode(boolean isDebugMode) {
    	
    	this.isDebugMode = isDebugMode;
    } 
    
    public boolean isEnabled() {
    	
    	return this.isEnabled;
    }
    
    public void setEnabled(boolean isEnabled) {
    	
    	this.isEnabled = isEnabled;
    }  
    
    public boolean isVisible() {
    	
    	return this.isVisible;
    }
    
    public void setVisible(boolean isVisible) {
    	
    	this.isVisible = isVisible;
    }
    
    public boolean isHovered() {
    	
        return this.isHovered;
    }
    
    public void setHovered(boolean isHovered) {
    	
    	this.isHovered = isHovered;
    }
    
    public boolean isToggled() {
    	
        return this.isDragged;
    }
    
    public void setToggled(boolean isToggled) {
    	
    	this.isToggled = isToggled;
    }
    
    public boolean isDragged() {
    	
        return this.isDragged;
    }
    
    public void setDragged(boolean isDragged) {
    	
    	this.isDragged = isDragged;
    }
    
    public boolean isBackgroundEnabled() {
    	
    	return this.isBackgroundEnabled;
    }
    
    public void setBackgroundEnabled(boolean isEnabled) {
    	
    	this.isBackgroundEnabled = isEnabled;
    }
    
    public boolean isTextureEnabled() {
    	
    	return this.isTextureEnabled;
    }
    
    public void setTextureEnabled(boolean isEnabled) {
    	
    	this.isTextureEnabled = isEnabled;
    }
    
    public boolean isCustomPopup() {
    	
    	return this.isCustomPopup;
    }
    
    public void setCustomPopup(boolean isCustomPopup) {
    	
    	this.isCustomPopup = isCustomPopup;
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
    
    /**
     * Добавляет всплывающий текст при наведении курсора.
     */
    public void setPopupText(String popupText) {
    	
    	this.popupText = popupText;
    }
    
    public int getDebugColor() {
    	
    	return this.debugColor;
    }
    
    public void setDebugColor(int colorHex) {
    	
    	this.debugColor = colorHex;
    }
    
    public int getEnabledColor() {
    	
    	return this.enabledColor;
    }
    
    public void setEnabledColor(int colorHex) {
    	
    	this.enabledColor = colorHex;
    }
    
    public int getDisabledColor() {
    	
    	return this.disabledColor;
    }
    
    public void setDisabledColor(int colorHex) {
    	
    	this.disabledColor = colorHex;
    }
    
    public int getHoveredColor() {
    	
    	return this.hoveredColor;
    }
    
    public void setHoveredColor(int colorHex) {
    	
    	this.hoveredColor = colorHex;
    }
    
    public int getEnabledBackgroundColor() {
    	
    	return this.enabledBackgroundColor;
    }
    
    public void setEnabledBackgroundColor(int colorHex) {
    	
    	this.enabledBackgroundColor = colorHex;
    }
    
    public int getDisabledBackgroundColor() {
    	
    	return this.disabledBackgroundColor;
    }
    
    public void setDisabledBackgroundColor(int colorHex) {
    	
    	this.disabledBackgroundColor = colorHex;
    }
    
    public int getHoveredBackgroundColor() {
    	
    	return this.hoveredBackgroundColor;
    }
    
    public void setHoveredBackgroundColor(int colorHex) {
    	
    	this.hoveredBackgroundColor = colorHex;
    }
    
    public int getPopupBackgroundColor() {
    	
    	return this.popupBackgroundColor;
    }
    
    public void setPopupBackgroundColor(int colorHex) {
    	
    	this.popupBackgroundColor = colorHex;
    }
    
    public int getEnabledTextColor() {
    	
    	return this.enabledTextColor;
    }
    
    public void setEnabledTextColor(int colorHex) {
    	
    	this.enabledTextColor = colorHex;
    }
    
    public int getDisabledTextColor() {
    	
    	return this.disabledTextColor;
    }
    
    public void setDisabledTextColor(int colorHex) {
    	
    	this.disabledTextColor = colorHex;
    }
    
    public int getHoveredTextColor() {
    	
    	return this.hoveredTextColor;
    }
    
    public void setHoveredTextColor(int colorHex) {
    	
    	this.hoveredTextColor = colorHex;
    }
    
    public int getPopupTextColor() {
    	
    	return this.popupTextColor;
    }
    
    public void setPopupTextColor(int colorHex) {
    	
    	this.popupTextColor = colorHex;
    }
    
    public float getScale() {
    	
    	return this.scaleFactor;
    }
    
    public void setScale(float scaleFactor) {
    	
    	this.scaleFactor = scaleFactor;
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
    
    public static void drawRect(int xStart, int yStart, int xEnd, int yEnd, int color) {
    	
        int j1;

        if (xStart < xEnd) {
        	
            j1 = xStart;
            xStart = xEnd;
            xEnd = j1;
        }

        if (yStart < yEnd) {
        	
            j1 = yStart;
            yStart = yEnd;
            yEnd = j1;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        
        Tessellator tessellator = Tessellator.instance;
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        
        GL11.glColor4f(f, f1, f2, f3);
        
        tessellator.startDrawingQuads();
        
        tessellator.addVertex((double) xStart, (double) yEnd, 0.0D);
        tessellator.addVertex((double) xEnd, (double) yEnd, 0.0D);
        tessellator.addVertex((double) xEnd, (double) yStart, 0.0D);
        tessellator.addVertex((double) xStart, (double) yStart, 0.0D);
        
        tessellator.draw();
        
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    public static void drawGradientRect(int xStart, int yStart, int xEnd, int yEnd, int colorDec1, int colorDec2, double zLevel) {
    	
        float f = (float) (colorDec1 >> 24 & 255) / 255.0F;
        float f1 = (float) (colorDec1 >> 16 & 255) / 255.0F;
        float f2 = (float) (colorDec1 >> 8 & 255) / 255.0F;
        float f3 = (float) (colorDec1 & 255) / 255.0F;
        float f4 = (float) (colorDec2 >> 24 & 255) / 255.0F;
        float f5 = (float) (colorDec2 >> 16 & 255) / 255.0F;
        float f6 = (float) (colorDec2 >> 8 & 255) / 255.0F;
        float f7 = (float) (colorDec2 & 255) / 255.0F;
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        
        GL11.glShadeModel(GL11.GL_SMOOTH);
        
        Tessellator tessellator = Tessellator.instance;
        
        tessellator.startDrawingQuads();
        
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        
        tessellator.addVertex((double) xEnd, (double) yStart, zLevel);
        tessellator.addVertex((double) xStart, (double) yStart, zLevel);
        
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        
        tessellator.addVertex((double) xStart, (double) yEnd, zLevel);
        tessellator.addVertex((double) xEnd, (double) yEnd, zLevel);
        
        tessellator.draw();
        
        GL11.glShadeModel(GL11.GL_FLAT);
        
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}

