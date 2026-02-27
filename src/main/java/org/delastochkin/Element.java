package org.delastochkin;

import java.util.Optional;

public class Element extends Setting {
    private final Settings value;

    protected Element(String name, Settings value) {
        super(name);
        this.value = value;
    }

    @Override
    public boolean isAttribute() {
        return false;
    }

    @Override
    public boolean isElement() {
        return true;
    }

    @Override
    public Optional<String> attribute() {
        return Optional.empty();
    }

    @Override
    public Optional<Settings> element() {
        return Optional.of(value);
    }

    @Override
    public Settings value() {
        return value;
    }
}
