    package Characters;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class WarriorContainer {
	private String url="jdbc:mysql://localhost/races?serverTimezone=UTC",user="root",password="P@ssw0rd";
    private String query="select w.*,r.* from warriors w join races r on w.warrior_race = r.race_id";
    private ArrayList<Warrior> warriors= new ArrayList<Warrior>();


    public ArrayList<Warrior> getWarriors() {
        return warriors;
    }
    
    public Warrior getWarrior(int id) {
    	return warriors.get(id);
    }

    public void setWarriors(ArrayList<Warrior> warriors) {
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
            //Instantiating warriors from database
            while(rs.next()) {
            	Warrior w = null;
            	if (rs.getString(6).compareToIgnoreCase("Human")==0){
            		w = new Human(rs.getInt(1),rs.getString(2),rs.getString(6),rs.getString(3),rs.getInt(7),rs.getInt(8),rs.getInt(9),rs.getInt(10),rs.getInt(11),rs.getInt(12));
            	}else if (rs.getString(6).compareToIgnoreCase("Dwarf")==0) {
            		w = new Dwarf(rs.getInt(1),rs.getString(2),rs.getString(6),rs.getString(3),rs.getInt(7),rs.getInt(8),rs.getInt(9),rs.getInt(10),rs.getInt(11),rs.getInt(12));
            	}else if (rs.getString(6).compareToIgnoreCase("Elf")==0) {
            		w = new Elf(rs.getInt(1),rs.getString(2),rs.getString(6),rs.getString(3),rs.getInt(7),rs.getInt(8),rs.getInt(9),rs.getInt(10),rs.getInt(11),rs.getInt(12));

            	}
            	warriors.add(w);
            	
            }
           
        }catch (SQLException e) {
        	e.printStackTrace();
            System.out.println("Could not establish a connection");
        }
        this.warriors = warriors;
    }
}
