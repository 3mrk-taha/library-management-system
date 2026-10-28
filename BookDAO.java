import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public void add(Book b) throws SQLException {
        String sql = "INSERT INTO books (title, author, isbn, copies_total, copies_available) VALUES (?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, b.getTitle());
            ps.setString(2, b.getAuthor());
            ps.setString(3, b.getIsbn());
            ps.setInt(4, b.getCopiesTotal());
            ps.setInt(5, b.getCopiesAvailable());
            ps.executeUpdate();
        }
    }

    public List<Book> findAll() throws SQLException {
        return query("SELECT * FROM books ORDER BY id", null);
    }

    public List<Book> search(String keyword) throws SQLException {
        return query("SELECT * FROM books WHERE title LIKE ? OR author LIKE ? ORDER BY id", keyword);
    }

    public boolean delete(int id) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM books WHERE id = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private List<Book> query(String sql, String keyword) throws SQLException {
        List<Book> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            if (keyword != null) {
                ps.setString(1, "%" + keyword + "%");
                ps.setString(2, "%" + keyword + "%");
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"),
                            rs.getString("isbn"), rs.getInt("copies_total"), rs.getInt("copies_available")));
                }
            }
        }
        return list;
    }
}
