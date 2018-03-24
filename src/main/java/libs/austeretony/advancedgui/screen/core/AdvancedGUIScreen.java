package libs.austeretony.advancedgui.screen.core;

import java.io.IOException;

import org.lwjgl.input.Mouse;

import libs.austeretony.advancedgui.screen.browsing.GUIScroller.GUIEnumScrollerType;
import libs.austeretony.advancedgui.screen.button.GUIButton;
import libs.austeretony.advancedgui.screen.framework.GUIFramework;
import libs.austeretony.advancedgui.screen.panel.GUIButtonPanel;
import libs.austeretony.advancedgui.screen.panel.GUIButtonPanel.GUIEnumOrientation;
import libs.austeretony.advancedgui.screen.text.GUITextField;
import libs.austeretony.advancedgui.screen.text.GUITextLabel;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Модернизированный GuiScreen. Должен быть унаследован вашим ГПИ простого интерфейса.
 * ВНИМАНИЕ! Это ранняя версия, функционал не гарантируется.
 */
@SideOnly(Side.CLIENT)
public abstract class AdvancedGUIScreen<T extends AdvancedGUIScreen> extends GuiScreen {
    
    protected int 
    xSize = 176,
    ySize = 166,
    guiLeft, guiTop;
    
    /** Рабочее пространство */
    private GUIWorkspace workspace;
    
    protected boolean workspaceCreated, isScrolling;
    
	private int yPrevMouse;
		
    public void initGui() {
    	    	
        this.workspace = this.initWorkspace();
        
        this.workspace.setCurrentSection(this.getDefaultSection());
                
        this.xSize = this.workspace.getWidth();
        this.ySize = this.workspace.getHeight();
                
        this.guiLeft = this.workspace.getX();
        this.guiTop = this.workspace.getY();  
        
        this.workspaceCreated = true;
    	
    	this.init();
    }
    
    private void init() {
    	
    	this.initSections();
    	
    	this.getWorkspace().getSections().forEach(section -> section.init());
    }
    
    /**
     * Используется для инициализпции GUIWorkspace рабочего пространства ГПИ.
     * Инициализацию  НЕОБХОДИМО производить здесь при активном {@link GUIWorkspace#allowUpdate()}.
     * Используйте createSection() для инициализации новых секций.
     * 
     * @return настроенный GUIWorkspace для этого ГПИ
     */
    protected abstract GUIWorkspace initWorkspace();
    
    /**
     * Определяет раздел, отображающийся при открытии ГПИ.
     * 
     * @return инициализированный GUISection
     */
    protected abstract GUISection getDefaultSection();
    
    /**
     * Используется для наполнения объектов GUISection (разделов). Инициализировать здесь  
     * разделы можно только в том случае, если {@link GUIWorkspace#allowUpdate()} 
     * не установлен.
     */
    protected abstract void initSections();
    
    protected GUIWorkspace getWorkspace() {
    	
    	return this.workspace;
    }
    
    protected boolean isWorkspaceCreated() {
    	
    	return this.workspaceCreated;
    }
    
    /**
     * Для рендера перед рендером фона раздела.
     */
    protected void drawBackgroundLayer(int mouseX, int mouseY, float partialTicks) {}
    
    /**
     * Для рендера после рендера фона раздела.
     */
    protected void drawForegroundLayer(int mouseX, int mouseY, float partialTicks) {}

    //TODO drawScreen()
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    	
        if (this.isWorkspaceCreated()) {
    	
        	RenderHelper.disableStandardItemLighting();
        
        	GlStateManager.disableLighting();
        	GlStateManager.disableDepth();   	        	
        	
        	this.getWorkspace().draw();
        	
        	this.drawBackgroundLayer(mouseX, mouseY, partialTicks);
        	        	        	
	        GUISection section = this.getWorkspace().getCurrentSection();
	        
	        section.draw();
	        
        	this.drawForegroundLayer(mouseX, mouseY, partialTicks);
        	
	        GlStateManager.pushMatrix();
	        
	        GlStateManager.translate((float) this.guiLeft, (float) this.guiTop, 0.0F);
	        
	        for (GUITextLabel textLabel : section.getTextLabelsList()) {
	        	
	        	textLabel.mouseOver(mouseX - this.guiLeft, mouseY - this.guiTop);
	            
	        	textLabel.draw();	
	        	
	        	textLabel.drawPopup(mouseX - this.guiLeft, mouseY - this.guiTop);
	        } 
	        
	        for (GUITextField textField : section.getTextFieldsList()) {
	        	
	        	textField.mouseOver(mouseX - this.guiLeft, mouseY - this.guiTop);
	            
	        	textField.draw();	
	        	
	        	textField.drawPopup(mouseX - this.guiLeft, mouseY - this.guiTop);
	        } 
        	
	        for (GUIButton button : section.getButtonsList()) {
	        	        	
	            button.mouseOver(mouseX - this.guiLeft, mouseY - this.guiTop);
	            
	            button.draw();	        
	            
	            button.drawPopup(mouseX - this.guiLeft, mouseY - this.guiTop);
	        }
	        
	        for (GUIButtonPanel buttonPanel : section.getButtonPanelsList()) {
	        	
	            buttonPanel.mouseOver(mouseX - this.guiLeft, mouseY - this.guiTop);
	            
	            buttonPanel.draw();	  
	            
	            buttonPanel.drawPopup(mouseX - this.guiLeft, mouseY - this.guiTop);
	            
				if (buttonPanel.hasScroller() && buttonPanel.getScroller().hasSlider()) {
	    			
					buttonPanel.getScroller().getSlider().mouseOver(mouseX - this.guiLeft, mouseY - this.guiTop);
	        			
					buttonPanel.getScroller().getSlider().mouseClicked(mouseX, mouseY);
					
	        		buttonPanel.getScroller().getSlider().draw();				
	        			        			
					this.handlePanelSlidebar(buttonPanel, mouseY);	        		
				}
	        }    
	        
			for (GUIFramework framework : section.frameworks) {
				
		        for (GUITextField textField : framework.getTextFieldsList()) {
		        	
		        	textField.mouseOver(mouseX - this.guiLeft, mouseY - this.guiTop);
		            
		        	textField.draw();	
		        	
		        	textField.drawPopup(mouseX - this.guiLeft, mouseY - this.guiTop);
		        } 
							
		        for (GUIButton button : framework.getButtonsList()) {
		        	
		            button.mouseOver(mouseX - this.guiLeft, mouseY - this.guiTop);
		            
		            button.draw();	        
		            
		            button.drawPopup(mouseX - this.guiLeft, mouseY - this.guiTop);
		        }	     
		        
		        for (GUIButtonPanel buttonPanel : framework.getButtonPanelsList()) {
		        	
		            buttonPanel.mouseOver(mouseX - this.guiLeft, mouseY - this.guiTop);
		            
		            buttonPanel.draw();	  
		            
		            buttonPanel.drawPopup(mouseX - this.guiLeft, mouseY - this.guiTop);
		            
					if (buttonPanel.hasScroller() && buttonPanel.getScroller().hasSlider()) {
							        			
						buttonPanel.getScroller().getSlider().mouseOver(mouseX - this.guiLeft, mouseY - this.guiTop);
		        			
						buttonPanel.getScroller().getSlider().mouseClicked(mouseX, mouseY);
						
		        		buttonPanel.getScroller().getSlider().draw();	
		        			        			
						this.handlePanelSlidebar(buttonPanel, mouseY);		        					
					}
		        }	               
			}
			
	        GlStateManager.popMatrix();
						
	        GlStateManager.enableLighting();
	        GlStateManager.enableDepth();
	                
	        RenderHelper.enableGUIStandardItemLighting();	        
	        
	        this.yPrevMouse = mouseY;
        }              
    }
    
    protected void handlePanelSlidebar(GUIButtonPanel buttonPanel, int mouseY) {
    	
        int slidebarOffset;
        
        float sliderActiveHeight, currentPosition;
    	
		if (buttonPanel.getScroller().getSlider().isDragged()) {          			
			
			slidebarOffset = mouseY - buttonPanel.getScroller().getSlider().getSlidebarY() - this.guiTop;
				
			sliderActiveHeight = buttonPanel.getScroller().getSlider().getHeight() - buttonPanel.getScroller().getSlider().getSlidebarHeight();
				
			currentPosition = mouseY - slidebarOffset - this.yPrevMouse + mouseY - this.guiTop;
				        				
			buttonPanel.getScroller().setPosition((int) ((float) buttonPanel.getScroller().getMaxPosition() * ((currentPosition - buttonPanel.getScroller().getSlider().getY()) / sliderActiveHeight)));       				
					
			buttonPanel.getScroller().getSlider().handleSlidebarViaCursor((int) (sliderActiveHeight * (currentPosition / sliderActiveHeight)));
				
			this.scrollButtons(buttonPanel);    				
		}
    }
         
    /**
     * Метод для управления активностью кнопок. Если кнопка не относится к определённому фреймворку
     * GUIFramework будет null.
     * 
     * @param section раздел, к которому относится кнопка
     * @param framework фреймворк, к которому относится активированная кнопка
     * @param button кнопка, которая была активирована
     */
    protected abstract void handleButtonPress(GUISection section, GUIFramework framework, GUIButton button);

    //TODO mouseClicked()
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    	
        if (mouseButton == 0) {
        	
            for (int i = 0; i < this.buttonList.size(); ++i) {
            	
                GuiButton guibutton = this.buttonList.get(i);

                if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
                	
                    net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre event = new net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Pre(this, guibutton, this.buttonList);
                    if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event))
                        break;
                    guibutton = event.getButton();
                    this.selectedButton = guibutton;
                    guibutton.playPressSound(this.mc.getSoundHandler());
                    this.actionPerformed(guibutton);
                    if (this.equals(this.mc.currentScreen))
                        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent.Post(this, event.getButton(), this.buttonList));
                }
            }
        }
        
	    GUISection section = this.getWorkspace().getCurrentSection();
        
	    if (mouseButton == 0) {
	        	
	        for (GUITextField textField : section.getTextFieldsList()) {
	        		
	        	textField.mouseClicked(mouseX, mouseY);
	        }
	        	
	        for (GUIButton button : section.getButtonsList()) {
	        		
	        	if (button.mouseClicked(mouseX, mouseY)) {
	        		
            		if (button.hasSound()) {
            			
            			this.mc.player.playSound(button.getSound().soundEvent, button.getSound().volume, button.getSound().pitch);
            		}
	        		
	        		this.handleButtonPress(section, null, button);
	        	
	        		section.handleButtonPress(section, null, button);
	        	}
	        }
	        	
	        for (GUIButtonPanel buttonPanel : section.getButtonPanelsList()) {
	        		
	        	for (GUIButton button : buttonPanel.visibleButtons) {
	        			
	            	if (button.mouseClicked(mouseX, mouseY)) {
	            		
	            		if (button.hasSound()) {
	            			
	            			this.mc.player.playSound(button.getSound().soundEvent, button.getSound().volume, button.getSound().pitch);
	            		}
	            		
	            		this.handleButtonPress(section, null, button);
	            	
		        		section.handleButtonPress(section, null, button);
	            	}
	        	}
	        }
	        	      	
	        for (GUIFramework framework : section.frameworks) {
	        		        	      	
		        for (GUITextField textField : framework.getTextFieldsList()) {
		        		
		        	textField.mouseClicked(mouseX, mouseY);
		        }
	        		
	            for (GUIButton button : framework.getButtonsList()) {
	            		
	            	if (button.mouseClicked(mouseX, mouseY)) {
	            		
	            		if (button.hasSound()) {
	            			
	            			this.mc.player.playSound(button.getSound().soundEvent, button.getSound().volume, button.getSound().pitch);
	            		}
	            		
	            		this.handleButtonPress(section, framework, button);
	            		
		        		section.handleButtonPress(section, framework, button);
	            	}
	            }
	            	
	            for (GUIButtonPanel buttonPanel : framework.getButtonPanelsList()) {
	            		
	            	for (GUIButton button : buttonPanel.visibleButtons) {
	            			
	                	if (button.mouseClicked(mouseX, mouseY)) {
	                		
		            		if (button.hasSound()) {
		            			
		            			this.mc.player.playSound(button.getSound().soundEvent, button.getSound().volume, button.getSound().pitch);
		            		}
	                		
	                		this.handleButtonPress(section, framework, button);
	                	
			        		section.handleButtonPress(section, framework, button);
	                	}
	            	}
	            }
	        }   
        }
    }
    
    //TODO keyTyped()
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    	
        if (keyCode == 1) {
        	
            this.mc.displayGuiScreen((GuiScreen) null);

            if (this.mc.currentScreen == null) {
            	
                this.mc.setIngameFocus();
            }
        }
            		
    	GUISection section = this.getWorkspace().getCurrentSection();    
    		
    	for (GUITextField textField : section.getTextFieldsList()) {
    			
    		textField.keyTyped(typedChar, keyCode);
    	}
        	
        for (GUIFramework frame : this.getWorkspace().getCurrentSection().frameworks) {       		    	    	

        	for (GUITextField textField : frame.getTextFieldsList()) {
        			
        		textField.keyTyped(typedChar, keyCode);
    	    }
    	}
    }

    //TODO handleMouseInput()
    public void handleMouseInput() throws IOException {
    	
    	super.handleMouseInput();
                	
        this.isScrolling = Mouse.getDWheel() != 0;
        
        if (this.isWorkspaceCreated()) {
        	
	        GUISection section = this.workspace.getCurrentSection();
	        
	    	if (this.isScrolling) {   
	    		
	        	for (GUIButtonPanel buttonPanel : section.getButtonPanelsList()) {
	        		
	    			if (buttonPanel.hasScroller()) {   
	    									
	    	    		if (buttonPanel.isHovered() || buttonPanel.getScroller().shouldIgnoreBorders()) {
	    	    			    	    			    				    					    	    			
	    	    			if (buttonPanel.getScroller().handleScroller()) {
	    	    				    	    				  	    		
	    	    				this.scrollButtons(buttonPanel);
	    	    				        	    			
	    	    				if (buttonPanel.hasScroller() && buttonPanel.getScroller().hasSlider()) {
	        							
	    	    					buttonPanel.getScroller().getSlider().handleSlidebarViaScroller();
	    	    				}
	    	    			}
	    				}
	    			}
	        	}
	    		        	
	        	for (GUIFramework framework : section.frameworks) {
	        		
	            	for (GUIButtonPanel buttonPanel : framework.getButtonPanelsList()) {
	
	        			if (buttonPanel.hasScroller()) {   
	    					
	        	    		if (buttonPanel.isHovered() || buttonPanel.getScroller().shouldIgnoreBorders()) {
	        	    			    				    					    	    			
	        	    			if (buttonPanel.getScroller().handleScroller()) {
	        	    				  	    		
	        	    				this.scrollButtons(buttonPanel);
	        	    				        	    			
	        	    				if (buttonPanel.getScroller() != null && buttonPanel.getScroller().getSlider() != null) {
	            							
	        	    					buttonPanel.getScroller().getSlider().handleSlidebarViaScroller();
	        	    				}
	        	    			}
	        				}
	        			}
	            	}
	    		}
	    	}
	    	
	    	if (!Mouse.isButtonDown(0)) {
	    		
	        	for (GUIButtonPanel buttonPanel : section.getButtonPanelsList()) {
	        		
					if (buttonPanel.hasScroller() && buttonPanel.getScroller().hasSlider()) {
						
						if (buttonPanel.getScroller().getSlider().isDragged()) {
							    	  			
							buttonPanel.getScroller().getSlider().setSlidebarNotDragged(); 	  		
	    	  			}
					}
	        	}
	       	        	
	        	for (GUIFramework framework : section.frameworks) {
	        		
	            	for (GUIButtonPanel buttonPanel : framework.getButtonPanelsList()) {
	            		
	    				if (buttonPanel.hasScroller() && buttonPanel.getScroller().hasSlider()) {
	    					
	    					if (buttonPanel.getScroller().getSlider().isDragged()) {
	    						    	  			
	    						buttonPanel.getScroller().getSlider().setSlidebarNotDragged(); 	  		
	        	  			}
	    				}
	            	}       		        		        	
	        	}
	    	}
        }
    }
    
    //TODO scrollButtons()
    protected void scrollButtons(GUIButtonPanel panel) {
    	
		int i = 0, size;
    	
    	GUIButton button, buttonCopy;
    	    	
    	panel.visibleButtons.clear();
    	
    	if (panel.getScroller().scrollerType == GUIEnumScrollerType.STANDARD) {
    		
    		for (i = panel.getScroller().getPosition(); i < panel.getScroller().getPosition() + panel.getScroller().rowsVisible; i++) {
    		
    			if (i < panel.buttonsBuffer.size()) {
    			
    				button = panel.buttonsBuffer.get(i);  
    			            	            	
    				size = panel.visibleButtons.size();
					    				
    				button.setPosition(panel.orientation == GUIEnumOrientation.HORIZONTAL ? panel.getX() + size * (panel.getButtonWidth() + panel.getButtonsOffset()) - (size / panel.getVisibleButtonsAmount()) * (panel.getMaxButtonsAmount() * (panel.getButtonWidth() + panel.getButtonsOffset())) : panel.getX(),
    						panel.orientation == GUIEnumOrientation.VERTICAL ? panel.getY() + size * (panel.getButtonHeight() + panel.getButtonsOffset()) - (size / panel.getVisibleButtonsAmount()) * (panel.getMaxButtonsAmount() * (panel.getButtonHeight() + panel.getButtonsOffset())) : panel.getY());
    				
    				panel.visibleButtons.add(button);
    			}
    		}
    	}
    	
    	else {
    		
    		//TODO Handle smooth scroller
    	}
    }
    
    //TODO updateScreen()
    public void updateScreen() {
    	
    	if (this.isWorkspaceCreated()) {
    		
    		GUISection section = this.getWorkspace().getCurrentSection();    
    		
    		for (GUITextField textField : section.getTextFieldsList()) {
    			
    			textField.updateCursorCounter();
    		}
        	
        	for (GUIFramework frame : this.getWorkspace().getCurrentSection().frameworks) {       		    	    	

        		for (GUITextField textField : frame.getTextFieldsList()) {
        			
        			textField.updateCursorCounter();
        		}
    	    }
    	}
    }
    
    /**
     * Возвращает длину переданной строки.
     * 
     * @param text
     * 
     * @return длина строки в пикселях
     */
    public int width(String text) {
    	
    	return this.mc.fontRenderer.getStringWidth(text);
    }
}
