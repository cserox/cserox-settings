package org.delastochkin;

public interface SettingFilter<Q> {
    boolean check(Setting setting, Q query);
}
