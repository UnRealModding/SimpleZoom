package com.unrealdinnerbone.simplezoom;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.living.ZombieEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("WeakerAccess")
@Mod.EventBusSubscriber
@Mod(modid = SimpleZoom.MOD_ID, clientSideOnly = true, version = SimpleZoom.MOD_VERSION, name = SimpleZoom.MOD_NAME)
public class SimpleZoom {

    public static final String MOD_NAME = "Simple Zoom";
    public static final String MOD_ID = "simplezoom";
    public static final String MOD_VERSION = "@VERSION@";

    private static KeyBinding zoomBind;
    private static boolean isSmoothCameraOn = false;
    private static float zoomCount = 0.0f;

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
    public static void onFOVModifierEvent(EntityViewRenderEvent.FOVModifier event) {
        if (zoomBind.isKeyDown()) {
            if (ZoomConfig.smoothCamera && !isSmoothCameraOn) {
                setSmoothZoom(true);
            }
            if (ZoomConfig.doSlowZoom) {
                if (zoomCount > ZoomConfig.zoomAmount) {
                    zoomCount -= ZoomConfig.zoomSpeed;
                    if (zoomCount < ZoomConfig.zoomAmount) {
                        zoomCount = ZoomConfig.zoomAmount;
                    }
                }
            }
            event.setFOV(zoomCount);
            return;
        }

        if (ZoomConfig.smoothCamera && isSmoothCameraOn) {
            setSmoothZoom(false);
        }
        if (ZoomConfig.doSlowZoom) {
            if (zoomCount < event.getFOV()) {
                zoomCount += ZoomConfig.zoomSpeed;
                if (zoomCount > event.getFOV()) {
                    zoomCount = event.getFOV();
                }
            }
            event.setFOV(zoomCount);
        } else {
            zoomCount = ZoomConfig.zoomAmount;
        }

    }

    @Config(modid = SimpleZoom.MOD_ID, name = "../local/client/simplezoom")
    public static class ZoomConfig {

        @Config.Comment("The Amount that will be zoomed in")
        public static float zoomAmount = 5.0f;
        @Config.Comment("Toggle cinematic camera on while zooming")
        public static boolean smoothCamera = true;
        @Config.RangeDouble(min = 1.0, max = 2.0)
        @Config.Comment({"The speed at witch the camera will be zoomed", "doSlowZoom must be in order to work"})
        public static double zoomSpeed = 1.0;
        @Config.Comment("Make zooming in more progressive")
        public static boolean doSlowZoom = false;

    }


    private static void setSmoothZoom(boolean smoothOn) {
        isSmoothCameraOn = smoothOn;
        Minecraft.getMinecraft().gameSettings.smoothCamera = smoothOn;
    }


}



