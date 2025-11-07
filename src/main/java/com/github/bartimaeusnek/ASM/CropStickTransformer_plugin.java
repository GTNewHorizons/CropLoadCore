package com.github.bartimaeusnek.ASM;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.TransformerExclusions({ "com.github.bartimaeusnek.ASM" })
@IFMLLoadingPlugin.Name(CropStickTransformer_plugin.NAME)
public class CropStickTransformer_plugin implements IFMLLoadingPlugin {

    public static final String NAME = "CroploadCore ASM Core";

    @Override
    public String[] getASMTransformerClass() {
        return null;
    }

    @Override
    public String getModContainerClass() {
        return "com.github.bartimaeusnek.ASM.CropLoadCoreASM";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
