package libs.austeretony.advancedgui.screen.framework;

import java.util.ArrayList;
import java.util.List;

import libs.austeretony.advancedgui.screen.button.GUIButton;
import libs.austeretony.advancedgui.screen.panel.GUIButtonPanel;

/**
 * Объект-каркас для работы с новыми элементами ГПИ.
 * 
 * @author AustereTony
 */
public class GUIFramework {

    private List<GUIButton> buttonsList = new ArrayList<GUIButton>();
    
    private List<GUIButtonPanel> buttonPanelsList = new ArrayList<GUIButtonPanel>();
    
    public GUIFramework() {}
        
	/**
	 * Добавляет объект GUIButton в фреймворк.
	 * 
	 * @param button
	 */
	public void addButton(GUIButton button) {
		
		if (!this.buttonsList.contains(button)) this.buttonsList.add(button);
	}
	
	public List<GUIButton> getButtonsList() {
		
		return this.buttonsList;
	}
	
	/**
	 * Добавляет объект GUIButtonPanel для GUIButton в фреймворк.
	 * 
	 * @param buttonPanel
	 */
	public void addButtonPanel(GUIButtonPanel buttonPanel) {
		
		if (!this.buttonPanelsList.contains(buttonPanel)) this.buttonPanelsList.add(buttonPanel);
	}
	
	public List<GUIButtonPanel> getButtonPanelsList() {
		
		return this.buttonPanelsList;
	}
}
