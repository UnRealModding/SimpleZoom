package com.unrealdinnerbone.simplezoom;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber
@Mod(modid = SimpleZoom.MOD_ID, clientSideOnly = true, version = SimpleZoom.MOD_VERSION, name = SimpleZoom.MOD_NAME)
public class SimpleZoom {

    public static final String MOD_NAME = "Simple Zoom";
    public static final String MOD_ID = "simplezoom";
    public static final String MOD_VERSION = "@VERSION@";

    private static KeyBinding zoomBind;


    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(MOD_ID)) {
            ConfigManager.sync(MOD_ID, Config.Type.INSTANCE);
        }
    }

    @Mod.EventHandler
    public static void onInit(FMLInitializationEvent initializationEvent) {
        zoomBind = new KeyBinding("key.zoom.desc", Keyboard.KEY_C, "key.zoom.catg");
        ClientRegistry.registerKeyBinding(zoomBind);
    }

    @SubscribeEvent
    public static void onFOVUpdateEvent(FOVUpdateEvent event) {
        if (ZoomConfig.zoomType == ZoomType.FOV && zoomBind.isKeyDown()) {
            event.setNewfov(-ZoomConfig.zoomAmount);
        }
    }
    @SubscribeEvent
    public static void onFOVModifierEvent(EntityViewRenderEvent.FOVModifier event) {
        if (ZoomConfig.zoomType == ZoomType.CAMERA && zoomBind.isKeyDown()) {
            event.setFOV(ZoomConfig.zoomAmount);
        }
    }

    @Config(modid = SimpleZoom.MOD_ID, name = "../local/client/simplezoom")
    public static class ZoomConfig {

        public static float zoomAmount = 5.0f;
        public static ZoomType zoomType = ZoomType.CAMERA;

    }

    public static enum ZoomType {
        FOV,
        CAMERA
    }

}



