package com.zeusguy.zglib;

import com.zeusguy.zglib.recipe.ModRecipes;
import com.zeusguy.zglib.registry.ZGLibItems;

//import org.slf4j.Logger;

//import com.mojang.logging.LogUtils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ZGLib.MODID)
public class ZGLib {
    public static final String MODID = "zglib";
    //private static final Logger LOGGER = LogUtils.getLogger();

    public ZGLib() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ZGLibItems.register(modEventBus);

        ModRecipes.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}
