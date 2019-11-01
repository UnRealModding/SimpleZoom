package com.unrealdinnerbone.simplezoom;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("simplezoom")
public class SimpleZoom
{
    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static ForgeConfigSpec.IntValue zoomAmount;
    private static SimpleZoom INSTANCE;


    private static final Logger LOGGER = LogManager.getLogger();

    public SimpleZoom() {
        INSTANCE = this;
        builder.push("client");
        zoomAmount = builder.comment("Zoom Amount").defineInRange("zoomamount", 90, -180, 180);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, builder.build());
        DistExecutor.runWhenOn(Dist.CLIENT, () -> SimpleZoomClient::new);

    }

    public static SimpleZoom getInstance() {
        return INSTANCE;
    }


}

