package org.delastochkin;

import java.util.Optional;

public abstract class Setting {
    public static Setting element(String name, Settings element) {
        return new Element(name, element);
    }

    public static Setting attribute(String name, String attribute) {
        return new Attribute(name, attribute);
    }

    public abstract boolean isAttribute();
    public abstract boolean isElement();
    public abstract Optional<String> attribute();
    public abstract Optional<Settings> element();
    public abstract Object value();

    private final String name;
    protected Setting(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public Setting self() {
        return this;
    }

	@Override
	public String toString() {
		return value().toString();
	}
}
