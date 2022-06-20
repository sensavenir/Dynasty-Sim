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
	HashMap <String, String> posFormulae = new HashMap<String, String>();
	HashMap <String, String> posAdjustments = new HashMap<String, String>();
	
	public String getName() {
		return this.name;
	}
	
	public String getHand() {
		return this.batting;
	}
	
	public int getSplit() {
		return this.handedness;
	}
	
	public int getPower() {
		return this.actPower.get(actPower.size()-1)[1];
	}
	
	public int getContact() {
		return this.actContact.get(actContact.size()-1)[1];
	}
	
	public int getSwing() {
		return this.actSwing.get(actSwing.size()-1)[1];
	}
	
	public int getAggression() {
		return this.actAggression.get(actAggression.size()-1)[1];
	}
	
	public int getRecognition() {
		return this.actRecognition.get(actRecognition.size()-1)[1];
	}
	
	public int getSpeed() {
		return this.actSpeed.get(actSpeed.size()-1)[1];
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
			
	//generate random rating 1-99
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
	public Player(String pos) {
		this.position = pos;
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
		this.potPower = setPotential();	
		this.potContact = setPotential();
		this.potSwing = setPotential();
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
		tempActual[1] = (int) Math.round(this.potPower - ((r.nextGaussian())*5) - ((26-this.age)*5));
		this.actPower.add(tempActual);
		tempActual[1] = (int) Math.round(this.potContact - ((r.nextGaussian())*5) - ((26-this.age)*5));
		this.actContact.add(tempActual);
		tempActual[1] = (int) Math.round(this.potRecognition - ((r.nextGaussian())*5) - ((26-this.age)*5));
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
