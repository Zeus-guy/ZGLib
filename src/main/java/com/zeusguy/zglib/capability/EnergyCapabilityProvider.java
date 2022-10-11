package com.zeusguy.zglib.capability;

import javax.annotation.Nullable;

import com.zeusguy.zglib.item.IEnergyItem;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyCapabilityProvider implements IEnergyStorage, ICapabilityProvider {

    private final Capability<? extends IEnergyStorage> capability;
    private final LazyOptional<IEnergyStorage> holder = LazyOptional.of(() -> this);

    protected final ItemStack container;
    protected final IEnergyItem item;

    public EnergyCapabilityProvider(ItemStack containerIn, IEnergyItem itemIn, Capability<? extends IEnergyStorage> capability) {

        this.container = containerIn;
        this.item = itemIn;
        this.capability = capability;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {

        if (!canReceive()) {
            return 0;
        }
        return item.receiveEnergy(container, maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {

        if (!canExtract()) {
            return 0;
        }
        return item.extractEnergy(container, maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {

        return item.getEnergy(container);
    }

    @Override
    public int getMaxEnergyStored() {

        return item.getMaxEnergy(container);
    }

    @Override
    public boolean canExtract() {

        return item.getExtract(container) > 0;
    }

    @Override
    public boolean canReceive() {

        return item.getReceive(container) > 0;
    }
    
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {

        if (cap == capability) {
            return holder.cast();
        }
        return LazyOptional.empty();
    }
    
}
