package io.github.kgriff0n.event;

import io.github.kgriff0n.Config;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class DyeShulkerBox implements UseBlockCallback {

    private static final Map<Integer, BlockState> SHULKER_MAP = Util.make(new HashMap<>(), map -> {
        map.put(0, Blocks.WHITE_SHULKER_BOX.getDefaultState());
        map.put(1, Blocks.ORANGE_SHULKER_BOX.getDefaultState());
        map.put(2, Blocks.MAGENTA_SHULKER_BOX.getDefaultState());
        map.put(3, Blocks.LIGHT_BLUE_SHULKER_BOX.getDefaultState());
        map.put(4, Blocks.YELLOW_SHULKER_BOX.getDefaultState());
        map.put(5, Blocks.LIME_SHULKER_BOX.getDefaultState());
        map.put(6, Blocks.PINK_SHULKER_BOX.getDefaultState());
        map.put(7, Blocks.GRAY_SHULKER_BOX.getDefaultState());
        map.put(8, Blocks.LIGHT_GRAY_SHULKER_BOX.getDefaultState());
        map.put(9, Blocks.CYAN_SHULKER_BOX.getDefaultState());
        map.put(10, Blocks.PURPLE_SHULKER_BOX.getDefaultState());
        map.put(11, Blocks.BLUE_SHULKER_BOX.getDefaultState());
        map.put(12, Blocks.BROWN_SHULKER_BOX.getDefaultState());
        map.put(13, Blocks.GREEN_SHULKER_BOX.getDefaultState());
        map.put(14, Blocks.RED_SHULKER_BOX.getDefaultState());
        map.put(15, Blocks.BLACK_SHULKER_BOX.getDefaultState());
        map.put(16, Blocks.SHULKER_BOX.getDefaultState());
    });

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        ItemStack itemStack = player.getMainHandStack();
        Item item = itemStack.getItem();
        if (block instanceof ShulkerBoxBlock && hand == Hand.MAIN_HAND) {
            if (Config.canDyeBlock && item instanceof DyeItem dye && SHULKER_MAP.get((dye).getColor().getId()) != block.getDefaultState()) {
                ShulkerBoxBlockEntity oldShulkerBox = (ShulkerBoxBlockEntity) world.getBlockEntity(pos);
                NbtCompound nbt = oldShulkerBox.createNbt(world.getRegistryManager());
                DyeColor color = ((DyeItem) item).getColor();

                world.setBlockState(pos, SHULKER_MAP.get(color.getId()).with(Properties.FACING, blockState.get(Properties.FACING)));
                ShulkerBoxBlockEntity newShulkerBox = (ShulkerBoxBlockEntity) world.getBlockEntity(pos);
                newShulkerBox.read(nbt, world.getRegistryManager());

                if (!player.isCreative()) {
                    player.getMainHandStack().decrement(1);
                }

                return ActionResult.SUCCESS_SERVER;

            } else if (Config.canDyeBlock && item == Items.BRUSH && block.getDefaultState() != Blocks.SHULKER_BOX.getDefaultState()) {
                ShulkerBoxBlockEntity oldShulkerBox = (ShulkerBoxBlockEntity) world.getBlockEntity(pos);
                NbtCompound nbt = oldShulkerBox.createNbt(world.getRegistryManager());

                if (!player.isCreative()) {
                    player.getMainHandStack().damage(1, player);
                }

                world.setBlockState(pos, SHULKER_MAP.get(16).with(Properties.FACING, blockState.get(Properties.FACING)));
                ShulkerBoxBlockEntity newShulkerBox = (ShulkerBoxBlockEntity) world.getBlockEntity(pos);
                newShulkerBox.read(nbt, world.getRegistryManager());

                return ActionResult.SUCCESS_SERVER;
            } else if (Config.canRenameBlock && item == Items.NAME_TAG) {
                if (itemStack.getComponents().contains(DataComponentTypes.CUSTOM_NAME)) {
                    ShulkerBoxBlockEntity shulkerBox = (ShulkerBoxBlockEntity) world.getBlockEntity(pos);
                    NbtCompound nbt = shulkerBox.createNbt(world.getRegistryManager());
                    nbt.putString("CustomName", Text.Serialization.toJsonString(itemStack.getComponents().get(DataComponentTypes.CUSTOM_NAME), world.getRegistryManager()));
                    shulkerBox.read(nbt, world.getRegistryManager());
                    shulkerBox.markDirty();
                    if (!player.isCreative()) {
                        player.getMainHandStack().decrement(1);
                    }
                    return ActionResult.SUCCESS_SERVER;
                }
            }
        }
        return ActionResult.PASS;
    }
}
