/**
 *                     GNU GENERAL PUBLIC LICENSE
 *                         Version 3, 2015
 *
 *   Copyright (C) 2015 by KevinZeng87@126.com.
 *   Everyone is permitted to copy and distribute verbatim copies
 *   of this license document, but changing and profiting it is not allowed.
 */

package com.hermes.busconfig.feature;

/**
 * 
 * @author KevinZeng
 * 
 */
public class IviConfig {

	/* ************************************************************************
	 * Configurations
	 */
	/**
	 * Speed Volume
	 */
	public interface ISpeedVolume {
		static final String SETTING_KEY = "speed_volume";
		static final int SETTING_VALUE_CLOSE = 3;
		static final int SETTING_VALUE_HIGHT = 2;
		static final int SETTING_VALUE_MIDDLE = 1;
		static final int SETTING_VALUE_LOW = 0;
	}

	/**
	 * Audio Stream State
	 */
	public interface IAudioStreamState {

	}

	/**
	 * Scales for Adjusting
	 */
	public interface IAdjustScale {
		/** the scale of adjusting the lightness of screen once */
		static final int SCREEN_BRIGHTNESS_ADJUST_SCALE = 10;
		/** the scale of adjusting the sound volume of navigator tts */
		static final int NAVI_TTS_VOLUME_ADJUST_SCALE = 2;
		/** the scale of adjusting the sound volume of music */
		static final int MUSIC_VOLUME_ADJUST_SCALE = 2;
		/** the scale of adjusting the sound volume of default */
		static final int DEFAULT_VOLUME_ADJUST_SCALE = 2;
	}
}
