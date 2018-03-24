package libs.austeretony.advancedgui.screen.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import libs.austeretony.advancedgui.screen.button.GUIButton;
import libs.austeretony.advancedgui.screen.framework.GUIFramework;
import libs.austeretony.advancedgui.screen.panel.GUIButtonPanel;
import libs.austeretony.advancedgui.screen.text.GUITextField;
import libs.austeretony.advancedgui.screen.text.GUITextLabel;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Класс для настройки рабочей области ГПИ.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUIWorkspace extends GUIAdvancedElement<GUIWorkspace> {
	
	public final AdvancedGUIScreen screen;
	
	private boolean updateOnSectionChange;
	
    private final List<GUISection> sectionsList = new ArrayList<GUISection>();
	
	private GUISection currentSection;
	
	/**
	 * Создаёт рабочее пространство для ГПИ.
	 * По умолчанию рабочая зона размещается по центру экрана, используйте 
	 * {@link GUIWorkspace#setAlignment(GUIEnumAlignment, int, int)}
	 * для настройки положения.
	 * 
	 * @param screen AdvancedGUIScreen для которого создаётся пространство
	 * @param width ширина рабочей зоны
	 * @param height высота рабочей зоны
	 * @param mainSection стартовый раздел ГПИ
	 */
	public GUIWorkspace(AdvancedGUIScreen screen, int width, int height) {
		
    	this.screen = screen;
		
		this.setSize(width, height);
		
		this.setPosition((screen.width - width) / 2, (screen.height - height) / 2);
	}
		
	@Override
    public void draw() {
    	
    	if (this.isDebugMode()) {
    		
    		this.drawRect(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), this.getDebugColor());
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
	
	public List<GUISection> getSections() {
		
		return this.sectionsList;
	}
	
	public GUISection getCurrentSection() {
		
		return this.currentSection;
	}
	
    /**
     * Создаёт внутренний раздел ГПИ для этого рабочего пространства.
     *
     * @return новый раздел
     */
    public GUISection createSection() {
    	
    	GUISection section = new GUIBaseSection(this.screen);   	
    	
    	this.getSections().add(section);
    	
    	section.setPosition(this.getX(), this.getY());
    	section.setSize(this.getWidth(), this.getHeight());
    	
    	return section;
    }
    
    /**
     * Добавляет внешний раздел в ГПИ.
     * 
     * @param section
     * 
     * @return добавляемый раздел
     */
    public GUISection initSection(GUISection section) {
    	
    	this.getSections().add(section);

    	section.setPosition(this.getX(), this.getY());
    	section.setSize(this.getWidth(), this.getHeight());
    	
    	return section;
    }
	
	/**
	 * Устанавливает активный раздел ГПИ. 
	 * 
	 * @param section
	 */
	public void setCurrentSection(GUISection section) {
		
		this.currentSection = section;
	}
	
	//TODO Придумать менее костыльный способ обновления.
	/**
	 * Пересоздаёт содержимое ВСЕХ разделов ГПИ при 
	 * установленом {@link GUIWorkspace#allowUpdate()}
	 */
	public void updateSections() {
		
		if (this.isUpdateAllowed()) {
						
			Iterator 
			sectionsIterator = this.sectionsList.iterator(),
			textLabelsIterator, textFieldsIterator, buttonsIterator, buttonPanelsIterator, frameworksIterator;
			
			GUISection section;
			
			GUIFramework framework;
			
			GUITextLabel textLabel;
			
			GUITextField textField;
			
			GUIButton buton; 
			
			GUIButtonPanel butonPanel;			
			
			while (sectionsIterator.hasNext()) {
				
				section = (GUISection) sectionsIterator.next();
				
				textLabelsIterator = section.getTextLabelsList().iterator();
				
				while (textLabelsIterator.hasNext()) {
					
					textLabel = (GUITextLabel) textLabelsIterator.next();
					
					textLabelsIterator.remove();
				}	
				
				textFieldsIterator = section.getTextFieldsList().iterator();
				
				while (textFieldsIterator.hasNext()) {
					
					textField = (GUITextField) textFieldsIterator.next();
					
					textFieldsIterator.remove();
				}
				
				buttonsIterator = section.getButtonsList().iterator();
				
				while (buttonsIterator.hasNext()) {
					
					buton = (GUIButton) buttonsIterator.next();
					
					buttonsIterator.remove();
				}	
				
				buttonPanelsIterator = section.getButtonPanelsList().iterator();
				
				while (buttonPanelsIterator.hasNext()) {
					
					butonPanel = (GUIButtonPanel) buttonPanelsIterator.next();
					
					buttonPanelsIterator.remove();
				}
				
				frameworksIterator = section.frameworks.iterator();
				
				while (frameworksIterator.hasNext()) {
										
					framework = (GUIFramework) frameworksIterator.next();						
					
					textFieldsIterator = framework.getTextFieldsList().iterator();
					
					while (textFieldsIterator.hasNext()) {
						
						textField = (GUITextField) textFieldsIterator.next();
						
						textFieldsIterator.remove();
					}
							
					buttonsIterator = framework.getButtonsList().iterator();
					
					while (buttonsIterator.hasNext()) {
						
						buton = (GUIButton) buttonsIterator.next();
						
						buttonsIterator.remove();
					}	
					
					buttonPanelsIterator = framework.getButtonPanelsList().iterator();
					
					while (buttonPanelsIterator.hasNext()) {
						
						butonPanel = (GUIButtonPanel) buttonPanelsIterator.next();
						
						buttonPanelsIterator.remove();
					}
					
					frameworksIterator.remove();
				}
			}

			this.screen.initSections();			
		}
	}
	
	/**
	 * Метод для юстировки, позволяет сдвинуть рабочую область в определённом направлении, 
	 * при этом сдвигаются все элементы на экране.
	 * 
	 * @param alignment положение
	 * @param xOffset отступ по горизонтали
	 * @param yOffset отступ по вертикали
	 */
	public GUIWorkspace setAlignment(GUIEnumAlignment alignment, int xOffset, int yOffset) {
		
		int 
		xOffsetCalc = alignment == GUIEnumAlignment.LEFT || alignment == GUIEnumAlignment.BOTTOM_LEFT || alignment == GUIEnumAlignment.TOP_LEFT ? - xOffset : (alignment == GUIEnumAlignment.RIGHT || alignment == GUIEnumAlignment.BOTTOM_RIGHT || alignment == GUIEnumAlignment.TOP_RIGHT ? xOffset : 0),
		yOffsetCalc = alignment == GUIEnumAlignment.TOP || alignment == GUIEnumAlignment.TOP_LEFT || alignment == GUIEnumAlignment.TOP_RIGHT ? - yOffset : (alignment == GUIEnumAlignment.BOTTOM || alignment == GUIEnumAlignment.BOTTOM_LEFT || alignment == GUIEnumAlignment.BOTTOM_RIGHT ? yOffset : 0);
		
		this.setPosition((this.screen.width - this.getWidth()) / 2 + xOffsetCalc, (this.screen.height - this.getHeight()) / 2 + yOffsetCalc);
		
		return this;
	}
	
	public boolean isUpdateAllowed() {
		
		return this.updateOnSectionChange;
	}
	
	/**
	 * Позволяет пересоздавать содержимое ВСЕХ разделов вызовом {@link GUIWorkspace#updateSections()}.
	 * Используйте только в случае крайней необходимости 
	 * (если требуется отобразить изменения, произведённые извне).
	 * 
	 * @return
	 */
	public GUIWorkspace allowUpdate() {
		
		this.updateOnSectionChange = true;
		
		return this;
	}
    
    /**
     * Enum для определения положения рабочего пространства.
     */
	@SideOnly(Side.CLIENT)
    public enum GUIEnumAlignment {
    	
    	LEFT,
    	BOTTOM_LEFT,
    	TOP_LEFT,
    	RIGHT,
    	BOTTOM_RIGHT,
    	TOP_RIGHT,
    	TOP,
    	BOTTOM,
    }   
}
