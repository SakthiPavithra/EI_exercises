import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class BookRoom {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/office_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter room ID: ");
            int roomId = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            System.out.print("Enter booking time (yyyy-MM-dd HH:mm:ss): ");
            String bookingTimeInput = scanner.nextLine();

            System.out.print("Enter duration in minutes: ");
            int duration = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            LocalDateTime bookingTime = LocalDateTime.parse(bookingTimeInput.replace(" ", "T"));
            LocalDateTime leavingTime = bookingTime.plus(duration, ChronoUnit.MINUTES);

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

                // Check if room is already booked for the given time
                String checkBookingSql = "SELECT COUNT(*) FROM booked_rooms WHERE room_id = ? AND ((booking_time <= ? AND leaving_time > ?) OR (booking_time < ? AND leaving_time >= ?))";
                PreparedStatement checkBookingStmt = connection.prepareStatement(checkBookingSql);
                checkBookingStmt.setInt(1, roomId);
                checkBookingStmt.setTimestamp(2, Timestamp.valueOf(bookingTime));
                checkBookingStmt.setTimestamp(3, Timestamp.valueOf(bookingTime));
                checkBookingStmt.setTimestamp(4, Timestamp.valueOf(leavingTime));
                checkBookingStmt.setTimestamp(5, Timestamp.valueOf(leavingTime));
                ResultSet bookingResultSet = checkBookingStmt.executeQuery();

                if (bookingResultSet.next() && bookingResultSet.getInt(1) > 0) {
                    System.out.println("Room ID " + roomId + " is already booked for the specified time.");
                    returnToDashboard(scanner);
                    return;
                }

                // Insert into booked_rooms table
                String insertBookingSql = "INSERT INTO booked_rooms (room_id, booking_time, duration, leaving_time) VALUES (?, ?, ?, ?)";
                PreparedStatement insertBookingStmt = connection.prepareStatement(insertBookingSql);
                insertBookingStmt.setInt(1, roomId);
                insertBookingStmt.setTimestamp(2, Timestamp.valueOf(bookingTime));
                insertBookingStmt.setInt(3, duration);
                insertBookingStmt.setTimestamp(4, Timestamp.valueOf(leavingTime));
                insertBookingStmt.executeUpdate();

                // Update room_status table
                String updateRoomStatusSql = "UPDATE room_status SET booking_status = TRUE, booking_time = ?, leaving_time = ? WHERE room_id = ?";
                PreparedStatement updateRoomStatusStmt = connection.prepareStatement(updateRoomStatusSql);
                updateRoomStatusStmt.setTimestamp(1, Timestamp.valueOf(bookingTime));
                updateRoomStatusStmt.setTimestamp(2, Timestamp.valueOf(leavingTime));
                updateRoomStatusStmt.setInt(3, roomId);
                updateRoomStatusStmt.executeUpdate();

                System.out.println("Room booked successfully.");
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
