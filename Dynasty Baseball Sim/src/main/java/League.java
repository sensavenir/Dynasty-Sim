

import java.util.ArrayList;

public class League {
	private String name;
	private int salaryCap;
	private boolean scouting;
	private boolean ruleV;
	private boolean IFA;
	private String draftType;
	private int ID;
	private ArrayList<Team> teams = new ArrayList<Team>();
	private ArrayList<Player> faHitters = new ArrayList<Player>();
	private ArrayList<Pitcher> faPitchers = new ArrayList<Pitcher>();
	
	public Team getTeam(int index) {
		return teams.get(index);
	}
	
	public League(String name, int cap, boolean scout, boolean rule, String draft, int size, String fas, boolean ifas) {
		this.name = name;
		this.salaryCap = cap;
		this.scouting = scout;
		this.ruleV = rule;
		this.draftType = draft;
		this.IFA = IFA;
		for(int i = 0; i < size; i++) {
			Team newTeam = new Team(this.ID);
			teams.add(newTeam);
			System.out.println(newTeam.getName());
		}
		int f = 0;
		if(fas.equals("many")) {
			f = 6;
		}
		else if(fas.equals("average")) {
			f = 4;
		}
		else {
			f = 2;
		}
		for(int i = 0; i < f*12; i++) {
			Pitcher tempPitcher = new Pitcher();
			faPitchers.add(tempPitcher);
		}
		for(int i = 0; i < f*3; i++) {
			Player tempCatcher = new Player("C");
			faHitters.add(tempCatcher);
			Player tempFirst = new Player("1B");
			faHitters.add(tempFirst);
			Player tempSecond = new Player("2B");
			faHitters.add(tempSecond);
			Player tempSS = new Player("SS");
			faHitters.add(tempSS);
			Player tempThird = new Player("3B");
			faHitters.add(tempThird);
			Player tempLeft = new Player("LF");
			faHitters.add(tempLeft);
			Player tempCenter = new Player("CF");
			faHitters.add(tempCenter);
			Player tempRight = new Player("RF");
			faHitters.add(tempRight);
		}		
	}
}
