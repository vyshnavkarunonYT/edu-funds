import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import javax.swing.border.MatteBorder;

public class FeedListItem extends JPanel {
	public FeedListItem(Donation donation) {
		setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)));
		
		setPreferredSize(new Dimension(560, 180));
		setBackground(Color.WHITE);
		setLayout(null);
		
		JPanel donorDP = new JPanel(){
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.white);
				g2.fillRect(0, 0, 280, 200);
				int pointer = donation.getAvatar();
				System.out.println("Avatar is "+ pointer);
				int horVal = 975/5;
				int vertVal = 780/4;
				int vertMult = pointer/5;
				int horMult = pointer%5;
				g2.drawImage(new ImageIcon(getClass().getResource("Avatars/Avatars.jpeg")).getImage(),
						10,10,105,105,
						(horVal * (horMult))+20, (vertVal*vertMult), (horVal * (horMult +1)), (vertVal*(vertMult+1)),
						this);
			}
			}
			;
		donorDP.setBackground(Color.WHITE);
		donorDP.setBounds(10, 28, 125, 125);
		add(donorDP);
		 
		JPanel organisationDP = new JPanel(){
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.white);
				g2.fillRect(0, 0, 160, 100);
				g2.drawImage(new ImageIcon(getClass().getResource(donation.getLogo())).getImage(),10,10,105,105,this);
			}
			}
			;
		organisationDP.setBackground(Color.WHITE);
		organisationDP.setBounds(425, 28, 125, 125);
		add(organisationDP);
		
		JTextArea taFeedListItem = new JTextArea(donation.getDonor()+" donated " + donation.getDonation() + " to " + donation.getReceiver());
		taFeedListItem.setWrapStyleWord(true);
		taFeedListItem.setEditable(false);
		taFeedListItem.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		taFeedListItem.setLineWrap(true);
		taFeedListItem.setBounds(145, 65, 275, 83);
		add(taFeedListItem);
	}
}
