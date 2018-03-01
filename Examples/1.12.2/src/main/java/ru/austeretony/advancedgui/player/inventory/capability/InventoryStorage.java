package ru.austeretony.advancedgui.player.inventory.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class InventoryStorage implements IStorage<IExtendedInventory> {

    @Override
    public NBTBase writeNBT(Capability<IExtendedInventory> capability, IExtendedInventory instance, EnumFacing side) {
    	
        NBTTagCompound properties = new NBTTagCompound();
        
        instance.getInventory().writeToNBT(properties);
        
        return properties;
    }

    @Override
    public void readNBT(Capability<IExtendedInventory> capability, IExtendedInventory instance, EnumFacing side, NBTBase nbt) {
    	
        NBTTagCompound properties = (NBTTagCompound) nbt;
        
        instance.getInventory().readFromNBT(properties);
    }
}
