
import java.util.ArrayList;

/**
 * Guest is the implementation of a wedding guest
 */
public class Guest {
	String name;
	ArrayList<String> domain;
	String value;
	
	/**
	 * Constructor to create guest
	 * @param name of the guest
	 */
	public Guest(String name) {
		this.name = name;
		domain = new ArrayList<String>();
	}
	/**
	 * function to add a domain value for the guest
	 * @param value which should be added
	 */
	public void addDomain(String value) {
		domain.add(value);
	}
	/**
	 * function to assign value to the guest
	 * @param value which should be assigned
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * function to print out the guest information (function was necessary within the development phase-> could be important to feature updates)
	 */
	public void printGuest() {
		System.out.println("-------------------");
		System.out.println("Name: "+ name);
		System.out.println("Value: "+ value);
		System.out.print("Domain: ");
		for(String value: domain) {
			System.out.print(value+" ");
		}
		System.out.println("");
		System.out.println("-------------------");
	}
	/**
	 * function to print out the name of the guest
	 */
	public void printName() {
		System.out.println(name);
	}
	/**
	 * function to get the name of the guest
	 * @return name of the guest
	 */
	public String getName() {
		return name;
	}
	/**
	 * function to get the assigned value of the guest
	 * @return the assigned value of the guest
	 */
	public String getValue() {
		return value;
	}
	/**
	 * function to create a copy of the guest (name and domain)
	 * @return copied guest object
	 */
	public Guest copyGuest() {
		Guest g2=  new Guest(this.name);
		for(String value: domain) {
			g2.addDomain(value+"");
		}
		return g2;
	}
	/**
	 * function to get the domain of a guest
	 * @return domain of the guest
	 */
	public ArrayList<String> getDomain() {
		return domain;
	}
	/**
	 * function to change the domain of a guest based on a change of the domain of a friend
	 * @param value of friend
	 */
	public void setFriend(String value) {
		if(!domain.contains(value)) {
			domain.clear();
		}
		else {
			domain.clear();
			domain.add(value);
		}
	}
	/**
	 * function to change the domain of a guest based on a change of the domain of a enemy
	 * @param value of enemy
	 */
	public void setEnemy(String value) {
		domain.remove(value);
	}
}
