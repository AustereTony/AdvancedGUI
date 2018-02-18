package libs.austeretony.advancedgui.guicontainer.core;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import libs.austeretony.advancedgui.guicontainer.utils.GUIUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

/**
 * Объект для настройки рабочего пространства ГПИ.
 */
@SideOnly(Side.CLIENT)
public class GUIContainerWorkspace {
	
	private Minecraft mc;

	private int xPosition, yPosition, width, height, textureXOffset, textureYOffset, textureWidth, textureHeight;
	
	private boolean debugMode;
	
	private int debugBackgroundColor = 0x64FF00DC;
	
	private ResourceLocation texture;
		
	/**
	 * Создаёт рабочее пространство для ГПИ 
	 * (определяет зону, в которой можно взаимодействовать со слотами).
	 * Юстировка позволяет сдвинуть эту область в определённом направлении, 
	 * при этом сдвигаются все элементы на экране.
	 * 
	 * @param guiScreen GuiScreen для которого создаётся пространство
	 * @param width ширина рабочей зоны
	 * @param height высота рабочей зоны
	 * @param alignment параметр юстировки
	 * @param xOffset отступ по горизонтали
	 * @param yOffset отступ по вертикали
	 */
	public GUIContainerWorkspace(GuiScreen guiScreen, int width, int height, GUIAlignment alignment, int xOffset, int yOffset) {
		
		this.width = width;
		this.height = height;
		
		int 
		xOffsetCalc = alignment == GUIAlignment.CENTER ? 0 : (alignment == GUIAlignment.LEFT || alignment == GUIAlignment.BOTTOM_LEFT || alignment == GUIAlignment.TOP_LEFT? - xOffset : (alignment == GUIAlignment.RIGHT || alignment == GUIAlignment.BOTTOM_RIGHT || alignment == GUIAlignment.TOP_RIGHT ? xOffset : 0)),
		yOffsetCalc = alignment == GUIAlignment.CENTER ? 0 : (alignment == GUIAlignment.TOP || alignment == GUIAlignment.TOP_LEFT || alignment == GUIAlignment.TOP_RIGHT ? - yOffset : (alignment == GUIAlignment.BOTTOM || alignment == GUIAlignment.BOTTOM_LEFT || alignment == GUIAlignment.BOTTOM_RIGHT ? yOffset : 0));
		
		this.xPosition = (guiScreen.width - width) / 2 + xOffsetCalc;
		this.yPosition = (guiScreen.height - height) / 2 + yOffsetCalc;
		
        this.mc = Minecraft.getMinecraft();
	}
	
	public int getXPosition() {
		
		return this.xPosition;
	}
	
	public int getYPosition() {
		
		return this.yPosition;
	}
	
	public int getWidth() {
		
		return this.width;
	}
	
	public int getHeight() {
		
		return this.height;
	}
	
	/**
	 * Заливает рабочую область установленным цветом для отражения границ и положения.
	 */
	public void enableDebugMode() {
		
		this.debugMode = true;
	}
	
	/**
	 * Устанавлиает отступ для текстуры от начала рабочего пространства (от левого верхнего угла).
	 * 
	 * @param xOffset отступ по горизонтали
	 * @param yOffset отступ по вертикали
	 */
	public void setTextureOffset(int xOffset, int yOffset) {
		
		this.textureXOffset = xOffset;
		this.textureYOffset = yOffset;
	}
	
    /**
     * Установка текстуры для ГПИ.
     * 
     * @param textureLoctaion путь к текстуре
     * @param textureWidth ширина
     * @param textureHeight высота
     */
    public void setTexture(ResourceLocation textureLoctaion, int textureWidth, int textureHeight) {
    	
    	this.texture = textureLoctaion;
    	
    	this.textureWidth = textureWidth;
    	this.textureHeight = textureHeight;
    }
    
    public void drawTexture() {
    	
    	if (this.debugMode) {
    		
    		GUIUtils.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, this.debugBackgroundColor);
    	}
    	
    	if (this.texture != null) {
    		
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            
            this.mc.getTextureManager().bindTexture(this.texture);
            
            GUIUtils.drawCustomSizedTexturedRect(this.xPosition + this.textureXOffset, this.yPosition + this.textureYOffset, 0, 0, this.textureWidth, this.textureHeight, this.textureWidth, this.textureHeight); 
            
            GL11.glDisable(GL11.GL_BLEND);	
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_LIGHTING);
    	}   
    }
    
    public void setDebugBackgroundColor(int colorHex) {
    	
    	this.debugBackgroundColor = colorHex;
    }
    
    /**
     * Enum для юстировки рабочего пространства.
     */
    public enum GUIAlignment {
    	
    	LEFT,
    	BOTTOM_LEFT,
    	TOP_LEFT,
    	RIGHT,
    	BOTTOM_RIGHT,
    	TOP_RIGHT,
    	TOP,
    	BOTTOM,
    	CENTER
    }   
}
