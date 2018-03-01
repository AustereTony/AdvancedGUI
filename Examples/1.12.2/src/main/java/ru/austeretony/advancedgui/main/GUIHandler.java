package ru.austeretony.advancedgui.main;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import ru.austeretony.advancedgui.inventory.ContainerStorage;
import ru.austeretony.advancedgui.inventory.GUIStorage;
import ru.austeretony.advancedgui.player.inventory.ContainerExtendedInventory;
import ru.austeretony.advancedgui.player.inventory.GUIExtendedInventory;
import ru.austeretony.advancedgui.player.inventory.capability.IExtendedInventory;
import ru.austeretony.advancedgui.player.inventory.capability.InventoryProvider;
import ru.austeretony.advancedgui.tiles.TileEntityStorage;

public class GUIHandler implements IGuiHandler {

	public static final int 
	INVENTORY = 0,
	STORAGE = 1;
	
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		
		IExtendedInventory eInventory = player.getCapability(InventoryProvider.INVENTORY_CAP, null);
		
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		
		switch(id) {
		
			case INVENTORY:  				
				return new ContainerExtendedInventory(player, eInventory.getInventory());
				
			case STORAGE:  				
				return new ContainerStorage(player, eInventory.getInventory(), (TileEntityStorage) tileEntity);
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

		IExtendedInventory eInventory = player.getCapability(InventoryProvider.INVENTORY_CAP, null);
		
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		
		switch(id) {
		
			case INVENTORY: 				
				return new GUIExtendedInventory(player, eInventory.getInventory());
				
			case STORAGE: 				
				return new GUIStorage(player, eInventory.getInventory(), (TileEntityStorage) tileEntity);
		}
		
		return null;
	}
}
