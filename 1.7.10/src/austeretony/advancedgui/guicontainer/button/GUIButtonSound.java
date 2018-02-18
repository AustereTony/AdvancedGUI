package libs.austeretony.advancedgui.guicontainer.button;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

/**
 * Упакованный звук для контекстного действия.
 */
@SideOnly(Side.CLIENT)
public class GUIButtonSound {

	public final ResourceLocation fileLocation;
	
	public final float volume;
	
	/**
	 * Звук для контекстного действия.
	 * 
	 * @param fileLocation путь к файлу.
	 * @param volume громкость.
	 */
	public GUIButtonSound(ResourceLocation fileLocation, float volume) {
		
		this.fileLocation = fileLocation;
		this.volume = volume;
	}
}
