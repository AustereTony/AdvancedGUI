package libs.austeretony.advancedgui.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Класс для создания форматированного текста, используется GUITextBuilder. 
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUITextFormatter {

    private final List<String> rawStringsList = new ArrayList<String>();
	
    //Компоненты убраны, т.к. используется обёртка GUITextBuilder.
    //private final List<GUITextFormatter> componentsList = new ArrayList<GUITextFormatter>();
        
    private String formattingCodeBefore, formattingCodeAfter;
    
    /**
     * Конструктор форматтера текста. 
     * 
     */
    public GUITextFormatter() {
    	
    	this.formattingCodeBefore = "";
    	this.formattingCodeAfter = "";
    }
    
    /**
     * Возвращает строку, переданную в конструктор.
     * 
     * @return
     */
    public String getRawText() {
    	
        StringBuilder stringBuilder = new StringBuilder();

        for (String string : this.rawStringsList) {
        	
        	stringBuilder.append(string);
        }
        
		return stringBuilder.toString();
    }
    
    /**
     * Возвращает отформатированную строку с учётом всех компонентов.
     * 
     * @return отформатированная строка
     */
    public String getFormattedText() {
    	
        StringBuilder stringBuilder = new StringBuilder();
    	
        stringBuilder.append(this.formattingCodeBefore);
        
        for (String string : this.rawStringsList) {
        	
        	stringBuilder.append(string);
        }       
        
        stringBuilder.append(this.formattingCodeAfter);
            	
    	/*for (GUITextFormatter textBuilder : this.componentsList) {
    		
    		stringBuilder.append(textBuilder.getFormattedText());
    	}*/
    	
    	return stringBuilder.toString();
    }
    
    /**
     * Возвращает список компонентов форматтера.
     * 
     * @return
     */
    /*public List<GUITextFormatter> getComponents() {
    	
    	return this.componentsList;
    }*/
    
    /**
     * Возвращает список компонентов форматтера.
     * 
     * @return
     */
    public List<String> getRawStringsList() {
    	
    	return this.rawStringsList;
    }
    
    /**
     * Установка цвета, можно выбрать один из 16 доступных в GUIEnumTextColor.
     * 
     * @param color
     * 
     * @return
     */
    public GUITextFormatter setColor(GUIEnumTextColor color) {
    	
    	this.formatBefore(color.getCode());
    	
    	return this;
    }
    
    public GUITextFormatter setObfuscated() {
    	
    	this.formatBefore(GUIEnumTextStyle.OBFUSCATED.getCode());
    	
    	return this;
    }
    
    public GUITextFormatter setBold() {
    	
    	this.formatBefore(GUIEnumTextStyle.BOLD.getCode());
    	
    	return this;
    }
    
    public GUITextFormatter setStrikethrough() {
    	
    	this.formatBefore(GUIEnumTextStyle.STRIKETHROUGH.getCode());
    	
    	return this;
    }
    
    public GUITextFormatter setUnderlined() {
    	
    	this.formatBefore(GUIEnumTextStyle.UNDERLINE.getCode());
    	
    	return this;
    }
    
    public GUITextFormatter setItalic() {
    	
    	this.formatBefore(GUIEnumTextStyle.ITALIC.getCode());
    	
    	return this;
    }
    
    /**
     * Добавление пробела между строками.
     * 
     * @return
     */
    public GUITextFormatter addSpace() {
    	
    	this.rawStringsList.add(" ");
    	
    	return this;
    }
    
    /**
     * Остановка форматирования (все последующие СТРОКИ, добавленные через {@link GUITextFormatter#addText(String)} 
     * не будут форматироваться и НЕ МОГУТ быть форматированы).
     * 
     * @return
     */
    public GUITextFormatter stopFormatting() {
    	
    	this.formatAfter(GUIEnumTextStyle.RESET.getCode());
    	
    	return this;
    }
    
    /**
     * Добавление строки к ТЕКУЩЕМУ элементу. Строку нельзя изменить извне.
     * 
     * @param text
     * 
     * @return
     */
    public GUITextFormatter addText(String text) {
    	
    	this.rawStringsList.add(text);
    	
    	return this;
    }
    
    /**
     * Добавление нового компонента.
     * 
     * @param textBuilder
     * 
     * @return
     */
    /*public GUITextFormatter addComponent(GUITextFormatter textBuilder) {
    	
    	this.componentsList.add(textBuilder);
    	
    	return this;
    }*/
    
    private void formatBefore(String code) {
    	
    	this.formattingCodeBefore = code + this.formattingCodeBefore;
    }
    
    private void formatAfter(String code) {
    	
    	this.formattingCodeAfter = this.formattingCodeAfter + code;
    }
    
    public String getBeforeFormattingCode() {
    	
    	return this.formattingCodeBefore;
    }
    
    public void setBeforeFormattingCode(String code) {
    	
    	this.formattingCodeBefore = code;
    }
    
    public String getAfterFormattingCode() {
    	
    	return this.formattingCodeAfter;
    }
    
    public void setAfterFormattingCode(String code) {
    	
    	this.formattingCodeAfter = code;
    }
    
    public GUITextFormatter copy() {
    	
    	GUITextFormatter formatterCopy = new GUITextFormatter();
    	
    	formatterCopy.setBeforeFormattingCode(this.getBeforeFormattingCode());
    	formatterCopy.setAfterFormattingCode(this.getAfterFormattingCode());
    	
    	for (String rawString : this.getRawStringsList()) {
    		
    		formatterCopy.getRawStringsList().add(rawString);
    	}
    	
    	/*for (GUITextFormatter formatter : this.getComponents()) {
    		
    		formatterCopy.addComponent(formatter);
    	}*/
    	
    	return formatterCopy;
    }
    
    /**
     * Enum с кодами для форматирования цвета.
     */
    @SideOnly(Side.CLIENT)
    public enum GUIEnumTextColor {
    	
    	BLACK("\u00a70"),
    	DARK_BLUE("\u00a71"),
    	DARK_GREEN("\u00a72"),
    	DARK_AQUA("\u00a73"),
    	DARK_RED("\u00a74"),
    	DARK_PURPLE("\u00a75"),
    	GOLD("\u00a76"),
    	GRAY("\u00a77"),
    	DARK_GRAY("\u00a78"),
    	BLUE("\u00a79"),
    	GREEN("\u00a7a"),
    	AQUA("\u00a7b"),
    	RED("\u00a7c"),
    	LIGHT_PURPLE("\u00a7d"),
    	YELLOW("\u00a7e"),
    	WHITE("\u00a7f");
    	
    	private final String formatCode;
    	
    	GUIEnumTextColor(String code) {
    		
    		this.formatCode = code;
    	}
    	
    	public String getCode() {
    		
    		return this.formatCode;
    	}
    }
    
    /**
     * Enum с кодами форматирования стиля.
     */
    @SideOnly(Side.CLIENT)
    public enum GUIEnumTextStyle {

    	OBFUSCATED("\u00a7k"),
    	BOLD("\u00a7l"),
    	STRIKETHROUGH("\u00a7m "),
    	UNDERLINE("\u00a7n"),
    	ITALIC("\u00a7o"),
    	RESET("\u00a7r");
    	
    	private final String formatCode;
    	
    	GUIEnumTextStyle(String code) {
    		
    		this.formatCode = code;
    	}
    	
    	public String getCode() {
    		
    		return this.formatCode;
    	}
    }
}
