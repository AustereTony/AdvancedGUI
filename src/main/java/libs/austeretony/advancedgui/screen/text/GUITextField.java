package libs.austeretony.advancedgui.screen.text;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import libs.austeretony.advancedgui.screen.core.GUISimpleElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ChatAllowedCharacters;

/**
 * Простое поле для ввода текста (однострочное).
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUITextField extends GUISimpleElement<GUITextField> {

    private String typedText;
    
    private char cursorSymbol;
    
    private final int maxStringLength;
    
    private boolean canLoseFocus, hasCustomCursor;
            
    private int lineScrollOffset, cursorCounter, cursorPosition, selectionEnd;
    
    /**
     * Текстовое поле.
     * 
     * @param xPosition позиция по x
     * @param yPosition позиция по y
     * @param width ширина
     * @param maxStringLength максимальное кол-во символов, которое можно ввести
     */
    public GUITextField(int xPosition, int yPosition, int width, int maxStringLength) {
    	    	
        this.setPosition(xPosition, yPosition);
        this.setSize(width, this.FONT_HEIGHT);
        this.maxStringLength = maxStringLength > 32 ? 32 : maxStringLength;
        
        this.typedText = "";
        
        this.setEnabled(true);
        this.setVisible(true);
        this.canLoseFocus = true;
    }

    public void updateCursorCounter() {
    	
        this.cursorCounter++;
    }
    
    public String getTypedText() {
    	
        return this.typedText;
    }

    private void setText(String text) {
    	
        if (text.length() > this.maxStringLength) {
        	
            this.typedText = text.substring(0, this.maxStringLength);
        }
        
        else {
        	
            this.typedText = text;
        }

        this.setCursorPositionEnd();
    }

    private String getSelectedText() {
    	
        int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        
        return this.typedText.substring(i, j);
    }

    private void writeText(String text) {
    	
        String s1 = "";
        String s2 = ChatAllowedCharacters.filerAllowedCharacters(text);
        
        int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        int k = this.maxStringLength - this.typedText.length() - (i - this.selectionEnd);
        
        boolean flag = false;

        if (this.typedText.length() > 0) {
        	
            s1 = s1 + this.typedText.substring(0, i);
        }

        int l;

        if (k < s2.length()) {
        	
            s1 = s1 + s2.substring(0, k);
            
            l = k;
        }
        
        else {
        	
            s1 = s1 + s2;
            
            l = s2.length();
        }

        if (this.typedText.length() > 0 && j < this.typedText.length()) {
        	
            s1 = s1 + this.typedText.substring(j);
        }

        this.typedText = s1;
        
        this.moveCursorBy(i - this.selectionEnd + l);
    }

    private void deleteWords(int index) {
    	
        if (this.typedText.length() != 0) {
        	
            if (this.selectionEnd != this.cursorPosition) {
            	
                this.writeText("");
            }
            
            else {
            	
                this.deleteFromCursor(this.getNthWordFromCursor(index) - this.cursorPosition);
            }
        }
    }

    private void deleteFromCursor(int index) {
    	
        if (this.typedText.length() != 0) {
        	
            if (this.selectionEnd != this.cursorPosition) {
            	
                this.writeText("");
            }
            
            else {
            	
                boolean flag = index < 0;
                
                int j = flag ? this.cursorPosition + index : this.cursorPosition;
                int k = flag ? this.cursorPosition : this.cursorPosition + index;
                
                String s = "";

                if (j >= 0) {
                	
                    s = this.typedText.substring(0, j);
                }

                if (k < this.typedText.length()) {
                	
                    s = s + this.typedText.substring(k);
                }

                this.typedText = s;

                if (flag) {
                	
                    this.moveCursorBy(index);
                }
            }
        }
    }

    private int getNthWordFromCursor(int index) {
    	
        return this.getNthWordFromPos(index, this.getCursorPosition());
    }

    private int getNthWordFromPos(int index1, int index2) {
    	
        return this.getWord(index1, this.getCursorPosition(), true);
    }

    private int getWord(int index1, int index2, boolean flag) {
    	
        int k = index2;
        
        boolean flag1 = index1 < 0;
        
        int l = Math.abs(index1);

        for (int i1 = 0; i1 < l; ++i1) {
        	
            if (flag1) {
            	
                while (flag && k > 0 && this.typedText.charAt(k - 1) == 32) {
                	
                    k--;
                }

                while (k > 0 && this.typedText.charAt(k - 1) != 32) {
                	
                    k--;
                }
            }
            
            else {
            	
                int j1 = this.typedText.length();
                
                k = this.typedText.indexOf(32, k);

                if (k == - 1) {
                	
                    k = j1;
                }
                
                else {
                	
                    while (flag && k < j1 && this.typedText.charAt(k) == 32) {
                    	
                        k++;
                    }
                }
            }
        }

        return k;
    }

    private void moveCursorBy(int offset) {
    	
        this.setCursorPosition(this.selectionEnd + offset);
    }

    private void setCursorPosition(int index) {
    	
        this.cursorPosition = index;
        
        int j = this.typedText.length();

        if (this.cursorPosition < 0) {
        	
            this.cursorPosition = 0;
        }

        if (this.cursorPosition > j) {
        	
            this.cursorPosition = j;
        }

        this.setSelectionPos(this.cursorPosition);
    }

    private void setCursorPositionZero() {
    	
        this.setCursorPosition(0);
    }

    private void setCursorPositionEnd() {
    	
        this.setCursorPosition(this.typedText.length());
    }

    public boolean keyTyped(char keyChar, int keyCode) {
    	
        if (!this.isDragged()) {
        	
            return false;
        }
        
        else {
        	
            switch (keyChar) {
            
                case 1:
                	
                    this.setCursorPositionEnd();
                    this.setSelectionPos(0);
                    
                    return true;
                    
                case 3:
                	
                    GuiScreen.setClipboardString(this.getSelectedText());
                    
                    return true;
                    
                case 22:
                	
                    if (this.isEnabled()) {
                    	
                        this.writeText(GuiScreen.getClipboardString());
                    }

                    return true;
                    
                case 24:
                	
                    GuiScreen.setClipboardString(this.getSelectedText());

                    if (this.isEnabled()) {
                    	
                        this.writeText("");
                    }

                    return true;
                    
                default:
                	
                    switch (keyCode) {
                    
                        case 14:
                        	
                            if (GuiScreen.isCtrlKeyDown()) {
                            	
                                if (this.isEnabled()) {
                                	
                                    this.deleteWords(- 1);
                                }
                            }
                            
                            else if (this.isEnabled()) {
                            	
                                this.deleteFromCursor(- 1);
                            }

                            return true;
                            
                        case 199:
                        	
                            if (GuiScreen.isShiftKeyDown()) {
                            	
                                this.setSelectionPos(0);
                            }
                            
                            else {
                            	
                                this.setCursorPositionZero();
                            }

                            return true;
                            
                        case 203:
                        	
                            if (GuiScreen.isShiftKeyDown()) {
                            	
                                if (GuiScreen.isCtrlKeyDown()) {
                                	
                                    this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                                }
                                
                                else {
                                	
                                    this.setSelectionPos(this.getSelectionEnd() - 1);
                                }
                            }
                            
                            else if (GuiScreen.isCtrlKeyDown()) {
                            	
                                this.setCursorPosition(this.getNthWordFromCursor(-1));
                            }
                            
                            else {
                            	
                                this.moveCursorBy(-1);
                            }

                            return true;
                            
                        case 205:
                        	
                            if (GuiScreen.isShiftKeyDown()) {
                            	
                                if (GuiScreen.isCtrlKeyDown()) {
                                	
                                    this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                                }
                                
                                else {
                                	
                                    this.setSelectionPos(this.getSelectionEnd() + 1);
                                }
                            }
                            
                            else if (GuiScreen.isCtrlKeyDown()) {
                            	
                                this.setCursorPosition(this.getNthWordFromCursor(1));
                            }
                            
                            else {
                            	
                                this.moveCursorBy(1);
                            }

                            return true;
                            
                        case 207:
                        	
                            if (GuiScreen.isShiftKeyDown()) {
                            	
                                this.setSelectionPos(this.typedText.length());
                            }
                            
                            else {
                            	
                                this.setCursorPositionEnd();
                            }

                            return true;
                            
                        case 211:
                        	
                            if (GuiScreen.isCtrlKeyDown()) {
                            	
                                if (this.isEnabled()) {
                                	
                                    this.deleteWords(1);
                                }
                            }
                            
                            else if (this.isEnabled()) {
                            	
                                this.deleteFromCursor(1);
                            }

                            return true;
                            
                        default:
                        	
                            if (ChatAllowedCharacters.isAllowedCharacter(keyChar)) {
                            	
                                if (this.isEnabled()) {
                                	
                                    this.writeText(Character.toString(keyChar));
                                }

                                return true;
                            }
                            
                            else {
                            	
                                return false;
                            }
                    }
            }
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY) {
    	    	
    	boolean flag = false;
    	
    	if (this.isDoubleClickRequired()) {
    		
    		if (this.isClickedLately()) {
    			
        		flag = true;
        		
        		this.setLastClickTime(0L);
    		}
    		
    		else {
    			
    			this.setLastClickTime(Minecraft.getSystemTime());
    		}
    	}
    	
    	else {
    		
    		flag = true;
    	}
    	
    	if (flag) {
    	
	        if (this.canLoseFocus) {
	        	
	            this.setDragged(this.isHovered());
	        }
	        
	        if (!this.isHovered()) {
	        	        	
	        	this.setText("");
	        }
	
	        if (this.isDragged()) {
	        	
	            int l = mouseX - this.getX();
	
	            if (this.isBackgroundEnabled()) {
	            	
	                l -= 4;
	            }
	
	            String s = this.mc.fontRenderer.trimStringToWidth(this.typedText.substring(this.lineScrollOffset), this.getWidth());
	            
	            this.setCursorPosition(this.mc.fontRenderer.trimStringToWidth(s, l).length() + this.lineScrollOffset);
	        }
	        
	        return true;
    	}
    	
    	return false;
    }

    @Override
    public void draw() {
    	
        if (this.isVisible()) {
        	        	
        	GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        	
            GL11.glPushMatrix();
            
            GL11.glTranslatef(this.getX(), this.getY(), 0.0F);
            
            GL11.glScalef(this.getScale(), this.getScale(), 0.0F);                           
        	
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    	
            if (this.isBackgroundEnabled()) {
            	
            	this.drawRect(this.ZERO, this.ZERO - 1, this.ZERO + this.getWidth(), this.ZERO + this.getHeight() + 1, this.getEnabledBackgroundColor());
                
                if (this.isDragged()) {
                	
                	this.drawRect(this.ZERO, this.ZERO - 1, this.ZERO + this.getWidth(), this.ZERO + this.getHeight() + 1, this.getHoveredBackgroundColor());
                }
            }
            
            int 
            i = this.isEnabled() ? this.getEnabledTextColor() : this.getDisabledTextColor(),
            j = this.cursorPosition - this.lineScrollOffset,
            k = this.selectionEnd - this.lineScrollOffset;           
            
            String s = this.mc.fontRenderer.trimStringToWidth(this.typedText.substring(this.lineScrollOffset), this.getWidth()); 
            
            boolean 
            flag = j >= 0 && j <= s.length(),
            flag1 = this.isDragged() && this.cursorCounter / 6 % 2 == 0 && flag;
            
            int l = this.isBackgroundEnabled() ? this.ZERO + 4 : this.ZERO,
            i1 = this.isBackgroundEnabled() ? this.ZERO + (this.getHeight() - this.FONT_HEIGHT) / 2 : this.ZERO,
            j1 = l;

            if (k > s.length()) {
            	
                k = s.length();
            }

            if (s.length() > 0) {
            	
                String s1 = flag ? s.substring(0, j) : s;
                j1 = this.mc.fontRenderer.drawStringWithShadow(s1, l, i1 + 1, i);
            }
            
            if (this.getDisplayText().length() > 0 && !this.isDragged()) {
            	
            	this.mc.fontRenderer.drawStringWithShadow(this.getDisplayText(), l, i1 + 1, this.getDisplayTextColor());
            }

            boolean flag2 = this.cursorPosition < this.typedText.length() || this.typedText.length() >= this.getMaxStringLength();
            
            int k1 = j1;

            if (!flag) {
            	
                k1 = j > 0 ? l + this.getWidth() : l;
            }
            
            else if (flag2) {
            	
                k1 = j1 - 1;
                
                j1++;
            }

            if (s.length() > 0 && flag && j < s.length()) {
            	
                this.mc.fontRenderer.drawStringWithShadow(s.substring(j), j1, i1, i);
            }

            if (flag1) {
            	
                if (!this.hasCustomCursor()) {

	                if (flag2) {
	                	
	                	this.drawRect(k1, i1 - 1, k1 + 1, i1 + this.FONT_HEIGHT, - 3092272);
	                }
	                
	                else {
	                	
	                	this.drawRect(k1, i1 - 1, k1 + 1, i1 + this.FONT_HEIGHT, - 3092272);
	                }
                }
                
                else {
                	
                    this.mc.fontRenderer.drawStringWithShadow(String.valueOf(this.getCursorSymbol()), k1, i1 + 1, this.getEnabledTextColor());
                }
            }

            if (k != j) {
            	
                int l1 = l + this.mc.fontRenderer.getStringWidth(s.substring(0, k));
                
                if (!this.hasCustomCursor()) {
                	
                	this.drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + this.FONT_HEIGHT);
                }
                
                else {
                	
                    this.mc.fontRenderer.drawStringWithShadow(String.valueOf(this.getCursorSymbol()), k1, i1 + 1, this.getEnabledTextColor());
                }
            }
            
            GL11.glPopMatrix();

            GL11.glEnable(GL12.GL_RESCALE_NORMAL);  
        }
    }

   private void drawCursorVertical(int xStart, int yStart, int xEnd, int yEnd) {
    	
        int i1;

        if (xStart < xEnd) {
        	
            i1 = xStart;
            xStart = xEnd;
            xEnd = i1;
        }

        if (yStart < yEnd) {
        	
            i1 = yStart;
            yStart = yEnd;
            yEnd = i1;
        }

        if (xEnd > this.getX() + this.getWidth()) {
        	
            xEnd = this.getX() + this.getWidth();
        }

        if (xStart > this.getX() + this.getWidth()) {
        	
            xStart = this.getX() + this.getWidth();
        }

        Tessellator tessellator = Tessellator.instance;
        
        GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
        
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
        GL11.glLogicOp(GL11.GL_OR_REVERSE);
        
        tessellator.startDrawingQuads();
        
        tessellator.addVertex((double)xStart, (double)yEnd, 0.0D);
        tessellator.addVertex((double)xEnd, (double)yEnd, 0.0D);
        tessellator.addVertex((double)xEnd, (double)yStart, 0.0D);
        tessellator.addVertex((double)xStart, (double)yStart, 0.0D);
        
        tessellator.draw();
        
        GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public int getMaxStringLength() {
    	
        return this.maxStringLength;
    }

    public int getCursorPosition() {
    	
        return this.cursorPosition;
    }

    @Override
    public GUITextField setDragged(boolean isDragged) {
        	
    	super.setDragged(isDragged);
    	
    	if (!this.isCanNotBeDragged()) {
    		
    		this.cursorCounter = 0;
    	}
    	
    	return this;
    }
    
    public int getSelectionEnd() {
    	
        return this.selectionEnd;
    }

    @Override
    public int getWidth() {
    	
        return this.isBackgroundEnabled() ? super.getWidth() + 8 : super.getWidth();
    }

    private void setSelectionPos(int index) {
    	
        int j = this.typedText.length();

        if (index > j) {
        	
            index = j;
        }

        if (index < 0) {
        	
            index = 0;
        }

        this.selectionEnd = index;

        if (this.mc.fontRenderer != null) {
        	
            if (this.lineScrollOffset > j) {
            	
                this.lineScrollOffset = j;
            }

            int k = this.getWidth();
            
            String s = this.mc.fontRenderer.trimStringToWidth(this.typedText.substring(this.lineScrollOffset), k);
            
            int l = s.length() + this.lineScrollOffset;

            if (index == this.lineScrollOffset) {
            	
                this.lineScrollOffset -= this.mc.fontRenderer.trimStringToWidth(this.typedText, k, true).length();
            }

            if (index > l) {
            	
                this.lineScrollOffset += index - l;
            }
            
            else if (index <= this.lineScrollOffset) {
            	
                this.lineScrollOffset -= this.lineScrollOffset - index;
            }

            if (this.lineScrollOffset < 0) {
            	
                this.lineScrollOffset = 0;
            }

            if (this.lineScrollOffset > j) {
            	
                this.lineScrollOffset = j;
            }
        }
    }

    public GUITextField setCanLoseFocus(boolean canLose) {
    	
        this.canLoseFocus = canLose;
        
        return this;
    }
    
    public boolean hasCustomCursor() {
    	
    	return this.hasCustomCursor;
    }
    
    public char getCursorSymbol() {
    	
    	return this.cursorSymbol;
    }
    
    /**
     * Использует указанный символ как символ курсора.
     * 
     * @param symbol
     * 
     * @return
     */
    public GUITextField setCustomCursor(char symbol) {
    	
    	this.cursorSymbol = symbol;
    	
    	this.hasCustomCursor = true;
    	
    	return this;
    }
}
