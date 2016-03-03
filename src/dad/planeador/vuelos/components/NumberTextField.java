package dad.planeador.vuelos.components;

import javafx.scene.control.TextField;

public class NumberTextField extends TextField {

	private int maximumDigits = -1;

	@Override
	public void replaceText(int start, int end, String text) {
		if (validate(text)) {
			super.replaceText(start, end, text);
			verify();
		}
	}

	@Override
	public void replaceSelection(String text) {
		if (validate(text)) {
			super.replaceSelection(text);
			verify();
		}
	}

	public void setMaximumDigits(int max) {
		maximumDigits = max;
	}

	private boolean validate(String text) {
		return text.matches("[0-9]*");
	}

	private void verify() {
		if (maximumDigits != -1 && getText().length() > maximumDigits) {
			setText(getText().substring(0, maximumDigits));
			positionCaret(maximumDigits);
		}
	}
}
