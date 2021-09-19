import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.border.LineBorder;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.border.MatteBorder;

public class DonationHistoryListItem extends JPanel {
	static Color eduFundsGreen = new Color(120,160,64);
	
	
	public DonationHistoryListItem(String donatedTo, int donated, String logo) {
		setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)));
		setPreferredSize(new Dimension(640, 100));

	
		setBackground(Color.WHITE);
		setLayout(null);
		
		JPanel panel = new JPanel() {
				@Override
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2 = (Graphics2D) g;
					g2.setColor(Color.white);
					g2.fillRect(0, 0, 160, 100);
					g2.drawImage(new ImageIcon(getClass().getResource(logo)).getImage(),20,10,120,80,this);
				}
				}
				;
		panel.setBackground(Color.WHITE);
		panel.setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)));
		panel.setBounds(0, 0, 160, 100);
		add(panel);
		
		JLabel lblDonateTo = new JLabel("Donated to");
		lblDonateTo.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
		lblDonateTo.setBounds(223, 11, 160, 40);
		add(lblDonateTo);
		
		JLabel labelDonatedToVal = new JLabel(donatedTo);
		labelDonatedToVal.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		labelDonatedToVal.setBounds(393, 11, 160, 40);
		add(labelDonatedToVal);
		
		JLabel lblDonated = new JLabel("Donated");
		lblDonated.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
		lblDonated.setBounds(223, 49, 160, 40);
		add(lblDonated);
		
		JLabel lblDonatedVal = new JLabel(donated+"");
		lblDonatedVal.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		lblDonatedVal.setBounds(393, 49, 160, 40);
		add(lblDonatedVal);
	}
}
