package ru.austeretony.advancedgui.inventory;

import libs.austeretony.advancedgui.container.browsing.GUIScroller;
import libs.austeretony.advancedgui.container.browsing.GUIScroller.GUIScrollerType;
import libs.austeretony.advancedgui.container.button.GUISlider;
import libs.austeretony.advancedgui.container.contextmenu.GUIContextMenu;
import libs.austeretony.advancedgui.container.contextmenu.GUIPresetAction;
import libs.austeretony.advancedgui.container.core.AdvancedGUIContainer;
import libs.austeretony.advancedgui.container.core.GUIContainerWorkspace;
import libs.austeretony.advancedgui.container.core.GUIContainerWorkspace.GUIAlignment;
import libs.austeretony.advancedgui.container.framework.GUIContainerFramework;
import libs.austeretony.advancedgui.container.framework.GUIContainerFramework.GUIPosition;
import libs.austeretony.advancedgui.container.text.GUISearchField;
import libs.austeretony.advancedgui.screen.button.GUIButton;
import libs.austeretony.advancedgui.screen.framework.GUIFramework;
import libs.austeretony.advancedgui.screen.panel.GUIButtonPanel;
import libs.austeretony.advancedgui.screen.panel.GUIButtonPanel.GUIOrientation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import ru.austeretony.advancedgui.inventory.contextactions.ActionStowStack;
import ru.austeretony.advancedgui.inventory.contextactions.ActionWithdrawStack;
import ru.austeretony.advancedgui.main.AdvancedGUIMain;
import ru.austeretony.advancedgui.player.inventory.GUIExtendedInventory;
import ru.austeretony.advancedgui.player.inventory.InventoryExtended;
import ru.austeretony.advancedgui.tiles.TileEntityStorage;

public class GUIStorage extends AdvancedGUIContainer {
	
	/*
	 * В данном примере ограничено расписано использование основных возможностей AdvancedGUI, 
	 * полностью расписывать использование библиотеки начну только после релиза, а пока не ленитесь 
	 * просматривать классы и разбираться самостоятельно, если хотите пользоваться ей по полной уже сейчас.
	 */
	
	private static final ResourceLocation TEXTURE_STORAGE = new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/storage.png");
	
	private final EntityPlayer player;
	
	//Инвентарь хранилища.
	private final TileEntityStorage storageInventory;
	
	//Новый инвентарь игрока.
	private final InventoryExtended playerInventory;
	
	/*
	 * Объявление настройщика рабочего пространства для ГПИ, который определяет размер, положение и позволяет задать текстуру.
	 * При изменении положения рабочего пространства все элементы GUIFramework будут перемещаться вместе с ним.
	 */
    private GUIContainerWorkspace storageWorkspace;
	
	/*
	 * Обьявление переменных GUIFramework, описывающих пространство слотов.
	 * В данном случае используются два объекта для слотов ванильного 
	 * инвентаря (броня+хотбар) и нового инвентаря (150 слотов для хранения)
	 * соответственно.
	 */
	private GUIContainerFramework storageFramework, inventoryFramework;		
	
	//Скроллер по ГПИ
	private GUIScroller storageScroller, inventoryScroller;
	
	//Слайдер для скроллера
	private GUISlider storageSlider, inventorySlider;
	
	//Поисковое поле для поика предметов в фреймворке
	private GUISearchField storageSearchField, inventorySearchField;
	
	//Панель для кнопок, используется для удобного размещения и позиционирования кнопок GUIButton
	private GUIButtonPanel storageSortersPanel, inventorySortersPanel;
    
	//Функционал кнопок реализуется в методе handleButtonPress()
	//Эти кнопки используются для сортировки инвентаря по секциям (броня, оружие и т.д.), будут добавлены на панель
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
    
    //Контекстные меню
    private GUIContextMenu storageContextMenu, inventoryContextMenu;
    
    //Контекстные действия для меню.
    public static final GUIPresetAction
    STOW = new ActionStowStack("contextAction.stow"),//выложить предмет из инвентаря в хранилище.
    WITHDRAW = new ActionWithdrawStack("contextAction.withdraw");//достать предмет из хранилища.
      
	public GUIStorage(EntityPlayer player, InventoryExtended playerInventory, TileEntityStorage storageInventory) {
		
		super(new ContainerStorage(player, playerInventory, storageInventory));
		
		this.player = player;
		
		this.playerInventory = playerInventory;		
		this.storageInventory = storageInventory;
	}
	
	//Здесь инициализируется GUIContainerWorkspace, определяющий рабочее пространство
    protected GUIContainerWorkspace initWorkspace() {
    	
    	/*
    	 * Создание и настройка рабочего пространста ГПИ.
    	 * 
    	 * Параметры:
    	 * 
    	 * 1 - AdvancedGUIScreen для извлечения и расчёта размеров
    	 * 2 - ширина рабочей области
    	 * 3 - высота рабочей области
    	 * 4 - параметр юстировки, определяющий смещение
    	 * 5 - смещение по горизонтали
    	 * 6 - смещение по вертикали
    	 */
        this.storageWorkspace = new GUIContainerWorkspace(this, 384, 256, GUIAlignment.RIGHT, 42, 0);
        
        //this.inventoryWorkspace.setDebugMode(true);//Тестовая заливка для наглядного отображения определения позиции и границ.
        
        //Установка текстуры ГПИ. Для текстуры так же может быть задано смещение.
        this.storageWorkspace.setTexture(this.TEXTURE_STORAGE, 384, 256);
        
        return this.storageWorkspace;
    }
    
    //Здесь регистрируются объекты GUIFramework.
    protected void initFrameworks() {
    	
    	//ВНИМАНИЕ! Инициализировать фреймы слотов нужно в том порядке, в котором их слоты добавлялись в Container'e.
    	this.initStorage();//Для удобства настройки разных зон созданы отдельные методы
    	this.initInventory();
    }
    
    //Добавление слотов (броня+хотбар) из оригинального инвентаря.
    private void initStorage() {
    	 
    	/*
    	 * Параметры:
    	 * 
    	 * 1 - позиция будет указана в ГПИ.
    	 * 2 - передача IInventory кастомного инвентаря-хранилища.
    	 * 3 - индекс первого слота (включительно) из последовательности, которую нужно добавить. 
    	 * Это индекс, соответствующий позиции слота в Container#inventorySlots.
    	 * 4 - индекс последнего слота (включительно). Добавлено 150 слотов.
    	 * 5 - кол-во слотов отображаемых по горизонтали.
    	 * 6 - кол-во слотов отображаемых по вертикали. В итоге 35 слотов будут отображены в области 5 * 7 (35 слотов видны, 315 скрыты).
    	 */   
    	this.storageFramework = new GUIContainerFramework(GUIPosition.CUSTOM, this.storageInventory, 0, 349, 5, 7);//Инициализация, использование координат из ГПИ.
    	
    	this.storageFramework.setPosition(90, 112);//Указание кастомной позиции фреймворка (x, y), оригинальные позиии слотов, заданные в Container будут игнорироваться.
    	
    	//Инициализация скроллера. Конструктор GUIScroller принимает объект GUIFramework, в котором должен функционировать и enum параметр, описывающий поведение. 
    	//Скроллер расчитает необходимые параметры и позволит сколлить содержимое инвентаря (в данном случае по всем 350 слотам).
    	this.storageScroller = new GUIScroller(GUIScrollerType.STANDARD, this.storageFramework);
    	
    	this.storageFramework.initScroller(this.storageScroller);//Инициализация скроллера для фреймворка инвентаря.
    	
    	/*
    	 * Инициализация слайдера для скроллера. Это графический элемент, отображающий прогресс скроллинга,
    	 * а так же обладающий обратной связью, что позволяет скроллить слоты перемещая слайдбар слайдера курсором.
    	 * 
    	 * Параметры:
    	 * 
    	 * 1 - скроллер, для которого создаётся слайдер.
    	 * 2 - позиция по x.
    	 * 3 - позиция по y.
    	 * 4 - ширина.
    	 * 5 - высота.
    	 * 6 - коэффициент скалирования, позволяет скалировать размер (используется glScalef). 1.0 - натуральная величина.
    	 */
    	this.storageSlider = new GUISlider(this.storageScroller, 216, 112, 4, 90, 1.0F);
    	
    	this.storageSlider.setBackgroundEnabled(false);//По умолчанию для отрисовки фона используется заливка стандартным цветом, отключение.
    	this.storageSlider.setTextureEnabled(true);//Разрешение на использование фоном скроллера текстуры.
    	
    	this.storageSlider.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/slider.png"));//Установка текстуры.
    	
    	this.storageSlider.setSlidebarBackgroundEnabled(false);//Отключение стандартной заливки слайдбара.
    	this.storageSlider.setSlidebarTextureEnabled(true);//Разрешение на использование текстуры.
    	    	
    	this.storageSlider.setSlidebarTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/slidebar.png"));//Установка текстуры.
    	
    	//Заливку можно использовать совместно с текстурой, но необходимо задать полупрозрачные цвета вместо стандартных.
    	
    	this.storageFramework.initSlider(this.storageSlider);//Инициализация слайдера в фреймворке.
    	
    	/*
    	 * Инициализация поискового поля. Поле позволяет искать предметы по названию в слотах.
    	 * 
    	 * Параметры:
    	 * 
    	 * 1 - позиция по x.
    	 * 2 - позиция по y.
    	 * 3 - ширина.
    	 * 4 - высота.
    	 * 5 - коэффициент скалирования.
    	 */
    	this.storageSearchField = new GUISearchField(89, 102, 80, 15, 0.75F);
    	
    	this.storageSearchField.setEnabledBackgroundColor(0x8C252525);//Изменение цветов фона для разных состояний.
    	this.storageSearchField.setHoveredBackgroundColor(0x8C505050);
    		
    	this.storageSearchField.setDefaultText("searchField.search");//Установка текста, который будет отображаться по умолчанию в поисковом поле. Текст автоматически локализуется.
    	
    	this.storageFramework.initSearchField(this.storageSearchField);//Инициализация в фреймворке.
    	
    	/*
    	 * Инициализация панели для кнопок.
    	 * 
    	 * Параметры:
    	 * 
    	 * 1 - ориентация панели - горизонтальное расположение кнопок или вертикальное.
    	 * 2 - позиция по x.
    	 * 3 - позиция по y.
    	 * 4 - ширина кнопки.
    	 * 5 - высота кнопки.
    	 * 6 - коэффициент скалирования.
    	 */
    	this.storageSortersPanel = new GUIButtonPanel(GUIOrientation.VERTICAL, 73, 112, 15, 15, 1.0F);
    	
    	this.storageSortersPanel.setBackgroundEnabled(true);//Включение отображения фона за кнопками.
    	
    	this.storageSortersPanel.setEnabledBackgroundColor(0x8C202020);//Устанавливает цвет фона.
    	
    	//Работа с кнопками
    	
    	//Кнопка главной секции "зажата". И спользует подсвеченую текстуру. 
    	//Демонстрирует нахождение в главной секции при открытии инвентаря.
    	this.storageBaseSorter.setToggled(true);
    	
    	//Установка текстур для кнопок. Текстура должна состоять из ТРЁХ секций (текстур) для разных состояний (неактивна, активна, наведён курсор) кнопок, 
    	//смотрите пример текстур в папке ресурсов.	Рзмер текстуры указывается для ОДНОЙ кнопки (прим. файл 45*15 = указываемые размеры 15*15).
    	this.storageBaseSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/main.png"), 15, 15);
    	this.storageWeaponSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/weapon.png"), 15, 15);
    	this.storageArmorSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/armor.png"), 15, 15);
    	this.storageToolsSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/tools.png"), 15, 15);
    	this.storageConsumablesSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/consumables.png"), 15, 15);
    	this.storageMaterialsSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/materials.png"), 15, 15);
    	
    	//Установка всплывающего текста при наведении курсора на кнопку, по желанию. Можно добавлять множество строк, разделяя их знаком переноса "/n".
    	//По умалчанию используются стандартный стиль попап текста (как при наведении курсора на вкладку в креативе). 
    	//Можно задать собственную текстуру и/или заливку для фона попап текста.
    	this.storageBaseSorter.setPopupText("guiButton.mainSection");
    	this.storageWeaponSorter.setPopupText("guiButton.weaponSection");
    	this.storageArmorSorter.setPopupText("guiButton.armorSection");
    	this.storageToolsSorter.setPopupText("guiButton.toolsSection");
    	this.storageConsumablesSorter.setPopupText("guiButton.consumablesSection");
    	this.storageMaterialsSorter.setPopupText("guiButton.materialsSection");
    	
    	//Добавление кнопок на панель.
    	this.storageSortersPanel.addButton(this.storageBaseSorter);
    	this.storageSortersPanel.addButton(this.storageWeaponSorter);
    	this.storageSortersPanel.addButton(this.storageArmorSorter);
    	this.storageSortersPanel.addButton(this.storageToolsSorter);
    	this.storageSortersPanel.addButton(this.storageConsumablesSorter);
    	this.storageSortersPanel.addButton(this.storageMaterialsSorter);
    	
    	//Добавление панели в фреймворк.
    	this.storageFramework.addButtonPanel(this.storageSortersPanel);
    	
    	/*
    	 * Инициализация контекстного меню.
    	 * 
    	 * Параметры:
    	 * 
    	 * 1 - коэффициент скалирования.
    	 */
    	this.storageContextMenu = new GUIContextMenu(1.0F);
    	
    	//Добавление пользовательских действий в меню. Настройка граф. характеристик происходит для контекстного меню, а не для самих действий.
    	this.storageContextMenu.addAction(this.WITHDRAW);
    	this.storageContextMenu.addAction(GUIExtendedInventory.DESTROY);
    	this.storageContextMenu.addAction(GUIExtendedInventory.SPLIT);
    	
    	this.storageFramework.initContextMenu(this.storageContextMenu);//Инициализация меню в фреймворке.
    	
    	this.addFramework(this.storageFramework);//Регистрация фреймворка.
    	
    	this.updateFramework(this.storageFramework, GUIContainerFramework.BASE_SORTER);//Загрузка оригинального содержимого слотов фреймворка.
    }
    
    //Добавление слотов из нового инвентаря.
    private void initInventory() {
    	
    	/*
    	 * Параметры:
    	 * 
    	 * 1 - позиция будет указана в ГПИ.
    	 * 2 - передача IInventory кастомного инвентаря-хранилища.
    	 * 3 - индекс первого слота (включительно) из последовательности, которую нужно добавить. 
    	 * Это индекс, соответствующий позиции слота в Container#inventorySlots.
    	 * 4 - индекс последнего слота (включительно). Добавлено 150 слотов.
    	 * 5 - кол-во слотов отображаемых по горизонтали.
    	 * 6 - кол-во слотов отображаемых по вертикали. В итоге 50 слотов будут отображены в области 10 * 5 (50 слотов видны, 100 скрыты).
    	 */   
    	this.inventoryFramework = new GUIContainerFramework(GUIPosition.CUSTOM, this.playerInventory, 350, 499, 10, 5);//Инициализация, использование координат из ГПИ.
    	
    	this.inventoryFramework.setPosition(261, 51);//Указание кастомной позиции фреймворка (x, y), оригинальные позиии слотов, заданные в Container будут игнорироваться.
    	
    	//Инициализация скроллера. Эта версия конструктора принимает параметрами макс. кол-во строк слотов и кол-во строк слотов, отображаемых единовременно. 
    	//Данную версию рекомендуется использовать если набор слотов, который передаётся фрейму периодически изменяется (прим. расширяемые инвентари).
    	//В данном случае это просто демонстрация.
    	//Скроллер расчитает необходимые параметры и позволит сколлить содержимое инвентаря (в данном случае по всем 150 слотам).
    	this.inventoryScroller = new GUIScroller(GUIScrollerType.STANDARD, 30, 10);
    	
    	this.inventoryFramework.initScroller(this.inventoryScroller);//Инициализация скроллера для фреймворка инвентаря.
    	
    	/*
    	 * Инициализация слайдера для скроллера. Это графический элемент, отображающий прогресс скроллинга,
    	 * а так же обладающий обратной связью, что позволяет скроллить слоты перемещая слайдбар слайдера курсором.
    	 * 
    	 * Параметры:
    	 * 
    	 * 1 - скроллер, для которого создаётся слайдер.
    	 * 2 - позиция по x.
    	 * 3 - позиция по y.
    	 * 4 - ширина.
    	 * 5 - высота.
    	 * 6 - коэффициент скалирования, позволяет скалировать размер (используется glScalef). 1.0 - натуральная величина.
    	 */
    	this.inventorySlider = new GUISlider(this.inventoryScroller, 351, 50, 4, 180, 1.0F);
    	
    	this.inventoryFramework.initSlider(this.inventorySlider);//Инициализация слайдера в фреймворке.
    	
    	/*
    	 * Инициализация поискового поля. Поле позволяет искать предметы по названию в слотах.
    	 * 
    	 * Параметры:
    	 * 
    	 * 1 - позиция по x.
    	 * 2 - позиция по y.
    	 * 3 - ширина.
    	 * 4 - высота.
    	 * 5 - коэффициент скалирования.
    	 */
    	this.inventorySearchField = new GUISearchField(260, 25, 80, 15, 0.75F);
    		
    	this.inventorySearchField.setDefaultText("searchField.search");//Установка текста, который будет отображаться по умолчанию в поисковом поле. Текст автоматически локализуется.
    	
    	this.inventoryFramework.initSearchField(this.inventorySearchField);//Инициализация в фреймворке.
    	
    	/*
    	 * Инициализация панели для кнопок.
    	 * 
    	 * Параметры:
    	 * 
    	 * 1 - ориентация панели - горизонтальное расположение кнопок или вертикальное.
    	 * 2 - позиция по x.
    	 * 3 - позиция по y.
    	 * 4 - ширина кнопки.
    	 * 5 - высота кнопки.
    	 * 6 - коэффициент скалирования.
    	 */
    	this.inventorySortersPanel = new GUIButtonPanel(GUIOrientation.HORIZONTAL, 260, 33, 15, 15, 1.0F);
    	
    	this.inventorySortersPanel.setBackgroundEnabled(true);//Включение отображения фона за кнопками.
    	
    	this.inventorySortersPanel.setEnabledBackgroundColor(0x8C202020);//Устанавливает цвет фона.
    	
    	//Работа с кнопками
    	
    	//Кнопка главной секции "зажата". И спользует подсвеченую текстуру. 
    	//Демонстрирует нахождение в главной секции при открытии инвентаря.
    	this.inventoryBaseSorter.setToggled(true);
    	
    	//Установка текстур для кнопок. Текстура должна состоять из ТРЁХ секций (текстур) для разных состояний (неактивна, активна, наведён курсор) кнопок, 
    	//смотрите пример текстур в папке ресурсов.	Рзмер текстуры указывается для ОДНОЙ кнопки (прим. файл 45*15 = указываемые размеры 15*15).
    	this.inventoryBaseSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/main.png"), 15, 15);
    	this.inventoryWeaponSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/weapon.png"), 15, 15);
    	this.inventoryArmorSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/armor.png"), 15, 15);
    	this.inventoryToolsSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/tools.png"), 15, 15);
    	this.inventoryConsumablesSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/consumables.png"), 15, 15);
    	this.inventoryMaterialsSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/materials.png"), 15, 15);
    	
    	//Установка всплывающего текста при наведении курсора на кнопку, по желанию. Можно добавлять множество строк, разделяя их знаком переноса "/n".
    	//По умалчанию используются стандартный стиль попап текста (как при наведении курсора на вкладку в креативе). 
    	//Можно задать собственную текстуру и/или заливку для фона попап текста.
    	this.inventoryBaseSorter.setPopupText("guiButton.mainSection");
    	this.inventoryWeaponSorter.setPopupText("guiButton.weaponSection");
    	this.inventoryArmorSorter.setPopupText("guiButton.armorSection");
    	this.inventoryToolsSorter.setPopupText("guiButton.toolsSection");
    	this.inventoryConsumablesSorter.setPopupText("guiButton.consumablesSection");
    	this.inventoryMaterialsSorter.setPopupText("guiButton.materialsSection");
    	
    	//Добавление кнопок на панель.
    	this.inventorySortersPanel.addButton(this.inventoryBaseSorter);
    	this.inventorySortersPanel.addButton(this.inventoryWeaponSorter);
    	this.inventorySortersPanel.addButton(this.inventoryArmorSorter);
    	this.inventorySortersPanel.addButton(this.inventoryToolsSorter);
    	this.inventorySortersPanel.addButton(this.inventoryConsumablesSorter);
    	this.inventorySortersPanel.addButton(this.inventoryMaterialsSorter);
    	
    	//Добавление панели в фреймворк.
    	this.inventoryFramework.addButtonPanel(this.inventorySortersPanel);
    	
    	/*
    	 * Инициализация контекстного меню.
    	 * 
    	 * Параметры:
    	 * 
    	 * 1 - коэффициент скалирования.
    	 */
    	this.inventoryContextMenu = new GUIContextMenu(1.0F);
    	
    	//Добавление пользовательских действий в меню. Настройка граф. характеристик происходит для контекстного меню, а не для самих действий.
    	this.inventoryContextMenu.addAction(this.STOW);
    	this.inventoryContextMenu.addAction(GUIExtendedInventory.DESTROY);
    	this.inventoryContextMenu.addAction(GUIExtendedInventory.SPLIT);
    	
    	this.inventoryFramework.initContextMenu(this.inventoryContextMenu);//Инициализация меню в фреймворке.
    	
    	this.addFramework(this.inventoryFramework);//Регистрация фреймворка.
    	
    	this.updateFramework(this.inventoryFramework, GUIContainerFramework.BASE_SORTER);//Загрузка оригинального содержимого слотов фреймворка.
    }
    
    //В этом методе происходит управление действиями при клике по кнопкам.
    protected void handleButtonPress(GUIFramework framework, GUIButton button) {
    	
    	if (framework == this.inventoryFramework) {//Проверка на принадлежность кнопки к конкретному фрейму, дабы не "отжимались" кнопки на панелях других фреймворков.
    		
    		if (button == this.inventoryBaseSorter) {//Если нажата кнопка "инвентарь"
    		
    			this.updateFramework(this.inventoryFramework, GUIContainerFramework.BASE_SORTER);//Загрузка оригинального содержимого.
    		
    			this.inventoryBaseSorter.setToggled(true);//"Зажимаем" кнопку (подсвечивание).
    		}
    	
    		else {
    		
    			this.inventoryBaseSorter.setToggled(false);//Если нажата другая кнопка - "отжимаем" эту.
    		}
    	
    		//И так далее...
    	
    		if (button == this.inventoryWeaponSorter) {
    		
    			this.updateFramework(this.inventoryFramework, GUIExtendedInventory.WEAPON_SORTER);
    		
    			this.inventoryWeaponSorter.setToggled(true);
    		}
    	
    		else {
    		
    			this.inventoryWeaponSorter.setToggled(false);
    		}
    	
    		if (button == this.inventoryArmorSorter) {
    		
    			this.updateFramework(this.inventoryFramework, GUIExtendedInventory.ARMOR_SORTER);
    		
    			this.inventoryArmorSorter.setToggled(true);
    		}
    	
    		else {
    		
    			this.inventoryArmorSorter.setToggled(false);
    		}
    	
    		if (button == this.inventoryToolsSorter) {
    		
    			this.updateFramework(this.inventoryFramework, GUIExtendedInventory.TOOLS_SORTER);
    		
    			this.inventoryToolsSorter.setToggled(true);
    		}
    	
    		else {
    		
    			this.inventoryToolsSorter.setToggled(false);
    		}
    	
    		if (button == this.inventoryConsumablesSorter) {
    		
    			this.updateFramework(this.inventoryFramework, GUIExtendedInventory.CONSUMABLES_SORTER);
    		
    			this.inventoryConsumablesSorter.setToggled(true);
    		}
    	
    		else {
    		
    			this.inventoryConsumablesSorter.setToggled(false);
    		}
    	
    		if (button == this.inventoryMaterialsSorter) {
    		
    			this.updateFramework(this.inventoryFramework, GUIExtendedInventory.MATERIALS_SORTER);
    		
    			this.inventoryMaterialsSorter.setToggled(true);
    		}
    	
    		else {
    		
    			this.inventoryMaterialsSorter.setToggled(false);
    		}
    	}
    	
    	else if (framework == this.storageFramework) {//Проверка на принадлежность кнопки к конкретному фрейму, дабы не "отжимались" кнопки на панелях других фреймворков.
    		
    		if (button == this.storageBaseSorter) {//Если нажата кнопка "инвентарь"
    		
    			this.updateFramework(this.storageFramework, GUIContainerFramework.BASE_SORTER);//Загрузка оригинального содержимого.
    		
    			this.storageBaseSorter.setToggled(true);//"Зажимаем" кнопку (подсвечивание).
    		}
    	
    		else {
    		
    			this.storageBaseSorter.setToggled(false);//Если нажата другая кнопка - "отжимаем" эту.
    		}
    	
    		//И так далее...
    	
    		if (button == this.storageWeaponSorter) {
    		
    			this.updateFramework(this.storageFramework, GUIExtendedInventory.WEAPON_SORTER);
    		
    			this.storageWeaponSorter.setToggled(true);
    		}
    	
    		else {
    		
    			this.storageWeaponSorter.setToggled(false);
    		}
    	
    		if (button == this.storageArmorSorter) {
    		
    			this.updateFramework(this.storageFramework, GUIExtendedInventory.ARMOR_SORTER);
    		
    			this.storageArmorSorter.setToggled(true);
    		}
    	
    		else {
    		
    			this.storageArmorSorter.setToggled(false);
    		}
    	
    		if (button == this.storageToolsSorter) {
    		
    			this.updateFramework(this.storageFramework, GUIExtendedInventory.TOOLS_SORTER);
    		
    			this.storageToolsSorter.setToggled(true);
    		}
    	
    		else {
    		
    			this.storageToolsSorter.setToggled(false);
    		}
    	
    		if (button == this.storageConsumablesSorter) {
    		
    			this.updateFramework(this.storageFramework, GUIExtendedInventory.CONSUMABLES_SORTER);
    		
    			this.storageConsumablesSorter.setToggled(true);
    		}
    	
    		else {
    		
    			this.storageConsumablesSorter.setToggled(false);
    		}
    	
    		if (button == this.storageMaterialsSorter) {
    		
    			this.updateFramework(this.storageFramework, GUIExtendedInventory.MATERIALS_SORTER);
    		
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
    
    //Переопределено, что бы убрать ванильное затемнение фона. Заменено нормальной (да, да) заливкой.
    public void drawDefaultBackground() {
    	
        this.drawGradientRect(0, this.storageWorkspace.getY(), this.width, this.storageWorkspace.getY() + 20, 0, 0x64000000);       
    	
        this.drawRect(0, this.storageWorkspace.getY() + 20, this.width, this.storageWorkspace.getY() + this.storageWorkspace.getHeight() - 20, 0x64000000);        
    
        this.drawGradientRect(0, this.storageWorkspace.getY() + this.storageWorkspace.getHeight() - 20, this.width, this.storageWorkspace.getY() + this.storageWorkspace.getHeight(), 0x64000000, 0); 
    }
        
	protected void drawGuiContainerBackgroundLayer(float renderPartialTicks, int mouseX, int mouseY) {
		
		//Получение начала координат рабочего пространства. 
        int 
        k = this.storageWorkspace.getX(),
        l = this.storageWorkspace.getY();					 
	}
}
