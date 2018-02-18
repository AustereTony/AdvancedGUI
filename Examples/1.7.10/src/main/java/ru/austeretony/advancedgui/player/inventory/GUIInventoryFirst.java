package ru.austeretony.advancedgui.player.inventory;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

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
import libs.austeretony.advancedgui.guicontainer.framework.GUISorter;
import libs.austeretony.advancedgui.guicontainer.panel.GUIButtonPanel;
import libs.austeretony.advancedgui.guicontainer.panel.GUIButtonPanel.GUIOrientation;
import libs.austeretony.advancedgui.guicontainer.text.GUISearchField;
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

public class GUIInventoryFirst extends AdvancedGUIContainer {
	
	private static final ResourceLocation TEXTURE_INVENTORY = new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/inventoryFirst.png");
	
	private final EntityPlayer player;
	
	private final PlayerInventory inventory;
	
	/*
	 * Объявление настройщика рабочего пространства для ГПИ, который определяет её размер, положение и позволяет задать текстуру.
	 * При изменении положения рабочего пространства все элементы GUIFramework будут перемещаться вместе с ним.
	 */
    private GUIContainerWorkspace inventoryWorkspace;
	
	/*
	 * Обьявление переменных GUIFramework, описывающих пространство слотов.
	 * В данном случае используются два объекта для слотов ванильного 
	 * инвентаря (броня+хотбар) и нового инвентаря (150 слотов для хранения)
	 * соответственно.
	 */
	private GUIFramework equipmentFramework, inventoryFramework;		
	
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
	
	//Скроллер по ГПИ.
	private GUIScroller scroller;
	
	//Слайдер для скроллера.
	private GUISlider slider;
	
	//Поисковое поле для поика предметов в фреймворке.
	private GUISearchField searchField;
	
	//Панель для кнопок, используется для удобного размещения и позиционирования кнопок GUIButton.
	private GUIButtonPanel sortersPanel;
    
	//Функционал кнопок реализуется в методе handleButtonPress().
	//Эти кнопки используются для сортировки инвентаря по секциям (броня, оружие и т.д.), будут добавлены на панель.
    private GUIButton 
    baseSorter = new GUIButton(), 
    weaponSorter = new GUIButton(), 
    armorSorter = new GUIButton(), 
    toolsSorter = new GUIButton(), 
    consumablesSorter = new GUIButton(), 
    materialsSorter = new GUIButton();
    
    //Контекстные меню.
    private GUIContextMenu equipmentContextMenu, inventoryContextMenu;
    
    //Контекстные действия. 
    public static final GUIPresetAction
    DROP = new ActionDropStack("contextAction.drop"),//Выбросить предмет
    DESTROY = new ActionDestroyStack("contextAction.destroy"),//Уничтожить предмет
    SPLIT = new ActionSplitStack("contextAction.split"),//Разделить стак пополам
    EQUIP = new ActionEquip("contextAction.equip"),//Экипировать элемент
    UNEQUIP = new ActionUnequip("contextAction.unequip");//Деэкипировать элемент
      
	public GUIInventoryFirst(EntityPlayer player, PlayerInventory inventory) {
		
		super(new ContainerInventory(player, inventory));
		
		this.player = player;
		
		this.inventory = inventory;
	}
	
	//Здесь инициализируется GUIContainerWorkspace, определяющая рабочее пространство (размер и положение).
    protected GUIContainerWorkspace initWorkspace() {
    	
    	/*
    	 * Создание и настройка рабочего пространста ГПИ.
    	 * 
    	 * Параметры:
    	 * 
    	 * 1 - GuiScreen.
    	 * 2 - ширина рабочей области.
    	 * 3 - высота рабочей области.
    	 * 4 - параметр юстировки, определяющий сторону смещения.
    	 * 5 - смещение по горизонтали.
    	 * 6 - смещение по вертикали.
    	 */
        this.inventoryWorkspace = new GUIContainerWorkspace(this, 384, 256, GUIAlignment.RIGHT, 42, 0);
        
        //this.inventoryPanel.enableDebugMode();//Заливка для отображения границ.
        
        this.inventoryWorkspace.setTexture(this.TEXTURE_INVENTORY, 384, 256);//Установка текстуры ГПИ.
        
        return this.inventoryWorkspace;
    }
    
    //Здесь регистрируются фреймворки - объекты GUIFramework.
    protected void initFrameworks() {
    	
    	this.initEquipment();//Вспомогательные методы для разделения регистрации и описания разных фреймворков.
    	this.initInventory();
    }
    
    //Добавление слотов (броня+хотбар) из оригинального инвентаря.
    private void initEquipment() {
    	 
    	/*
    	 * Инициализация фреймворка для слотов ванильного инвентаря.
    	 * 
    	 * Параметры:
    	 * 
    	 * 1 - определяет, что слоты будут использовать координаты, заданные в Container (ContainerInventory).
    	 * 2 - передача IInventory ванильного инвентаря игрока.
    	 * 3 - индекс первого слота (включительно) из последовательности, которую нужно добавить. 
    	 * Это индекс, соответствующий позиции слота в Container#inventorySlots.
    	 * 4 - индекс последнего слота (включительно). По идее нужно добавить 13 слотов (4+9) (0-12), 
    	 * но последовательность 0-13 включает 14 слотов (вместо 13) - это какой то баг ванильного инвентаря (или фича структуры, не разбирался).
    	 * 5 - кол-во слотов отображаемых по горизонтали, при использовании GUIPosition.CONTAINER параметр должен быть равен нулю.
    	 * 6 - кол-во слотов отображаемых по вертикали, при использовании GUIPosition.CONTAINER параметр должен быть равен нулю.
    	 */    	
    	this.equipmentFramework = new GUIFramework(GUIPosition.CONTAINER, this.player.inventory, 0, 13, 0, 0);//Инициализация фреймворка для экипировки
    	
    	/*
    	 * Инициализация контекстного меню.
    	 * 
    	 * Параметры:
    	 * 
    	 * 1 - коэффициент скалирования.
    	 */
    	this.equipmentContextMenu = new GUIContextMenu(1.0F);
    	
    	//Добавление пользовательских действий в меню.
    	this.equipmentContextMenu.addAction(new GUIContextAction(this.UNEQUIP));
    	this.equipmentContextMenu.addAction(new GUIContextAction(this.DROP));
    	this.equipmentContextMenu.addAction(new GUIContextAction(this.DESTROY));
    	
    	this.equipmentFramework.initContextMenu(this.equipmentContextMenu);//Инициализация меню в фреймворке.
    	
    	this.addFramework(this.equipmentFramework);//Регистрация.
    	
    	this.updateFramework(this.equipmentFramework, GUIFramework.BASE_SORTER);//Загрузка оригинального содержимого.
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
    	this.inventoryFramework = new GUIFramework(GUIPosition.CUSTOM, this.inventory, 13, 162, 10, 5);//Инициализация, использование координат из ГПИ.
    	
    	this.inventoryFramework.setPosition(261, 50);//Указание кастомной позиции фреймворка (x, y), оригинальные позиии слотов будут игнорироваться.
    	
    	//Инициализация скроллера. Конструктор GUIScroller принимает объект GUIFramework, в котором должен функционировать. 
    	//Скроллер расчитает необходимые параметры и позволит сколлить содержимое инвентаря (в данном случае по всем 150 слотам).
    	this.scroller = new GUIScroller(this.inventoryFramework);
    	
    	//Скроллер будет работать независимо от положения курсора (по умалчанию скроллить можно только когда
    	//курсор находится в области фреймворка (для исключения конфликтов, когда скроллеров несколько))
    	//Т.к. скроллер один, то возможность его использования не ограничивается (речь идёт о скроллле колёском).
    	this.scroller.ignoreFrameworkBorders();	
    	
    	this.inventoryFramework.initScroller(this.scroller);//Инициализация скроллера для фреймворка инвентаря.
    	
    	/*
    	 * Инициализация слайдера для скроллера. Это графический элемент, отображающий прогресс скроллинга,
    	 * а так же обладающий обратной связью, что позволяет скроллить перемещая слайдбар слайдера.
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
    	this.slider = new GUISlider(this.scroller, 351, 50, 6, 180, 1.0F);
    	
    	this.slider.setBackgroundColor(0x8C555555);//Изменение цветов на прозрачные.
    	this.slider.setSlidebarColor(0x8CA5A5A5);
    	this.slider.setHoveredSlidebarColor(0x8CBFBFBF);
    	
    	this.inventoryFramework.initSlider(this.slider);//Инициализация слайдера в фреймворке.
    	
    	/*
    	 * Инициализация поискового поля. Поле позволяет искать предметы по названию в ассоциированном фреймворке.
    	 * 
    	 * Параметры:
    	 * 
    	 * 1 - позиция по x.
    	 * 2 - позиция по y.
    	 * 3 - ширина.
    	 * 4 - высота.
    	 * 5 - коэффициент скалирования.
    	 */
    	this.searchField = new GUISearchField(260, 21, 60, 15, 1.0F);
    	
    	this.searchField.setBackgroundColor(0x8C252525);//Изменение цветов на прозрачные.
    	this.searchField.setFocusedBackgroundColor(0x8C505050);
    		
    	this.searchField.setDefaultText("searchField.search");//Установка текста, который будет отображаться по умалчанию в поисковом поле.
    	
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
    	this.sortersPanel = new GUIButtonPanel(GUIOrientation.HORIZONTAL, 260, 32, 15, 15, 1.0F);
    	
    	this.sortersPanel.setBackgroundEnabled(true);//Отображает фон за кнопками.
    	
    	this.sortersPanel.setBackgroundColor(0x8C202020);//Устанавливает цвет фона.
    	
    	//Работа с кнопками
    	
    	//Кнопка главной секции "зажата". И спользует подсвеченую текстуру. 
    	//Демонстрирует нахождение в главной секции при открытии инвентаря.
    	this.baseSorter.setToggled(true);
    	
    	//Установка текстур для кнопок.
    	this.baseSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/main.png"), 15, 15);
    	this.weaponSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/weapon.png"), 15, 15);
    	this.armorSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/armor.png"), 15, 15);
    	this.toolsSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/tools.png"), 15, 15);
    	this.consumablesSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/consumables.png"), 15, 15);
    	this.materialsSorter.setTexture(new ResourceLocation(AdvancedGUIMain.MODID, "textures/gui/inventory/buttons/materials.png"), 15, 15);
    	
    	//Установка всплывающего текста при наведении курсора на кнопку.
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
    	
    	//Добавление пользовательских действий в меню.
    	this.inventoryContextMenu.addAction(new GUIContextAction(this.DROP));
    	this.inventoryContextMenu.addAction(new GUIContextAction(this.DESTROY));
    	this.inventoryContextMenu.addAction(new GUIContextAction(this.EQUIP));
    	this.inventoryContextMenu.addAction(new GUIContextAction(this.SPLIT));
    	
    	this.inventoryFramework.initContextMenu(this.inventoryContextMenu);//Инициализация меню в фреймворке.
    	
    	this.addFramework(this.inventoryFramework);//Регистрация фреймворка.
    	
    	this.updateFramework(this.inventoryFramework, GUIFramework.BASE_SORTER);//Загрузка оригинального содержимого слотов фреймворка.
    }
    
    protected void handleButtonPress(GUIFramework framework, GUIButton button) {
    	
    	if (button == this.baseSorter) {//Если нажата кнопка "инвентарь"
    		
    		this.updateFramework(this.inventoryFramework, GUIFramework.BASE_SORTER);//Загрузка оригинального содержимого.
    		
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
    
    //Переопределено, что бы убрать ванильное затемнение фона.
    public void drawDefaultBackground() {
    	
        this.drawGradientRect(0, this.inventoryWorkspace.getYPosition(), this.width, this.inventoryWorkspace.getYPosition() + 20, 0, 0x64000000);       
    	
        this.drawRect(0, this.inventoryWorkspace.getYPosition() + 20, this.width, this.inventoryWorkspace.getYPosition() + this.inventoryWorkspace.getHeight() - 20, 0x64000000);        
    
        this.drawGradientRect(0, this.inventoryWorkspace.getYPosition() + this.inventoryWorkspace.getHeight() - 20, this.width, this.inventoryWorkspace.getYPosition() + this.inventoryWorkspace.getHeight(), 0x64000000, 0); 
    }
        
	protected void drawGuiContainerBackgroundLayer(float renderPartialTicks, int mouseX, int mouseY) {
		
		//Получение начала координат рабочего пространства. 
        int 
        k = this.inventoryWorkspace.getXPosition(),
        l = this.inventoryWorkspace.getYPosition();					
        
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        
		drawPlayerModel(k + 157, l + 138, 30, 0, 0, this.mc.thePlayer);
		
        GL11.glDisable(GL11.GL_DEPTH_TEST);	  
	}

	public static void drawPlayerModel(int x, int y, int scale, float yaw, float pitch, EntityLivingBase entity) {
		
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        
        GL11.glPushMatrix();
        
        GL11.glTranslatef((float) x, (float) y, 50.0F);
        
        GL11.glScalef((float) - scale, (float) scale, (float) scale);
        
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        
        float f2 = entity.renderYawOffset;
        float f3 = entity.rotationYaw;
        float f4 = entity.rotationPitch;
        float f5 = entity.prevRotationYawHead;
        float f6 = entity.rotationYawHead;
        
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        
        RenderHelper.enableStandardItemLighting();
        
        GL11.glRotatef(- 135.0F, 0.0F, 1.0F, 0.0F);
        
        GL11.glRotatef(- ((float) Math.atan((double) (pitch / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        
        entity.renderYawOffset = (float) Math.atan((double) (yaw / 40.0F)) * 20.0F;
        entity.rotationYaw = (float) Math.atan((double) (yaw / 40.0F)) * 40.0F;
        entity.rotationPitch = - ((float) Math.atan((double) (pitch / 40.0F))) * 20.0F;
        entity.rotationYawHead = entity.rotationYaw;
        entity.prevRotationYawHead = entity.rotationYaw;
        
        GL11.glTranslatef(0.0F, entity.yOffset, 0.0F);
        
        RenderManager.instance.playerViewY = 180.0F;
        RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        
        entity.renderYawOffset = f2;
        entity.rotationYaw = f3;
        entity.rotationPitch = f4;
        entity.prevRotationYawHead = f5;
        entity.rotationYawHead = f6;
        
        GL11.glPopMatrix();
        
        RenderHelper.disableStandardItemLighting();
        
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
	}
}
