package Characters;

import java.awt.image.BufferedImage;

import Weapons.Weapon;

public class Elf extends Warrior{
	public Elf() {
		super();
	}
	public Elf(int id, String name,String race, BufferedImage image , int hp, int strength, int defense, int agility, int speed, int points ) {
		super( id, name, race, image, hp, strength,defense,agility ,speed, points);
		
	}
	public Elf(int id, String name,String race, String image , int hp, int strength, int defense, int agility, int speed, int points ) {
		super( id, name, race, image, hp, strength,defense,agility ,speed, points);
		
	}
	public Elf(int id, String name,String race, String image , int hp, int strength, int defense, int agility, int speed, Weapon object,int points ) {
		super(id, name, race, image, hp, strength, defense, agility, speed, object, points);
		
	}
	public Elf(int id, String name,String race, BufferedImage image , int hp, int strength, int defense, int agility, int speed, Weapon object,int points ) {
		super(id, name, race, image, hp, strength, defense, agility, speed, object, points);

		
	}
}
