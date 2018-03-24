package ru.austeretony.advancedgui.inventory;

import libs.austeretony.advancedgui.container.core.AdvancedGUIContainer;
import libs.austeretony.advancedgui.screen.button.GUIButton;
import libs.austeretony.advancedgui.screen.core.GUISection;
import libs.austeretony.advancedgui.screen.core.GUIWorkspace;
import libs.austeretony.advancedgui.screen.core.GUIWorkspace.GUIEnumAlignment;
import libs.austeretony.advancedgui.screen.framework.GUIFramework;
import libs.austeretony.advancedgui.screen.text.GUITextLabel;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import ru.austeretony.advancedgui.main.AdvancedGUIMain;
import ru.austeretony.advancedgui.player.inventory.InventoryExtended;
import ru.austeretony.advancedgui.tiles.TileEntityStorage;

public class GUIStorage extends AdvancedGUIContainer<GUIStorage> {
		
	protected final EntityPlayer player;
	
	protected final TileEntityStorage storageInventory;	
	protected final InventoryExtended playerInventory;
	
	protected GUIWorkspace workspace;
    
	protected GUISection mainSection, testSection;
	
	protected GUIButton switchSectionButton;
	
	protected GUITextLabel testSectionLabel;
      
	public GUIStorage(EntityPlayer player, InventoryExtended playerInventory, TileEntityStorage storageInventory) {
		
		super(new ContainerStorage(player, playerInventory, storageInventory));
		
		this.player = player;
		
		this.playerInventory = playerInventory;		
		this.storageInventory = storageInventory;
	}
	
	@Override
    protected GUIWorkspace initWorkspace() {
						    	                
        return this.workspace = new GUIWorkspace(this, 384, 256).setAlignment(GUIEnumAlignment.RIGHT, 42, 0);
    }
    
	@Override
    protected GUISection getDefaultSection() {		
    					
		//Все элементы этого ГПИ располагаются в классе раздела SectionStorage.
		return this.mainSection = this.workspace.initSection(new SectionStorage(this));   		
    }
    
	@Override
    protected void initSections() {
		
		this.switchSectionButton = new GUIButton(this.workspace.getWidth() - 16, 10, 16, 16).setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/section.png"), 16, 16);
		
		this.switchSectionButton.setPopupText(I18n.format("section.switch")).setPopupAlignment(- this.width(I18n.format("section.switch")) / 2, - 10);
		
		this.testSectionLabel = new GUITextLabel(this.workspace.getWidth() - 150, 14).setDisplayText(I18n.format("section.test"));		
		
		//Простой раздел, который будет настроен в классе ГПИ.
		this.testSection = this.workspace.createSection();
				
		this.testSection.addButton(this.switchSectionButton);
		
		this.testSection.addTextLabel(this.testSectionLabel);
	}
    
    @Override
    protected void handleButtonPress(GUISection section, GUIFramework framework, GUIButton button) {
    	
		if (section == this.testSection) {
			
			if (button == this.switchSectionButton) {
				
				this.workspace.setCurrentSection(this.mainSection);
			}
		}
    }
    
    @Override
    public boolean doesGuiPauseGame() {
    	
        return false;
    }
    
    @Override
    public void drawDefaultBackground() {
    	
        this.drawGradientRect(0, this.workspace.getY(), this.width, this.workspace.getY() + 20, 0, 0x64000000);       
    	
        this.drawRect(0, this.workspace.getY() + 20, this.width, this.workspace.getY() + this.workspace.getHeight() - 20, 0x64000000);        
    
        this.drawGradientRect(0, this.workspace.getY() + this.workspace.getHeight() - 20, this.width, this.workspace.getY() + this.workspace.getHeight(), 0x64000000, 0); 
    }
}