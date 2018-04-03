package sample;

import java.time.LocalDate;

public class TodoItems {
    private String decs;
    private String details;
    private LocalDate localDate;

    public TodoItems(String decs, String details, LocalDate localDate) {
        this.decs = decs;
        this.details = details;
        this.localDate = localDate;
    }

    public String getDecs() {
        return decs;
    }

    public String getDetails() {
        return details;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }
}
