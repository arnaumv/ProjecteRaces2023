package Characters;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Weapons.Weapon;

public abstract class Warrior{
	private int id;
	private String name;
	private String race;
    private BufferedImage image;
	private int hp;
	private int maxHP;
	private int strength;
	private int defense;
	private int agility;
	private int speed;
	private Weapon object;
	private int points;
	
	
	//Constructors
	public Warrior() {

	}
	public Warrior(int id, String name,String race, BufferedImage image , int hp, int strength, int defense, int agility, int speed, int points ) {
		this.id = id;
        this.name=name;
        this.race=race;
        this.image=image;
        this.hp=hp;
        setMaxHP(hp);
        this.strength=strength;
        this.defense=defense;
        this.agility=agility;
        this.speed=speed;
        this.points=points;
		
	}
	public Warrior(int id, String name,String race, String image , int hp, int strength, int defense, int agility, int speed, int points ) {
		this.id = id;
        this.name=name;
        this.race=race;
        setImage(image);
        this.hp=hp;
        setMaxHP(hp);
        this.strength=strength;
        this.defense=defense;
        this.agility=agility;
        this.speed=speed;
        this.points=points;
		
	}
	public Warrior(int id, String name,String race, String image , int hp, int strength, int defense, int agility, int speed, Weapon object,int points ) {
		this.id = id;
        this.name=name;
        this.race=race;
        setImage(image);
        this.hp=hp;
        setMaxHP(hp);
        this.strength=strength;
        this.defense=defense;
        this.agility=agility;
        this.speed=speed;
        this.object=object;
        this.points=points;
		
	}
	public Warrior(int id, String name,String race, BufferedImage image , int hp, int strength, int defense, int agility, int speed, Weapon object,int points ) {
		this.id = id;
        this.name=name;
        this.race=race;
        this.image=image;
        this.hp=hp;
        setMaxHP(hp);
        this.strength=strength;
        this.defense=defense;
        this.agility=agility;
        this.speed=speed;
        this.object=object;
        this.points=points;
		
	}
	
	//Getters and Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id=id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name=name;
	}
	public String getRace() {
		return race;
	}
	public void setRace(String race) {
		this.race=race;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(String urlPhoto) {
		 try {
			BufferedImage image = ImageIO.read(new File(urlPhoto));
			this.image = image;
		 } catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setImage(BufferedImage image) {
		this.image=image;
	}
	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getMaxHP() {
		return maxHP;
	}
	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}
	
	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public int getAgility() {
		return agility;
	}

	public void setAgility(int agility) {
		this.agility = agility;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public void setWeapon(Weapon object) {
		this.object=object;
	}

	public Weapon getWeapon() {
		return object;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points=points;
	}
	
	@Override
	public String toString() {
		return	"[name= "+name+", race="+race+", hp=" + hp + ", strength=" + strength + ", defense=" + defense + ", agility=" + agility
				+ ", speed=" + speed + ", points="+points+"] "+object;
	}
    public void setCharacterImage(Object object2) {
    }
	
	
	
	
}