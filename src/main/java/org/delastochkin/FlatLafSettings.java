package org.delastochkin;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlatLafSettings extends Settings {
    private Map<String, String> preferences;
    private static final Collection<String> UI_ELEMENTS = List.of(
            "Button", "ToggleButton", "CheckBox", "ComboBox", "Label",
            "List", "MenuBar", "MenuItem", "Menu", "CheckBoxMenuItem", "RadioButtonMenuItem",
            "Panel", "PopupMenu", "ProgressBar", "RadioButton", "ScrollBar", "ScrollPane",
            "Separator", "Slider", "Spinner", "SplitPane", "TabbedPane", "Table", "TableHeader",
            "TextArea", "TextPane", "EditorPane", "TextField", "FormattedTextField", "PasswordField",
            "ToolBar", "ToolTip", "Tree", "Borders"
    );

    public FlatLafSettings(Settings base) {
        super(base);
    }

    public String toPreferences() {
        replaceColors();
        preferences = new LinkedHashMap<>();
        addUiPreferences();
        return preferencesToString();
    }

    private String preferencesToString() {
        StringBuilder builder = new StringBuilder();
        String prefix = "";
        for (Map.Entry<String, String> preference : preferences.entrySet()) {
            builder.append(prefix).append(preference.getKey()).append("=").append(preference.getValue());
            prefix = "\n";
        }
        return builder.toString();
    }

    private void addUiPreferences() {
        Settings ui = fetchElementWithName(this, "ui");
        addCommonUiPreferences(ui);
        ui.childElements().forEach(this::addUiElementPreferences);
    }

    private void addUiElementPreferences(Element element) {
        if (element.name().equals("*")) {
            return;
        }
        Map<String, Collection<Attribute>> attributePaths = new LinkedHashMap<>();
        Stack<Pair<String, Element>> elementPaths = new Stack<>();
        elementPaths.push(new Pair<>(element.name(), element));
        while(!elementPaths.isEmpty()) {
            Pair<String, Element> elementPath = elementPaths.pop();
            String path = elementPath.first;
            Element currentElement = elementPath.second;
            attributePaths.put(path, currentElement.value().childAttributes().collect(Collectors.toSet()));
            currentElement.value().childElements().forEach((child)->
                    elementPaths.push(new Pair<>(path + "." + child.name(), child)));
        }
        for(Map.Entry<String, Collection<Attribute>> entry : attributePaths.entrySet()) {
            String path = entry.getKey();
            Collection<Attribute> attributes = entry.getValue();
            for(Attribute attribute : attributes) {
                preferences.put(path + "." + attribute.name(), attribute.value());
            }
        }
    }

    private void addCommonUiPreferences(Settings ui) {
        Settings common = fetchElementWithName(ui, "*");
        common.childAttributes().forEach(this::addCommonUiPreference);
    }

    private void addCommonUiPreference(Attribute attribute) {
        for (String element : UI_ELEMENTS) {
            preferences.put(element + "." + attribute.name(), attribute.value());
        }
    }

    private Settings fetchElementWithName(Settings settings, String name) {
        return settings.fetchAll(this::elementWithName, name, Setting::element)
                .stream().findFirst().orElse(Optional.empty()).orElse(Settings.empty());
    }

    private boolean elementWithName(Setting setting, String name) {
        return setting.isElement() && setting.name().equals(name);
    }

    private void replaceColors() {
        Stream<Attribute> colors = fetchAll(this::isColor, this::attributes)
                .stream().findFirst().orElse(Stream.empty());
        colors.forEach(this::replaceColor);
        System.out.println("Всичко");
    }

    private void replaceColor(Attribute color) {
        Stream<Settings> usesColors = fetchAll(this::usesColor, color, Setting::element)
                .stream().filter(Optional::isPresent).map(Optional::get);
        Consumer<Settings> replaceColorInAttribute
                = (element) -> this.replaceColorInAttribute(element, color);
        usesColors.forEach(replaceColorInAttribute);
    }

    private void replaceColorInAttribute(Settings element, Attribute color) {
        element.childAttributes().forEach((attribute) -> {
            if (attribute.value().equals(color.name())) {
                element.setAttribute(attribute.name(), color.value());
            }
        });
    }

    private boolean usesColor(Setting setting, Attribute color) {
        return setting.element().orElse(Settings.empty()).childAttributes().anyMatch(
                (attribute -> attribute.value().equals(color.name()))
        );
    }

    private boolean isColor(Setting setting) {
        return setting.element().isPresent() && setting.name().equals("colors");
    }

    private Stream<Attribute> attributes(Setting setting) {
        return setting.element().orElse(Settings.empty()).childAttributes();
    }
}
