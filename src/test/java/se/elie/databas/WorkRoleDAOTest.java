package se.elie.databas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class WorkRoleDAOTest {

    private Connection connection;
    private WorkRoleDAO workRoleDAO;
    private Properties properties;

    @BeforeEach
    void setUp() throws SQLException, IOException {
        // Läs in properties
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IOException("Could not find application.properties");
            }
            properties.load(input);
        }

        // Hämta databasanslutning
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");

        connection = DriverManager.getConnection(url, user, password);
        connection.setAutoCommit(false); // Använd transaktioner för testisolering

        // Skapa DAO-objektet
        workRoleDAO = new WorkRoleDAO(connection);

        // Rensa tabellen innan varje test för att säkerställa ett rent tillstånd
        cleanUp();
    }

@AfterEach
    void tearDown() throws SQLException {
        // Rulla tillbaka transaktionen för att säkerställa att inga förändringar sparas i databasen
        connection.rollback();
    }

    // Rensa WorkRole-tabellen för att säkerställa testisolering
    void cleanUp() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM work_role");
        }
    }


    @Test
    void testInsertAndGetAllWorkRoles() throws SQLException {
        System.out.println("Running test: testInsertAndGetAllWorkRoles");

        WorkRole newRole = new WorkRole("Software Engineer", "Responsible for developing software solutions.", 60000.00, LocalDate.now());
        workRoleDAO.insertWorkRole(newRole);

        List<WorkRole> workRoles = workRoleDAO.getAllWorkRoles();
        System.out.println("Number of WorkRoles: " + workRoles.size());

        // Kontrollera att det finns exakt 1 WorkRole i databasen
        assertEquals(1, workRoles.size(), "There should be exactly 1 WorkRole in the database");

        // Kontrollera att den hämtade rollen matchar den insatta rollen
        WorkRole retrievedRole = workRoles.get(0);
        System.out.println("Retrieved Role: " + retrievedRole.getTitle());
        assertEquals(newRole.getTitle(), retrievedRole.getTitle(), "The title of the retrieved role should match the inserted role");
    }


    @Test
    void testInsertMultipleWorkRoles() throws SQLException {
        WorkRole role1 = new WorkRole("Software Engineer", "Develops software.", 70000.00, LocalDate.now());
        WorkRole role2 = new WorkRole("Data Scientist", "Analyzes data.", 80000.00, LocalDate.now());

        workRoleDAO.insertWorkRole(role1);
        workRoleDAO.insertWorkRole(role2);

        List<WorkRole> workRoles = workRoleDAO.getAllWorkRoles();
        System.out.println("WorkRoles in database:" + workRoles.size());
        for (WorkRole role : workRoles) {
            System.out.println("Role: " + role.getTitle() + ", " + role.getDescription() + ", " + role.getSalary());
        }

        // Kontrollera att det finns exakt 2 WorkRoles i databasen
        assertEquals(2, workRoles.size(), "There should be exactly 2 WorkRoles in the database");
    }
}
