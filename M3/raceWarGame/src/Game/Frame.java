package Game;
import Characters.*;
import Weapons.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;
import java.io.*;
import java.sql.*;
import java.util.*;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicProgressBarUI;


public class Frame extends JFrame {
	
	//Visual elements
	private JPanel playerPanel, playerHBPanel, enemyPanel, enemyHBPanel, playPanel, attackPanel, battlePanel, optionPanel, allyStats, enemyStats;
	private JButton fightButton, attackButton, clearConsole, warriorButton, weaponButton, rankingButton;
	private JTextArea console;
	private JScrollPane scroll;
	private String fightText ="";
	
	//Health and statistics bars
	private JProgressBar healthBar,strengthBar, dexterityBar,speedBar,defenseBar, playerHPBar, enemyHPBar ;
	
	//Warrior images
	private BufferedImage allyImage, enemyImage, allyHand, enemyHand;
	private JPanel allyImagePanel, enemyImagePanel, allyHandPanel, enemyHandPanel;
	
	//Battle attributes
	private Warrior globalAlly=null, globalEnemy=null;
	int playerTurn = 0, allyHP=0, enemyHP=0;
	private int playerPoints=0;
	private String playerName = "Player";
	private int playerID;
	
	//Database connection
	private String url="jdbc:mysql://localhost/races?serverTimezone=UTC";
	private String user="root";
	private String password="P@ssw0rd";
	
	//Boolean to determine if the turn is done
	boolean turnDecided = false;
	//Boolean to see if the call to Frame comes from a win state
	boolean win = false;
		
//Weapons and warriors
	private WeaponContainer weapCont = new WeaponContainer();
	private WarriorContainer warCont = new WarriorContainer();
	private ArrayList<Weapon> weapons = new ArrayList<Weapon>();
	private ArrayList<Warrior> warriors = new ArrayList<Warrior>();
	private Button[] warrButton, weapButton;

	private int j=0;
//Player character warrior and weapon
	private Warrior alliedWarrior = null;
	private Weapon alliedWeapon=null;
//Panels for selection menus
	private JPanel warriorSelection, weaponSelection, nameSelection;
	private BufferedImage background = null;
	
	//FRAME CONSTRUCTOR
	public Frame(boolean warSel, Warrior warrior, boolean win, int ID, int points) {
		setSize ( 900, 600 );
		setLocationRelativeTo ( null ); //Centering the frame
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Races Game");
		setIconImage(new ImageIcon("Images/StatsImages/icon.png").getImage());
		setResizable(false);
		//Call function to initialize visual elements
		initComponent();
		
		//Check if a warrior has been selected
		if (warSel==false) {
		//Begin warrior and weapon selection
		warriorSelection.setVisible(true);
		weaponSelection.setVisible(false);
		}
		else {
			//Set player character as warrior selected
			alliedWarrior = warrior;
			//Set playerID and points according to values (in case the player comes from a win state)
			playerID = ID;
			playerPoints = points;
			//Check if the player comes from a win state
			if (win == false) {
				//Weapon selection interface and listener setting
				add(weaponSelection);

				int i =0;
					for (Weapon w : weapons){
						weapButton=new Button[weapons.size()];
						BufferedImage image=w.getImage();
						image=resize(image,200,200);
						weapButton[i]=new Button(new ImageIcon(image),i);
						weapButton[i].setOpaque(false);
					    weapButton[i].setContentAreaFilled(false);
					    weapButton[i].setBorderPainted(true);
					    weapButton[i].setBorder(BorderFactory.createLineBorder(Color.black));
					    setWeaponListener(i);
						if (w.getRace().contains(warrior.getRace())) {
						weaponSelection.add(weapButton[i]);			
							
					}
						i++;
				}
					//Visibility of selection menus
					warriorSelection.setVisible(false);
					weaponSelection.setVisible(false);
					weaponSelection.setVisible(true);
			}
			else {
				//If player comes from win state, generate enemy and call the Battle interface
				Warrior enemy = enemyGeneration();
				BattleInterfaces(getAlliedWarrior(), enemy) ;
			}
			
			
		}
		//Visibility of frame
		setVisible(true);
	}	
	
//METHODS
	
	//Initialize visual components
	public void initComponent() {
		//Load info from database
		weapCont.setWeapons(weapons);
		warCont.setWarriors(warriors);
		warrButton=new Button[warriors.size()];
	//Warrior selection panel
		setWarriorPanel(background);
}
	
	public void setWarriorPanel(BufferedImage background) {
		//Set background for panel
		try {
			background = ImageIO.read(new File("Images/CharacterImages/background3.jpg"));
			warriorSelection=new ImageComponent(background);
		} catch (IOException e1) {
			e1.printStackTrace();
			
		}
		
		//Set grid layout for warrior panel
		warriorSelection.setLayout(new GridLayout(1,3));
		int i=0;
		//Add warrior images and buttons to panel
		for (Warrior w : warriors){
			
			BufferedImage image=w.getImage();
			image=resize(image,300,563);
			
	        warrButton[i]=new Button(new ImageIcon(image),i);
	        warrButton[i].setOpaque(false);
	        warrButton[i].setContentAreaFilled(false);
	        warrButton[i].setBorderPainted(true);
	        warrButton[i].setBorder(BorderFactory.createLineBorder(Color.black));
	  
			warriorSelection.add(warrButton[i]);	
		
			i++;
		
		}
		//Set listener for when the images are clicked
		setWarriorListener();

		warriorSelection.setVisible(false);
		add(warriorSelection);
		
		//Set weapon panel
		setWeaponPanel(background);
		
	}

	public void setWeaponPanel(BufferedImage background) {
		//Load background for weapon selection panel
		try {
			background = ImageIO.read(new File("Images/CharacterImages/background2.jpg"));
			weaponSelection=new ImageComponent(background);
		} catch (IOException e1) {
			e1.printStackTrace();
			
		}
		//Set grid layout for weapon panel
		weaponSelection.setLayout(new GridLayout(3,3));
	}
	//Method for resizing images
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}  
	
//LISTENERS
	//Set listener for warrior selection buttons
	public void setWarriorListener() {
		j=0;
		for (j=0;j<warriors.size();j++) {
		warrButton[j].addActionListener(
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						//Get button pressed
						Button b = (Button) arg0.getSource();
						
						//Select warrior according to button
						Warrior model=warriors.get(b.getId());
						//Instantiate warrior according to race
						if (model.getRace().compareToIgnoreCase("Human")==0) {
							alliedWarrior=new Human(model.getId(),model.getName(),model.getRace(),model.getImage(),model.getHp(),model.getStrength(),model.getDefense(),model.getAgility(),model.getSpeed(),model.getWeapon(),model.getPoints());
						}else if (model.getRace().compareToIgnoreCase("Dwarf")==0) {
							alliedWarrior=new Dwarf(model.getId(),model.getName(),model.getRace(),model.getImage(),model.getHp(),model.getStrength(),model.getDefense(),model.getAgility(),model.getSpeed(),model.getWeapon(),model.getPoints());
						}else if (model.getRace().compareToIgnoreCase("Elf")==0) {
							alliedWarrior=new Elf(model.getId(),model.getName(),model.getRace(),model.getImage(),model.getHp(),model.getStrength(),model.getDefense(),model.getAgility(),model.getSpeed(),model.getWeapon(),model.getPoints());
						}

						//Hide warrior selection menu
						warriorSelection.setVisible(false);
						
						//Show weapon selection menu
						add(weaponSelection);
						
						int i =0;
							for (Weapon w : weapons){
								weapButton=new Button[weapons.size()];
								BufferedImage image=w.getImage();
								image=resize(image,200,200);
								weapButton[i]=new Button(new ImageIcon(image),i);
								weapButton[i].setOpaque(false);
							    weapButton[i].setContentAreaFilled(false);
							    weapButton[i].setBorderPainted(true);
							    weapButton[i].setBorder(BorderFactory.createLineBorder(Color.black));
							    setWeaponListener(i);
							    //Get weapons according to race
								if (w.getRace().contains(warriors.get(b.getId()).getRace())) {
									weaponSelection.add(weapButton[i]);				
								
							}
							i++;
						}
						weaponSelection.setVisible(true);
							
					}
				}
			);
		
		}
	}
		
	//Set listener for weapon selection buttons	
	public void setWeaponListener(int i) {

		weapButton[i].addActionListener(
			new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					//Add weapon selected to warrior
					alliedWeapon=weapons.get(i);
					alliedWarrior.setWeapon(alliedWeapon);
					weaponSelection.setVisible(false);
					//Show Name Entering Panel
					ImageComponent namewarwepP;
					
					try {
						//Set background for panel
						BufferedImage background = ImageIO.read(new File("Images/CharacterImages/background2.jpg"));
						namewarwepP= new ImageComponent(background);
						
						//Set subpanels for current panel
						JPanel warwepP= new JPanel();
						JPanel warriorP= new JPanel();
						JPanel weaponP= new JPanel();
						//Player name field label
						JLabel chooseName=new JLabel("Enter your name:");
						JButton ok=new JButton("Ok");
						nameSelection=new JPanel();
						JTextField selectedName=new JTextField(40);
						//Show warrior and weapon selected
						ImageComponent warriorImage=new ImageComponent(alliedWarrior.getImage());						
						ImageComponent weaponImage = new ImageComponent(alliedWeapon.getImage());
						//Set image sizes
						warriorImage.setPreferredSize(new Dimension(200,400));
						warriorImage.setMinimumSize(new Dimension(200,400));
						weaponImage.setPreferredSize(new Dimension(200,200));
						weaponImage.setMinimumSize(new Dimension(200,200));
						//Set panel box layout
						nameSelection.setLayout(new BoxLayout(nameSelection,BoxLayout.X_AXIS));
						warwepP.setLayout(new BoxLayout(warwepP, BoxLayout.X_AXIS));
						namewarwepP.setLayout(new BoxLayout(namewarwepP, BoxLayout.Y_AXIS));
						//Set panel sizes
						selectedName.setPreferredSize(new Dimension(100,30));
						chooseName.setPreferredSize(new Dimension(230,30));
						selectedName.setMaximumSize(new Dimension(100,30));
						chooseName.setMaximumSize(new Dimension(230,30));
						warriorP.setPreferredSize(new Dimension(200,400));
						warwepP.setPreferredSize(new Dimension(400,800));
						weaponP.setPreferredSize(new Dimension(200,200));
						//Adding elements to current panel
						namewarwepP.add(nameSelection);
						nameSelection.add(chooseName);
						nameSelection.add(selectedName);
						nameSelection.add(ok);
						warriorP.add(warriorImage);
						weaponP.add(weaponImage);
						warwepP.add(warriorP);
						warwepP.add(weaponP);
						namewarwepP.add(warwepP);
						//setting the panels visible
						warriorP.setVisible(true);
						weaponP.setVisible(true);
						nameSelection.setVisible(true);
						warwepP.setVisible(true);
						namewarwepP.setVisible(true);
						//Set opaque
						warriorP.setOpaque(false);
						weaponP.setOpaque(false);
						nameSelection.setOpaque(false);
						warwepP.setOpaque(false);
						add(namewarwepP);
						ok.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent arg0) {
								setDefaultCloseOperation(EXIT_ON_CLOSE);
								//If entered name is blank, leave player name as "Player", as already initialized 
								if (selectedName.getText().isBlank()) {
									
								}
								//Set player name as String entered
								else
									playerName=selectedName.getText();
								//Insert player in database
							
								try {
									Connection con=DriverManager.getConnection(url, user, password);
									con = DriverManager.getConnection(url, user, password);
									 Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
						                ResultSet rs = st.executeQuery("select * from players");
						                rs = st.getResultSet();
	
						                rs.moveToInsertRow();
						                rs.updateString(2, playerName);
						                rs.insertRow();
	
						                //Save player Id for later use
						                rs = st.executeQuery("select * from players");
						                rs = st.getResultSet();
						                rs.last();
						                playerID = rs.getInt(1);
						                
								setDefaultCloseOperation(EXIT_ON_CLOSE);
								} catch (SQLException e) {
									e.printStackTrace();
								}
								namewarwepP.setVisible(false);
								//Generate random enemy with random weapon
								Warrior enemy=enemyGeneration();
								//Start battle with warrior selected and enemy generated
								BattleInterfaces(getAlliedWarrior(), enemy) ;
							}
						});
					} catch (IOException e1) {
						e1.printStackTrace();
					}	
				}
			});
		}

	//Getter and setter for alliedWarrior
	public Warrior getAlliedWarrior() {
		return alliedWarrior;
	}

	public void setAlliedWarrior(Warrior alliedWarrior) {
		this.alliedWarrior = alliedWarrior;
	}

//BATTLE INTERFACE

	public void  BattleInterfaces(Warrior ally, Warrior enemy)  {
		
		//Set warriors and initial health values
		this.globalAlly=ally;
		this.globalEnemy=enemy;
		this.allyHP=ally.getHp();
		this.enemyHP=enemy.getHp();
		
		//Instantiation of visual elements
		optionPanel = new JPanel();
		battlePanel = new JPanel();
		playerPanel = new JPanel();
		enemyPanel = new JPanel();
		playPanel = new JPanel();
		
		//Main panel buttons
		warriorButton = new JButton("Choose Warrior");
		weaponButton = new JButton("Choose Weapon");
		rankingButton = new JButton("Ranking");
		
		optionPanel.add(warriorButton);
		optionPanel.add(weaponButton);
		optionPanel.add(rankingButton);
		
		
		
		//Images for warriors
		allyImage = ally.getImage();
		enemyImage = enemy.getImage();
		allyHand = ally.getWeapon().getImage();
		enemyHand = enemy.getWeapon().getImage();
		allyImagePanel  = new ImageComponent(allyImage);
		enemyImagePanel  = new ImageComponent(enemyImage);
		allyHandPanel  = new ImageComponent(allyHand);
		enemyHandPanel  = new ImageComponent(enemyHand);

		//Size setting
		allyImagePanel.setPreferredSize(new Dimension(300,300));
		enemyImagePanel.setPreferredSize(new Dimension(300,300));
		allyHandPanel.setPreferredSize(new Dimension(30,30));
		enemyHandPanel.setPreferredSize(new Dimension(30,30));
		 
		//Creating and adding warrior panels
		playerHBPanel = new JPanel();
		playerHPBar = healthBar(ally);
		playerHBPanel.add(playerHPBar);
		playerHBPanel.setSize(new Dimension(210,20));
		
		enemyHBPanel = new JPanel();
		enemyHPBar = healthBar(enemy);
		enemyHBPanel.add(enemyHPBar);
		enemyHBPanel.setSize(new Dimension(210,20));
		
		//Set stats panel to reflect warrior stats
		allyStats = populateStatsPanel(ally);
		enemyStats = populateStatsPanel(enemy);
		
		//Add panels for each warrior
		playerPanel.add(playerHBPanel);
		playerPanel.add(allyImagePanel);
		playerPanel.add(allyStats);
		playerPanel.setLayout(new BoxLayout(playerPanel,BoxLayout.PAGE_AXIS));
		
		enemyPanel.add(enemyHBPanel);
		enemyPanel.add(enemyImagePanel);
		enemyPanel.add(enemyStats);
		enemyPanel.setLayout(new BoxLayout(enemyPanel, BoxLayout.PAGE_AXIS));
		
		battlePanel.add(playerPanel);
		battlePanel.add(enemyPanel);
		battlePanel.setLayout(new BoxLayout(battlePanel, BoxLayout.LINE_AXIS));
		
		//Fighting panel elements
		attackButton = new JButton("Attack");
		fightButton = new JButton("Fight");
		clearConsole = new JButton("Clear Console");
		
		//Fight console with scroll bar
		console= new JTextArea(12,60);
		scroll = new JScrollPane(console,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//Attack button for the player
		attackPanel = new JPanel();
		attackPanel.add(attackButton);
		attackPanel.setVisible(false);
		//Set buttons on lower battle panel
		playPanel.add(attackPanel, BorderLayout.WEST);
		playPanel.add(fightButton, BorderLayout.CENTER);
		playPanel.add(clearConsole, BorderLayout.EAST);
		playPanel.add(scroll, BorderLayout.SOUTH);

//BATTLE ACTION LISTENERS		
		//Action Listener to change warrior
		warriorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Frame(false, null, false, 0, 0);		}
		});
		
		//Action Listener to change weapon
		weaponButton.addActionListener(new ActionListener() {
				
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new Frame(true, globalAlly, false, playerID, 0);			}
			
		});
		
		//Action Listener to start battle with "Fight" button
		fightButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//Set other buttons to not visible
				warriorButton.setVisible(false);
				weaponButton.setVisible(false);
				rankingButton.setVisible(false);
				
				//Stat setting
				ally.setHp(allyHP);
				enemy.setHp(enemyHP);
				ally.setSpeed(ally.getSpeed()+ally.getWeapon().getSpeed());
				ally.setStrength(ally.getStrength()+ally.getWeapon().getAttack());
				enemy.setSpeed(enemy.getSpeed()+enemy.getWeapon().getSpeed());
				enemy.setStrength(enemy.getStrength()+enemy.getWeapon().getAttack());
					
				//Call Fight method
				fight(ally, enemy);
					
				attackPanel.setVisible(true);
				fightButton.setVisible(false);

			}
		});
		
		//Add Action Listener to "Attack" button
		
		attackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//Call attack function for the player
				attack(globalEnemy, globalAlly);
				//Update health bar to reflect damage received
				updateHealth(globalEnemy, enemyHPBar);
				//Change turn
				playerTurn = playerTurn + 1;
				//Call fight
				fight(globalAlly, globalEnemy);
			}
		});
		
		//Clear console button
		clearConsole.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				console.setText("");
			}
		});
		
		// Ranking button
rankingButton.addActionListener(new ActionListener() {

    @Override
    public void actionPerformed(ActionEvent arg0) {
        // Database connection
        try {
            // Get all player names
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);
            ArrayList<String> ranking = new ArrayList<String>();
            PreparedStatement pst = con.prepareStatement("SELECT * FROM players");
            ResultSet rs = pst.executeQuery();
            StringBuilder sb = new StringBuilder();
            
            while (rs.next())
                ranking.add(rs.getString(2));

            // Show top 10 players
            pst = con.prepareStatement("SELECT * FROM ranking ORDER BY player_points DESC");
            rs = pst.executeQuery();

            int ranking_num = 1;

            sb.append(String.format("%-10s | %-20s | %-10s | %-20s\n", "Ranking", "Name", "Points", "Warrior"));

            while (rs.next() && ranking_num <= 10) {
                String name = ranking.get(rs.getInt(1) - 1);

                sb.append(String.format("%-10d | %-20s | %-10d | %-20s\n", ranking_num, name, rs.getInt(2), warCont.getWarrior(rs.getInt(3) - 1).getName()));
                ranking_num += 1;
            }

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            textArea.setEditable(false);

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(600, 300));

            JOptionPane.showMessageDialog(rootPane, scrollPane, "Ranking", JOptionPane.INFORMATION_MESSAGE);

        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
});
		
		
		//Add panels to main frame and set main configurations
		
		this.add(optionPanel, BorderLayout.NORTH);
		this.add(battlePanel, BorderLayout.CENTER);
		this.add(playPanel,BorderLayout.SOUTH);
		this.setSize(700,700);
		this.setVisible(true);
		
		};
		

//FIGHT METHOD	
		
	public void fight(Warrior player, Warrior enemy) {		
		
		//Set initial turn if this is the first call of the function
		if (turnDecided == false) {
			playerTurn = getFirstTurn(player, enemy);
			turnDecided = true;
		}
			
		
		//Keep the fight going until one of the warriors falls
		while (player.getHp() > 0 && enemy.getHp() > 0) {
			fightText=("**************************************\n");
			console.append(fightText);
			//If the turn is a pair number, it's the players', otherwise, it's the computer's
			if (playerTurn % 2 == 0) {
				fightText=(playerName + "'s turn: \n\n\n");
				console.append(fightText);
				//Exit while to let the player press "Attack" when they want to
				break;
			}
			else {
				fightText=("Oponent's turn: \n\n\n");
				console.append(fightText);
				attack(player, enemy);
				updateHealth(player, playerHPBar);
				playerTurn = playerTurn + 1;
			}
				
		}
		//Print death message
		if (globalAlly.getHp()<=0) {
			fightText = globalAlly.getName()+"'s health points have dropped to zero!\n\n\n";
			console.append(fightText);
			deathMenu();
		} else if (globalEnemy.getHp()<=0) {
			fightText = globalEnemy.getName()+"'s health points have dropped to zero!\n\n\n";
			console.append(fightText);
			deathMenu();
		} 
	}
	

	//Choosing who goes first
	public int getFirstTurn(Warrior player, Warrior enemy) {
		Random rn = new Random();
		
		if (player.getSpeed() < enemy.getSpeed())
			return 1;
		else if (player.getSpeed () > enemy.getSpeed())
			return 0;
		else if (player.getAgility () < enemy.getAgility())
			return 1;
		else if (player.getAgility() > enemy.getAgility())
			return 0;
		
		return rn.nextInt(2);
	}
	
	//Attack method
		public void attack(Warrior defender, Warrior attacker) {
			Random rn = new Random();
			int accuracy = rn.nextInt(100)+1;
			
			//Check if the attack connects
			if (attacker.getAgility()*10>accuracy) {
				int defense = rn.nextInt(50)+1;
				//Check if the defender dodges
				if (defender.getAgility()>defense) {
					fightText =("\t"+defender.getName()+" dodged the attack.\n\n");
					console.append(fightText);
				} else {
					//Calculate damage
					int attackerDamage = attacker.getStrength()-defender.getDefense();
					defender.setHp(defender.getHp()-attackerDamage);
					fightText =("\t"+defender.getName()+" received "+attackerDamage+" points of damage.\n\n");
					console.append(fightText);
				}
			} else {
				fightText = ("\t"+attacker.getName()+" missed the attack.\n\n");
				console.append(fightText);
			}
			
			//Check if the attacker attacks again
			if (attacker.getSpeed() > defender.getSpeed()) {
				if ((attacker.getSpeed() - defender.getSpeed())*10 > rn.nextInt(100)+1) {
					fightText = ("\tAdditional attack!\n\n");
					console.append(fightText);
					attack(defender, attacker);
				}
			}
			
		}
	
	// After combat method
public void deathMenu() {
    int option = JOptionPane.showOptionDialog(rootPane, "Do you want to keep fighting?", " ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[] { "Yes", "No" }, "Yes");
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, user, password);

        // SAVE BATTLE DATA
        if (globalEnemy.getHp() <= 0) {
            playerPoints += globalEnemy.getPoints() + globalEnemy.getWeapon().getPoints();
        }

        // CONTINUE? YES/NO
        // If YES
        if (option == 0) {
            PreparedStatement pst = con.prepareStatement("SELECT * FROM players");
            pst.executeQuery();

            // If player lost
            if (globalAlly.getHp() <= 0) {
                // Save ranking data
                String insert = "INSERT INTO ranking VALUES(?, ?, ?)";
                pst = con.prepareStatement(insert);
                pst.setInt(1, playerID);
                pst.setInt(2, playerPoints);
                pst.setInt(3, globalAlly.getId());
                pst.executeUpdate();

                // Reset player and enemy stats
                resetPlayerStats();
                resetEnemyStats();

                // Display image indicating no character chosen
                // Update weapon panel

                // Call new frame
                dispose();
                new Frame(false, null, false, 0, 0);

            // If player won
            } else {
                // Reset player and enemy stats
                resetPlayerStats();
                resetEnemyStats();

                // Call new frame with player info
                globalAlly.setHp(allyHP);
                dispose();
                new Frame(true, globalAlly, true, playerID, playerPoints);
            }

        // If NO
        } else {
            // Save ranking data
            String insert = "INSERT INTO ranking VALUES(?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(insert);
            pst.setInt(1, playerID);
            pst.setInt(2, playerPoints);
            pst.setInt(3, globalAlly.getId());
            pst.executeUpdate();

            // Exit
            super.dispose();
        }

    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    turnDecided = false;
}

// Método para resetear las estadísticas del jugador
private void resetPlayerStats() {
    // Reset player stats as needed
}

// Método para resetear las estadísticas del enemigo
private void resetEnemyStats() {
    // Reset enemy stats as needed
}


//HEALTH BAR METHODS
	
	//Generate health bar
	public JProgressBar healthBar(Warrior warrior) {
		healthBar = new JProgressBar(0,100);
		healthBar.setUI(new BasicProgressBarUI() {
			protected Color getSelectionForeground() {return Color.BLACK;}	
		});
		healthBar.setStringPainted(true);

		healthBar.setPreferredSize(new Dimension(210,20));
		healthBar.setForeground(Color.green);
		healthBar.setValue(100);
		return healthBar;		
	}
	
	//Update health bar
	public void updateHealth(Warrior warrior, JProgressBar healthBar) {
		healthBar.setValue(warrior.getHp()*100/warrior.getMaxHP());
	}
	
	//Set stats panel according to warrior stats	
	public JPanel populateStatsPanel(Warrior warrior) {
		JPanel superpanel = new JPanel(); 
		JPanel statsPanel = new JPanel();
		BufferedImage strengthIcon = null, dexterityIcon = null, speedIcon = null, defenseIcon=null;

		try {
			strengthIcon = ImageIO.read(new File("Images/StatsImages/strength.png"));
			dexterityIcon = ImageIO.read(new File("Images/StatsImages/dexterity.png"));
			speedIcon = ImageIO.read(new File("Images/StatsImages/speed.png"));
			defenseIcon = ImageIO.read(new File("Images/StatsImages/defense.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JPanel  strengthIconPanel = new ImageComponent(strengthIcon);
		JPanel  dexterityIconPanel = new ImageComponent(dexterityIcon);
		JPanel  speedIconPanel = new ImageComponent(speedIcon);
		JPanel  defenseIconPanel = new ImageComponent(defenseIcon);

		statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.PAGE_AXIS));
		
		strengthBar = new JProgressBar(0,11);
		strengthBar.setPreferredSize(new Dimension(180,15));
		strengthBar.setForeground(Color.red);
		strengthBar.setValue(warrior.getStrength()+warrior.getWeapon().getAttack());
		
		dexterityBar = new JProgressBar(0,11);
		dexterityBar.setPreferredSize(new Dimension(180,15));
		dexterityBar.setForeground(Color.magenta);
		dexterityBar.setValue(warrior.getAgility());
		
		speedBar = new JProgressBar(0,11);
		speedBar.setPreferredSize(new Dimension(180,15));
		speedBar.setForeground(Color.yellow);
		speedBar.setValue(warrior.getSpeed()+warrior.getWeapon().getSpeed());
		
		defenseBar = new JProgressBar(0,11);
		defenseBar.setPreferredSize(new Dimension(180,15));
		defenseBar.setForeground(Color.blue);
		defenseBar.setValue(warrior.getDefense());
		

		JPanel iconPanel = new JPanel();
		strengthIconPanel.setPreferredSize(new Dimension(15,15));
		iconPanel.add(strengthIconPanel);
		dexterityIconPanel.setPreferredSize(new Dimension(15,15));

		statsPanel.add(strengthBar);
		iconPanel.add(dexterityIconPanel);
		statsPanel.add(dexterityBar);
		speedIconPanel.setPreferredSize(new Dimension(15,15));

		iconPanel.add(speedIconPanel);
		statsPanel.add(speedBar);
		defenseIconPanel.setPreferredSize(new Dimension(15,15));

		iconPanel.add(defenseIconPanel);
		statsPanel.add(defenseBar);
		iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.PAGE_AXIS));

		superpanel.setSize(600,600);
		superpanel.add(iconPanel);
		superpanel.add(statsPanel);

		JPanel weapon = new JPanel();
		BufferedImage object = warrior.getWeapon().getImage();
		JPanel objectPanel = new ImageComponent(object);
		objectPanel.setPreferredSize(new Dimension(50,50));
		weapon.add(objectPanel);
		superpanel.add(weapon,BorderLayout.EAST);
		return superpanel;
	}

	//Generate random enemy
	public Warrior enemyGeneration() {
		
		Warrior enemy = null;
		Weapon enemyWeapon= new Weapon();
		int randomEnemy=(int)(Math.random()*3);
		boolean loop=true;
		Warrior model=warriors.get(randomEnemy);
		if (model.getRace().compareToIgnoreCase("Human")==0) {
			enemy = new Human(model.getId(),model.getName(),model.getRace(), model.getImage(), model.getHp(), model.getStrength(),model.getDefense(),model.getAgility(),model.getSpeed(),model.getWeapon(),model.getPoints());
		}else if (model.getRace().compareToIgnoreCase("Dwarf")==0) {
			enemy = new Dwarf(model.getId(),model.getName(),model.getRace(), model.getImage(), model.getHp(), model.getStrength(),model.getDefense(),model.getAgility(),model.getSpeed(),model.getWeapon(),model.getPoints());
		}else if (model.getRace().compareToIgnoreCase("Elf")==0) {
			enemy = new Elf(model.getId(),model.getName(),model.getRace(), model.getImage(), model.getHp(), model.getStrength(),model.getDefense(),model.getAgility(),model.getSpeed(),model.getWeapon(),model.getPoints());
		}
	
		while (loop){
			int randomWeapon=(int)(Math.random()*9);
			enemyWeapon=weapons.get(randomWeapon);
			if (enemyWeapon.getRace().contains(enemy.getRace())) {		
				enemy.setWeapon(enemyWeapon);
				loop=false;
				
			}
		}
		return enemy;
}
	
//Personalized button class to store id info	
class Button extends JButton{
	private int id = 0;
	//Constructor
	public Button(ImageIcon nombre, int id) {
		super(nombre);
		this.id=id;
	}
	//Getter and Setter for id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	}
}