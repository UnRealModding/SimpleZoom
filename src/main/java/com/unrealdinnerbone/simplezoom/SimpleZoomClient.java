package com.unrealdinnerbone.simplezoom;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class SimpleZoomClient
{
    private static KeyBinding BINDING;

    public SimpleZoomClient() {
        BINDING = new KeyBinding("Zoom", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_Z, "keygroup.simplezoom");
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

    }

    private void clientSetup(FMLClientSetupEvent clientSetupEvent) {
        ClientRegistry.registerKeyBinding(BINDING);

    }

    public static class ClientEvents {
        @SubscribeEvent
        public void onFovUpdate(FOVUpdateEvent event) {
            if(BINDING.isKeyDown()) {
                event.setNewfov(event.getFov() - SimpleZoom.zoomAmount.get());
            }
        }
    }


}
