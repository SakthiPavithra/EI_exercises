import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConfigureRoom {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/office_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Integer> configuredRoomIds = new ArrayList<>();

        System.out.print("Enter the number of rooms to configure: ");
        int numberOfRooms = scanner.nextInt();
        scanner.nextLine();  // Consume the newline

        if (numberOfRooms <= 0) {
            System.out.println("Invalid number of rooms. Exiting.");
            scanner.close();
            return;
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Load and register MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            String insertSQL = "INSERT INTO room_status (room_id, booking_status, ac_status, light_status, booking_time, leaving_time) VALUES (?, FALSE, FALSE, FALSE, NULL, NULL)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);

            for (int i = 0; i < numberOfRooms; i++) {
                System.out.print("Enter room ID for room " + (i + 1) + ": ");
                int roomId = scanner.nextInt();
                scanner.nextLine();  // Consume the newline

                preparedStatement.setInt(1, roomId);

                try {
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        configuredRoomIds.add(roomId);
                        //System.out.println("Room ID " + roomId + " has been configured.");
                    } else {
                        System.out.println("Failed to configure Room ID " + roomId + ".");
                    }
                } catch (SQLException e) {
                    System.err.println("Error configuring room ID " + roomId + ": " + e.getMessage());
                }
            }

            // Display summary
            if (!configuredRoomIds.isEmpty()) {
                System.out.println("\nOffice configured with " + configuredRoomIds.size() + " rooms.");
                System.out.println("Configured room IDs: " + configuredRoomIds);
            } else {
                System.out.println("No rooms were configured.");
            }

        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Connection failed.");
            e.printStackTrace();
        } 

       System.out.println("Enter exit to go to main menu");
       String e=scanner.nextLine();
       if(e.equals("exit"))
       Dashboard.main(null);

       
        scanner.close();
    }

   
    
}
