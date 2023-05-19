package Weapons;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Weapon {
	
	
	@Override
	public String toString() {
		return "Weapon [id=" + id + ", name=" + name + ", Image=" + image + ", race=" + race + ", attack=" + attack
				+ ", speed=" + speed + ", points=" + points + "]";
	}
	
	//Weapon attributes
	private int id;
	private String name;
	private BufferedImage image;
	private String race;
	private int attack;
	private int speed;
	private int points;
	
	//Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(String urlPhoto) {
		 try {
			image = ImageIO.read(new File(urlPhoto));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void setImage(BufferedImage image) {
		this.image=image;
	}
}

	