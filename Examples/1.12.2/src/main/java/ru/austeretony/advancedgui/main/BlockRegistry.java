package ru.austeretony.advancedgui.main;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.austeretony.advancedgui.blocks.BlockStorage;

public class BlockRegistry {

    public static final Block 
    BLOCK_STORAGE = new BlockStorage("block_storage", Material.ROCK, 10.0F, 10.0F, SoundType.STONE).setCreativeTab(AdvancedGUIMain.ADVANCED_GUI);
    
    public static void register() {

    	setBlockRegister(BLOCK_STORAGE);
    	setItemBlockRegister(BLOCK_STORAGE);
        GameRegistry.registerTileEntity(((BlockStorage) BLOCK_STORAGE).getTileEntityClass(), BLOCK_STORAGE.getRegistryName().toString());
    }
    
    @SideOnly(Side.CLIENT)
    public static void registerRender() {
    	
    	setItemBlockRender(BLOCK_STORAGE);
    }
    
    private static void setBlockRegister(Block block) {
    	
        ForgeRegistries.BLOCKS.register(block);        
    }
    
    private static void setItemBlockRegister(Block block) {
    	
        ForgeRegistries.ITEMS.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }
    
    @SideOnly(Side.CLIENT)
    private static void setItemBlockRender(Block block) {
    	
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }
}
