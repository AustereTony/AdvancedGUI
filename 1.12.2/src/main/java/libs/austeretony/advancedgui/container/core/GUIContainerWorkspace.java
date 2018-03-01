package libs.austeretony.advancedgui.container.core;

import libs.austeretony.advancedgui.screen.core.AdvancedGUIScreen;
import libs.austeretony.advancedgui.screen.core.GUIElement;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Объект для настройки рабочего пространства ГПИ.
 */
@SideOnly(Side.CLIENT)
public class GUIContainerWorkspace extends GUIElement {
	
	private int textureOffsetX, textureOffsetY;
					
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
	public GUIContainerWorkspace(AdvancedGUIScreen guiScreen, int width, int height, GUIAlignment alignment, int xOffset, int yOffset) {
		
    	super(1.0F);
		
		this.setSize(width, height);
		
		int 
		xOffsetCalc = alignment == GUIAlignment.CENTER ? 0 : (alignment == GUIAlignment.LEFT || alignment == GUIAlignment.BOTTOM_LEFT || alignment == GUIAlignment.TOP_LEFT? - xOffset : (alignment == GUIAlignment.RIGHT || alignment == GUIAlignment.BOTTOM_RIGHT || alignment == GUIAlignment.TOP_RIGHT ? xOffset : 0)),
		yOffsetCalc = alignment == GUIAlignment.CENTER ? 0 : (alignment == GUIAlignment.TOP || alignment == GUIAlignment.TOP_LEFT || alignment == GUIAlignment.TOP_RIGHT ? - yOffset : (alignment == GUIAlignment.BOTTOM || alignment == GUIAlignment.BOTTOM_LEFT || alignment == GUIAlignment.BOTTOM_RIGHT ? yOffset : 0));
		
		this.setPosition((guiScreen.width - width) / 2 + xOffsetCalc, (guiScreen.height - height) / 2 + yOffsetCalc);
	}
			   
    public void draw() {
    	
    	if (this.isDebugMode()) {
    		
    		this.drawRect(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), this.getDebugColor());
    	}
    	
    	if (this.getTexture() != null) {
    		
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            
            GlStateManager.disableLighting();     
            GlStateManager.enableDepth();      
            GlStateManager.enableBlend();    
            
            this.mc.getTextureManager().bindTexture(this.getTexture());
            
            this.drawCustomSizedTexturedRect(this.getX() + this.getTextureOffsetX(), this.getY() + this.getTextureOffsetY(), 0, 0, this.getTextureWidth(), this.getTextureHeight(), this.getTextureWidth(), this.getTextureHeight()); 
            
            GlStateManager.disableBlend();      
            GlStateManager.disableDepth();      
            GlStateManager.enableLighting();     
    	}   
    }
    
    public int getTextureOffsetX() {
    	
    	return this.textureOffsetX;
    }
    
    public int getTextureOffsetY() {
    	
    	return this.textureOffsetY;
    }
    
	/**
	 * Устанавлиает отступ для текстуры от начала рабочего пространства (от левого верхнего угла).
	 * 
	 * @param xOffset отступ по горизонтали
	 * @param yOffset отступ по вертикали
	 */
	public void setTextureOffset(int offsetX, int offsetY) {
		
		this.textureOffsetX = offsetX;
		this.textureOffsetY = offsetY;
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
