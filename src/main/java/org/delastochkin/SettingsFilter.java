package org.delastochkin;

public interface SettingsFilter<T> {
    boolean isApplicable(Setting setting, T parameters);
}
