
public class Donation {
	
	
	private int id;
	private String donor;
	private String receiver;
	private int avatar;
	private String logo;
	private int donation;
	
	public Donation(int id, String donor,String receiver, int avatar, String logo, int donation) {
		this.id = id;
		this.donor = donor;
		this.receiver = receiver;
		this.avatar = avatar;
		this.logo = logo;
		this.donation = donation;
	}
	
	public int getID() {
		return id;
	}
	
	public String getDonor() {
		return donor;
	}
	
	public String getReceiver() {
		return receiver;
	}
	
	public int getAvatar() {
		return avatar;
	}
	
	public String getLogo() {
		return logo;
	}
	
	public int getDonation() {
		return donation;
	}
	
}
