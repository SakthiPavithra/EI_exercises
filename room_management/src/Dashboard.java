import java.util.Scanner;

public class Dashboard {

    private enum State {
        MAIN_MENU,
        CONFIGURE_ROOM,
        CONFIGURE_SPECIFIC_ROOM,
        ADD_OCCUPANT,
        BOOK_ROOM,
        CANCEL_ROOM,
        UNCONFIGURE_ROOM,
        LOGOUT
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        State currentState = State.MAIN_MENU;

        while (currentState != State.LOGOUT) {
            switch (currentState) {
                case MAIN_MENU:
                    currentState = showMainMenu(scanner);
                    break;
                case CONFIGURE_ROOM:
                    ConfigureRoom.main(null);
                    currentState = State.MAIN_MENU;
                    break;
                case CONFIGURE_SPECIFIC_ROOM:
                    ConfigureSpecificRoom.main(null);
                    currentState = State.MAIN_MENU;
                    break;
                case ADD_OCCUPANT:
                    AddOccupant.main(null);
                    currentState = State.MAIN_MENU;
                    break;
                case BOOK_ROOM:
                    BookRoom.main(null);
                    currentState = State.MAIN_MENU;
                    break;
                case CANCEL_ROOM:
                    CancelRoom.main(null);
                    currentState = State.MAIN_MENU;
                    break;
                case UNCONFIGURE_ROOM:
                    UnConfigureRoom.main(null);
                    currentState = State.MAIN_MENU;
                    break;    
                default:
                    System.out.println("Unknown state.");
                    currentState = State.LOGOUT;
                    break;
            }
        }

        System.out.println("Logging out...");
        scanner.close();
        System.exit(0);
    }

    private static State showMainMenu(Scanner scanner) {
        System.out.println("\n--- Dashboard ---");
        System.out.println("1. Configure Room");
        System.out.println("2. Configure Specific Room");
        System.out.println("3. Add Occupant");
        System.out.println("4. Book Room");
        System.out.println("5. Cancel Room");
        System.out.println("6. UnConfigure Room");
        System.out.println("0. Logout");

        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        switch (choice) {
            case 1:
                return State.CONFIGURE_ROOM;
            case 2:
                return State.CONFIGURE_SPECIFIC_ROOM;
            case 3:
                return State.ADD_OCCUPANT;
            case 4:
                return State.BOOK_ROOM;
            case 5:
                return State.CANCEL_ROOM;
            case 6:
                return State.UNCONFIGURE_ROOM;
            case 0:
                return State.LOGOUT;
            default:
                System.out.println("Invalid choice. Please enter a number between 0 and 6.");
                return State.MAIN_MENU;
        }
    }
}
