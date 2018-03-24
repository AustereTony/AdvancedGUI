package libs.austeretony.advancedgui.util;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import libs.austeretony.advancedgui.util.GUITextFormatter.GUIEnumTextColor;

/**
 * Класс для создания форматированного текста. 
 * Доступны все стандартные цвета и стили майнкрафта.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUITextBuilder {

    private final List<GUITextFormatter> componentsList = new ArrayList<GUITextFormatter>();
    
    private GUITextFormatter currentFormatter;

    /**
     * Возвращает отформатированную строку с учётом всех компонентов.
     * 
     * @return отформатированная строка
     */
    public String getFormattedText() {
    	
        StringBuilder stringBuilder = new StringBuilder();
    	        	
    	for (GUITextFormatter textBuilder : this.componentsList) {
    		
    		stringBuilder.append(textBuilder.getFormattedText());
    	}
    	
    	stringBuilder.append(this.currentFormatter.getFormattedText());
    	
    	return stringBuilder.toString();
    }
    
    public GUITextBuilder newPart() {
    	
    	if (this.currentFormatter != null) {
    		
    		this.componentsList.add(this.currentFormatter.copy());
    		
    		this.currentFormatter = new GUITextFormatter();
    	}
    	
    	else {
    		
    		this.currentFormatter = new GUITextFormatter();
    	}
    	
    	return this;
    } 
    
    /**
     * Установка цвета, можно выбрать один из 16 доступных в GUIEnumTextColor.
     * 
     * @param color
     * 
     * @return
     */
    public GUITextBuilder setColor(GUIEnumTextColor color) {
    	
    	this.currentFormatter.setColor(color);
    	
    	return this;
    }
    
    public GUITextBuilder setObfuscated() {
    	
    	this.currentFormatter.setObfuscated();
    	
    	return this;
    }
    
    public GUITextBuilder setBold() {
    	
    	this.currentFormatter.setBold();
    	
    	return this;
    }
    
    public GUITextBuilder setStrikethrough() {
    	
    	this.currentFormatter.setStrikethrough();
    	
    	return this;
    }
    
    public GUITextBuilder setUnderlined() {
    	
    	this.currentFormatter.setUnderlined();
    	
    	return this;
    }
    
    public GUITextBuilder setItalic() {
    	
    	this.currentFormatter.setItalic();
    	
    	return this;
    }
    
    /**
     * Добавление пробела между строками.
     * 
     * @return
     */
    public GUITextBuilder addSpace() {
    	
    	this.currentFormatter.addSpace();
    	
    	return this;
    }
    
    /**
     * Остановка форматирования (все последующие СТРОКИ, добавленные через {@link GUITextFormatter#addText(String)} 
     * не будут форматироваться и НЕ МОГУТ быть форматированы).
     * 
     * @return
     */
    public GUITextBuilder stopFormatting() {
    	
    	this.currentFormatter.stopFormatting();
    	
    	return this;
    }
    
    /**
     * Добавление строки к ТЕКУЩЕМУ элементу. Строку нельзя изменить извне.
     * 
     * @param text
     * 
     * @return
     */
    public GUITextBuilder addText(String text) {
    	
    	this.currentFormatter.addText(text);
    	
    	return this;
    }
    
    public String toString() {
    	
    	return this.getFormattedText();
    }
}
