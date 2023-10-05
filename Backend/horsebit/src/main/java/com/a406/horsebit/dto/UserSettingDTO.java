package com.a406.horsebit.dto;

public class UserSettingDTO {
	private boolean alarmOn;
	private boolean biometricLogin;

	public UserSettingDTO(boolean alarmOn, boolean biometricLogin) {
		this.alarmOn = alarmOn;
		this.biometricLogin = biometricLogin;
	}

	public boolean isAlarmOn() {
		return alarmOn;
	}

	public void setAlarmOn(boolean alarmOn) {
		this.alarmOn = alarmOn;
	}

	public boolean isBiometricLogin() {
		return biometricLogin;
	}

	public void setBiometricLogin(boolean biometricLogin) {
		this.biometricLogin = biometricLogin;
	}
}
