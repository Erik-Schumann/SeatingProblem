
/**
 * class to store the name of a couple (friends or enemies)
 */
public class GuestPair {
	String name1;
	String name2;
	/**
	 * Constructor to create couple
	 * @param name1 of first person
	 * @param name2 of second person
	 */
	public GuestPair(String name1, String name2) {
		this.name1= name1;
		this.name2= name2;
	}
	/**
	 * function to get the first name of the couple
	 * @return name of the person
	 */
	public String getName1() {
		return name1;
	}
	/**
	 * function to get the second name of the couple
	 * @return name of the person
	 */
	public String getName2() {
		return name2;
	}
}
