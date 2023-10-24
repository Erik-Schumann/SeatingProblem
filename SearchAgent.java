
import java.util.Stack;

/**
 * class to search a solution for the Wedding Setaing Problem
 */
public class SearchAgent {
	SeatingProblem prob;
	/**
	 * Constructor to create the search agent
	 * @param prob the seating problem which should be solved
	 */
	public SearchAgent(SeatingProblem prob) {
		this.prob= prob;
	}
	/**
	 * function to search the CSP seating problem
	 */
	public void search() {
		//get initial State
		SeatingState currState = prob.getInitialState();
		//initialize container
		Stack<SeatingState> container = new Stack<SeatingState>();
		//as long the current state is not a solution
		while(!prob.isGoalState(currState)) {
			//print state
			currState.printState();
			//choose the most desirable guest to continue with
			Guest nextGuest = prob.selectUnassignedVariable(currState.unassigned_guests);
			//print name of most desirable guest
			System.out.println("Choose>>"+nextGuest.getName());
			//get successors (assign the available domain values to guest)
			for(SeatingState state:prob.getSuccessors(currState, nextGuest)) {
				container.add(state);
			}
			//get next state from container
			currState= container.pop();
			//when container is empty stop and clear current state
			if(container.isEmpty()) {
				currState=null;
				break;
			}
		}
		//if current state is available (there is a solution) print state
		if(currState!=null) {
			currState.printState();
		}
		//if there is no solution, print error message
		if(currState==null) {
			System.out.println("THERE WAS NO SOLUTION TO YOUR PROBLEM: CHECK FOR CONTRADICTIONS IN ENEMIES/FRIENDS OR TOO MUCH GUESTS");
		}
	}
}
