import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    public void add(Member m) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("INSERT INTO members (name, phone) VALUES (?,?)")) {
            ps.setString(1, m.getName());
            ps.setString(2, m.getPhone());
            ps.executeUpdate();
        }
    }

    public List<Member> findAll() throws SQLException {
        List<Member> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM members ORDER BY id")) {
            while (rs.next()) {
                list.add(new Member(rs.getInt("id"), rs.getString("name"), rs.getString("phone")));
            }
        }
        return list;
    }
}
