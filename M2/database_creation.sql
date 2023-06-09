CREATE DATABASE IF NOT EXISTS races;


use races;

CREATE TABLE weapons (weapon_id integer not null auto_increment primary key,weapon_name varchar(20),weapon_image_path varchar(200),weapon_race varchar(20),attack_modifier integer,speed_modifier integer,weapon_points integer);

INSERT INTO weapons VALUES (default,'dagger','Images/WeaponImages/dagger.png','human,elf',0,3,10);
INSERT INTO weapons VALUES (default,'sword','Images/WeaponImages/sword.png','human,elf,dwarf',1,1,10);
INSERT INTO weapons VALUES (default,'axe','Images/WeaponImages/axe.png','human,dwarf',3,0,10);
INSERT INTO weapons VALUES (default,'twin swords','Images/WeaponImages/twin_swords.png','human,elf',2,2,14);
INSERT INTO weapons VALUES (default,'scimitar','Images/WeaponImages/scimitar.png','human,elf',2,1,14);
INSERT INTO weapons VALUES (default,'bow','Images/WeaponImages/bow.png','elf',5,1,15);
INSERT INTO weapons VALUES (default,'katana','Images/WeaponImages/katana.png','human',3,2,18);
INSERT INTO weapons VALUES (default,'dirk','Images/WeaponImages/dirk.png','human,elf,dwarf',0,4,12);
INSERT INTO weapons VALUES (default,'battleaxe','Images/WeaponImages/battleaxe.png','dwarf',5,0,20);


CREATE TABLE races (race_id integer not null auto_increment primary key,race_name varchar(5),base_hp integer,base_strength integer,base_defense integer,base_dexterity integer,base_speed integer,points integer);

INSERT INTO races VALUES (default,'dwarf', 60, 6, 4, 5, 3, 21);
INSERT INTO races VALUES (default,'human', 50, 5, 3, 6, 5, 20);
INSERT INTO races VALUES (default,'elf', 40, 4, 2, 7, 7, 19);


CREATE TABLE warriors (warrior_id integer not null auto_increment primary key,warrior_name varchar(30),warrior_image_path varchar(200),warrior_race integer, foreign key(warrior_race) references races(race_id));

INSERT INTO warriors VALUES (default,'Gimli','Images/CharacterImages/dwarf.png',1);
INSERT INTO warriors VALUES (default,'Aragorn','Images/CharacterImages/human.png',2);
INSERT INTO warriors VALUES (default,'Legolas','Images/CharacterImages/elf.png',3);


CREATE TABLE players (player_id integer not null auto_increment primary key,player_name varchar(20));

CREATE TABLE battle (battle_id integer not null auto_increment primary key,player_id integer,warrior_id integer,warrior_weapon_id integer,opponent_id integer,opponent_weapon_id integer,damage_caused integer,damage_recieved integer,battle_points integer, foreign key (player_id) references players(player_id), foreign key (warrior_id) references warriors(warrior_id), foreign key(warrior_weapon_id) references weapons(weapon_id),foreign key(opponent_id) references warriors(warrior_id), foreign key(opponent_weapon_id) references weapons(weapon_id));

CREATE TABLE ranking (player_id integer primary key,player_points integer, warrior_id integer, foreign key (player_id) references players(player_id), foreign key(warrior_id) references warriors(warrior_id));

CREATE USER 'user'@'localhost' IDENTIFIED BY '';
GRANT ALL PRIVILEGES ON * . * TO 'user'@'localhost'; 

