package com.unrealdinnerbone.simplezoom;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.lwjgl.glfw.GLFW;

import java.nio.file.Paths;

public class SimpleZoomClient {

    private final SimpleZoom simpleZoom;
    private final KeyMapping zoomBind;

    private static boolean isSmoothCameraOn = false;
    private static double zoomCount = 0.0f;

    public SimpleZoomClient(SimpleZoom simpleZoom) {
        this.simpleZoom = simpleZoom;
        zoomBind = new KeyMapping(SimpleZoom.MOD_ID + "." + "zoom", GLFW.GLFW_KEY_Z, SimpleZoom.MOD_ID + "." + "zoom");
        ClientRegistry.registerKeyBinding(zoomBind);
        MinecraftForge.EVENT_BUS.addListener(this::onFOVModifierEvent);
    }


    public void onFOVModifierEvent(EntityViewRenderEvent.FOVModifier event) {
        if (zoomBind.isDown()) {
            if (simpleZoom.smoothCamera.get() && !isSmoothCameraOn) {
                setSmoothZoom(true);
            }
            if (simpleZoom.doSlowZoom.get()) {
                if (zoomCount > simpleZoom.zoomAmount.get()) {
                    zoomCount -= simpleZoom.zoomSpeed.get();
                    if (zoomCount < simpleZoom.zoomAmount.get()) {
                        zoomCount = simpleZoom.zoomAmount.get();
                    }
                }
            }
            event.setFOV(zoomCount);
            return;
        }

        if (simpleZoom.smoothCamera.get() && isSmoothCameraOn) {
            setSmoothZoom(false);
        }
        if (simpleZoom.doSlowZoom.get()) {
            if (zoomCount < event.getFOV()) {
                zoomCount += simpleZoom.zoomSpeed.get();
                if (zoomCount > event.getFOV()) {
                    zoomCount = event.getFOV();
                }
            }
            event.setFOV(zoomCount);
        } else {
            zoomCount = simpleZoom.zoomAmount.get();
        }

    }

    private void setSmoothZoom(boolean smoothOn) {
        isSmoothCameraOn = smoothOn;
        if(Minecraft.getInstance() != null && Minecraft.getInstance().options != null) {
            Minecraft.getInstance().options.smoothCamera = smoothOn;
        }
    }


}
