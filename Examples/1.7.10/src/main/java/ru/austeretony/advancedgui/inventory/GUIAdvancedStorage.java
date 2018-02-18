package ru.austeretony.advancedgui.inventory;

import libs.austeretony.advancedgui.guicontainer.browsing.GUIScroller;
import libs.austeretony.advancedgui.guicontainer.button.GUIButton;
import libs.austeretony.advancedgui.guicontainer.button.GUISlider;
import libs.austeretony.advancedgui.guicontainer.contextmenu.GUIContextAction;
import libs.austeretony.advancedgui.guicontainer.contextmenu.GUIContextMenu;
import libs.austeretony.advancedgui.guicontainer.contextmenu.GUIPresetAction;
import libs.austeretony.advancedgui.guicontainer.core.AdvancedGUIContainer;
import libs.austeretony.advancedgui.guicontainer.core.GUIContainerWorkspace;
import libs.austeretony.advancedgui.guicontainer.core.GUIContainerWorkspace.GUIAlignment;
import libs.austeretony.advancedgui.guicontainer.framework.GUIFramework;
import libs.austeretony.advancedgui.guicontainer.framework.GUIFramework.GUIPosition;
import libs.austeretony.advancedgui.guicontainer.panel.GUIButtonPanel;
import libs.austeretony.advancedgui.guicontainer.panel.GUIButtonPanel.GUIOrientation;
import libs.austeretony.advancedgui.guicontainer.text.GUISearchField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import ru.austeretony.advancedgui.inventory.contextactions.ActionStowStack;
import ru.austeretony.advancedgui.inventory.contextactions.ActionWithdrawStack;
import ru.austeretony.advancedgui.main.AdvancedGUIMain;
import ru.austeretony.advancedgui.player.inventory.GUIInventoryFirst;
import ru.austeretony.advancedgui.player.inventory.PlayerInventory;
import ru.austeretony.advancedgui.tiles.TileEntityAdvancedStorage;

public class GUIAdvancedStorage extends AdvancedGUIContainer {
	
	private static final ResourceLocation TEXTURE_INVENTORY = new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/storage.png");
	
	private final EntityPlayer player;
	
	private final PlayerInventory playerInventory;
	
    private GUIContainerWorkspace containerWorkspace;
	
	private final TileEntityAdvancedStorage storageInventory;
		
	private GUIFramework inventoryFramework, storageFramework;		
	
	private GUIScroller inventoryScroller, storageScroller;
	
	private GUISlider inventorySlider, storageSlider;
	
	private GUISearchField inventorySearchField, storageSearchField;
	
	private GUIButtonPanel inventorySortersPanel, storageSortersPanel;
    
    private GUIButton 
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
    STOW = new ActionStowStack("contextAction.stow"),//выложить предмет из инвентаря в хранилище.
    WITHDRAW = new ActionWithdrawStack("contextAction.withdraw");//достать предмет из хранилища.
    
    private GUIContextMenu inventoryContextMenu, storageContextMenu;
      
	public GUIAdvancedStorage(EntityPlayer player, PlayerInventory playerInventory, TileEntityAdvancedStorage storageInventory) {
		
		super(new ContainerAdvancedStorage(player, playerInventory, storageInventory));
		
		this.player = player;
		
		this.playerInventory = playerInventory;		
		this.storageInventory = storageInventory;
	}
	
	//Здесь инициализируется GUIContentPanel, определяющая рабочее пространство (размер и положение).
    protected GUIContainerWorkspace initWorkspace() {
    	
    	/*
    	 * Создание и настройка рабочего пространста ГПИ.
    	 */
        this.containerWorkspace = new GUIContainerWorkspace(this, 384, 256, GUIAlignment.RIGHT, 42, 0);
        
        //this.contentPanel.enableDebugMode();//Заливка для отображения границ.
        
        this.containerWorkspace.setTexture(this.TEXTURE_INVENTORY, 384, 256);//Установка текстуры ГПИ.
        
        return this.containerWorkspace;
    }
    
    protected void initFrameworks() {
    	
    	this.initInventory();
    	this.initStorage();
    }
    
    private void initInventory() {
    	
    	this.inventoryFramework = new GUIFramework(GUIPosition.CUSTOM, this.playerInventory, 0, 149, 10, 5);
    	
    	this.inventoryFramework.setPosition(261, 50);
    	
    	this.inventoryScroller = new GUIScroller(30, 10);//Вторая версия конструктора скроллера.
    	
    	this.inventoryFramework.initScroller(this.inventoryScroller);
    	
    	this.inventorySlider = new GUISlider(this.inventoryScroller, 351, 50, 6, 180, 1.0F);
    	
    	this.inventorySlider.setBackgroundColor(0x8C555555);
    	this.inventorySlider.setSlidebarColor(0x8CA5A5A5);
    	this.inventorySlider.setHoveredSlidebarColor(0x8CBFBFBF);
    	
    	this.inventoryFramework.initSlider(this.inventorySlider);
    	
    	this.inventorySearchField = new GUISearchField(260, 21, 80, 15, 1.0F);
    	
    	this.inventorySearchField.setBackgroundColor(0x8C252525);
    	this.inventorySearchField.setFocusedBackgroundColor(0x8C505050);
    	
    	this.inventorySearchField.setDefaultText("searchField.search");
    	
    	this.inventoryFramework.initSearchField(this.inventorySearchField);
    	
    	this.inventorySortersPanel = new GUIButtonPanel(GUIOrientation.HORIZONTAL, 260, 32, 15, 15, 1.0F);
    	
    	this.inventorySortersPanel.setBackgroundEnabled(true);
    	
    	this.inventorySortersPanel.setBackgroundColor(0x8C202020);
    	
    	this.inventoryBaseSorter.setToggled(true);
    	
    	this.inventoryBaseSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/main.png"), 15, 15);
    	this.inventoryWeaponSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/weapon.png"), 15, 15);
    	this.inventoryArmorSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/armor.png"), 15, 15);
    	this.inventoryToolsSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/tools.png"), 15, 15);
    	this.inventoryConsumablesSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/consumables.png"), 15, 15);
    	this.inventoryMaterialsSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/materials.png"), 15, 15);
    	
    	this.inventoryBaseSorter.setPopupText("guiButton.mainSection");
    	this.inventoryWeaponSorter.setPopupText("guiButton.weaponSection");
    	this.inventoryArmorSorter.setPopupText("guiButton.armorSection");
    	this.inventoryToolsSorter.setPopupText("guiButton.toolsSection");
    	this.inventoryConsumablesSorter.setPopupText("guiButton.consumablesSection");
    	this.inventoryMaterialsSorter.setPopupText("guiButton.materialsSection");
    	
    	this.inventorySortersPanel.addButton(this.inventoryBaseSorter);
    	this.inventorySortersPanel.addButton(this.inventoryWeaponSorter);
    	this.inventorySortersPanel.addButton(this.inventoryArmorSorter);
    	this.inventorySortersPanel.addButton(this.inventoryToolsSorter);
    	this.inventorySortersPanel.addButton(this.inventoryConsumablesSorter);
    	this.inventorySortersPanel.addButton(this.inventoryMaterialsSorter);
    	
    	this.inventoryFramework.addButtonPanel(this.inventorySortersPanel);
    	
    	this.inventoryContextMenu = new GUIContextMenu(1.0F);
    	
    	this.inventoryContextMenu.addAction(new GUIContextAction(this.STOW));
    	this.inventoryContextMenu.addAction(new GUIContextAction(GUIInventoryFirst.DESTROY));
    	this.inventoryContextMenu.addAction(new GUIContextAction(GUIInventoryFirst.SPLIT));
    	
    	this.inventoryFramework.initContextMenu(this.inventoryContextMenu);
    	
    	this.addFramework(this.inventoryFramework);
    	
    	this.updateFramework(this.inventoryFramework, GUIFramework.BASE_SORTER);
    }
    
    private void initStorage() {
    	
    	this.storageFramework = new GUIFramework(GUIPosition.CUSTOM, this.storageInventory, 150, 499, 5, 7);
    	
    	this.storageFramework.setPosition(90, 112);
    	
    	this.storageScroller = new GUIScroller(50, 5);
    	
    	this.storageFramework.initScroller(this.storageScroller);
    	
    	this.storageSlider = new GUISlider(this.storageScroller, 216, 112, 6, 90, 1.0F);
    	
    	this.storageSlider.setBackgroundColor(0x8C555555);//Изменение цветов на прозрачные.
    	this.storageSlider.setSlidebarColor(0x8CA5A5A5);
    	this.storageSlider.setHoveredSlidebarColor(0x8CBFBFBF);
    	
    	this.storageFramework.initSlider(this.storageSlider);
    	
    	this.storageSearchField = new GUISearchField(89, 100, 80, 15, 1.0F);
    	
    	this.storageSearchField.setBackgroundColor(0x8C252525);//Изменение цветов на прозрачные.
    	this.storageSearchField.setFocusedBackgroundColor(0x8C505050);
    	
    	this.storageSearchField.setDefaultText("searchField.search");
    	
    	this.storageFramework.initSearchField(this.storageSearchField);
    	
    	this.storageSortersPanel = new GUIButtonPanel(GUIOrientation.VERTICAL, 73, 112, 15, 15, 1.0F);
    	
    	this.storageSortersPanel.setBackgroundEnabled(true);
    	
    	this.storageSortersPanel.setBackgroundColor(0x8C202020);
    	
    	this.storageBaseSorter.setToggled(true);
    	
    	this.storageBaseSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/main.png"), 15, 15);
    	this.storageWeaponSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/weapon.png"), 15, 15);
    	this.storageArmorSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/armor.png"), 15, 15);
    	this.storageToolsSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/tools.png"), 15, 15);
    	this.storageConsumablesSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/consumables.png"), 15, 15);
    	this.storageMaterialsSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/materials.png"), 15, 15);
    	
    	this.storageBaseSorter.setPopupText("guiButton.mainSection");
    	this.storageWeaponSorter.setPopupText("guiButton.weaponSection");
    	this.storageArmorSorter.setPopupText("guiButton.armorSection");
    	this.storageToolsSorter.setPopupText("guiButton.toolsSection");
    	this.storageConsumablesSorter.setPopupText("guiButton.consumablesSection");
    	this.storageMaterialsSorter.setPopupText("guiButton.materialsSection");
    	
    	this.storageSortersPanel.addButton(this.storageBaseSorter);
    	this.storageSortersPanel.addButton(this.storageWeaponSorter);
    	this.storageSortersPanel.addButton(this.storageArmorSorter);
    	this.storageSortersPanel.addButton(this.storageToolsSorter);
    	this.storageSortersPanel.addButton(this.storageConsumablesSorter);
    	this.storageSortersPanel.addButton(this.storageMaterialsSorter);
    	
    	this.storageFramework.addButtonPanel(this.storageSortersPanel);
    	
    	this.storageContextMenu = new GUIContextMenu(1.0F);
    	
    	this.storageContextMenu.addAction(new GUIContextAction(this.WITHDRAW));
    	this.storageContextMenu.addAction(new GUIContextAction(GUIInventoryFirst.DESTROY));
    	this.storageContextMenu.addAction(new GUIContextAction(GUIInventoryFirst.SPLIT));
    	
    	this.storageFramework.initContextMenu(this.storageContextMenu);
    	
    	this.addFramework(this.storageFramework);
    	
    	this.updateFramework(this.storageFramework, GUIFramework.BASE_SORTER);
    }
    
    protected void handleButtonPress(GUIFramework framework, GUIButton button) {
    	
    	if (framework == this.inventoryFramework) {
    		
    		if (button == this.inventoryBaseSorter) {
    		
    			this.updateFramework(this.inventoryFramework, GUIFramework.BASE_SORTER);
    		
    			this.inventoryBaseSorter.setToggled(true);
    		}
    	
    		else {
    		
    			this.inventoryBaseSorter.setToggled(false);
    		}
    	
    		if (button == this.inventoryWeaponSorter) {
    		
    			this.updateFramework(this.inventoryFramework, GUIInventoryFirst.WEAPON_SORTER);
    		
    			this.inventoryWeaponSorter.setToggled(true);
    		}
    	
    		else {
    		
    			this.inventoryWeaponSorter.setToggled(false);
    		}
    	
    		if (button == this.inventoryArmorSorter) {
    		
    			this.updateFramework(this.inventoryFramework, GUIInventoryFirst.ARMOR_SORTER);
    		
    			this.inventoryArmorSorter.setToggled(true);
    		}
    	
    		else {
    		
    			this.inventoryArmorSorter.setToggled(false);
    		}
    	
    		if (button == this.inventoryToolsSorter) {
    		
    			this.updateFramework(this.inventoryFramework, GUIInventoryFirst.TOOLS_SORTER);
    		
    			this.inventoryToolsSorter.setToggled(true);
    		}
    	
    		else {
    		
    			this.inventoryToolsSorter.setToggled(false);
    		}
    	
    		if (button == this.inventoryConsumablesSorter) {
    		
    			this.updateFramework(this.inventoryFramework, GUIInventoryFirst.CONSUMABLES_SORTER);
    		
    			this.inventoryConsumablesSorter.setToggled(true);
    		}
    	
    		else {
    		
    			this.inventoryConsumablesSorter.setToggled(false);
    		}
    	
    		if (button == this.inventoryMaterialsSorter) {
    		
    			this.updateFramework(this.inventoryFramework, GUIInventoryFirst.MATERIALS_SORTER);
    		
    			this.inventoryMaterialsSorter.setToggled(true);
    		}
    	
    		else {
    		
    			this.inventoryMaterialsSorter.setToggled(false);
    		}
    	}
    	
    	if (framework == this.storageFramework) {
    		
    		if (button == this.storageBaseSorter) {
    		
    			this.updateFramework(this.storageFramework, GUIFramework.BASE_SORTER);
    			
    			this.storageBaseSorter.setToggled(true);
    		}
    	
    		else {
    		
    			this.storageBaseSorter.setToggled(false);
    		}
    	
    		if (button == this.storageWeaponSorter) {
    		
    			this.updateFramework(this.storageFramework, GUIInventoryFirst.WEAPON_SORTER);
    		
    			this.storageWeaponSorter.setToggled(true);
    		}
    	
    		else {
    		
    			this.storageWeaponSorter.setToggled(false);
    		}
    	
    		if (button == this.storageArmorSorter) {
    		
    			this.updateFramework(this.storageFramework, GUIInventoryFirst.ARMOR_SORTER);
    		
    			this.storageArmorSorter.setToggled(true);
    		}
    	
    		else {
    		
    			this.storageArmorSorter.setToggled(false);
    		}
    	
    		if (button == this.storageToolsSorter) {
    		
    			this.updateFramework(this.storageFramework, GUIInventoryFirst.TOOLS_SORTER);
    		
    			this.storageToolsSorter.setToggled(true);
    		}
    	
    		else {
    		
    			this.storageToolsSorter.setToggled(false);
    		}
    	
    		if (button == this.storageConsumablesSorter) {
    		
    			this.updateFramework(this.storageFramework, GUIInventoryFirst.CONSUMABLES_SORTER);
    		
    			this.storageConsumablesSorter.setToggled(true);
    		}
    	
    		else {
    		
    			this.storageConsumablesSorter.setToggled(false);
    		}
    	
    		if (button == this.storageMaterialsSorter) {
    		
    			this.updateFramework(this.storageFramework, GUIInventoryFirst.MATERIALS_SORTER);
    		
    			this.storageMaterialsSorter.setToggled(true);
    		}
    	
    		else {
    		
    			this.storageMaterialsSorter.setToggled(false);
    		}
    	}
    }
    
    public boolean doesGuiPauseGame() {
    	
        return false;
    }
    
    public void drawDefaultBackground() {}
}
