package centralServer;

import java.io.Serializable;

/*
 * Container class for the ranking data for a a single player
 */
public class RankContainer implements Serializable {
	private static final long serialVersionUID = 6412772210281108230L;
	
	private double maxpoints = 0;
	private String username = null;
	
	public RankContainer(String username, double maxpoints) {
		this.username = username;
		this.maxpoints = maxpoints;
	}
	/*
	 * Setters
	 */
	public void setMaxPoints(double mp) {
		maxpoints = mp;
		}
	public void setUsername(String s) {
		username = s;
	}
	
	/*
	 * Getters
	 */
	public double getRating() {
		return maxpoints;
	}
	public String getUsername() {
		return username;
	}
}
