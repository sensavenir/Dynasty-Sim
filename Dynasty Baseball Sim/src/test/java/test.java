
public class test {

	public static void main(String[] args) {
		League league = new League("Test", 75, true, true, "Wheel", 2, "Average", true);
		Game game = new Game(league.getTeam(0), league.getTeam(1));
	}
}
