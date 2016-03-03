package dad.planeador.vuelos.utils;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimacionesUtils {
	
	public static void aplicarFadeIn(long millis, Node node) {
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(millis), node);
		fadeTransition.setFromValue(0.0);
		fadeTransition.setToValue(1.0);
		fadeTransition.play();
	}
	
	public static void aplicarFadeOut(long millis, Node node) {
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(millis), node);
		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0.0);
		fadeTransition.play();
	}
}
