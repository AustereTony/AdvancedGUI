package libs.austeretony.advancedgui.guicontainer.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;

public class GUIUtils {
	
	public static final Logger LOGGER = LogManager.getLogger();

	private GUIUtils() {}
	
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
