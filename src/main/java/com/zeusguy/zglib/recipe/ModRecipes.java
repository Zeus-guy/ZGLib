package com.zeusguy.zglib.recipe;


import com.zeusguy.zglib.ZGLib;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ZGLib.MODID);
    
    public static final RegistryObject<RecipeSerializer<EnergyKeepingRecipe>> ENERGY_KEEPING_RECIPE_SERIALIZER = SERIALIZERS.register("energy_keeping_recipe", () -> EnergyKeepingRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
