

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
	private String batting;
	private String throwing;
	private int scoutability;
	
	public String getHand() {
		return this.throwing;
	}
	
	public int getSplit() {
		return this.handedness;
	}
	
	public int getVelocity() {
		return this.actVelocity.get(actVelocity.size()-1)[1];
	}
	
	public int getMovement() {
		return this.actMovement.get(actMovement.size()-1)[1];
	}
	
	public int getControl() {
		return this.actControl.get(actControl.size()-1)[1];
	}
	
	public int getDeception() {
		return this.actDeception.get(actDeception.size()-1)[1];
	}
	
	public int getPickoff() {
		return this.actPickoff.get(actPickoff.size()-1)[1];
	}
	
	public int getReaction() {
		return this.actReaction.get(actReaction.size()-1)[1];
	}
	
	public int getHands() {
		return this.actHands.get(actHands.size()-1)[1];
	}
	
	public int getArm() {
		return this.actArm.get(actArm.size()-1)[1];
	}
	
	public String getPitch(int balls, int strikes) {
		String pitch = "";
		if(balls == 3 && strikes != 2) {
			if(pitchTypes.contains("4-seamer") || pitchTypes.contains("2-seamer")) {
				
			}
		}
		if(strikes == 2) {
			if(pitchTypes.contains("4-seamer")) {
				
			}
			else if(pitchTypes.contains("2-seamer")) {
				
			}
		}
		if(pitchTypes.get(0).equals("Knuckleball")) {
			int k = (int) Math.random()*9;
			if(k < 9) {
				pitch = "Knuckleball";
			}
			else {
				pitch = "2-seamer";
			}
		}
		return pitch;
	}
	
	private int setPotential() {
		Random r = new Random();
		int tempRating = (int) Math.round((r.nextGaussian()*15)+55);
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
	
	//constructor
	public Pitcher () {
		ArrayList<String> firstNames = new ArrayList<String>();
		Random r = new Random();
		try { 
			BufferedReader in = new BufferedReader(new FileReader("~/src/main/java/firstnames.txt"));
			while (in.ready()) { 
				firstNames.add(in.readLine());
			}
			in.close();
			String firstName = firstNames.get(r.nextInt(firstNames.size()));

			ArrayList<String> lastNames = new ArrayList<String>();
			BufferedReader into = new BufferedReader(new FileReader("~/src/main/java/lastnames.txt"));
			while (into.ready()) { 
				lastNames.add(into.readLine());
			}
			into.close();
			String lastName = lastNames.get(r.nextInt(lastNames.size()));
			this.name = firstName + lastName;

			ArrayList<String> countries = new ArrayList<String>();
			BufferedReader inthree = new BufferedReader(new FileReader("~/src/main/java/countries.txt"));
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
		tempActual[1] = (int) Math.round(this.potDeception - ((r.nextGaussian())*5) - ((26-this.age)*5));
		this.actDeception.add(tempActual); 
		tempActual[1] = (int) Math.round(this.potControl - ((r.nextGaussian())*10));
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
		if(this.potVelocity > 55) {
			pitchTypes.add("4-seamer");
		}	
		if(r.nextInt(99) < this.potMovement/2 && r.nextInt(99) < this.potVelocity/2) {
			pitchTypes.add("Sinker");
		}
		else if(r.nextInt(99) < this.potMovement/2 && r.nextInt(99) < this.potVelocity/2) {
			pitchTypes.add("Cutter");
		}
		else if(r.nextInt(99) < this.potMovement) {
			pitchTypes.add("2-seamer");
		}
		if(r.nextInt(99) < this.potMovement/8 && r.nextInt(99) < this.potDeception/8) {
			pitchTypes.add("Screwball");
		}
		if(r.nextInt(99) < this.potMovement/2) {
				pitchTypes.add("Curveball");				
		}
		if(r.nextInt(99) < this.potMovement/2) {
			pitchTypes.add("Slider");
		}
		if(r.nextInt(99) < this.potDeception/10) {
			pitchTypes.add("Splitter");
		}
		else if(r.nextInt(99) < this.potDeception/2) {
			pitchTypes.add("Changeup");
		}
		if(this.potDeception > 90 && (potVelocity+potControl+potMovement) < 150) {
			pitchTypes.clear();
			pitchTypes.add("Knuckleball");
			pitchTypes.add("2-seamer");			
		}
	}
}

