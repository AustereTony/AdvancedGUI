package libs.austeretony.advancedgui.screen.core;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import libs.austeretony.advancedgui.screen.button.GUIButton;
import libs.austeretony.advancedgui.screen.framework.GUIFramework;

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
