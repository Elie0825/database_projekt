import org.junit.jupiter.api.Test;
import se.elie.databas.JDBCUtil;


import java.sql.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WorkRoleDAOImplTestInsert {

    @Test
    void testInsertWorkRole() throws SQLException {
        String insertSQL = "INSERT INTO WORK_ROLE (TITLE, DESCRIPTION, SALARY, CREATION_DATE) VALUES (?, ?, ?, ?)";

        try (Connection conn = JDBCUtil.getConnection(); // Hämta anslutning
             PreparedStatement pstmt = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

            // Sätt värden för testdata
            pstmt.setString(1, "Software Engineer");
            pstmt.setString(2, "Responsible for developing software solutions.");
            pstmt.setDouble(3, 60000.00);
            pstmt.setDate(4, Date.valueOf(LocalDate.now()));

            // Utför INSERT
            int rowsAffected = pstmt.executeUpdate();
            assertEquals(1, rowsAffected, "En rad bör läggas till i WORK_ROLE-tabellen");

            // Kontrollera att ID genererades
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                assertTrue(generatedKeys.next(), "Ett genererat ID bör finnas");
                int generatedId = generatedKeys.getInt(1);
                assertTrue(generatedId > 0, "Genererat ID bör vara större än 0");
                System.out.println("Genererat ID: " + generatedId);
            }

        } catch (SQLException e) {
            fail("Misslyckades med att infoga post i WORK_ROLE: " + e.getMessage());
        }
    }

}