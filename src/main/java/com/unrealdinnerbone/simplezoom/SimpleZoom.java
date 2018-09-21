package com.unrealdinnerbone.simplezoom;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber
@Mod(modid = SimpleZoom.MOD_ID, clientSideOnly = true)
public class SimpleZoom {

    public static final String MOD_ID = "simplezoom";

    private static KeyBinding zoomBind;


    @Mod.EventHandler
    public static void onPreInit(FMLPreInitializationEvent event) {
        ConfigManager.sync(MOD_ID, Config.Type.INSTANCE);
    }

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(MOD_ID)) {
            ConfigManager.sync(MOD_ID, Config.Type.INSTANCE);
        }
    }

    @Mod.EventHandler
    public static void onInit(FMLInitializationEvent initializationEvent) {
        if(initializationEvent.getSide() == Side.CLIENT) {
            zoomBind = new KeyBinding("key.zoom.desc", Keyboard.KEY_Z, "key.zoom.catg");
            ClientRegistry.registerKeyBinding(zoomBind);
        }
    }

    @SubscribeEvent()
    public static void onEvent(FOVUpdateEvent event) {
        if (zoomBind.isKeyDown()) {
            event.setNewfov(ZoomConfig.zoomAmount);
        }
    }

    @Config(modid = SimpleZoom.MOD_ID)
    public static class ZoomConfig {

        public static float zoomAmount = -5.0f;

    }

}



