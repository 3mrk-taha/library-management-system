import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    private static final String SELECT_LOANS =
        "SELECT l.id, b.title, m.name, l.loan_date, l.due_date, l.return_date " +
        "FROM loans l JOIN books b ON l.book_id = b.id JOIN members m ON l.member_id = m.id ";

    /** Borrow a book. Uses a transaction so both updates succeed or neither does. */
    public void borrow(int bookId, int memberId, int days) throws SQLException {
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                int available;
                try (PreparedStatement ps = c.prepareStatement(
                        "SELECT copies_available FROM books WHERE id = ? FOR UPDATE")) {
                    ps.setInt(1, bookId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new SQLException("Book not found");
                        available = rs.getInt(1);
                    }
                }
                if (available <= 0) throw new SQLException("No copies available for this book");

                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO loans (book_id, member_id, loan_date, due_date) " +
                        "VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL ? DAY))")) {
                    ps.setInt(1, bookId);
                    ps.setInt(2, memberId);
                    ps.setInt(3, days);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement(
                        "UPDATE books SET copies_available = copies_available - 1 WHERE id = ?")) {
                    ps.setInt(1, bookId);
                    ps.executeUpdate();
                }
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }

    public void returnBook(int loanId) throws SQLException {
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                int bookId;
                try (PreparedStatement ps = c.prepareStatement(
                        "SELECT book_id FROM loans WHERE id = ? AND return_date IS NULL FOR UPDATE")) {
                    ps.setInt(1, loanId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) throw new SQLException("Loan not found or already returned");
                        bookId = rs.getInt(1);
                    }
                }
                try (PreparedStatement ps = c.prepareStatement(
                        "UPDATE loans SET return_date = CURDATE() WHERE id = ?")) {
                    ps.setInt(1, loanId);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = c.prepareStatement(
                        "UPDATE books SET copies_available = copies_available + 1 WHERE id = ?")) {
                    ps.setInt(1, bookId);
                    ps.executeUpdate();
                }
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }

    public List<Loan> activeLoans() throws SQLException {
        return query(SELECT_LOANS + "WHERE l.return_date IS NULL ORDER BY l.due_date");
    }

    public List<Loan> overdueLoans() throws SQLException {
        return query(SELECT_LOANS + "WHERE l.return_date IS NULL AND l.due_date < CURDATE() ORDER BY l.due_date");
    }

    private List<Loan> query(String sql) throws SQLException {
        List<Loan> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Date ret = rs.getDate("return_date");
                list.add(new Loan(rs.getInt("id"), rs.getString("title"), rs.getString("name"),
                        rs.getDate("loan_date").toLocalDate(), rs.getDate("due_date").toLocalDate(),
                        ret == null ? null : ret.toLocalDate()));
            }
        }
        return list;
    }
}
