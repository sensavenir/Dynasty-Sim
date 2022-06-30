
public class test {

	public static void main(String[] args) {
		League league = new League("Test", 75, true, true, "Wheel", 8, "Average", true);
		for(int i = 0; i < 500; i++) {
			Game game = new Game(league.getTeam(0), league.getTeam(1));
			Game game1 = new Game(league.getTeam(0), league.getTeam(2));
			Game game2 = new Game(league.getTeam(0), league.getTeam(3));
			Game game3 = new Game(league.getTeam(0), league.getTeam(4));
			Game game4 = new Game(league.getTeam(0), league.getTeam(5));
			Game game5 = new Game(league.getTeam(0), league.getTeam(6));
			Game game6 = new Game(league.getTeam(0), league.getTeam(7));
			Game game8 = new Game(league.getTeam(1), league.getTeam(2));
			Game game9 = new Game(league.getTeam(1), league.getTeam(3));
			Game game0 = new Game(league.getTeam(1), league.getTeam(4));
			Game game11 = new Game(league.getTeam(1), league.getTeam(5));
			Game game12 = new Game(league.getTeam(1), league.getTeam(6));
			Game game13 = new Game(league.getTeam(1), league.getTeam(7));
			Game game15 = new Game(league.getTeam(2), league.getTeam(3));
			Game game16 = new Game(league.getTeam(2), league.getTeam(4));
			Game game17 = new Game(league.getTeam(2), league.getTeam(5));
			Game game18 = new Game(league.getTeam(2), league.getTeam(6));
			Game game19 = new Game(league.getTeam(2), league.getTeam(7));
			Game game21 = new Game(league.getTeam(3), league.getTeam(4));
			Game game22 = new Game(league.getTeam(3), league.getTeam(5));
			Game game23 = new Game(league.getTeam(3), league.getTeam(6));
			Game game24 = new Game(league.getTeam(3), league.getTeam(7));
			Game game26 = new Game(league.getTeam(4), league.getTeam(5));
			Game game27 = new Game(league.getTeam(4), league.getTeam(6));
			Game game28 = new Game(league.getTeam(4), league.getTeam(7));
			Game game30 = new Game(league.getTeam(5), league.getTeam(6));
			Game game31 = new Game(league.getTeam(5), league.getTeam(7));
			Game game33 = new Game(league.getTeam(6), league.getTeam(7));
		}
		for(int a = 0; a < 8; a++) {
			for(int i = 0; i < league.getTeam(0).getRHP().size(); i++) {
				league.getTeam(a).getRHP().get(i).printLine();
			}
			for(int i = 0; i < 5; i++) {
				league.getTeam(a).getStarter().printLine();
			}
		}		
	}
}
