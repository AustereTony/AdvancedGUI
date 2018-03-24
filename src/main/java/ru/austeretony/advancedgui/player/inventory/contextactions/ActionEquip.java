package ru.austeretony.advancedgui.player.inventory.contextactions;

import libs.austeretony.advancedgui.container.contextmenu.GUIPresetAction;
import libs.austeretony.advancedgui.screen.button.GUISound;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import ru.austeretony.advancedgui.network.NetworkHandler;
import ru.austeretony.advancedgui.network.server.SPContextActionEquip;
import ru.austeretony.advancedgui.player.inventory.InventoryExtended;

public class ActionEquip extends GUIPresetAction {
	
	public ActionEquip(String name) {
		
		super(name);
	}

	@Override
	public boolean isValidAction(IInventory inventory, Slot slot, EntityPlayer player) {		
		
        EntityEquipmentSlot entityEquipmentSlot = EntityLiving.getSlotForItemStack(slot.getStack());               
		
		boolean 
		isWeapon = slot.getStack().getItem() instanceof ItemSword || slot.getStack().getItem() instanceof ItemBow || slot.getStack().getItem() instanceof ItemTool,
		isArmor = slot.getStack().getItem() instanceof ItemArmor,
		hasHotbarSpace = player.inventory.getFirstEmptyStack() != - 1 && player.inventory.getFirstEmptyStack() < 9,
		hasValidSpaceArmor = false;
		
		if (isArmor) {
										
			hasValidSpaceArmor = player.inventory.getStackInSlot(36 + entityEquipmentSlot.getIndex()) == ItemStack.EMPTY || 
					player.inventory.getStackInSlot(36 + entityEquipmentSlot.getIndex()).getItem() == Items.AIR;
		}
		
		return (isWeapon && hasHotbarSpace) || (isArmor && hasValidSpaceArmor);
	}

	@Override
	public void performAction(IInventory inventory, Slot slot, EntityPlayer player) {
		
		int inventoryId = - 1;
		
		if (inventory instanceof InventoryExtended) {
			
			inventoryId = 0;
		}
		
		else if (inventory instanceof InventoryPlayer) {
			
			inventoryId = 2;
		}
		
		NetworkHandler.sendToServer(new SPContextActionEquip(inventoryId, slot.getSlotIndex()));
	}

	@Override
	public GUISound getSound() {
		
		//return new GUIButtonSound(new ResourceLocation(AdvancedGUIMain.MODID + ":" + "click"), 1.0F);
		return null;
	}
}
