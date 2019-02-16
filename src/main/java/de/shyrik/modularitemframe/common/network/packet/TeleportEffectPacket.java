package de.shyrik.modularitemframe.common.network.packet;

import de.shyrik.modularitemframe.common.network.NetworkHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TeleportEffectPacket implements IMessage, IMessageHandler<TeleportEffectPacket, IMessage> {
    private BlockPos pos;

    public TeleportEffectPacket() {
    }

    public TeleportEffectPacket(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IMessage onMessage(TeleportEffectPacket message, MessageContext ctx) {
        NetworkHandler.getThreadListener(ctx).addScheduledTask(() -> {
            Minecraft mc = Minecraft.getMinecraft();
            mc.ingameGUI.getBossOverlay().clearBossInfos();
            mc.getSoundHandler().playSound(new PositionedSoundRecord(SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.AMBIENT, 0.4F, 1F, message.pos));
            for (int i = 0; i < 128; i++) {
                mc.world.spawnParticle(EnumParticleTypes.PORTAL, message.pos.getX() + (mc.world.rand.nextDouble() - 0.5) * 3, message.pos.getY() + mc.world.rand.nextDouble() * 3, message.pos.getZ() + (mc.world.rand.nextDouble() - 0.5) * 3, (mc.world.rand.nextDouble() - 0.5) * 2, -mc.world.rand.nextDouble(), (mc.world.rand.nextDouble() - 0.5) * 2);
            }
        });
        return null;
    }
}
