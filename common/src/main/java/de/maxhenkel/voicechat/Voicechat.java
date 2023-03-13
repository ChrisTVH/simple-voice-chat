package de.maxhenkel.voicechat;

import de.maxhenkel.configbuilder.ConfigBuilder;
import de.maxhenkel.voicechat.command.VoicechatCommands;
import de.maxhenkel.voicechat.config.ServerConfig;
import de.maxhenkel.voicechat.config.Translations;
import de.maxhenkel.voicechat.intercompatibility.CommonCompatibilityManager;
import de.maxhenkel.voicechat.plugins.PluginManager;
import de.maxhenkel.voicechat.voice.server.ServerVoiceEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.regex.Pattern;

public abstract class Voicechat {

    public static final String MODID = "voicechat";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static ServerVoiceEvents SERVER;
    public static ServerConfig SERVER_CONFIG;
    public static Translations TRANSLATIONS;

    public static int COMPATIBILITY_VERSION = BuildConstants.COMPATIBILITY_VERSION;

    public static final Pattern GROUP_REGEX = Pattern.compile("^[^\"\\n\\r\\t\\s][^\"\\n\\r\\t]{0,15}$");

    public void initialize() {
        if (debugMode()) {
            LOGGER.warn("Running in debug mode - Don't leave this enabled in production!");
        }

        LOGGER.info("Compatibility version {}", COMPATIBILITY_VERSION);

        TRANSLATIONS = ConfigBuilder.build(Path.of(".").resolve("config").resolve(MODID).resolve("translations.properties"), true, Translations::new);

        CommonCompatibilityManager.INSTANCE.getNetManager().init();
        SERVER = new ServerVoiceEvents();
        PluginManager.instance().init();
        CommonCompatibilityManager.INSTANCE.onRegisterServerCommands(VoicechatCommands::register);
    }

    public static void logDebug(String message, Object... objects) {
        if (debugMode()) {
            LOGGER.info(message, objects);
        }
    }

    public static boolean debugMode() {
        return CommonCompatibilityManager.INSTANCE.isDevEnvironment() || System.getProperty("voicechat.debug") != null;
    }

}
