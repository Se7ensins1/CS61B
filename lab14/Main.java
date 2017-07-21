import lab14lib.Generator;
import lab14lib.GeneratorAudioVisualizer;
import lab14lib.GeneratorDrawer;
import lab14lib.GeneratorPlayer;

public class Main {
	public static void main(String[] args) {
//		Generator generator = new SineWaveGenerator(440);
//		GeneratorPlayer gp = new GeneratorPlayer(generator);
//		gp.play(1000000);
//
//
//		GeneratorDrawer gd = new GeneratorDrawer(generator);
//		gd.draw(4096);



//		Generator generator = new SawToothGenerator(512);
//		GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
//		gav.drawAndPlay(4096, 1000000);


		Generator generator = new StrangeBitwiseGenerator(200);
		GeneratorAudioVisualizer gav = new GeneratorAudioVisualizer(generator);
		gav.drawAndPlay(4096, 1000000);
	}
} 