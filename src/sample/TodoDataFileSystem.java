package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class TodoDataFileSystem {
    private static TodoDataFileSystem ourInstance = new TodoDataFileSystem();
    private ObservableList<TodoItems> items;

    public static TodoDataFileSystem getInstance() {
        return ourInstance;
    }

    private String fileName = "Text.txt";
    private DateTimeFormatter formatter;

    private TodoDataFileSystem() {
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public void loadData() throws IOException {
        items = FXCollections.observableArrayList();
        Path path = Paths.get(fileName);
        BufferedReader br = Files.newBufferedReader(path);
        try {
            String input = "";
            while ((input = br.readLine()) != null) {
                String[] arrInput = input.split("\t");
                String desc = arrInput[0];
                String details = arrInput[1];
                String localDate = arrInput[2];

                LocalDate date = LocalDate.parse(localDate, formatter);
                TodoItems item = new TodoItems(desc, details, date);
                items.add(item);
            }
        } finally {
            br.close();
        }
    }

    public ObservableList<TodoItems> getItems() {
        return items;
    }

    public void writeToDisk() throws IOException {
        Path path = Paths.get(fileName);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try {
            Iterator<TodoItems> itemsIterator = items.iterator();
            while (itemsIterator.hasNext()) {
                TodoItems items = itemsIterator.next();
                bw.write(String.format("%s\t%s\t%s", items.getDecs(), items.getDetails(), items.getLocalDate().format(formatter)));
                bw.newLine();
            }
        } finally {
            bw.close();
        }
    }

    public void AddToDoItems(TodoItems items) {
        this.items.add(items);
    }

    public void deleteTodoItems(TodoItems item) {
        this.items.remove(item);
    }
}
