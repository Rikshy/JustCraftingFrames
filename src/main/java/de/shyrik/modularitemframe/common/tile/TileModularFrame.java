package de.shyrik.modularitemframe.common.tile;

import com.teamwizardry.librarianlib.features.autoregister.TileRegister;
import com.teamwizardry.librarianlib.features.base.block.tile.TileModTickable;
import com.teamwizardry.librarianlib.features.kotlin.CommonUtilMethods;
import de.shyrik.modularitemframe.api.UpgradeBase;
import de.shyrik.modularitemframe.api.ConfigValues;
import de.shyrik.modularitemframe.api.ModuleBase;
import de.shyrik.modularitemframe.api.ModuleRegistry;
import de.shyrik.modularitemframe.common.block.BlockModularFrame;
import de.shyrik.modularitemframe.common.module.ModuleEmpty;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@TileRegister("modular_frame")
public class TileModularFrame extends TileModTickable {

	private static final String NBTMODULE = "framemodule";
	private static final String NBTMODULEDATA = "framemoduledata";

	public ModuleBase module;
	public List<UpgradeBase> upgrades = new ArrayList<>();

	public TileModularFrame() {
		setModule(new ModuleEmpty());
	}

	public void setModule(ModuleBase mod) {
		module = mod;
		module.setTile(this);
	}

	public void addUpgrade(UpgradeBase up) {
	    upgrades.add(up);
	    module.onUpgradesChanged();
    }

	public EnumFacing blockFacing() {
		return world.getBlockState(pos).getValue(BlockModularFrame.FACING);
	}

	public TileEntity getNeighbor(EnumFacing facing) {
		return world.getTileEntity(pos.offset(facing));
	}

	public boolean acceptsModule() {
		return module instanceof ModuleEmpty;
	}

	public boolean acceptsUpgrade() {
        return upgrades.size() <= ConfigValues.MaxFrameUpgrades;
    }

    public int countUpgradeOfType(Class<? extends UpgradeBase> clsUp) {
	    int count = 0;
	    for (UpgradeBase up : upgrades) {
	        if (clsUp.isInstance(up))
	            count++;
        }
        return count;
    }

	@Override
	public void tick() {
		module.tick(world, pos);
	}

	@Override
	public void writeCustomNBT(@NotNull NBTTagCompound cmp, boolean sync) {
		super.writeCustomNBT(cmp, sync);
		cmp.setString(NBTMODULE, ModuleRegistry.getModuleId(module.getClass()));
		cmp.setTag(NBTMODULEDATA, module.serializeNBT());
	}

	@Override
	public void readCustomNBT(@NotNull NBTTagCompound cmp) {
		super.readCustomNBT(cmp);
		if (ModuleRegistry.getModuleId(module.getClass()).equals(cmp.getString(NBTMODULE))) {
			module.deserializeNBT(cmp.getCompoundTag(NBTMODULEDATA));
		} else {
			module = ModuleRegistry.createModuleInstance(cmp.getString(NBTMODULE));
			if (module == null) module = new ModuleEmpty();
			module.deserializeNBT(cmp.getCompoundTag(NBTMODULEDATA));
			module.setTile(this);
			cmp.removeTag(NBTMODULEDATA);
		}
	}

	@Override
	public void writeCustomBytes(@Nonnull ByteBuf buf, boolean sync) {
		if (module == null) CommonUtilMethods.writeNullSignature(buf);
		else {
			CommonUtilMethods.writeNonnullSignature(buf);
			CommonUtilMethods.writeString(buf, ModuleRegistry.getModuleId(module.getClass()));
			CommonUtilMethods.writeTag(buf, module.serializeNBT());
		}
	}

	@Override
	public void readCustomBytes(@Nonnull ByteBuf buf) {
		if (CommonUtilMethods.hasNullSignature(buf)) module = null;
		else {
			module = ModuleRegistry.createModuleInstance(CommonUtilMethods.readString(buf));
			if (module == null) module = new ModuleEmpty();
			module.setTile(this);
			module.deserializeNBT(CommonUtilMethods.readTag(buf));
		}
	}
}
