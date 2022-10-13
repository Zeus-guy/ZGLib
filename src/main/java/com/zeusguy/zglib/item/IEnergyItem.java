package com.zeusguy.zglib.item;

import javax.annotation.Nullable;

import com.zeusguy.zglib.capability.EnergyCapabilityProvider;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.extensions.IForgeItem;

public interface IEnergyItem extends IForgeItem {

    
    default boolean hasEnergy(ItemStack stack) {

        return getEnergy(stack) >= getEnergyPerUse(stack);
    }

    abstract int getEnergyPerUse(ItemStack stack);

    default int getRemainingEnergy(ItemStack container) {

        return getMaxEnergy(container) - getEnergy(container);
    }

    default int getScaledEnergy(ItemStack container, int scale) {

        return (int)Math.round((double) getEnergy(container) * scale / getMaxEnergy(container));
    }

    default int getEnergy(ItemStack container) {

        CompoundTag tag = container.getOrCreateTag();
        return Math.min(tag.getInt("Energy"), getMaxEnergy(container));
    }
    
    default void setEnergy(ItemStack container, int energy) {

        CompoundTag tag = container.getOrCreateTag();
        tag.putInt("Energy", Math.max(0, Math.min(energy, getMaxEnergy(container))));
    }

    default int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {

        CompoundTag tag = container.getOrCreateTag();
        int stored = Math.min(tag.getInt("Energy"), getMaxEnergy(container));
        int receive = Math.min(Math.min(maxReceive, getReceive(container)), getRemainingEnergy(container));

        if (!simulate) {
            stored += receive;
            tag.putInt("Energy", stored);
        }
        return receive;
    }

    default int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {

        CompoundTag tag = container.getOrCreateTag();
        int stored = Math.min(tag.getInt("Energy"), getMaxEnergy(container));
        int extract = Math.min(Math.min(maxExtract, getExtract(container)), stored);

        if (!simulate) {
            stored -= extract;
            tag.putInt("Energy", stored);
        }
        return extract;
    }
    
    @Override
    default ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {

        return new EnergyCapabilityProvider(stack, this, ForgeCapabilities.ENERGY);
    }
    
    abstract int getExtract(ItemStack container);

    abstract int getReceive(ItemStack container);
    
    abstract int getMaxEnergy(ItemStack container);
}
