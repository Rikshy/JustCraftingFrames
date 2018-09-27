package de.shyrik.modularitemframe;

import de.shyrik.modularitemframe.api.ModuleRegistry;
import de.shyrik.modularitemframe.client.gui.GuiHandler;
import de.shyrik.modularitemframe.common.module.t1.ModuleCrafting;
import de.shyrik.modularitemframe.common.module.t1.ModuleIO;
import de.shyrik.modularitemframe.common.network.NetworkHandler;
import de.shyrik.modularitemframe.init.Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(
        modid = ModularItemFrame.MOD_ID,
        name = ModularItemFrame.MOD_NAME,
        version = ModularItemFrame.VERSION,
        dependencies = ModularItemFrame.DEPENDENCIES)
public class ModularItemFrame {

    public static final String MOD_ID = "modularitemframe";
    public static final String MOD_NAME = "Modular Item Frame";
    public static final String VERSION = "@GRADLE:VERSION@";
    public static final String DEPENDENCIES = "after:mcmultipart;";
    public static final String CHANNEL = MOD_ID;

    public static final CreativeTabs TAB = new CreativeTabs("modularitemframe") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Items.SCREWDRIVER);
        }
    };

    @Mod.Instance
    public static ModularItemFrame instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        NetworkHandler.registerPackets();

        ModuleRegistry.register(ModuleCrafting.LOC, ModuleCrafting.class);
        ModuleRegistry.register(ModuleIO.LOC, ModuleIO.class);
    }
}
