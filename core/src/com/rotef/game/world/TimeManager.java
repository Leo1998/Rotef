package com.rotef.game.world;

public class TimeManager {

	public enum Time {
		Day, Night;
	}

	/**
	 * How many seconds a single day take
	 */
	private static final int SECONDS_PER_DAY = 120;

	private static final int SUNSET_TIME = 7000;
	private static final int SUNRISE_TIME = 19000;

	private float totalWorldSeconds = 0.0f;
	private World world;

	/**
	 * the daytime from 0 to 24000
	 */
	private int daytime = 0;
	/**
	 * Whether its Day or Night
	 */
	private Time time;
	/**
	 * the intensity the sun should have regarding to the daytime
	 */
	private float sunIntensity = 1.0f;

	public TimeManager(World world) {
		this.world = world;
	}

	public void update(float delta) {
		this.totalWorldSeconds += delta;

		this.daytime = (int) (totalWorldSeconds * (24000 / SECONDS_PER_DAY));

		updateTime();
		updateSunIntensity();
	}

	private void updateTime() {
		int daytime = getDaytime();

		if (daytime > SUNSET_TIME && daytime < SUNRISE_TIME) {
			this.time = Time.Night;
		} else {
			this.time = Time.Day;
		}
	}

	private void updateSunIntensity() {
		int daytime = getDaytime();

		if (time == Time.Day) {
			sunIntensity = 1.0f;
		}
		if (time == Time.Night) {
			sunIntensity = 0.0f;
		}

		final int CHANGING_TIME = 1000;
		{
			int sunsetDif = daytime - SUNSET_TIME + CHANGING_TIME;
			if (sunsetDif < CHANGING_TIME && sunsetDif > 0) {
				sunIntensity = (CHANGING_TIME - (float) sunsetDif) / CHANGING_TIME;
			}
		}
		{
			int sunriseDif = SUNRISE_TIME - daytime;
			if (sunriseDif < CHANGING_TIME && sunriseDif > 0) {
				sunIntensity = (CHANGING_TIME - (float) sunriseDif) / CHANGING_TIME;
			}
		}
	}

	public float getTotalWorldSeconds() {
		return totalWorldSeconds;
	}

	/**
	 * Only use for setting saved time!
	 * 
	 * @param totalWorldSeconds
	 */
	public void setTotalWorldSeconds(float totalWorldSeconds) {
		this.totalWorldSeconds = totalWorldSeconds;
	}

	public World getWorld() {
		return world;
	}

	public int getDaytime() {
		return daytime % 24000;
	}

	public int getDay() {
		return daytime / 24000;
	}

	public void setDaytime(int daytime) {
		this.daytime = daytime;
	}

	public Time getTime() {
		return time;
	}

	public float getSunIntensity() {
		return sunIntensity;
	}

}
