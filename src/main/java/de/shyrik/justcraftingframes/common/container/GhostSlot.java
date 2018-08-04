package de.shyrik.justcraftingframes.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class GhostSlot extends SlotItemHandler {

	public GhostSlot(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		return false;
	}

	@Override
	public boolean isItemValid(@Nonnull ItemStack stack) {
		return true;
	}

	@Override
	public int getItemStackLimit(@Nonnull ItemStack stack) { return 1; }
}
