package org.delastochkin;

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

    public static Settings empty() {
        return EMPTY_SETTINGS;
    }

    public void setAttribute(String name, String value){
        settings.put(name, Setting.attribute(name, value));
    }

    public Settings newElement(String name) {
        Settings element = new Settings();
        settings.put(name, Setting.element(name, element));
        return element;
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

    public <T, Q> Stream<T> fetch(SettingFilter<Q> filter, Q query, Function<Setting, T> getter) {
        return fetch((setting) -> filter.check(setting, query), getter);
    }

    public <T> Stream<T> fetch(Predicate<Setting> filter, Function<Setting, T> mapper) {
        Stream<T> fetched = Stream.<T>builder().build();
        Stack<Settings> settingsToCheck = new Stack<>();
        settingsToCheck.push(this);
        while(!settingsToCheck.isEmpty()) {
            Settings settingToCheck = settingsToCheck.pop();
            fetched = Stream.concat(fetched, settingToCheck.children(filter, mapper));
            settingToCheck.childElements().map(Element::value).forEach(settingsToCheck::push);
        }
        return fetched;
    }

    public Stream<Attribute> childAttributes() {
        return children(Setting::isAttribute, Attribute.class::cast);
    }

    public Stream<Element> childElements() {
        return children(Setting::isElement, Element.class::cast);
    }

    protected <T> Stream<T> children(Predicate<Setting> filter, Function<Setting, T> caster) {
        return settingStream().filter(filter).map(caster);
    }

    protected Stream<Setting> settingStream() {
        return settings.values().stream();
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
        protected Stream<Setting> settingStream() {
            return Stream.empty();
        }
    }
}
