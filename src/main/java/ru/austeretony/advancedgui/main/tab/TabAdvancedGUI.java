package ru.austeretony.advancedgui.main.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ru.austeretony.advancedgui.main.BlockRegistry;

public class TabAdvancedGUI extends CreativeTabs {

	public TabAdvancedGUI(int index, String label) {
		
		super(index, label);
	}

	@Override
	public ItemStack getTabIconItem() {
		
		return new ItemStack(Item.getItemFromBlock(BlockRegistry.BLOCK_STORAGE));
	}
}
