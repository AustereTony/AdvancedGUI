package libs.austeretony.advancedgui.screen.button;


import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
