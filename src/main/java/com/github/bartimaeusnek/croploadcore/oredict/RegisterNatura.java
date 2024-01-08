package com.github.bartimaeusnek.croploadcore.oredict;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import mods.natura.common.NContent;

public class RegisterNatura implements OreDictRegistrator {

    @Override
    public String getModWhichRegisters() {
        return "Natura";
    }

    @Override
    public void run() {
        OreDictionary.registerOre("cropSaguaroBerry", new ItemStack(NContent.seedFood, 1, 0));
        OreDictionary.registerOre("listAllberry", new ItemStack(NContent.seedFood, 1, 0));
        OreDictionary.registerOre("cropGooseberry", new ItemStack(NContent.berryItem, 1, 3));
        OreDictionary.registerOre("cropCacti", new ItemStack(NContent.saguaro));
        OreDictionary.registerOre("cropSaguaro", new ItemStack(NContent.saguaro));
        OreDictionary.registerOre("cropVine", new ItemStack(NContent.thornVines));
        OreDictionary.registerOre("cropThornVines", new ItemStack(NContent.thornVines));
    }
}
