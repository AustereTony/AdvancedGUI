package ru.austeretony.advancedgui.main.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import ru.austeretony.advancedgui.main.AdvancedGUIMain;

public class TabAdvancedGUI extends CreativeTabs {

	public TabAdvancedGUI(int id, String name) {
		
		super(id, name);
	}

	@Override
	public Item getTabIconItem() {
		
		return Item.getItemFromBlock(AdvancedGUIMain.BLOCK_STORAGE);
	}
}
