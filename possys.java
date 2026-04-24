import java.util.Scanner;
import java.time.LocalTime;
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.io.*;
public class possys {
    static DecimalFormat df = new DecimalFormat("0.00");
    static Scanner scanner = new Scanner(System.in);
    static final String fileName = "users.txt";

    public static void main(String[] args) {

        //Decimal and Scanner//


        //Display Time
        int hour = displayTime();

        //Login System//
        String username = newloginSystem();
        boolean isMember = !username.equals("Guest");


        //Menu, Prices and Discounts Array
        String[] menu = {"Iced Latte", "Iced Cappuccino", "Iced Americano", "Matcha Latte", "Cake", "Egg Tart", "Donut", "Water"};
        double[] price = {2.0, 2.5, 1.5, 2.5, 3.0, 1.5, 1.0, 0.75};
        boolean[] isDiscountable = {false, false, false, false, true, true, true, false};


        ArrayList<String> itemsOrdered = new ArrayList<>();
        ArrayList<Integer> quantitiesOrdered = new ArrayList<>();
        ArrayList<Double> discountAmount = new ArrayList<>();
        ArrayList<Double> originalPrice = new ArrayList<>();
        ArrayList<Double> subtotalList = new ArrayList<>();


        //Ordering Loop
        boolean ordering = true;

        //Displaying Menu
        while (ordering) {
            displayMenu(menu, price);


            //Asking for quantity
            System.out.print("Select item (1-8): ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            if (choice < 1 || choice > menu.length) {
                System.out.println("Invalid Choice. Please try again!");
                continue;
            }


            //Quantity and Error Handling
            int amount = 0;
            while (true) {
                System.out.print("Enter your quantity: ");
                if (scanner.hasNextInt()) {
                    amount = scanner.nextInt();
                    scanner.nextLine();
                    if (amount > 0) {
                        break;
                    } else {
                        System.out.println("Quantity must be greater than 0.");
                    }
                } else {

                    System.out.println("Please enter a valid number");
                    scanner.nextLine();

                }
            }

            //Adding Items into Array
            String itemsAdd = menu[choice - 1];
            double itemPrice = price[choice - 1];
            originalPrice.add(itemPrice);

            itemsOrdered.add(itemsAdd);
            quantitiesOrdered.add(amount);


            //Calculating Discounts and Non-Discounts Subtotal
            double discountPercent = 0;
            if (isDiscountable[choice - 1] && (hour >= 17 && hour <= 22)) {
                discountPercent = 30;
            }
            discountAmount.add(discountPercent);

            //Calculating the Subtotal Price
            double subtotalDiscountedPrice = itemPrice * amount * (1 - (discountPercent / 100));
            subtotalList.add(subtotalDiscountedPrice);

            //Looping the order
            String orderAgain = "";
            while (true) {
                System.out.print("Do you want to order again? [y/n]: ");
                orderAgain = scanner.next();
                if (orderAgain.equalsIgnoreCase("y") || orderAgain.equalsIgnoreCase("n")) {
                    break;
                } else {
                    System.out.println("Invalid Input");
                }
            }

            if (!orderAgain.equalsIgnoreCase("y")) {
                ordering = false;
            }
        }

        // Calculate total quantity
        int totalQty = 0;
        for (int s : quantitiesOrdered) {
            totalQty += s;
        }
        double totalPrice = 0;
        for (double s : subtotalList) {
            totalPrice += s;
        }

        double bulkDiscount = 0;
        //Discount if order 5 or more items
        if (totalQty >= 5) {
            bulkDiscount += 10;
        }

        //Discount if customer has a membership
        if (isMember) {
            bulkDiscount += 10;
        }

        printReceipt(itemsOrdered, originalPrice, quantitiesOrdered, discountAmount, subtotalList, totalPrice, bulkDiscount);


    }

    static int displayTime() {
        LocalTime currentTime = LocalTime.now();
        int hour = currentTime.getHour();
        int minute = currentTime.getMinute();
        String minuteFixed = (minute < 10 ? "0" : "") + minute;
        if (hour > 12) {
            System.out.println("Current Time: " + (hour - 12) + ":" + minuteFixed + " PM");
        } else if (hour == 12) {
            System.out.println("Current Time: " + hour + ":" + minuteFixed + " PM");
        } else if (hour == 0) {
            System.out.println("Current Time: 12: " + minuteFixed + " AM");
        } else {
            System.out.println("Current Time: " + hour + ":" + minuteFixed + " AM");
        }
        return hour;
    }

    static void displayMenu(String[] menu, double[] price) {
        System.out.println("\nMenu");
        System.out.printf("%-2s %-18s %-6s\n",
                "",
                "Food and Drinks",
                "Price");
        for (int i = 0; i < menu.length; i++) {
            System.out.printf("%-1d: %-18s $%-6.2f\n",
                    (i + 1),
                    menu[i],
                    price[i]);
        }


    }


    static void printReceipt(ArrayList<String> itemsOrdered, ArrayList<Double> originalPrice, ArrayList<Integer> quantitiesOrdered, ArrayList<Double> discountAmount, ArrayList<Double> subtotalList, double totalPrice, double bulkDiscount) {
        System.out.println("\n\nOrder Receipt");
        System.out.println("--- Final Receipt ---\n");

        System.out.printf("%-18s %-10s %-7s %-6s  %-6s\n",
                "Item",
                "Price",
                "Qty",
                "Disc%",
                "Subtotal");

        for (int i = 0; i < itemsOrdered.size(); i++) {
            System.out.printf("%-18s $%-9.2f x%-7d %-6.0f  $%-6.2f\n",
                    itemsOrdered.get(i),
                    originalPrice.get(i),
                    quantitiesOrdered.get(i),
                    discountAmount.get(i),
                    subtotalList.get(i));

        }
        System.out.println("\nSubtotal: " + "$" + df.format(totalPrice));
        System.out.println("Bulk Discount: " + bulkDiscount + "%");
        System.out.println("Grand Total: " + "$" + df.format(totalPrice * ((100 - bulkDiscount) / 100)));
    }


    static boolean loginSystem() {
        boolean isMember = false;
        String hasMember = "";
        while (true) {
            System.out.print("Do you have membership account? [y/n]: ");
            hasMember = scanner.nextLine();
            if (hasMember.equalsIgnoreCase("y") || hasMember.equalsIgnoreCase("n")) {
                break;
            } else {
                System.out.println("Invalid Input");
            }
        }

        if (hasMember.equalsIgnoreCase("y")) {
            int attempt = 0;
            while (attempt < 3) {
                System.out.print("Enter your username: ");
                String userName = scanner.nextLine();
                System.out.print("Enter your password: ");
                String passWord = scanner.nextLine();


                if (userName.equals("member") && passWord.equals("member123")) {
                    isMember = true;
                    System.out.println("\nMembership benefits granted\n");
                    break;
                } else {
                    System.out.println("Incorrect Username or Password");
                    attempt += 1;
                    System.out.println("Remaining Attempts: " + (3 - attempt));
                    if (attempt >= 3) {
                        System.out.println("\nToo many attempts. Logging in as normal user\n");
                    }
                }
            }
        } else {
            System.out.println("\nLogging in as normal user\n");
        }
        return isMember;
    }



    static String newloginSystem() {
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Guest");
        System.out.print("Choose: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        while (true) {
            if (choice != 1 && choice != 2 && choice != 3) {
                System.out.print("Invalid Choice. Select again: ");
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                if (choice == 1) {
                    return register();
                } else if (choice == 2) {
                    return loginUser();
                } else {
                    return "Guest";
                }
            }
        }

    }


    static String register() {
        while (true) {
            System.out.println("Registration for membership cost 5$. Proceed to payment? [y/n]");
            String userChoice = scanner.nextLine();
            if (userChoice.equalsIgnoreCase("y")) {
                String nameInput;

                while (true) {
                    System.out.print("Enter your username: ");
                    nameInput = scanner.nextLine();
                    if (userExists(nameInput)) {
                        System.out.println("Username already exists. Please choose another username");
                    } else {
                        break;
                    }
                }
                System.out.print("Enter your password: ");
                String pwInput = scanner.nextLine();

                try {
                    FileWriter fw = new FileWriter(fileName, true);
                    fw.write(nameInput + ":" + pwInput + "\n");
                    fw.close();
                    System.out.println("Registered Successfully");
                    return nameInput;
                } catch (IOException e) {
                    System.out.println("Error registering");
                    return null;
                }
            } else if (userChoice.equalsIgnoreCase("n")) {
                System.out.println("Proceeding as guest.");
                return "Guest";
            }
        }

    }

    static boolean userExists(String username) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equals(username)) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {

        }
        return false;
    }

    static String loginUser() {
        int attempt = 3;
        while (true) {
            System.out.print("Enter your username: ");
            String userInput = scanner.nextLine();
            System.out.print("Enter your password: ");
            String pwInput = scanner.nextLine();

            try {
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                String line;
                while ((line = reader.readLine()) != null ) {
                    String[] parts = line.split(":");
                    if (parts[0].equals(userInput) && parts[1].equals(pwInput)) {
                        System.out.println("Login Successful!");
                        return userInput;
                    }
                }
                reader.close();
            } catch (IOException e) {
                System.out.println("No users registered yet. Logging in as guest.");
                return "Guest";
            }
            attempt -= 1;
            System.out.println("Invalid username or password. Attempt Remaining: " + attempt);
            if (attempt <= 0) {
                System.out.println("Too many attempts. Logging in as guest");
                return "Guest";
            }
        }

    }
}





