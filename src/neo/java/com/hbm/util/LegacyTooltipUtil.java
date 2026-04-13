package com.hbm.util;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class LegacyTooltipUtil {
    private static final ChatFormatting DEFAULT_TOOLTIP_COLOR = ChatFormatting.GRAY;

    private LegacyTooltipUtil() {
    }

    public static void appendLegacyDescription(Item item, ItemStack stack, List<Component> tooltipComponents) {
        Language language = Language.getInstance();
        String descriptionId = item.getDescriptionId(stack);
        String legacyDescriptionId = "item." + BuiltInRegistries.ITEM.getKey(item).getPath();

        if (appendDescriptionGroup(language, descriptionId, tooltipComponents)) {
            return;
        }

        appendDescriptionGroup(language, legacyDescriptionId, tooltipComponents);
    }

    private static boolean appendDescriptionGroup(Language language, String baseKey, List<Component> tooltipComponents) {
        boolean appended = false;
        String descKey = baseKey + ".desc";

        if (language.has(descKey)) {
            appendTranslatedLines(language.getOrDefault(descKey), tooltipComponents);
            appended = true;
        }

        for (int index = 1; language.has(baseKey + ".desc" + index); index++) {
            appendTranslatedLines(language.getOrDefault(baseKey + ".desc" + index), tooltipComponents);
            appended = true;
        }

        return appended;
    }

    private static void appendTranslatedLines(String translatedText, List<Component> tooltipComponents) {
        for (String line : translatedText.split("\\$")) {
            tooltipComponents.add(parseLegacyFormatting(line));
        }
    }

    private static Component parseLegacyFormatting(String text) {
        MutableComponent component = Component.empty();
        Style style = Style.EMPTY.withColor(DEFAULT_TOOLTIP_COLOR);
        StringBuilder chunk = new StringBuilder();
        String normalized = text.replace("Ã‚Â§", "\u00A7");

        for (int index = 0; index < normalized.length(); index++) {
            char current = normalized.charAt(index);
            if (current == '\u00A7' && index + 1 < normalized.length()) {
                if (chunk.length() > 0) {
                    component.append(Component.literal(chunk.toString()).setStyle(style));
                    chunk.setLength(0);
                }

                ChatFormatting formatting = ChatFormatting.getByCode(normalized.charAt(++index));
                if (formatting != null) {
                    style = applyLegacyFormatting(style, formatting);
                    continue;
                }

                chunk.append('\u00A7').append(normalized.charAt(index));
                continue;
            }

            chunk.append(current);
        }

        if (chunk.length() > 0) {
            component.append(Component.literal(chunk.toString()).setStyle(style));
        }

        return component;
    }

    private static Style applyLegacyFormatting(Style currentStyle, ChatFormatting formatting) {
        if (formatting == ChatFormatting.RESET) {
            return Style.EMPTY.withColor(DEFAULT_TOOLTIP_COLOR);
        }

        if (formatting.isColor()) {
            return Style.EMPTY.withColor(formatting);
        }

        return switch (formatting) {
            case BOLD -> currentStyle.withBold(true);
            case ITALIC -> currentStyle.withItalic(true);
            case UNDERLINE -> currentStyle.withUnderlined(true);
            case STRIKETHROUGH -> currentStyle.withStrikethrough(true);
            case OBFUSCATED -> currentStyle.withObfuscated(true);
            default -> currentStyle;
        };
    }
}
