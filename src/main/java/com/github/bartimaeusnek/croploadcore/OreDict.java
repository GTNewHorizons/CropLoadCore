package com.github.bartimaeusnek.croploadcore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.github.bartimaeusnek.croploadcore.oredict.RegisterBoP;
import com.github.bartimaeusnek.croploadcore.oredict.RegisterNatura;
import com.github.bartimaeusnek.croploadcore.oredict.RegisterThaumcraft;
import com.github.bartimaeusnek.croploadcore.oredict.RegisterThaumicBases;
import com.github.bartimaeusnek.croploadcore.oredict.RegisterTinkersConstruct;
import com.github.bartimaeusnek.croploadcore.oredict.RegisterVanilla;
import com.github.bartimaeusnek.croploadcore.oredict.RegisterWitchery;

import cpw.mods.fml.common.Loader;
import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;

public class OreDict {

    private static boolean isregistered = false;

    public static ItemStack ISget(String name) {
        if (OreDictionary.getOres(name).size() != 0) {
            return OreDictionary.getOres(name).get(OreDictionary.getOres(name).size() - 1);
        } else return null;
    }

    public static void BSget(String name, CropCard crop) {
        String hname;
        if (OreDictionary.getOres(name).size() != 0) for (int i = 0; i < OreDictionary.getOres(name).size(); i++)
            Crops.instance.registerBaseSeed(OreDictionary.getOres(name).get(i), crop, 1, 1, 1, 1);
        if (name.contains("crop")) {
            hname = name.replace("crop", "seed");
            if (OreDictionary.getOres(name).size() != 0) for (int i = 0; i < OreDictionary.getOres(name).size(); i++)
                Crops.instance.registerBaseSeed(OreDictionary.getOres(name).get(i), crop, 1, 1, 1, 1);
        }
        if (name.contains("seed")) {
            hname = name.replace("seed", "crop");
            if (OreDictionary.getOres(hname).size() != 0) for (int i = 0; i < OreDictionary.getOres(hname).size(); i++)
                Crops.instance.registerBaseSeed(OreDictionary.getOres(hname).get(i), crop, 1, 1, 1, 1);
        }
    }

    public static List<ItemStack> get_subtypes(List<Item> subtypes, boolean CleanList) {
        // FMLLog.info("get_subtypes was called");
        List<ItemStack> itemsgot = new ArrayList<ItemStack>();
        List<Item> cleansubtypes = new ArrayList<Item>();
        List<String> itemnames = new ArrayList<String>();
        int j = 0;
        boolean k = false;

        if (CleanList) {
            Set<Item> s = new HashSet<Item>(subtypes);
            cleansubtypes = new ArrayList<Item>(s);
        } else cleansubtypes = new ArrayList<Item>(subtypes);

        for (int i = 0; i < cleansubtypes.size(); i++) {
            j = 0;
            k = false;
            if (!((cleansubtypes.get(i).getClass().toString().contains("binnie"))
                    || (cleansubtypes.get(i).getClass().toString().contains("forestry")))) {
                do {
                    if (j > 0) k = ((new ItemStack(cleansubtypes.get(i), 1, j).getDisplayName())
                            .equals(new ItemStack(cleansubtypes.get(i), 1, 0).getDisplayName())
                            || ((new ItemStack(cleansubtypes.get(i), 1, j).getDisplayName()).contains("."))
                            || ((new ItemStack(cleansubtypes.get(i), 1, j).getDisplayName())
                                    .contains(Integer.toString(j)))
                            || ((new ItemStack(cleansubtypes.get(i), 1, j).getDisplayName())
                                    .equals(new ItemStack(cleansubtypes.get(i), 1, (j + 1)).getDisplayName())
                                    && (new ItemStack(cleansubtypes.get(i), 1, j).getDisplayName())
                                            .equals(new ItemStack(cleansubtypes.get(i), 1, j + 2).getDisplayName())));
                    if (!k) itemsgot.add(new ItemStack(cleansubtypes.get(i), 1, j));
                    j++;
                } while (!k);
            }
        }
        return itemsgot;
    }

    public static boolean register() {

        if (!isregistered) {
            new RegisterTinkersConstruct().register();
            new RegisterThaumcraft().register();
            new RegisterBoP().register();
            new RegisterNatura().register();
            if (Loader.isModLoaded("witchery")) new RegisterWitchery().register();
            new RegisterThaumicBases().register();

            if (!OreDictionary.getOres("cropBlackberry").isEmpty())
                for (int i = 0; i < OreDictionary.getOres("cropBlackberry").size(); i++)
                    if (!OreDictionary.getOres("listAllberry").contains(OreDictionary.getOres("cropBlackberry").get(i)))
                        OreDictionary.registerOre("listAllberry", OreDictionary.getOres("cropBlackberry").get(i));

            if (!OreDictionary.getOres("cropBlueberry").isEmpty())
                for (int i = 0; i < OreDictionary.getOres("cropBlueberry").size(); i++)
                    if (!OreDictionary.getOres("listAllberry").contains(OreDictionary.getOres("cropBlueberry").get(i)))
                        OreDictionary.registerOre("listAllberry", OreDictionary.getOres("cropBlueberry").get(i));

            if (!OreDictionary.getOres("cropGooseberry").isEmpty())
                for (int i = 0; i < OreDictionary.getOres("cropGooseberry").size(); i++)
                    if (!OreDictionary.getOres("listAllberry").contains(OreDictionary.getOres("cropGooseberry").get(i)))
                        OreDictionary.registerOre("listAllberry", OreDictionary.getOres("cropGooseberry").get(i));

            if (!OreDictionary.getOres("cropRaspberry").isEmpty())
                for (int i = 0; i < OreDictionary.getOres("cropRaspberry").size(); i++)
                    if (!OreDictionary.getOres("listAllberry").contains(OreDictionary.getOres("cropRaspberry").get(i)))
                        OreDictionary.registerOre("listAllberry", OreDictionary.getOres("cropRaspberry").get(i));

            if (!OreDictionary.getOres("cropStrawberry").isEmpty())
                for (int i = 0; i < OreDictionary.getOres("cropStrawberry").size(); i++)
                    if (!OreDictionary.getOres("listAllberry").contains(OreDictionary.getOres("cropStrawberry").get(i)))
                        OreDictionary.registerOre("listAllberry", OreDictionary.getOres("cropStrawberry").get(i));

            RegisterVanilla.register();

            isregistered = true;
            CropLoadCore.CLClogger.info("Plants registered to OreDict.");
        }
        return isregistered;
    }

}
