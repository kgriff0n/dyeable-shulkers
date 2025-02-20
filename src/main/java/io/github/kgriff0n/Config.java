package io.github.kgriff0n;

import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

public class Config {

    public static boolean canDyeBlock;
    public static boolean canDyeMob;
    public static boolean canRenameBlock;
    public static boolean canCleanShulker;

    public static Path configPath = FabricLoader.getInstance().getConfigDir();
    public static String properties = configPath + "/dyeable-shulkers.properties";

    public static void loadFile() {
        Properties lambdaConfigs = new Properties();
        try {
            lambdaConfigs.load(new FileInputStream(properties));
        } catch (IOException e) {
            DyeableShulkers.LOGGER.error("Can't load file.");
        }

        canDyeBlock = Boolean.parseBoolean(lambdaConfigs.getProperty("can_dye_block"));
        canDyeMob = Boolean.parseBoolean(lambdaConfigs.getProperty("can_dye_mob"));
        canRenameBlock = Boolean.parseBoolean(lambdaConfigs.getProperty("can_rename_block"));
        canCleanShulker = Boolean.parseBoolean(lambdaConfigs.getProperty("can_clean_shulker"));

    }

    public static boolean exist() {
        return new File(properties).exists();
    }

    public static void writeDefaultConfig() {

        try (Writer writer = new FileWriter(properties)) {
            writer.write("# Dyeable Shulkers configuration file\n\n");
            writer.write("# Don't edit the following line\n");
            writer.write("version=v1.1.0\n\n");

            writer.write("can_dye_block=true\n");
            writer.write("can_dye_mob=true\n");
            writer.write("can_rename_block=true\n");
            writer.write("can_clean_shulker=true\n");

        } catch (IOException e) {
            DyeableShulkers.LOGGER.info("Can't write file.");
        }
    }

    public static void save() {
        try (Writer writer = new FileWriter(properties)) {
            writer.write("# Dyeable Shulkers configuration file\n\n");
            writer.write("# Don't edit the following line\n");
            writer.write("version=v1.2.2\n\n");

            writer.write("can_dye_block=" + canDyeBlock + "\n");
            writer.write("can_dye_mob=" + canDyeMob + "\n");
            writer.write("can_rename_block=" + canRenameBlock + "\n");
            writer.write("can_clean_shulker=" + canCleanShulker + "\n");

        } catch (IOException e) {
            DyeableShulkers.LOGGER.info("Can't save file.");
        }
    }

    public static void createConfigFile() {
        File database = new File(properties);
        try {
            database.createNewFile();
        } catch (IOException e) {
            DyeableShulkers.LOGGER.error("Can't create file.");
        }
    }

}
