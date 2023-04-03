package io.github.hotlava03.chatutils;

import io.github.hotlava03.chatutils.events.SendMessageEvent;
import io.github.hotlava03.chatutils.fileio.ChatStorage;
import io.github.hotlava03.chatutils.fileio.ChatUtilsConfig;
import io.github.hotlava03.chatutils.events.ReceiveMessageEvent;
import io.github.hotlava03.chatutils.events.SendCommandEvent;
import io.github.hotlava03.chatutils.listeners.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import org.apache.logging.log4j.LogManager;

public class ChatUtilsMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // To prevent HeadlessExceptions when copying to clipboard.
        System.setProperty("java.awt.headless", "false");
        ChatUtilsConfig.loadFromFile();
        ChatStorage.getInstance().load();

        // Register Fabric API events.
        ClientPlayConnectionEvents.INIT.register(new RetrieveChatListener());

        // Register custom events.
        ReceiveMessageEvent.LISTENERS.register(new AntiSpamListener());
        ReceiveMessageEvent.LISTENERS.register(new CopyChatListener());
        ReceiveMessageEvent.LISTENERS.register(new ChatPersistListener());
        SendCommandEvent.LISTENERS.register(new CopyToClipboardListener());
        SendMessageEvent.LISTENERS.register(new SendMessageListener());

        LogManager.getLogger().info("Started!");
    }
}
