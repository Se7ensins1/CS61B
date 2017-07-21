public class TestPlanet {
	public static void main (String[] args){
		Planet p1 = new Planet(0.0, 0.0, 1.0, 1.0, 5.0, "jupiter.gif");
		Planet p2 = new Planet(1.0, 1.0, 1.0, 1.0, 5.0, "jupiter.gif");
		System.out.print("( " + p1.calcForceExertedByX(p2) + ", " + p1.calcForceExertedByY(p2) + " )");
	}
}