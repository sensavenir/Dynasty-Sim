import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;

public class Game {
	private int id;
	private int outs = 0;	
	private boolean top = true;
	int lineupCount1 = 1;
	int lineupCount2 = 1;
	private Pitcher pitcher1;
	private Pitcher pitcher2;
	private HashMap<String, Player> lineup1 = new HashMap<String, Player>();
	private HashMap<String, Player> lineup2 = new HashMap<String, Player>();
	private String[] first = new String[2];
	private String[] second = new String[2];
	private String[] third = new String[2];
	private int strikes = 0;
	private int balls = 0;
	HashMap<String, Integer[]> positioning = new HashMap<String, Integer[]>();
	String[] fourSeamer = {"4-seamer", "35", "21", "34", "10"};
	String[] twoSeamer = {"2-seamer", "46", "19", "27", "8"};
	String[] Cutter = {"Cutter", "44", "20", "27", "9"};
	String[] Sinker = {"Sinker", "59 ", "17", "19", "5"};
	String[] Slider = {"Slider", "45" , "17" ,"29", "9"};
	String[] Curveball = {"Curveball", "50", "19", "25", "6"};
	String[] Changeup = {"Changeup", "49" , "18" , "26" ,"7"}; 
	String[] Splitter = {"Splitter", "53" , "17" , "24" ,"6"};
	String[] Knucklecurve = {"Screwball", "56" , "15" , "26" ,"4"};
	String[] Knuckleball = {"Knuckleball", "43" , "17" , "30" ,"10"}; 

	//generate batted ball outcome
	private BattedBall outCome(String pitch, Integer[] location, Pitcher pitcher, Player hitter) {
		int batSpeed = normalizeRating(hitter.getPower());
		int swing = normalizeRating(hitter.getSwing());
		int contact = normalizeRating(hitter.getContact());
		int velocity = normalizeRating(pitcher1.getVelocity());
		int movement = normalizeRating(pitcher1.getMovement());		
		//roll for exit velocity, batted ball type and azimuth
		Random d = new Random();			
		double exitVelocity = d.nextGaussian()*7 + (scale(batSpeed, 1, 60, 81, 97)) + (scale(contact, 1, 60, 0, 5)) - (scale(movement, 1, 60, 0, 5));
		double azimuth = 0;
		String bbType = "";
		int bb = (int) Math.round(Math.random()*100);
		String[][] bbPitch = new String[9][];
		bbPitch[0] = fourSeamer.clone();
		bbPitch[1] = twoSeamer.clone();
		bbPitch[2] = Cutter.clone();
		bbPitch[3] = Sinker.clone();
		bbPitch[4] = Slider.clone();
		bbPitch[5] = Curveball.clone();
		bbPitch[6] = Changeup.clone();
		bbPitch[7] = Splitter.clone();
		bbPitch[8] = Knuckleball.clone();
		bbPitch[9] = Knucklecurve.clone();
		//batted ball
		int launchAngle = 0;
		for(int i = 0; i < 10; i++) {
			if(pitch.equals(bbPitch[i][0])) {				
				double count = 99 - Integer.parseInt(bbPitch[i][1]) + (location[1] - 13) + (scale(swing, 1, 60, 0, 26) - 13);				
				if(bb > count) {
					bbType = "Groundball";
					launchAngle = (int) Math.round((Math.random()*50)-45);
					break;
				}					
				count = count - Integer.parseInt(bbPitch[i][2]);	
				if(bb > count) {
					bbType = "Liner";
					launchAngle = (int) Math.round(Math.random()*20)+5;
					break;
				}
				count = count - Integer.parseInt(bbPitch[i][3]) - (int) Math.round((((double) location[1] - 13)*0.8)) - (int) Math.round(((scale(swing, 1, 60, 0, 26) - 13)*0.8));
				if(bb > count) {
					bbType = "Flyball";
					launchAngle = (int) Math.round(Math.random()*25)+25;
					break;
				}
				if(bb > count) {
					bbType = "Popup";
					launchAngle = (int) Math.round(Math.random()*20)+80;
					if(launchAngle == 90) {
						launchAngle = 91;
					}
					break;
				}
			}				
		}
		//azimuth
		int a = (int) Math.round(Math.random()*100);
		if(hitter.getHand().equals("L") || (hitter.getHand().equals("SL") && pitcher.getHand().equals("R"))) {
			if(a > 49 + (hitter.getSplit()) - (scale(batSpeed, 1, 60, 0, 12) - 6) + (scale(velocity, 1, 60, 0, 12) - 6)) {
				azimuth = scale(a, 0, 100, 0, 15);
			}
			else {
				azimuth = scale(a, 0, 100, 16, 30);
			}
		}
		else {
			if(a > 49 + (hitter.getSplit()) + (scale(batSpeed, 1, 60, 0, 12) - 6) - (scale(velocity, 1, 60, 0, 12) - 6)) {
				azimuth = scale(a, 0, 100, 0, 15);
			}
			else {
				azimuth = scale(a, 0, 100, 16, 30);
			}
		}
		//distance & time
		double distance = 0;
		if(launchAngle > 89) {
			distance = ((Math.pow(exitVelocity-(Math.pow(exitVelocity/22, 2)), 2) * Math.sin(2*(180-launchAngle)))/21.9)*1.4667;
		}
		else if(launchAngle > 0) {
			distance = ((Math.pow(exitVelocity-(Math.pow(exitVelocity/22, 2)), 2) * Math.sin(2*launchAngle))/21.9)*1.4667;
		}
		else {
			distance = ((Math.pow(exitVelocity-(Math.pow(exitVelocity/22, 2)), 2) * Math.sin(2*Math.abs(launchAngle)))/21.9)*1.4667;
		}
		double time = distance/(exitVelocity*Math.cos(distance));
		//foul
		double foulThresh = 31 + (scale(velocity, 1, 60, 0, 10) - 5);
		if(pitch.equals("4-seamer")) {
			foulThresh = foulThresh + 13 - (scale(batSpeed, 1, 60, 0, 20) - 10);
		}
		else if(pitch.equals("2-seamer") || pitch.equals("Cutter") || pitch.equals("Slider")) {
			foulThresh = foulThresh + 8 - (scale(batSpeed, 1, 60, 0, 16) - 8);
		}
		int f = (int) Math.round(Math.random()*100);
		boolean foul = false;
		if(f <= foulThresh) { 
			foul = true;
		}
		BattedBall battedBall = new BattedBall(bbType, exitVelocity, azimuth, distance, time, launchAngle, foul);
		return battedBall;
	}
	
	
	//calculate defensive outcome
	private boolean routeCalc(String type, double distance, double azimuth, double time, Player defender) {
		boolean caught = false;
		Integer[] firstPos = {110, 34};
		Integer[] secondPos = {151, 11};
		Integer[] thirdPos = {118, 29};
		Integer[] shortPos = {145, 11};
		Integer[] leftPos = {298, 27};
		Integer[] centerPos = {322, 0};
		Integer[] rightPos = {294, 27};
		Integer[] cPos = {5, 0};
		positioning.put("1B", firstPos);
		positioning.put("2B", secondPos);
		positioning.put("3B", thirdPos);
		positioning.put("SS", shortPos);
		positioning.put("LF", leftPos);
		positioning.put("CF", centerPos);
		positioning.put("RF", rightPos);
		positioning.put("C", cPos);
		Random r = new Random();
		double xBall = distance*Math.sin(azimuth);
		double yBall = distance*Math.cos(azimuth);
		Integer[] pos = positioning.get(defender.getPosition());
		double xDef = pos[0]*Math.sin(pos[1]);
		double yDef = pos[0]*Math.cos(pos[1]);
		double ground2Cover = Math.sqrt(Math.pow(xBall - xDef, 2) + Math.pow(yBall - yDef, 2));
		double defTime = (r.nextGaussian()*0.1 + scale(normalizeRating(100-defender.getReaction()), 1, 60, 0.5, 2)) 
				       + (ground2Cover/(r.nextGaussian()*0.67 + (scale(normalizeRating(defender.getSpeed()), 1, 60, 14, 20))));
		if(time >= defTime) {
			caught = true;
		}
		return caught;
	}
	
	//generate foul outcome
	private boolean foulOutcome(BattedBall bb, HashMap<String, Player> lineup, Team team) {
		boolean caught = false;
		Player defender = new Player("random");
		if(bb.getType().equals("Flyball")) {			
			if(bb.getAzimuth() < 15) {
				lineup.get("LF");				
				caught = routeCalc(bb.getType(), bb.getDistance(), 55, bb.getTime(), defender);
			}
			else {
				lineup.get("RF");			
				caught = routeCalc(bb.getType(), bb.getDistance(), 55, bb.getTime(), defender);
			}
		}	
		if(bb.getType().equals("Popup")) {
			if(bb.getLaunch() < 90) {
				if(bb.getAzimuth() < 15) {
					lineup.get("3B");
					caught = routeCalc(bb.getType(), bb.getDistance(), 55, bb.getTime(), defender);
				}
				else {
					lineup.get("1B");
					caught = routeCalc(bb.getType(), bb.getDistance(), 55, bb.getTime(), defender);
				}
			}
			else {
				lineup.get("C");
				caught = routeCalc(bb.getType(), bb.getDistance(), bb.getAzimuth(), bb.getTime(), defender);
			}
		}
		else if(bb.getType().equals("Flyball")) {
			if(bb.getAzimuth() < 15) {
				lineup.get("LF");
				caught = routeCalc(bb.getType(), bb.getDistance(), 55, bb.getTime(), defender);
			}
			else {
				lineup.get("RF");
				caught = routeCalc(bb.getType(), bb.getDistance(), 55, bb.getTime(), defender);
			}
		}
		return caught;
	}

	//generate hit outcome
	private String[] hitOutcome(BattedBall bb, HashMap<String, Player> lineup, Player batter) {
		String[] hit = new String[2];
		boolean caught = false;
		boolean error = false;
		Random r = new Random();
		Player defender = new Player("x");			
		if(bb.getType().equals("Groundball")) {
			if(bb.getAzimuth() < 2) {
				defender = lineup.get("3B");
				int defense = normalizeRating(defender.getReaction()) + normalizeRating(defender.getSpeed());
				if(r.nextInt(1000) > 500 - defense) {
					caught = true;							
				}
			}
			else if(bb.getAzimuth() < 5) {
				defender = lineup.get("3B");
				int defense = normalizeRating(defender.getReaction()) + normalizeRating(defender.getSpeed());
				if(r.nextInt(1000) > 150 - defense) {
					caught = true;
				}					
			}
			else if(bb.getAzimuth() < 8) {
				defender = lineup.get("3B");
				int defense = normalizeRating(defender.getReaction()) + normalizeRating(defender.getSpeed());
				if(r.nextInt(1000) > 150 - defense) {
					caught = true;
				}					
				if(caught == false) {
					defender = lineup.get("SS");
					defense = normalizeRating(defender.getReaction()) + normalizeRating(defender.getSpeed());
					if(r.nextInt(1000) > 150 - defense) {
						caught = true;
					}						
				}
			}
			else if (bb.getAzimuth() < 12) {
				defender = lineup.get("SS");
				int defense = normalizeRating(defender.getReaction()) + normalizeRating(defender.getSpeed());
				if(r.nextInt(1000) > 120 - defense) {
					caught = true;
				}
			}
			else if (bb.getAzimuth() < 18) {
				defender = lineup.get("SS");
				int defense = normalizeRating(defender.getReaction()) + normalizeRating(defender.getSpeed());
				if(r.nextInt(1000) > 800 - defense) {
					caught = true;
				}
				if(caught == false) {
					defender = lineup.get("2B");
					defense = normalizeRating(defender.getReaction()) + normalizeRating(defender.getSpeed());
					if(r.nextInt(1000) > 800 - defense) {
						caught = true;
					}						
				}
			}
			else if (bb.getAzimuth() < 23) {
				defender = lineup.get("2B");
				int defense = normalizeRating(defender.getReaction()) + normalizeRating(defender.getSpeed());
				if(r.nextInt(1000) > 120 - defense) {
					caught = true;
				}
			}		
			else if (bb.getAzimuth() < 26) {
				defender = lineup.get("2B");
				int defense = normalizeRating(defender.getReaction()) + normalizeRating(defender.getSpeed());
				if(r.nextInt(1000) > 633 - defense) {
					caught = true;
				}
				if(caught == false) {
					defender = lineup.get("1B");
					defense = normalizeRating(defender.getReaction()) + normalizeRating(defender.getSpeed());
					if(r.nextInt(1000) > 633 - defense) {
						caught = true;
					}						
				}
			}
			else if (bb.getAzimuth() < 29) {
				defender = lineup.get("1B");
				int defense = normalizeRating(defender.getReaction()) + normalizeRating(defender.getSpeed());
				if(r.nextInt(1000) > 230 - defense) {
					caught = true;
				}
			}
			else {
				defender = lineup.get("1B");
				int defense = normalizeRating(defender.getReaction()) + normalizeRating(defender.getSpeed());
				if(r.nextInt(1000) > 450 - defense) {
					caught = true;
				}
			}
		}
		else if(bb.getType().equals("Flyball") || bb.getType().equals("Flyball")) {
			if(bb.getAzimuth() < 8) {
				defender = lineup.get("LF");
				caught = routeCalc(bb.getType(), bb.getDistance(), 55, bb.getTime(), defender);
			}
			else if(bb.getAzimuth() < 16) {
				defender = lineup.get("CF");
				caught = routeCalc(bb.getType(), bb.getDistance(), 55, bb.getTime(), defender);
				if(caught == false) {{
					defender = lineup.get("LF");
					caught = routeCalc(bb.getType(), bb.getDistance(), 55, bb.getTime(), defender);
				}
				}	
				else if(bb.getAzimuth() < 24) {
					defender = lineup.get("CF");
				}
			}
			caught = routeCalc(bb.getType(), bb.getDistance(), 55, bb.getTime(), defender);
			if(caught == false) {
				defender = lineup.get("RF");
				caught = routeCalc(bb.getType(), bb.getDistance(), 55, bb.getTime(), defender);
			}
		}
		else {
			defender = lineup.get("RF");
			caught = routeCalc(bb.getType(), bb.getDistance(), 55, bb.getTime(), defender);		
		}
		if(caught == false) {
			if(bb.getType().equals("Groundball")) {
				hit[0] = "1B";
				hit[1] = defender.getPosition();
			}
			else if(bb.getType().equals("Flyball")) {
				if(bb.getDistance() > positioning.get(defender.getPosition())[0]) {
					if(r.nextInt(100) > 80 + scale(normalizeRating(defender.getArm()), 1, 60, 0, 9) + scale(normalizeRating(batter.getSpeed()), 1, 60, 0, 10)) {
						hit[0] = "3B";
						hit[1] = defender.getPosition();
					}
					else {
						hit[0] = "2B";
						hit[1] = defender.getPosition();
					}
				}
				else {
					if(r.nextInt(100) > 70 + scale(normalizeRating(defender.getArm()), 1, 60, 0, 9) + scale(normalizeRating(batter.getSpeed()), 1, 60, 0, 10)) {
						hit[0] = "2B";
						hit[1] = defender.getPosition();
					}
					else {
						hit[0] = "1B";
						hit[1] = defender.getPosition();
					}
				}
			}
			else {
				if(bb.getDistance() > positioning.get(defender.getPosition())[0]) {
					if(r.nextInt(100) > 80 + scale(normalizeRating(defender.getArm()), 1, 60, 0, 9) + scale(normalizeRating(batter.getSpeed()), 1, 60, 0, 10)) {
						hit[0] = "3B";
						hit[1] = defender.getPosition();
					}
					else {
						hit[0] = "2B";
						hit[1] = defender.getPosition();
					}
				}
				else {
					if(r.nextInt(100) > 50 + scale(normalizeRating(defender.getArm()), 1, 60, 0, 9) + scale(normalizeRating(batter.getSpeed()), 1, 60, 0, 10)) {
						hit[0] = "2B";
						hit[1] = defender.getPosition();
					}
					else {
						hit[0] = "1B";
						hit[1] = defender.getPosition();
					}
				}
			}
		}
		else {
			hit[0] = "Caught";
			hit[1] = defender.getPosition();
		}
		return hit;
	}

	//get semi-random pitch location
	private Integer[] getLocation(String pitch) {
		Integer[] location = new Integer[2];
		if(pitch.equals("4-seamer") || pitch.equals("2-seamer") || pitch.equals("Cutter") || pitch.equals("Slider") || pitch.equals("Curveball")) {
			location[0] = (int) Math.round(Math.random()*20);
			int y = (int) Math.round(Math.random()*26);			
			if(location[0] >= 8 && location[0] <= 12) {
				if(y < 10 || y > 16) {
					location[1] = y;
				}
				else if(y > 9 && y < 17) {
					location[1] = 9;
				}
				else {
					location[1] = 17;
				}
			}
			else {
				location[1] = y;
			}
		}
		else if(pitch.equals("Sinker") || pitch.equals("Splitter") || pitch.equals("Changeup")) {			
			location[0] = (int) Math.round(Math.random()*10);
			location[1] = (int) Math.round(Math.random()*13);			
		}
		else if(pitch.equals("Knuckleball")) {
			location[0] = (int) Math.round(Math.random()*10);
			location[1] = (int) Math.round(Math.random()*26);
		}
		return location;
	}
		
	//convert rating to exponential scale
	public int normalizeRating(int rating) {
		double doubled = (double) rating;
		int norm = (int) Math.round((0.00000207192*(Math.pow(doubled, 4)))+(-0.000281775*Math.pow(doubled, 3))
				 +(0.0133464*Math.pow(doubled, 2))+(-0.021359*doubled)+2.2);
		return norm;
	}
	
	//adjust ratings for platoon effect
	public int platoonEffect(int rating, Player hitter, Pitcher pitcher, String pitch, String type) {
		if(hitter.getHand().equals("SR")) {
			if(pitcher.getHand().equals("R")) {
				rating = rating + (hitter.getSplit() - pitcher.getSplit());
			}
			else {
				if(!pitch.equals("Splitter") && !pitch.equals("Changeup") && !pitch.equals("Curveball") && !pitch.equals("Screwball") 
				   && !pitch.equals("Knuckleball")) {
					rating = rating + ((Math.abs(hitter.getSplit()) + pitcher.getSplit())/2);
				}
			}
		}
		else if(hitter.getHand().equals("SL")) {
			if(pitcher.getHand().equals("R")) {
				if(!pitch.equals("Splitter") && !pitch.equals("Changeup") && !pitch.equals("Curveball") && !pitch.equals("Screwball")
				   && !pitch.equals("Knuckleball")) {
					rating = rating + (hitter.getSplit() + Math.abs(pitcher.getSplit()));
				}
			}
			else {
				rating = rating + (-hitter.getSplit() + pitcher.getSplit());
			}
		}
		else if(hitter.getHand().equals("R")) {
			if(pitcher.getHand().equals("R")) {
				rating = rating + ((hitter.getSplit() + pitcher.getSplit())/3);
			}
			else {
				rating = rating + ((hitter.getSplit() + pitcher.getSplit())/2);
			}
		}
		else if(hitter.getHand().equals("L")) {
			if(pitcher.getHand().equals("R")) {
				if(!pitch.equals("Splitter") && !pitch.equals("Changeup") && !pitch.equals("Curveball") && !pitch.equals("Screwball")
				   && !pitch.equals("Knuckleball")) {
					rating = rating + (hitter.getSplit() + Math.abs(pitcher.getSplit()));
				}
			}
			else {
				rating = rating - (hitter.getSplit() + pitcher.getSplit());
			}
		}
		if(rating < 1) {
			rating = 1;
		}
		else if(rating > 60) {
			rating = 60;
		}
		return rating;
	}
	
	//range scaler
	public double scale(double value,  double min, double max, double finalMin, double finalMax) {
		return ((finalMax - finalMin) * (value - min) / (max - min)) + finalMin;
	}

	public Game(Team team1, Team team2) {
		//import starters and lineups
		pitcher1 = team1.getStarter();
		pitcher2 = team2.getStarter();
		if(pitcher1.getHand().equals("R")) {
			for(int i = 0; i < lineup2.size(); i++) {
				lineup2.put(team2.getRHP().get(i).getPosition(), team2.getRHP().get(i));
			}
		}
		else {
			for(int i = 0; i < lineup2.size(); i++) {
				lineup2.put(team2.getLHP().get(i).getPosition(), team2.getLHP().get(i));
			}
		}
		if(pitcher2.getHand().equals("R")) {
			for(int i = 0; i < lineup1.size(); i++) {
				lineup1.put(team1.getRHP().get(i).getPosition(), team1.getRHP().get(i));
			}
		}
		else {
			for(int i = 0; i < lineup1.size(); i++) {
				lineup1.put(team1.getLHP().get(i).getPosition(), team1.getLHP().get(i));
			}
		}
		//start game
		for(int inning = 1; inning < 10; inning++) {
			while(top == true) {
				//pickoff check
				if(!first.equals("")) {
					int p = (int) Math.round(Math.random()*10);
					if(p == 5) {
						int pick = (int) Math.round(Math.random()*100);
						if(pick > (49 - (pitcher1.getPickoff()/10) + (lineup2.get(lineupCount2).getSpeed()/10) - (team2.getStealing()/2))
								&& pick < (51 + (pitcher1.getPickoff()/10) - (lineup2.get(lineupCount2).getSpeed()/10)) + (team2.getStealing()/2)) {	
							System.out.println(lineup2.get(lineupCount2).getName() + " gets picked off!");
							outs++;
							lineupCount2++;
							if(lineupCount2 == 10) {
								lineupCount2 = 1;
							}
							if(outs == 3) {
								outs = 0;							
								top = false;
								break;
							}
						}
					}
				}
				//simulate at-bat
				String pitch = pitcher1.getPitch(balls, strikes);
				Integer[] location = getLocation(pitch).clone();
				Random r = new Random();
				int xMiss = (int) Math.round(r.nextGaussian()*(120/normalizeRating(pitcher1.getControl())));						
				int yMiss = (int) Math.round(r.nextGaussian()*(120/normalizeRating(pitcher1.getControl())));
				if(pitch.equals("4-seamer") || pitch.equals("2-seamer")) {
					xMiss = (int) Math.round(xMiss/2);
					yMiss = (int) Math.round(yMiss/2);
				}
				location[0] = location[0] + xMiss;
				location[1] = location[1] + yMiss;
				System.out.println(pitch + " | x: " + location[0] + " y: " + location[1]);
				//roll for swing
				int oThreshold = 30;
				int zThreshold = 60;
				if(strikes == 2) {
					oThreshold = 50;
					zThreshold = 80;
				}
				//out of the zone
				if(location[0] < 0 || location[0] > 20 || location[1] < 0 || location[1] > 26) {
					int s = (int) Math.round(Math.random() * 100);
					//swing roll			
					int recogRating = platoonEffect(normalizeRating(lineup2.get(lineupCount2).getRecognition()), 
									  lineup2.get(lineupCount2), pitcher1, pitch, "recognition");
					int deceptRating = normalizeRating(pitcher1.getDeception());
					int aggressRating = normalizeRating(lineup2.get(lineupCount2).getAggression());					
					if(s <= oThreshold - (scale(recogRating, 1, 60, 0, 22) - 11) + (scale(deceptRating, 1, 60, 0, 14 - 7)) 
				       + (scale(aggressRating, 2, 57, 0, 14) - 7)) {
						//swung, contact roll
						int c = (int) Math.round(Math.random() * 100);
						int contactSplit = platoonEffect(normalizeRating(lineup2.get(lineupCount2).getContact()), 
									  	   lineup2.get(lineupCount2), pitcher1, pitch, "contact");
						int recogSplit = platoonEffect(normalizeRating(lineup2.get(lineupCount2).getRecognition()), 
										 lineup2.get(lineupCount2), pitcher1, pitch, "recognition");
						int powerSplit = platoonEffect(normalizeRating(lineup2.get(lineupCount2).getPower()),
										 lineup2.get(lineupCount2), pitcher1, pitch, "batspeed");
						int normVelocity = normalizeRating(pitcher1.getVelocity());
						int normMovement = normalizeRating(pitcher1.getMovement());
						int normDeception = normalizeRating(pitcher1.getDeception());
						int contactThreshold = 60;
						boolean caught = false;
						String[] outcome = new String[2];
						if(pitch.equals("Slider") || pitch.equals("Changeup") || pitch.equals("Screwball") || pitch.equals("Splitter")) {
							contactThreshold-=5;
						}
						else if(pitch.equals("Curveball") || pitch.equals("4-seamer") || pitch.equals("Knuckleball")) {
							contactThreshold-=3;
						}
						if(c <= contactThreshold + ((scale(contactSplit, 1, 60, 0, 36) - 18)) + (scale(recogSplit, 1, 60, 0, 14 - 7)) 
						   + (scale(powerSplit, 1, 60, 0, 14 - 7)) - (scale(normVelocity, 1, 60, 0, 22 - 11)) 
						   - (scale(normMovement, 1, 60, 0, 22 - 11)) - (scale(normDeception, 1, 60, 0, 22 - 11)) 
						   - (int)Math.round((Math.abs(location[0] - 10)/2)) - (int)Math.round((Math.abs(location[1] - 10)/2))) {
							BattedBall battedBall = outCome(pitch, location, pitcher1, lineup2.get(lineupCount2));
							if(battedBall.getFoul() == true) {
								if(battedBall.getType().equals("Groundball")) {
									if(strikes < 2) {
										strikes++;
									}
									continue;
								}
								else if(battedBall.getType().equals("Popup")){
									if(battedBall.getAzimuth() < 3 || battedBall.getAzimuth() > 27) {
										if(battedBall.getVelo() < 85) {
											caught = foulOutcome(battedBall, lineup2, team2);
											if(caught == false) {
												if(strikes < 2) {
													strikes++;
												}
												continue;
											}
											else {
												System.out.println(lineup2.get(lineupCount2).getName() + " fouls out!");
												outs++;
												lineupCount2++;
												balls = 0;
												strikes = 0;
												if(lineupCount2 == 10) {
													lineupCount2 = 1;
												}
												if(outs == 3) {
													outs = 0;							
													top = false;
													break;
												}
												continue;
											}
										}
										else {
											caught = foulOutcome(battedBall, lineup2, team2);
											if(caught == false) {
												if(strikes < 2) {
													strikes++;
												}
												continue;
											}
											else {
												System.out.println(lineup2.get(lineupCount2).getName() + " fouls out!");
												outs++;
												lineupCount2++;
												balls = 0;
												strikes = 0;
												if(lineupCount2 == 10) {
													lineupCount2 = 1;
												}
												if(outs == 3) {
													outs = 0;							
													top = false;
													break;
												}
												continue;
											}											
										}										
									}
								}
								else if(battedBall.getType().equals("Flyball")){
									caught = foulOutcome(battedBall, lineup2, team2);
									if(caught == false) {
										if(strikes < 2) {
											strikes++;
										}
										continue;
									}
									else {
										System.out.println(lineup2.get(lineupCount2).getName() + " fouls out!");
										outs++;										
										lineupCount2++;
										balls = 0;
										strikes = 0;
										if(lineupCount2 == 10) {
											lineupCount2 = 1;
										}
										if(outs == 3) {
											outs = 0;							
											top = false;
											break;
										}
										continue;
									}
								}
							}
							else if(battedBall.getType().equals("Groundball")) {
								outcome = hitOutcome(battedBall, lineup1, lineup2.get(lineupCount2));
								Player defender = new Player("x");
								if(outcome[0].equals("Caught")) {
									System.out.println(lineup2.get(lineupCount2).getName() + " grounds out!");
									outs++;
									lineupCount2++;
									balls = 0;
									strikes = 0;
									if(lineupCount2 == 10) {
										lineupCount2 = 1;
									}
									if(outs == 3) {
										outs = 0;							
										top = false;
										break;
									}
									continue;
								}
								else {
									System.out.println(lineup2.get(lineupCount2).getName() + " hits a single!");
									lineupCount2++;
									if(lineupCount2 == 10) {
										lineupCount2 = 1;
									}
									if(!first.equals("")) {
										second = first;
										first[0] = "";
										first[1] = "";
									}
									if(!second.equals("")) {
										third = second;
										second[0] = "";
										second[1] = "";
									}
									if(!third.equals("")) {
										System.out.println(third + " scores!");
										third[0] = "";
										third[1] = "";
									}
								}
							}						
							else if(battedBall.getType().equals("Flyball")) {
								outcome = hitOutcome(battedBall, lineup1, lineup2.get(lineupCount2));
								Player defender = new Player("x");
								Player runner = new Player("x");
								if(outcome[0].equals("Caught")) {
									System.out.println(lineup2.get(lineupCount2).getName() + " grounds out!");
									outs++;
									lineupCount2++;
									balls = 0;
									strikes = 0;
									if(lineupCount2 == 10) {
										lineupCount2 = 1;
									}
									if(outs == 3) {
										outs = 0;							
										top = false;
										break;
									}
									continue;
								}
								else {
									if(outcome[0].equals("1B")) {
										System.out.println(lineup2.get(lineupCount2).getName() + " hits a single!");
										lineupCount2++;
										if(lineupCount2 == 10) {
											lineupCount2 = 1;
										}
										if(!first.equals("") && !second.equals("")) {
											if(outcome[1].equals("CF") || outcome[1].equals("RF")) {
												defender = lineup1.get(outcome[1]);
												runner = lineup2.get(second[1]);
											}
											if(r.nextInt(100) > (40 + scale(normalizeRating(defender.getArm()), 1, 60, 1, 20) 
											   - scale(normalizeRating(runner.getSpeed()), 1, 60, 1, 20))) {
												if(r.nextInt(100) > (50 - scale(normalizeRating(defender.getArm()), 1, 60, 1, 20) 
												   + scale(normalizeRating(runner.getSpeed()), 1, 60, 1, 20))) {
													System.out.println(runner.getName() + " advances to home!");
													third = first;
													second[0] = "";
													second[0] = "";
													first[1] = "";
													first[1] = "";
												}
												else {
													System.out.println(runner.getName() + " gets thrown out by " + defender.getName() + "!");
												}
											}											
											else {
												third = second;
												second = first;
												first[0] = "";
												first[1] = "";
												
											}
										}
										if(!first.equals("")) {
											
										}
										if(!second.equals("")) {
											third = second;
											second[0] = "";
											second[1] = "";
										}
										if(!third.equals("")) {
											System.out.println(third + " scores!");
											third[0] = "";
											third[1] = "";
										}
									}
								}								
							}
							else if(battedBall.getType().equals("Popup")) {
								outcome = hitOutcome(battedBall, lineup1, lineup2.get(lineupCount2));
								if(outcome[0].equals("Caught")) {
									System.out.println(lineup2.get(lineupCount2).getName() + " grounds out!");
									outs++;
									lineupCount2++;
									balls = 0;
									strikes = 0;
									if(lineupCount2 == 10) {
										lineupCount2 = 1;
									}
									if(outs == 3) {
										outs = 0;							
										top = false;
										break;
									}
									continue;
								}
								else {
									//hit, baserunning etc.
								}
							}
						}
						//no contact
						else {
							strikes++;
							System.out.println("strike " + strikes);
							if(strikes == 3) {
								System.out.println(lineup2.get(lineupCount2).getName() + " strikes out!");
								outs++;
								lineupCount2++;
								balls = 0;
								strikes = 0;
							}
							continue;
						}
					}
					//no swing
					else {
						balls++;
						System.out.println("ball " + balls);
						if(balls == 4) {
							System.out.println(lineup2.get(lineupCount2).getName() + " walks!");	
							lineupCount2++;
							balls = 0;
							strikes = 0;
						}
						continue;
					}
				}
				//in the zone (PLACEHOLDER)
				else {
					int s = (int) Math.round(Math.random() * 100);
					//swing
					if(s <= zThreshold - ((scale(normalizeRating(lineup2.get(lineupCount2).getRecognition()), 2, 57, 0, 22)) - 11) 
					   + ((scale(normalizeRating(lineup2.get(lineupCount2).getAggression()), 2, 57, 0, 14)) - 7)) {
						
					}
					//no swing
					else {
						strikes++;
						System.out.println("strike " + strikes);
						if(strikes == 3) {
							System.out.println(lineup2.get(lineupCount2).getName() + " strikes out!");
							outs++;
							lineupCount2++;
							balls = 0;
							strikes = 0;
						}
						continue;
					}
				}
			}			
		}
	}
}