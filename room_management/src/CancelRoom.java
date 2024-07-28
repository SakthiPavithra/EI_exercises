import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class CancelRoom {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/office_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter room ID: ");
            int roomId = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                // Load and register MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Check if room ID exists in booked_rooms table
                String checkBookingSql = "SELECT COUNT(*) FROM booked_rooms WHERE room_id = ?";
                PreparedStatement checkBookingStmt = connection.prepareStatement(checkBookingSql);
                checkBookingStmt.setInt(1, roomId);
                ResultSet bookingResultSet = checkBookingStmt.executeQuery();

                if (bookingResultSet.next() && bookingResultSet.getInt(1) > 0) {
                    // Delete from booked_rooms table
                    String deleteBookingSql = "DELETE FROM booked_rooms WHERE room_id = ?";
                    PreparedStatement deleteBookingStmt = connection.prepareStatement(deleteBookingSql);
                    deleteBookingStmt.setInt(1, roomId);
                    deleteBookingStmt.executeUpdate();
                }

                // Check if room ID exists in occupants table
                String checkOccupantSql = "SELECT COUNT(*) FROM occupant WHERE room_id = ?";
                PreparedStatement checkOccupantStmt = connection.prepareStatement(checkOccupantSql);
                checkOccupantStmt.setInt(1, roomId);
                ResultSet occupantResultSet = checkOccupantStmt.executeQuery();

                if (occupantResultSet.next() && occupantResultSet.getInt(1) > 0) {
                    // Delete from occupants table
                    String deleteOccupantSql = "DELETE FROM occupant WHERE room_id = ?";
                    PreparedStatement deleteOccupantStmt = connection.prepareStatement(deleteOccupantSql);
                    deleteOccupantStmt.setInt(1, roomId);
                    deleteOccupantStmt.executeUpdate();
                }

                // Update room_status table
                String updateRoomStatusSql = "UPDATE room_status SET status = 'unoccupied', booking_status = FALSE, ac_status = FALSE, light_status = FALSE, booking_time = NULL, leaving_time = NULL WHERE room_id = ?";
                PreparedStatement updateRoomStatusStmt = connection.prepareStatement(updateRoomStatusSql);
                updateRoomStatusStmt.setInt(1, roomId);
                updateRoomStatusStmt.executeUpdate();

                System.out.println("Room booking and occupant information cancelled successfully.");
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
