import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AddOccupant {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/office_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter room ID: ");
            int roomId = scanner.nextInt();
            scanner.nextLine();  // Consume the newline

            System.out.print("Enter number of persons: ");
            int numberOfPersons = scanner.nextInt();
            scanner.nextLine();  // Consume the newline

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // Load and register MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Check if room ID exists in room_status table
                String checkRoomSql = "SELECT COUNT(*) FROM room_status WHERE room_id = ?";
                PreparedStatement checkRoomStmt = connection.prepareStatement(checkRoomSql);
                checkRoomStmt.setInt(1, roomId);
                ResultSet roomResultSet = checkRoomStmt.executeQuery();

                if (!roomResultSet.next() || roomResultSet.getInt(1) == 0) {
                    System.out.println("Room ID " + roomId + " does not exist.");
                    returnToDashboard(scanner);
                    return;
                }

                // Check if room ID is configured and if the number of persons is valid
                String checkCapacitySql = "SELECT max_capacity FROM room_config WHERE room_id = ?";
                PreparedStatement checkCapacityStmt = connection.prepareStatement(checkCapacitySql);
                checkCapacityStmt.setInt(1, roomId);
                ResultSet capacityResultSet = checkCapacityStmt.executeQuery();

                boolean isConfigured = capacityResultSet.next();
                int maxCapacity = 0;
                if (isConfigured) {
                    maxCapacity = capacityResultSet.getInt("max_capacity");
                }

                // Check if room ID already exists in occupants table
                String checkOccupantSql = "SELECT no_of_persons FROM occupant WHERE room_id = ?";
                PreparedStatement checkOccupantStmt = connection.prepareStatement(checkOccupantSql);
                checkOccupantStmt.setInt(1, roomId);
                ResultSet occupantResultSet = checkOccupantStmt.executeQuery();

                int totalPersons = numberOfPersons;
                if (occupantResultSet.next()) {
                    int existingPersons = occupantResultSet.getInt("no_of_persons");
                    totalPersons += existingPersons;

                    if (isConfigured && totalPersons > maxCapacity) {
                        System.out.println("The total number of persons exceeds the maximum capacity of the room.");
                        returnToDashboard(scanner);
                        return;
                    }
                    if (totalPersons <= 1) {
                        System.out.println("The total number of persons must be greater than 1.");
                        returnToDashboard(scanner);
                        return;
                    }

                    // Update occupants table
                    String updateOccupantSql = "UPDATE occupant SET no_of_persons = ? WHERE room_id = ?";
                    PreparedStatement updateOccupantStmt = connection.prepareStatement(updateOccupantSql);
                    updateOccupantStmt.setInt(1, totalPersons);
                    updateOccupantStmt.setInt(2, roomId);
                    updateOccupantStmt.executeUpdate();
                } else {
                    if (numberOfPersons <= 1) {
                        System.out.println("The total number of persons must be greater than 1.");
                        returnToDashboard(scanner);
                        return;
                    }
                    if (isConfigured && numberOfPersons > maxCapacity) {
                        System.out.println("The total number of persons exceeds the maximum capacity of the room.");
                        returnToDashboard(scanner);
                        return;
                    }

                    // Insert into occupants table
                    String insertOccupantSql = "INSERT INTO occupant (room_id, no_of_persons) VALUES (?, ?)";
                    PreparedStatement insertOccupantStmt = connection.prepareStatement(insertOccupantSql);
                    insertOccupantStmt.setInt(1, roomId);
                    insertOccupantStmt.setInt(2, numberOfPersons);
                    insertOccupantStmt.executeUpdate();
                }

                if (isConfigured) {
                    // Update max capacity in room_config table
                    String updateCapacitySql = "UPDATE room_config SET max_capacity = max_capacity - ? WHERE room_id = ?";
                    PreparedStatement updateCapacityStmt = connection.prepareStatement(updateCapacitySql);
                    updateCapacityStmt.setInt(1, numberOfPersons);
                    updateCapacityStmt.setInt(2, roomId);
                    updateCapacityStmt.executeUpdate();
                }

                // Update room_status table to set ac_status, light_status to true and status to occupied
                String updateRoomStatusSql = "UPDATE room_status SET ac_status = TRUE, light_status = TRUE, status = 'occupied' WHERE room_id = ?";
                PreparedStatement updateRoomStatusStmt = connection.prepareStatement(updateRoomStatusSql);
                updateRoomStatusStmt.setInt(1, roomId);
                updateRoomStatusStmt.executeUpdate();

                System.out.println("Occupants added and room status updated successfully.");
                returnToDashboard(scanner);

            } catch (ClassNotFoundException e) {
                System.err.println("MySQL JDBC Driver not found.");
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("Database operation failed.");
                e.printStackTrace();
            }
        } finally {
            scanner.close();
        }
    }

    private static void returnToDashboard(Scanner scanner) {
        System.out.println("Returning to main menu...");
        Dashboard.main(null);
        scanner.close();
    }
}
