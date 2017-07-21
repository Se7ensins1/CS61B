
public class Planet {

	public double xxPos;	// its current x position
	public double yyPos;	// its current y position
	public double xxVel;	// its current velocity in the x direction
	public double yyVel;	// its current velocity in the y direction
	public double mass;		// its mass
	public String imgFileName;	// the image of an image in the images directory that depicts the planet
	private static double gConst = 6.67*Math.pow(10, -11);

	/** Constructor to instantiates a planet.
	 */
    public Planet(double xxPos, double yyPos, double xxVel, double yyVel, double mass, String imgFileName) {
    	this.xxPos = xxPos;
    	this.yyPos = yyPos;
    	this.xxVel = xxVel;
    	this.yyVel = yyVel;
    	this.mass = mass;
    	this.imgFileName = imgFileName;
    }

    /** Constructor to initialize an identical Planet object
     * @param x - planet that is copied
     */
    public Planet(Planet x) {
    	this(p.xxPos, p.yyPos, p.xxVel, p.yyVel, p.mass, p.imgFileName);
    }

    /** Finds the change in distance in the x-direction
	 * @param x - planet that is compared to
	 * @return change in x-direction
 	 */
    private double dx(Planet x) {
    	return x.xxPos - this.xxPos;
    }

    /** Finds the change in distance in the y-direction
	 * @param x - planet that is compared to
	 * @return change in y-direction
 	 */
    private double dy(Planet x) {
    	return x.yyPos - this.yyPos;
    }

    /** Finds the change in distance in the r-direction
	 * @param x - planet that is compared to
	 * @return change in r-direction
 	 */
    public double calcDistance(Planet x) {
    	double dxSquared = this.dx(x) * this.dx(x);
    	double dySquared = this.dy(x) * this.dy(x);
    	return Math.sqrt(dxSquared + dySquared);
    }

    /** Finds the force exerted by the planet
	 * @param x - planet that is compared to
	 * @return force exerted on each other
 	 */
    public double calcForceExertedBy(Planet x) {
    	return (gConst * this.mass * x.mass) / Math.pow(this.calcDistance(x), 2);
    }

    /** Finds the force exerted by the planet in the x-direction
	 * @param x - planet that is compared to
	 * @return force in the x-direction
 	 */
    public double calcForceExertedByX(Planet x) {
    	return (this.calcForceExertedBy(x) * this.dx(x)) / this.calcDistance(x);
    }

    /** Finds the force exerted by the planet in the y-direction
	 * @param x - planet that is compared to
	 * @return force in the y-direction
 	 */
    public double calcForceExertedByY(Planet x) {
    	return (this.calcForceExertedBy(x) * this.dy(x)) / this.calcDistance(x);
    }

    /** Finds the forces exerted by the planets in the x-direction
     * @param x - list of planets that is compared to
     * @return force in the x-direction
     */
    public double calcNetForceExertedByX(Planet[] xs) {
        double forceTot = 0;
        for (int i = 0; i < xs.length; i++) {
            if (!this.equals(xs[i])){
                forceTot += this.calcForceExertedByX(xs[i]); 
            }
        }
        return forceTot;
    }

    /** Finds the forces exerted by the planets in the y-direction
     * @param x - list of planets that is compared to
     * @return force in the y-direction
     */
    public double calcNetForceExertedByY(Planet[] xs) {
        double forceTot = 0;
        for (int i = 0; i < xs.length; i++) {
            if (!this.equals(xs[i])){
                forceTot += this.calcForceExertedByY(xs[i]); 
            }
        }
        return forceTot;
    }

    /** Updates the planet with new positions and velocities
	 * @param dt - specific change in time
	 * @param fX - force in x-direction
	 * @param fY - force in y-direction
 	 */
    public void update(double dt, double fX, double fY) {
    	double aX = fX / this.mass;
    	double aY = fY / this.mass;
    	double vNewX = this.xxVel + (dt * aX);
    	double vNewY = this.yyVel + (dt * aY);
    	double pNewX = this.xxPos + (dt * vNewX);
    	double pNewY = this.yyPos + (dt * vNewY);
    	this.xxPos = pNewX;
    	this.yyPos = pNewY;
    	this.xxVel = vNewX;
    	this.yyVel = vNewY;
    }

    /** Uses the StdDraw API to draw the planet's img at the Planet's position
     */
    public void draw(){
    	StdDraw.picture(this.xxPos, this.yyPos, "images/" + this.imgFileName);
    }
}
