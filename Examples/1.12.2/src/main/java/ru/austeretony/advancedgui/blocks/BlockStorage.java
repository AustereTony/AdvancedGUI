package ru.austeretony.advancedgui.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ru.austeretony.advancedgui.blocks.tile.BlockTileEntity;
import ru.austeretony.advancedgui.main.AdvancedGUIMain;
import ru.austeretony.advancedgui.main.GUIHandler;
import ru.austeretony.advancedgui.tiles.TileEntityStorage;

public class BlockStorage extends BlockTileEntity<TileEntityStorage> {

	public BlockStorage(String name, Material material, float hardness, float resistanse, SoundType soundType) {
		
		super(name, material, hardness, resistanse, soundType);
		
		this.setHarvestLevel("pickaxe", 3);
	}
	
    public boolean onBlockActivated(World world, BlockPos position, IBlockState blockState, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (!world.isRemote && !player.isSneaking()) {
			
			player.openGui(AdvancedGUIMain.instance, GUIHandler.STORAGE, world , position.getX(), position.getY(), position.getZ());
			
			return true;
		}
    	
    	return false;
    }

	@Override
	public Class<TileEntityStorage> getTileEntityClass() {
		
		return TileEntityStorage.class;
	}

	@Override
	public TileEntityStorage createTileEntity(World world, IBlockState blockState) {
		
		return new TileEntityStorage();
	}
}
