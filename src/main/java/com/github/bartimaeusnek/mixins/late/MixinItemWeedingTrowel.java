package com.github.bartimaeusnek.mixins.late;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import ic2.core.Ic2Items;
import ic2.core.crop.TileEntityCrop;
import ic2.core.item.tool.ItemWeedingTrowel;
import ic2.core.util.StackUtil;

@Mixin(ItemWeedingTrowel.class)
public class MixinItemWeedingTrowel {

    /**
     * @author bartimaeusnek
     * @reason for WeedingTrowel to accept custom Weeds
     */
    @Overwrite(remap = false)
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
            float hitX, float hitY, float hitZ) {
        if (world.isRemote) return false;

        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityCrop) {
            TileEntityCrop crop = (TileEntityCrop) te;

            if (crop.getCrop() != null && crop.getCrop().tier() < 1) {
                StackUtil.dropAsEntity(world, x, y, z, new ItemStack(Ic2Items.weed.getItem(), crop.size));
                crop.reset();
                return true;
            }
            if (crop.weedlevel > 0) {
                StackUtil.dropAsEntity(world, x, y, z, new ItemStack(Ic2Items.weed.getItem(), crop.weedlevel));
                crop.weedlevel = 0;
                crop.updateState();
                return true;
            }
        }
        return false;
    }
}
