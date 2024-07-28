import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UnConfigureRoom {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/office_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Integer> unconfiguredRoomIds = new ArrayList<>();

        System.out.print("Enter the number of rooms to unconfigure: ");
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

            String deleteSQL = "DELETE FROM room_status WHERE room_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL);

            for (int i = 0; i < numberOfRooms; i++) {
                System.out.print("Enter room ID for room " + (i + 1) + ": ");
                int roomId = scanner.nextInt();
                scanner.nextLine();  // Consume the newline

                preparedStatement.setInt(1, roomId);

                try {
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        unconfiguredRoomIds.add(roomId);
                        //System.out.println("Room ID " + roomId + " has been configured.");
                    } else {
                        System.out.println("Failed to unconfigure Room ID " + roomId + ".");
                    }
                } catch (SQLException e) {
                    System.err.println("Error unconfiguring room ID " + roomId + ": " + e.getMessage());
                }
            }

            // Display summary
            if (!unconfiguredRoomIds.isEmpty()) {
                System.out.println("\nOffice unconfigured with " + unconfiguredRoomIds.size() + " rooms.");
                System.out.println("UnConfigured room IDs: " + unconfiguredRoomIds);
            } else {
                System.out.println("No rooms were unconfigured.");
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
