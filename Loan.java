import java.time.LocalDate;

public class Loan {
    private int id;
    private String bookTitle;
    private String memberName;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;

    public Loan(int id, String bookTitle, String memberName,
                LocalDate loanDate, LocalDate dueDate, LocalDate returnDate) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.memberName = memberName;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return String.format("[Loan %d] '%s' borrowed by %s | from %s | due %s%s",
                id, bookTitle, memberName, loanDate, dueDate,
                returnDate != null ? " | returned " + returnDate : "");
    }
}
