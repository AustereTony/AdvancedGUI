package ru.austeretony.advancedgui.main;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import ru.austeretony.advancedgui.inventory.ContainerStorage;
import ru.austeretony.advancedgui.inventory.GUIStorage;
import ru.austeretony.advancedgui.player.ExtendedPlayer;
import ru.austeretony.advancedgui.player.inventory.ContainerInventory;
import ru.austeretony.advancedgui.player.inventory.GUIInventory;
import ru.austeretony.advancedgui.tiles.TileEntityStorage;

public class GUIHandler implements IGuiHandler {

	public static final int 
	INVENTORY_FIRST = 0,
	STORAGE = 1;
	
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		
		ExtendedPlayer ePlayer = ExtendedPlayer.get(player);
		
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		
		switch(id) {
		
			case INVENTORY_FIRST:  				
				return new ContainerInventory(player, ePlayer.inventory);
				
			case STORAGE:  				
				return new ContainerStorage(player, ePlayer.inventory, (TileEntityStorage) tileEntity);
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

		ExtendedPlayer ePlayer = ExtendedPlayer.get(player);
		
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		
		switch(id) {
		
			case INVENTORY_FIRST: 				
				return new GUIInventory(player, ePlayer.inventory);
				
			case STORAGE: 				
				return new GUIStorage(player, ePlayer.inventory, (TileEntityStorage) tileEntity);
		}
		
		return null;
	}
}