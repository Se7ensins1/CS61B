import lab14lib.Generator;
import lab14lib.GeneratorAudioAnimator;
<<<<<<< HEAD

=======
>>>>>>> 4416f2170a02659f7f2788b21457cb521c35f9ce
public class SineWaveAnimation {
    public static void main(String[] args) {
        Generator generator = new SineWaveGenerator(440);
        GeneratorAudioAnimator ga = new GeneratorAudioAnimator(generator);
        ga.drawAndPlay(500,400000);
    }
}
