package dad.planeador.vuelos.dialogs;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class ExceptionAlert extends Alert{
	
	private Label exLabel;

	public ExceptionAlert(AlertType alertType) {
		super(alertType);
		exLabel = new Label("The exception stacktrace was:");
	}
	
	public void setException(Exception ex) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(exLabel, 0, 0);
		expContent.add(textArea, 0, 1);

		getDialogPane().setExpandableContent(expContent);
	}
	
	public void setExpandableLabelText(String text) {
		exLabel.setText(text);
	}

}
