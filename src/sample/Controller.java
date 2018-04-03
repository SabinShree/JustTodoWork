package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller {
    @FXML
    private ListView<TodoItems> listView;
    @FXML
    private Label localDate;
    @FXML
    private TextArea textArea;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private ToggleButton filterToggleButton;
    @FXML
    private FilteredList<TodoItems> filteredList;
    private List<TodoItems> todoItems;

    @FXML
    public void initialize() {
        contextMenu = new ContextMenu();
        MenuItem deleteMenuItems = new MenuItem("Delete");
        deleteMenuItems.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TodoItems items = listView.getSelectionModel().getSelectedItem();
                deleteMenuItems(items);
            }
        });
        contextMenu.getItems().add(deleteMenuItems);

        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                TodoItems items = listView.getSelectionModel().getSelectedItem();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy"); // "d M yy");
                textArea.setText(items.getDetails());
                localDate.setText(formatter.format(items.getLocalDate()));
            }
        });

        filteredList = new FilteredList<>(TodoDataFileSystem.getInstance().getItems(), new Predicate<TodoItems>() {
            @Override
            public boolean test(TodoItems todoItems) {
                return true;
            }
        });
        SortedList<TodoItems> sortedList = new SortedList<>(filteredList, new Comparator<TodoItems>() {
            @Override
            public int compare(TodoItems o1, TodoItems o2) {
                return o1.getDecs().compareToIgnoreCase(o2.getDecs());
            }
        });
        //  listView.setItems(TodoDataFileSystem.getInstance().getItems());
        listView.setItems(sortedList);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.getSelectionModel().selectFirst();

        listView.setCellFactory(new Callback<ListView<TodoItems>, ListCell<TodoItems>>() {
            @Override
            public ListCell<TodoItems> call(ListView<TodoItems> param) {
                ListCell<TodoItems> cell = new ListCell<TodoItems>() {
                    @Override
                    protected void updateItem(TodoItems item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(item.getDecs());
                            if (item.getLocalDate().equals(LocalDate.now())) {
                                setTextFill(Color.RED);
                            } else if (item.getLocalDate().isBefore(LocalDate.now())) {
                                setTextFill(Color.BLUE);
                            }
                        }
                    }
                };
                cell.emptyProperty().addListener(
                        new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                if (newValue) {
                                    cell.setContextMenu(null);
                                } else {
                                    cell.setContextMenu(contextMenu);
                                }
                            }
                        }
                );
                return cell;
            }
        });
    }

    private void deleteMenuItems(TodoItems item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Todo Items");
        alert.setHeaderText("Delete Items : " + item.getDecs());
        alert.setContentText("Are you sure ? Press Ok to confirm , Or cancel to Back out.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            TodoDataFileSystem.getInstance().deleteTodoItems(item);
        }
    }

    public void exit() {
        Platform.exit();
    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) throws Exception {
        TodoItems items = listView.getSelectionModel().getSelectedItem();
        if (items != null) {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                deleteMenuItems(items);
            }
        }
    }

    public void showNewItemsDialog() throws Exception {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoItemsDailog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (Exception e) {
            System.out.println("e.getMessage() " + e.getMessage());
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> dialogType = dialog.showAndWait();
        if (dialogType.isPresent() && dialogType.get() == ButtonType.OK) {
            DialogController controller = fxmlLoader.getController();
            TodoItems items = controller.processResults();
            listView.getSelectionModel().select(items);
        }
    }

    public void handleFilterButton() {
        TodoItems items = listView.getSelectionModel().getSelectedItem();
        if (filterToggleButton.isSelected()) {
            filteredList.setPredicate(new Predicate<TodoItems>() {
                @Override
                public boolean test(TodoItems todoItems) {
                    return todoItems.getLocalDate().equals(LocalDate.now());
                }
            });
            if (filteredList.isEmpty()) {
                textArea.clear();
                localDate.setText("");
            } else if (filteredList.contains(items)) {
                listView.getSelectionModel().select(items);
            } else {
                listView.getSelectionModel().selectFirst();
            }
        } else {
            filteredList.setPredicate(new Predicate<TodoItems>() {
                @Override
                public boolean test(TodoItems todoItems) {
                    return true;
                }
            });
            listView.getSelectionModel().select(items);
        }
    }

}
