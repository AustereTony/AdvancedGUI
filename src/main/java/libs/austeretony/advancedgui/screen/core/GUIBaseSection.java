package libs.austeretony.advancedgui.screen.core;

import libs.austeretony.advancedgui.screen.button.GUIButton;
import libs.austeretony.advancedgui.screen.framework.GUIFramework;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Класс базового раздела, используемого в качестве внутреннего раздела ГПИ (без создания собственного).
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUIBaseSection extends GUISection {

	public GUIBaseSection(AdvancedGUIScreen screen) {
		
		super(screen);
	}
	
	@Override
	public void init() {}

	@Override
	public void handleButtonPress(GUISection section, GUIFramework framework, GUIButton button) {}
}
