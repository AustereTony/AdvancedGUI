package ru.austeretony.advancedgui.inventory;

import libs.austeretony.advancedgui.container.contextmenu.GUIContextMenu;
import libs.austeretony.advancedgui.container.contextmenu.GUIPresetAction;
import libs.austeretony.advancedgui.container.framework.GUIContainerFramework;
import libs.austeretony.advancedgui.container.framework.GUIContainerFramework.GUIEnumPosition;
import libs.austeretony.advancedgui.container.framework.GUIContainerSlots;
import libs.austeretony.advancedgui.screen.browsing.GUIScroller;
import libs.austeretony.advancedgui.screen.button.GUIButton;
import libs.austeretony.advancedgui.screen.button.GUISlider;
import libs.austeretony.advancedgui.screen.button.GUISound;
import libs.austeretony.advancedgui.screen.core.GUISection;
import libs.austeretony.advancedgui.screen.framework.GUIFramework;
import libs.austeretony.advancedgui.screen.panel.GUIButtonPanel;
import libs.austeretony.advancedgui.screen.panel.GUIButtonPanel.GUIEnumOrientation;
import libs.austeretony.advancedgui.screen.text.GUITextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import ru.austeretony.advancedgui.inventory.contextactions.ActionStowStack;
import ru.austeretony.advancedgui.inventory.contextactions.ActionWithdrawStack;
import ru.austeretony.advancedgui.main.AdvancedGUIMain;
import ru.austeretony.advancedgui.main.SoundHandler;
import ru.austeretony.advancedgui.player.inventory.GUIInventory;

public class SectionStorage extends GUISection {
	
	private static final ResourceLocation TEXTURE_STORAGE = new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/inventory_storage.png");
	
	private final GUIStorage screen;//Ссылка на ГПИ хранилища для взаимодействия с другими элементами (и разделами).
	
	protected GUIContainerFramework storageFramework, inventoryFramework;		
				
	protected GUIButtonPanel storageSortersPanel, inventorySortersPanel;
	    	
	protected GUIButton 
    inventoryBaseSorter = new GUIButton(), 
    inventoryWeaponSorter = new GUIButton(), 
    inventoryArmorSorter = new GUIButton(), 
    inventoryToolsSorter = new GUIButton(), 
    inventoryConsumablesSorter = new GUIButton(), 
    inventoryMaterialsSorter = new GUIButton(),
    
    storageBaseSorter = new GUIButton(), 
    storageWeaponSorter = new GUIButton(), 
    storageArmorSorter = new GUIButton(), 
    storageToolsSorter = new GUIButton(), 
    storageConsumablesSorter = new GUIButton(), 
    storageMaterialsSorter = new GUIButton();
    
    public static final GUIPresetAction
    STOW = new ActionStowStack(I18n.format("contextAction.stow")),
    WITHDRAW = new ActionWithdrawStack(I18n.format("contextAction.withdraw"));

	public SectionStorage(GUIStorage screen) {
		
		super(screen);
		
		this.screen = screen;
		
		this.setTexture(this.TEXTURE_STORAGE, 384, 256);//Текстура у каждого раздела может быть своя.
    }
	
	@Override
	public void init() {
		
		this.addButton(this.screen.switchSectionButton);//Добавление кнопки, объявленной и инициализированной в классе GUIStorage.
		
    	this.initStorage();
    	this.initInventory();
	}
	   
    private void initStorage() {
    	 
    	this.storageFramework = new GUIContainerFramework(GUIEnumPosition.CUSTOM, this.screen.storageInventory, 0, 349, 5, 7).setPosition(90, 112).enableSlotBottomLayer();
    	    	
    	this.storageFramework.slots.initScroller(new GUIScroller(this.storageFramework).initSlider(new GUISlider(216, 112, 4, 88).enableBackground()));
    		    	
    	this.storageFramework.slots.initSearchField(new GUITextField(89, 102, 80, 15).setScale(0.75F).setDisplayText(I18n.format("searchField.search")).enableBackground().setEnabledBackgroundColor(0x8C252525).setHoveredBackgroundColor(0x8C505050));
    
    	this.storageSortersPanel = new GUIButtonPanel(GUIEnumOrientation.VERTICAL, 73, 112, 15, 15).enableBackground().setEnabledBackgroundColor(0x8C202020);
    	    	
    	this.storageSortersPanel.addButton(this.storageBaseSorter.setSound(new GUISound(SoundHandler.CLICK, 1.0F, 1.0F)).setToggledAtStart(true).setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/main.png"), 15, 15).setPopupText(I18n.format("guiButton.mainSection")));
    	this.storageSortersPanel.addButton(this.storageWeaponSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/weapon.png"), 15, 15).setPopupText(I18n.format("guiButton.weaponSection")));
    	this.storageSortersPanel.addButton(this.storageArmorSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/armor.png"), 15, 15).setPopupText(I18n.format("guiButton.armorSection")));
    	this.storageSortersPanel.addButton(this.storageToolsSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/tools.png"), 15, 15).setPopupText(I18n.format("guiButton.toolsSection")));
    	this.storageSortersPanel.addButton(this.storageConsumablesSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/consumables.png"), 15, 15).setPopupText(I18n.format("guiButton.consumablesSection")));
    	this.storageSortersPanel.addButton(this.storageMaterialsSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/materials.png"), 15, 15).setPopupText(I18n.format("guiButton.materialsSection")));
    	
    	this.storageFramework.addButtonPanel(this.storageSortersPanel);
    	    	
    	this.storageFramework.slots.initContextMenu(new GUIContextMenu().addAction(this.WITHDRAW).addAction(GUIInventory.DESTROY).addAction(GUIInventory.SPLIT));
    	
    	this.addFramework(this.storageFramework);
    	
    	this.screen.updateFramework(this.storageFramework, GUIContainerSlots.BASE_SORTER);
    }
    
    private void initInventory() {
    	 
    	this.inventoryFramework = new GUIContainerFramework(GUIEnumPosition.CUSTOM, this.screen.playerInventory, 350, 499, 10, 5).setPosition(261, 51).enableSlotBottomLayer();
    	    	
    	this.inventoryFramework.slots.initScroller(new GUIScroller(30, 10).initSlider(new GUISlider(351, 50, 4, 180).enableBackground()));
        		    	
    	this.inventoryFramework.slots.initSearchField(new GUITextField(260, 25, 80, 15).setScale(0.75F).setDisplayText(I18n.format("searchField.search")).enableBackground().setEnabledBackgroundColor(0x8C252525).setHoveredBackgroundColor(0x8C505050));

    	this.inventorySortersPanel = new GUIButtonPanel(GUIEnumOrientation.HORIZONTAL, 260, 33, 15, 15).enableBackground().setEnabledBackgroundColor(0x8C202020);
    	
    	this.inventorySortersPanel.addButton(this.inventoryBaseSorter.setSound(new GUISound(SoundHandler.CLICK, 1.0F, 1.0F)).setToggledAtStart(true).setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/main.png"), 15, 15).setPopupText(I18n.format("guiButton.mainSection")));
    	this.inventorySortersPanel.addButton(this.inventoryWeaponSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/weapon.png"), 15, 15).setPopupText(I18n.format("guiButton.weaponSection")));
    	this.inventorySortersPanel.addButton(this.inventoryArmorSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/armor.png"), 15, 15).setPopupText(I18n.format("guiButton.armorSection")));
    	this.inventorySortersPanel.addButton(this.inventoryToolsSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/tools.png"), 15, 15).setPopupText(I18n.format("guiButton.toolsSection")));
    	this.inventorySortersPanel.addButton(this.inventoryConsumablesSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/consumables.png"), 15, 15).setPopupText(I18n.format("guiButton.consumablesSection")));
    	this.inventorySortersPanel.addButton(this.inventoryMaterialsSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/materials.png"), 15, 15).setPopupText(I18n.format("guiButton.materialsSection")));
    	
    	this.inventoryFramework.addButtonPanel(this.inventorySortersPanel);
    	
    	this.inventoryFramework.slots.initContextMenu(new GUIContextMenu().addAction(this.STOW).addAction(GUIInventory.DESTROY).addAction(GUIInventory.SPLIT));
    	
    	this.addFramework(this.inventoryFramework);
    	
    	this.screen.updateFramework(this.inventoryFramework, GUIContainerSlots.BASE_SORTER);
    }
	
    //Вызывается при клике по кнопке.
	@Override
	public void handleButtonPress(GUISection section, GUIFramework framework, GUIButton button) {
		
		if (section == this) {
			
			if (button == this.screen.switchSectionButton) {
				
				this.screen.workspace.setCurrentSection(this.screen.testSection);
			}
		}
		
    	if (framework == this.inventoryFramework) {
    		
        	if (this.inventoryBaseSorter.setToggled(button == this.inventoryBaseSorter)) {
        		
        		this.screen.updateFramework(this.inventoryFramework, GUIContainerSlots.BASE_SORTER);
        	}
        	
        	else if (this.inventoryWeaponSorter.setToggled(button == this.inventoryWeaponSorter)) {
        		
        		this.screen.updateFramework(this.inventoryFramework, GUIInventory.WEAPON_SORTER);
        	}    	
        	
        	else if (this.inventoryArmorSorter.setToggled(button == this.inventoryArmorSorter)) {
        		
        		this.screen.updateFramework(this.inventoryFramework, GUIInventory.ARMOR_SORTER);
        	}
        	
        	else if (this.inventoryToolsSorter.setToggled(button == this.inventoryToolsSorter)) {
        		
        		this.screen.updateFramework(this.inventoryFramework, GUIInventory.TOOLS_SORTER);
        	}
        	
        	else if (this.inventoryConsumablesSorter.setToggled(button == this.inventoryConsumablesSorter)) {
        		
        		this.screen.updateFramework(this.inventoryFramework, GUIInventory.CONSUMABLES_SORTER);
        	}
        	
        	else if (this.inventoryMaterialsSorter.setToggled(button == this.inventoryMaterialsSorter)) {
        		
        		this.screen.updateFramework(this.inventoryFramework, GUIInventory.MATERIALS_SORTER);
        	}
    	}
    	
    	else if (framework == this.storageFramework) {
    		
        	if (this.storageBaseSorter.setToggled(button == this.storageBaseSorter)) {
        		
        		this.screen.updateFramework(this.storageFramework, GUIContainerSlots.BASE_SORTER);
        	}
        	  	
        	else if (this.storageWeaponSorter.setToggled(button == this.storageWeaponSorter)) {
        		
        		this.screen.updateFramework(this.storageFramework, GUIInventory.WEAPON_SORTER);
        	}    	
        	
        	else if (this.storageArmorSorter.setToggled(button == this.storageArmorSorter)) {
        		
        		this.screen.updateFramework(this.storageFramework, GUIInventory.ARMOR_SORTER);
        	}
        	
        	else if (this.storageToolsSorter.setToggled(button == this.storageToolsSorter)) {
        		
        		this.screen.updateFramework(this.storageFramework, GUIInventory.TOOLS_SORTER);
        	}
        	
        	else if (this.storageConsumablesSorter.setToggled(button == this.storageConsumablesSorter)) {
        		
        		this.screen.updateFramework(this.storageFramework, GUIInventory.CONSUMABLES_SORTER);
        	}
        	
        	else if (this.storageMaterialsSorter.setToggled(button == this.storageMaterialsSorter)) {
        		
        		this.screen.updateFramework(this.storageFramework, GUIInventory.MATERIALS_SORTER);
        	}
    	}
	}
}
