public class NBody {

	public static void main(String[] args) {
		double t = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String fileName = args[2];
		int time = 0;

		double radius = readRadius(fileName);
		Planet[] planets = readPlanets(fileName);

		// Draw universe & planets
		StdDraw.setScale(-radius, radius);
		StdDraw.picture(0, 0, "images/starfield.jpg");
		for (int i = 0; i < planets.length; i++) {
			planets[i].draw();
		}

		// Create animations
		while (time < t) {
			// Create an xForces array and yForces array.
			Double[] xForces = new Double [planets.length];
			Double[] yForces = new Double [planets.length];

			// Calculate the net x and y forces for each planet.
			for (int i = 0; i < planets.length; i++) {
				xForces[i] = planets[i].calcNetForceExertedByX(planets);
				yForces[i] = planets[i].calcNetForceExertedByY(planets);
			}

			// Call update on each of the planets.
			for (int i = 0; i < planets.length; i++) {
				planets[i].update(dt, xForces[i], yForces[i]);
			}

			// Draw the background image.
			StdDraw.setScale(-radius, radius);
			StdDraw.picture(0, 0, "images/starfield.jpg");

			// Draw all of the planets.
			for (int i = 0; i < planets.length; i++) {
				planets[i].draw();
			}

			// Pause the animation for 10 milliseconds.
			StdDraw.show(10);

			// Increase the time variable by dt.
			time += dt;
		}

		// Print the universe.
		StdOut.printf("%d\n", planets.length);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < planets.length; i++) {
			StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
		   		planets[i].xxPos, planets[i].yyPos, planets[i].xxVel, planets[i].yyVel, planets[i].mass, planets[i].imgFileName);	
		}	
	}

	/** Parses a file to get the universe's radius.
	 * @param fileName - location of the universe file
	 * @return the radius of the universe in that file
	 */
	public static double readRadius(String fileName) {
		In x = new In(fileName);
		x.readInt(); 			// reads number of planets
		return x.readDouble();
	}

	/** Parses a file to get the universe's planets.
	 * @param fileName - location of the universe file
	 * @return the planets of the universe in that file
	 */
	public static Planet[] readPlanets(String fileName) {
		In x = new In(fileName);
		int num = x.readInt();	// reads number of planets
		int i = 0;
		x.readDouble();			// reads radius of universe
		Planet[] p = new Planet[num];
		while(num > i) {
			double xP = x.readDouble();
			double yP = x.readDouble();
			double xV = x.readDouble();
			double yV = x.readDouble();
			double m = x.readDouble();
			String img = x.readString();
			p[i] = new Planet(xP, yP, xV, yV, m, img);
			i++;
		}
		return p;
	}
}
