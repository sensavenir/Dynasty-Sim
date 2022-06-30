import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;

public class Game {
	private int id;
	private int outs = 0;	
	private boolean top = true;
	int lineupCount1 = 0;
	int lineupCount2 = 0;
	ArrayList<String> index1 = new ArrayList<String>();
	ArrayList<String> index2 = new ArrayList<String>();
	private Pitcher pitcher1;
	private Pitcher pitcher2;
	private HashMap<String, Player> lineup1 = new HashMap<String, Player>();
	private HashMap<String, Player> lineup2 = new HashMap<String, Player>();
	private Team team1 = new Team(0);
	private Team team2 = new Team(0);
	private String[] first = {"", ""};
	private String[] second = {"", ""};
	private String[] third = {"", ""};
	private int strikes = 0;
	private int balls = 0;
	HashMap<String, Integer[]> positioning = new HashMap<String, Integer[]>();
	String[] fourSeamer = {"4-seamer", "35", "21", "34", "10"};
	String[] twoSeamer = {"2-seamer", "46", "19", "27", "8"};
	String[] Cutter = {"Cutter", "44", "20", "27", "9"};
	String[] Sinker = {"Sinker", "59", "17", "19", "5"};
	String[] Slider = {"Slider", "45" , "17" ,"29", "9"};
	String[] Curveball = {"Curveball", "50", "19", "25", "6"};
	String[] Changeup = {"Changeup", "49" , "18" , "26" ,"7"}; 
	String[] Splitter = {"Splitter", "53" , "17" , "24" ,"6"};
	String[] Screwball = {"Screwball", "56" , "15" , "26" ,"4"};
	String[] Knuckleball = {"Knuckleball", "43" , "17" , "30" ,"10"}; 

	//generate batted ball outcome
	private BattedBall outCome(String pitch, Integer[] location, Pitcher pitcher, Player hitter) {
		int batSpeed = hitter.getPower();
		int swing = hitter.getSwing();
		int contact = hitter.getContact();
		int velocity = normalizeRating(pitcher1.getVelocity());
		int movement = normalizeRating(pitcher1.getMovement());		
		//roll for exit velocity, batted ball type and azimuth
		int	outVelo = -16 + Math.abs(10 - location[0]);
		outVelo = outVelo + Math.abs(13 - location[1]);
		outVelo = (int) Math.round(outVelo/4);
		Random d = new Random();			
		double exitVelocity = (d.nextGaussian()*6) + (scale(batSpeed, 1, 99, 73, 88)) - (scale(movement, 1, 60, 0, 10));
		exitVelocity = exitVelocity - outVelo;
		double azimuth = 0;
		String bbType = "";
		int bb = (int) Math.round(Math.random()*100);
		String[][] bbPitch = new String[10][];
		bbPitch[0] = fourSeamer.clone();
		bbPitch[1] = twoSeamer.clone();
		bbPitch[2] = Cutter.clone();
		bbPitch[3] = Sinker.clone();
		bbPitch[4] = Slider.clone();
		bbPitch[5] = Curveball.clone();
		bbPitch[6] = Changeup.clone();
		bbPitch[7] = Splitter.clone();
		bbPitch[8] = Knuckleball.clone();
		bbPitch[9] = Screwball.clone();
		//batted ball
		int launchAngle = 0;
		for(int i = 0; i < 10; i++) {
			if(pitch.equals(bbPitch[i][0])) {				
				double count = 99 - Integer.parseInt(bbPitch[i][1]) + (location[1]) + (scale(swing, 1, 99, -15, 5)) - scale(movement, 1, 99, 0, 16);				
				if(bb > count) {
					bbType = "Groundball";
					launchAngle = (int) Math.round((Math.random()*45) - 40);
					break;
				}					
				count = count - Integer.parseInt(bbPitch[i][2]);	
				if(bb > count) {
					bbType = "Liner";
					launchAngle = (int) Math.round(Math.random()*20)+10;
					break;
				}
				count = count - Integer.parseInt(bbPitch[i][3]) - (int) Math.round((((double) location[1] - 13)*0.8)) - (int) Math.round(((scale(swing, 1, 60, 0, 26) - 13)*0.8));
				if(bb > count) {
					bbType = "Flyball";
					launchAngle = (int) Math.round((Math.random()*30) + 25 + (scale(swing, 1, 99, 0, 15)) - (scale(movement, 1, 99, 0, 16)*0.8));
					break;
				}
				if(bb > count) {
					bbType = "Popup";
					launchAngle = (int) Math.round(Math.random()*40)*70;
					if(launchAngle == 90) {
						launchAngle = 91;
					}
					break;
				}
				else {
					bbType = "Flyball";
					launchAngle = (int) Math.round(Math.random()*25)+25;
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
			distance = ((Math.pow(exitVelocity-(Math.pow(exitVelocity/25, 2)), 2) * Math.sin(Math.toRadians(2*(180-launchAngle))))/21.9)*1.4667;
		}
		else if(launchAngle >= 0) {
			distance = ((Math.pow(exitVelocity-(Math.pow(exitVelocity/25, 2)), 2) * Math.sin(Math.toRadians(2*launchAngle)))/21.9)*1.4667;
		}
		else {
			distance = ((Math.pow(exitVelocity-(Math.pow(exitVelocity/25, 2)), 2) * Math.sin(Math.toRadians(2*Math.abs(launchAngle))))/21.9)*1.4667;
		}
		System.out.println(exitVelocity + ": EV");
		System.out.println(distance + ": distance");
		System.out.println(launchAngle + ": launch angle");
		System.out.println(azimuth*3 + ": azimuth");
		double time = (2*(exitVelocity*0.44704)*Math.sin(Math.toRadians(launchAngle))/9.82);
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
		double xBall = distance*Math.sin(Math.toRadians(azimuth));
		double yBall = distance*Math.cos(Math.toRadians(azimuth));
		Integer[] pos = positioning.get(defender.getPosition()).clone();
		double xDef = pos[0]*Math.sin(Math.toRadians(pos[1]));
		double yDef = pos[0]*Math.cos(Math.toRadians(pos[1]));
		double ground2Cover = Math.sqrt(Math.pow(xBall - xDef, 2) + Math.pow(yBall - yDef, 2));
		double defTime = r.nextGaussian()*scale(normalizeRating(60-defender.getReaction()), 1, 60, 0.5, 1.5) + ground2Cover/(r.nextGaussian() + scale(normalizeRating(defender.getDefense()), 1, 60, 15, 23));
		System.out.println(defender.getPosition() + " | Hang time: " + time + ", Route time: " + defTime);		
		if(time >= defTime) {
			caught = true;
		}
		return caught;
	}
	
	//generate foul outcome
	private boolean foulOutcome(BattedBall bb, HashMap<String, Player> lineup, Team team) {
		boolean caught = false;
		if(bb.getType().equals("Flyball") || bb.getType().equals("Liner")) {			
			if(bb.getAzimuth() < 16) {
				caught = routeCalc(bb.getType(), bb.getDistance(), 45+(bb.getAzimuth()), bb.getTime(), lineup.get("LF"));
			}
			else {							
				caught = routeCalc(bb.getType(), bb.getDistance(), (bb.getAzimuth())+30, bb.getTime(), lineup.get("RF"));
			}
		}	
		if(bb.getType().equals("Popup")) {
			if(bb.getAzimuth() < 16) {					
				caught = routeCalc(bb.getType(), bb.getDistance(), 45+(bb.getAzimuth()), bb.getTime(), lineup.get("3B"));
			}
			else {
				caught = routeCalc(bb.getType(), bb.getDistance(), (bb.getAzimuth())+30, bb.getTime(), lineup.get("1B"));
			}
		}	
		return caught;
	}
	
	private boolean checkHR(BattedBall bb, Player batter) {
		boolean hr = false;
		System.out.println("wall distance: ");
		if(bb.getAzimuth() <= 10 && bb.getLaunch() > 0) {
			System.out.println((team1.getStadium()[0]/Math.cos(Math.toRadians(bb.getAzimuth()*3))) + (team1.getStadium()[3]/Math.tan(Math.toRadians(bb.getLaunch()))));
			if(bb.getDistance() > (team1.getStadium()[0]/Math.cos(Math.toRadians(bb.getAzimuth()*3))) + (team1.getStadium()[3]/Math.tan(Math.toRadians(bb.getLaunch())))) {
				hr = true;
				System.out.println(batter.getName() + " hits a home run!");
				batter.setOutcome("HR");
			}
		}
		else if(bb.getAzimuth() <= 15 && bb.getLaunch() > 0) {
			System.out.println((team1.getStadium()[1]/Math.cos(Math.toRadians(45-(bb.getAzimuth()*3)))) + (team1.getStadium()[4]/Math.tan(Math.toRadians(bb.getLaunch()))));
			if(bb.getDistance() > (team1.getStadium()[1]/Math.cos(Math.toRadians(45-(bb.getAzimuth()*3)))) + (team1.getStadium()[4]/Math.tan(Math.toRadians(bb.getLaunch())))) {
				hr = true;
				System.out.println(batter.getName() + " hits a home run!");
				batter.setOutcome("HR");
			}
		}
		else if(bb.getAzimuth() <= 20 && bb.getLaunch() > 0) {
			System.out.println((team1.getStadium()[1]/Math.cos(Math.toRadians((bb.getAzimuth()*3)-45)))  + (team1.getStadium()[4]/Math.tan(Math.toRadians(bb.getLaunch()))));
			if(bb.getDistance() > (team1.getStadium()[1]/Math.cos(Math.toRadians((bb.getAzimuth()*3)-45))) + (team1.getStadium()[4]/Math.tan(Math.toRadians(bb.getLaunch())))) {
				hr = true;
				System.out.println(batter.getName() + " hits a home run!");
				batter.setOutcome("HR");
			}
		}
		else if(bb.getAzimuth() <= 30 && bb.getLaunch() > 0) {			
			System.out.println((team1.getStadium()[2]/Math.cos(Math.toRadians(90-(bb.getAzimuth()*3)))) + (team1.getStadium()[5]/Math.tan(Math.toRadians(bb.getLaunch()))));
			if(bb.getDistance() > (team1.getStadium()[2]/Math.cos(Math.toRadians(90-(bb.getAzimuth()*3)))) + (team1.getStadium()[5]/Math.tan(Math.toRadians(bb.getLaunch())))) {				
				hr = true;
				System.out.println(batter.getName() + " hits a home run!");
				batter.setOutcome("HR");
			}
		}
		return hr;
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
				if(r.nextInt(1000) > 450 - defense) {
					caught = true;							
				}
			}
			else if(bb.getAzimuth() < 5) {
				defender = lineup.get("3B");
				int defense = normalizeRating(defender.getReaction()) + normalizeRating(defender.getSpeed());
				if(r.nextInt(1000) > 200 - defense) {
					caught = true;
				}					
			}
			else if(bb.getAzimuth() < 8) {
				defender = lineup.get("3B");
				int defense = normalizeRating(defender.getReaction()) + normalizeRating(defender.getSpeed());
				if(r.nextInt(1000) > 200 - defense) {
					caught = true;
				}					
				if(caught == false) {
					defender = lineup.get("SS");
					defense = normalizeRating(defender.getReaction()) + normalizeRating(defender.getSpeed());
					if(r.nextInt(1000) > 200 - defense) {
						caught = true;
					}						
				}
			}
			else if (bb.getAzimuth() < 12) {
				defender = lineup.get("SS");
				int defense = normalizeRating(defender.getReaction()) + normalizeRating(defender.getSpeed());
				if(r.nextInt(1000) > 200 - defense) {
					caught = true;
				}
			}
			else if (bb.getAzimuth() < 18) {
				defender = lineup.get("SS");
				int defense = normalizeRating(defender.getReaction()) + normalizeRating(defender.getSpeed());
				if(r.nextInt(1000) > 850 - defense) {
					caught = true;
				}
				if(caught == false) {
					defender = lineup.get("2B");
					defense = normalizeRating(defender.getReaction()) + normalizeRating(defender.getSpeed());
					if(r.nextInt(1000) > 850 - defense) {
						caught = true;
					}						
				}
			}
			else if (bb.getAzimuth() < 23) {
				defender = lineup.get("2B");
				int defense = normalizeRating(defender.getReaction()) + normalizeRating(defender.getSpeed());
				if(r.nextInt(1000) > 200 - defense) {
					caught = true;
				}
			}		
			else if (bb.getAzimuth() < 26) {
				defender = lineup.get("2B");
				int defense = normalizeRating(defender.getReaction()) + normalizeRating(defender.getSpeed());
				if(r.nextInt(1000) > 200 - defense) {
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
				if(r.nextInt(1000) > 200 - defense) {
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
		else if(bb.getType().equals("Flyball") || bb.getType().equals("Liner")) {
			if(bb.getAzimuth() < 8) {
				defender = lineup.get("LF");
				caught = routeCalc(bb.getType(), bb.getDistance(), 45-(bb.getAzimuth()*3), bb.getTime(), defender);
			}
			else if(bb.getAzimuth() < 16) {
				defender = lineup.get("CF");
				caught = routeCalc(bb.getType(), bb.getDistance(), 45-(bb.getAzimuth()*3), bb.getTime(), defender);
				if(caught == false) {
					defender = lineup.get("LF");
					caught = routeCalc(bb.getType(), bb.getDistance(), 45-(bb.getAzimuth()*3), bb.getTime(), defender);
				}	
			}
			else if(bb.getAzimuth() < 24) {
				defender = lineup.get("CF");
				caught = routeCalc(bb.getType(), bb.getDistance(), (bb.getAzimuth()*3)-45, bb.getTime(), defender);
				if(caught == false) {
					defender = lineup.get("RF");
					caught = routeCalc(bb.getType(), bb.getDistance(), (bb.getAzimuth()*3)-45, bb.getTime(), defender);
				}
			}
			else if(bb.getAzimuth() <= 30) {
				defender = lineup.get("RF");
				caught = routeCalc(bb.getType(), bb.getDistance(), (bb.getAzimuth()*3)-45, bb.getTime(), defender);
			}
		}		
		if(caught == false) {
			if(bb.getType().equals("Groundball")) {
				hit[0] = "1B";
				hit[1] = defender.getPosition();
			}
			else if(bb.getType().equals("Flyball")) {
				if(bb.getDistance() > positioning.get(defender.getPosition())[0]) {
					if(r.nextInt(100) > 80 + scale(normalizeRating(defender.getArm()), 1, 60, 0, 9) - scale(normalizeRating(batter.getSpeed()), 1, 60, 0, 9)) {
						hit[0] = "3B";
						hit[1] = defender.getPosition();
					}
					else {
						hit[0] = "2B";
						hit[1] = defender.getPosition();
					}
				}
				else {
					if(r.nextInt(100) > 70 + scale(normalizeRating(defender.getArm()), 1, 60, 0, 9)- scale(normalizeRating(batter.getSpeed()), 1, 60, 0, 10) ) {
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
					if(r.nextInt(100) > 90 + scale(normalizeRating(defender.getArm()), 1, 60, 0, 9) - scale(normalizeRating(batter.getSpeed()), 1, 60, 0, 9) - scale(bb.getVelo(), 65, 102, -18, 18) && bb.getVelo() > 75) {
						hit[0] = "3B";
						hit[1] = defender.getPosition();
					}
					else if(r.nextInt(100) > 70 + scale(normalizeRating(defender.getArm()), 1, 60, 0, 9) - scale(normalizeRating(batter.getSpeed()), 1, 60, 0, 9) - scale(bb.getVelo(), 65, 102, -18, 18) && bb.getVelo() > 75) {
						hit[0] = "2B";
						hit[1] = defender.getPosition();
					}
					else {
						hit[0] = "1B";
						hit[1] = defender.getPosition();
					}
				}
				else {
					if(r.nextInt(100) > 67 + scale(normalizeRating(defender.getArm()), 1, 60, 0, 9) - scale(normalizeRating(batter.getSpeed()), 1, 60, 0, 10) - scale(bb.getVelo(), 65, 102, -18, 18) && bb.getVelo() > 75) {
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
	
	private void baseRunning(HashMap<String,Player> batLineup, HashMap<String,Player> defLineup, ArrayList<String> index, int lineupCount, String[] outcome) {
		Random r = new Random();
		Player defender = new Player("x");
		Player runner = new Player("x");
		//single		
		if(outcome[0].equals("1B")) {			
			//runner on 2nd
			if(!third[0].equals("")) {
				System.out.println(third[0] + " scores!");
				third[0] = "";
				third[0] = "";
			}	
			if(!second[0].equals("")) {
				if(outcome[1].equals("CF") || outcome[1].equals("RF") || outcome[1].equals("RF")) {
					defender = lineup1.get(outcome[1]);
					runner = lineup2.get(second[1]);
					//advance attempt
					if(r.nextInt(100) > (40 + scale(normalizeRating(defender.getArm()), 1, 60, 1, 20) 
					   - scale(normalizeRating(runner.getSpeed()), 1, 60, 1, 20))) {
						//outfield assist
						if(r.nextInt(100) > (50 - scale(normalizeRating(defender.getArm()), 1, 60, 1, 20) 
								+ scale(normalizeRating(runner.getSpeed()), 1, 60, 1, 20))) {
							System.out.println(runner.getName() + " advances to home!");
							if(!first[0].equals("")) {
								third = first;
							}
							second[0] = "";
							second[0] = "";
							first[0] = batLineup.get(index.get(lineupCount)).getName();
							first[1] = batLineup.get(index.get(lineupCount)).getPosition();
						}
						else {
							System.out.println(runner.getName() + " gets thrown out by " + defender.getName() + "at home!");
							outs++;
							if(outs == 3) {
								outs = 0;
								top = !top;
							}
							if(!first[0].equals("")) {
								third = first;
							}
							second[0] = "";
							second[1] = "";
							first[0] = batLineup.get(index.get(lineupCount)).getName();
							first[1] = batLineup.get(index.get(lineupCount)).getPosition();
						}
					}
					//no advance											
					else {
						third = second;
						if(!first[0].equals("")) {
							second = first;
						}
						else {
							second[0] = "";
							second[0] = "";
						}
						first[0] = batLineup.get(index.get(lineupCount)).getName();
						first[1] = batLineup.get(index.get(lineupCount)).getPosition();
					}
				}
				//non outfield hit
				else {			
					if(!first[0].equals("")) {
						third = second;
						second = first;	
						first[0] = batLineup.get(index.get(lineupCount)).getName();
						first[1] = batLineup.get(index.get(lineupCount)).getPosition();
					}
					else {
						third = second;
						second[0] = "";
						second[0] = "";
						first[0] = batLineup.get(index.get(lineupCount)).getName();
						first[1] = batLineup.get(index.get(lineupCount)).getPosition();
					}
				}
			}
			else if(!first[0].equals("")) {
				if(outcome[1].equals("CF") || outcome[1].equals("RF")) {
					defender = lineup1.get(outcome[1]);
					runner = lineup2.get(first[1]);
					if(r.nextInt(100) > (40 + scale(normalizeRating(defender.getArm()), 1, 60, 1, 20) 
					   - scale(normalizeRating(runner.getSpeed()), 1, 60, 1, 20))) {
						if(r.nextInt(100) > (50 - scale(normalizeRating(defender.getArm()), 1, 60, 1, 20) 
						   + scale(normalizeRating(runner.getSpeed()), 1, 60, 1, 20))) {
							System.out.println(runner.getName() + " advances to third!");
							third = first;
							first[0] = batLineup.get(index.get(lineupCount)).getName();
							first[1] = batLineup.get(index.get(lineupCount)).getPosition();
						}
						else {
							System.out.println(runner.getName() + " gets thrown out by " + defender.getName() + "at third!");
							outs++;
							if(outs == 3) {
								outs = 0;
								top = !top;
							}
							first[0] = batLineup.get(index.get(lineupCount)).getName();
							first[1] = batLineup.get(index.get(lineupCount)).getPosition();
						}
					}											
					else {
						second = first;
						first[0] = batLineup.get(index.get(lineupCount)).getName();
						first[1] = batLineup.get(index.get(lineupCount)).getPosition();
					}
				}
				//non outfield hit
				else if(!third[0].equals("")) {
					System.out.println(third[0] + " scores!");			
					third[0] = "";
					third[0] = "";
					second = first;
					first[0] = batLineup.get(index.get(lineupCount)).getName();
					first[1] = batLineup.get(index.get(lineupCount)).getPosition();
				}
			}		
			else if(!third[0].equals("")) {
				System.out.println(third + " scores!");
				first[0] = batLineup.get(index.get(lineupCount)).getName();
				first[1] = batLineup.get(index.get(lineupCount)).getPosition();
			}
		}
		else if(outcome[0].equals("2B")) {
			//runner on 1st
			if(!third[0].equals("")) {
				System.out.println(third[0] + " scores!");
				third[0] = "";
				third[0] = "";
			}
			if(!second[0].equals("")) {
				System.out.println(second[0] + " scores!");
				second[0] = "";
				second[0] = "";
			}
			if(!first[0].equals("")) {
				if(outcome[1].equals("CF") || outcome[1].equals("RF") || outcome[1].equals("RF")) {
					defender = lineup1.get(outcome[1]);
					runner = lineup2.get(first[1]);					
					//advance attempt
					if(r.nextInt(100) > (40 + scale(normalizeRating(defender.getArm()), 1, 60, 1, 20) 
					   - scale(normalizeRating(runner.getSpeed()), 1, 60, 1, 20))) {
						//outfield assist
						if(r.nextInt(100) > (50 - scale(normalizeRating(defender.getArm()), 1, 60, 1, 20) 
								+ scale(normalizeRating(runner.getSpeed()), 1, 60, 1, 20))) {
							System.out.println(runner.getName() + " advances to home!");
							second[0] = batLineup.get(index.get(lineupCount)).getName();
							second[1] = batLineup.get(index.get(lineupCount)).getPosition();
						}
						else {
							System.out.println(runner.getName() + " gets thrown out by " + defender.getName() + "at home!");
							outs++;
							if(outs == 3) {
								outs = 0;
								top = !top;
							}
							second[0] = batLineup.get(index.get(lineupCount)).getName();
							second[1] = batLineup.get(index.get(lineupCount)).getPosition();
						}
					}
					//no advance											
					else {
						third = first;
						second[0] = batLineup.get(index.get(lineupCount)).getName();
						second[1] = batLineup.get(index.get(lineupCount)).getPosition();
					}
				}
				//non outfield hit
				else {
					third = first;
					second[0] = batLineup.get(index.get(lineupCount)).getName();
					second[1] = batLineup.get(index.get(lineupCount)).getPosition();
				}
			}			
		}
		else if(outcome[0].equals("3B")) {
			if(!third[0].equals("")) {
				System.out.println(third[0] + " scores!");
				third[0] = "";
				third[0] = "";
			}
			if(!second[0].equals("")) {
				System.out.println(second[0] + " scores!");
				second[0] = "";
				second[0] = "";
			}	
			if(!first[0].equals("")) {
				System.out.println(first[0] + " scores!");
				first[0] = "";
				first[0] = "";
			}
			third[0] = batLineup.get(index.get(lineupCount)).getName();
			third[1] = batLineup.get(index.get(lineupCount)).getPosition();
		}
		else if(outcome[0].equals("HR")) {
			if(!third[0].equals("")) {
				System.out.println(third[0] + " scores!");
				third[0] = "";
				third[0] = "";
			}
			if(!second[0].equals("")) {
				System.out.println(second[0] + " scores!");
				second[0] = "";
				second[0] = "";
			}	
			if(!first[0].equals("")) {
				System.out.println(first[0] + " scores!");
				first[0] = "";
				first[0] = "";
			}
			System.out.println(batLineup.get(index.get(lineupCount)).getName() + " scores!");
		}
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
			location[0] = (int) Math.round(Math.random()*20);
			location[1] = (int) Math.round(Math.random()*8);			
		}
		else if(pitch.equals("Knuckleball")) {
			location[0] = (int) Math.round(Math.random()*20);
			location[1] = (int) Math.round(Math.random()*26);
		}
		if(location[0] == null || location [1] == null) {
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
		this.team1 = team1;
		this.team2 = team2;
		//import starters and lineups
		pitcher1 = team1.getStarter();
		pitcher2 = team2.getStarter();		
		if(pitcher1.getHand().equals("R")) {
			for(int i = 0; i < 9; i++) {
				lineup2.put(team2.getRHP().get(i).getPosition(), team2.getRHP().get(i));
				index2.add(team2.getRHP().get(i).getPosition());
			}
		}
		else {
			for(int i = 0; i < 9; i++) {
				lineup2.put(team2.getLHP().get(i).getPosition(), team2.getLHP().get(i));
				index2.add(team2.getRHP().get(i).getPosition());
			}
		}
		if(pitcher2.getHand().equals("R")) {
			for(int i = 0; i < 9; i++) {
				lineup1.put(team1.getRHP().get(i).getPosition(), team1.getRHP().get(i));
				index1.add(team2.getRHP().get(i).getPosition());
			}
		}
		else {
			for(int i = 0; i < 9; i++) {
				lineup1.put(team1.getLHP().get(i).getPosition(), team1.getLHP().get(i));
				index1.add(team2.getRHP().get(i).getPosition());
			}
		}
		//start game
		for(int inning = 1; inning < 10; inning++) {			
			while(top == true) {
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				pitcher1.printOut();
				lineup2.get(index2.get(lineupCount2)).printOut();
				//pickoff check
				if(!first[0].equals("")) {
					int p = (int) Math.round(Math.random()*10);
					if(p == 5) {
						int pick = (int) Math.round(Math.random()*100);
						if(pick > (49 - (normalizeRating(pitcher1.getPickoff())/4) + (normalizeRating(lineup2.get(first[1]).getSpeed())/4) - (team2.getStealing()/2))
						   && pick < (51 + (normalizeRating(pitcher1.getPickoff())/4) - (normalizeRating(lineup2.get(first[1]).getSpeed()))/4) + (team2.getStealing()/2)) {	
							System.out.println(first[0] + " gets picked off!");
							outs++;
							first[0] = "";
							first[1] = "";								
							if(outs == 3) {
								outs = 0;							
								top = !top;
								continue;
							}
						}
					}
				}
				//simulate at-bat
				String pitch = pitcher1.getPitch(balls, strikes, lineup2.get(index2.get(lineupCount2)));
				Integer[] location = getLocation(pitch).clone();
				Random r = new Random();
				int xMiss = (int) Math.round(r.nextGaussian()*(1200/pitcher1.getControl()));						
				int yMiss = (int) Math.round(r.nextGaussian()*(1200/pitcher1.getControl()));
				if(pitch.equals("4-seamer") || pitch.equals("2-seamer") || pitch.equals("Cutter") || pitch.equals("Sinker")) {
					xMiss = (int) Math.round(xMiss*0.67);
					yMiss = (int) Math.round(yMiss*0.67);
				}
				System.out.println("xMiss: " + xMiss + " yMiss: " + yMiss);
				location[0] = location[0] + xMiss;
				location[1] = location[1] + yMiss;
				if(pitcher1.getHand().equals("R")) {
					if(lineup2.get(index2.get(lineupCount2)).getHand().equals("R") || lineup2.get(index2.get(lineupCount2)).getHand().equals("SL")) {
						if((location[0] > 36 && location[0] < 52) && (location[1] > -5 && location[1] < 40)) {
							if(r.nextInt(100) > 40) {
								System.out.println(lineup2.get(index2.get(lineupCount2)).getName() + " gets hit by a pitch!");
								lineupCount2++;
								if(lineupCount2 == 9) {
									lineupCount2 = 0;
								}
								first[0] = lineup2.get(index2.get(lineupCount2)).getName();
								first[1] = lineup2.get(index2.get(lineupCount2)).getPosition();
								balls = 0;
								strikes = 0;
								continue;
							}
						}
					}
				}
				else if(pitcher1.getHand().equals("L")) {
					if(lineup2.get(index2.get(lineupCount2)).getHand().equals("L") || lineup2.get(index2.get(lineupCount2)).getHand().equals("SR")) {
						if((location[0]< -16 && location[0] > -32) && (location[1] > -5 && location[1] < 40)) {
							if(r.nextInt(100) > 40) {
								System.out.println(lineup2.get(index2.get(lineupCount2)).getName() + " gets hit by a pitch!");
								lineupCount2++;
								if(lineupCount2 == 9) {
									lineupCount2 = 0;
								}
								first[0] = lineup2.get(index2.get(lineupCount2)).getName();
								first[1] = lineup2.get(index2.get(lineupCount2)).getPosition();
								continue;
							}
						}
					}
				}
				System.out.println(pitch + " | x: " + location[0] + " y: " + location[1]);
				//roll for swing
				int threshold = 30;
				boolean ball = false;
				if(location[0] < 0) {
					threshold = threshold + (location[0]);
					ball = true;
				}
				else if(location[0] > 20) {			
					threshold = threshold - ((location[0]-20));
					ball = true;
				}
				if(location[1] < 0) {
					threshold = threshold + (location[1]);
				}				
				else if(location[1] > 26) {
					threshold = threshold - ((location[1]-26));
					ball = true;
				}				
				else if(location[0] < 8 || location[0] > 12 || location[1] < 10 || location[1] > 16) {
					threshold = 60;
				}
				else {
					threshold = 80;
				}
				if(strikes == 2) {
					threshold = threshold + 20;
				}
				if(balls == 3 && strikes < 2) {
					threshold = threshold - 20;
				}
				if(!first[0].equals("") && second[0].equals("")) {
					int steal = (int) Math.round(Math.random()*100);
					if(steal > (45 + scale(normalizeRating(pitcher1.getPickoff()), 1, 60, -5, 5) + scale(normalizeRating(lineup1.get("C").getArm()), 1, 60, -5, 5) - scale(normalizeRating(lineup2.get(first[1]).getSpeed()), 1, 60, -10, 10) - (team2.getStealing()/2))
					   && steal < (55 - scale(normalizeRating(pitcher1.getPickoff()), 1, 60, -5, 5) - scale(normalizeRating(lineup1.get("C").getArm()), 1, 60, -5, 5) + scale(normalizeRating(lineup2.get(first[1]).getSpeed()), 1, 60, -10, 10) + (team2.getStealing()/2))) {	
						if(r.nextInt(100) > 70 - scale(normalizeRating(pitcher1.getPickoff()), 1, 60, -5, 5) - scale(normalizeRating(lineup1.get("C").getArm()), 1, 60, -5, 5) + scale(normalizeRating(lineup2.get(first[1]).getSpeed()), 1, 60, -10, 10)) {
							System.out.println(first[0] + " gets caught stealing, thrown out by " + lineup1.get("C").getName() + "!");
							lineup2.get(index2.get(lineupCount2)).setOutcome("CS");
							outs++;
							first[0] = "";
							first[1] = "";	
							if(outs == 3) {
								System.out.println("~~~~~~~~~~~~~~~~ bottom of the inning ~~~~~~~~~~~~~~~~~");
								outs = 0;	
								balls = 0;
								strikes = 0;
								top = !top;
								continue;
							}
						}		
						else { 
							System.out.println(first[0] + " steals second!");
							lineup2.get(index2.get(lineupCount2)).setOutcome("SB");
							second = first;
							first[0] = "";
							first[1] = "";
						}
					}
				}
				else if(!first[0].equals("") && !second[0].equals("")) {
					int steal = (int) Math.round(Math.random()*100);
					if(steal > (48 + scale(normalizeRating(pitcher1.getPickoff()), 1, 60, -5, 5) + scale(normalizeRating(lineup1.get("C").getArm()), 1, 60, -5, 5) - scale(normalizeRating(lineup2.get(second[1]).getSpeed()), 1, 60, -10, 10) - (team2.getStealing()/2))
							&& steal < (52 - scale(normalizeRating(pitcher1.getPickoff()), 1, 60, -5, 5) - scale(normalizeRating(lineup1.get("C").getArm()), 1, 60, -5, 5) + scale(normalizeRating(lineup2.get(second[1]).getSpeed()), 1, 60, -10, 10) + (team2.getStealing()/2))) {	
						if(r.nextInt(100) > 80 - scale(normalizeRating(pitcher1.getPickoff()), 1, 60, -5, 5) - scale(normalizeRating(lineup1.get("C").getArm()), 1, 60, -5, 5) + scale(normalizeRating(lineup2.get(second[1]).getSpeed()), 1, 60, -10, 10)) {
							System.out.println("Double steal! " + second[0] + " gets caught stealing, thrown out by " + lineup1.get("C").getName() + "!");
							outs++;
							second = first;
							first[0] = "";
							first[1] = "";
							if(outs == 3) {
								System.out.println("~~~~~~~~~~~~~~~~ bottom of the inning ~~~~~~~~~~~~~~~~~");
								outs = 0;	
								balls = 0;
								strikes = 0;
								top = !top;
								continue;
							}
						}		
						else { 
							System.out.println("Double steal! " + second[0] + " steals second!");
							third = second;
							second = first;
							first[0] = "";
							first[1] = "";
						}
					}
				}
				int s = (int) Math.round(Math.random() * 100);
				int recogRating = platoonEffect(normalizeRating(lineup2.get(index2.get(lineupCount2)).getRecognition()), 
								  lineup2.get(index2.get(lineupCount2)), pitcher1, pitch, "recognition");
				int deceptRating = normalizeRating(pitcher1.getDeception());
				int veloRating = normalizeRating(pitcher1.getVelocity());
				int moveRating = normalizeRating(pitcher1.getMovement());
				int aggressRating = normalizeRating(lineup2.get(index2.get(lineupCount2)).getAggression());					
				boolean swung = false;
				if(s <= threshold - (scale(recogRating, 1, 60, -20, 30)) + (scale(deceptRating, 1, 60, -5, 5)) 
				   + (scale(veloRating, 1, 60, -5, 5)) + (scale(moveRating, 1, 60, -15, 15)) + (scale(aggressRating, 2, 57, -6, 6))) {
					swung = true;
				}
				else {
					if(s <= threshold + (scale(recogRating, 1, 60, -20, 30)) - (scale(deceptRating, 1, 60, -5, 5)) 
					   - (scale(veloRating, 1, 60, -5, 5)) - (scale(moveRating, 1, 60, -15, 15)) + (scale(aggressRating, 2, 57, -6, 6))) {
						swung = true;
					}
				}		
				if(swung == true) {
					//swung, contact roll
					int c = (int) Math.round(Math.random() * 100);
					int contactSplit = platoonEffect(lineup2.get(index2.get(lineupCount2)).getContact(), 
							lineup2.get(index2.get(lineupCount2)), pitcher1, pitch, "contact");
					int recogSplit = platoonEffect(normalizeRating(lineup2.get(index2.get(lineupCount2)).getRecognition()), 
							lineup2.get(index2.get(lineupCount2)), pitcher1, pitch, "recognition");
					int powerSplit = platoonEffect(normalizeRating(lineup2.get(index2.get(lineupCount2)).getPower()),
							lineup2.get(index2.get(lineupCount2)), pitcher1, pitch, "batspeed");
					int normVelocity = normalizeRating(pitcher1.getVelocity());
					int normMovement = normalizeRating(pitcher1.getMovement());
					int contact = 80 - Math.abs(10-location[0]) - Math.abs(13-location[1]);
					boolean caught = false;
					boolean hr = false;
					String[] outcome = new String[2];
					if(pitch.equals("Slider") || pitch.equals("Changeup") || pitch.equals("Screwball") || pitch.equals("Splitter")) {
						contact = contact - 5;
					}
					else if(pitch.equals("Curveball") || pitch.equals("4-seamer") || pitch.equals("Knuckleball")) {
						contact = contact - 3;
					}
					if(c <= contact + scale(contactSplit, 1, 99, -50, 50) + (scale(recogSplit, 1, 60, -5, 5)) - (scale(normVelocity, 1, 60, -20, 20))
					   - (scale(normMovement, 1, 60, -15, 15)) - (pitcher1.getPitchSize()-1)*3) {
						BattedBall battedBall = outCome(pitch, location, pitcher1, lineup2.get(index2.get(lineupCount2)));
						//foul
						if(battedBall.getFoul() == true) {
							if(battedBall.getType().equals("Groundball")) {
								if(strikes < 2) {
									strikes++;
								}
								System.out.println(lineup2.get(index2.get(lineupCount2)).getName() + " hits it foul! " + strikes + " strikes");
								continue;
							}
							//foul
							else if(battedBall.getType().equals("Popup")){
								caught = foulOutcome(battedBall, lineup2, team2);
								if(caught == false) {											
									if(strikes < 2) {
										strikes++;
									}
									System.out.println(lineup2.get(index2.get(lineupCount2)).getName() + " hits it foul! " + strikes + " strikes");
									continue;
								}
								else {
									System.out.println(lineup2.get(index2.get(lineupCount2)).getName() + " fouls out!");
									lineup2.get(index2.get(lineupCount2)).setOutcome("FL");
									pitcher1.setOutcome("FL");
									lineup2.get(index2.get(lineupCount2)).setOutcome("PA");
									pitcher1.setOutcome("PA");
									outs++;
									lineupCount2++;
									balls = 0;
									strikes = 0;
									if(lineupCount2 == 9) {
										lineupCount2 = 0;
									}
									if(outs == 3) {
										System.out.println("~~~~~~~~~~~~~~~~ bottom of the inning ~~~~~~~~~~~~~~~~~");
										outs = 0;							
										top = !top;										
									}
									continue;
								}													
							}
							//foul
							else if(battedBall.getType().equals("Flyball") || battedBall.getType().equals("Liner")) {
								caught = foulOutcome(battedBall, lineup2, team2);
								if(caught == false) {
									if(strikes < 2) {
										strikes++;
									}
									System.out.println(lineup2.get(index2.get(lineupCount2)).getName() + " hits it foul! " + strikes + " strikes");
									continue;
								}
								else {
									System.out.println(lineup2.get(index2.get(lineupCount2)).getName() + " fouls out!");
									lineup2.get(index2.get(lineupCount2)).setOutcome("FL");
									pitcher1.setOutcome("FL");
									lineup2.get(index2.get(lineupCount2)).setOutcome("PA");
									pitcher1.setOutcome("PA");
									outs++;										
									lineupCount2++;
									balls = 0;
									strikes = 0;
									if(lineupCount2 == 9) {
										lineupCount2 = 0;
									}
									if(outs == 3) {
										System.out.println("~~~~~~~~~~~~~~~~ bottom of the inning ~~~~~~~~~~~~~~~~~");
										outs = 0;							
										top = !top;
									}
									continue;
								}
							}
						}
						//groundball
						else if(battedBall.getType().equals("Groundball")) {
							outcome = hitOutcome(battedBall, lineup1, lineup2.get(index2.get(lineupCount2)));
							if(outcome[0].equals("Caught")) {
								System.out.println(lineup2.get(index2.get(lineupCount2)).getName() + " grounds out!");
								lineup2.get(index2.get(lineupCount2)).setOutcome("GO");
								pitcher1.setOutcome("GO");
								lineup2.get(index2.get(lineupCount2)).setOutcome("PA");
								pitcher1.setOutcome("PA");
								outs++;
								lineupCount2++;
								balls = 0;
								strikes = 0;
								if(lineupCount2 == 9) {
									lineupCount2 = 0;
								}
								if(outs == 3) {
									System.out.println("~~~~~~~~~~~~~~~~ bottom of the inning ~~~~~~~~~~~~~~~~~");
									outs = 0;							
									top = !top;
								}
								continue;
							}
							else {
								System.out.println(lineup2.get(index2.get(lineupCount2)).getName() + " hits a single!");
								lineup2.get(index2.get(lineupCount2)).setOutcome("1B");
								pitcher1.setOutcome("1B");
								lineup2.get(index2.get(lineupCount2)).setOutcome("PA");
								pitcher1.setOutcome("PA");
								first[0] = lineup2.get(index2.get(lineupCount2)).getName();
								first[1] = lineup2.get(index2.get(lineupCount2)).getPosition();
								strikes = 0;
								balls = 0;
								lineupCount2++;
								if(lineupCount2 == 9) {
									lineupCount2 = 0;
								}
							}
						}						
						else if(battedBall.getType().equals("Flyball")) {
							hr = checkHR(battedBall, lineup2.get(index2.get(lineupCount2)));							
							if(hr == true) {
								lineup2.get(index2.get(lineupCount2)).setOutcome("HR");
								pitcher1.setOutcome("HR");
								lineup2.get(index2.get(lineupCount2)).setOutcome("PA");
								pitcher1.setOutcome("PA");
								lineupCount2++;
								if(lineupCount2 == 9) {
									lineupCount2 = 0;
								}
								strikes = 0;
								balls = 0;
							}
							else {
								outcome = hitOutcome(battedBall, lineup1, lineup2.get(index2.get(lineupCount2)));
								if(outcome[0].equals("Caught")) {
									System.out.println(lineup2.get(index2.get(lineupCount2)).getName() + " flies out!");
									lineup2.get(index2.get(lineupCount2)).setOutcome("FO");
									pitcher1.setOutcome("FO");
									lineup2.get(index2.get(lineupCount2)).setOutcome("PA");
									pitcher1.setOutcome("PA");
									outs++;
									lineupCount2++;
									balls = 0;
									strikes = 0;
									if(lineupCount2 == 9) {
										lineupCount2 = 0;
									}
									if(outs == 3) {
										System.out.println("~~~~~~~~~~~~~~~~ bottom of the inning ~~~~~~~~~~~~~~~~~");
										outs = 0;							
										top = !top;
									}
									continue;
								}
								else if(outcome[0].equals("1B")) {
									System.out.println(lineup2.get(index2.get(lineupCount2)).getName() + " hits a single!");
									lineup2.get(index2.get(lineupCount2)).setOutcome("1B");
									pitcher1.setOutcome("1B");
									lineup2.get(index2.get(lineupCount2)).setOutcome("PA");
									pitcher1.setOutcome("PA");
									lineupCount2++;
									if(lineupCount2 == 9) {
										lineupCount2 = 0;
									}
									strikes = 0;
									balls = 0;
								}
								else if(outcome[0].equals("2B")) {
									System.out.println(lineup2.get(index2.get(lineupCount2)).getName() + " hits a double!");
									lineup2.get(index2.get(lineupCount2)).setOutcome("2B");
									pitcher1.setOutcome("2B");
									lineup2.get(index2.get(lineupCount2)).setOutcome("PA");
									pitcher1.setOutcome("PA");
									lineupCount2++;
									if(lineupCount2 == 9) {
										lineupCount2 = 0;
									}
									strikes = 0;
									balls = 0;
								}
								else if(outcome[0].equals("3B")) {
									System.out.println(lineup2.get(index2.get(lineupCount2)).getName() + " hits a triple!");
									lineup2.get(index2.get(lineupCount2)).setOutcome("3B");
									pitcher1.setOutcome("3B");
									lineup2.get(index2.get(lineupCount2)).setOutcome("PA");
									pitcher1.setOutcome("PA");
									lineupCount2++;
									if(lineupCount2 == 9) {
										lineupCount2 = 0;
									}
									strikes = 0;
									balls = 0;
								}
							}
						}
						else if(battedBall.getType().equals("Liner")) {
							hr = checkHR(battedBall, lineup2.get(index2.get(lineupCount2)));							
							if(hr == true) {
								lineup2.get(index2.get(lineupCount2)).setOutcome("HR");
								pitcher1.setOutcome("HR");
								lineup2.get(index2.get(lineupCount2)).setOutcome("PA");
								pitcher1.setOutcome("PA");
								lineupCount2++;
								if(lineupCount2 == 9) {
									lineupCount2 = 0;
								}
								strikes = 0;
								balls = 0;
							}
							else {
								outcome = hitOutcome(battedBall, lineup1, lineup2.get(index2.get(lineupCount2)));
								if(outcome[0].equals("Caught")) {
									System.out.println(lineup2.get(index2.get(lineupCount2)).getName() + " lines out!");
									lineup2.get(index2.get(lineupCount2)).setOutcome("LO");
									pitcher1.setOutcome("LO");
									lineup2.get(index2.get(lineupCount2)).setOutcome("PA");
									pitcher1.setOutcome("PA");
									outs++;
									lineupCount2++;
									balls = 0;
									strikes = 0;
									if(lineupCount2 == 9) {
										lineupCount2 = 0;
									}
									if(outs == 3) {
										System.out.println("~~~~~~~~~~~~~~~~ bottom of the inning ~~~~~~~~~~~~~~~~~");
										outs = 0;							
										top = !top;
									}
									continue;
								}
								else if(outcome[0].equals("1B")) {
									System.out.println(lineup2.get(index2.get(lineupCount2)).getName() + " hits a single!");
									lineup2.get(index2.get(lineupCount2)).setOutcome("1B");
									pitcher1.setOutcome("1B");
									lineup2.get(index2.get(lineupCount2)).setOutcome("PA");
									pitcher1.setOutcome("PA");
									lineupCount2++;
									if(lineupCount2 == 9) {
										lineupCount2 = 0;
									}
									strikes = 0;
									balls = 0;
								}
								else if(outcome[0].equals("2B")) {
									System.out.println(lineup2.get(index2.get(lineupCount2)).getName() + " hits a double!");
									lineup2.get(index2.get(lineupCount2)).setOutcome("2B");
									pitcher1.setOutcome("2B");
									lineup2.get(index2.get(lineupCount2)).setOutcome("PA");
									pitcher1.setOutcome("PA");
									lineupCount2++;
									if(lineupCount2 == 9) {
										lineupCount2 = 0;
									}
									strikes = 0;
									balls = 0;
								}
								else if(outcome[0].equals("3B")) {
									System.out.println(lineup2.get(index2.get(lineupCount2)).getName() + " hits a triple!");
									lineup2.get(index2.get(lineupCount2)).setOutcome("3B");
									pitcher1.setOutcome("3B");
									lineup2.get(index2.get(lineupCount2)).setOutcome("PA");
									pitcher1.setOutcome("PA");
									lineupCount2++;
									if(lineupCount2 == 9) {
										lineupCount2 = 0;
									}
									strikes = 0;
									balls = 0;
								}
							}
						}
						else if(battedBall.getType().equals("Popup")) {
							outcome = hitOutcome(battedBall, lineup1, lineup2.get(index2.get(lineupCount2)));
							if(outcome[0].equals("Caught")) {
								System.out.println(lineup2.get(index2.get(lineupCount2)).getName() + " pops out!");
								lineup2.get(index2.get(lineupCount2)).setOutcome("PO");
								pitcher1.setOutcome("PO");
								lineup2.get(index2.get(lineupCount2)).setOutcome("PA");
								pitcher1.setOutcome("PA");
								outs++;
								lineupCount2++;
								balls = 0;
								strikes = 0;
								if(lineupCount2 == 9) {
									lineupCount2 = 0;
								}
								if(outs == 3) {
									System.out.println("~~~~~~~~~~~~~~~~ bottom of the inning ~~~~~~~~~~~~~~~~~");
									outs = 0;							
									top = !top;
								}
								continue;
							}
						}
					}
					//no contact
					else {
						//check for wild pitch						
						if(r.nextInt(100) > 99 - (Math.abs(10-location[0])/4) - (Math.abs(13-location[1])/4) + scale(normalizeRating(lineup2.get("C").getHands()), 1, 60, 2, 20)) {
							if(location[0] < -24 || location[0] > 44 || location[1] > 62 || location[1] < -24) {
								System.out.println("Wild pitch!");
							}
							else {
								System.out.println("Passed ball!");
							}
							if(!third[0].equals("")) {
								System.out.println(third[0] + " scores!");
							}
							third = second;
							second = first;
							first[0] = "";
							first[1] = "";
						}
						strikes++;
						System.out.println("swinging strike " + strikes);
						if(strikes == 3) {
							System.out.println(lineup2.get(index2.get(lineupCount2)).getName() + " strikes out!");
							lineup2.get(index2.get(lineupCount2)).setOutcome("K");
							pitcher1.setOutcome("K");
							lineup2.get(index2.get(lineupCount2)).setOutcome("PA");
							pitcher1.setOutcome("PA");
							outs++;
							lineupCount2++;
							balls = 0;
							strikes = 0;
							if(lineupCount2 == 9) {
								lineupCount2 = 0;
							}
							if(outs == 3) {
								System.out.println("~~~~~~~~~~~~~~~~ bottom of the inning ~~~~~~~~~~~~~~~~~");
								outs = 0;							
								top = !top;
							}
						}
						continue;
					}
				}
				//no swing
				else {
					//check for wild pitch
					if(r.nextInt(100) > 99 - (Math.abs(10-location[0])/4) - (Math.abs(13-location[1])/4) + scale(normalizeRating(lineup2.get("C").getHands()), 1, 60, 2, 20)) {
						if(location[0] < -24 || location[0] > 44 || location[1] > 62) {
							System.out.println("Wild pitch!");
						}
						else {
							System.out.println("Passed ball!");
						}
						if(!third[0].equals("")) {
							System.out.println(third[0] + " scores!");
						}
						third = second;
						second = first;
						first[0] = "";
						first[1] = "";
					}
					if(location[0] < 0 || location[0] > 20 || location[1] < 0 || location[1] > 26) {
						balls++;
						System.out.println("ball " + balls);
						if(balls == 4) {
							System.out.println(lineup2.get(index2.get(lineupCount2)).getName() + " walks!");	
							lineup2.get(index2.get(lineupCount2)).setOutcome("BB");
							pitcher1.setOutcome("BB");
							lineup2.get(index2.get(lineupCount2)).setOutcome("PA");
							pitcher1.setOutcome("PA");
							lineupCount2++;
							if(lineupCount2 == 9) {
								lineupCount2 = 0;
							}
							balls = 0;
							strikes = 0;
						}
						continue;
					}
					else {
						strikes++;
						System.out.println("strike " + strikes);
						if(strikes == 3) {
							System.out.println(lineup2.get(index2.get(lineupCount2)).getName() + " strikes out looking!");
							lineup2.get(index2.get(lineupCount2)).setOutcome("K");
							pitcher1.setOutcome("K");
							lineup2.get(index2.get(lineupCount2)).setOutcome("PA");
							pitcher1.setOutcome("PA");
							outs++;
							lineupCount2++;
							if(lineupCount2 == 9) {
								lineupCount2 = 0;
							}
							balls = 0;
							strikes = 0;
							if(outs == 3) {
								System.out.println("~~~~~~~~~~~~~~~~ bottom of the inning ~~~~~~~~~~~~~~~~~");
								outs = 0;							
								top = !top;
							}
						}
						continue;
					}
				}
			}			
			while(top == false) {				
				System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				pitcher2.printOut();
				lineup1.get(index1.get(lineupCount1)).printOut();
				//pickoff check
				if(!first[0].equals("")) {
					int p = (int) Math.round(Math.random()*10);
					if(p == 5) {
						int pick = (int) Math.round(Math.random()*100);
						if(pick > (49 - (normalizeRating(pitcher2.getPickoff())/4) + (normalizeRating(lineup2.get(first[1]).getSpeed())/4) - (team2.getStealing()/2))
						   && pick < (51 + (normalizeRating(pitcher2.getPickoff())/4) - (normalizeRating(lineup2.get(first[1]).getSpeed()))/4) + (team2.getStealing()/2)) {		
							System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " gets picked off!");
							outs++;
							lineupCount1++;
							if(lineupCount1 == 9) {
								lineupCount1 = 0;
							}
							if(outs == 3) {
								outs = 0;							
								top = !top;
								System.out.println("~~~~~~~~~~~~~~~~ top of the inning ~~~~~~~~~~~~~~~~~");
								continue;
							}
						}
					}
				}
				//simulate at-bat
				String pitch = pitcher2.getPitch(balls, strikes, lineup1.get(index1.get(lineupCount1)));
				Integer[] location = getLocation(pitch).clone();
				Random r = new Random();
				int xMiss = (int) Math.round(r.nextGaussian()*(1200/pitcher2.getControl()));						
				int yMiss = (int) Math.round(r.nextGaussian()*(1200/pitcher2.getControl()));
				if(pitch.equals("4-seamer") || pitch.equals("2-seamer") || pitch.equals("Cutter") || pitch.equals("Sinker")) {
					xMiss = (int) Math.round(xMiss*0.67);
					yMiss = (int) Math.round(yMiss*0.67);
				}
				location[0] = location[0] + xMiss;
				location[1] = location[1] + yMiss;
				if(pitcher2.getHand().equals("R")) {
					if(lineup1.get(index1.get(lineupCount1)).getHand().equals("R") || lineup1.get(index1.get(lineupCount1)).getHand().equals("SL")) {
						if((location[0] > 40 && location[0] < 52) && (location[1] > -5 && location[1] < 40)) {
							if(r.nextInt(100) > 40) {
								System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " gets hit by a pitch!");
								lineupCount1++;
								if(lineupCount1 == 9) {
									lineupCount1 = 0;
								}
								first[0] = lineup1.get(index1.get(lineupCount1)).getName();
								first[1] = lineup1.get(index1.get(lineupCount1)).getPosition();
								balls = 0;
								strikes = 0;
								continue;
							}
						}
					}
				}
				else if(pitcher2.getHand().equals("L")) {
					if(lineup1.get(index1.get(lineupCount1)).getHand().equals("L") || lineup1.get(index1.get(lineupCount1)).getHand().equals("SR")) {
						if((location[0]< -20 && location[0] > -32) && (location[1] > -5 && location[1] < 40)) {
							if(r.nextInt(100) > 40) {
								System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " gets hit by a pitch!");
								lineupCount1++;
								if(lineupCount1 == 9) {
									lineupCount1 = 0;
								}
								first[0] = lineup1.get(index1.get(lineupCount1)).getName();
								first[1] = lineup1.get(index1.get(lineupCount1)).getPosition();
								continue;
							}
						}
					}
				}
				System.out.println("xMiss: " + xMiss + " yMiss: " + yMiss);
				System.out.println(pitch + " | x: " + location[0] + " y: " + location[1]);
				//roll for swing
				int threshold = 30;
				boolean ball = false;
				if(location[0] < 0) {
					threshold = 30 + (location[0]);
					ball = true;
				}
				if(location[1] < 0) {
					threshold = threshold + (location[1]);
				}
				if(location[0] > 20) {
					threshold = threshold - ((location[0]-20));
					ball = true;
				}
				if(location[1] > 26) {
					threshold = threshold - ((location[1]-26));
					ball = true;
				}	
				else if(location[0] < 8 || location[0] > 12 || location[1] < 10 || location[1] > 16) {
					threshold = 60;
				}
				else {
					threshold = 80;
				}
				if(strikes == 2) {
					threshold = threshold + 20;
				}
				if(balls == 3 && strikes < 2) {
					threshold = threshold - 20;
				}
				if(!first[0].equals("") && second[0].equals("")) {
					int steal = (int) Math.round(Math.random()*100);
					if(steal > (45 + scale(normalizeRating(pitcher2.getPickoff()), 1, 60, -5, 5) + scale(normalizeRating(lineup1.get("C").getArm()), 1, 60, -5, 5) - scale(normalizeRating(lineup1.get(first[1]).getSpeed()), 1, 60, -10, 10) - (team1.getStealing()/2))
					   && steal < (55 - scale(normalizeRating(pitcher2.getPickoff()), 1, 60, -5, 5) - scale(normalizeRating(lineup1.get("C").getArm()), 1, 60, -5, 5) + scale(normalizeRating(lineup1.get(first[1]).getSpeed()), 1, 60, -10, 10) + (team1.getStealing()/2))) {	
						if(r.nextInt(100) > 70 - scale(normalizeRating(pitcher2.getPickoff()), 1, 60, -5, 5) - scale(normalizeRating(lineup1.get("C").getArm()), 1, 60, -5, 5) + scale(normalizeRating(lineup1.get(first[1]).getSpeed()), 1, 60, -10, 10)) {
							System.out.println(first[0] + " gets caught stealing, thrown out by " + lineup1.get("C").getName() + "!");
							outs++;
							first[0] = "";
							first[1] = "";	
							if(outs == 3) {
								System.out.println("~~~~~~~~~~~~~~~~ top of the inning ~~~~~~~~~~~~~~~~~");
								outs = 0;	
								balls = 0;
								strikes = 0;
								top = !top;
								continue;
							}
						}		
						else { 
							System.out.println(first[0] + " steals second!");
							second = first;
							first[0] = "";
							first[1] = "";
						}
					}
				}
				else if(!first[0].equals("") && !second[0].equals("")) {
					int steal = (int) Math.round(Math.random()*100);
					if(steal > (48 + scale(normalizeRating(pitcher2.getPickoff()), 1, 60, -5, 5) + scale(normalizeRating(lineup1.get("C").getArm()), 1, 60, -5, 5) - scale(normalizeRating(lineup1.get(second[1]).getSpeed()), 1, 60, -10, 10) - (team1.getStealing()/2))
							&& steal < (52 - scale(normalizeRating(pitcher2.getPickoff()), 1, 60, -5, 5) - scale(normalizeRating(lineup1.get("C").getArm()), 1, 60, -5, 5) + scale(normalizeRating(lineup1.get(second[1]).getSpeed()), 1, 60, -10, 10) + (team1.getStealing()/2))) {	
						if(r.nextInt(100) > 80 - scale(normalizeRating(pitcher2.getPickoff()), 1, 60, -5, 5) - scale(normalizeRating(lineup1.get("C").getArm()), 1, 60, -5, 5) + scale(normalizeRating(lineup1.get(second[1]).getSpeed()), 1, 60, -10, 10)) {
							System.out.println("Double steal! " + second[0] + " gets caught stealing, thrown out by " + lineup1.get("C").getName() + "!");
							outs++;
							second = first;
							first[0] = "";
							first[1] = "";
							if(outs == 3) {
								System.out.println("~~~~~~~~~~~~~~~~ top of the inning ~~~~~~~~~~~~~~~~~");
								outs = 0;	
								balls = 0;
								strikes = 0;
								top = !top;								
								continue;
							}
						}		
						else { 
							System.out.println("Double steal! " + second[0] + " steals second!");
							third = second;
							second = first;
							first[0] = "";
							first[1] = "";
						}
					}
				}
				int s = (int) Math.round(Math.random() * 100);
				int recogRating = platoonEffect(normalizeRating(lineup1.get(index1.get(lineupCount1)).getRecognition()), 
						lineup1.get(index1.get(lineupCount1)), pitcher2, pitch, "recognition");
				int deceptRating = normalizeRating(pitcher2.getDeception());
				int veloRating = normalizeRating(pitcher2.getVelocity());
				int moveRating = normalizeRating(pitcher2.getMovement());
				int aggressRating = normalizeRating(lineup1.get(index1.get(lineupCount1)).getAggression());					
				boolean swung = false;
				if(ball == true) {
					if(s <= threshold - (scale(recogRating, 1, 60, -20, 30)) + (scale(deceptRating, 1, 60, -5, 5)) 
					   + (scale(veloRating, 1, 60, -5, 5)) + (scale(moveRating, 1, 60, -15, 15)) + (scale(aggressRating, 2, 57, -6, 6))) {
						swung = true;
					}
				}
				else {
					if(s <= threshold + (scale(recogRating, 1, 60, -20, 30)) - (scale(deceptRating, 1, 60, -5, 5)) 
					   - (scale(veloRating, 1, 60, -5, 5)) - (scale(moveRating, 1, 60, -15, 15)) + (scale(aggressRating, 2, 57, -6, 6))) {
						swung = true;
					}
				}
				if(swung == true) {
					//swung, contact roll
					int c = (int) Math.round(Math.random() * 100);
					int contactSplit = platoonEffect(lineup1.get(index1.get(lineupCount1)).getContact(), 
							lineup1.get(index1.get(lineupCount1)), pitcher2, pitch, "contact");
					int recogSplit = platoonEffect(normalizeRating(lineup1.get(index1.get(lineupCount1)).getRecognition()), 
							lineup1.get(index1.get(lineupCount1)), pitcher2, pitch, "recognition");
					int powerSplit = platoonEffect(normalizeRating(lineup1.get(index1.get(lineupCount1)).getPower()),
							lineup1.get(index1.get(lineupCount1)), pitcher2, pitch, "batspeed");
					int normVelocity = normalizeRating(pitcher2.getVelocity());
					int normMovement = normalizeRating(pitcher2.getMovement());
					int contact = 80 - Math.abs(10-location[0]) - Math.abs(13-location[1]);
					boolean caught = false;
					boolean hr = false;
					String[] outcome = new String[2];
					if(pitch.equals("Slider") || pitch.equals("Changeup") || pitch.equals("Screwball") || pitch.equals("Splitter")) {
						contact = contact - 5;
					}
					else if(pitch.equals("Curveball") || pitch.equals("4-seamer") || pitch.equals("Knuckleball")) {
						contact = contact - 3;
					}
					if(c <= contact + scale(contactSplit, 1, 99, -50, 50) + (scale(recogSplit, 1, 60, -5, 5)) - (scale(normVelocity, 1, 60, -20, 20))
					   - (scale(normMovement, 1, 60, -15, 15)) - (pitcher1.getPitchSize()-1)*3) {
						BattedBall battedBall = outCome(pitch, location, pitcher2, lineup1.get(index1.get(lineupCount1)));
						//foul
						if(battedBall.getFoul() == true) {
							if(battedBall.getType().equals("Groundball")) {
								System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " hits it foul!");
								if(strikes < 2) {
									strikes++;
								}
								continue;
							}
							//foul
							else if(battedBall.getType().equals("Popup")) {
								caught = foulOutcome(battedBall, lineup1, team1);
								if(caught == false) {
									System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " hits it foul!");
									if(strikes < 2) {
										strikes++;
									}
									continue;
								}
								else {
									System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " fouls out!");
									lineup1.get(index1.get(lineupCount1)).setOutcome("FO");
									pitcher2.setOutcome("FO");
									lineup1.get(index1.get(lineupCount1)).setOutcome("PA");
									pitcher2.setOutcome("PA");
									outs++;
									lineupCount1++;
									balls = 0;
									strikes = 0;
									if(lineupCount1 == 9) {
										lineupCount1 = 0;
									}
									if(outs == 3) {
										System.out.println("~~~~~~~~~~~~~~~~ top of the inning ~~~~~~~~~~~~~~~~~");
										outs = 0;							
										top = !top;
									}
									continue;
								}
							}
							//foul
							else if(battedBall.getType().equals("Flyball")){
								caught = foulOutcome(battedBall, lineup1, team1);
								if(caught == false) {
									System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " hits it foul!");
									if(strikes < 2) {
										strikes++;
									}
									continue;
								}
								else {
									System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " fouls out!");
									lineup1.get(index1.get(lineupCount1)).setOutcome("FO");
									pitcher2.setOutcome("FO");
									lineup1.get(index1.get(lineupCount1)).setOutcome("PA");
									pitcher2.setOutcome("PA");
									outs++;										
									lineupCount1++;
									balls = 0;
									strikes = 0;
									if(lineupCount1 == 9) {
										lineupCount1 = 0;
									}
									if(outs == 3) {
										System.out.println("~~~~~~~~~~~~~~~~ top of the inning ~~~~~~~~~~~~~~~~~");
										outs = 0;							
										top = !top;
									}
									continue;
								}
							}
						}
						//groundball
						else if(battedBall.getType().equals("Groundball")) {
							outcome = hitOutcome(battedBall, lineup2, lineup1.get(index1.get(lineupCount1)));
							if(outcome[0].equals("Caught")) {
								System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " grounds out!");
								lineup1.get(index1.get(lineupCount1)).setOutcome("GO");
								pitcher2.setOutcome("GO");
								lineup1.get(index1.get(lineupCount1)).setOutcome("PA");
								pitcher2.setOutcome("PA");
								outs++;
								lineupCount1++;
								balls = 0;
								strikes = 0;
								if(lineupCount1 == 9) {
									lineupCount1 = 0;
								}
								if(outs == 3) {
									System.out.println("~~~~~~~~~~~~~~~~ top of the inning ~~~~~~~~~~~~~~~~~");
									outs = 0;							
									top = !top;
								}
								continue;
							}
							else {
								System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " hits a single!");
								first[0] = lineup1.get(index1.get(lineupCount1)).getName();
								first[1] = lineup1.get(index1.get(lineupCount1)).getPosition();
								lineupCount1++;
								if(lineupCount1 == 9) {
									lineupCount1 = 0;
								}
								balls = 0;
								strikes = 0;
							}
						}						
						else if(battedBall.getType().equals("Flyball")) {
							hr = checkHR(battedBall, lineup1.get(index1.get(lineupCount1)));						
							if(hr == true) {
								lineup1.get(index1.get(lineupCount1)).setOutcome("HR");
								pitcher2.setOutcome("HR");
								lineup1.get(index1.get(lineupCount1)).setOutcome("PA");
								pitcher2.setOutcome("PA");
								lineupCount1++;
								if(lineupCount1 == 9) {
									lineupCount1 = 0;
								}
								strikes = 0;
								balls = 0;
							}
							else {
								outcome = hitOutcome(battedBall, lineup2, lineup1.get(index1.get(lineupCount1)));
								if(outcome[0].equals("Caught")) {
									System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " flies out!");
									lineup1.get(index1.get(lineupCount1)).setOutcome("FO");
									pitcher2.setOutcome("FO");
									lineup1.get(index1.get(lineupCount1)).setOutcome("PA");
									pitcher2.setOutcome("PA");
									outs++;
									lineupCount1++;
									balls = 0;
									strikes = 0;
									if(lineupCount1 == 9) {
										lineupCount1 = 0;
									}
									if(outs == 3) {
										System.out.println("~~~~~~~~~~~~~~~~ top of the inning ~~~~~~~~~~~~~~~~~");
										outs = 0;							
										top = !top;
									}
									continue;
								}
								else if(outcome[0].equals("1B")) {
									System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " hits a single!");
									lineup1.get(index1.get(lineupCount1)).setOutcome("1B");
									pitcher2.setOutcome("1B");
									lineup1.get(index1.get(lineupCount1)).setOutcome("PA");
									pitcher2.setOutcome("PA");
									baseRunning(lineup1, lineup2, index1, lineupCount1, outcome);
									lineupCount1++;
									if(lineupCount1 == 9) {
										lineupCount1 = 0;
									}
								}
								else if(outcome[0].equals("2B")) {
									System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " hits a double!");
									lineup1.get(index1.get(lineupCount1)).setOutcome("2B");
									pitcher2.setOutcome("2B");
									lineup1.get(index1.get(lineupCount1)).setOutcome("PA");
									pitcher2.setOutcome("PA");
									baseRunning(lineup1, lineup2, index1, lineupCount1, outcome);
									lineupCount1++;
									if(lineupCount1 == 9) {
										lineupCount1 = 0;
									}
								}
								else if(outcome[0].equals("3B")) {
									System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " hits a triple!");
									lineup1.get(index1.get(lineupCount1)).setOutcome("3B");
									pitcher2.setOutcome("3B");
									lineup1.get(index1.get(lineupCount1)).setOutcome("PA");
									pitcher2.setOutcome("PA");
									baseRunning(lineup1, lineup2, index1, lineupCount1, outcome);
									lineupCount1++;
									if(lineupCount1 == 9) {
										lineupCount1 = 0;
									}
								}
							}
						}
						else if(battedBall.getType().equals("Liner")) {
							hr = checkHR(battedBall, lineup1.get(index1.get(lineupCount1)));
							if(hr == true) {
								lineup1.get(index1.get(lineupCount1)).setOutcome("HR");
								pitcher2.setOutcome("HR");
								lineup1.get(index1.get(lineupCount1)).setOutcome("PA");
								pitcher2.setOutcome("PA");
								lineupCount1++;
								if(lineupCount1 == 9) {
									lineupCount1 = 0;
								}
								strikes = 0;
								balls = 0;
							}
							else {
								outcome = hitOutcome(battedBall, lineup2, lineup1.get(index1.get(lineupCount1)));
								if(outcome[0].equals("Caught")) {
									System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " lines out!");
									lineup1.get(index1.get(lineupCount1)).setOutcome("LO");
									lineup1.get(index1.get(lineupCount1)).setOutcome("PA");
									pitcher2.setOutcome("PA");
									pitcher2.setOutcome("LO");
									outs++;
									lineupCount1++;
									balls = 0;
									strikes = 0;
									if(lineupCount1 == 9) {
										lineupCount1 = 0;
									}
									if(outs == 3) {
										System.out.println("~~~~~~~~~~~~~~~~ top of the inning ~~~~~~~~~~~~~~~~~");
										outs = 0;							
										top = !top;
									}
									continue;
								}
								else if(outcome[0].equals("1B")) {
									System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " hits a single!");
									lineup1.get(index1.get(lineupCount1)).setOutcome("1B");
									pitcher2.setOutcome("1B");
									lineup1.get(index1.get(lineupCount1)).setOutcome("PA");
									pitcher2.setOutcome("PA");
									baseRunning(lineup1, lineup2, index1, lineupCount1, outcome);
									lineupCount1++;
									if(lineupCount1 == 9) {
										lineupCount1 = 0;
									}
								}
								else if(outcome[0].equals("2B")) {
									System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " hits a double!");
									lineup1.get(index1.get(lineupCount1)).setOutcome("2B");
									pitcher2.setOutcome("2B");
									lineup1.get(index1.get(lineupCount1)).setOutcome("PA");
									pitcher2.setOutcome("PA");
									baseRunning(lineup1, lineup2, index1, lineupCount1, outcome);
									lineupCount1++;
									if(lineupCount1 == 9) {
										lineupCount1 = 0;
									}
								}
								else if(outcome[0].equals("3B")) {
									System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " hits a triple!");
									lineup1.get(index1.get(lineupCount1)).setOutcome("3B");
									pitcher2.setOutcome("3B");
									lineup1.get(index1.get(lineupCount1)).setOutcome("PA");
									pitcher2.setOutcome("PA");
									baseRunning(lineup1, lineup2, index1, lineupCount1, outcome);
									lineupCount1++;
									if(lineupCount1 == 9) {
										lineupCount1 = 0;
									}
								}
							}
						}
						else if(battedBall.getType().equals("Popup")) {
							outcome = hitOutcome(battedBall, lineup2, lineup1.get(index1.get(lineupCount1)));
							if(outcome[0].equals("Caught")) {
								System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " pops out!");
								lineup1.get(index1.get(lineupCount1)).setOutcome("PO");
								pitcher2.setOutcome("PO");
								lineup1.get(index1.get(lineupCount1)).setOutcome("PA");
								pitcher2.setOutcome("PA");
								outs++;
								lineupCount1++;
								balls = 0;
								strikes = 0;
								if(lineupCount1 == 9) {
									lineupCount1 = 0;
								}
								if(outs == 3) {
									System.out.println("~~~~~~~~~~~~~~~~ top of the inning ~~~~~~~~~~~~~~~~~");
									outs = 0;							
									top = !top;
								}
								continue;
							}
						}
					}
					//no contact
					else {
						//check for wild pitch
						if(r.nextInt(100) > 99 - (Math.abs(10-location[0])/4) - (Math.abs(13-location[1])/4) + scale(normalizeRating(lineup1.get("C").getHands()), 1, 60, 2, 20)) {
							if(location[0] < -24 || location[0] > 44 || location[1] > 62) {
								System.out.println("Wild pitch!");
							}
							else {
								System.out.println("Passed ball!");
							}
							if(!third[0].equals("")) {
								System.out.println(third[0] + " scores!");
							}
							third = second;
							second = first;
							first[0] = "";
							first[1] = "";
						}
						strikes++;
						System.out.println("swinging strike " + strikes);
						if(strikes == 3) {
							System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " strikes out!");
							lineup1.get(index1.get(lineupCount1)).setOutcome("K");
							pitcher2.setOutcome("K");
							lineup1.get(index1.get(lineupCount1)).setOutcome("PA");
							pitcher2.setOutcome("PA");
							outs++;
							lineupCount1++;
							if(lineupCount1 == 9) {
								lineupCount1 = 0;
							}
							balls = 0;
							strikes = 0;
							if(outs == 3) {
								System.out.println("~~~~~~~~~~~~~~~~ top of the inning ~~~~~~~~~~~~~~~~~");
								outs = 0;							
								top = !top;
							}
						}
						continue;
					}
				}
				//no swing
				else {
					//check for wild pitch
					if(r.nextInt(100) > 99 - (Math.abs(10-location[0])/4) - (Math.abs(13-location[1])/4) + scale(normalizeRating(lineup1.get("C").getHands()), 1, 60, 2, 20)) {
						if(location[0] < -24 || location[0] > 44 || location[1] > 62) {
							System.out.println("Wild pitch!");
						}
						else {
							System.out.println("Passed ball!");
						}
						if(!third[0].equals("")) {
							System.out.println(third[0] + " scores!");
						}
						third = second;
						second = first;
						first[0] = "";
						first[1] = "";
					}
					if(location[0] < 0 || location[0] > 20 || location[1] < 0 || location[1] > 26) {
						balls++;
						System.out.println("ball " + balls);						
						if(balls == 4) {
							System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " walks!");	
							lineup1.get(index1.get(lineupCount1)).setOutcome("BB");
							pitcher2.setOutcome("BB");
							lineup1.get(index1.get(lineupCount1)).setOutcome("PA");
							pitcher2.setOutcome("PA");
							pitcher2.setOutcome("Innings");
							lineupCount1++;
							if(lineupCount1 == 9) {
								lineupCount1 = 0;
							}							
							balls = 0;
							strikes = 0;
							if(outs == 3) {
								System.out.println("~~~~~~~~~~~~~~~~ top of the inning ~~~~~~~~~~~~~~~~~");
								outs = 0;							
								top = !top;
							}
						}
						continue;
					}
					else {
						strikes++;
						System.out.println("strike " + strikes);
						if(strikes == 3) {
							System.out.println(lineup1.get(index1.get(lineupCount1)).getName() + " strikes out looking!");
							lineup1.get(index1.get(lineupCount1)).setOutcome("K");
							pitcher2.setOutcome("K");
							lineup1.get(index1.get(lineupCount1)).setOutcome("PA");
							pitcher2.setOutcome("PA");
							pitcher2.setOutcome("Innings");
							outs++;							
							lineupCount1++;
							if(lineupCount1 == 9) {
								lineupCount1 = 0;
							}							
							balls = 0;
							strikes = 0;
							if(outs == 3) {
								System.out.println("~~~~~~~~~~~~~~~~ top of the inning ~~~~~~~~~~~~~~~~~");
								outs = 0;							
								top = !top;
							}
						}
						continue;
					}
				}
			}
		}			
	}
}