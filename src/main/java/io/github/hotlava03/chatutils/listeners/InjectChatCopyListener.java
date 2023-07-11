package io.github.hotlava03.chatutils.listeners;

import io.github.hotlava03.chatutils.fileio.ChatUtilsConfig;
import io.github.hotlava03.chatutils.events.ReceiveMessageCallback;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.*;

import java.util.List;
import java.util.regex.Pattern;

import static io.github.hotlava03.chatutils.util.StringUtils.textToLegacy;
import static io.github.hotlava03.chatutils.util.StringUtils.translateAlternateColorCodes;

public class InjectChatCopyListener implements ReceiveMessageCallback {
    // Taken from https://github.com/SpigotMC/BungeeCord
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§([0-9A-FK-OR]|#[a-f0-9]{6})");

    @Override
    public void accept(Text text, List<ChatHudLine.Visible> lines) {
        String tooltip;
        String toCopy = textToLegacy(text, ChatUtilsConfig.COPY_HEX_COLORS.value());
        if (!ChatUtilsConfig.COPY_COLORS.value()) {
            toCopy = STRIP_COLOR_PATTERN.matcher(toCopy).replaceAll("");
        } else {
            toCopy = STRIP_COLOR_PATTERN.matcher(toCopy).replaceAll(matchResult ->
                    matchResult.group().replace("§", "&"));
        }

        if (ChatUtilsConfig.PREVIEW_CONTENT.value()) {
            tooltip = translateAlternateColorCodes(
                    ChatUtilsConfig.COPY_TO_CLIPBOARD_MESSAGE.value() + "\n\n&9Preview:\n&f") + toCopy;
        } else {
            tooltip = translateAlternateColorCodes(ChatUtilsConfig.COPY_TO_CLIPBOARD_MESSAGE.value());
        }

        Style style = text.getStyle();
        if (ChatUtilsConfig.TOOLTIP_ENABLED.value()) {
            style = style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    Text.literal(tooltip)));
        }

        if (text.getStyle().getClickEvent() == null && ChatUtilsConfig.ENABLED.value()) {
            ((MutableText) text).setStyle(style);
        }
    }
}
