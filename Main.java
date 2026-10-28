import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner in = new Scanner(System.in);
    private static final BookDAO bookDAO = new BookDAO();
    private static final MemberDAO memberDAO = new MemberDAO();
    private static final LoanDAO loanDAO = new LoanDAO();

    public static void main(String[] args) {
        while (true) {
            printMenu();
            int choice = readInt("Choose: ");
            try {
                switch (choice) {
                    case 1 -> addBook();
                    case 2 -> printAll(bookDAO.findAll());
                    case 3 -> printAll(bookDAO.search(readLine("Search keyword: ")));
                    case 4 -> deleteBook();
                    case 5 -> addMember();
                    case 6 -> printAll(memberDAO.findAll());
                    case 7 -> borrow();
                    case 8 -> returnBook();
                    case 9 -> printAll(loanDAO.activeLoans());
                    case 10 -> printAll(loanDAO.overdueLoans());
                    case 0 -> { System.out.println("Goodbye!"); return; }
                    default -> System.out.println("Invalid choice");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n===== Library Management System =====");
        System.out.println("1. Add book          2. List books       3. Search books");
        System.out.println("4. Delete book       5. Add member       6. List members");
        System.out.println("7. Borrow book       8. Return book");
        System.out.println("9. Active loans      10. Overdue loans   0. Exit");
    }

    private static void addBook() throws SQLException {
        String title = readLine("Title: ");
        String author = readLine("Author: ");
        String isbn = readLine("ISBN: ");
        int copies = readInt("Number of copies: ");
        bookDAO.add(new Book(title, author, isbn, copies));
        System.out.println("Book added successfully.");
    }

    private static void deleteBook() throws SQLException {
        int id = readInt("Book ID: ");
        System.out.println(bookDAO.delete(id) ? "Book deleted." : "Book not found.");
    }

    private static void addMember() throws SQLException {
        memberDAO.add(new Member(readLine("Name: "), readLine("Phone: ")));
        System.out.println("Member added successfully.");
    }

    private static void borrow() throws SQLException {
        int bookId = readInt("Book ID: ");
        int memberId = readInt("Member ID: ");
        int days = readInt("Loan period (days): ");
        loanDAO.borrow(bookId, memberId, days);
        System.out.println("Book borrowed successfully.");
    }

    private static void returnBook() throws SQLException {
        loanDAO.returnBook(readInt("Loan ID: "));
        System.out.println("Book returned successfully.");
    }

    private static <T> void printAll(List<T> items) {
        if (items.isEmpty()) System.out.println("(no results)");
        else items.forEach(System.out::println);
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readLine(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
