package org.delastochkin;

public class TryingToModifyEmptySettingsError extends RuntimeException {
	public TryingToModifyEmptySettingsError() {
		super("Empty settings object can't be modified");
	}
}
