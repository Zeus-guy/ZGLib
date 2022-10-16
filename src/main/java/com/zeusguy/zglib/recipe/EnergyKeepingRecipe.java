package com.zeusguy.zglib.recipe;

import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.zeusguy.zglib.ZGLib;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class EnergyKeepingRecipe implements CraftingRecipe {
    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;

    public EnergyKeepingRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(CraftingContainer container, Level p_44003_) {
        for(int i = 0; i <= container.getWidth() - 3; ++i) {
           for(int j = 0; j <= container.getHeight() - 3; ++j) {
              if (this.matches(container, i, j, true)) {
                 return true;
              }
  
              if (this.matches(container, i, j, false)) {
                 return true;
              }
           }
        }
  
        return false;
    }

    private boolean matches(CraftingContainer p_44171_, int p_44172_, int p_44173_, boolean p_44174_) {
        for(int i = 0; i < p_44171_.getWidth(); ++i) {
           for(int j = 0; j < p_44171_.getHeight(); ++j) {
              int k = i - p_44172_;
              int l = j - p_44173_;
              Ingredient ingredient = Ingredient.EMPTY;
              if (k >= 0 && l >= 0 && k < 3 && l < 3) {
                 if (p_44174_) {
                    ingredient = this.recipeItems.get(3 - k - 1 + l * 3);
                 } else {
                    ingredient = this.recipeItems.get(k + l * 3);
                 }
              }
  
              if (!ingredient.test(p_44171_.getItem(i + j * p_44171_.getWidth()))) {
                 return false;
              }
           }
        }
  
        return true;
     }

    @Override
    public ItemStack assemble(CraftingContainer container) {
        
        int energy = 0;
        ItemStack item = output.copy();
        
        for (int i = 0; i < container.getContainerSize(); i++) {
            energy += container.getItem(i).getOrCreateTag().getInt("Energy");
        }
        
        item.getOrCreateTag().putInt("Energy", energy);

        return item;
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return output;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    static Map<String, Ingredient> keyFromJson(JsonObject p_44211_) {
        Map<String, Ingredient> map = Maps.newHashMap();

        for (Map.Entry<String, JsonElement> entry : p_44211_.entrySet()) {
            if (entry.getKey().length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + (String) entry.getKey()
                        + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            map.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    static String[] shrink(String... p_44187_) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for (int i1 = 0; i1 < p_44187_.length; ++i1) {
            String s = p_44187_[i1];
            i = Math.min(i, firstNonSpace(s));
            int j1 = lastNonSpace(s);
            j = Math.max(j, j1);
            if (j1 < 0) {
                if (k == i1) {
                    ++k;
                }

                ++l;
            } else {
                l = 0;
            }
        }

        if (p_44187_.length == l) {
            return new String[0];
        } else {
            String[] astring = new String[p_44187_.length - l - k];

            for (int k1 = 0; k1 < astring.length; ++k1) {
                astring[k1] = p_44187_[k1 + k].substring(i, j + 1);
            }

            return astring;
        }
    }

    private static int firstNonSpace(String p_44185_) {
        int i;
        for (i = 0; i < p_44185_.length() && p_44185_.charAt(i) == ' '; ++i) {
        }

        return i;
    }

    private static int lastNonSpace(String p_44201_) {
        int i;
        for (i = p_44201_.length() - 1; i >= 0 && p_44201_.charAt(i) == ' '; --i) {
        }

        return i;
    }

    static String[] patternFromJson(JsonArray p_44197_) {
        String[] astring = new String[p_44197_.size()];
        if (astring.length > 3) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, " + 3 + " is maximum");
        } else if (astring.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for (int i = 0; i < astring.length; ++i) {
                String s = GsonHelper.convertToString(p_44197_.get(i), "pattern[" + i + "]");
                if (s.length() > 3) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, " + 3 + " is maximum");
                }

                if (i > 0 && astring[0].length() != s.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                astring[i] = s;
            }

            return astring;
        }
    }

    static NonNullList<Ingredient> dissolvePattern(String[] p_44203_, Map<String, Ingredient> p_44204_, int p_44205_,
            int p_44206_) {
        NonNullList<Ingredient> nonnulllist = NonNullList.withSize(p_44205_ * p_44206_, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(p_44204_.keySet());
        set.remove(" ");

        for (int i = 0; i < p_44203_.length; ++i) {
            for (int j = 0; j < p_44203_[i].length(); ++j) {
                String s = p_44203_[i].substring(j, j + 1);
                Ingredient ingredient = p_44204_.get(s);
                if (ingredient == null) {
                    throw new JsonSyntaxException(
                            "Pattern references symbol '" + s + "' but it's not defined in the key");
                }

                set.remove(s);
                nonnulllist.set(j + p_44205_ * i, ingredient);
            }
        }

        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return nonnulllist;
        }
    }

    public static ItemStack itemStackFromJson(JsonObject p_151275_) {
        return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(p_151275_, true, true);
    }


    public static class Serializer implements RecipeSerializer<EnergyKeepingRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(ZGLib.MODID, "energy_keeping_recipe");

        @Override
        public EnergyKeepingRecipe fromJson(ResourceLocation id, JsonObject json) {
           
            Map<String, Ingredient> map = EnergyKeepingRecipe.keyFromJson(GsonHelper.getAsJsonObject(json, "key"));
            String[] astring = EnergyKeepingRecipe
                    .shrink(EnergyKeepingRecipe.patternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));
            int i = astring[0].length();
            int j = astring.length;
            NonNullList<Ingredient> nonnulllist = EnergyKeepingRecipe.dissolvePattern(astring, map, i, j);
            ItemStack itemstack = EnergyKeepingRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            
            return new EnergyKeepingRecipe(id, itemstack, nonnulllist);
        }

        @Override
        public @Nullable EnergyKeepingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(9, Ingredient.EMPTY);

            for (int k = 0; k < nonnulllist.size(); ++k) {
                nonnulllist.set(k, Ingredient.fromNetwork(buffer));
            }

            ItemStack itemstack = buffer.readItem();
            return new EnergyKeepingRecipe(id, itemstack, nonnulllist);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, EnergyKeepingRecipe recipe) {
            for (Ingredient ingredient : recipe.recipeItems) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeItem(recipe.output);

        }
    }

}