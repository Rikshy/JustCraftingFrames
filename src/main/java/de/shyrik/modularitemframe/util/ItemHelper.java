package de.shyrik.modularitemframe.util;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class ItemHelper {

    public static boolean areItemsEqual(ItemStack stack, ItemStack stack2) {
        return stack.getItem() == stack2.getItem();
    }

    public static boolean simpleAreStacksEqual(ItemStack stack, ItemStack stack2) {
        return stack.getItem() == stack2.getItem() && stack.getDamage() == stack2.getDamage();
    }

    public static void ejectStack(@NotNull World world, @NotNull BlockPos pos, @NotNull Direction facing, ItemStack stack) {
        Vector3d position = new Vector3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        Vector3d velocity = Vector3d.ZERO;

        switch (facing) {
            case UP:
                position = position.add(0.0D, -0.25D, 0.0D);
                break;
            case DOWN:
                position = position.add(0.0D, -0.25D, 0.0D);
                velocity = velocity.add(0.0D, 0.2D, 0.0D);
                break;
            case WEST:
                position = position.add(0.25D, -0.25D, 0.0D);
                velocity = velocity.add(-0.2D, 0.0D, 0.0D);
                break;
            case EAST:
                position = position.add(-0.25D, -0.25D, 0.0D);
                velocity = velocity.add(0.2D, 0.0D, 0.0D);
                break;
            case NORTH:
                position = position.add(0.0D, -0.25D, 0.25D);
                velocity = velocity.add(0.0D, 0.0D, -0.2D);
                break;
            case SOUTH:
                position = position.add(0.0D, -0.25D, -0.25D);
                velocity = velocity.add(0.0D, 0.0D, 0.2D);
                break;
        }

        ItemEntity item = new ItemEntity(world, position.x, position.y, position.z, stack);
        item.setVelocity(velocity.x, velocity.y, velocity.z);
        world.addEntity(item);
    }


    public static ICraftingRecipe getRecipe(IItemHandler itemHandler, World world) {
        CraftingInventory craft = new CraftingInventory(new Container(ContainerType.CRAFTING, world.rand.nextInt()) {
            @Override
            public boolean canInteractWith(@NotNull PlayerEntity playerIn) {
                return false;
            }
        }, 3, 3);

        for (int i = 0; i < 9; i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);

            if (stack.isEmpty())
                continue;

            craft.setInventorySlotContents(i, stack.copy());
        }

        return world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, craft, world).orElse(null);
    }
}
