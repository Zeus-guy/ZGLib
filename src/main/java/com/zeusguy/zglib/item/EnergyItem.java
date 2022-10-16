package com.zeusguy.zglib.item;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class EnergyItem extends Item implements IEnergyItem {

    protected int max_energy, energy_per_use, energy_extract, energy_receive;

    public EnergyItem(Properties properties) {

        super(properties.setNoRepair().stacksTo(1));
        this.max_energy = 100;

    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {

        tooltip.add(Component.literal(getEnergy(stack) + "/" + getMaxEnergy(stack) + " FE"));

        if (getEnergyPerUse(stack) > 0) {
            tooltip.add(Component.translatable("info.zglib.energy_use_1").append(Component.literal(" " + getEnergyPerUse(stack) + " FE/")).append(Component.translatable("info.zglib.energy_use_2")));
        }

        List<Component> tooltips = addTooltips();
        if (!tooltips.isEmpty()) {
            tooltip.addAll(tooltips);
        }

        List<Component> extraTooltips = addExtraTooltips();
        if (!extraTooltips.isEmpty()) {
            if (Screen.hasShiftDown()) {
                tooltip.addAll(extraTooltips);
            } else {
                tooltip.add(Component.translatable("info.zglib.hold_shift").withStyle(ChatFormatting.YELLOW));
            }
        }

    }

    public List<Component> addTooltips() {
        return new ArrayList<Component>();
    }

    public List<Component> addExtraTooltips() {
        return new ArrayList<Component>();
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0xFF161D;
    }

    @Override
    public int getBarWidth(ItemStack stack) {

        if (stack.getTag() == null) {
            return 0;
        }
        return getScaledEnergy(stack, 13);

    }

    @Override
    public int getEnergyPerUse(ItemStack stack) {
        return energy_per_use;
    }

    @Override
    public int getExtract(ItemStack container) {
        return energy_extract;
    }

    @Override
    public int getReceive(ItemStack container) {
        return energy_receive;
    }

    @Override
    public int getMaxEnergy(ItemStack container) {
        return max_energy;
    }

    @Override
    public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> list) {

        if (allowedIn(tab)) {
            list.add(new ItemStack(this));

            ItemStack maxedItem = new ItemStack(this);
            setEnergy(maxedItem, getMaxEnergy(maxedItem));
            list.add(maxedItem);
        }

    }

}
