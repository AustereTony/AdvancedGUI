package libs.austeretony.advancedgui.screen.core;

import java.util.ArrayList;
import java.util.List;

import libs.austeretony.advancedgui.screen.button.GUIButton;
import libs.austeretony.advancedgui.screen.framework.GUIFramework;
import libs.austeretony.advancedgui.screen.panel.GUIButtonPanel;
import libs.austeretony.advancedgui.screen.text.GUITextField;
import libs.austeretony.advancedgui.screen.text.GUITextLabel;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Класс, инкапсулирующий элементы ГПИ. ГПИ может содержать несколько разных разделов. 
 * Единовременно может отображаться только один раздел.
 * 
 * @author AustereTony
 */
public abstract class GUISection extends GUIAdvancedElement<GUISection> {
	
	protected final AdvancedGUIScreen screen;
	
	private final List<GUITextLabel> textLabelsList = new ArrayList<GUITextLabel>();
	
	private final List<GUITextField> textFieldsList = new ArrayList<GUITextField>();
		
    private final List<GUIButton> buttonsList = new ArrayList<GUIButton>();
    
    private final List<GUIButtonPanel> buttonPanelsList = new ArrayList<GUIButtonPanel>();	
	
    /** Массив объектов GUIFramework, инкапсулирующих элементы ГПИ. */
	public final List<GUIFramework> frameworks = new ArrayList<GUIFramework>();
	
	public GUISection(AdvancedGUIScreen screen) {
		
		this.screen = screen;
		
		this.setDebugColor(0x6400FFFF);
	}
	
	/**
	 * Вызывается при создании или обновлении раздела.
	 */
	public abstract void init();
	
	@Override
    public void draw() {
		
    	if (this.isDebugMode()) {
    		
    		this.drawRect(this.getX() + this.getTextureOffsetX(), this.getY() + this.getTextureOffsetY(), this.getX() + this.getTextureOffsetX() + this.getTextureWidth(), this.getY() + this.getTextureOffsetY() + this.getTextureHeight(), this.getDebugColor());
    	}
    	
    	if (this.isTextureEnabled()) {
    		
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
	
    /**
     * Используется для добавления фреймворка в раздел.
     * 
     * @param framework содержащий набор слотов и управляющих элементов
     */
    public final void addFramework(GUIFramework framework) {
    	
    	if (!this.frameworks.contains(framework)) this.frameworks.add(framework);
    }
    
	public final List<GUITextLabel> getTextLabelsList() {
		
		return this.textLabelsList;
	}
    
	/**
	 * Добавляет объект GUITextLabel в раздел.
	 * 
	 * @param textLabel
	 */
	public final void addTextLabel(GUITextLabel textLabel) {
		
		if (!this.textLabelsList.contains(textLabel)) this.textLabelsList.add(textLabel);
	}
	
	public final List<GUITextField> getTextFieldsList() {
		
		return this.textFieldsList;
	}
    
	/**
	 * Добавляет объект GUITextField в раздел.
	 * 
	 * @param textField
	 */
	public final void addTextField(GUITextField textField) {
		
		if (!this.textFieldsList.contains(textField)) this.textFieldsList.add(textField);
	}
    
	public final List<GUIButton> getButtonsList() {
		
		return this.buttonsList;
	}
    
	/**
	 * Добавляет объект GUIButton в раздел.
	 * 
	 * @param button
	 */
	public final void addButton(GUIButton button) {
		
		if (!this.buttonsList.contains(button)) this.buttonsList.add(button);
	}
	
	/**
	 * Добавляет объект GUIButtonPanel для GUIButton в раздел.
	 * 
	 * @param buttonPanel
	 */
	public final void addButtonPanel(GUIButtonPanel buttonPanel) {
		
		if (!this.buttonPanelsList.contains(buttonPanel)) this.buttonPanelsList.add(buttonPanel);
	}
	
	public final List<GUIButtonPanel> getButtonPanelsList() {
		
		return this.buttonPanelsList;
	}
	
    /**
     * Метод для управления активностью кнопок. Если кнопка не относится к определённому фреймворку
     * GUIFramework будет null.
     * 
     * @param section раздел, к которому относится кнопка
     * @param framework фреймворк, к которому относится активированная кнопка
     * @param button кнопка, которая была активирована
     */
	public abstract void handleButtonPress(GUISection section, GUIFramework framework, GUIButton button);
}
