package ru.austeretony.advancedgui.player.inventory.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class InventoryProvider implements ICapabilitySerializable<NBTBase> {

    @CapabilityInject(IExtendedInventory.class)
    public static final Capability<IExtendedInventory> INVENTORY_CAP = null;

    private IExtendedInventory instance = this.INVENTORY_CAP.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    	
         return capability == this.INVENTORY_CAP;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    	
         return capability == this.INVENTORY_CAP ? this.INVENTORY_CAP.<T> cast(this.instance) : null;
    }

    @Override
    public NBTBase serializeNBT() {
    	
         return this.INVENTORY_CAP.getStorage().writeNBT(this.INVENTORY_CAP, this.instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
    	
    	this.INVENTORY_CAP.getStorage().readNBT(this.INVENTORY_CAP, this.instance, null, nbt);
    }
}
