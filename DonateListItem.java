import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.MatteBorder;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JTextArea;

public class DonateListItem extends JPanel {
	
	static Color eduFundsGreen = new Color(120,160,64);
	
	public DonateListItem(Organisation org) {
		setBackground(new Color(254,254,254));
		setBorder(new MatteBorder(0, 0, 1, 0, (Color) new Color(0, 0, 0)));
		setLayout(null);
		setPreferredSize(new Dimension(530, 260));
		
		JPanel panel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.white);
				g2.fillRect(0, 0, 160, 100);
				g2.drawImage(new ImageIcon(getClass().getResource(org.getLogo())).getImage(),10,10,130,130,this);
			}
			};
		panel.setBackground(Color.WHITE);
		panel.setBounds(10, 11, 150, 150);
		add(panel);
		
		JLabel lblOrgName = new JLabel(org.getName());
		lblOrgName.setFont(new Font("Trebuchet MS", Font.BOLD | Font.ITALIC, 20));
		lblOrgName.setHorizontalAlignment(SwingConstants.LEFT);
		lblOrgName.setBounds(198, 11, 292, 35);
		add(lblOrgName);
		
		JButton btnDonate = new JButton("Donate");
		btnDonate.setBackground(eduFundsGreen);
		btnDonate.setForeground(Color.white);
		btnDonate.setFocusable(false);
		btnDonate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EduFunds.switchToDonateScreen(org);
			}
		});
		btnDonate.setBounds(10, 172, 150, 50);
		add(btnDonate);
		
		JLabel lblType = new JLabel("Type");
		lblType.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblType.setBounds(198, 75, 93, 30);
		add(lblType);
		
		JLabel lblReach = new JLabel("Reach");
		lblReach.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblReach.setBounds(198, 116, 93, 30);
		add(lblReach);
		
		JLabel lblTypeVal = new JLabel(org.getType());
		lblTypeVal.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblTypeVal.setBounds(301, 75, 199, 30);
		add(lblTypeVal);
		
		JLabel lblReachVal = new JLabel(org.getReach());
		lblReachVal.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblReachVal.setBounds(299, 116, 191, 30);
		add(lblReachVal);
		
		JTextArea taBioVal = new JTextArea(org.getBio());
		taBioVal.setEditable(false);
		taBioVal.setWrapStyleWord(true);
		taBioVal.setLineWrap(true);
		taBioVal.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		taBioVal.setBounds(198, 166, 264, 56);
		add(taBioVal);
	}
}
