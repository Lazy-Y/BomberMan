package Server;

public class BMRealPlayer extends BMPlayer {
//	The human player of the game. Implement all the data variables and functions of BMPlayer class.
//	Function:
//		+ BMRealPlayer() 
//		+ getMessage(string msg) : void
//		- run() : void
//	Variables:
//		- BurfferedReader : br
//		- Socket : s
//		- PrintWriter : pw

	public BMRealPlayer(int ID, int initialLives, String name, boolean vip){
		super(ID, initialLives);
		username = name;
		this.vip = vip;
		if (vip) {
			speed+=2;
			power+=1;
			coolingTime-=1;
			detonatedTime-=1;
		}
		System.out.println("User's name " +username);
	}
}
