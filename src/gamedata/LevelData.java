package gamedata;

import java.util.Map;

/**
 * Holds all the data that is encoded for a level
 * such as:
 * 1) Enemies
 * 2) Preferences (e.g., waves of attack)
 * 3) Difficulty
 * 4) Duration of enemy attack
 * 5) Health, attack and speed multipliers
 * (more of each with higher levels)
 * 
 * @author Maddie
 *
 */
public class LevelData {
	private PreferencesData myPreferences;
	private Map<ActorData, Integer> troops;
	private int difficulty;
	private double duration; //duration for enemy presence
	
	//Increase by level
	private double healthMultiplier;
	private double attackMultiplier;
	private double speedMultiplier;

	public LevelData(Map<ActorData, Integer> troops){
		//TODO: Implement constructors
	}

	public PreferencesData getMyPreferences() {
		return myPreferences;
	}

	public void setMyPreferences(PreferencesData myPreferences) {
		this.myPreferences = myPreferences;
	}

	public Map<ActorData, Integer> getTroops() {
		return troops;
	}

	public void setTroops(Map<ActorData, Integer> troops) {
		this.troops = troops;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public double getHealthMultiplier() {
		return healthMultiplier;
	}

	public void setHealthMultiplier(double healthMultiplier) {
		this.healthMultiplier = healthMultiplier;
	}

	public double getAttackMultiplier() {
		return attackMultiplier;
	}

	public void setAttackMultiplier(double attackMultiplier) {
		this.attackMultiplier = attackMultiplier;
	}

	public double getSpeedMultiplier() {
		return speedMultiplier;
	}

	public void setSpeedMultiplier(double speedMultiplier) {
		this.speedMultiplier = speedMultiplier;
	}
	
	
	
}
