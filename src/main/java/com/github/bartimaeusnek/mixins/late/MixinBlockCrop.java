package com.github.bartimaeusnek.mixins.late;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import cpw.mods.fml.common.registry.GameRegistry;
import ic2.core.crop.BlockCrop;
import ic2.core.crop.TileEntityCrop;

@Mixin(BlockCrop.class)
public class MixinBlockCrop {

    /**
     * @author bartimaeusnek
     * @reason allow CropBlock to be placeable on any soil
     */
    @Overwrite
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return world.getBlock(x, y - 1, z).canSustainPlant(
                world,
                x,
                y,
                z,
                ForgeDirection.UP,
                new ItemSeedFood(4, 0.6F, Blocks.carrots, Blocks.farmland));
    }

    /**
     * @author bartimaeusnek
     * @reason Patching CropBlock to not pop off when neighbours change and not placed on farmland, but on any other
     *         soil -_-'
     */
    @Overwrite
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        if (!this.canPlaceBlockAt(world, x, y, z)) {
            world.setBlockToAir(x, y, z);
            GameRegistry.findBlock("IC2", "blockCrop").dropBlockAsItem(world, x, y, z, 0, 0);
        } else {
            TileEntity te = world.getTileEntity(x, y, z);
            if (!(te instanceof TileEntityCrop)) {
                return;
            }
            ((TileEntityCrop) te).onNeighbourChange();
        }
    }
}
