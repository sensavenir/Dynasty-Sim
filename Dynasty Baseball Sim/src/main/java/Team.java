

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Team {
	private String name;
	private int ID;
	private int leagueID;
	private Integer[] stadium = {330, 410, 330, 8, 8, 8};
	private ArrayList<Player> hitters = new ArrayList<Player>();
	private ArrayList<Pitcher> pitchers = new ArrayList<Pitcher>();
	private int starter = 0;
	private String[] allocations = new String[4];
	private ArrayList<Player> rhpLineup = new ArrayList<Player>();
	private ArrayList<Player> lhpLineup = new ArrayList<Player>();
	private ArrayList<Pitcher> rotation = new ArrayList<Pitcher>();
	private ArrayList<Pitcher> bullpen = new ArrayList<Pitcher>();
	private int stealing = 5;
	private int baserunning = 5;
	
	public Pitcher getStarter() {
		Pitcher tempPitcher = rotation.get(starter);
		starter++;
		if(starter == 5) {
			starter = 0;
		}
		return tempPitcher;
	}
	
	public Player getHitter(String name) {
		Player tempPlayer = new Player("SS");
		for(int i = 0; i < hitters.size(); i++) {
			if(name.equals(hitters.get(i).getName())) {
				tempPlayer = hitters.get(i);
			}
		}
		return tempPlayer;
	}
	
	public ArrayList<Player> getHitters() {
		return hitters;
	}
	
	public ArrayList<Pitcher> getPitchers() {
		return pitchers;
	}
	
	public ArrayList<Player> getRHP() {
		return this.rhpLineup;
	}
	
	public ArrayList<Player> getLHP() {
		return this.lhpLineup;
	}	
	
	public int getStealing() {
		return this.stealing;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Integer[] getStadium() {
		return this.stadium;
	}
	
	public void setStadium(Integer[] dimensions) {
		stadium = dimensions.clone();
	}
	
	public Team(int id) {
		this.leagueID = id;
		ArrayList<String> placeNames = new ArrayList<String>();
		Random r = new Random();
		try { 
			InputStream is = getClass().getClassLoader().getResourceAsStream("places.txt");
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			while (in.ready()) { 
				placeNames.add(in.readLine());
			}
			in.close();
			String firstName = placeNames.get(r.nextInt(placeNames.size()));
			
			ArrayList<String> teamNames = new ArrayList<String>();
			InputStream isto = getClass().getClassLoader().getResourceAsStream("teams.txt");
			BufferedReader into = new BufferedReader(new InputStreamReader(isto));
			while (into.ready()) { 
				teamNames.add(into.readLine());
			}
			into.close();
			String lastName = teamNames.get(r.nextInt(teamNames.size()));
			this.name = firstName + " " + lastName;
			System.out.println(name);
		}
		catch (FileNotFoundException ex){
			System.out.println(ex);
		}
		catch (IOException ex){
			System.out.println(ex);
		}		
		for(int i = 0; i < 11; i++) {
			Pitcher tempPitcher = new Pitcher();
			pitchers.add(tempPitcher);
			if(i < 5) {
				rotation.add(tempPitcher);				
			}
			else {
				bullpen.add(tempPitcher);
			}
		}
		String[] positions = {"C", "1B", "2B", "SS", "3B", "LF", "CF", "RF"};
		for(int i = 0; i < positions.length; i++) {
			Player tempPlayer = new Player(positions[i]);
			hitters.add(tempPlayer);
			rhpLineup.add(tempPlayer);
			lhpLineup.add(tempPlayer);
		}
		Player tempCatcher2 = new Player("C");
		hitters.add(tempCatcher2);
		Player tempFirst = new Player("DH");
		hitters.add(tempFirst);
		rhpLineup.add(tempFirst);
		lhpLineup.add(tempFirst);
		Player tempSecond = new Player("2B");
		hitters.add(tempSecond);
		Player tempSS = new Player("SS");
		hitters.add(tempSS);
		Player tempThird = new Player("3B");
		hitters.add(tempThird);
		Player tempLeft = new Player("LF");
		hitters.add(tempLeft);
		Player tempCenter = new Player("CF");
		hitters.add(tempCenter);
		Player tempRight = new Player("RF");
		hitters.add(tempRight);
	}
}
