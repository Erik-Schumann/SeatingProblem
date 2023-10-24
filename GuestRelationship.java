
import java.util.ArrayList;
import java.util.List;

/**
 * class to answer everything about the guests and their relationships
 */
public class GuestRelationship{
	int[][]relationships;
	ArrayList<String>  guestList;
	int guestCount;
	/**
	 * Constructor to create the relationship class
	 * @param guests everyone invited to the wedding
	 * @param friends couples which should sit together
	 * @param enemies couple which cannot sit together
	 */
	public GuestRelationship(ArrayList<Guest>guests, ArrayList<GuestPair> friends, ArrayList<GuestPair> enemies) {
		relationships= new int[guests.size()][guests.size()];
		this.guestList = new ArrayList<String>();
		guestCount= guests.size();
		//add guests to guestList
		for(Guest guest: guests) {
			guestList.add(guest.getName());
		}
		//initialize matrix
		initMat();
		//set friends
		for(GuestPair friend:friends) {
			setFriends(friend.getName1(), friend.getName2());
		}
		//set enemies
		for(GuestPair enemie:enemies) {
			setEnemies(enemie.getName1(), enemie.getName2());
		}
	}
	/**
	 * function to initialize matrix (a person is not allowed to sit with their selves)
	 */
	public void initMat() {
		for(int i= 0; i<guestCount; i++) {
			relationships[i][i]=-1;
		}
	}
	/**
	 * function to print relationship matrix (function was necessary within the development phase-> could be important to feature updates)
	 */
	public void printMatrix() {
		int i= 0;
		for(int[] row: relationships) {
			i++;
			System.out.print(String.format("%-10s", guestList.get(i-1)));
			int j= 1;
			for(int col: row) {
				System.out.print(" "+String.format("%-2s", Integer.toString(relationships[i-1][j-1])));
				j++;
			}
			System.out.println("");
			
		}
	}
	/**
	 * function to set friends badge in matrix
	 * @param name of first friend
	 * @param name2 of second friend
	 */
	public void setFriends(String name, String name2) {
		if(isOnGuestlist(name)&&isOnGuestlist(name2)) {
			relationships[guestList.indexOf(name)][guestList.indexOf(name2)]=1;
			relationships[guestList.indexOf(name2)][guestList.indexOf(name)]=1;
		}
	}
	/**
	 * function to set enemy badge in matrix
	 * @param name of first enemy
	 * @param name2 of second enemy
	 */
	public void setEnemies(String name, String name2) {
		if(isOnGuestlist(name)&&isOnGuestlist(name2)) {
			relationships[guestList.indexOf(name)][guestList.indexOf(name2)]=-1;
			relationships[guestList.indexOf(name2)][guestList.indexOf(name)]=-1;
		}
	}
	/**
	 * function to check is someone is actually on the guestList
	 * @param name of the guest to be checked
	 * @return boolean, if person is on the guestList
	 */
	public boolean isOnGuestlist(String name) {
		return guestList.contains(name);
	}
	/**
	 * function to get the relationship of a couple
	 * 0 = no relationship
	 * 1 = need to sit together
	 * -1= cannot sit together
	 * @param name of first person
	 * @param name2 of second person
	 * @return boolean indicating the relationship
	 */
	public int canSitTogether(String name, String name2) {
		return relationships[guestList.indexOf(name)][guestList.indexOf(name2)];
	}
	/**
	 * function to count the constraints of a person
	 * @param guestName name of the guest
	 * @return count of constraints
	 */
	public int getCntContraints(String guestName) {
		int i = 0;
		int[] row = relationships[guestList.indexOf(guestName)];
		for(int cons: row) {
			i+=Math.abs(cons);
		}
		return i;
	}
	/**
	 * function to get the most constraining guest of a group of guests
	 * @param guests group
	 * @return guest who has the most constraints
	 */
	public Guest getMostConstrainingGuest(ArrayList<Guest> guests) {
		//initialize values
		Guest mostContraining=null;
		int constraintsCnt=0;
		//iterate through every guest, if he/she has more constraints than the stored person, set him/her as guest, set constraint counter
		for(Guest guest:guests) {
			if(getCntContraints(guest.getName())>constraintsCnt) {
				constraintsCnt = getCntContraints(guest.getName());
				mostContraining = guest;
			}
		}
		//return most constraining guest
		return mostContraining;
	}
	/**
	 * function to get the guest with the smallest domain
	 * @param guests group
	 * @return the guest with the lowest domain count
	 */
	public int getLowestDomainCount(List<Guest> guests) {
		//initialize values
		int domainCnt=50000;
		//iterate through every guest, if he/she has a lower domain count than the stored person, set him/her as guest, set domain counter
		for(Guest guest:guests) {
			if(guest.getDomain().size()<domainCnt) {
				domainCnt = guest.getDomain().size();
			}
		}
		return domainCnt;
	}
}
