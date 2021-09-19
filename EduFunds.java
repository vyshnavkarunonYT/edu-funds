import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import java.awt.CardLayout;
import javax.swing.JSplitPane;
import java.awt.FlowLayout;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;



import java.awt.SystemColor;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.border.BevelBorder;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class EduFunds {
	
	Color eduFundsGreenLightest = new Color(150,200,80);
	Color eduFundsGreenLighter = new Color(135,180,72);
	static Color eduFundsGreen = new Color(120,160,64);
	Color eduFundsGreenDarker = new Color(105,140,56);
	Color eduFundsGreenDarkest = new Color(90,120,49);
	private JFrame frame;
	private JTextField tfLoginUsername;
	private JPasswordField pfLoginPassword;
	private static Connection con;
	
	//Components 
	private static JLabel lblLoginErrorMessage;
	private static JLabel lblCreateAccountErrorMessage;
	
	//Data retreived from the MySql Database
	private static ResultSet userData;
	private JTextField tfCreateAccountUsername;
	private JTextField tfCreateAccountFirstName;
	private JTextField tfCreateAccountLastName;
	private JPasswordField pfCreateAccountPassword;
	private JPasswordField pfCreateAccountConfirmPassword;

	
	
	//CURRENT USER
	private static User user;
	
	
	//Account Creation Variables
	private static int selectedAvatar = 12;
	
	
	//Profile form details
	static JLabel lblLandingProfileDonatedVal;
	static	JLabel	lblLandingProfileUsername;
	static JLabel lblLandingProfileFirstname;
	static JLabel lblLandingProfileSecondname;
	static JPanel panelLandingProfileDP;
	
	//PanelLandingCard
	static JPanel panelLandingCard;
	protected Container paneLandingCard;
	
	//Organization data
	static ResultSet orgData;
	static ArrayList<Organisation> organisationData;
	
	
	 //LandingAnalyticsVariables
	static User highestDonor;
	static Organisation highestOrganisation;
	
	
	//Donation Data
	static ResultSet donData;
	static ArrayList<Donation> donationData;
	
	//Landing Page Buttons
	static JButton btnFooterNews, btnFooterAnalytics, btnFooterProfile, btnFooterDonate;
	
	//DonationHistory Panel
	static JScrollPane spLandingProfile;
	static JPanel panelDHView;
	
	//LandingDonate
	static JPanel panelLandingDonateList;
	static JPanel panelLandingDonateToOrg;
	
	//Landing News
	static JPanel panelLandingFeedList;
	private static JTextField tfDonate;
	
	//Donate Organisation
	static Organisation donateOrganisation;
	
	static JLabel lblDonateOrgName;
	static JLabel lblDonationResult;
	
	/**
	 * Launch the application.
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/edufunds?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true";
		String password = ""; //ENTER YOUR MYSQL PASSWORD HERE
		String username = "root";	//ENTER YOUR MYSQL USERNAME HERE
		

		con = DriverManager.getConnection(url,username,password); //String url, username, password
		
		//Get the organisation database from mysql
		getOrganisationData();
		
		//Gets the user database from mysql
		getUserData();
		
		//Getting donation data
		getDonationData();
		
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EduFunds window = new EduFunds();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private static void getUserData() throws SQLException {
		String query = "select * from user";
		Statement st = con.createStatement();
        userData = st.executeQuery(query);
        
        //Updating highest donor information
        int max =0;
        userData.beforeFirst();
        while(userData.next()) {
        	if(userData.getInt(5)>max) {
        		max = userData.getInt(5);
        		highestDonor = new User(userData.getString(1), userData.getString(2),
        					userData.getString(3), userData.getString(4),
        					userData.getInt(5), userData.getInt(6)
        				);
        	}
        }
        
        userData.beforeFirst();
	}
	
	private static void getOrganisationData() throws SQLException {
		String query = "select * from organisation";
		Statement st = con.createStatement();
        orgData = st.executeQuery(query);
        
        organisationData = new ArrayList<Organisation>();
        while(orgData.next()) {
        	organisationData.add(new Organisation(
        			orgData.getString(1),
        			orgData.getString(2),
        			orgData.getString(3),
        			orgData.getString(4),
        			orgData.getString(5),
        			orgData.getInt(6)
        			));
        }
        
        
       //Setting max to first organisation
        int max = organisationData.get(0).getDonations();
        int indexOfMax = 0;
        for(int i =1; i<organisationData.size(); i++) {
        	if(organisationData.get(i).getDonations()>max) {
        		max = organisationData.get(i).getDonations();
        		indexOfMax = i;
        	}
        }
        
        highestOrganisation = organisationData.get(indexOfMax);
        
        System.out.println("Name of highest receiving organisation is " + highestOrganisation.getName() );
        
        
//        for(int i =0; i <organisationData.size(); i++) {
//        	System.out.println(organisationData.get(i).getName());
//        }
        
	}
	
	
	private static void getDonationData() throws SQLException {
		String query = "select * from donation";
		Statement st = con.createStatement();
        donData = st.executeQuery(query);
        
        //Printing out the donation data
//        while(donData.next()) {
//        	System.out.println(donData.getString(2)+"		"+donData.getString(3));	
//        }
        
        //Filling the donation data list
        donationData = new ArrayList<Donation>();
        while(donData.next()) {
        	donationData.add(new Donation(donData.getInt(1), donData.getString(2), donData.getString(3),
        			donData.getInt(4), donData.getString(5), donData.getInt(6)));
        }
	}
	
	public static boolean createAccount(
			String username,
			String firstName,
			String lastName,
			String password,
			String confirmPassword
			) throws SQLException {
		if(!password.equals(confirmPassword)) {
			lblCreateAccountErrorMessage.setText("The passwords do not match");
			System.out.println("Account cannot be created");
			return false;
		}
		
		userData.beforeFirst();
		
		while(userData.next()) {
			if(userData.getString(1).equals(username)) {
				lblCreateAccountErrorMessage.setText("The username already exists please pick another one");
				System.out.println("Account cannot be created");
				return false;
			}
		}
		
		if(firstName.isEmpty()) {
			lblCreateAccountErrorMessage.setText("First name field is empty");
			System.out.println("Account cannot be created");
			return false;
		}
		
		if(lastName.isEmpty()) {
			lblCreateAccountErrorMessage.setText("Last name field is empty");
			System.out.println("Account cannot be created");
			return false;
		}
		
		if(password.isEmpty()) {
			lblCreateAccountErrorMessage.setText("Password field is empty");
			System.out.println("Account cannot be created");
			return false;
		}
		
		String query = "insert into user(username,first_name,second_name,password, donations, avatar) values(?,?,?,?,?,?)";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, username);
		ps.setString(2, firstName);
		ps.setString(3, lastName);
		ps.setString(4, password);
		ps.setInt(5, 0);
		ps.setInt(6, selectedAvatar);
		ps.executeUpdate();
		ps.close();
		
		user = new User(username,firstName,lastName, password, 0, selectedAvatar);
		initProfileFormDetails();
		
		return true;
	}
	
	private static boolean checkLoginCredentials(String username, String password) throws SQLException {
		System.out.println(password);
		boolean allowLogin = false;
		while(userData.next()) {
			if(userData.getString(1).equals(username) && userData.getString(4).equals(password)) {
				allowLogin = true;
				user = new User(userData.getString(1),
						userData.getString(2),
						userData.getString(3),
						userData.getString(4),
						userData.getInt(5),
						userData.getInt(6)
						);
				break;
			}
		}
		userData.beforeFirst();
		//IF LOGIN IS DENIED
		if(!allowLogin) {
			Toolkit.getDefaultToolkit().beep();
			lblLoginErrorMessage.setText("Wrong username or password");
			lblLoginErrorMessage.setForeground(Color.red);
		}
		
		
		//Fill profile form details
		initProfileFormDetails();
		//Get donation history of the Current user and fill donation history scroll pane
		fillDonationHistory();
        //Populating the landingDonate page
        fillLandingDonate();
        //Populating the ladingFeed page
        fillFeed();
		
		
		return allowLogin;
	}

	/**
	 * Create the application.
	 */
	public EduFunds() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100,0,1000,700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		Image eduFundsLogo = new ImageIcon(getClass().getResource("Logos/eduFundsIcon.jpg")).getImage();
		frame.setTitle("EduFunds");
		frame.setIconImage(eduFundsLogo);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));
		frame.setResizable(false);
		
		
		JPanel panelBody = new JPanel();
		panelBody.setBackground(Color.RED);
		frame.getContentPane().add(panelBody);
		panelBody.setLayout(new CardLayout(0, 0));
		panelBody.addMouseListener(new MouseListener()
					{

						@Override
						public void mouseClicked(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mouseEntered(MouseEvent arg0) {
							// TODO Auto-generated method stub
							 
						}

						@Override
						public void mouseExited(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mousePressed(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mouseReleased(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}
						
					}
				);
		
	
		JPanel panelLogin = new JPanel() {
			
			@Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        Graphics2D g2 = (Graphics2D) g;
		        //g2.drawImage(new ImageIcon(getClass().getResource("Wallpapers/library.jpeg")).getImage(), 
		        //0, 0, frame.getWidth(), frame.getHeight(), null);
		        g2.setPaint(new Color(250,250,250));
		        g2.fillRect(0, 0, frame.getWidth()/2, frame.getHeight());
		        g2.setPaint(new Color(250,250,250));
		        g2.fillRect(frame.getWidth()/2, 0, frame.getWidth(), frame.getHeight());
		        
		        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		        int w = getWidth();
		        int h = getHeight();
		        Color color1 = eduFundsGreenLighter;
		        Color color2 = eduFundsGreenDarker;
		        GradientPaint gp = new GradientPaint(0, 0, color1, w, 0, color2);
		        g2.setPaint(gp);
		        g2.fillRect(0, 0, w/2, h);
		        GradientPaint gp2 = new GradientPaint(0, 0, color2, w, 0, color1);
		        g2.setPaint(gp2);
		        g2.fillRect(w/2, 0, w, h);
		        
		        g2.setPaint(eduFundsGreenDarker);
		        g2.fillRect(320, 142, 370, 396);
		        
		        
		        Color glassyWhite = new Color(255,255,255,50);
		        g2.setPaint(glassyWhite);
		        g2.fillRect(0,0,frame.getWidth(),frame.getHeight());
		    }
		};
		panelBody.add(panelLogin, "panel_Login");
		panelLogin.setLayout(null);

		
		JPanel panelLoginForm = new JPanel();
		panelLoginForm.setBounds(311, 131, 372, 400);
		panelLoginForm.setBorder(null);
		panelLoginForm.setBackground(Color.WHITE);
		panelLogin.add(panelLoginForm);
		GridBagLayout gbl_panelLoginForm = new GridBagLayout();
		gbl_panelLoginForm.columnWidths = new int[]{370, 0};
		gbl_panelLoginForm.rowHeights = new int[]{62, 14, 44, 14, 44, 14, 44, 42, 14, 0};
		gbl_panelLoginForm.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelLoginForm.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
		panelLoginForm.setLayout(gbl_panelLoginForm);
		
		JButton btnLogin = new JButton("Log in\r\n");
		btnLogin.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
		btnLogin.setForeground(Color.white);
		btnLogin.setEnabled(false);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(checkLoginCredentials(tfLoginUsername.getText(),new String(pfLoginPassword.getPassword())))
					{
					 CardLayout cl = (CardLayout)(panelBody.getLayout());
					 cl.next(panelBody);
					 frame.repaint();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		JLabel lblLoginFormHeader = new JLabel("EduFunds ");
		lblLoginFormHeader.setForeground(eduFundsGreen);
		lblLoginFormHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblLoginFormHeader.setFont(new Font("Trebuchet MS", Font.BOLD, 30));
		GridBagConstraints gbc_lblLoginFormHeader = new GridBagConstraints();
		gbc_lblLoginFormHeader.fill = GridBagConstraints.BOTH;
		gbc_lblLoginFormHeader.insets = new Insets(0, 20, 0, 20);
		gbc_lblLoginFormHeader.gridx = 0;
		gbc_lblLoginFormHeader.gridy = 0;
		panelLoginForm.add(lblLoginFormHeader, gbc_lblLoginFormHeader);
		
		JLabel lblUsername = new JLabel("Username\r\n");
		lblUsername.setForeground(Color.GRAY);
		lblUsername.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.fill = GridBagConstraints.BOTH;
		gbc_lblUsername.insets = new Insets(0, 20, 0, 20);
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 1;
		panelLoginForm.add(lblUsername, gbc_lblUsername);
		
		tfLoginUsername = new JTextField();
		tfLoginUsername.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		GridBagConstraints gbc_tfLoginUsername = new GridBagConstraints();
		gbc_tfLoginUsername.fill = GridBagConstraints.BOTH;
		gbc_tfLoginUsername.insets = new Insets(10, 20, 10, 20);
		gbc_tfLoginUsername.gridx = 0;
		gbc_tfLoginUsername.gridy = 2;
		panelLoginForm.add(tfLoginUsername, gbc_tfLoginUsername);
		tfLoginUsername.setColumns(10);
		
		 //CardLayout cl = (CardLayout)(panelBody.getLayout());
		 //cl.show(panelBody,"name_147168431510941");
		
		
		// Listen for changes in the text
		tfLoginUsername.getDocument().addDocumentListener(new DocumentListener() {
		  public void changedUpdate(DocumentEvent e) {
		    check();
		  }
		  public void removeUpdate(DocumentEvent e) {
		    check();
		  }
		  public void insertUpdate(DocumentEvent e) {
		    check();
		  }

		  public void check() {
		     if (tfLoginUsername.getText().length()==0 || pfLoginPassword.getPassword().length==0){
		    	btnLogin.setBackground(Color.gray);
		    	btnLogin.setEnabled(false);
		     }
		     else {
		    	 btnLogin.setBackground(eduFundsGreen);
		    	 btnLogin.setEnabled(true);
		     }
		  }
		});
		
		JLabel lblLoginPassword = new JLabel("Password");
		lblLoginPassword.setForeground(Color.GRAY);
		lblLoginPassword.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
		GridBagConstraints gbc_lblLoginPassword = new GridBagConstraints();
		gbc_lblLoginPassword.fill = GridBagConstraints.BOTH;
		gbc_lblLoginPassword.insets = new Insets(0, 20, 0, 20);
		gbc_lblLoginPassword.gridx = 0;
		gbc_lblLoginPassword.gridy = 3;
		panelLoginForm.add(lblLoginPassword, gbc_lblLoginPassword);
		
		pfLoginPassword = new JPasswordField();
		pfLoginPassword.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
		GridBagConstraints gbc_pfLoginPassword = new GridBagConstraints();
		gbc_pfLoginPassword.fill = GridBagConstraints.BOTH;
		gbc_pfLoginPassword.insets = new Insets(10, 20, 10, 20);
		gbc_pfLoginPassword.gridx = 0;
		gbc_pfLoginPassword.gridy = 4;
		panelLoginForm.add(pfLoginPassword, gbc_pfLoginPassword);
		
		pfLoginPassword.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
			    check();
			  }
			  public void removeUpdate(DocumentEvent e) {
			    check();
			  }
			  public void insertUpdate(DocumentEvent e) {
			    check();
			  }

			  public void check() {
			     if (tfLoginUsername.getText().length()==0 || pfLoginPassword.getPassword().length==0){
			    	btnLogin.setBackground(Color.gray);
			    	btnLogin.setEnabled(false);
			     }
			     else {
			    	 btnLogin.setBackground(eduFundsGreen);
			    	 btnLogin.setEnabled(true);
			     }
			  }
			});
		
		lblLoginErrorMessage = new JLabel("");
		lblLoginErrorMessage.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblLoginErrorMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblLoginErrorMessage.setForeground(UIManager.getColor("ToolBar.dockingForeground"));
		GridBagConstraints gbc_lblLoginErrorMessage = new GridBagConstraints();
		gbc_lblLoginErrorMessage.fill = GridBagConstraints.BOTH;
		gbc_lblLoginErrorMessage.insets = new Insets(0, 20, 0, 20);
		gbc_lblLoginErrorMessage.gridx = 0;
		gbc_lblLoginErrorMessage.gridy = 5;
		panelLoginForm.add(lblLoginErrorMessage, gbc_lblLoginErrorMessage);
		
		btnLogin.setFocusPainted(false);
		btnLogin.setContentAreaFilled(true);
		btnLogin.setBackground(Color.gray);
		btnLogin.setFocusable(true);
		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.fill = GridBagConstraints.BOTH;
		gbc_btnLogin.insets = new Insets(20, 20, 0, 20);
		gbc_btnLogin.gridx = 0;
		gbc_btnLogin.gridy = 6;
		panelLoginForm.add(btnLogin, gbc_btnLogin);
		
		JLabel lblCreateAccount = new JLabel("Create an account\r\n");
		lblCreateAccount.setForeground(eduFundsGreen);
		lblCreateAccount.setHorizontalAlignment(SwingConstants.CENTER);
		lblCreateAccount.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
		lblCreateAccount.addMouseListener(new MouseListener()
				{
					@Override
					public void mouseClicked(MouseEvent arg0) {
						// TODO Auto-generated method stub
						 CardLayout cl = (CardLayout)(panelBody.getLayout());
						 cl.show(panelBody, "panel_CreateAccount");
						 frame.repaint();
					}

					@Override
					public void mouseEntered(MouseEvent arg0) {
						// TODO Auto-generated method stub
						lblCreateAccount.setForeground(eduFundsGreenDarker);
					}

					@Override
					public void mouseExited(MouseEvent arg0) {
						// TODO Auto-generated method stub
						lblCreateAccount.setForeground(eduFundsGreen);
					}

					@Override
					public void mousePressed(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseReleased(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}
				}
				);
		GridBagConstraints gbc_lblCreateAccount = new GridBagConstraints();
		gbc_lblCreateAccount.fill = GridBagConstraints.VERTICAL;
		gbc_lblCreateAccount.gridx = 0;
		gbc_lblCreateAccount.gridy = 8;
		panelLoginForm.add(lblCreateAccount, gbc_lblCreateAccount);
		
		JPanel panelLanding = new JPanel();
		panelLanding.setBackground(UIManager.getColor("ToggleButton.highlight"));
		panelBody.add(panelLanding, "panel_Dashboard");
		panelLanding.setLayout(null);
		
		JPanel panelFooter = new JPanel();
		panelFooter.setBounds(0, 604, 994, 67);
		panelLanding.add(panelFooter);
		panelFooter.setLayout(null);
		
		btnFooterNews = new JButton("News");
		ImageIcon footer_icon = new ImageIcon(EduFunds.class.getResource("/Icons/news.png"));
		Image img = footer_icon.getImage() ;  
		Image newimg = img.getScaledInstance( 45, 45,  java.awt.Image.SCALE_SMOOTH ) ;  
		footer_icon = new ImageIcon( newimg );
		btnFooterNews.setIcon(footer_icon);
		btnFooterNews.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cl2 = (CardLayout)(panelLandingCard.getLayout());
				cl2.show(panelLandingCard, "panel_landingNews");
				setActive("News");
				frame.repaint();
			}
		});
		btnFooterNews.setForeground(Color.black);
		btnFooterNews.setFocusable(false);
		btnFooterNews.setBackground(Color.WHITE);
		btnFooterNews.setBounds(0, 0, 260, 67);
		panelFooter.add(btnFooterNews);
		
		btnFooterDonate = new JButton("Donate");
		footer_icon = new ImageIcon(EduFunds.class.getResource("/Icons/donate.png"));
		img = footer_icon.getImage() ;  
		newimg = img.getScaledInstance( 40, 40,  java.awt.Image.SCALE_SMOOTH ) ;  
		footer_icon = new ImageIcon( newimg );
		btnFooterDonate.setIcon(footer_icon);
		btnFooterDonate.setBackground(Color.WHITE);
		btnFooterDonate.setBounds(259, 0, 260, 67);
		btnFooterDonate.setFocusable(false);
		btnFooterDonate.setForeground(Color.black);
		panelFooter.add(btnFooterDonate);
		btnFooterDonate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cl2 = (CardLayout)(panelLandingCard.getLayout());
				cl2.show(panelLandingCard, "panel_landingDonate");
				setActive("Donate");
				frame.repaint();
			}
		});
		
	
		btnFooterAnalytics = new JButton("Analytics\r\n");
		footer_icon = new ImageIcon(EduFunds.class.getResource("/Icons/analytics.png"));
		img = footer_icon.getImage() ;  
		newimg = img.getScaledInstance( 40, 40,  java.awt.Image.SCALE_SMOOTH ) ;  
		footer_icon = new ImageIcon( newimg );
		btnFooterAnalytics.setIcon(footer_icon);
		btnFooterAnalytics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl2 = (CardLayout)(panelLandingCard.getLayout());
				cl2.show(panelLandingCard, "panel_landingAnalytics");
				setActive("Analytics");
				frame.repaint();
			}
		});
		btnFooterAnalytics.setForeground(Color.black);
		btnFooterAnalytics.setFocusable(false);
		btnFooterAnalytics.setBackground(Color.WHITE);
		btnFooterAnalytics.setBounds(517, 0, 248, 67);
		panelFooter.add(btnFooterAnalytics);
		
		btnFooterProfile = new JButton("Profile");
		footer_icon = new ImageIcon(EduFunds.class.getResource("/Icons/profile.png"));
		img = footer_icon.getImage() ;  
		newimg = img.getScaledInstance( 40, 40,  java.awt.Image.SCALE_SMOOTH ) ;  
		footer_icon = new ImageIcon( newimg );
		btnFooterProfile.setIcon(footer_icon);
		btnFooterProfile.setFocusable(false);
		btnFooterProfile.setForeground(Color.white);
		btnFooterProfile.setBackground(eduFundsGreen);
		btnFooterProfile.setBounds(764, 0, 230, 67);
		panelFooter.add(btnFooterProfile);
		btnFooterProfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cl2 = (CardLayout)(panelLandingCard.getLayout());
				cl2.show(panelLandingCard, "panel_landingProfile");
				setActive("Profile");
				frame.repaint();
			}
		});
		
		
		panelLandingCard = new JPanel();
		panelLandingCard.setBackground(Color.WHITE);
		panelLandingCard.setBounds(0, 0, 994, 606);
		panelLanding.add(panelLandingCard);
		panelLandingCard.setLayout(new CardLayout(0, 0));
		
		
		
		JPanel panelLandingDonate = new JPanel();
		panelLandingDonate.setBackground(Color.WHITE);
		panelLandingDonate.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelLandingCard.add(panelLandingDonate, "panel_landingDonate");
		panelLandingCard.setBackground(Color.white);
		panelLandingDonate.setLayout(null);
		
		panelLandingDonateList = new JPanel();
		panelLandingDonateList.setBackground(Color.WHITE);
		JScrollPane spLandingDonate = new JScrollPane(panelLandingDonateList);
		spLandingDonate.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelLandingDonateList.setLayout(new BoxLayout(panelLandingDonateList, BoxLayout.Y_AXIS));
		spLandingDonate.setBounds(247, 0, 530, 606);
		spLandingDonate.setBackground(Color.white);
		spLandingDonate.getViewport().setBackground(Color.white);
		spLandingDonate.setHorizontalScrollBar(null);
		panelLandingDonate.add(spLandingDonate);
		
		
		
		panelLandingDonateToOrg = new JPanel();
		panelLandingDonateToOrg.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelLandingDonateToOrg.setBackground(Color.WHITE);
		panelLandingCard.add(panelLandingDonateToOrg, "panel_landingDonateToOrg");
		panelLandingDonateToOrg.setLayout(null);
		
		JButton btnOpenLandingDonate = new JButton("<html><u>Back</u></html>");
		btnOpenLandingDonate.setHorizontalAlignment(SwingConstants.LEADING);
		btnOpenLandingDonate.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
		btnOpenLandingDonate.setBackground(Color.WHITE);
		btnOpenLandingDonate.setForeground(eduFundsGreen);
		btnOpenLandingDonate.setOpaque(false);
		btnOpenLandingDonate.setContentAreaFilled(true);
		btnOpenLandingDonate.setEnabled(true);
		btnOpenLandingDonate.setBounds(10, 11, 58, 23);
		btnOpenLandingDonate.setBorderPainted(false);
		btnOpenLandingDonate.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e) {
						lblDonationResult.setText("");
						CardLayout cl2 = (CardLayout)(panelLandingCard.getLayout());
						cl2.show(panelLandingCard, "panel_landingDonate");
					}
				}
				);	
		panelLandingDonateToOrg.add(btnOpenLandingDonate);
		
		JPanel panelDonateOrgDP = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.white);
				g2.fillRect(0, 0, 160, 100);
				if(donateOrganisation!=null)
				{
				g2.drawImage(new ImageIcon(getClass().getResource(donateOrganisation.getLogo())).getImage(),10,10,230,180,this);
				}
			}
			};
		panelDonateOrgDP.setBackground(Color.WHITE);
		panelDonateOrgDP.setBounds(372, 85, 250, 200);
		panelLandingDonateToOrg.add(panelDonateOrgDP);
		
		lblDonateOrgName = new JLabel("Org Name");
		lblDonateOrgName.setHorizontalAlignment(SwingConstants.CENTER);
		lblDonateOrgName.setFont(new Font("Trebuchet MS", Font.BOLD | Font.ITALIC, 18));
		lblDonateOrgName.setBounds(372, 296, 250, 44);
		panelLandingDonateToOrg.add(lblDonateOrgName);
		
		JLabel lblNewLabel_6 = new JLabel("How much do you wish to donate?");
		lblNewLabel_6.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
		lblNewLabel_6.setBounds(284, 351, 442, 44);
		panelLandingDonateToOrg.add(lblNewLabel_6);
		
		tfDonate = new JTextField();
		tfDonate.setBounds(372, 404, 250, 44);
		panelLandingDonateToOrg.add(tfDonate);
		tfDonate.setColumns(10);
		
		JButton btnDonateToOrg = new JButton("Donate");
		btnDonateToOrg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!tfDonate.getText().equals("")) {
					try {
						makeDonation();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnDonateToOrg.setForeground(Color.white);
		btnDonateToOrg.setBackground(eduFundsGreen);
		btnDonateToOrg.setFocusable(false);
		btnDonateToOrg.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
		btnDonateToOrg.setBounds(394, 501, 201, 44);
		panelLandingDonateToOrg.add(btnDonateToOrg);
		
		lblDonationResult = new JLabel("");
		lblDonationResult.setFont(new Font("Trebuchet MS", Font.ITALIC, 16));
		lblDonationResult.setBounds(623, 501, 226, 44);
		lblDonationResult.setForeground(eduFundsGreen);
		panelLandingDonateToOrg.add(lblDonationResult);
		
		JPanel panelLandingNews = new JPanel();
		panelLandingNews.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelLandingNews.setBackground(Color.WHITE);
		panelLandingCard.add(panelLandingNews, "panel_landingNews");
		panelLandingNews.setLayout(null);
		
		
		panelLandingFeedList = new JPanel();
		panelLandingFeedList.setBorder(new LineBorder(new Color(0, 0, 0)));
		JScrollPane spLandingFeed = new JScrollPane(panelLandingFeedList);
		spLandingFeed.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panelLandingFeedList.setLayout(new BoxLayout(panelLandingFeedList, BoxLayout.Y_AXIS));
		spLandingFeed.setBounds(236, 0, 560, 606);
		panelLandingNews.add(spLandingFeed);
		
		JPanel panelLandingAnalytics = new JPanel();
		panelLandingAnalytics.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelLandingAnalytics.setBackground(Color.WHITE);
		panelLandingCard.add(panelLandingAnalytics, "panel_landingAnalytics");
		panelLandingAnalytics.setLayout(null);
		
		JPanel panelLandingAnalyticsDonor = new JPanel();
		panelLandingAnalyticsDonor.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelLandingAnalyticsDonor.setBackground(Color.WHITE);
		panelLandingAnalyticsDonor.setBounds(20, 121, 466, 393);
		panelLandingAnalytics.add(panelLandingAnalyticsDonor);
		panelLandingAnalyticsDonor.setLayout(null);
		
		JLabel lblNewLabel_2 = new JLabel("Highest Donor");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblNewLabel_2.setBounds(10, 6, 446, 54);
		panelLandingAnalyticsDonor.add(lblNewLabel_2);
		
		JLabel lblNewLabel_4 = new JLabel("Donated");
		lblNewLabel_4.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4.setBounds(37, 328, 192, 54);
		panelLandingAnalyticsDonor.add(lblNewLabel_4);
		
		JLabel lblLandingHighestDonatedVal = new JLabel(""+highestDonor.getDonated());
		lblLandingHighestDonatedVal.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		lblLandingHighestDonatedVal.setHorizontalAlignment(SwingConstants.CENTER);
		lblLandingHighestDonatedVal.setBounds(239, 328, 192, 54);
		panelLandingAnalyticsDonor.add(lblLandingHighestDonatedVal);
		
		JLabel lblLandingHighestDonorUsername = new JLabel(highestDonor.getUserName());
		lblLandingHighestDonorUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblLandingHighestDonorUsername.setFont(new Font("Trebuchet MS", Font.BOLD | Font.ITALIC, 16));
		lblLandingHighestDonorUsername.setBounds(125, 266, 200, 50);
		panelLandingAnalyticsDonor.add(lblLandingHighestDonorUsername);
		
		JPanel panelHighestDonorDP = new JPanel()
				{
				public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.white);
				g2.fillRect(0, 0, 240, 200);
				int pointer = highestDonor.getAvatar();
				System.out.println("Avatar is "+ pointer);
				int horVal = 975/5;
				int vertVal = 780/4;
				int vertMult = pointer/5;
				int horMult = pointer%5;
				g2.drawImage(new ImageIcon(getClass().getResource("Avatars/Avatars.jpeg")).getImage(),
						10,10,220,190,
						(horVal * (horMult))+20, (vertVal*vertMult), (horVal * (horMult +1)), (vertVal*(vertMult+1)),
						this);
				}
				};
		panelHighestDonorDP.setBackground(Color.WHITE);
		panelHighestDonorDP.setBounds(113, 66, 240, 200);
		panelLandingAnalyticsDonor.add(panelHighestDonorDP);
		
		JPanel panelLandingAnalyticsReceiver = new JPanel();
		panelLandingAnalyticsReceiver.setLayout(null);
		panelLandingAnalyticsReceiver.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelLandingAnalyticsReceiver.setBackground(Color.WHITE);
		panelLandingAnalyticsReceiver.setBounds(518, 121, 466, 393);
		panelLandingAnalytics.add(panelLandingAnalyticsReceiver);
		
		JLabel lblHighestReceiver = new JLabel("Highest Receiver");
		lblHighestReceiver.setHorizontalAlignment(SwingConstants.CENTER);
		lblHighestReceiver.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
		lblHighestReceiver.setBounds(10, 6, 446, 54);
		panelLandingAnalyticsReceiver.add(lblHighestReceiver);
		
		JLabel lblReceived = new JLabel("Received");
		lblReceived.setHorizontalAlignment(SwingConstants.CENTER);
		lblReceived.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		lblReceived.setBounds(39, 328, 192, 54);
		panelLandingAnalyticsReceiver.add(lblReceived);
		
		JLabel lblReceivedval = new JLabel(highestOrganisation.getDonations()+"");
		lblReceivedval.setHorizontalAlignment(SwingConstants.CENTER);
		lblReceivedval.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		lblReceivedval.setBounds(241, 328, 192, 54);
		panelLandingAnalyticsReceiver.add(lblReceivedval);
		
		JPanel panel = new JPanel()
				{
				public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.white);
				g2.fillRect(0, 0, 240, 200);
				g2.drawImage(new ImageIcon(getClass().getResource(highestOrganisation.getLogo())).getImage(),
						10,10,220,190,this);
				}	
				}
				;
		panel.setBackground(Color.WHITE);
		panel.setBounds(112, 71, 240, 200);
		panelLandingAnalyticsReceiver.add(panel);
		
		JLabel lblNewLabel_3 = new JLabel(highestOrganisation.getName());
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setFont(new Font("Trebuchet MS", Font.BOLD | Font.ITALIC, 16));
		lblNewLabel_3.setBounds(122, 277, 200, 50);
		panelLandingAnalyticsReceiver.add(lblNewLabel_3);
		
		JPanel panelLandingProfile = new JPanel();
		panelLandingProfile.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelLandingCard.add(panelLandingProfile, "panel_landingProfile");
		panelLandingProfile.setBackground(Color.WHITE);
		panelLandingProfile.setLayout(null);
		
		
		CardLayout cl2 = (CardLayout)(panelLandingCard.getLayout());
		cl2.show(panelLandingCard, "panel_landingProfile");
		
		panelLandingProfileDP = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.white);
				g2.fillRect(0, 0, 280, 200);
				int pointer = user.getAvatar();
				System.out.println("Avatar is "+ pointer);
				int horVal = 975/5;
				int vertVal = 780/4;
				int vertMult = pointer/5;
				int horMult = pointer%5;
				g2.drawImage(new ImageIcon(getClass().getResource("Avatars/Avatars.jpeg")).getImage(),
						10,10,270,190,
						(horVal * (horMult))+20, (vertVal*vertMult), (horVal * (horMult +1)), (vertVal*(vertMult+1)),
						this);
			}
		};
		panelLandingProfileDP.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelLandingProfileDP.setBounds(40, 11, 280, 200);
		panelLandingProfile.add(panelLandingProfileDP);
		
		JPanel panelLandingProfileForm = new JPanel();
		panelLandingProfileForm.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelLandingProfileForm.setBackground(Color.WHITE);
		panelLandingProfileForm.setBounds(40, 222, 280, 323);
		panelLandingProfile.add(panelLandingProfileForm);
		panelLandingProfileForm.setLayout(null);
		
		lblLandingProfileUsername = new JLabel(user!=null?user.getUserName():"");
		lblLandingProfileUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblLandingProfileUsername.setFont(new Font("Trebuchet MS", Font.BOLD | Font.ITALIC, 24));
		lblLandingProfileUsername.setBounds(10, 11, 260, 57);
		lblLandingProfileUsername.setForeground(eduFundsGreen);
		panelLandingProfileForm.add(lblLandingProfileUsername);
		
		lblLandingProfileFirstname = new JLabel(user!=null?user.getFirstName():"");
		lblLandingProfileFirstname.setHorizontalAlignment(SwingConstants.CENTER);
		lblLandingProfileFirstname.setFont(new Font("Trebuchet MS", Font.PLAIN, 24));
		lblLandingProfileFirstname.setBounds(10, 69, 260, 57);
		panelLandingProfileForm.add(lblLandingProfileFirstname);
		
		lblLandingProfileSecondname = new JLabel(user!=null?user.getLastName():"");
		lblLandingProfileSecondname.setHorizontalAlignment(SwingConstants.CENTER);
		lblLandingProfileSecondname.setFont(new Font("Trebuchet MS", Font.PLAIN, 24));
		lblLandingProfileSecondname.setBounds(10, 123, 260, 57);
		panelLandingProfileForm.add(lblLandingProfileSecondname);
		
		JLabel lblLandingProfileDonated = new JLabel("Donated ");
		lblLandingProfileDonated.setHorizontalAlignment(SwingConstants.CENTER);
		lblLandingProfileDonated.setFont(new Font("Trebuchet MS", Font.BOLD, 20));
		lblLandingProfileDonated.setBounds(10, 204, 260, 33);
		lblLandingProfileDonated.setForeground(eduFundsGreen);
		panelLandingProfileForm.add(lblLandingProfileDonated);
		
		lblLandingProfileDonatedVal = new JLabel(user!=null?""+user.getDonated():"");
		lblLandingProfileDonatedVal.setFont(new Font("Tahoma", Font.BOLD, 36));
		lblLandingProfileDonatedVal.setHorizontalAlignment(SwingConstants.CENTER);
		lblLandingProfileDonatedVal.setBounds(10, 224, 260, 88);
		lblLandingProfileDonatedVal.setForeground(eduFundsGreen);
		panelLandingProfileForm.add(lblLandingProfileDonatedVal);
		
		JButton btnLandingLogOut = new JButton("Log out");
		btnLandingLogOut.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
		btnLandingLogOut.setBackground(eduFundsGreen);
		btnLandingLogOut.setForeground(Color.white);
		btnLandingLogOut.setBounds(40, 556, 280, 36);
		btnLandingLogOut.setFocusable(false);
		panelLandingProfile.add(btnLandingLogOut);
		
		JLabel lblLandingProfileDonationHistory = new JLabel("Donation History");
		lblLandingProfileDonationHistory.setHorizontalAlignment(SwingConstants.CENTER);
		lblLandingProfileDonationHistory.setFont(new Font("Trebuchet MS", Font.BOLD | Font.ITALIC, 24));
		lblLandingProfileDonationHistory.setBounds(330, 11, 654, 43);
		lblLandingProfileDonationHistory.setForeground(eduFundsGreen);
		
		
		panelLandingProfile.add(lblLandingProfileDonationHistory);
		
		panelDHView = new JPanel();
		spLandingProfile = new JScrollPane(panelDHView);
		spLandingProfile.setHorizontalScrollBar(null);
		panelDHView.setLayout(new BoxLayout(panelDHView, BoxLayout.Y_AXIS));
		spLandingProfile.setBounds(340, 65, 640, 480);
		panelLandingProfile.add(spLandingProfile);
		spLandingProfile.getViewport().setOpaque(false);
		spLandingProfile.setOpaque(false);
		
		
		
		
		spLandingProfile.getViewport().setLayout(new FlowLayout());
		btnLandingLogOut.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					 CardLayout cl = (CardLayout)(panelBody.getLayout());
					 cl.show(panelBody, "panel_Login");
					 frame.repaint();
				}
				}
				);
		
		JPanel panelCreateAccount = new JPanel();
		panelCreateAccount.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelCreateAccount.setBackground(Color.WHITE);
		panelBody.add(panelCreateAccount, "panel_CreateAccount");
		panelCreateAccount.setLayout(null);
		
		JLabel lblCreateAccountHeader = new JLabel("Create an Edufunds account");
		lblCreateAccountHeader.setFont(new Font("Trebuchet MS", Font.PLAIN, 24));
		lblCreateAccountHeader.setBounds(23, 37, 454, 61);
		lblCreateAccountHeader.setForeground(eduFundsGreen);
		panelCreateAccount.add(lblCreateAccountHeader);
		
		JLabel lblNewLabel_1 = new JLabel("Enter a username so we know it's you.");
		lblNewLabel_1.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		lblNewLabel_1.setBounds(41, 133, 465, 31);
		panelCreateAccount.add(lblNewLabel_1);
		
		tfCreateAccountUsername = new JTextField();
		tfCreateAccountUsername.setBounds(41, 175, 258, 31);
		panelCreateAccount.add(tfCreateAccountUsername);
		tfCreateAccountUsername.setColumns(10);
		
		JLabel lblEnterYourFirst = new JLabel("Enter your first name.");
		lblEnterYourFirst.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		lblEnterYourFirst.setBounds(41, 217, 465, 31);
		panelCreateAccount.add(lblEnterYourFirst);
		
		tfCreateAccountFirstName = new JTextField();
		tfCreateAccountFirstName.setColumns(10);
		tfCreateAccountFirstName.setBounds(41, 259, 258, 31);
		panelCreateAccount.add(tfCreateAccountFirstName);
		
		JLabel lblEnterYourLast = new JLabel("Enter your last name.");
		lblEnterYourLast.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		lblEnterYourLast.setBounds(41, 301, 465, 31);
		panelCreateAccount.add(lblEnterYourLast);
		
		tfCreateAccountLastName = new JTextField();
		tfCreateAccountLastName.setColumns(10);
		tfCreateAccountLastName.setBounds(41, 343, 258, 31);
		panelCreateAccount.add(tfCreateAccountLastName);
		
		JLabel lblPasswordsHelpKeep = new JLabel("Passwords help keep your account secure.");
		lblPasswordsHelpKeep.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		lblPasswordsHelpKeep.setBounds(41, 395, 465, 31);
		panelCreateAccount.add(lblPasswordsHelpKeep);
		
		pfCreateAccountPassword = new JPasswordField();
		pfCreateAccountPassword.setBounds(41, 437, 258, 31);
		panelCreateAccount.add(pfCreateAccountPassword);
		
		JLabel lblConfirmPassword = new JLabel("Confirm Password.");
		lblConfirmPassword.setFont(new Font("Trebuchet MS", Font.BOLD, 16));
		lblConfirmPassword.setBounds(41, 479, 465, 31);
		panelCreateAccount.add(lblConfirmPassword);
		
		pfCreateAccountConfirmPassword = new JPasswordField();
		pfCreateAccountConfirmPassword.setBounds(41, 521, 258, 31);
		panelCreateAccount.add(pfCreateAccountConfirmPassword);
		
		JButton btnCreateAccount = new JButton("Create Account");
		btnCreateAccount.setContentAreaFilled(true);
		btnCreateAccount.setFocusable(false);
		btnCreateAccount.setBackground(eduFundsGreen);
		btnCreateAccount.setForeground(Color.white);
		btnCreateAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(createAccount(tfCreateAccountUsername.getText(), tfCreateAccountFirstName.getText(),
							tfCreateAccountLastName.getText(), new String(pfCreateAccountPassword.getPassword()),
							new String( pfCreateAccountConfirmPassword.getPassword())
							))
					{
					 CardLayout cl = (CardLayout)(panelBody.getLayout());
					 cl.show(panelBody, "panel_Dashboard");
					 frame.repaint();
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnCreateAccount.setBounds(783, 629, 174, 31);
		panelCreateAccount.add(btnCreateAccount);
		
		JButton btnCreateAccountBack = new JButton("Back");
		btnCreateAccountBack.setContentAreaFilled(true);
		btnCreateAccountBack.setFocusable(false);
		btnCreateAccountBack.setBackground(Color.white);
		btnCreateAccountBack.setForeground(eduFundsGreen);
		btnCreateAccountBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CardLayout cl = (CardLayout)(panelBody.getLayout());
				 cl.show(panelBody, "panel_Login");
				 frame.repaint();
			}
		});
		btnCreateAccountBack.setBounds(663, 629, 105, 31);
		panelCreateAccount.add(btnCreateAccountBack);
		
		lblCreateAccountErrorMessage = new JLabel("");
		lblCreateAccountErrorMessage.setForeground(Color.RED);
		lblCreateAccountErrorMessage.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		lblCreateAccountErrorMessage.setBounds(41, 578, 436, 31);
		panelCreateAccount.add(lblCreateAccountErrorMessage);
		
		JLabel lblNewLabel = new JLabel("Select An Avatar");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
		lblNewLabel.setBounds(400, 519, 557, 31);
		panelCreateAccount.add(lblNewLabel);
		
		JPanel avatarContainer = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.fillRect(0, 0, 400, 400);
				g2.drawImage(new ImageIcon(getClass().getResource("Avatars/Avatars.jpeg")).getImage(),0,0,560,400,this);
				g2.drawImage(new ImageIcon(getClass().getResource("Avatars/tick.png")).getImage(), 
						(560/5)*(selectedAvatar%5)+80, (400/4)*(selectedAvatar/5)+10, 30, 30, this);
			}
		};
		
		avatarContainer.addMouseListener(new MouseListener()
				{

					@Override
					public void mouseClicked(MouseEvent e) {
						// TODO Auto-generated method stub
						int x = e.getX();
						int y = e.getY();
						//Accounting for x coordinate
						selectedAvatar= x*5/560;
						//Accounting for y coordinate
						selectedAvatar+= (y/100)*5;
						avatarContainer.repaint();
					}

					@Override
					public void mouseEntered(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseExited(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mousePressed(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseReleased(MouseEvent arg0) {
						// TODO Auto-generated method stub
						
					}
				}
				);
		
		avatarContainer.setBounds(400, 110, 560, 400);
		panelCreateAccount.add(avatarContainer);

	}
	
	private static void initProfileFormDetails() {
		lblLandingProfileFirstname.setText(user.getFirstName());
		lblLandingProfileSecondname.setText(user.getLastName());
		lblLandingProfileUsername.setText(user.getUserName());
		lblLandingProfileDonatedVal.setText(user.getDonated()+"");
		System.out.println(user.getAvatar()+" is the avatar value");
		panelLandingProfileDP.repaint();
	}
	
	
	
	private static void setActive(String buttonName) {
		btnFooterProfile.setBackground(Color.white);
		btnFooterNews.setBackground(Color.white);
		btnFooterAnalytics.setBackground(Color.white);
		btnFooterDonate.setBackground(Color.white);
		btnFooterProfile.setForeground(Color.black);
		btnFooterAnalytics.setForeground(Color.black);
		btnFooterNews.setForeground(Color.black);
		btnFooterDonate.setForeground(Color.black);
		
		if(buttonName.equals("News")) {
			btnFooterNews.setBackground(eduFundsGreen);
			btnFooterNews.setForeground(Color.white);
		}
		else if(buttonName.equals("Profile")) {
			btnFooterProfile.setBackground(eduFundsGreen);
			btnFooterProfile.setForeground(Color.white);
		}
		else if(buttonName.equals("Analytics")) {
			btnFooterAnalytics.setBackground(eduFundsGreen);
			btnFooterAnalytics.setForeground(Color.white);
		}
		else {
			btnFooterDonate.setBackground(eduFundsGreen);
			btnFooterDonate.setForeground(Color.white);
		}
		
	}
	
	private static void fillDonationHistory() {
		panelDHView.removeAll();
		for(int i =0; i<donationData.size(); i++) {
			if(donationData.get(i).getDonor().equals(user.getUserName()))
			{
			panelDHView.add(new DonationHistoryListItem(donationData.get(i).getReceiver(),
					donationData.get(i).getDonation(),
					donationData.get(i).getLogo()
					));
			
			System.out.println("Added component to screen");
			}
	
		}
		spLandingProfile.repaint();
	}
	
	private static void fillLandingDonate() {
		panelLandingDonateList.removeAll();
		for(int i =0; i < organisationData.size(); i++) {
			panelLandingDonateList.add(new DonateListItem(organisationData.get(i)));
			System.out.println("Added donate List Item to screen");
		}
	}
	
	private static void fillFeed() {
		panelLandingFeedList.removeAll();
		for(int i =donationData.size()-1; i >= 0; i--) {
			panelLandingFeedList.add(new FeedListItem(donationData.get(i)));
			System.out.println("Added feed List Item to screen");
		}
	}
	
	public static void switchToDonateScreen(Organisation org) {
		CardLayout cl2 = (CardLayout)(panelLandingCard.getLayout());
		cl2.show(panelLandingCard, "panel_landingDonateToOrg");
		donateOrganisation = org;
		lblDonateOrgName.setText(donateOrganisation.getName());
		lblDonationResult.setText("");
	}
	
	public static void makeDonation() throws SQLException {
		String query = "insert into donation(donor,receiver, avatar, logo, donation) values(?,?,?,?,?)";
		PreparedStatement ps = con.prepareStatement(query);
		ps.setString(1, user.getUserName());
		ps.setString(2, donateOrganisation.getName());
		ps.setInt(3, user.getAvatar());
		ps.setString(4, donateOrganisation.getLogo());
		ps.setInt(5, Integer.parseInt(tfDonate.getText()));
		ps.executeUpdate();
		ps.close();
		
		String user_query = "update user set donations = ? where username = ?";
		PreparedStatement ps2 = con.prepareStatement(user_query);
		int don_val = user.getDonated() + Integer.parseInt(tfDonate.getText());
		lblLandingProfileDonatedVal.setText(""+don_val);
		ps2.setInt(1, don_val);
		ps2.setString(2, user.getUserName());
		ps2.close();
		
		String org_query = "update organisation set donations = ? where username= ?";
		PreparedStatement ps3 = con.prepareStatement(org_query);
		int org_val = donateOrganisation.getDonations() + Integer.parseInt(tfDonate.getText());
		ps3.setInt(1, org_val);
		ps3.setString(2, donateOrganisation.getName());
		ps3.close();
		
		Toolkit.getDefaultToolkit().beep();
		lblDonationResult.setText("Donation made! Great Going!");
		System.out.println("Made donation");
		
		getDonationData();
		fillFeed();
		fillDonationHistory();
	}
}
