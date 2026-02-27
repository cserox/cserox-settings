package org.delastochkin;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Settings {
    private final static Settings EMPTY_SETTINGS = new Empty();
    protected final Map<String, Setting> settings;

    public Settings() {
        this.settings = new LinkedHashMap<>();
    }

    protected Settings(Settings base) {
        this.settings = new LinkedHashMap<>(base.settings);
    }

    public static Settings fromJsonString(String input) throws IOException {
        return new JsonParser(input).getSettings();
    }

    protected static Settings empty() {
        return EMPTY_SETTINGS;
    }

    public Settings newElement(String name) {
        Settings element = new Settings();
        settings.put(name, Setting.element(name, element));
        return element;
    }

    public void setAttribute(String name, String value){
        settings.put(name, Setting.attribute(name, value));
    }

    public <T, Q> Collection<T> fetchAll(SettingFilter<Q> filter, Q query, Function<Setting, T> getter) {
        return fetchAll((setting) -> filter.check(setting, query), getter);
    }

    public <T> Collection<T> fetchAll(Predicate<Setting> filter, Function<Setting, T> mapper) {
        return fetch(filter, mapper, true);
    }

    public <T> Collection<T> fetchDirectChildren(Predicate<Setting> filter, Function<Setting, T> mapper) {
        return fetch(filter, mapper, false);
    }

    public <T, Q> Collection<T> fetchDirectChildren(SettingFilter<Q> filter, Q query, Function<Setting, T> getter) {
        return fetchDirectChildren((setting) -> filter.check(setting, query), getter);
    }

    protected <T> Collection<T> fetch(Predicate<Setting> filter, Function<Setting, T> mapper, boolean all) {
        Collection<T> fetched = new ArrayList<>();
        Stack<Settings> settingsToCheck = new Stack<>();
        settingsToCheck.push(this);
        while(!settingsToCheck.isEmpty()) {
            Settings settingToCheck = settingsToCheck.pop();
            settingToCheck.settingStream()
                    .filter(filter)
                    .map(mapper)
                    .forEach(fetched::add);
            if(all) {
                settingToCheck.settingStream()
                        .map(Setting::element)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(settingsToCheck::push);
            }
        }
        return fetched;
    }

    protected Stream<Setting> settingStream() {
        return settings.values().stream();
    }

    public Optional<String> attribute(String name) {
        return setting(name).flatMap(Setting::attribute);
    }

    public Optional<Settings> element(String name) {
        return setting(name).flatMap(Setting::element);
    }

    protected Optional<Setting> setting(String name) {
        return Optional.ofNullable(settings.get(name));
    }

    public Stream<Attribute> childAttributes() {
        return fetchDirectChildren(Setting::isAttribute, Setting::self).stream().map(Attribute.class::cast);
    }

    public Stream<Element> childElements() {
        return fetchDirectChildren(Setting::isElement, Setting::self).stream().map(Element.class::cast);
    }

    public String toJsonString() {
        StringBuilder builder = new StringBuilder("{");
        String prefix = "\n";
        for (Map.Entry<String, Setting> entry : settings.entrySet()) {
            Setting setting = entry.getValue();
            String settingJsonString;
            if (setting.element().isPresent()) {
                settingJsonString = setting.element().get().toJsonString();
            } else {
                settingJsonString = String.format("\"%s\"", setting.attribute().orElseThrow());
            }
            builder.append(prefix)
                    .append("\"")
                    .append(entry.getKey())
                    .append("\": ")
                    .append(settingJsonString);
            prefix = ",\n";
        }
        builder.append("\n}");
        return builder.toString();
    }

    private static class Empty extends Settings {
        @Override
        public Settings newElement(String name) {
            throw new RuntimeException("Empty settings are immutable");
        }

        @Override
        public void setAttribute(String name, String value) {
            throw new RuntimeException("Empty settings are immutable");
        }

        @Override
        public Optional<Settings> element(String name) {
            return Optional.empty();
        }

        @Override
        public Optional<String> attribute(String name) {
            return Optional.empty();
        }

        @Override
        public Optional<Setting> setting(String name) {
            return Optional.empty();
        }

        @Override
        public String toJsonString() {
            return "{}";
        }

        @Override
        protected <T> Collection<T> fetch(Predicate<Setting> filter, Function<Setting, T> mapper, boolean all) {
            return Collections.emptySet();
        }

        @Override
        protected Stream<Setting> settingStream() {
            return Stream.empty();
        }
    }
}
