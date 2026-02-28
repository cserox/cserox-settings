package org.delastochkin;

public class JsonBuilder {
    private final StringBuilder builder;
    private final Settings settings;
    private final String offset;
    private String prefix;

    private JsonBuilder(Settings settings, String offset) {
        this.offset = offset;
        this.builder = new StringBuilder("{");
        this.settings = settings;
        this.prefix = "";
    }

    public static String fromSettings(Settings settings) {
        return new JsonBuilder(settings, "").fromSettings();
    }

    private String fromSettings() {
        settings.settingStream().forEach(this::addSettingToBuilder);
        builder.append("\n").append(offset).append("}");
        return builder.toString();
    }

    private void addSettingToBuilder(Setting setting) {
        builder.append(prefix)
				.append("\n")
                .append(offset)
                .append('\t')
                .append(putInDoubleQuotes(setting.name()))
                .append(": ")
                .append(setting.element().map(this::fromSettingsNextOffset)
                        .orElse(putInDoubleQuotes(setting.value())));
        prefix = ",";
    }

    private String fromSettingsNextOffset(Settings settings) {
        return new JsonBuilder(settings, offset + "\t").fromSettings();
    }

    private String putInDoubleQuotes(Object object) {
        return String.format(String.format("\"%s\"", object));
    }

}
