

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Pitcher {
	private String name;
	private String position = "P";
	private int ID;
	private int age;
	private int weight;
	private int height;
	private String nationality;
	private ArrayList<Team> team = new ArrayList<Team>();
	private ArrayList<Integer[]> actSalary = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> actVelocity = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> actMovement = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> actControl = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> actDeception = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> actPickoff = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> actReaction = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> actHands = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> actArm = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> actEndurance = new ArrayList<Integer[]>();
	private ArrayList<String> pitchTypes = new ArrayList<String>();
	private int potVelocity;
	private int potMovement;
	private int potControl;
	private int potDeception;
	private int potPickoff;
	private int potReaction;
	private int potHands;
	private int potArm;
	private int potEndurance;
	private int development;
	private int decline;
	private int peak;
	private int greed;
	private int handedness;
	private int mphVelocity;
	private String batting;
	private String throwing;
	private int scoutability;
	private double ops;
	HashMap <String, Double> outcomes = new HashMap<String, Double>();
	
	public String getHand() {
		return this.throwing;
	}
	
	public int getSplit() {
		return this.handedness;
	}
	
	public int getVelocity() {
		return this.potVelocity;
	}
	
	public double getVelo() {		
		return this.mphVelocity;
	}
	
	public int getMovement() {
		return this.potMovement;
	}
	
	public int getControl() {
		return this.potControl;
	}
	
	public int getDeception() {
		return this.potDeception;
	}
	
	public int getPickoff() {
		return this.potPickoff;
	}
	
	public int getReaction() {
		return this.potReaction;
	}
	
	public int getHands() {
		return this.potHands;
	}
	
	public int getArm() {
		return this.potArm;
	}
	
	public int getPitchSize() {
		return this.pitchTypes.size();
	}
	
	public String getPitch(int balls, int strikes, Player hitter) {
		String pitch = "";
		if((this.getHand().equals("R") && (hitter.getHand().equals("L") || hitter.getHand().equals("SL")))
			|| (this.getHand().equals("L") && (hitter.getHand().equals("R") || hitter.getHand().equals("SR")))) {
			ArrayList<String> offSpeed = new ArrayList<String>();
			if(pitchTypes.contains("Changeup")) {
				offSpeed.add(pitchTypes.get(pitchTypes.indexOf("Changeup")));
			}
			if(pitchTypes.contains("Screwball")) {
				offSpeed.add(pitchTypes.get(pitchTypes.indexOf("Screwball")));
			}
			if(pitchTypes.contains("Curveball")) {
				offSpeed.add(pitchTypes.get(pitchTypes.indexOf("Curveball")));
			}
			if(pitchTypes.contains("Knuckleball")) {
				offSpeed.add(pitchTypes.get(pitchTypes.indexOf("Knuckleball")));
			}
			int k = (int) Math.random()*offSpeed.size()*2;
			if(k < offSpeed.size()) {
				pitch = offSpeed.get(k);
			}
		}	
		if(balls == 3 && strikes != 2 && pitch.equals("")) {
			ArrayList<String> fastBalls = new ArrayList<String>();
			if(pitchTypes.contains("4-seamer")) {
				fastBalls.add(pitchTypes.get(pitchTypes.indexOf("4-seamer")));
			}
			if(pitchTypes.contains("2-seamer")) {
				fastBalls.add(pitchTypes.get(pitchTypes.indexOf("2-seamer")));
			}
			if(pitchTypes.contains("Cutter")) {
				fastBalls.add(pitchTypes.get(pitchTypes.indexOf("Cutter")));
			}
			if(pitchTypes.contains("Sinker")) {
				fastBalls.add(pitchTypes.get(pitchTypes.indexOf("Sinker")));
			}
			int k = (int) Math.round(Math.random()*9);
			if(k < 7) {
				int f = (int) Math.round(Math.random()*(fastBalls.size()-1));
				pitch = fastBalls.get(f);
			}
		}
		else {
			int k = (int) Math.round(Math.random()*(pitchTypes.size()-1));
			if(k == -1) {
				k = 0;
			}
			pitch = pitchTypes.get(k);
		}
		if(pitchTypes.contains("Knuckleball")) {
			int k = (int) Math.random()*9;
			if(k < 9) {
				pitch = "Knuckleball";
			}
			else {
				pitch = "2-seamer";
			}
		}
		if(pitch.equals("")) {
			pitch = pitchTypes.get(0);
		}
		return pitch;
	}
	
	private int setPotential() {
		Random r = new Random();
		int tempRating = (int) Math.round((r.nextGaussian()*13)+70);
		if(tempRating > 99) {
			tempRating = 99;
		}
		if(tempRating < 1) {
			tempRating = 1;
		}
		return tempRating;
	}
	
	//calculate salary
	private int setSalary() {
		int salary = 0;
		return salary;
	}
	
	public void printOut() {
		System.out.println(name + ": pitcher" + " | " + this.throwing);
		System.out.println("PO " + potVelocity + " FI " + potMovement + " CO " + potControl  + " SY " + potDeception + " EN " + potEndurance + ", " + mphVelocity + " mph fastball");
		String pitches = pitchTypes.get(0) + ", ";
		for(int i = 1; i < pitchTypes.size(); i++) {
			pitches = pitches + pitchTypes.get(i);
			if(i != pitchTypes.size()-1) {
				pitches = pitches + ", ";
			}
		}
		System.out.println(pitches);
	}
	
	public double getOPS() {
		return this.ops;
	}
	
	public void printLine() {
		printOut();
		double innings = outcomes.get("PA")/3;
		System.out.println("Innings: " + innings);
		System.out.println("PA: " + outcomes.get("PA"));
		System.out.println("1B: " + outcomes.get("1B"));
		System.out.println("1B: " + outcomes.get("2B"));
		System.out.println("1B: " + outcomes.get("3B"));
		System.out.println("HR: " + outcomes.get("HR"));
		double k = (outcomes.get("K")/outcomes.get("PA"))*100;		
		System.out.println("K%: " + k);
		double bb = (outcomes.get("BB")/outcomes.get("PA"))*100;
		System.out.println("BB%: " + bb);
		double average = (outcomes.get("1B") + outcomes.get("2B") + outcomes.get("3B") + outcomes.get("HR"))/outcomes.get("PA");
		System.out.println("Average: " + average);
		double obp = (outcomes.get("1B") + outcomes.get("2B") + outcomes.get("3B") + outcomes.get("HR") + outcomes.get("BB"))/outcomes.get("PA");
		System.out.println("OBP: " + obp);
		double slg = (outcomes.get("1B") + (2*outcomes.get("2B")) + (3*outcomes.get("3B")) + (4*outcomes.get("HR")))/outcomes.get("PA");
		System.out.println("SLG: " + slg);
		this.ops = obp + slg;
		System.out.println("OPS: " + ops);
		double gofo = outcomes.get("GO")/outcomes.get("FO");		
		System.out.println("GB/FB: " + gofo);
	}
	
	public void setOutcome(String type) {
		outcomes.put(type, outcomes.get(type)+1);
	}
	
	//constructor
	public Pitcher () {
		outcomes.put("Innings", 0.0);
		outcomes.put("PA", 0.0);
		outcomes.put("1B", 0.0);
		outcomes.put("2B", 0.0);
		outcomes.put("3B", 0.0);
		outcomes.put("HR", 0.0);
		outcomes.put("BB", 0.0);
		outcomes.put("K", 0.0);
		outcomes.put("SB", 0.0);
		outcomes.put("CS", 0.0);
		outcomes.put("GO", 0.0);
		outcomes.put("FO", 0.0);
		outcomes.put("LO", 0.0);
		outcomes.put("FF", 0.0);
		outcomes.put("FL", 0.0);
		ArrayList<String> firstNames = new ArrayList<String>();
		Random r = new Random();
		try { 
			InputStream is = getClass().getClassLoader().getResourceAsStream("firstnames.txt");
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			while (in.ready()) { 
				firstNames.add(in.readLine());
			}
			in.close();
			String firstName = firstNames.get(r.nextInt(firstNames.size()));
			if(firstName.charAt(firstName.length()-1) == 'a') {
				firstName = firstName.substring(0, firstName.length()-1);
				double n = Math.random() * 3;
				if(n < 1) {
					firstName = firstName + "o";
				}
				else if(n < 2) {
					firstName = firstName + "i";
				}
				else {
					firstName = firstName + "e";
				}
			}
			ArrayList<String> lastNames = new ArrayList<String>();
			InputStream isto = getClass().getClassLoader().getResourceAsStream("lastnames.txt");
			BufferedReader into = new BufferedReader(new InputStreamReader(isto));
			while (into.ready()) { 
				lastNames.add(into.readLine());
			}
			into.close();
			String lastName = lastNames.get(r.nextInt(lastNames.size()));
			this.name = firstName + " " + lastName;
			ArrayList<String> countries = new ArrayList<String>();
			InputStream isthree = getClass().getClassLoader().getResourceAsStream("countries.txt");
			BufferedReader inthree = new BufferedReader(new InputStreamReader(isthree));
			while (inthree.ready()) { 
				countries.add(inthree.readLine());
			}
			inthree.close();	
			this.nationality = countries.get(r.nextInt(countries.size()));
		}
		catch (FileNotFoundException ex){
			System.out.println(ex);
		}
		catch (IOException ex){
			System.out.println(ex);
		}		
		this.age = (int) Math.round((Math.random()*6)+17);
		this.weight = (int) (r.nextGaussian()*30)+180;
		if(this.weight < 101) {
			this.weight = 101;
		}
		if(this.weight > 301) {
			this.weight = 301;
		}
		this.height = (int) Math.round((r.nextGaussian()*8)+135+(weight/4));
		if(this.height < 151) {
			this.height = 151;
		}
		if(this.height > 219) {
			this.height = 219;
		}
		this.potVelocity = setPotential();		
		this.mphVelocity = (int) Math.round((0.00151836*Math.pow(potVelocity, 2)) + (0.0168361*potVelocity) + 82.3);
		this.potMovement = setPotential();
		this.potControl = setPotential();
		this.potDeception = setPotential();
		this.potPickoff = setPotential();
		this.potReaction = setPotential();
		this.potHands = setPotential();
		this.potArm = potVelocity;
		this.potEndurance = setPotential();
		this.development = setPotential();
		this.decline = setPotential();
		this.peak = (int) Math.round((r.nextGaussian()*1.5)+30);
		this.greed = setPotential();		
		double rand = Math.random()*10;
		if(rand > 3.5) {
			throwing = "R";
			this.handedness = (int) Math.round((r.nextGaussian()*1.3)-4);			
		}
		else {
			throwing = "L";
			this.handedness = (int) Math.round((r.nextGaussian()*1.3)+4);
		}
		double random = Math.random()*10;
		if(random > 3.5) {
			batting = "R";
		}
		else {
			batting = "L";
		}
		int s = (int) Math.round(Math.random());
		if(s == 5) {
			batting = "S";
		}				
		this.scoutability = setPotential();
		Integer[] tempActual = new Integer[2];
		tempActual[0] = this.age;
		tempActual[1] = (int) Math.round(this.potVelocity - ((r.nextGaussian())*5) - ((26-this.age)*5));		
		this.actVelocity.add(tempActual);
		tempActual[1] = (int) Math.round(this.potMovement - ((r.nextGaussian())*5) - ((26-this.age)*5));
		this.actMovement.add(tempActual);
		tempActual[1] = (int) Math.round(this.potDeception - ((r.nextGaussian())*5) - ((26-this.age)*7));
		this.actDeception.add(tempActual); 
		tempActual[1] = (int) Math.round(this.potControl - ((r.nextGaussian())*5) - ((26-this.age)*6));
		this.actControl.add(tempActual);
		tempActual[1] = (int) Math.round(this.potPickoff - ((r.nextGaussian())*5) - ((26-this.age)*5));
		this.actPickoff.add(tempActual);
		tempActual[1] = (int) Math.round(this.potReaction - ((r.nextGaussian())*5) - ((26-this.age)*5));
		this.actReaction.add(tempActual);
		tempActual[1] = (int) Math.round(this.potHands - ((r.nextGaussian())*5) - ((26-this.age)*5));
		this.actHands.add(tempActual);
		tempActual[1] = (int) Math.round(this.potArm - ((r.nextGaussian())*5) - ((26-this.age)*5));
		this.actArm.add(tempActual);
		tempActual[1] = (int) Math.round(this.potEndurance - ((r.nextGaussian())*5) - ((26-this.age)*5));
		this.actEndurance.add(tempActual);
		tempActual[1] = setSalary();
		this.actSalary.add(tempActual);
		if(this.potVelocity > 70) {
			pitchTypes.add("4-seamer");
		}	
		else if(r.nextInt(99) < this.potVelocity/2) {
			pitchTypes.add("4-seamer");
		}
		if(r.nextInt(99) < this.potMovement/3) {
			pitchTypes.add("Sinker");
		}
		else if(r.nextInt(99) < this.potMovement/2 && r.nextInt(99) < this.potVelocity/2) {
			pitchTypes.add("Cutter");
		}
		else {
			pitchTypes.add("2-seamer");
		}
		if(r.nextInt(99) < this.potMovement/6 && r.nextInt(99) < this.potDeception/6) {
			pitchTypes.add("Screwball");
		}
		if(r.nextInt(99) < this.potMovement/2) {
				pitchTypes.add("Curveball");				
		}
		if(r.nextInt(99) < this.potMovement/2) {
			pitchTypes.add("Slider");
		}
		if(r.nextInt(99) < this.potDeception/5) {
			pitchTypes.add("Splitter");
		}
		else if(r.nextInt(99) < this.potDeception) {
			pitchTypes.add("Changeup");
		}
		if(this.potDeception > 90 && (potVelocity+potControl+potMovement) < 150) {
			pitchTypes.clear();
			pitchTypes.add("Knuckleball");
			pitchTypes.add("2-seamer");			
		}
		if(pitchTypes.size() == 1) {
			if(!pitchTypes.get(0).equals("4-seamer") && !pitchTypes.get(0).equals("2-seamer")) {
				pitchTypes.add("4-seamer");
			}
			else {
				pitchTypes.add("Slider");
			}
		}
		if(!pitchTypes.contains("Changeup") && !pitchTypes.contains("Curveball") && !pitchTypes.contains("Slider")
		   && !pitchTypes.contains("Splitter") && !pitchTypes.contains("Screwball") && !pitchTypes.contains("Knuckleball")) {
			pitchTypes.add("Changeup");
		}
		printOut();
	}
}

