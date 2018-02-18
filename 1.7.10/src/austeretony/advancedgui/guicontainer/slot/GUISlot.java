package libs.austeretony.advancedgui.guicontainer.slot;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public class GUISlot {

	private Minecraft mc;
    
    public final int slotWidth, slotHeight;
    
    private GUISlotRenderer slotRenderer;
	
	public GUISlot(int slotWidth, int slotHeight) {
		
		this.slotWidth = slotWidth;
		this.slotHeight = slotHeight;
		
        this.mc = Minecraft.getMinecraft();
	}	
	
	public void draw() {}
}
