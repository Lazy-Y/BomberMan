package centralServer;

import java.io.Serializable;
import java.sql.Timestamp;

public class GameRecord implements Serializable {

	private static final long serialVersionUID = 4064181985723480070L;
	
	double points;
	int kills;
	int deaths;
	Timestamp time;
	
	public GameRecord(double points, int kills, int deaths, Timestamp time) {
		this.points = points;
		this.kills = kills;
		this.deaths = deaths;
		this.time = time;
	}
	
	/*
	 * Setters
	 */
	public void setPoints(double points) {
		this.points = points;
	}
	public void setKillCount(int kills) {
		this.kills = kills;
	}
	public void setDeathCount(int deaths) {
		this.deaths = deaths;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	
	/*
	 * Getters
	 */
	public double getPoints() {
		return points;
	}
	public int getKillCount() {
		return kills;
	}
	public int getDeathCount() {
		return deaths;
	}
	public Timestamp getTime() {
		return time;
	}

}
