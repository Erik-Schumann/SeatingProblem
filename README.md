<pre>Assignment Report 2: CSP Application
CSCI 485: Advanced Topics in General Computer Science: Foundations of AI
Erik Schumann
667072862
____________________________________________________________________________________________________________________________________________________________________________
1.How to compile and execute your program? If your source code files need to be placed into subfolders in order to use the makefile, explain how the files should be organized.

In the beginning the following files should be in the following structure:

(README = Assignment Report)
(Doc=Class documentation)
(src=Java fies)

./src
./src/SeatingCSP.java
./src/SeatingState.java
./src/SeatingState.class
./src/Guest.java
./src/SeatingProblem.class
./src/SeatingProblem.java
./src/Input.properties
./src/GuestRelationship.class
./src/GuestPair.java
./src/SearchAgent.java
./src/SearchAgent.class
./src/SeatingCSP.class
./src/GuestRelationship.java
./src/GuestPair.class
./src/Guest.class


Get into the folder src using cmd:

./SeatingCSP.java
./SeatingState.java
./Guest.java
./SeatingProblem.java
./Input.properties
./GuestPair.java
./SearchAgent.java
./GuestRelationship.java 

run following command to compile the program:

	>javac *.java

Now the following files should be there (.class files are the compiled ones):

./SeatingCSP.java
./SeatingState.java
./SeatingState.class
./Guest.java
./SeatingProblem.class
./SeatingProblem.java
./Input.properties
./GuestRelationship.class
./GuestPair.java
./SearchAgent.java
./SearchAgent.class
./SeatingCSP.class
./GuestRelationship.java
./GuestPair.class
./Guest.class

Run following command to run the program:

	>java SeatingCSP
	
To clean up the program, run the following command

	>rm *.class

____________________________________________________________________________________________________________________________________________________________________________	
2. How do you encode the party seating problem as a Constraint Satisfaction Problem? That is, what is the formal problem definition of this particular party seating problem that conforms to the Constraint Satisfaction Problem representation standard.

Variables are the guests
Domains (values of the variables) are the Tables 1 to N
Constraints are:
1. N Tables have a capacity M (encoded in the int array in a state of size N with a counter M, which decrements everytime someone is assigned to that table)
2. There are G guests and it goes without saying that G is less than or equal to N times M (this is required by the user to check before setting input (as said: it goes without saying)
3. Friends should sit together (represented by a 1 in the relationship matrix of the class GuestRelationship.java) and checked by setFriends in Guest (set Domain accordingly to the assigned value of the friend)
4. Enemies should not sit together (represented by a -1 in the relationship matrix of the class GuestRelationship.java) and checked by setEnemies in Guest (set Domain accordingly to the assigned value of the enemy)

____________________________________________________________________________________________________________________________________________________________________________	
3. How should a user enter the information/data of a party seating problem instance to your program? That is, what is the encoding format of a party seating problem instance's data and constraints acceptable by your program?

The user should input the data in the Input.properties-file

NumTables = 4 
TableSize = 4
Guests = Andy;Bob;Cathy;David;Ethan;Frank;Gareth;Hugh;Irene;Jack;Ken;Liam;Mary;Nathan;Owen
Friends = Cathy&Ethan;David&Frank;Irene&Nathan
#If you add someone as friends and as enemies, they will be counted as enemies
Enemies = Andy&Frank;Hugh&Owen;Gareth&Ken;Hugh&Mary;Gareth&Frank

The guests should be separated by ; (no space)
The enemies/friends should be name1&name2
The pairs should be separated by ; (no space)


____________________________________________________________________________________________________________________________________________________________________________
4. What output can be expected from your program if your AI agent finds a solution to the given problem or fails to find a solution?

When the AI agent finds a solution, you can expect it to print the checked nodes from beginning to end state . The last state shows all guests and their assigned table (the brackets afterwards). The unassigned list should be empty and there also will be shown how many seats are left per table:

…
-------------------------
State:
assigned:
-Frank(4)
-David(4)
-Gareth(3)
-Andy(3)
-Ken(4)
-Hugh(4)
-Mary(3)
-Owen(3)
-Cathy(2)
-Ethan(2)
-Irene(2)
-Nathan(2)
-Bob(1)
-Jack(1)
-Liam(1)
unassigned:
(empty)
Tables:
Table 1: 1
Table 2: 0
Table 3: 0
Table 4: 0


If the AI agent doesn't find a solution, there are 2 cases:

There will be the checked nodes printed with an error message an the end: 

THERE WAS NO SOLUTION TO YOUR PROBLEM: CHECK FOR CONTRADICTIONS IN ENEMIES/FRIENDS OR TOO MUCH GUESTS

____________________________________________________________________________________________________________________________________________________________________________
5.How does your program encode and maintain the problem states so that the required heuristics can be applied?

A state is represented by an instance of SeatingState.java
This stores the following information:

ArrayList<Guest> assigned_guests; -> List of assigned guests
ArrayList<Guest> unassigned_guests; -> List of unassigned guests
int[] table; ->Capacity counter for each table
int M= 5; -> Table capacity
GuestRelationship rel; -> Relationship matrix of the guests

Guests are instances of the Guest.java class

____________________________________________________________________________________________________________________________________________________________________________
6.Where and how, in your program, are the forward checking and the two required heuristics implemented respectively?

>>>Forward-checking:

The getSuccessors function calls the assignGuest method of the SeatingState-class:

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
Depending on the relationship the domain of the unassigned guests is edited. If this causes their domain to be empty, false is returned
The method getSuccessors stores this boolean in the variable valid:

Boolean valid = newstate.assignGuest(guest, value);
//decrement available space in the table where the guest is assigned to
newstate.table[Integer.valueOf(value)]--;
//if table had the space for the guest and if there was no empty domain error
if(newstate.table[Integer.valueOf(value)]>=0 && valid) {
	//add successor to successor list
	successors.add(newstate);
}
If there is an empty domain error (valid is false) or if the tables does not have the capacity for an addiitional person, the successor node is just not added to the successor list and by that would not be put in the Stack by the search agent

 >>>two required heuristics

This is implemented by the selectUnassignedVariable method:

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
At first the lowest domain count of all unassigned guests is determinate and then every guest with this domain count is stored in a list (heuristic minimum remaining value)
At the end the most constraining guest of this list is terminated (heuristic most constraining variable)

The methods are implemented as follows:

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

And 

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
The getMostConstrainingGuest() method calls getCntContraints() which counts the non-zero elements of the row of the specific person in the relationship matrix

____________________________________________________________________________________________________________________________________________________________________________
7.Any known bugs

None


____________________________________________________________________________________________________________________________________________________________________________
8.Any comment you would like the marker to know.

None
</pre>
