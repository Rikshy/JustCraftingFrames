package de.shyrik.modularitemframe.common.compat;

import de.shyrik.modularitemframe.common.block.BlockModularFrame;
import de.shyrik.modularitemframe.common.tile.TileModularFrame;
import mcmultipart.api.multipart.IMultipart;
import mcmultipart.api.multipart.IMultipartTile;
import mcmultipart.api.multipart.MultipartHelper;
import mcmultipart.api.slot.EnumFaceSlot;
import mcmultipart.block.BlockMultipartContainer;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CompatHelper {

    public static List<TileEntity> getTiles(World world, BlockPos pos) {
        List<TileEntity> re = new ArrayList<>();
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileModularFrame) {
            re = Collections.singletonList(te);
        } else {
            if (Loader.isModLoaded("mcmultipart")) {
                for (EnumFacing face : EnumFacing.values()) {
                    Optional<TileEntity> optte = getPartTile(world, pos, face);
                    if (optte.isPresent()) re.add(optte.get());
                }
            }
        }
        return re;
    }

    public static Optional<TileEntity> getTile(World world, BlockPos pos, EnumFacing face) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileModularFrame) {
            return Optional.of(te);
        } else {
            if (Loader.isModLoaded("mcmultipart")) {
                return getPartTile(world, pos, face);
            }
        }
        return Optional.empty();
    }

    @net.minecraftforge.fml.common.Optional.Method(modid = "mcmultipart")
    private static Optional<TileEntity> getPartTile(World world, BlockPos pos, EnumFacing face) {
        return MultipartHelper.getPartTile(world, pos, EnumFaceSlot.fromFace(face)).map(IMultipartTile::getTileEntity);
    }

    public static Optional<Block> getBlock(World world, BlockPos pos, EnumFacing side) {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof BlockModularFrame) {
            return  Optional.of(block);
        } else {
            if (Loader.isModLoaded("mcmultipart")) {
                return getPartBlock(world, pos, side);
            }
        }
        return Optional.empty();
    }

    @net.minecraftforge.fml.common.Optional.Method(modid = "mcmultipart")
    private static Optional<Block> getPartBlock(World world, BlockPos pos, EnumFacing side) {
        return MultipartHelper.getPart(world, pos,  EnumFaceSlot.fromFace(side)).map(IMultipart::getBlock);
    }

    public static boolean canPlace(World world, BlockPos pos, EnumFacing face) {
        Block block = world.getBlockState(pos).getBlock();
        if (block instanceof BlockModularFrame) {
            return false;
        } else {
            if (Loader.isModLoaded("mcmultipart")) {
                return canPlaceOnPart(world, pos, face, block);
            }
        }
        return true;
    }

    @net.minecraftforge.fml.common.Optional.Method(modid = "mcmultipart")
    private static boolean canPlaceOnPart(World world, BlockPos pos, EnumFacing face, Block block) {
        if (block instanceof BlockMultipartContainer) {
            Optional<Block> partBlock = getPartBlock(world, pos, face);
            if (!partBlock.isPresent())
                return false;
            if (partBlock.get() instanceof BlockModularFrame)
                return false;
        }
        return true;
    }
}
