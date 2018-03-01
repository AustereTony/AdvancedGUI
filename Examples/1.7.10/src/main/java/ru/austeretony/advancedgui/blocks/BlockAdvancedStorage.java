package ru.austeretony.advancedgui.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import ru.austeretony.advancedgui.main.AdvancedGUIMain;
import ru.austeretony.advancedgui.main.GUIHandler;
import ru.austeretony.advancedgui.tiles.TileEntityStorage;

public class BlockAdvancedStorage extends Block implements ITileEntityProvider {
	
	public BlockAdvancedStorage() {
		
		super(Material.rock);
		
		this.setCreativeTab(AdvancedGUIMain.ADVANCED_GUI);
		this.setBlockUnbreakable();
		this.setBlockTextureName(AdvancedGUIMain.MODID + ":" + "sampleTexture");
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i, float a, float b, float c) {
		
		if (!player.isSneaking()) {
			
			player.openGui(AdvancedGUIMain.instance, GUIHandler.STORAGE, world , x, y, z);
			
			return true;
		}
		
		return false;
	}

	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		
		return new TileEntityStorage();
	}
}

