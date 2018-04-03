package sample;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.time.LocalDate;


public class DialogController {
    @FXML
    private TextField shortDescription;
    @FXML
    private TextArea detailsArea;
    @FXML
    private DatePicker deadLinePicker;
   @FXML
    private Label warning;

     TodoItems processResults() {
        String shortDescriptions = shortDescription.getText().trim();
        String details = detailsArea.getText().trim();
        LocalDate deadLineDatePicker = deadLinePicker.getValue();
        if (deadLineDatePicker != null && !details.isEmpty() && (details.trim().length() > 0)) {
            if (!deadLineDatePicker.isBefore(LocalDate.now())) {
                TodoItems todoItems = new TodoItems(shortDescriptions, details, deadLineDatePicker);
                TodoDataFileSystem.getInstance().AddToDoItems(todoItems);
                return todoItems;
            }
        } else {
            warning.isVisible();
            warning.setTextFill(Color.RED);
            warning.setFont(Font.font("Times New Roman", 23));
        }
        return null;
    }
}
