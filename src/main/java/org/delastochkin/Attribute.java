package org.delastochkin;

import java.util.Optional;

public class Attribute extends Setting {
    private final String value;
    protected Attribute(String name, String value) {
        super(name);
        this.value = value;
    }

    @Override
    public boolean isAttribute() {
        return true;
    }

    @Override
    public boolean isElement() {
        return false;
    }

    @Override
    public Optional<String> attribute() {
        return Optional.of(value);
    }

    @Override
    public Optional<Settings> element() {
        return Optional.empty();
    }

    @Override
    public String value() {
        return value;
    }
}
