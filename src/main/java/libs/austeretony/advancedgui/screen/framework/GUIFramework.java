package libs.austeretony.advancedgui.screen.framework;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import libs.austeretony.advancedgui.screen.button.GUIButton;
import libs.austeretony.advancedgui.screen.core.GUIBaseElement;
import libs.austeretony.advancedgui.screen.panel.GUIButtonPanel;
import libs.austeretony.advancedgui.screen.text.GUITextField;

/**
 * Объект-каркас для работы с элементами ГПИ простого графического интерфейса.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUIFramework extends GUIBaseElement<GUIFramework> {
	
	private final List<GUITextField> textFieldsList = new ArrayList<GUITextField>();

    private final List<GUIButton> buttonsList = new ArrayList<GUIButton>();
    
    private final List<GUIButtonPanel> buttonPanelsList = new ArrayList<GUIButtonPanel>();
        
	public List<GUITextField> getTextFieldsList() {
		
		return this.textFieldsList;
	}
	
	public GUIFramework() {
		
		this.setEnabled(true);
	}
    
	/**
	 * Добавляет объект GUITextField в фреймворк.
	 * 
	 * @param textField
	 */
	public void addTextField(GUITextField textField) {
		
		if (!this.textFieldsList.contains(textField)) this.textFieldsList.add(textField);
	}
    
	public List<GUIButton> getButtonsList() {
		
		return this.buttonsList;
	}
	
	/**
	 * Добавляет объект GUIButton в фреймворк.
	 * 
	 * @param button
	 */
	public void addButton(GUIButton button) {
		
		if (!this.buttonsList.contains(button)) this.buttonsList.add(button);
	}
	
	public List<GUIButtonPanel> getButtonPanelsList() {
		
		return this.buttonPanelsList;
	}
	
	/**
	 * Добавляет объект GUIButtonPanel для GUIButton в фреймворк.
	 * 
	 * @param buttonPanel
	 */
	public void addButtonPanel(GUIButtonPanel buttonPanel) {
		
		if (!this.buttonPanelsList.contains(buttonPanel)) this.buttonPanelsList.add(buttonPanel);
	}
}
