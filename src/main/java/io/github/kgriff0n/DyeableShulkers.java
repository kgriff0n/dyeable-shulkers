package io.github.kgriff0n;

import io.github.kgriff0n.command.ConfigCommand;
import io.github.kgriff0n.event.CleanShulker;
import io.github.kgriff0n.event.DyeShulkerBox;
import io.github.kgriff0n.event.ServerStop;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.nbt.NbtCompound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DyeableShulkers implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("dyeable-shulkers");

	@Override
	public void onInitialize() {

		if (!Config.exist()) {
			Config.createConfigFile();
			Config.writeDefaultConfig();
		}

		Config.loadFile();

		ConfigCommand.register();

		UseEntityCallback.EVENT.register(new CleanShulker());
		UseBlockCallback.EVENT.register(new DyeShulkerBox());
		ServerLifecycleEvents.SERVER_STOPPED.register(new ServerStop());
	}

	public static void setColor(ShulkerEntity entity, int color) {
		NbtCompound nbt = new NbtCompound();
		entity.writeCustomDataToNbt(nbt);
		nbt.putInt("Color", color);
		entity.readCustomDataFromNbt(nbt);
	}
}