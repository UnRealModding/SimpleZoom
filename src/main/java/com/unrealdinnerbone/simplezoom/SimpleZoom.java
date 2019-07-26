package com.unrealdinnerbone.simplezoom;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

@Mod("simplezoom")
@Mod.EventBusSubscriber
public class SimpleZoom
{
    private static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    private static ForgeConfigSpec.IntValue zoomAmount;
    private final KeyBinding BINDING;
    private static SimpleZoom INSTANCE;


    private static final Logger LOGGER = LogManager.getLogger();

    public SimpleZoom() {
        INSTANCE = this;
        BINDING = new KeyBinding("Zoom", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_Z, "keygroup.simplezoom");
        builder.push("client");
        zoomAmount = builder.comment("Zoom Amount").defineInRange("zoomamount", 90, -180, 180);
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, builder.build());

    }

    private void clientSetup(FMLClientSetupEvent clientSetupEvent) {
        LOGGER.info("Loading Client....");
        ClientRegistry.registerKeyBinding(BINDING);

    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Loading....");
    }


    public static SimpleZoom getInstance() {
        return INSTANCE;
    }

    @SubscribeEvent
    public static void onFovUpdate(FOVUpdateEvent event) {
        if(SimpleZoom.getInstance().BINDING.isKeyDown()) {
            event.setNewfov(event.getFov() - 90);
        }
    }


}
