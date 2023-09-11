import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Theatre {
    // Sorting the tickets by price method
    public static void sort_tickets(){
        List<Ticket> sortedList = new ArrayList<>(Theatre.tickets);
        for (int i = 0; i< sortedList.size() -1; i++){
            int minIndex =i;
            for (int j = i + 1; j < sortedList.size(); j++) {
                if (sortedList.get(j).getPrice() < sortedList.get(minIndex).getPrice()) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                swap(sortedList, i, minIndex);
            }
        }
        for(Ticket ticket: sortedList) {
            ticket.print();
            System.out.println();
        }

    }

    private static void swap(List<Ticket> list, int i, int j) {
        Ticket tkt = list.get(i);
        list.set(i, list.get(j));
        list.set(j, tkt);
    }


    // method to show tickets info
    public static void show_ticket_info() {
        if (tickets.size() == 0) {
            System.out.println("No tickets sold yet.");
            return;
        }

        for (int i = 0; i < tickets.size(); i++) {
            Ticket tkt = tickets.get(i);

            System.out.println("Ticket " + (i+1) + ":");
            System.out.println("\tName: " + tkt.getPerson().getName());
            System.out.println("\tRow: " + tkt.getRow());
            System.out.println("\tSeat: " + tkt.getSeat());
            System.out.println();
        }

        System.out.println("---------------------------------");
        System.out.println("Total price is : $" + total_price + ".00"); // display the total price of tickets
    }

    // completing the load method
    public static void load() {
        try {
            File file = new File("seatInfo.txt");
            Scanner file_reader = new Scanner(file);
            int line_count = 0;

            while (file_reader.hasNextLine()) {
                String row_line = file_reader.nextLine();
                if (line_count == 0) {
                    setRowFromLine(row1, row_line);
                } else if (line_count == 1) {
                    setRowFromLine(row2, row_line);
                } else if (line_count == 2) {
                    setRowFromLine(row3, row_line);
                } else {
                    System.out.println("Invalid line in file");
                }
                line_count++;
            }
            file_reader.close();
            System.out.println("File has been loaded successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred while reading from file.");
            e.printStackTrace();
        }
    }

    // reading each character from the file and store it in rows
    private static void setRowFromLine(int[] row, String line) {
        for (int i = 0; i < row.length; i++) {
            char character = line.charAt(i);
            if (character == 'O') {
                row[i] = 0;
            } else {
                row[i] = 1;
            }
        }
    }

    // save to a file method
    public static void save() {
        try {
            FileWriter writer = new FileWriter("seatInfo.txt");

            writeRowToFile(writer, row1);
            writeRowToFile(writer, row2);
            writeRowToFile(writer, row3);

            writer.close();
            System.out.println("Array has been written to file successfully!");

        } catch (IOException e) {
            System.out.println("An error occurred while writing to file.");
            e.printStackTrace();
        }
    }

    // Write string values to the file
    public static void writeRowToFile(FileWriter writer, int[] row) throws IOException {
        for (int j : row) {
            if (j == 0) {
                writer.write("O");
            } else {
                writer.write("X");
            }
        }
        writer.write(System.lineSeparator());
    }

    // Show available seats
    public static void show_available() {
        showRowAvailability(1, row1);
        showRowAvailability(2, row2);
        showRowAvailability(3, row3);
    }

    // check availability
    public static void showRowAvailability(int rowNum, int[] row) {
        System.out.print("Seats available in row " + rowNum + ": ");
        for (int i = 0; i < row.length; i++) {
            if (row[i] == 0) {
                System.out.print(i+1);
                if (i < row.length - 1) {
                    System.out.print(", ");
                } else {
                    System.out.print(".");
                }
            }
        }
        System.out.println();
    }


    // Cancel ticket method
    public static void cancel_ticket() {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter the row number: ");
        int row = input.nextInt();
        System.out.print("Enter the Seat number: ");
        int seat = input.nextInt();

        // Find the ticket to cancel based on the seat location
        for (Ticket ticket : tickets) {
            if (ticket.getRow() == row && ticket.getSeat() == seat) {
                // Remove the ticket from the ArrayList
                tickets.remove(ticket);

                // Reduce the price and update the arrays by the row number
                switch (row) {
                    case 1:
                        if (check_seat_availability(seat, row1, 1, 0)) {
                            total_price -= row1_price;
                        } else {
                            cancel_ticket();
                        }
                        break;
                    case 2:
                        if (check_seat_availability(seat, row2, 1, 0)) {
                            total_price -= row2_price;
                        } else {
                            cancel_ticket();
                        }
                        break;
                    case 3:
                        if (check_seat_availability(seat, row3, 1, 0)) {
                            total_price -= row3_price;
                        } else {
                            cancel_ticket();
                        }
                        break;
                    default:
                        System.out.println("Invalid row number");
                        return;
                }

                System.out.println("Ticket canceled successfully.");
                return;
            }
        }

        // If no ticket was found with the given seat location
        System.out.println("No ticket found with row " + row + " and seat " + seat);
    }


    // Check seat availability
    public static boolean check_seat_availability(int seat_number, int[] row_array, int check, int to) {
        if (seat_number < 1 || seat_number > row_array.length) {
            System.out.println("Please Enter a valid Seat Number!");
            return false;

        } else if (row_array[seat_number - 1] == check) {
            row_array[seat_number - 1] = to;
            return true;

        } else {
            System.out.println("The seat is already taken...");
            System.out.print("If you want to see the available seats press '1' or enter any key to retry: ");
            try {
                Scanner seat = new Scanner(System.in);
                int see_seats = seat.nextInt();
                if (see_seats == 1) {
                    print_seating_area();
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please try again.");
            }

            return false;
        }
    }

    // defining the total price variable that can be accessed form any method in class
    static int total_price = 0;
    // declaring the variables to carry the price of seats by row number
    static int row1_price;
    static int row2_price;
    static int row3_price;

    // Buy tickets method
    public static void buy_ticket() {
        Scanner seat = new Scanner(System.in);

        int row_number;
        int seat_number;
        int price;

        while (true) {
            try {
                System.out.print("Enter the row number: ");
                String row_input = seat.nextLine();
                row_number = Integer.parseInt(row_input);

                System.out.print("Enter the seat number: ");
                String seat_input = seat.nextLine();
                seat_number = Integer.parseInt(seat_input);

                if (row_number == 1) {
                    if (check_seat_availability(seat_number, row1, 0, 1)) {
                        price = row1_price;
                        break;
                    }
                } else if (row_number == 2) {
                    if (check_seat_availability(seat_number, row2, 0, 1)) {
                        price = row2_price;
                        break;
                    }
                } else if (row_number == 3) {
                    if (check_seat_availability(seat_number, row3, 0, 1)) {
                        price = row3_price;
                        break;
                    }
                } else {
                    System.out.println("Please enter a valid row number!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a number.");
            }
        }
        personal_info(row_number, seat_number, price);
    }


    // Getting personal info for the ticket
    public static void personal_info(int row, int seat, int ticket_price){
        Scanner input = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String name = input.next();

        System.out.print("Enter your surname: ");
        String surname = input.next();

        System.out.print("Enter your email: ");
        String email = input.next();

        // Inputting the values to the person class with an object person
        Person person = new Person(name, surname, email);

        total_price += ticket_price;

        // Inputting the values to the Ticket class with an object ticket
        Ticket ticket = new Ticket(row, seat, ticket_price, person);
        tickets.add(ticket);

        System.out.println("\nTicket Purchased Successfully!!");
        ticket.print();
    }

    // Print seating area method
    public static void print_seating_area() {
        // Print stage
        System.out.println("\t\t\t  ***********");
        System.out.println("\t\t\t  *  STAGE  *");
        System.out.println("\t\t\t  ***********");

        // print seating structure
        System.out.print("Row 1 :     ");
        for (int i=0; i< row1.length; i++){  // seat structure for row 1
            if (i == (row1.length / 2)) {
                System.out.print("\t");
            } if (row1[i] == 0){
                System.out.print("O");
            } else {
                System.out.print("X");
            }
        }

        System.out.println();

        System.out.print("Row 2 :   ");
        for (int i=0; i< row2.length; i++){  // seat structure for row 2
            if (i == (row2.length / 2)) {
                System.out.print("\t");
            } if (row2[i] == 0){
                System.out.print("O");
            } else {
                System.out.print("X");
            }
        }

        System.out.println();

        System.out.print("Row 3 : ");
        for (int i=0; i< row3.length; i++){  // seat structure for row 3
            if (i == (row3.length / 2)) {
                System.out.print("\t");
            } if (row3[i] == 0){
                System.out.print("O");
            } else {
                System.out.print("X");
            }
        }

        System.out.println();
    }

    // display menu method
    public static void menu(){
        System.out.println("\n-------------------------------------------------");
        System.out.println("Please select an option:");
        System.out.println("1) Buy a ticket");
        System.out.println("2) Print seating area");
        System.out.println("3) Cancel ticket");
        System.out.println("4) List available seats");
        System.out.println("5) Save to file");
        System.out.println("6) Load from file");
        System.out.println("7) Print ticket information and total price");
        System.out.println("8) Sort tickets by price");
        System.out.println("0) Quit");
        System.out.println("-------------------------------------------------");
    }

    // validate the choice variable in switch
    public static boolean validate(int choice){
        return choice <= -1 || choice >= 9;
    }

    static int[] row1 = new int[12];
    static int[] row2 = new int[16];
    static int[] row3 = new int[20];

    // Array list declaration
    static ArrayList<Ticket> tickets = new ArrayList<>();

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        while (true) {
            try {
                // Initiate the theatre system for the day
                System.out.print("Enter the price of row 1 seats : $");
                row1_price = input.nextInt(); // setting row 1 price
                input.nextLine();
                System.out.print("Enter the price of row 2 seats : $");
                row2_price = input.nextInt(); // setting row 2 price
                input.nextLine();
                System.out.print("Enter the price of row 3 seats : $");
                row3_price = input.nextInt(); // setting row 3 price
                input.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Error: Please enter a number for price.");
                input.nextLine();
            }
        }

        System.out.println("=======================================");  //Welcome msg
        System.out.println("|                                     |");
        System.out.println("|           Welcome to the            |");
        System.out.println("|             Theatre!                |");
        System.out.println("|                                     |");
        System.out.println("=======================================");

        Arrays.fill(row1, 0);
        Arrays.fill(row2, 0);
        Arrays.fill(row3, 0);

        print_seating_area();

        menu(); // display menu

        // Executing the choice
        boolean quit = false;
        while (!quit) {

            // Get the user choice
            System.out.print("\nEnter Your Choice Here: ");
            int choice;
            try {
                choice = input.nextInt();
                input.nextLine(); // consume the leftover newline character

                if (validate(choice)) {
                    System.out.println("Please Enter a valid choice between 0 - 8");
                    continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please Enter Number...");
                input.nextLine(); // consume the leftover newline character
                continue;
            }

            switch (choice) {
                case 1:
                    // Buy a ticket
                    System.out.println("\n----- BUY TICKET -----\n");
                    while (true) { // ask for another seat
                        buy_ticket();
                        System.out.print("\nWould you like to book another seat (Enter yes or no) : ");
                        String ans = input.next();
                        if (ans.equalsIgnoreCase("yes")) {
                            System.out.println("For the new seat... ");
                        } else if (ans.equalsIgnoreCase("no")) {
                            break;
                        } else {
                            System.out.println("Please enter a valid response");
                        }
                    }
                    break;


                case 2:
                    // Print seating area
                    System.out.println("\n----- PRINT SEATING AREA -----\n");
                    print_seating_area();
                    break;

                case 3:
                    // Cancel ticket
                    System.out.println("\n----- CANCEL TICKET -----\n");
                    cancel_ticket();
                    break;

                case 4:
                    // List available seats
                    System.out.println("\n----- SHOW AVAILABLE SEATS -----\n");
                    show_available();
                    break;

                case 5:
                    // Save to file
                    System.out.println("\n----- SAVE SEATING AREA -----\n");
                    save();
                    break;

                case 6:
                    // Load from file
                    System.out.println("\n----- LOAD SEATING AREA -----\n");
                    load();
                    break;

                case 7:
                    // Print ticket information and total price
                    System.out.println("\n----- TICKET INFORMATION AND TOTAL PRICE -----\n");
                    show_ticket_info();
                    break;

                case 8:
                    System.out.println("\n----- SORT TICKETS BY PRICE -----\n");
                    // Sort tickets by price
                    sort_tickets();
                    break;

                case 0:
                    quit = true;
                    break;

                default:
                    System.out.println("Not a valid response. Try again...");
            }
            menu();
        }
    }
}

