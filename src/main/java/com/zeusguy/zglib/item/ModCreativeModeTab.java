package com.zeusguy.zglib.item;

import com.zeusguy.zglib.registry.ZGLibItems;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab ZGLIB_TAB = new CreativeModeTab("zglibtab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ZGLibItems.TEST_DRILL.get());
        }
    };
}
