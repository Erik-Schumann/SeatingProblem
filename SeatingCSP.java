
import java.util.ArrayList;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * class to read in user input (via file) and create problem, search agent and start serach)
 */
public class SeatingCSP {
	/**
	 * function to get the user input in properties file and start program
	 * @param args not used
	 */
	public static void main(String[] args) {
		//RelationshipMatrix relMat = new RelationshipMatrix(12);
		int N= 4;//Number Tables
		int M= 4;//Table capacity
		//initialize variables
		String[] names= null;
		ArrayList<GuestPair> friends= null;
		ArrayList<Guest> guests = null;
		ArrayList<GuestPair> enemies = null;
		
		//Read in file
		try (InputStream input = new FileInputStream(System.getProperty("user.dir")+"/Input.properties")) {

			//create Properties object
            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property value of N and M
            N = Integer.valueOf(prop.getProperty("NumTables"));
            M = Integer.valueOf(prop.getProperty("TableSize"));
            
            //get guests
            String guests_raw = prop.getProperty("Guests");
            names = guests_raw.split(";");
            //create guests and add initial domain
            guests = new ArrayList<Guest>();
    		for(String name:names) {
    			Guest g1 = new Guest(name);
    			for(int i= 1; i<=N; i++) {
    				g1.addDomain(i+"");
    			}
    			guests.add(g1);
    		}
            
            //get and create friends
            String friends_raw = prop.getProperty("Friends");
            String[] friends_pair_splitted = friends_raw.split(";");
            friends = new ArrayList<GuestPair>();
            for(String frnds:friends_pair_splitted) {
            	String[] friends_splitted = frnds.split("&");
            	friends.add(new GuestPair(friends_splitted[0], friends_splitted[1]));
            }
            
          //get and create enemies
            String enemies_raw = prop.getProperty("Enemies");
            String[] enemies_pair_splitted = enemies_raw.split(";");
            enemies = new ArrayList<GuestPair>();
            for(String enms:enemies_pair_splitted) {
            	String[] enemies_splitted = enms.split("&");
            	enemies.add(new GuestPair(enemies_splitted[0], enemies_splitted[1]));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
		//If you add someone as friends and as enemies, they will be counted as enemies
		//create relationship-matrix
		GuestRelationship rel = new GuestRelationship(guests,friends,enemies);
		//create seating problem
		SeatingProblem prob = new SeatingProblem(guests, rel, N, M);
		//create serach agent
		SearchAgent agent = new SearchAgent(prob);
		//start search
		agent.search();
	}
}
