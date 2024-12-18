import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BorrowRecord {
    private String bookTitle;
    private LocalDate borrowDate;
    private LocalDate dueDate;

    public BorrowRecord(String bookTitle, int weeks) {
        this.bookTitle = bookTitle;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusWeeks(weeks);  
    }

    public String getBookTitle() {
        return bookTitle;
    } 

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isOverdue() {
        return LocalDate.now().isAfter(dueDate);  
    }

    public long getRemainingDays() {
        return ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
