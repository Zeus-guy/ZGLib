package com.zeusguy.zglib.registry;

import com.zeusguy.zglib.ZGLib;
import com.zeusguy.zglib.item.EnergyItem;
import com.zeusguy.zglib.item.ModCreativeModeTab;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ZGLibItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ZGLib.MODID);
    
    public static final RegistryObject<Item> TEST_DRILL = ITEMS.register("test_drill", () -> new EnergyItem(new Item.Properties().tab(ModCreativeModeTab.ZGLIB_TAB)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
