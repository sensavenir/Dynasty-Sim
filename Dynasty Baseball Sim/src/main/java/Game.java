import java.util.ArrayList;
import java.util.Random;

public class Game {
	private int id;
	private int outs = 0;	
	private boolean top = true;
	int lineupCount1 = 1;
	int lineupCount2 = 1;
	private Pitcher pitcher1;
	private Pitcher pitcher2;
	private ArrayList<Player> lineup1 = new ArrayList<Player>();
	private ArrayList<Player> lineup2 = new ArrayList<Player>();
	private String first = "";
	private String second = "";
	private String third = "";
	private int strikes = 0;
	private int balls = 0;
	String[] fourSeamer = {"4-seamer", "35", "21", "34", "10"};
	String[] twoSeamer = {"2-seamer", "46", "19", "27", "8"};
	String[] Cutter = {"Cutter", "44", "20", "27", "9"};
	String[] Sinker = {"Sinker", "59 ", "17", "19", "5"};
	String[] Slider = {"Slider", "45" , "17" ,"29", "9"};
	String[] Curveball = {"Curveball", "50", "19", "25", "6"};
	String[] Changeup = {"Changeup", "49" , "18" , "26" ,"7"}; 
	String[] Splitter = {"Splitter", "53" , "17" , "24" ,"6"};
	String[] Knucklecurve = {"Knuckle curve", "53" , "17" , "24" ,"6"};
	String[] Knuckleball = {"Knuckleball", "43" , "17" , "30" ,"10"}; 

	//generate batted ball outcome
	private String[] outCome(String pitch, Integer[] location, Pitcher pitcher, Player hitter) {
		String[] battedBall = new String[4];		
		int batSpeed = normalizeRating(hitter.getPower());
		int swing = normalizeRating(hitter.getSwing());
		int contact = normalizeRating(hitter.getContact());
		int velocity = normalizeRating(pitcher1.getVelocity());
		int movement = normalizeRating(pitcher1.getMovement());		
		//roll for exit velocity, batted ball type and azimuth
		Random d = new Random();			
		double exitVelocity = d.nextGaussian()*7 + (scale(batSpeed, 1, 60, 80, 94)) + (scale(contact, 1, 60, 0, 5));
		int azimuth = 0;
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
		for(int i = 0; i < 10; i++) {
			if(pitch.equals(bbPitch[i][0])) {				
				int count = 99 - Integer.parseInt(bbPitch[i][1]) + (location[1] - 13) + (scale(swing, 1, 60, 0, 26) - 13);				
				if(bb > count) {
					bbType = "Groundball";
					break;
				}					
				count = count - Integer.parseInt(bbPitch[i][2]);	
				if(bb > count) {
					bbType = "Liner";
					break;
				}
				count = count - Integer.parseInt(bbPitch[i][3]) - (int) Math.round((((double) location[1] - 13)*0.8)) - (int) Math.round(((scale(swing, 1, 60, 0, 26) - 13)*0.8));
				if(bb > count) {
					bbType = "Flyball";
					break;
				}
				if(bb > count) {
					bbType = "Popup";
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
		battedBall[0] = bbType;
		battedBall[1] = Double.toString(exitVelocity);
		battedBall[2] = Integer.toString(azimuth);	
		//foul
		int foulThresh = 31 + (scale(velocity, 1, 60, 0, 10) - 5);
		if(pitch.equals("4-seamer")) {
			foulThresh = foulThresh + 13 - (scale(batSpeed, 1, 60, 0, 20) - 10);
		}
		else if(pitch.equals("2-seamer") || pitch.equals("Cutter") || pitch.equals("Slider")) {
			foulThresh = foulThresh + 8 - (scale(batSpeed, 1, 60, 0, 16) - 8);
		}
		int f = (int) Math.round(Math.random()*100);
		if(f <= foulThresh) { 
			battedBall[3] = "Foul";
		}
		return battedBall;
	}
	
	//generate foul outcome
	private boolean foulOutcome(String[] bb, ArrayList<Player> lineup, String pos) {
		return true;
	}
	
	//generate hit outcome
	private String hitOutcome(String[] bb, ArrayList<Player> lineup, String pos) {
		String hit = "";
		int distance = (int) Math.round(Math.pow(Double.parseDouble(bb[1]), 2) * Math.sin(2*Double.parseDouble(bb[2])));
		int time = (int) Math.round(distance/(Double.parseDouble(bb[1])*Math.cos(distance)));
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
				if(!pitch.equals("Splitter") && !pitch.equals("Changeup") && !pitch.equals("Curveball") && !pitch.equals("Knuckle curve") 
				   && !pitch.equals("Knuckleball") && !type.equals("recognition")) {
					rating = rating + ((Math.abs(hitter.getSplit()) + pitcher.getSplit())/2);
				}
			}
		}
		else if(hitter.getHand().equals("SL")) {
			if(pitcher.getHand().equals("R")) {
				if(!pitch.equals("Splitter") && !pitch.equals("Changeup") && !pitch.equals("Curveball") && !pitch.equals("Knuckle curve")
				   && !pitch.equals("Knuckleball") && !type.equals("recognition")) {
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
				if(!pitch.equals("Splitter") && !pitch.equals("Changeup") && !pitch.equals("Curveball") && !pitch.equals("Knuckle curve")
				   && !pitch.equals("Knuckleball") && !type.equals("recognition")) {
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
	public int scale(int value,  int min, int max, int finalMin, int finalMax) {
		return ((finalMax - finalMin) * (value - min) / (max - min)) + finalMin;
	}

	public Game(Team team1, Team team2) {
		//import starters and lineups
		pitcher1 = team1.getStarter();
		pitcher2 = team2.getStarter();
		if(pitcher1.getHand().equals("R")) {
			lineup2 = (ArrayList<Player>) team2.getRHP().clone();
		}
		else {
			lineup2 = (ArrayList<Player>) team2.getLHP().clone();
		}
		if(pitcher2.getHand().equals("R")) {
			lineup1 = (ArrayList<Player>) team1.getRHP().clone();
		}
		else {
			lineup1 = (ArrayList<Player>) team1.getLHP().clone();
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
				String pitch = pitcher1.getPitch();
				Integer[] location = getLocation(pitch).clone();
				Random r = new Random();
				int xMiss = (int) Math.round(r.nextGaussian()*(120/normalizeRating(pitcher1.getControl())));						
				int yMiss = (int) Math.round(r.nextGaussian()*(120/normalizeRating(pitcher1.getControl())));
				location[0] = location[0] + xMiss;
				location[1] = location[1] + yMiss;
				System.out.println(pitch + " | x: " + location[0] + " y: " + location[1]);
				//roll for swing
				int oThreshold = 30;
				int zThreshold = 60;
				if(strikes > balls) {
					oThreshold = 40;
					zThreshold = 70;
				}
				if(strikes == 2) {
					oThreshold = 50;
					zThreshold = 80;
				}
				//out of the zone
				if(location[0] < 0 || location[0] > 20 || location[1] < 0 || location[1] > 26) {
					int s = (int) Math.round(Math.random() * 100);
					//swing roll			
					int splitRating = platoonEffect(normalizeRating(lineup2.get(lineupCount2).getRecognition()), 
									  lineup2.get(lineupCount2), pitcher1, pitch, "recognition");
					int deceptRating = normalizeRating(pitcher1.getDeception());
					int aggressRating = normalizeRating(lineup2.get(lineupCount2).getAggression());					
					if(s <= oThreshold - (scale(splitRating, 1, 60, 0, 22) - 11) + (scale(deceptRating, 1, 60, 0, 14 - 7)) 
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
						int contactThreshold = 60;
						boolean caught = false;
						String outcome = "";						
						if(pitch.equals("Sinker")) {
							contactThreshold+=5;
						}
						else if(pitch.equals("Slider") || pitch.equals("Changeup") || pitch.equals("Knuckle curve")) {
							contactThreshold-=5;
						}
						else if(pitch.equals("Cutter") || pitch.equals("Curveball") || pitch.equals("4-seamer")) {
							contactThreshold-=3;
						}
						if(c <= contactThreshold + ((scale(contactSplit, 1, 60, 0, 36) - 18)) + (scale(recogSplit, 1, 60, 0, 14 - 7)) 
						   + (scale(powerSplit, 1, 60, 0, 14 - 7)) - (scale(normVelocity, 1, 60, 0, 14 - 7)) - (scale(normMovement, 1, 60, 0, 14 - 7)) 
						   - (int)Math.round((Math.abs(location[0] - 10)/2)) - (int)Math.round((Math.abs(location[1] - 10)/2))) {
							String[] battedBall = new String[4];
							battedBall = outCome(pitch, location, pitcher1, lineup2.get(lineupCount2)).clone();
							if(battedBall[4].equals("Foul")) {
								if(battedBall[0].equals("Groundball")) {
									if(strikes < 2) {
										strikes++;
									}
									continue;
								}
								else if(battedBall[0].equals("Popup")){
									if((Integer.parseInt(battedBall[2]) < 3 || Integer.parseInt(battedBall[3]) > 27)) {
										if(Integer.parseInt(battedBall[1]) < 90) {
											caught = foulOutcome(battedBall, lineup2, "C");
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
												continue;
											}
										}
										else if(Integer.parseInt(battedBall[2]) < 3) {
											caught = foulOutcome(battedBall, lineup2, "3B");
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
												continue;
											}											
										}
										else {
											caught = foulOutcome(battedBall, lineup2, "1B");
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
												continue;
											}
										}
									}
								}
								else if(battedBall[0].equals("Flyball")){
									if(Integer.parseInt(battedBall[2]) < 3) {
										caught = foulOutcome(battedBall, lineup2, "LF");
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
											continue;
										}
									}
									else if(Integer.parseInt(battedBall[3]) > 27) {
										caught = foulOutcome(battedBall, lineup2, "RF");
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
											continue;
										}
									}
								}							
							}
							else if(battedBall[0].equals("Groundball")) {
								if((Integer.parseInt(battedBall[2]) < 15)) {
									//need to implement SS/3B roll
									outcome = hitOutcome(battedBall, lineup2, "SS");
									if(outcome.equals("Caught")) {
										System.out.println(lineup2.get(lineupCount2).getName() + " grounds out!");
										outs++;
										lineupCount2++;
										balls = 0;
										strikes = 0;
										continue;
									}
									else {
										//hit, baserunning etc.
									}
								}
							}
							else if(battedBall[0].equals("Flyball")) {
								if((Integer.parseInt(battedBall[2]) < 15)) {
									outcome = hitOutcome(battedBall, lineup2, "LF");
									if(outcome.equals("Caught")) {
										System.out.println(lineup2.get(lineupCount2).getName() + " grounds out!");
										outs++;
										lineupCount2++;
										balls = 0;
										strikes = 0;
										continue;
									}
									else {
										//hit, baserunning etc.
									}
								}
							}
							else if(battedBall[0].equals("Popup")) {
								if((Integer.parseInt(battedBall[2]) < 15)) {
									outcome = hitOutcome(battedBall, lineup2, "3B");
									if(outcome.equals("Caught")) {
										System.out.println(lineup2.get(lineupCount2).getName() + " grounds out!");
										outs++;
										lineupCount2++;
										balls = 0;
										strikes = 0;
										continue;
									}
									else {
										//hit, baserunning etc.
									}
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