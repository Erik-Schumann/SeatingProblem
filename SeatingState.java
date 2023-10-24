
import java.util.ArrayList;
import java.util.List;

/**
 * class to encode the state of a seating problem
 */
public class SeatingState {
		//initialize values
		ArrayList<Guest> assigned_guests;
		ArrayList<Guest> unassigned_guests;
		int[] table;//Number Tables
		int M= 5;//Table capacity
		GuestRelationship rel;
		/**
		 * create Seating state (initial state) based on guestList
		 * @param guests which are invited to the wedding
		 * @param N Number of Tables
		 * @param M Size of the Tables
		 * @param rel relationship matrix of the guests
		 */
		public SeatingState (ArrayList<Guest> guests, int N, int M, GuestRelationship rel) {
			//initialize values
			assigned_guests = new ArrayList<Guest>();
			unassigned_guests = new ArrayList<Guest>();
			//add every guest to unassigned list (since this is the initial state)
			for(Guest guest:guests) {
				unassigned_guests.add(guest);
			}
			//create table array 
			table = new int[N+1];
			//initialize table array (showing how many seats are available per guest
			for(int i=1; i<table.length; i++) {
				table[i]= M;
			}
			//store table size
			this.M=M;
			//store relationship matrix
			this.rel = rel;
		}
		/**
		 * create Seating state based on existing seating state
		 * @param unassigned_guests list of unassigned guests
		 * @param assigned_guests list of assigned guests
		 * @param rel relationship matrix of the guests
		 * @param table capacity of the tables stored in an array
		 * @param M Size of the Tables
		 */
		public SeatingState (List<Guest> unassigned_guests,List<Guest> assigned_guests, GuestRelationship rel, int[] table, int M) {
			//initialize values
			this.assigned_guests = new ArrayList<Guest>();
			this.unassigned_guests = new ArrayList<Guest>();
			//add every unassigned guest to unassigned guestList
			for(Guest guest:unassigned_guests) {
				this.unassigned_guests.add(guest);
			}
			//add every assigned guest to assigned guestList
			for(Guest guest:assigned_guests) {
				this.assigned_guests.add(guest);
			}
			//store relationship matrix
			this.rel = rel;
			//store table capacities
			this.table = table;
			//store table size
			this.M = M;
		}
		/**
		 * function to print the seating state
		 */
		public void printState() {
			System.out.println("-------------------------");
			System.out.println("State:");
			//print assigned guest list and their assigned value
			System.out.println("assigned:");
			for(Guest guest:assigned_guests) {
				System.out.println("-"+guest.getName()+"("+guest.getValue()+")");
			}
			//if empty print empty placeholder
			if(assigned_guests.isEmpty()) {
				System.out.println("(empty)");
			}
			//print unassigned guest list and their remaining domain
			System.out.println("unassigned:");
			for(Guest guest:unassigned_guests) {
				System.out.print("-"+guest.getName()+" (");
				for(String value: guest.getDomain()) {
					System.out.print(value+" ");
				}
				System.out.println(")");
			}
			//if empty print empty placeholder
			if(unassigned_guests.isEmpty()) {
				System.out.println("(empty)");
			}
			//print capacity of the tables
			System.out.println("Tables:");
			for(int i=1; i<table.length; i++) {
				System.out.println("Table "+i+": "+table[i]);
			}
		}
		/**
		 * function to check whether the current state is a goal state (aka check if all guests are assigned)
		 * @return Boolean if the state is a goal state
		 */
		public Boolean isGoalState() {
			return unassigned_guests.isEmpty();
		}
		/**
		 * function to get a copy of the unassigned guests
		 * @return copy of unassigned guests
		 */
		public List<Guest> getUnassignedGuests() {
			//create list to store copies
			List<Guest> list = new ArrayList<Guest>();
			//for every unassigned guest, make a copy of that guest and store it in the list
			for(Guest unassigned: unassigned_guests) {
				list.add(unassigned.copyGuest());
			}
			//return the list
			return list;
		}
		/**
		 * function to get a copy of the assigned guests
		 * @return copy of assigned guests
		 */
		public List<Guest> getAssignedGuests() {
			//create list to store copies
			List<Guest> list = new ArrayList<Guest>();
			//for every unassigned guest, make a copy of that guest (also copy the assigned value) and store it in the list
			for(Guest assigned: assigned_guests) {
				Guest gst = assigned.copyGuest();
				gst.setValue(assigned.getValue());
				list.add(gst);
			}
			//return the list
			return list;
		}
		/**
		 * function to assign a value to a guest and edit the domain of the other unassigned guests domain accordingly
		 * @param guest who gets assigned a value
		 * @param value assigned value
		 * @return Boolean, if the was any empty domain error
		 */
		public Boolean assignGuest(Guest guest, String value) {
			//get the reference of the passed guest in the unassigned guest list
			Guest gst = unassigned_guests.stream()
					  .filter(inspectedGuest -> guest.getName().equals(inspectedGuest.getName()))
					  .findAny()
					  .orElse(null);
			//remove guest from unassigned gust list
			unassigned_guests.remove(gst);
			//assign value to guest
			gst.setValue(value);
			//add guest to assigned guests list
			assigned_guests.add(gst);
			//iterate through every remaining guest
			for(Guest unassigned: unassigned_guests) {
				//get the relationship of the guest and the remaining unassigned guest
				int relation= rel.canSitTogether(guest.getName(), unassigned.getName());
				//if they are friends, set domain accordingly and catch empty domain error
				if(relation == 1) {
					unassigned.setFriend(value);
					if(unassigned.domain.size()==0) {
						return false;
					}
				}
				//if they are enemies, set domain accordingly and catch empty domain error
				if(relation == -1) {
					unassigned.setEnemy(value);
					if(unassigned.domain.size()==0) {
						return false;
					}
				}
			}
			//return true, if there was no empty domain error
			return true;
		}
}
