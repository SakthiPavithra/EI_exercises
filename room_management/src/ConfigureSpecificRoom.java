import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ConfigureSpecificRoom {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/office_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter room ID to configure: ");
        int roomId = scanner.nextInt();
        scanner.nextLine();  // Consume the newline

        System.out.print("Enter maximum capacity for room ID " + roomId + ": ");
        int maxCapacity = scanner.nextInt();
        scanner.nextLine();
        

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Load and register MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Check if the room ID exists in the room_status table
            String checkRoomSql = "SELECT COUNT(*) FROM room_status WHERE room_id = ?";
            PreparedStatement checkRoomStmt = connection.prepareStatement(checkRoomSql);
            checkRoomStmt.setInt(1, roomId);
            ResultSet resultSet = checkRoomStmt.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                // Room ID exists, insert into room_config table
                String insertSql = "INSERT INTO room_config (room_id, max_capacity) VALUES (?, ?)";
                PreparedStatement insertStmt = connection.prepareStatement(insertSql);
                insertStmt.setInt(1, roomId);
                insertStmt.setInt(2, maxCapacity);

                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0 && maxCapacity > 0) {
                    System.out.println("Room " + roomId + "  maximum capacity set to " + maxCapacity);
                } else {
                    System.out.println("Invalid capacity. Please enter a valid positive number");
                }
            } else {
                // Room ID does not exist
                System.out.println("Room ID " + roomId + " does not exist.");
                Dashboard.main(null);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Database operation failed.");
            System.out.println("Invalid capacity. Please enter a valid positive number");
        }
        finally{ 
        System.out.println("Enter exit to go to main menu");
        String e=scanner.nextLine();
        if(e.equals("exit"))
        Dashboard.main(null);
 
        
         scanner.close();
    }
}
}
