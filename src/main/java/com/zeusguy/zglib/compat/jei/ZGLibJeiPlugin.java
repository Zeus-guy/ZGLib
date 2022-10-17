package com.zeusguy.zglib.compat.jei;

import com.zeusguy.zglib.ZGLib;
import com.zeusguy.zglib.registry.ZGLibItems;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

@JeiPlugin
public class ZGLibJeiPlugin implements IModPlugin {

    public static final IIngredientSubtypeInterpreter<ItemStack> NBT_INTERPRETER = (stack, context) -> { 
        if (context == UidContext.Ingredient) {
            if (stack.getOrCreateTag().getInt("Energy") != 0) {
                return "filled";
            } else {
                return "empty";
            }
        }
        return IIngredientSubtypeInterpreter.NONE;
    };

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ZGLib.MODID, "jei_plugin");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        for (RegistryObject<Item> registryItem : ZGLibItems.ITEMS.getEntries()) {
            registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, registryItem.get(), NBT_INTERPRETER);
        }
    }
    
}
