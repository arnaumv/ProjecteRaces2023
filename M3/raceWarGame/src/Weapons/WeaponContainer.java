package Weapons;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class WeaponContainer {
    private String url="jdbc:mysql://localhost/races?serverTimezone=UTC",user="root",password="P@ssw0rd";
    private String query="select * from weapons";
    private ArrayList<Weapon> weapons= new ArrayList<Weapon>();


    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(ArrayList<Weapon> weapons) {
        //Database connection
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot load driver");
        }

            try {
                Connection con=DriverManager.getConnection(url, user, password);
                //STATEMENT
                Statement st=con.createStatement();
                ResultSet rs=st.executeQuery(query);

                //Instantiating new weapons from database
                while(rs.next()) {
                    Weapon w=new Weapon();
                    w.setId(rs.getInt(1));
                    w.setName(rs.getString(2));
                    w.setImage(rs.getString(3));
                    w.setRace(rs.getString(4));
                    w.setAttack(rs.getInt(5));
                    w.setSpeed(rs.getInt(6));
                    w.setPoints(rs.getInt(7));
                    weapons.add(w);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Could not establish a connection");
            }



        this.weapons = weapons;




    }
}