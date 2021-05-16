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

    public final ForgeConfigSpec.DoubleValue zoomAmount;
    public final ForgeConfigSpec.BooleanValue smoothCamera;
    public final ForgeConfigSpec.DoubleValue zoomSpeed;
    public final ForgeConfigSpec.BooleanValue doSlowZoom;


    public SimpleZoom() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("client");
        zoomAmount = builder.comment("The Amount that will be zoomed in").defineInRange("zoomAmount", 5.0, 0, Integer.MAX_VALUE);
        smoothCamera = builder.comment("Toggle cinematic camera on while zooming").define("smoothCamera", true);
        zoomSpeed = builder.comment("The speed at witch the camera will be zoomed", "doSlowZoom must be in order to work").defineInRange("zoomSpeed", 1.0, 1, 2);
        doSlowZoom = builder.comment("Make zooming in more progressive").define("doSlowZoom", false);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, builder.build());
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            new SimpleZoomClient(this);
        });

    }
}
