package ru.austeretony.advancedgui.player.inventory;

import org.lwjgl.opengl.GL11;

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
import libs.austeretony.advancedgui.container.framework.GUISorter;
import libs.austeretony.advancedgui.container.text.GUISearchField;
import libs.austeretony.advancedgui.screen.button.GUIButton;
import libs.austeretony.advancedgui.screen.framework.GUIFramework;
import libs.austeretony.advancedgui.screen.panel.GUIButtonPanel;
import libs.austeretony.advancedgui.screen.panel.GUIButtonPanel.GUIOrientation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import ru.austeretony.advancedgui.main.AdvancedGUIMain;
import ru.austeretony.advancedgui.player.inventory.contextactions.ActionDestroyStack;
import ru.austeretony.advancedgui.player.inventory.contextactions.ActionDropStack;
import ru.austeretony.advancedgui.player.inventory.contextactions.ActionEquip;
import ru.austeretony.advancedgui.player.inventory.contextactions.ActionSplitStack;
import ru.austeretony.advancedgui.player.inventory.contextactions.ActionUnequip;
import ru.austeretony.advancedgui.player.inventory.sorters.ArmorSorter;
import ru.austeretony.advancedgui.player.inventory.sorters.ConsumablesSorter;
import ru.austeretony.advancedgui.player.inventory.sorters.MaterialsSorter;
import ru.austeretony.advancedgui.player.inventory.sorters.ToolsSorter;
import ru.austeretony.advancedgui.player.inventory.sorters.WeaponSorter;

public class GUIExtendedInventory extends AdvancedGUIContainer {
	
	/*
	 * В данном примере ограничено расписано использование основных возможностей AdvancedGUI, 
	 * полностью расписывать использование библиотеки начну только после релиза, а пока не ленитесь 
	 * просматривать классы и разбираться самостоятельно, если хотите пользоваться ей по полной уже сейчас.
	 */
	
	private static final ResourceLocation TEXTURE_INVENTORY = new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/inventory_first.png");
	
	private final EntityPlayer player;
	
	private final InventoryExtended inventory;
	
	/*
	 * Объявление настройщика рабочего пространства для ГПИ, который определяет размер, положение и позволяет задать текстуру.
	 * При изменении положения рабочего пространства все элементы GUIFramework будут перемещаться вместе с ним.
	 */
    private GUIContainerWorkspace inventoryWorkspace;
	
	/*
	 * Обьявление переменных GUIFramework, описывающих пространство слотов.
	 * В данном случае используются два объекта для слотов ванильного 
	 * инвентаря (броня+хотбар) и нового инвентаря (150 слотов для хранения)
	 * соответственно.
	 */
	private GUIContainerFramework equipmentFramework, inventoryFramework;		
	
	/*
	 * Создание сортировщиков для слотов.
	 * Сортировщик используется для фильтрования отображаемых слотов
	 * к примеру по содержащимся в них предметам.
	 */
	public static final GUISorter 
	WEAPON_SORTER = new WeaponSorter(), 
	ARMOR_SORTER = new ArmorSorter(), 
	TOOLS_SORTER = new ToolsSorter(),
	CONSUMABLES_SORTER = new ConsumablesSorter(), 
	MATERIALS_SORTER = new MaterialsSorter();
	
	//Скроллер по ГПИ
	private GUIScroller scroller;
	
	//Слайдер для скроллера
	private GUISlider slider;
	
	//Поисковое поле для поика предметов в фреймворке
	private GUISearchField searchField;
	
	//Панель для кнопок, используется для удобного размещения и позиционирования кнопок GUIButton
	private GUIButtonPanel sortersPanel;
    
	//Функционал кнопок реализуется в методе handleButtonPress()
	//Эти кнопки используются для сортировки инвентаря по секциям (броня, оружие и т.д.), будут добавлены на панель
    private GUIButton 
    baseSorter = new GUIButton(), 
    weaponSorter = new GUIButton(), 
    armorSorter = new GUIButton(), 
    toolsSorter = new GUIButton(), 
    consumablesSorter = new GUIButton(), 
    materialsSorter = new GUIButton();
    
    //Контекстные меню
    private GUIContextMenu equipmentContextMenu, inventoryContextMenu;
    
    //Контекстные действия для меню
    public static final GUIPresetAction
    DROP = new ActionDropStack("contextAction.drop"),//Выбросить предмет
    DESTROY = new ActionDestroyStack("contextAction.destroy"),//Уничтожить предмет
    SPLIT = new ActionSplitStack("contextAction.split"),//Разделить стак пополам
    EQUIP = new ActionEquip("contextAction.equip"),//Экипировать элемент
    UNEQUIP = new ActionUnequip("contextAction.unequip");//Деэкипировать элемент
      
	public GUIExtendedInventory(EntityPlayer player, InventoryExtended inventory) {
		
		super(new ContainerExtendedInventory(player, inventory));
		
		this.player = player;
		
		this.inventory = inventory;
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
        this.inventoryWorkspace = new GUIContainerWorkspace(this, 384, 256, GUIAlignment.RIGHT, 42, 0);
        
        //this.inventoryWorkspace.setDebugMode(true);//Тестовая заливка для наглядного отображения определения позиции и границ.
        
        //Установка текстуры ГПИ. Для текстуры так же может быть задано смещение.
        this.inventoryWorkspace.setTexture(this.TEXTURE_INVENTORY, 384, 256);
        
        return this.inventoryWorkspace;
    }
    
    //Здесь регистрируются объекты GUIFramework.
    protected void initFrameworks() {
    	
    	//ВНИМАНИЕ! Инициализировать фреймы слотов нужно в том порядке, в котором их слоты добавлялись в Container'e.
    	this.initEquipment();//Для удобства настройки разных зон созданы отдельные методы
    	this.initInventory();
    }
    
    //Добавление слотов (броня+хотбар) из оригинального инвентаря.
    private void initEquipment() {
    	 
    	/*
    	 * Инициализация фреймворка для слотов ванильного инвентаря (броня+хотбар).
    	 * 
    	 * Параметры:
    	 * 
    	 * 1 - определяет, что слоты будут использовать координаты, заданные в Container (ContainerInventory).
    	 * 2 - передача IInventory ванильного инвентаря игрока.
    	 * 3 - индекс первого слота (включительно) из последовательности, которую нужно добавить. 
    	 * Это индекс, соответствующий позиции слота в Container#inventorySlots.
    	 * 4 - индекс последнего слота (включительно). По идее добавлено всего 13 слотов (4+9), 
    	 * но последовательность должна быть 0-13 (14 слотов) для ванильных слотов (баг или фича?).
    	 * 5 - кол-во слотов отображаемых по горизонтали, при использовании GUIPosition.CONTAINER параметр должен быть равен нулю.
    	 * 6 - кол-во слотов отображаемых по вертикали, при использовании GUIPosition.CONTAINER параметр должен быть равен нулю.
    	 */    	
    	this.equipmentFramework = new GUIContainerFramework(GUIPosition.CONTAINER, this.player.inventory, 0, 13, 0, 0);//Инициализация фреймворка для экипировки
    	
    	/*
    	 * Инициализация контекстного меню.
    	 * 
    	 * Параметры:
    	 * 
    	 * 1 - коэффициент скалирования. Определяет размер, 1.0 по умолчанию - натуральный размер.
    	 */
    	this.equipmentContextMenu = new GUIContextMenu(1.0F);
    	
    	//Добавление пользовательских действий в меню.
    	this.equipmentContextMenu.addAction(this.UNEQUIP);
    	this.equipmentContextMenu.addAction(this.DROP);
    	this.equipmentContextMenu.addAction(this.DESTROY);
    	
    	this.equipmentFramework.initContextMenu(this.equipmentContextMenu);//Инициализация меню в фреймворке.
    	
    	this.addFramework(this.equipmentFramework);//Регистрация фреймворка.
    	
    	this.updateFramework(this.equipmentFramework, GUIContainerFramework.BASE_SORTER);//Загрузка оригинального содержимого слотов. 
    	//При инициализации проверка содержимого слотов всегда возвращает null, так что попытка проверки на указанный предмет в кастомном сортировщике
    	//для любого слота вернёт false, используйте BASE_SORTER во время инициализации.
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
    	this.inventoryFramework = new GUIContainerFramework(GUIPosition.CUSTOM, this.inventory, 13, 162, 10, 5);//Инициализация, использование координат из ГПИ.
    	
    	this.inventoryFramework.setPosition(261, 51);//Указание кастомной позиции фреймворка (x, y), оригинальные позиии слотов, заданные в Container будут игнорироваться.
    	
    	//Инициализация скроллера. Конструктор GUIScroller принимает объект GUIFramework, в котором должен функционировать и enum параметр, описывающий поведение. 
    	//Скроллер расчитает необходимые параметры и позволит сколлить содержимое инвентаря (в данном случае по всем 150 слотам).
    	this.scroller = new GUIScroller(GUIScrollerType.STANDARD, this.inventoryFramework);
    	
    	//Отключение запрета на скролл колёсиком если курсор не находится в зоне слотов фреймворка. 
    	//Это ограничение используется для исключения конфликтов нескольких скроллеров.
    	this.scroller.ignoreFrameworkBorders();	
    	
    	this.inventoryFramework.initScroller(this.scroller);//Инициализация скроллера для фреймворка инвентаря.
    	
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
    	this.slider = new GUISlider(this.scroller, 351, 50, 4, 180, 1.0F);
    	    	
    	this.inventoryFramework.initSlider(this.slider);//Инициализация слайдера в фреймворке.
    	
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
    	this.searchField = new GUISearchField(260, 25, 80, 15, 0.75F);
    		
    	this.searchField.setDefaultText("searchField.search");//Установка текста, который будет отображаться по умолчанию в поисковом поле. Текст автоматически локализуется.
    	
    	this.inventoryFramework.initSearchField(this.searchField);//Инициализация в фреймворке.
    	
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
    	this.sortersPanel = new GUIButtonPanel(GUIOrientation.HORIZONTAL, 260, 33, 15, 15, 1.0F);
    	
    	this.sortersPanel.setBackgroundEnabled(true);//Включение отображения фона за кнопками.
    	
    	this.sortersPanel.setEnabledBackgroundColor(0x8C202020);//Устанавливает цвет фона.      	
    	
    	//Работа с кнопками
    	
    	//Кнопка главной секции "зажата". И спользует подсвеченую текстуру. 
    	//Демонстрирует нахождение в главной секции при открытии инвентаря.
    	this.baseSorter.setToggled(true);
    	
    	//Установка текстур для кнопок. Текстура должна состоять из ТРЁХ секций (текстур) для разных состояний (неактивна, активна, наведён курсор) кнопок, 
    	//смотрите пример текстур в папке ресурсов.	Рзмер текстуры указывается для ОДНОЙ кнопки (прим. файл 45*15 = указываемые размеры 15*15).
    	this.baseSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/main.png"), 15, 15);
    	this.weaponSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/weapon.png"), 15, 15);
    	this.armorSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/armor.png"), 15, 15);
    	this.toolsSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/tools.png"), 15, 15);
    	this.consumablesSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/consumables.png"), 15, 15);
    	this.materialsSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/materials.png"), 15, 15);
    	
    	//Установка всплывающего текста при наведении курсора на кнопку, по желанию. Можно добавлять множество строк, разделяя их знаком переноса "/n".
    	//По умалчанию используются стандартный стиль попап текста (как при наведении курсора на вкладку в креативе). 
    	//Можно задать собственную текстуру и/или заливку для фона попап текста.
    	this.baseSorter.setPopupText("guiButton.mainSection");
    	this.weaponSorter.setPopupText("guiButton.weaponSection");
    	this.armorSorter.setPopupText("guiButton.armorSection");
    	this.toolsSorter.setPopupText("guiButton.toolsSection");
    	this.consumablesSorter.setPopupText("guiButton.consumablesSection");
    	this.materialsSorter.setPopupText("guiButton.materialsSection");
    	
    	//Добавление кнопок на панель.
    	this.sortersPanel.addButton(this.baseSorter);
    	this.sortersPanel.addButton(this.weaponSorter);
    	this.sortersPanel.addButton(this.armorSorter);
    	this.sortersPanel.addButton(this.toolsSorter);
    	this.sortersPanel.addButton(this.consumablesSorter);
    	this.sortersPanel.addButton(this.materialsSorter);
    	
    	//Добавление панели в фреймворк.
    	this.inventoryFramework.addButtonPanel(this.sortersPanel);
    	
    	/*
    	 * Инициализация контекстного меню.
    	 * 
    	 * Параметры:
    	 * 
    	 * 1 - коэффициент скалирования.
    	 */
    	this.inventoryContextMenu = new GUIContextMenu(1.0F);
    	
    	//Добавление пользовательских действий в меню. Настройка граф. характеристик происходит для контекстного меню, а не для самих действий.
    	this.inventoryContextMenu.addAction(this.DROP);
    	this.inventoryContextMenu.addAction(this.DESTROY);
    	this.inventoryContextMenu.addAction(this.EQUIP);
    	this.inventoryContextMenu.addAction(this.SPLIT);
    	
    	this.inventoryFramework.initContextMenu(this.inventoryContextMenu);//Инициализация меню в фреймворке.
    	
    	this.addFramework(this.inventoryFramework);//Регистрация фреймворка.
    	
    	this.updateFramework(this.inventoryFramework, GUIContainerFramework.BASE_SORTER);//Загрузка оригинального содержимого слотов фреймворка.
    }
    
    //В этом методе происходит управление действиями при клике по кнопкам.
    protected void handleButtonPress(GUIFramework framework, GUIButton button) {
    	
    	if (button == this.baseSorter) {//Если нажата кнопка "инвентарь"
    		
    		this.updateFramework(this.inventoryFramework, GUIContainerFramework.BASE_SORTER);//Загрузка оригинального содержимого.
    		
    		this.baseSorter.setToggled(true);//"Зажимаем" кнопку (подсвечивание).
    	}
    	
    	else {
    		
    		this.baseSorter.setToggled(false);//Если нажата другая кнопка - "отжимаем" эту.
    	}
    	
    	//И так далее...
    	
    	if (button == this.weaponSorter) {
    		
    		this.updateFramework(this.inventoryFramework, this.WEAPON_SORTER);
    		
    		this.weaponSorter.setToggled(true);
    	}
    	
    	else {
    		
    		this.weaponSorter.setToggled(false);
    	}
    	
    	if (button == this.armorSorter) {
    		
    		this.updateFramework(this.inventoryFramework, this.ARMOR_SORTER);
    		
    		this.armorSorter.setToggled(true);
    	}
    	
    	else {
    		
    		this.armorSorter.setToggled(false);
    	}
    	
    	if (button == this.toolsSorter) {
    		
    		this.updateFramework(this.inventoryFramework, this.TOOLS_SORTER);
    		
    		this.toolsSorter.setToggled(true);
    	}
    	
    	else {
    		
    		this.toolsSorter.setToggled(false);
    	}
    	
    	if (button == this.consumablesSorter) {
    		
    		this.updateFramework(this.inventoryFramework, this.CONSUMABLES_SORTER);
    		
    		this.consumablesSorter.setToggled(true);
    	}
    	
    	else {
    		
    		this.consumablesSorter.setToggled(false);
    	}
    	
    	if (button == this.materialsSorter) {
    		
    		this.updateFramework(this.inventoryFramework, this.MATERIALS_SORTER);
    		
    		this.materialsSorter.setToggled(true);
    	}
    	
    	else {
    		
    		this.materialsSorter.setToggled(false);
    	}
    }
    
    public boolean doesGuiPauseGame() {
    	
        return false;
    }
    
    //Переопределено, что бы убрать ванильное затемнение фона. Заменено нормальной (да, да) заливкой.
    public void drawDefaultBackground() {
    	
        this.drawGradientRect(0, this.inventoryWorkspace.getY(), this.width, this.inventoryWorkspace.getY() + 20, 0, 0x64000000);       
    	
        this.drawRect(0, this.inventoryWorkspace.getY() + 20, this.width, this.inventoryWorkspace.getY() + this.inventoryWorkspace.getHeight() - 20, 0x64000000);        
    
        this.drawGradientRect(0, this.inventoryWorkspace.getY() + this.inventoryWorkspace.getHeight() - 20, this.width, this.inventoryWorkspace.getY() + this.inventoryWorkspace.getHeight(), 0x64000000, 0); 
    }
        
	protected void drawGuiContainerBackgroundLayer(float renderPartialTicks, int mouseX, int mouseY) {
		
		//Получение начала координат рабочего пространства. 
        int 
        k = this.inventoryWorkspace.getX(),
        l = this.inventoryWorkspace.getY();					
        
        GL11.glEnable(GL11.GL_DEPTH_TEST);//Прямой вызов функций GL?! Убирает артефакты при поворотах модели.
        
		drawPlayerModel(k + 157, l + 138, 30, 0, 0, this.mc.player);//Отрисовка игрока.
		
        GL11.glDisable(GL11.GL_DEPTH_TEST);	  
	}

	public static void drawPlayerModel(int x, int y, int scale, float yaw, float pitch, EntityLivingBase entity) {
		
        GlStateManager.enableColorMaterial();
        
        GlStateManager.pushMatrix();
        
        GlStateManager.translate((float) x, (float) y, 50.0F);
        
        GlStateManager.scale((float) (- scale), (float) scale, (float) scale);
        
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        
        float f = entity.renderYawOffset;
        float f1 = entity.rotationYaw;
        float f2 = entity.rotationPitch;
        float f3 = entity.prevRotationYawHead;
        float f4 = entity.rotationYawHead;
        
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        
        RenderHelper.enableStandardItemLighting();
        
        GlStateManager.rotate(- 135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(- ((float) Math.atan((double) (pitch / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        
        entity.renderYawOffset = (float) Math.atan((double) (yaw / 40.0F)) * 20.0F;
        entity.rotationYaw = (float) Math.atan((double) (yaw / 40.0F)) * 40.0F;
        entity.rotationPitch = - ((float) Math.atan((double) (pitch / 40.0F))) * 20.0F;
        entity.rotationYawHead = entity.rotationYaw;
        entity.prevRotationYawHead = entity.rotationYaw;
        
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        
        entity.renderYawOffset = f;
        entity.rotationYaw = f1;
        entity.rotationPitch = f2;
        entity.prevRotationYawHead = f3;
        entity.rotationYawHead = f4;
        
        GlStateManager.popMatrix();
        
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}
}

