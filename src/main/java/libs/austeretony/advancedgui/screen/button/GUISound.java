package libs.austeretony.advancedgui.screen.button;


import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Упакованный звук для элементов ГПИ.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUISound {

	public final SoundEvent soundEvent;
	
	public final float volume, pitch;
	
	/**
	 * Звук для элементов ГПИ.
	 * 
	 * @param soundEvent зарегистрированный звук
	 * @param volume
	 * @param pitch
	 */
	public GUISound(SoundEvent soundEvent, float volume, float pitch) {
		
		this.soundEvent = soundEvent;
		this.volume = volume;
		this.pitch = pitch;
	}
}
