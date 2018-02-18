package ru.austeretony.advancedgui.main;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import ru.austeretony.advancedgui.inventory.ContainerAdvancedStorage;
import ru.austeretony.advancedgui.inventory.GUIAdvancedStorage;
import ru.austeretony.advancedgui.player.ExtendedPlayer;
import ru.austeretony.advancedgui.player.inventory.ContainerInventory;
import ru.austeretony.advancedgui.player.inventory.GUIInventoryFirst;
import ru.austeretony.advancedgui.player.inventory.GUIInventorySecond;
import ru.austeretony.advancedgui.tiles.TileEntityAdvancedStorage;

public class GUIHandler implements IGuiHandler {

	public static final int 
	INVENTORY_FIRST = 0,
	INVENTORY_SECOND = 1,
	STORAGE = 2;
	
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		
		ExtendedPlayer ePlayer = ExtendedPlayer.get(player);
		
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		
		switch(id) {
		
			case INVENTORY_FIRST:  				
				return new ContainerInventory(player, ePlayer.inventory);
				
			case INVENTORY_SECOND:  				
				return new ContainerInventory(player, ePlayer.inventory);
				
			case STORAGE:  				
				return new ContainerAdvancedStorage(player, ePlayer.inventory, (TileEntityAdvancedStorage) tileEntity);
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

		ExtendedPlayer ePlayer = ExtendedPlayer.get(player);
		
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		
		switch(id) {
		
			case INVENTORY_FIRST: 				
				return new GUIInventoryFirst(player, ePlayer.inventory);
				
			case INVENTORY_SECOND: 				
				return new GUIInventorySecond(player, ePlayer.inventory);
				
			case STORAGE: 				
				return new GUIAdvancedStorage(player, ePlayer.inventory, (TileEntityAdvancedStorage) tileEntity);
		}
		
		return null;
	}
}