package ru.austeretony.advancedgui.player.inventory.capability;

import ru.austeretony.advancedgui.player.inventory.InventoryExtended;

public class ExtendedInventory implements IExtendedInventory {

    private final InventoryExtended inventory = new InventoryExtended();
	
	@Override
	public InventoryExtended getInventory() {
		
		return this.inventory;
	}
	
    @Override
    public void copyInventory(IExtendedInventory inventory) {
    	
        this.inventory.copy(inventory.getInventory());
    }
}
