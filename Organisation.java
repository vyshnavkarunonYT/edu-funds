
public class Organisation {
	
	private String name;
	private String bio;
	private String reach;
	private String logo;
	private String type;
	private int donations;
	
	
	public Organisation(String name, String bio, String reach, String logo, String type, int donations) {
		this.name = name;
		this.bio = bio;
		this.reach = reach;
		this.logo = logo;
		this.type  = type;
		this.donations = donations;
	}
	
	
	public String getName() {
		return name;
	}
	
	public String getBio() {
		return bio;
	}
	
	public String getReach() {
		return reach;
	}
	
	public String getType() {
		return type;
	}
	
	public String getLogo() {
		return logo;
	}
	
	public int getDonations() {
		return donations;
	}
}
