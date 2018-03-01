package libs.austeretony.advancedgui.container.core;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.google.common.collect.Sets;

import libs.austeretony.advancedgui.container.browsing.GUIScroller.GUIScrollerType;
import libs.austeretony.advancedgui.container.framework.GUIContainerFramework;
import libs.austeretony.advancedgui.container.framework.GUIContainerFramework.GUIPosition;
import libs.austeretony.advancedgui.container.framework.GUISorter;
import libs.austeretony.advancedgui.screen.button.GUIButton;
import libs.austeretony.advancedgui.screen.core.AdvancedGUIScreen;
import libs.austeretony.advancedgui.screen.panel.GUIButtonPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Модернизированный GuiContainer. Должен быть унаследован вашим ГПИ контейнера.
 */
@SideOnly(Side.CLIENT)
public abstract class AdvancedGUIContainer extends AdvancedGUIScreen {
	    
	/**
	 *  Данный класс представляет собой изменённую версию GuiContainer. 
	 * Все оригинальные механики сохранены, дополнительные элементы работают 
	 * параллельно. Изменению подвергся принцип отрисовки и получения 
	 * "кликнутого" слота. 
	 *	Данному классу в конструктор передаётся контейнер,
	 * содержащий массив слотов (Container#inventorySlots). Оригинальный класс
	 * GuiContainer работал с этим массивом напрямую. Текущая реализация 
	 * использует объекты GUIFramework для перехвата определённого набора слотов
	 * и обеспечения работы всех дополнительных элементов. Массив GUIFramework#slots 
	 * содержит копии слотов из Container#inventorySlots, GUIFramework#slotsIndexes
	 * содержит индексы этих слотов, соответствующие позициям слотов в нём. Когда
	 * производится действие со слотом (клик), вызывается метод
	 * AdvancedGUIContainer#getSlotAtPosition(), который должен вернуть реальный слот. 
	 * Оригинальный метод перебирал слоты из Container#inventorySlots, 
	 * текущий метод перебирает GUIFramework#slots, и возвращает слот из 
	 * оригинального массива Container#inventorySlots, используя индекс из 
	 * GUIFramework#slotsIndexes. Таким образом любые действия производятся с
	 * оригинальными слотами, а GUIFramework лишь предоставляет возможности
	 * по контролю отображаемого контента, т.к. все внутренние действия производятся
	 * с собственными массивами слотов.
	 * 
	 *  Для работы с новыми элементами данный класс должен быть унаследован ГПИ контейнера.
	 * Для того что бы добавить слоты в ГПИ создайте новый GUIFramework. Используйте
	 * {@link AdvancedGUIContainer#addFramework()} в {@link AdvancedGUIContainer#initFrameworks()} 
	 * для его регистрации и вызовите {@link AdvancedGUIContainer#updateFramework()} 
	 * передав в него созданый фреймворк и {@link GUIContainerFramework#BASE_SORTER} для 
	 * отображения оригинального содержимого слотов.
	 */
	
    protected int xSize = 176;
    protected int ySize = 166;
    
    public Container inventorySlots;
    
    protected int guiLeft;
    protected int guiTop;
    
    protected Slot hoveredSlot;
    protected Slot clickedSlot;
    
    private boolean isRightMouseClick;
    
    protected ItemStack draggedStack = ItemStack.EMPTY;
    private int touchUpX;
    private int touchUpY;
    protected Slot returningStackDestSlot;
    private long returningStackTime;
    
    protected ItemStack returningStack = ItemStack.EMPTY;
    private Slot currentDragTargetSlot;
    private long dragItemDropDelay;
    protected final Set<Slot> dragSplittingSlots = Sets.<Slot>newHashSet();
    protected boolean dragSplitting;
    private int dragSplittingLimit;
    private int dragSplittingButton;
    private boolean ignoreMouseUp;
    private int dragSplittingRemnant;
    private long lastClickTime;
    protected Slot lastClickSlot;
    private int lastClickButton;
    private boolean doubleClick;
    protected ItemStack shiftClickedSlot = ItemStack.EMPTY;
    
    /** Рабочее пространство */
    private GUIContainerWorkspace containerWorkspace;
	
	private int righClickedFramework, openedMenuFramework, yPrevMouse;
	
	private boolean menuOpened;
	
	protected ItemStack dragged;
    
    public AdvancedGUIContainer(Container inventorySlots) {
    	
        this.inventorySlots = inventorySlots;
        this.ignoreMouseUp = true;
    }

    public void initGui() {
    	            	
        this.mc.player.openContainer = this.inventorySlots;
        
        this.containerWorkspace = this.initWorkspace();
        
        this.xSize = this.containerWorkspace.getWidth();
        this.ySize = this.containerWorkspace.getHeight();
                
        this.guiLeft = this.containerWorkspace.getX();
        this.guiTop = this.containerWorkspace.getY();    
        
        super.initGui();
    }
    
    /**
     * Используется для инициализпции рабочего пространства ГПИ. 
     * 
     * @return настроенный GUIContainerWorkspace для этого ГПИ
     */
    protected abstract GUIContainerWorkspace initWorkspace();

    /**
     * Метод для обновления содержимого фреймворка. Должен быть вызван при инициализации ГПИ
     * для созданного фреймворка после его регистрации. Вторым аргументом принимает сортировщик
     * GUISorter. Для отображения без сортировки использовать {@link GUIContainerFramework#BASE_SORTER}.
     * 
     * @param framework фреймворк, требующий обновления
     * @param sorter сортировщик
     */
    protected void updateFramework(GUIContainerFramework framework, GUISorter sorter) {
    	    	
		int i, k, size;
    	
    	Slot slot, slotCopy;
    	
    	Item itemInSlot;
    		
    	framework.slots.clear();
    	framework.slotsIndexes.clear();
    		
    	framework.slotsBuffer.clear();
    	framework.indexesBuffer.clear();
    		
    	framework.items.clear();
    	
    	framework.setCurrentSorter(sorter);
    	
    	if (framework.getSlider() != null) {
    		
    		framework.getSlider().reset();
    	}
    		
        for (i = framework.firstSlotIndex; i <= framework.lastSlotIndex; i++) {
    		
        	if (i < this.inventorySlots.inventorySlots.size()) {
        		        		
        		slot = (Slot) this.inventorySlots.inventorySlots.get(i);   
        	
        		slotCopy = this.copySlot(slot);  
    		
        		size = framework.slots.size();			
        				
        		if (sorter.isSlotValid(slotCopy, Minecraft.getMinecraft().player)) {   
        				
            		if (slotCopy.getHasStack()) {
            			            	    		
            			framework.items.put(size, slotCopy.getStack());
            		}
        			
        			if (framework.slotsPosition == GUIPosition.CUSTOM) {
    						
        				k = size / framework.columns;
    						
        				slotCopy.xPos = framework.getXPosition() + size * (framework.getSlotWidth() + framework.getSlotDistanceHorizontal()) - k * ((framework.getSlotWidth() + framework.getSlotDistanceHorizontal()) * framework.columns);
        				slotCopy.yPos = framework.getYPosition() + k * (framework.getSlotHeight() + framework.getSlotDistanceVertical()) - (size / framework.visibleSlots) * (framework.rows * (framework.getSlotHeight() + framework.getSlotDistanceVertical()));
        			}
    					
        			framework.slots.add(slotCopy);
        			framework.slotsIndexes.add(i);  
            	    	
        			framework.slotsBuffer.add(slotCopy);
        			framework.indexesBuffer.add(i);     					
        		}
    		}
    	}
    	
		if (sorter.shouldAddEmptySlotsAfter()) {
    	
			i = framework.firstSlotIndex;
    	
	        for (i = framework.firstSlotIndex; i <= framework.lastSlotIndex; i++) {
	        	
	        	if (i < this.inventorySlots.inventorySlots.size()) {
        		
	        		slot = (Slot) this.inventorySlots.inventorySlots.get(i);   
        	
	        		slotCopy = this.copySlot(slot);  
    		
	        		if (!slotCopy.getHasStack()) {
					
	        			size = framework.slots.size();
					    				
	        			if (framework.slotsPosition == GUIPosition.CUSTOM) {
						
	        				k = size / framework.columns;
						
	        				slotCopy.xPos = framework.getXPosition() + size * (framework.getSlotWidth() + framework.getSlotDistanceHorizontal()) - k * ((framework.getSlotWidth() + framework.getSlotDistanceHorizontal()) * framework.columns);
	        				slotCopy.yPos = framework.getYPosition() + k * (framework.getSlotHeight() + framework.getSlotDistanceVertical()) - (size / framework.visibleSlots) * (framework.rows * (framework.getSlotHeight() + framework.getSlotDistanceVertical()));
	        			}
					
	        			framework.slots.add(slotCopy);
	        			framework.slotsIndexes.add(i);  
        	    	
	        			framework.slotsBuffer.add(slotCopy);
	        			framework.indexesBuffer.add(i); 
	        		}
	        	}
			}
    	}
    }
    	
    protected final Slot copySlot(Slot slot) {
        	
    	return new Slot(slot.inventory, slot.getSlotIndex(), slot.xPos, slot.yPos);	
    }
    
    //TODO drawScreen()
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    	
        int 
        i = this.guiLeft,
        j = this.guiTop,
        k;
        
        this.drawDefaultBackground();
        
        if (this.containerWorkspace != null) {
        	
        	this.containerWorkspace.draw();
        	
            this.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
        }
                
        RenderHelper.disableStandardItemLighting();
        
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();

        GlStateManager.pushMatrix();
        
        GlStateManager.translate((float) i, (float) j, 0.0F);
        
    	GUIContainerFramework framework;
    	
    	for (k = 0; k < this.frameworks.size(); k++) {
    		
    		framework = (GUIContainerFramework) this.frameworks.get(k);
						
	        for (GUIButton button : framework.getButtonsList()) {
	        	
	            button.mouseOver(mouseX - this.guiLeft, mouseY - this.guiTop);
	            
	            button.draw();	            
	        }
	        
	        for (GUIButtonPanel buttonPanel : framework.getButtonPanelsList()) {
	        	
	            buttonPanel.mouseOver(mouseX - this.guiLeft, mouseY - this.guiTop);
	            
	            buttonPanel.draw();	            
	        }
		}
        
        GlStateManager.popMatrix();
        
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
                
        RenderHelper.enableGUIStandardItemLighting();
        
        GlStateManager.pushMatrix();
        
        GlStateManager.translate((float) i, (float) j, 0.0F);
        
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        
        GlStateManager.enableRescaleNormal();
        
        this.hoveredSlot = null;
        
        this.drawInventorySlots(mouseX, mouseY);

        RenderHelper.disableStandardItemLighting();           
        
        if (this.containerWorkspace != null) {

        	this.drawGuiContainerForegroundLayer(mouseX, mouseY);
        }
        
        GlStateManager.disableRescaleNormal();        
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        
        int slidebarOffset;
        
        float sliderActiveHeight, currentPosition;
    	
    	for (k = 0; k < this.frameworks.size(); k++) {
    		
    		framework = (GUIContainerFramework) this.frameworks.get(k);
			
			framework.mouseOver(mouseX - this.guiLeft, mouseY - this.guiTop);
        	
			if (framework.getSlider() != null) {
				
        		if ((framework.getSearchField() != null && !framework.getSearchField().isDragged()) || framework.getSearchField() == null) {
        			
        			framework.getSlider().mouseOver(mouseX - this.guiLeft, mouseY - this.guiTop);
        			
        			framework.getSlider().mouseClicked();
        			        			
        			if (framework.getSlider().isDragged()) {          			
        				        				        						
        				slidebarOffset = mouseY - framework.getSlider().getSlidebarY() - this.guiTop;
        				
        				sliderActiveHeight = framework.getSlider().getHeight() - framework.getSlider().getSlidebarHeight();
        				
        				currentPosition = mouseY - slidebarOffset - this.yPrevMouse + mouseY - this.guiTop;
        				        				
        				framework.getScroller().setPosition((int) ((float) framework.getScroller().getMaxPosition() * ((currentPosition - framework.getSlider().getY()) / sliderActiveHeight)));       				
        					
        				framework.getSlider().handleSlidebarViaCursor((int) (sliderActiveHeight * (currentPosition / sliderActiveHeight)));
        				
        				this.scrollSlots(framework);    				
        			}
        		}
        		
				framework.getSlider().draw();				
			}	     
						 
	        if (framework.getSearchField() != null) {
	        	
	        	framework.getSearchField().draw();
	        }	
	        
	        for (GUIButton button : framework.getButtonsList()) {
	            
	            button.drawPopup(mouseX - this.guiLeft, mouseY - this.guiTop);
	        }
	        
	        for (GUIButtonPanel buttonPanel : framework.getButtonPanelsList()) {
	            
	            buttonPanel.drawPopup(mouseX - this.guiLeft, mouseY - this.guiTop);
	        }
	        
			if (framework.getContextMenu() != null) {
				
				framework.getContextMenu().mouseOver(mouseX - this.guiLeft, mouseY - this.guiTop);
				
				framework.getContextMenu().draw();
			}
		}
		
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();  
        
        RenderHelper.enableGUIStandardItemLighting();
        
        InventoryPlayer inventoryplayer = this.mc.player.inventory;
        
        ItemStack itemStack = this.draggedStack.isEmpty() ? inventoryplayer.getItemStack() : this.draggedStack;
        
        this.dragged = itemStack;

        if (!itemStack.isEmpty()) {
        	
            int j2 = 8;
            int k2 = this.draggedStack.isEmpty() ? 8 : 16;
            String s = null;

            if (!this.draggedStack.isEmpty() && this.isRightMouseClick) {
            	
                itemStack = itemStack.copy();
                itemStack.setCount(MathHelper.ceil((float)itemStack.getCount() / 2.0F));
            }
            
            else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
            	
                itemStack = itemStack.copy();
                itemStack.setCount(this.dragSplittingRemnant);

                if (itemStack.isEmpty()) {
                	
                    s = "" + TextFormatting.YELLOW + "0";
                }
            }

            this.drawItemStack(itemStack, mouseX - i - 8, mouseY - j - k2, s);
        }

        if (!this.returningStack.isEmpty()) {
        	
            float f = (float) (Minecraft.getSystemTime() - this.returningStackTime) / 100.0F;

            if (f >= 1.0F) {
            	
                f = 1.0F;
                this.returningStack = ItemStack.EMPTY;
            }

            int l2 = this.returningStackDestSlot.xPos - this.touchUpX;
            int i3 = this.returningStackDestSlot.yPos - this.touchUpY;
            int l1 = this.touchUpX + (int)((float) l2 * f);
            int i2 = this.touchUpY + (int)((float) i3 * f);
            
            this.drawItemStack(this.returningStack, l1, i2, (String)null);
        }

        GlStateManager.popMatrix();
        
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        
        RenderHelper.enableStandardItemLighting();
        
		this.yPrevMouse = mouseY;
		
		this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    //TODO drawInventorySlots()
    protected void drawInventorySlots(int mouseX, int mouseY) {
    	
		int k, i;
		
		Slot slot;
				
    	GUIContainerFramework framework;
    	
    	for (k = 0; k < this.frameworks.size(); k++) {
    		
    		framework = (GUIContainerFramework) this.frameworks.get(k);
			
    		for (i = 0; i < framework.visibleSlots; i++) {        	
				
				if (i < framework.slots.size()) {
					
					slot = framework.slots.get(i);
					
					this.drawSlotBottomLayer(slot, framework);
					            
					if (framework.getSlotRenderer() != null) {
						
						framework.getSlotRenderer().drawSlotBottomLayer(slot);

						framework.getSlotRenderer().drawSlot(slot, this.itemRender);
					}
					
					else {
												
						this.drawSlot(slot, framework);
					}
						
					if (this.isMouseOverSlot(slot, mouseX, mouseY, framework) && slot.isEnabled()) {
						
						this.drawSlotHighlighting(slot, framework);
					}
				}
			}
		}
    }
    
    protected void drawSlotBottomLayer(Slot slot, GUIContainerFramework framework) {
        
        this.drawRect(slot.xPos, slot.yPos, slot.xPos + framework.getSlotWidth(), slot.yPos + framework.getSlotHeight(), framework.getSlotBottomLayerColor());
    }
    
    /**
     * Draws the given slot: any item in it, the slot's background, the hovered highlight, etc.
     * @param framework 
     */
    private void drawSlot(Slot slotIn, GUIContainerFramework framework) {
    	
        int i = slotIn.xPos;
        int j = slotIn.yPos;
        
        ItemStack itemstack = slotIn.getStack();
        
        boolean flag = false;
        boolean flag1 = slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && !this.isRightMouseClick;
        ItemStack itemstack1 = this.mc.player.inventory.getItemStack();
        String s = null;

        if (slotIn == this.clickedSlot && !this.draggedStack.isEmpty() && this.isRightMouseClick && !itemstack.isEmpty()) {
        	
            itemstack = itemstack.copy();
            itemstack.setCount(itemstack.getCount() / 2);
        }
        
        else if (this.dragSplitting && this.dragSplittingSlots.contains(slotIn) && !itemstack1.isEmpty()) {
        	
            if (this.dragSplittingSlots.size() == 1) {
            	
                return;
            }

            if (Container.canAddItemToSlot(slotIn, itemstack1, true) && this.inventorySlots.canDragIntoSlot(slotIn)) {
            	
                itemstack = itemstack1.copy();
                flag = true;
                Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack, slotIn.getStack().isEmpty() ? 0 : slotIn.getStack().getCount());
                int k = Math.min(itemstack.getMaxStackSize(), slotIn.getItemStackLimit(itemstack));

                if (itemstack.getCount() > k) {
                	
                    s = TextFormatting.YELLOW.toString() + k;
                    itemstack.setCount(k);
                }
            }
            
            else {
            	
                this.dragSplittingSlots.remove(slotIn);
                this.updateDragSplitting();
            }
        }

        this.zLevel = 100.0F;
        this.itemRender.zLevel = 100.0F;

        if (itemstack.isEmpty() && slotIn.isEnabled()) {
        	
            TextureAtlasSprite textureatlassprite = slotIn.getBackgroundSprite();

            if (textureatlassprite != null) {
            	
                GlStateManager.disableLighting();
                this.mc.getTextureManager().bindTexture(slotIn.getBackgroundLocation());
                this.drawTexturedModalRect(i, j, textureatlassprite, 16, 16);
                GlStateManager.enableLighting();
                flag1 = true;
            }
        }

        if (!flag1) {
        	
            if (flag) {
            	
                drawRect(i, j, i + 16, j + 16, -2130706433);
            }

            GlStateManager.enableDepth();
            this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, itemstack, i, j);
            this.itemRender.renderItemOverlayIntoGUI(this.fontRenderer, itemstack, i, j, s);
        }

        this.itemRender.zLevel = 0.0F;
        this.zLevel = 0.0F;
    }
    
    protected void drawSlotHighlighting(Slot slot, GUIContainerFramework framework) {

		if (!this.menuOpened) {
			
			this.hoveredSlot = slot;
			
			if (framework.getSlotRenderer() != null) {
			
				framework.getSlotRenderer().drawSlotHighlighting(slot);
			}
			
			else {
				
				int j, k;
		        
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();
		    
				j = slot.xPos;
				k = slot.yPos;
		    
				GlStateManager.colorMask(true, true, true, false);
		    
				this.drawGradientRect(j, k, j + 16, k + 16, - 2130706433, - 2130706433);
		    	
		    	GlStateManager.colorMask(true, true, true, true);
		    
		    	GlStateManager.enableLighting();
		    	GlStateManager.enableDepth();
			}
		}
    }
    
    //TODO handleMouseInput()
    public void handleMouseInput() throws IOException {
    	
    	super.handleMouseInput();
    	
    	if (Mouse.getDWheel() != 0) {
    		
    		int k;
    		
        	GUIContainerFramework framework;
        	
        	for (k = 0; k < this.frameworks.size(); k++) {
        		
        		framework = (GUIContainerFramework) this.frameworks.get(k);

    			if (framework.getScroller() != null) {   
    				    					
    	    		if (framework.isHovered() || framework.getScroller().shouldIgnoreBorders()) {
    	    			    				    					    	    			
    	    			if (framework.getScroller().handleScroller()) {
    	    				  	    				        				        					
    	    				this.scrollSlots(framework);
    	    				        	    			
    	    				if (framework.getSlider() != null) {
        							
    	    					framework.getSlider().handleSlidebarViaScroller();
    	    				}
    	    			}
    				}
    			}
    		}
    	}
    	
    	if (!Mouse.isButtonDown(0)) {
    		
    		int k;
    			
        	GUIContainerFramework framework;
        	
        	for (k = 0; k < this.frameworks.size(); k++) {
        		
        		framework = (GUIContainerFramework) this.frameworks.get(k);
    	  			
				if (framework.getSlider() != null) {
					
					if (framework.getSlider().isDragged()) {
    	  			
						framework.getSlider().setSlidebarNotDragged(); 	  		
    	  			}
				}
    		}
    	}
    }
    
    //TODO scrollSlots()
    protected void scrollSlots(GUIContainerFramework framework) {
    	
		int i = 0, size, k;
    	
    	Slot slot, slotCopy;
    	    	
    	framework.slots.clear();
    	framework.slotsIndexes.clear();
    	
    	if (framework.getScroller().scrollerType == GUIScrollerType.STANDARD) {
    		
    		for (i = framework.getScroller().getPosition() * framework.columns; i < framework.getScroller().getPosition() * framework.columns + framework.visibleSlots; i++) {
    		
    			if (i < framework.slotsBuffer.size()) {
    			
    				slot = framework.slotsBuffer.get(i);  
    			
    				slotCopy = this.copySlot(slot); 
            	            	
    				size = framework.slots.size();
    				
    				if (framework.slotsPosition == GUIPosition.CUSTOM) {
					
    					k = size / framework.columns;
					
    					slotCopy.xPos = framework.getXPosition() + size * (framework.getSlotWidth() + framework.getSlotDistanceHorizontal()) - k * ((framework.getSlotWidth() + framework.getSlotDistanceHorizontal()) * framework.columns);
    					slotCopy.yPos = framework.getYPosition() + k * (framework.getSlotHeight() + framework.getSlotDistanceVertical()) - (size / framework.visibleSlots) * (framework.rows * (framework.getSlotHeight() + framework.getSlotDistanceVertical()));
    				}
				
    				framework.slots.add(slotCopy);
    				framework.slotsIndexes.add(framework.indexesBuffer.get(i));
    			}
    		}
    	}
    	
    	else {
    		
    		//TODO Handle smooth scroller
    	}
    }

    protected void renderHoveredToolTip(int p_191948_1_, int p_191948_2_) {
    	
        if (this.mc.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
        	
            this.renderToolTip(this.hoveredSlot.getStack(), p_191948_1_, p_191948_2_);
        }
    }

    /**
     * Draws an ItemStack.
     *  
     * The z index is increased by 32 (and not decreased afterwards), and the item is then rendered at z=200.
     */
    private void drawItemStack(ItemStack stack, int x, int y, String altText) {
    	
        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRenderer;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y - (this.draggedStack.isEmpty() ? 0 : 8), altText);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {}

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {}

    private void updateDragSplitting() {
    	
        ItemStack itemstack = this.mc.player.inventory.getItemStack();

        if (!itemstack.isEmpty() && this.dragSplitting) {
        	
            if (this.dragSplittingLimit == 2) {
            	
                this.dragSplittingRemnant = itemstack.getMaxStackSize();
            }
            
            else {
            	
                this.dragSplittingRemnant = itemstack.getCount();

                for (Slot slot : this.dragSplittingSlots) {
                	
                    ItemStack itemstack1 = itemstack.copy();
                    ItemStack itemstack2 = slot.getStack();
                    int i = itemstack2.isEmpty() ? 0 : itemstack2.getCount();
                    Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, itemstack1, i);
                    int j = Math.min(itemstack1.getMaxStackSize(), slot.getItemStackLimit(itemstack1));

                    if (itemstack1.getCount() > j) {
                    	
                        itemstack1.setCount(j);
                    }

                    this.dragSplittingRemnant -= itemstack1.getCount() - i;
                }
            }
        }
    }

    //TODO getSlotAtPosition()
    /**
     * Returns the slot at the given coordinates or null if there is none.
     */
    //TODO getSlotAtPosition()
    protected Slot getSlotAtPosition(int mouseX, int mouseY, boolean flag) {
    	
    	int k, i;
    	
    	Slot slot;	
    	
    	GUIContainerFramework framework;
    	
    	for (k = 0; k < this.frameworks.size(); k++) {
    		
    		framework = (GUIContainerFramework) this.frameworks.get(k);
    		
    		if (!this.menuOpened || flag) {
    			
        		for (i = 0; i < framework.visibleSlots; i++) {        	
    				
        			if (i < framework.slots.size()) {
        				
        				slot = framework.slots.get(i);    
    				
        				if (this.isMouseOverSlot(slot, mouseX, mouseY, framework)) {
    					
        					if (flag) {
        						
        						this.righClickedFramework = this.frameworks.indexOf(framework);
        					}
    		        	
        					return (Slot) this.inventorySlots.inventorySlots.get(framework.slotsIndexes.get(i));
        				} 
        			}
    			}
    		}
    	}
    	
        return null;
    }
    
    //TODO handleRightClick()
    protected boolean handleRightClick(int mouseX, int mouseY) {
    	
    	Slot slot = this.getSlotAtPosition(mouseX, mouseY, true);
			
		if (this.dragged != ItemStack.EMPTY && this.dragged.getItem() != Items.AIR) {
			    		
			return false;
		}
    	    	
    	if (slot != null && slot.getHasStack()) {

    		GUIContainerFramework righClickedFramework = (GUIContainerFramework) this.frameworks.get(this.righClickedFramework);
    		
    		if (righClickedFramework.getContextMenu() != null) {
    			
    			if (!this.menuOpened) {
    				
    				righClickedFramework.getContextMenu().open(righClickedFramework.inventory, mouseX - this.guiLeft, mouseY - this.guiTop, slot);    				
    			}  
    			
    			else {
    				
    				((GUIContainerFramework) this.frameworks.get(this.openedMenuFramework)).getContextMenu().close();
    				
    				righClickedFramework.getContextMenu().open(righClickedFramework.inventory, mouseX - this.guiLeft, mouseY - this.guiTop, slot);   
    			}
    			    			
				this.openedMenuFramework = this.righClickedFramework;
				
    			this.menuOpened = true;
    			
 				return true;
    		}  
    	}   
    	
    	return false;
    }

    //TODO mouseClicked()
    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    	
        super.mouseClicked(mouseX, mouseY, mouseButton);
        boolean flag = this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(mouseButton - 100);
        Slot slot = this.getSlotAtPosition(mouseX, mouseY, false);
        long i = Minecraft.getSystemTime();
        this.doubleClick = this.lastClickSlot == slot && i - this.lastClickTime < 250L && this.lastClickButton == mouseButton;
        this.ignoreMouseUp = false;
        
        int rightButton = 0;
        
        if (mouseButton == 1) {
        	
        	if (!this.handleRightClick(mouseX, mouseY)) {
        		
        		rightButton = 1;
        	}
        }

        if (mouseButton == 0 || mouseButton == rightButton || flag) {
        	
            int j = this.guiLeft;
            int k = this.guiTop;
            boolean flag1 = this.hasClickedOutside(mouseX, mouseY, j, k);
            if (slot != null) flag1 = false; // Forge, prevent dropping of items through slots outside of GUI boundaries
            int l = - 1;

            if (slot != null) {
            	
                l = slot.slotNumber;
            }

            if (flag1) {
            	
                l = - 999;
            }

            if (this.mc.gameSettings.touchscreen && flag1 && this.mc.player.inventory.getItemStack().isEmpty()) {
            	
                this.mc.displayGuiScreen((GuiScreen) null);
                return;
            }

            if (l != - 1) {
            	
                if (this.mc.gameSettings.touchscreen) {
                	
                    if (slot != null && slot.getHasStack()) {
                    	
                        this.clickedSlot = slot;
                        this.draggedStack = ItemStack.EMPTY;
                        this.isRightMouseClick = mouseButton == 1;
                    }
                    
                    else {
                    	
                        this.clickedSlot = null;
                    }
                }
                
                else if (!this.dragSplitting) {
                	
                    if (this.mc.player.inventory.getItemStack().isEmpty()) {
                    	
                        if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(mouseButton - 100)) {
                        	
                            this.handleMouseClick(slot, l, mouseButton, ClickType.CLONE);
                        }
                        
                        else {
                        	
                            boolean flag2 = l != - 999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
                            ClickType clicktype = ClickType.PICKUP;

                            if (flag2) {
                            	
                                this.shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack().copy() : ItemStack.EMPTY;
                                clicktype = ClickType.QUICK_MOVE;
                            }
                            
                            else if (l == - 999) {
                            	
                                clicktype = ClickType.THROW;
                            }

                            this.handleMouseClick(slot, l, mouseButton, clicktype);
                        }

                        this.ignoreMouseUp = true;
                    }
                    
                    else {
                    	
                        this.dragSplitting = true;
                        this.dragSplittingButton = mouseButton;
                        this.dragSplittingSlots.clear();

                        if (mouseButton == 0) {
                        	
                            this.dragSplittingLimit = 0;
                        }
                        
                        else if (mouseButton == 1) {
                        	
                            this.dragSplittingLimit = 1;
                        }
                        
                        else if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(mouseButton - 100)) {
                        	
                            this.dragSplittingLimit = 2;
                        }
                    }
                }
            }
        }

        this.lastClickSlot = slot;
        this.lastClickTime = i;
        this.lastClickButton = mouseButton;
        
        if (mouseButton == 0) {
        	
        	int k;
        	
        	GUIContainerFramework framework;
        	
        	for (k = 0; k < this.frameworks.size(); k++) {
        		
        		framework = (GUIContainerFramework) this.frameworks.get(k);
        	      	
        		if (framework.getSearchField() != null) {       	
        		
        			framework.getSearchField().searchFieldClicked(mouseX - this.guiLeft, mouseY - this.guiTop);
                }        
            	
            	if (framework.getContextMenu() != null && !framework.getContextMenu().mousePressed(framework.inventory, mouseX, mouseY)) {
            		
            		framework.getContextMenu().close();
            		
            		this.menuOpened = false;
            	}
            	
            	for (GUIButton button : framework.getButtonsList()) {
            		
            		if (button.mouseClicked()) this.handleButtonPress(framework, button);
            	}
            	
            	for (GUIButtonPanel buttonPanel : framework.getButtonPanelsList()) {
            		
            		for (GUIButton button : buttonPanel.buttons) {
            			
                		if (button.mouseClicked()) this.handleButtonPress(framework, button);
            		}
            	}
        	}
        }
    }

    protected boolean hasClickedOutside(int p_193983_1_, int p_193983_2_, int p_193983_3_, int p_193983_4_) {
    	
        return p_193983_1_ < p_193983_3_ || p_193983_2_ < p_193983_4_ || p_193983_1_ >= p_193983_3_ + this.xSize || p_193983_2_ >= p_193983_4_ + this.ySize;
    }

    /**
     * Called when a mouse button is pressed and the mouse is moved around. Parameters are : mouseX, mouseY,
     * lastButtonClicked & timeSinceMouseClick.
     */
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    	
        Slot slot = this.getSlotAtPosition(mouseX, mouseY, false);
        ItemStack itemstack = this.mc.player.inventory.getItemStack();

        if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
        	
            if (clickedMouseButton == 0 || clickedMouseButton == 1) {
            	
                if (this.draggedStack.isEmpty()) {
                	
                    if (slot != this.clickedSlot && !this.clickedSlot.getStack().isEmpty()) {
                    	
                        this.draggedStack = this.clickedSlot.getStack().copy();                     
                    }
                }
                
                else if (this.draggedStack.getCount() > 1 && slot != null && Container.canAddItemToSlot(slot, this.draggedStack, false)) {
                	
                    long i = Minecraft.getSystemTime();

                    if (this.currentDragTargetSlot == slot) {
                    	
                        if (i - this.dragItemDropDelay > 500L) {
                        	
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, ClickType.PICKUP);
                            this.handleMouseClick(slot, slot.slotNumber, 1, ClickType.PICKUP);
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, ClickType.PICKUP);
                            this.dragItemDropDelay = i + 750L;
                            this.draggedStack.shrink(1);
                        }
                    }
                    
                    else {
                    	
                        this.currentDragTargetSlot = slot;
                        this.dragItemDropDelay = i;
                    }
                }
            }
        }
        
        else if (this.dragSplitting && slot != null && !itemstack.isEmpty() && (itemstack.getCount() > this.dragSplittingSlots.size() || this.dragSplittingLimit == 2) && Container.canAddItemToSlot(slot, itemstack, true) && slot.isItemValid(itemstack) && this.inventorySlots.canDragIntoSlot(slot)) {
            
        	this.dragSplittingSlots.add(slot);
            this.updateDragSplitting();
        }
    }

    /**
     * Called when a mouse button is released.
     */
    protected void mouseReleased(int mouseX, int mouseY, int state) {
    	
        super.mouseReleased(mouseX, mouseY, state); //Forge, Call parent to release buttons
        Slot slot = this.getSlotAtPosition(mouseX, mouseY, false);
        int i = this.guiLeft;
        int j = this.guiTop;
        boolean flag = this.hasClickedOutside(mouseX, mouseY, i, j);
        if (slot != null) flag = false; // Forge, prevent dropping of items through slots outside of GUI boundaries
        int k = -1;

        if (slot != null) {
        	
            k = slot.slotNumber;
        }

        if (flag) {
        	
            k = - 999;
        }

        if (this.doubleClick && slot != null && state == 0 && this.inventorySlots.canMergeSlot(ItemStack.EMPTY, slot)) {
        	
            if (isShiftKeyDown()) {
            	
                if (!this.shiftClickedSlot.isEmpty()) {
                	
                    for (Slot slot2 : this.inventorySlots.inventorySlots) {
                    	
                        if (slot2 != null && slot2.canTakeStack(this.mc.player) && slot2.getHasStack() && slot2.isSameInventory(slot) && Container.canAddItemToSlot(slot2, this.shiftClickedSlot, true)) {
                        	
                            this.handleMouseClick(slot2, slot2.slotNumber, state, ClickType.QUICK_MOVE);
                        }
                    }
                }
            }
            
            else {
            	
                this.handleMouseClick(slot, k, state, ClickType.PICKUP_ALL);
            }

            this.doubleClick = false;
            this.lastClickTime = 0L;
        }
        
        else {
        	
            if (this.dragSplitting && this.dragSplittingButton != state) {
            	
                this.dragSplitting = false;
                this.dragSplittingSlots.clear();
                this.ignoreMouseUp = true;
                return;
            }

            if (this.ignoreMouseUp) {
            	
                this.ignoreMouseUp = false;
                return;
            }

            if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {
            	
                if (state == 0 || state == 1) {
                	
                    if (this.draggedStack.isEmpty() && slot != this.clickedSlot) {
                    	
                        this.draggedStack = this.clickedSlot.getStack();
                    }

                    boolean flag2 = Container.canAddItemToSlot(slot, this.draggedStack, false);

                    if (k != - 1 && !this.draggedStack.isEmpty() && flag2) {
                    	
                        this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, ClickType.PICKUP);
                        this.handleMouseClick(slot, k, 0, ClickType.PICKUP);

                        if (this.mc.player.inventory.getItemStack().isEmpty()) {
                        	
                            this.returningStack = ItemStack.EMPTY;
                        }
                        
                        else {
                        	
                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, ClickType.PICKUP);
                            this.touchUpX = mouseX - i;
                            this.touchUpY = mouseY - j;
                            this.returningStackDestSlot = this.clickedSlot;
                            this.returningStack = this.draggedStack;
                            this.returningStackTime = Minecraft.getSystemTime();
                        }
                    }
                    
                    else if (!this.draggedStack.isEmpty()) {
                    	
                        this.touchUpX = mouseX - i;
                        this.touchUpY = mouseY - j;
                        this.returningStackDestSlot = this.clickedSlot;
                        this.returningStack = this.draggedStack;
                        this.returningStackTime = Minecraft.getSystemTime();
                    }

                    this.draggedStack = ItemStack.EMPTY;
                    this.clickedSlot = null;
                }
            }
            
            else if (this.dragSplitting && !this.dragSplittingSlots.isEmpty()) {
            	
                this.handleMouseClick((Slot)null, - 999, Container.getQuickcraftMask(0, this.dragSplittingLimit), ClickType.QUICK_CRAFT);

                for (Slot slot1 : this.dragSplittingSlots) {
                	
                    this.handleMouseClick(slot1, slot1.slotNumber, Container.getQuickcraftMask(1, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
                }

                this.handleMouseClick((Slot)null, - 999, Container.getQuickcraftMask(2, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
            }
            
            else if (!this.mc.player.inventory.getItemStack().isEmpty()) {
            	
                if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(state - 100)) {
                	
                    this.handleMouseClick(slot, k, state, ClickType.CLONE);
                }
                
                else {
                	
                    boolean flag1 = k != - 999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));

                    if (flag1) {
                    	
                        this.shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack().copy() : ItemStack.EMPTY;
                    }

                    this.handleMouseClick(slot, k, state, flag1 ? ClickType.QUICK_MOVE : ClickType.PICKUP);
                }
            }
        }

        if (this.mc.player.inventory.getItemStack().isEmpty()) {
        	
            this.lastClickTime = 0L;
        }

        this.dragSplitting = false;
    }

    /**
     * Returns whether the mouse is over the given slot.
     * @param framework 
     */
    private boolean isMouseOverSlot(Slot slot, int mouseX, int mouseY, GUIContainerFramework framework) {
    	
        return this.isPointInRegion(slot.xPos, slot.yPos, framework.getSlotWidth(), framework.getSlotHeight(), mouseX, mouseY);
    }

    /**
     * Test if the 2D point is in a rectangle (relative to the GUI). Args : rectX, rectY, rectWidth, rectHeight, pointX,
     * pointY
     */
    protected boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY) {
    	
        int i = this.guiLeft;
        int j = this.guiTop;
        
        pointX = pointX - i;
        pointY = pointY - j;
        
        return pointX >= rectX - 1 && pointX < rectX + rectWidth + 1 && pointY >= rectY - 1 && pointY < rectY + rectHeight + 1;
    }

    //TODO handleMouseClick()
    /**
     * Called when the mouse is clicked over a slot or outside the gui.
     */
    protected void handleMouseClick(Slot slot, int slotId, int mouseButton, ClickType type) {
    	
        if (slot != null) {
        	
            slotId = slot.slotNumber;
        }

        if (this.handleClickByType(type, slot, slotId, mouseButton)) {
        	
        	this.mc.playerController.windowClick(this.inventorySlots.windowId, slotId, mouseButton, type, this.mc.player);
        }
        
    	int k;
    	
    	GUIContainerFramework framework;
    	
    	for (k = 0; k < this.frameworks.size(); k++) {
    		
    		framework = (GUIContainerFramework) this.frameworks.get(k);
       	
        	if (framework.getForcedToUpdate()) {
        		
        		this.updateFramework(framework, framework.getCurrentSorter());
        	}
        }
    }
    
    /**
     * Позволяет отловить и клик по слоту в ГПИ и обработать его самостоятельно.
     * 
     * @param type тип клика
     * @param slot
     * @param slotId
     * @param mouseButton
     * 
     * @return true для продолжения обработки ванилью.
     */
    protected boolean handleClickByType(ClickType type, Slot slot, int slotId, int mouseButton) {
    	
    	return true;
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    	
        if (keyCode == 1) {
        	
            this.mc.player.closeScreen();
        }

        this.checkHotbarKeys(keyCode);

        if (this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
        	
            if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(keyCode)) {
            	
                this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, 0, ClickType.CLONE);
            }
            
            else if (this.mc.gameSettings.keyBindDrop.isActiveAndMatches(keyCode)) {
            	
                this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, ClickType.THROW);
            }
        }
        
    	int k;
    	
    	GUIContainerFramework framework;
    	
    	for (k = 0; k < this.frameworks.size(); k++) {
    		
    		framework = (GUIContainerFramework) this.frameworks.get(k);
       	
        	if (framework.getSearchField() != null) {       	      
        		
            	if (framework.getSearchField().keyTyped(typedChar, keyCode)) {
            		
            		if (framework.getSearchField().getTypedText().length() > 0) {
        			
            			this.updateSearchResult(framework);
            		}
        		
            		else {
            			
            			framework.searchSlots.clear();
            			framework.searchIndexes.clear();
        			
            			this.updateFramework(framework, framework.getCurrentSorter());
            		}
        		
            		if (framework.getSlider() != null) {
            			
            			framework.getSlider().reset();
            		}
            	}
        	}
        }
    }
    
    //TODO updateSearchResult()
    protected void updateSearchResult(GUIContainerFramework framework) {
    	
        int slotIndex, size, k;
        
        Slot slotCopy;
        
        ItemStack itemStack;
        
        String itemName;
        
    	String typedText = framework.getSearchField().getTypedText();
        
    	Iterator itemsIterator = framework.items.keySet().iterator();
    	
    	framework.slots.clear();
    	framework.slotsIndexes.clear();   
    	
    	framework.searchSlots.clear();
    	framework.searchIndexes.clear();   
        
        while (itemsIterator.hasNext()) {
        	
        	slotIndex = (Integer) itemsIterator.next();
        	
        	itemStack = framework.items.get(slotIndex);
        	
        	if (itemStack != null) {
        	        	
        		itemName = itemStack.getDisplayName().toLowerCase();
        	
        		if (itemName.startsWith(typedText) || itemName.contains(" " + typedText)) {
        		        					        			
        			size = framework.searchSlots.size();
        					
        			slotCopy = this.copySlot(framework.slotsBuffer.get(slotIndex));
        					        					
					if (framework.slotsPosition == GUIPosition.CUSTOM) {
						
						k = size / framework.columns;
						
						slotCopy.xPos = framework.getXPosition() + size * (framework.getSlotWidth() + framework.getSlotDistanceHorizontal()) - k * ((framework.getSlotWidth() + framework.getSlotDistanceHorizontal()) * framework.columns);
						slotCopy.yPos = framework.getYPosition() + k * (framework.getSlotHeight() + framework.getSlotDistanceVertical()) - (size / framework.visibleSlots) * (framework.rows * (framework.getSlotHeight() + framework.getSlotDistanceVertical()));
					}
					
        			framework.slots.add(slotCopy);
        			framework.slotsIndexes.add(framework.indexesBuffer.get(slotIndex));  
					
        			framework.searchSlots.add(slotCopy);
        			framework.searchIndexes.add(framework.indexesBuffer.get(slotIndex));  
        		}
        	}
        }
    }

    /**
     * Checks whether a hotbar key (to swap the hovered item with an item in the hotbar) has been pressed. If so, it
     * swaps the given items.
     * Returns true if a hotbar key was pressed.
     */
    protected boolean checkHotbarKeys(int keyCode) {
    	
        if (this.mc.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null) {
        	
            for (int i = 0; i < 9; ++i) {
            	
                if (this.mc.gameSettings.keyBindsHotbar[i].isActiveAndMatches(keyCode)) {
                	
                    this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, i, ClickType.SWAP);
                    
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed() {
    	
        if (this.mc.player != null) {
        	
            this.inventorySlots.onContainerClosed(this.mc.player);
        }
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame() {
    	
        return false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
    	
        super.updateScreen();

        if (!this.mc.player.isEntityAlive() || this.mc.player.isDead) {
        	
            this.mc.player.closeScreen();
        }
        
    	int k;
    	
    	GUIContainerFramework framework;
    	
    	for (k = 0; k < this.frameworks.size(); k++) {
    		
    		framework = (GUIContainerFramework) this.frameworks.get(k);
	    	
	    	if (framework.getSearchField() != null) {
	    		
	    		framework.getSearchField().updateCursorCounter();
	    	}
	    }
    }

    /* ======================================== FORGE START =====================================*/

    /**
     * Returns the slot that is currently displayed under the mouse.
     */
    @javax.annotation.Nullable
    public Slot getSlotUnderMouse() { return this.hoveredSlot; }
    public int getGuiLeft() { return guiLeft; }
    public int getGuiTop() { return guiTop; }
    public int getXSize() { return xSize; }
    public int getYSize() { return ySize; }

    /* ======================================== FORGE END   =====================================*/
}
