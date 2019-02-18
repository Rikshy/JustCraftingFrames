package de.shyrik.modularitemframe.common.module.t3;

import de.shyrik.modularitemframe.ModularItemFrame;
import de.shyrik.modularitemframe.init.ConfigValues;
import de.shyrik.modularitemframe.api.ModuleBase;
import de.shyrik.modularitemframe.api.utils.RenderUtils;
import de.shyrik.modularitemframe.client.render.FrameRenderer;
import de.shyrik.modularitemframe.common.block.BlockModularFrame;
import de.shyrik.modularitemframe.common.block.TileModularFrame;
import de.shyrik.modularitemframe.common.network.NetworkHandler;
import de.shyrik.modularitemframe.common.network.packet.TeleportEffectPacket;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class ModuleTeleport extends ModuleBase {

    public static final ResourceLocation LOC = new ResourceLocation(ModularItemFrame.MOD_ID, "module_t3_tele");

    private static final String NBT_LINK = "linked_pos";
    private static final String NBT_LINKX = "linked_posX";
    private static final String NBT_LINKY = "linked_posY";
    private static final String NBT_LINKZ = "linked_posZ";

    private BlockPos linkedLoc = null;

    @Override
    public ResourceLocation getId() {
        return LOC;
    }

    @Nonnull
    @Override
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation frontTexture() {
        return new ResourceLocation(ModularItemFrame.MOD_ID, "blocks/module_t1_item");
    }

    @Override
    public String getModuleName() {
        return I18n.format("modularitemframe.module.tele");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void specialRendering(FrameRenderer renderer, double x, double y, double z, float partialTicks, int destroyStage) {
        RenderUtils.renderEnd(renderer, x, y, z, info -> {
            switch (tile.blockFacing()) {
                case DOWN:
                    info.buffer.pos(x + 0.85d, y + 0.08d, z + 0.85d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    info.buffer.pos(x + 0.85d, y + 0.08d, z + 0.14d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    info.buffer.pos(x + 0.14d, y + 0.08d, z + 0.14d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    info.buffer.pos(x + 0.14d, y + 0.08d, z + 0.85d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    break;
                case UP:
                    info.buffer.pos(x + 0.85d, y + 0.92d, z + 0.16d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    info.buffer.pos(x + 0.85d, y + 0.92d, z + 0.85d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    info.buffer.pos(x + 0.16d, y + 0.92d, z + 0.85d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    info.buffer.pos(x + 0.16d, y + 0.92d, z + 0.16d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    break;
                case NORTH:
                    info.buffer.pos(x + 0.85d, y + 0.85d, z + 0.08d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    info.buffer.pos(x + 0.14d, y + 0.85d, z + 0.08d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    info.buffer.pos(x + 0.14d, y + 0.14d, z + 0.08d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    info.buffer.pos(x + 0.85d, y + 0.14d, z + 0.08d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    break;
                case SOUTH:
                    info.buffer.pos(x + 0.14d, y + 0.85d, z + 0.92d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    info.buffer.pos(x + 0.85d, y + 0.85d, z + 0.92d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    info.buffer.pos(x + 0.85d, y + 0.14d, z + 0.92d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    info.buffer.pos(x + 0.14d, y + 0.14d, z + 0.92d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    break;
                case WEST:
                    info.buffer.pos(x + 0.08d, y + 0.85d, z + 0.16d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    info.buffer.pos(x + 0.08d, y + 0.85d, z + 0.85d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    info.buffer.pos(x + 0.08d, y + 0.16d, z + 0.85d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    info.buffer.pos(x + 0.08d, y + 0.16d, z + 0.16d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    break;
                case EAST:
                    info.buffer.pos(x + 0.92d, y + 0.85d, z + 0.85d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    info.buffer.pos(x + 0.92d, y + 0.85d, z + 0.16d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    info.buffer.pos(x + 0.92d, y + 0.16d, z + 0.16d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    info.buffer.pos(x + 0.92d, y + 0.16d, z + 0.85d).color(info.color1, info.color2, info.color3, 1.0F).endVertex();
                    break;
            }
            return true;
        });
    }

    @Override
    public boolean onBlockActivated(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn instanceof FakePlayer) return false;

        if (!worldIn.isRemote) {
            if (hasValidConnection(worldIn, playerIn)) {
                BlockPos target;
                if (tile.blockFacing().getAxis().isHorizontal() || tile.blockFacing() == EnumFacing.UP)
                    target = linkedLoc.offset(EnumFacing.DOWN);
                else target = linkedLoc;

                if (playerIn.isBeingRidden()) {
                    playerIn.removePassengers();
                }

                playerIn.stopRiding();

                if (playerIn.attemptTeleport(target.getX() + 0.5F, target.getY() + 0.5F, target.getZ() + 0.5F)) {
                    NetworkHandler.sendAround(new TeleportEffectPacket(playerIn.getPosition()), worldIn, playerIn.getPosition(), 32);

                    playerIn.rotationYaw = getRotationYaw(worldIn.getBlockState(linkedLoc).get(BlockModularFrame.FACING).getOpposite());

                    NetworkHandler.sendAround(new TeleportEffectPacket(target), worldIn, target, 32);
                }
            }
        }
        return true;
    }

    public static float getRotationYaw(EnumFacing facing) {
        switch (facing) {
            case NORTH:
                return 180f;
            case SOUTH:
                return 0f;
            case WEST:
                return 90f;
            case EAST:
                return -90f;
        }
        return 0f;
    }

    @Override
    public void screw(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer playerIn, ItemStack driver) {
        NBTTagCompound nbt = driver.getTag();
        if (playerIn.isSneaking()) {
            if (nbt == null) nbt = new NBTTagCompound();
            nbt.putLong(NBT_LINK, tile.getPos().toLong());
            driver.setTag(nbt);
            playerIn.sendMessage(new TextComponentTranslation("modularitemframe.message.loc_saved"));
        } else {
            if (nbt != null && nbt.hasUniqueId(NBT_LINK)) {
                BlockPos tmp = BlockPos.fromLong(nbt.getLong(NBT_LINK));
                if (tile.getPos().getDistance(tmp.getX(), tmp.getY(), tmp.getZ()) < 1) return;
                TileEntity targetTile = tile.getWorld().getTileEntity(tmp);
                int countRange = tile.getRangeUpCount();
                if (!(targetTile instanceof TileModularFrame) || !((((TileModularFrame) targetTile).module instanceof ModuleTeleport)))
                    playerIn.sendMessage(new TextComponentTranslation("modularitemframe.message.invalid_target"));
                else if (tile.getPos().getDistance(tmp.getX(), tmp.getY(), tmp.getZ()) > ConfigValues.BaseTeleportRange + (countRange * 10)) {
                    playerIn.sendMessage(new TextComponentTranslation("modularitemframe.message.too_far", ConfigValues.BaseTeleportRange + (countRange * 10)));
                } else {
                    linkedLoc = tmp;
                    ((ModuleTeleport) ((TileModularFrame) targetTile).module).linkedLoc = tile.getPos();
                    playerIn.sendMessage(new TextComponentTranslation("modularitemframe.message.link_established"));
                    nbt.remove(NBT_LINK);
                    driver.setTag(nbt);
                }
            }
        }
    }

    private boolean isTargetLocationValid(@Nonnull World worldIn) {

        if (tile.blockFacing().getAxis().isHorizontal() || tile.blockFacing() == EnumFacing.UP)
            return worldIn.isAirBlock(linkedLoc.offset(EnumFacing.DOWN));
        else return worldIn.isAirBlock(linkedLoc.offset(EnumFacing.UP));
    }

    private boolean hasValidConnection(@Nonnull World world, @Nullable EntityPlayer player) {
        if (linkedLoc == null) {
            if (player != null) player.sendMessage(new TextComponentTranslation("modularitemframe.message.no_target"));
            return false;
        }
        TileEntity targetTile = world.getTileEntity(linkedLoc);
        if (!(targetTile instanceof TileModularFrame) || !(((TileModularFrame) targetTile).module instanceof ModuleTeleport)) {
            if (player != null)
                player.sendMessage(new TextComponentTranslation("modularitemframe.message.invalid_target"));
            return false;
        }
        if (!isTargetLocationValid(world)) {
            if (player != null)
                player.sendMessage(new TextComponentTranslation("modularitemframe.message.location_blocked"));
            return false;
        }
        return true;
    }

    @Override
    public void onRemove(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, @Nullable EntityPlayer playerIn) {
        if (hasValidConnection(worldIn, null)) {
            ((ModuleTeleport) ((TileModularFrame) Objects.requireNonNull(worldIn.getTileEntity(linkedLoc))).module).linkedLoc = null;
        }
        super.onRemove(worldIn, pos, facing, playerIn);
    }

    @Nonnull
    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = super.serializeNBT();
        if (linkedLoc != null) {
            compound.putInt(NBT_LINKX, linkedLoc.getX());
            compound.putInt(NBT_LINKY, linkedLoc.getY());
            compound.putInt(NBT_LINKZ, linkedLoc.getZ());
        }
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        if (nbt.hasUniqueId(NBT_LINKX))
            linkedLoc = new BlockPos(nbt.getInt(NBT_LINKX), nbt.getInt(NBT_LINKY), nbt.getInt(NBT_LINKZ));
    }
}
