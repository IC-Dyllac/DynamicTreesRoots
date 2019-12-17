package de.dumdidldum.dynamicTreesRoots.ritual;

import epicsquid.mysticallib.block.BlockCropBase;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.entity.ritual.EntityRitualBase;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.trees.Species;

import de.dumdidldum.dynamicTreesRoots.DynamicTreesRoots;

public class PatchedEntityRitualWildGrowth extends EntityRitualBase {
	
	private double x = 0;
	private double y = 0;
	private double z = 0;
	
	protected static Random random = new Random();
	protected static final DataParameter<Integer> lifetime = EntityDataManager
			.createKey(PatchedEntityRitualWildGrowth.class, DataSerializers.VARINT);

	public PatchedEntityRitualWildGrowth(World worldIn) {
		super(worldIn);
	    this.getDataManager().register(lifetime, RitualRegistry.ritual_wild_growth.getDuration() + 20);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		int curLifetime = getDataManager().get(lifetime);
		getDataManager().set(lifetime, curLifetime - 1);
		getDataManager().setDirty(lifetime);
		if (getDataManager().get(lifetime) < 0) {
			setDead();
		}
		if (!world.isRemote) {
			if (this.ticksExisted % 250 == 0) {
				List<BlockPos> eligiblePositions = Util.getBlocksWithinRadius(world, getPosition(), 10, 20, 10,
						(pos) -> {
							IBlockState state = world.getBlockState(pos);
							return state.getBlock() == ModBlocks.wildroot && state.getValue(BlockCropBase.AGE) == 7;
						});
				if (eligiblePositions.isEmpty())
					return;

				BlockPos pos = eligiblePositions.get(random.nextInt(eligiblePositions.size()));
				Species species = TreeRegistry.findSpecies(new ResourceLocation(DynamicTreesRoots.MODID, "wildwood"));			
				if (species != null) {
					world.setBlockToAir(pos);
					double x = pos.getX() + 0.5;
					double y = pos.getY() + 0.5;
					double z = pos.getZ() + 0.5;
					EntityItem itemEntity = new EntityItem(world, x, y, z, species.getSeedStack(random.nextInt(3) + 1));
					world.spawnEntity(itemEntity);
				}
			}
		}
	}

	@Override
	public DataParameter<Integer> getLifetime() {
		return lifetime;
	}

	@Override
	public void setPosition(double x, double y, double z) {
		super.setPosition(x, y, z);
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	protected void entityInit() {
	    this.posY = y;
	    this.posX = x;
	    this.posZ = z;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		this.x = compound.getDouble("x");
	    this.y = compound.getDouble("y");
	    this.z = compound.getDouble("z");
	    this.setEntityId(compound.getInteger("entity_id"));
	    this.setPosition(x, y, z);
	    getDataManager().set(getLifetime(), compound.getInteger("lifetime"));
	    getDataManager().setDirty(getLifetime());
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
	    compound.setDouble("x", x);
	    compound.setDouble("y", y);
	    compound.setDouble("z", z);
	    compound.setInteger("entity_id", getEntityId());
	    compound.setInteger("lifetime", getDataManager().get(getLifetime()));
	}
}
