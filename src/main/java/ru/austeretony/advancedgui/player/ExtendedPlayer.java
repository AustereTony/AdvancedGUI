package ru.austeretony.advancedgui.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import ru.austeretony.advancedgui.player.inventory.InventoryExtended;

public class ExtendedPlayer implements IExtendedEntityProperties {
	
	public final static String EEP_NAME = "PlayerEEP";
		
    private EntityPlayer player;
    
    private World world;
    
    public final InventoryExtended inventory = new InventoryExtended();
    
    private float maxHealth;
        
    public ExtendedPlayer() {
        
        this.maxHealth = 20.0F;
    }
    
    public static final void register(EntityPlayer player) {
    	
        player.registerExtendedProperties(ExtendedPlayer.EEP_NAME, new ExtendedPlayer());
    }
	
    public static final ExtendedPlayer get(EntityPlayer player) {
    	
        return (ExtendedPlayer) player.getExtendedProperties(EEP_NAME);
    }

	@Override
	public void saveNBTData(NBTTagCompound mainCompound) {
		
        NBTTagCompound newCompound = new NBTTagCompound();
        
        this.inventory.writeToNBT(newCompound);
        
        mainCompound.setTag(EEP_NAME, newCompound);
	}

	@Override
	public void loadNBTData(NBTTagCompound mainCompound) {
		
        NBTTagCompound newCompound = (NBTTagCompound) mainCompound.getTag(EEP_NAME);     
        
        this.inventory.readFromNBT(newCompound);
	}

	@Override
	public void init(Entity entity, World world) {
		
		this.player = (EntityPlayer) entity;
		
		this.world = world;
	}
	
	public EntityPlayer getPlayer() {
		
		return this.player;
	}
	
	public World getWorld() {
		
		return this.world;
	}
}
