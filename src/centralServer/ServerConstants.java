package centralServer;

public class ServerConstants {
	
	public static final String LOGIN = "login";
	public static final String SIGNUP = "signup";
	public static final String LOGOUT = "logout";
	public static final String DISCONNECT = "disconnect";
	public static final String SUCCESSFULLOGIN = " SUCCESFULLY LOGGED IN | ";
	public static final String LOGINFAILED = " LOGIN FAILED | ";
	public static final String SUCCESSFULLOGOUT = " SUCCESFULLY LOGGED OUT | ";
	public static final String LOGOUTFAILED = " LOGOUT FAILED | ";
	public static final String SUCCESSFULSIGNUP = " SUCCESFULLY SIGNED UP | ";
	public static final String SIGNUPFAILED = " SIGNUP FAILED | ";
	
	public static final String REQUESTPERSONALRECORDS = "Request for Personal Record";
	public static final String REQUESTWORLDRANKING = "Request for World Rankings";
	public static final String SENDINGPERSONALRECORD = "Sending Personal Record | ";
	public static final String SENDINGWORLDRANKING = "Sending World Ranking | ";
	public static final String CENTRALSERVERUPDATED = "Ranks and Records Successfully Updated";
	
	public static final String WorldRankingFetchFailure = "Error retrieving World Ranking";
	public static final String PersonalRankingFetchFailure = "Error retrieving Personal Record";
	public static final String CannotCompleteRequest = "Cannot complete request: ";
	public static final String NotLoggedIn = "Not Logged In to Server";
	public static final String NotConnected = "Not Connected to Server";
	
	public static final String VIPSTATUSREQUEST = "VIP Status Request Received | ";
	public static final String UPGRADETOVIP = "Upgrade to VIP | ";
	public static final String VIPSTATUSTRUE = "User is a VIP";
	public static final String VIPSTATUSFALSE = "User is not a VIP";
	
	public static final String GenericSQLException = "Error: Could not complete access to MySQL Database | ";
	
	
	/*
	 * PortGUI and other GUI Constants (copied from Factory code)
	 */
	public static final int lowPort = 0;
	public static final int highPort = 65535;
	public static final int defaultPort = 6789;
	public static final String defaultHostname = "localhost";
	
	public static final String portDescriptionString = "<html>Enter the port number on which<br />you would like the server to listen.</html>";
	public static final String portLabelString = "Port";
	public static final String submitPortString = "Start Listening";
	public static final String portGUITitleString = "Factory Server - Port";
	public static final int portGUIwidth = 300;
	public static final int portGUIheight = 170;
	public static final String portErrorString = "<html><font color=\"red\">Please enter a valid port<br />between " + lowPort + " and " + highPort + "</font></html>";
	public static final String portAlreadyInUseString = "<html><font color=\"red\">Port already in use.  Select another port<br />between " + lowPort + " and " + highPort + "</font></html>";
	
	public static final String startClientConnectedString = "Client with IP address ";
	public static final String endClientConnectedString = " connected.";
	public static final String clientDisconnected = "BMClient disconnected | ";
	
	public static final String connectionToMySQL = "Successful Connection to MySQL | ";
	public static final String failedConnectionToMySQL = "Failed to Connect to MySQL | ";
	public static final String disconnectedFromMySQL = "Disconnecting from MySQL | ";
	public static final String failedDisconnectFromMySQL = "Failed to Disconnect from MySQL | ";
	
	//For the BMCentralServerClient
	public static final String hostAndPortDescriptionString = "<html>Enter the hostname and port number of the Central Server</html>";
	public static final String hostnameLabelString = "Hostname";
	public static final String connectButtonString = "Connect";
	public static final int hostAndPortGUIwidth = 330;
	public static final int hostAndPortGUIheight = 200;
	public static final String unableToConnectString = "<html><font color=\"red\">Unable to connect to host.</font></html>";
	public static final String unableToGetStreams = "Could not get reader and writer for socket";
	
	/*
	 * Map and Updating constants
	 */
	public static final String usernameString = "username";
	public static final String pointsString = "points";
	public static final String killString = "kill";
	public static final String deathString = "death";
	
}
