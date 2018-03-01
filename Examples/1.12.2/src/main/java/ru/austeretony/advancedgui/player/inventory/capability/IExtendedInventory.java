package ru.austeretony.advancedgui.player.inventory.capability;

import ru.austeretony.advancedgui.player.inventory.InventoryExtended;

public interface IExtendedInventory {

    InventoryExtended getInventory();
    
    void copyInventory(IExtendedInventory inventory);
}
