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

    public void insertWorkRole(WorkRole role) throws SQLException {
        String sql = "INSERT INTO work_role (title, description, salary, creation_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, role.getTitle());
            stmt.setString(2, role.getDescription());
            stmt.setDouble(3, role.getSalary());
            stmt.setDate(4, Date.valueOf(role.getCreationDate()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error during insert: " + e.getMessage());
            throw e;
        }
    }

    public List<WorkRole> getAllWorkRoles() throws SQLException {
        String sql = "SELECT * FROM work_role";
        List<WorkRole> roles = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                roles.add(new WorkRole(
                        rs.getInt("role_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDouble("salary"),
                        rs.getDate("creation_date").toLocalDate()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error during retrieval: " + e.getMessage());
            throw e;
        }
        return roles;
    }

    public WorkRole getWorkRoleById(int roleId) throws SQLException {
        String sql = "SELECT * FROM work_role WHERE role_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, roleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new WorkRole(
                            rs.getInt("role_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getDouble("salary"),
                            rs.getDate("creation_date").toLocalDate()
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during retrieval by ID: " + e.getMessage());
            throw e;
        }
        return null;
    }

    public void updateWorkRole(Scanner scanner) throws SQLException {
        System.out.println("Enter the ID of the work role to update:");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        // Kontrollera om arbetsrollen finns
        WorkRole existingRole = getWorkRoleById(id);
        if (existingRole == null) {
            System.out.println("No work role found with ID " + id);
            return;
        }

        // Begär nya värden för titel, beskrivning och lön
        System.out.println("Enter new title (leave empty to keep the current):");
        String newTitle = scanner.nextLine();
        if (newTitle.isEmpty()) {
            newTitle = existingRole.getTitle();  // Behåll den gamla titeln om ingen ny anges
        }

        System.out.println("Enter new description (leave empty to keep the current):");
        String newDescription = scanner.nextLine();
        if (newDescription.isEmpty()) {
            newDescription = existingRole.getDescription();  // Behåll den gamla beskrivningen om ingen ny anges
        }

        System.out.println("Enter new salary (enter 0 to keep the current):");
        double newSalary = scanner.nextDouble();
        if (newSalary == 0) {
            newSalary = existingRole.getSalary();  // Behåll den gamla lönen om ingen ny anges
        }
        scanner.nextLine();  // Consume newline

        // Uppdatera arbetsrollen i databasen
        String sql = "UPDATE work_role SET title = ?, description = ?, salary = ? WHERE role_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newTitle);
            pstmt.setString(2, newDescription);
            pstmt.setDouble(3, newSalary);
            pstmt.setInt(4, id);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Work role with ID " + id + " has been updated.");
            } else {
                System.out.println("Failed to update the work role with ID " + id);
            }
        }
    }


    public void deleteWorkRole(Scanner scanner) throws SQLException {
        System.out.println("Enter the ID of the work role to delete:");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String sql = "DELETE FROM work_role WHERE role_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Work role with ID " + id + " was deleted successfully.");
            } else {
                System.out.println("No work role found with ID " + id);
            }
        }
    }

}
