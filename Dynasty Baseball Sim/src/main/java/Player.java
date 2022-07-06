import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.io.*;

public class Player {	
	private String name;
	private int ID;
	private String position;
	private int age;
	private int weight;
	private int height;
	private String nationality;
	private String biography;
	private String avatar;
	private ArrayList<Team> team = new ArrayList<Team>();
	private int potPower;
	private int potContact;
	private int potRecognition;
	private int potSwing;
	private int potAggression;
	private int potSpeed;
	private int potReaction;
	private int potHands;
	private int potArm;
	private int potEndurance;
	private ArrayList<Integer[]> actSalary = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> actPower = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> actContact = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> actRecognition = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> actSwing = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> actAggression = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> actSpeed = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> actReaction = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> actHands = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> actArm = new ArrayList<Integer[]>();
	private ArrayList<Integer[]> actEndurance = new ArrayList<Integer[]>();
	private int development;
	private int decline;
	private int peak;
	private int greed;
	private int handedness;
	private String batting;
	private String throwing;
	private int scoutability;
	private double ops;
	HashMap <String, String> posFormulae = new HashMap<String, String>();
	HashMap <String, String> posAdjustments = new HashMap<String, String>();
	HashMap <String, Double> outcomes = new HashMap<String, Double>();
	
	public String getName() {
		return this.name;
	}
	
	public String getPosition() {
		return this.position;
	}
	
	public String getHand() {
		return this.batting;
	}
	
	public int getSplit() {
		return this.handedness;
	}
	
	public int getPower() {
		return this.potPower;
	}
	
	public int getContact() {
		return this.potContact;
	}
	
	public int getSwing() {
		return this.potSwing;
	}
	
	public int getAggression() {
		return this.potAggression;
	}
	
	public int getRecognition() {
		return this.potRecognition;
	}
	
	public int getSpeed() {
		return this.potSpeed;
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
	
	public int getDefense() {
		int defense = 0;
		if(this.position.equals("LF") || this.position.equals("CF") || this.position.equals("RF")) {
			defense = (int) Math.round(0.67*this.potSpeed + 0.33*this.potReaction);
		}
		else {
			defense = (int) Math.round(0.5*this.potReaction + 0.5*this.potSpeed);
		}
		return defense;
	}
			
	//generate random rating 1-99
	private int setPotential() {
		Random r = new Random();
		int tempRating = (int) Math.round((r.nextGaussian()*13)+65);
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
	
	public double getOPS() {
		return this.ops;
	}
	
	public void printOut() {
		if(!position.equals("x")) {
			System.out.println(name + ": " + position + " | " + this.batting + "/" + this.throwing);			
			System.out.println("DI " + potRecognition + " CN " + potContact + " SW " + potSwing  + " SL " + potPower);
			System.out.println("SP " + potSpeed + " RA " + potReaction + " GL " + potHands  + " AR " + potArm);
		}
	}
	
	public void printLine() {
		printOut();	
		System.out.println("PA: " + outcomes.get("PA"));
		System.out.println("1B: " + outcomes.get("1B"));
		System.out.println("2B: " + outcomes.get("2B"));
		System.out.println("3B: " + outcomes.get("3B"));
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
	public Player(String pos) {
		this.position = pos;
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
		this.potPower = setPotential();	
		this.potContact = setPotential();
		this.potSwing =  (int) Math.round((r.nextGaussian()*15)+50);
		if(potSwing > 99) {
			potSwing = 99;
		}
		else if (potSwing < 1) {
			potSwing = 1;
		}
		this.potAggression = setPotential();
		this.potRecognition = setPotential();
		this.potSpeed = setPotential();
		this.potReaction = setPotential();
		this.potHands = setPotential();
		this.potArm = setPotential();
		this.potEndurance = setPotential();
		this.development = setPotential();
		this.decline = setPotential();
		this.peak = (int) Math.round((r.nextGaussian()*1.5)+30);
		this.greed = setPotential();
		printOut();
		double rand = Math.random()*10;
		if(rand > 3.5) {
			batting = "R";
			this.handedness = (int) Math.round((r.nextGaussian()*1.3)-4);			
		}
		else {
			batting = "L";
			this.handedness = (int) Math.round((r.nextGaussian()*1.3)+4);
		}		
		int s = (int) Math.round(Math.random()*10);
		if(s == 5) {
			int lr = (int) Math.round(Math.random()*10);
			if(lr < 6) {
				batting = "SR";
			}
			else {
				batting = "SL";
			}
		}
		if(position.equals("C") || position.equals("3B") || position.equals("SS") || position.equals("2B")) {
			throwing = "R";
		}
		else {
			double random = Math.random()*10;
			if(random > 3.5) {
				throwing = "R";
			}
			else {
				throwing = "L";
			}
		}
		this.scoutability = setPotential();
		Integer[] tempActual = new Integer[2];
		tempActual[0] = this.age;
		tempActual[1] = (int) Math.round(this.potPower - ((r.nextGaussian())*5) - ((26-this.age)*6));
		this.actPower.add(tempActual);
		tempActual[1] = (int) Math.round(this.potContact - ((r.nextGaussian())*5) - ((26-this.age)*5));
		this.actContact.add(tempActual);
		tempActual[1] = (int) Math.round(this.potRecognition - ((r.nextGaussian())*5) - ((26-this.age)*7));
		this.actRecognition.add(tempActual); 
		tempActual[1] = (int) Math.round(this.potSwing - ((r.nextGaussian())*10));
		this.actSwing.add(tempActual);
		tempActual[1] = (int) Math.round(this.potAggression - ((r.nextGaussian())*10));
		this.actAggression.add(tempActual);
		tempActual[1] = (int) Math.round(this.potSpeed - ((r.nextGaussian())*5) - ((26-this.age)*5));
		this.actSpeed.add(tempActual);
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
	}
}
