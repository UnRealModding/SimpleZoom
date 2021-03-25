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

@Mod(SimpleZoom.MOD_ID)
public class SimpleZoom {

    public static final String MOD_ID = "simplezoom";

    private final ForgeConfigSpec.DoubleValue zoomAmount;
    private final ForgeConfigSpec.BooleanValue smoothCamera;
    private final ForgeConfigSpec.DoubleValue zoomSpeed;
    private final ForgeConfigSpec.BooleanValue doSlowZoom;
    private final KeyMapping zoomBind;

    private static boolean isSmoothCameraOn = false;
    private static double zoomCount = 0.0f;

    public SimpleZoom() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("client");
        zoomAmount = builder.comment("The Amount that will be zoomed in").defineInRange("zoomAmount", 5.0, 0, Integer.MAX_VALUE);
        smoothCamera = builder.comment("Toggle cinematic camera on while zooming").define("smoothCamera", true);
        zoomSpeed = builder.comment("The speed at witch the camera will be zoomed", "doSlowZoom must be in order to work").defineInRange("zoomSpeed", 1.0, 1, 2);
        doSlowZoom = builder.comment("Make zooming in more progressive").define("doSlowZoom", false);
        zoomBind = new KeyMapping(MOD_ID + "." + "zoom", GLFW.GLFW_KEY_Z, MOD_ID + "." + "zoom");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, builder.build());
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            ClientRegistry.registerKeyBinding(zoomBind);
            MinecraftForge.EVENT_BUS.addListener(this::onFOVModifierEvent);
        });

    }


    public void onFOVModifierEvent(EntityViewRenderEvent.FOVModifier event) {
        if (zoomBind.isDown()) {
            if (smoothCamera.get() && !isSmoothCameraOn) {
                setSmoothZoom(true);
            }
            if (doSlowZoom.get()) {
                if (zoomCount > zoomAmount.get()) {
                    zoomCount -= zoomSpeed.get();
                    if (zoomCount < zoomAmount.get()) {
                        zoomCount = zoomAmount.get();
                    }
                }
            }
            event.setFOV(zoomCount);
            return;
        }

        if (smoothCamera.get() && isSmoothCameraOn) {
            setSmoothZoom(false);
        }
        if (doSlowZoom.get()) {
            if (zoomCount < event.getFOV()) {
                zoomCount += zoomSpeed.get();
                if (zoomCount > event.getFOV()) {
                    zoomCount = event.getFOV();
                }
            }
            event.setFOV(zoomCount);
        } else {
            zoomCount = zoomAmount.get();
        }

    }

    private void setSmoothZoom(boolean smoothOn) {
        isSmoothCameraOn = smoothOn;
        if(Minecraft.getInstance() != null && Minecraft.getInstance().options != null) {
            Minecraft.getInstance().options.smoothCamera = smoothOn;
        }
    }


}
