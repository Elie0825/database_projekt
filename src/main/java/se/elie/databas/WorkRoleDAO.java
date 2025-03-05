package se.elie.databas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WorkRoleDAO {

    private final Connection connection;

    public WorkRoleDAO(Connection connection) {
        this.connection = connection;
    }

    public void insertWorkRole(WorkRole workRole) throws SQLException {
        String query = "INSERT INTO work_role (title, description, salary, creation_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, workRole.getTitle());
            pstmt.setString(2, workRole.getDescription());
            pstmt.setDouble(3, workRole.getSalary());
            pstmt.setDate(4, Date.valueOf(workRole.getCreationDate()));

            pstmt.executeUpdate();
            JDBCUtil.commit(connection);  // Commit efter att ha infört nya data
        } catch (SQLException e) {
            JDBCUtil.rollback(connection); // Om något går fel, gör rollback
            throw e;
        }
    }

    public List<WorkRole> getAllWorkRoles() throws SQLException {
        List<WorkRole> roles = new ArrayList<>();
        String query = "SELECT * FROM work_role";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int roleId = rs.getInt("role_id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                double salary = rs.getDouble("salary");
                Date creationDate = rs.getDate("creation_date");

                roles.add(new WorkRole(roleId, title, description, salary, creationDate.toLocalDate()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    public WorkRole getWorkRoleById(int id) throws SQLException {
        String query = "SELECT * FROM work_role WHERE role_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    double salary = rs.getDouble("salary");
                    Date creationDate = rs.getDate("creation_date");
                    return new WorkRole(id, title, description, salary, creationDate.toLocalDate());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateWorkRole(Scanner scanner) throws SQLException {
        System.out.println("Enter work role ID to update:");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("Enter new title:");
        String title = scanner.nextLine();
        System.out.println("Enter new description:");
        String description = scanner.nextLine();
        System.out.println("Enter new salary:");
        double salary = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        String query = "UPDATE work_role SET title = ?, description = ?, salary = ? WHERE role_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setDouble(3, salary);
            pstmt.setInt(4, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JDBCUtil.commit(connection);  // Commit if update was successful
                System.out.println("Work role updated.");
            } else {
                System.out.println("No work role found with that ID.");
            }
        } catch (SQLException e) {
            JDBCUtil.rollback(connection); // Rollback if there's an error
            e.printStackTrace();
        }
    }

    public void deleteWorkRole(Scanner scanner) throws SQLException {
        System.out.println("Enter work role ID to delete:");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String query = "DELETE FROM work_role WHERE role_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JDBCUtil.commit(connection);  // Commit if deletion was successful
                System.out.println("Work role deleted.");
            } else {
                System.out.println("No work role found with that ID.");
            }
        } catch (SQLException e) {
            JDBCUtil.rollback(connection); // Rollback if there's an error
            e.printStackTrace();
        }
    }
}
