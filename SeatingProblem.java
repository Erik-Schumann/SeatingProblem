
import java.util.ArrayList;

/**
 * class to encode the problem of a seating problem
 */
public class SeatingProblem {
	//initialize values
	GuestRelationship rel;
	int N= 4;//Number Tables
	int M= 5;//Table capacity
	ArrayList<Guest> guests;
	SeatingState initState;

	/**
	 * Constructor to create problem
	 * @param guests group
	 * @param rel relationship-matrix of the guests
	 * @param N Number Tables
	 * @param M Size of Tables
	 */
	public SeatingProblem(ArrayList<Guest>guests, GuestRelationship rel, int N, int M){
		this.N = N;
		this.M = M;
		this.guests= guests;
		this.rel = rel;
		//create new initial state
		initState = new SeatingState(guests, N, M, rel);
	}
	/**
	 * function to get successor nodes of a seating state based on a selected guest (variable) and its domain
	 * @param state predecessor state
	 * @param guest selected guest
	 * @return List of successor nodes
	 */
	public ArrayList<SeatingState> getSuccessors(SeatingState state, Guest guest){
		//create list to hold successor nodes
		ArrayList<SeatingState> successors= new ArrayList<SeatingState>();
		//for every domain of the selected guest
		for(String value:guest.getDomain()) {
			//clone predecessor state
			SeatingState newstate = new SeatingState(state.getUnassignedGuests(),state.getAssignedGuests(),rel,state.table.clone(),state.M);
			//assign the value to the guest (and edit the domain of all friends and enemies in the unassigned guest list)
			//returns false if any of the friends or enemies runs into a empty domain because of that assignment
			Boolean valid = newstate.assignGuest(guest, value);
			//decrement available space in the table where the guest is assigned to
			newstate.table[Integer.valueOf(value)]--;
			//if table had the space for the guest and if there was no empty domain error
			if(newstate.table[Integer.valueOf(value)]>=0 && valid) {
				//add successor to successor list
				successors.add(newstate);
			}
		}
		//return successor list
		return successors;
	}
	/**
	 * function to get the guest object reference of a list based on the name (currently not used)
	 * @param guests collection
	 * @param name of the guest, where the refernece is searched
	 * @return reference to the guest
	 */
	public Guest getGuestbyName(ArrayList<Guest> guests, String name) {
		Guest gst = guests.stream()
				  .filter(inspectedGuest -> "Erik".equals(inspectedGuest.getName()))
				  .findAny()
				  .orElse(null);
		return gst;
	}
	/**
	 * function to get the most desirable guest to continue with
	 * @param guests group
	 * @return most desirable guest
	 */
	public Guest selectUnassignedVariable(ArrayList<Guest> guests) {
		//get lowest domain
		int lowestDomain = rel.getLowestDomainCount(guests);
		//collect all guest with that domain count
		ArrayList<Guest> guestsFilteredDomain= new ArrayList<Guest>();
		for(Guest guest:guests) {
			if(guest.getDomain().size()==lowestDomain) {
				guestsFilteredDomain.add(guest);
			}
		}
		//return the guest with the most constraints of that list
		return rel.getMostConstrainingGuest(guestsFilteredDomain);
	}
	/**
	 * function to check whether a state is in goal state (not used)
	 * @param state which should be checked
	 * @return Boolean, if state is in goal state
	 */
	public Boolean isGoalState(SeatingState state) {
		return (state.unassigned_guests.size()==0);
	}
	/**
	 * function to get the initial state of the problem
	 * @return initial state
	 */
	public SeatingState getInitialState() {
		return initState;
	}
}
