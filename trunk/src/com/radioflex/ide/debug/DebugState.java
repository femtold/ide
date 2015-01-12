package com.radioflex.ide.debug;

public class DebugState {
	private static int state = 1;

	public static int getState() {
		return state;
	}

	public static void setState(int state1) {
		state = state1;
	}

}
