package ru.austeretony.advancedgui.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class AdvancedGUIUtils {
	
	public static final Logger LOGGER = LogManager.getLogger();

	private AdvancedGUIUtils() {}
	
    public static void drawCustomSizedTexturedRect(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight) {
    	
        float f = 1.0F / textureWidth;
        float f1 = 1.0F / textureHeight;
        
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();       
        
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        
        bufferbuilder.pos((double) x, (double) (y + height), 0.0D).tex((double) (u * f), (double) ((v + (float) height) * f1)).endVertex();
        bufferbuilder.pos((double) (x + width), (double) (y + height), 0.0D).tex((double) ((u + (float) width) * f), (double) ((v + (float) height) * f1)).endVertex();
        bufferbuilder.pos((double) (x + width), (double) y, 0.0D).tex((double) ((u + (float) width) * f), (double) (v * f1)).endVertex();
        bufferbuilder.pos((double) x, (double) y, 0.0D).tex((double) (u * f), (double) (v * f1)).endVertex();
        
        tessellator.draw();
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

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        
        Tessellator tessellator = Tessellator.getInstance();
        
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        
        bufferbuilder.pos((double)left, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)bottom, 0.0D).endVertex();
        bufferbuilder.pos((double)right, (double)top, 0.0D).endVertex();
        bufferbuilder.pos((double)left, (double)top, 0.0D).endVertex();
        
        tessellator.draw();
        
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor, double zLevel) {
    	
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;
        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;
        
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        
        Tessellator tessellator = Tessellator.getInstance();
        
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        
        bufferbuilder.pos((double) right, (double)top, zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) left, (double)top, zLevel).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos((double) left, (double)bottom, zLevel).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos((double) right, (double)bottom, zLevel).color(f5, f6, f7, f4).endVertex();
        
        tessellator.draw(); 
        
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}
