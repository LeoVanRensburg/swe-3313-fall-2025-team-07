import java.util.Scanner;
public class DecisionTable {
    public static void main (String[]args){
        Scanner scanner = new Scanner(System.in);

        System.out.println("--------------------------");
        System.out.println("The Big 5 - Decision Table");
        System.out.println("--------------------------");

        int login;

        do {
            System.out.println("Login Menu");
            System.out.println("--------------------------");
            System.out.println("Do you want to login:");
            System.out.println("1. Yes");
            System.out.println("2. No");
            System.out.println("3. Exit");
            System.out.print("Option: ");
            login = scanner.nextInt();
            System.out.println();


            switch (login) {
                case 1:
                    System.out.println("Are you an Administrator? (Yes/No)");
                    String admin = scanner.next().toLowerCase();
                    scanner.nextLine();

                    if (admin.equals("yes")) {
                        System.out.println("Welcome back Admin!");
                        System.out.println("------------------------------");
                        System.out.println("is the item in Stock? (Yes/No)");
                        String itemInStock = scanner.next().toLowerCase();
                        scanner.nextLine();

                        if (itemInStock.equals("yes")) {

                            System.out.println("Is the item in the cart? (Yes/No)");
                            String inCart = scanner.next().toLowerCase();
                            scanner.nextLine();

                            System.out.println("Has any sale been made? (Yes/No)");
                            String salesMade = scanner.next().toLowerCase();
                            scanner.nextLine();

                            if (inCart.equals("yes") && salesMade.equals("yes")) {
                                int userOption;
                                do {
                                    System.out.println();
                                    System.out.println("What do you want to do?");
                                    System.out.println("1. Purchase ");
                                    System.out.println("2. Find and View Items");
                                    System.out.println("3. Manage Inventory");
                                    System.out.println("4. View Sales and History Report");
                                    System.out.println("5. Exit to Login Menu");
                                    System.out.print("Option: ");
                                    userOption = scanner.nextInt();
                                    System.out.println();

                                    switch (userOption) {
                                        case 1:
                                            System.out.println("R1");
                                            System.out.println("Purchasing Item...");
                                            System.out.println();
                                            break;
                                        case 2:
                                            System.out.println("R2");
                                            System.out.println("Finding and Viewing Items...");
                                            System.out.println();
                                            break;
                                        case 3:
                                            System.out.println("R3");
                                            System.out.println("Managing Inventory...");
                                            System.out.println();
                                            break;
                                        case 4:
                                            System.out.println("R4");
                                            System.out.println("Viewing Sales and History Report...");
                                            System.out.println();
                                            break;
                                        case 5:
                                            System.out.println("Going back to Login Menu...");
                                            System.out.println();
                                            break;

                                    }
                                } while (userOption != 5);
                            } else if (inCart.equals("no") && salesMade.equals("yes")) {
                                int userOption;
                                do {
                                    System.out.println();
                                    System.out.println("What do you want to do?");
                                    System.out.println("1. Find and View Items");
                                    System.out.println("2. Manage Inventory");
                                    System.out.println("3. View Sales and History Report");
                                    System.out.println("4. Exit to Login Menu");
                                    System.out.print("Option: ");
                                    userOption = scanner.nextInt();
                                    System.out.println();
                                    switch (userOption) {
                                        case 1:
                                            System.out.println("R5");
                                            System.out.println("Finding and Viewing Items...");
                                            System.out.println();
                                            break;
                                        case 2:
                                            System.out.println("R6");
                                            System.out.println("Managing Inventory...");
                                            System.out.println();
                                            break;
                                        case 3:System.out.println("R7");
                                            System.out.println("Viewing Sales and History Report...");
                                            System.out.println();
                                            break;
                                        case 4:
                                            System.out.println("Going back to Login Menu...");
                                            System.out.println();
                                            break;
                                    }
                                } while (userOption != 4);
                            } else if (inCart.equals("yes") && salesMade.equals("no")) {
                                int userOption;
                                do{
                                    System.out.println();
                                    System.out.println("What do you want to do?");
                                    System.out.println("1. Purchase ");
                                    System.out.println("2. Find and View Items");
                                    System.out.println("3. Manage Inventory");
                                    System.out.println("4. Exit to Login Menu");
                                    System.out.print("Option: ");
                                    userOption = scanner.nextInt();
                                    System.out.println();

                                    switch (userOption) {
                                        case 1:
                                            System.out.println("R8");
                                            System.out.println("Purchasing Item...");
                                            System.out.println();
                                            break;
                                        case 2:
                                            System.out.println("R9");
                                            System.out.println("Finding and Viewing Items...");
                                            System.out.println();
                                            break;
                                        case 3:
                                            System.out.println("R10");
                                            System.out.println("Managing Inventory...");
                                            System.out.println();
                                            break;
                                        case 4:
                                            System.out.println("Going back to Login Menu...");
                                            System.out.println();
                                            break;
                                    }
                                } while (userOption != 4);
                            } else {
                                int userOption;
                                do{
                                    System.out.println();
                                    System.out.println("What do you want to do?");
                                    System.out.println("1. Find and View Items");
                                    System.out.println("2. Manage Inventory");
                                    System.out.println("3. Exit to Login Menu");
                                    System.out.print("Option: ");
                                    userOption = scanner.nextInt();
                                    System.out.println();

                                    switch (userOption) {
                                        case 1:
                                            System.out.println("R11");
                                            System.out.println("Finding and Viewing Items...");
                                            System.out.println();
                                            break;
                                        case 2:
                                            System.out.println("R12");
                                            System.out.println("Managing Inventory...");
                                            System.out.println();
                                            break;
                                        case 3:
                                            System.out.println("Going back to Login Menu...");
                                            System.out.println();
                                            break;
                                    }
                                } while (userOption != 3);
                            }

                        }else{
                            int userOption;
                            String salesMade;
                            System.out.println("Have sales been made? (Yes/No)");
                            salesMade = scanner.nextLine();
                            if(salesMade.equalsIgnoreCase("yes")) {

                                do {
                                    System.out.println("What would you like to do?");
                                    System.out.println("1. Manage inventory");
                                    System.out.println("2. View sales report");
                                    System.out.println("3. Exit to Login Menu");
                                    System.out.print("Option: ");
                                    userOption = scanner.nextInt();
                                    System.out.println();

                                    switch (userOption) {
                                        case 1:
                                            System.out.println("R13");
                                            System.out.println("Managing inventory...");
                                            System.out.println();
                                            break;

                                        case 2:
                                            System.out.println("R14");
                                            System.out.println("Viewing Sales and History Report...");
                                            System.out.println();
                                            break;

                                        case 3:
                                            System.out.println("Going back to Login Menu...");
                                            System.out.println();
                                            break;
                                    }

                                } while (userOption != 3);
                            }
                            else{
                                int choice;
                                do {
                                    System.out.println("What would you like to do?");
                                    System.out.println("1. Manage inventory");
                                    System.out.println("2. Exit to login menu");
                                    System.out.print("Option: ");
                                    choice = scanner.nextInt();
                                    System.out.println();

                                    switch (choice) {
                                        case 1:
                                            System.out.println("R15");
                                            System.out.println("Managing inventory...");
                                            System.out.println();
                                            break;

                                        case 2:
                                            System.out.println("Going back to Login Menu...");
                                            System.out.println();
                                            break;
                                    }
                                }while(choice !=2);
                            }
                        }
                    }else {
                        //R16-R18 here
                        System.out.println("You are a Registered User");
                        System.out.println("------------------------------");
                        System.out.println("is the item in Stock? (Yes/No)");
                        String itemInStock = scanner.next().toLowerCase();
                        scanner.nextLine();

                        if (itemInStock.equals("yes")) {

                            System.out.println("Is the item in the cart? (Yes/No)");
                            String inCart = scanner.next().toLowerCase();
                            scanner.nextLine();

                            if (inCart.equals("yes")){
                                int userChoice;

                                do {
                                    System.out.println("What would you like to do?");
                                    System.out.println("1. Make a purchase");
                                    System.out.println("2. Find items");
                                    System.out.println("3. Exit to login menu");
                                    System.out.print("Option: ");
                                    userChoice = scanner.nextInt();
                                    System.out.println();

                                    switch (userChoice){
                                        case 1:
                                            System.out.println("R16");
                                            System.out.println("Purchasing item...");
                                            System.out.println();
                                            break;

                                        case 2:
                                            System.out.println("R17");
                                            System.out.println("Finding and viewing items...");
                                            System.out.println();
                                            break;

                                        case 3:
                                            System.out.println("Going back to Login Menu...");
                                            System.out.println();
                                            break;
                                    }
                                }while (userChoice != 3);
                            }else{
                                int userChoice;

                                do {
                                    System.out.println("What would you like to do?");
                                    System.out.println("1. Find items");
                                    System.out.println("2. Exit to login menu");
                                    System.out.print("Option: ");
                                    userChoice = scanner.nextInt();
                                    System.out.println();

                                    switch (userChoice){
                                        case 1:
                                            System.out.println("R18");
                                            System.out.println("Finding and viewing items...");
                                            System.out.println();
                                            break;

                                        case 2:
                                            System.out.println("Going back to Login Menu...");
                                            System.out.println();
                                            break;
                                    }
                                }while (userChoice != 2);

                            }
                        }else{
                            System.out.println("R20");
                            System.out.println("Unfortunately there is no item in stock, there is nothing to find/view, come back later!");
                            System.out.println();
                        }
                    }
                    break;
                case 2:
                    System.out.println("You are an UnRegister User");
                    System.out.println("------------------------------");
                    System.out.println("is the item in Stock? (Yes/No)");
                    String itemInStock = scanner.next().toLowerCase();
                    scanner.nextLine();
                    System.out.println();

                    if (itemInStock.equals("yes")) {
                        int userChoice;

                        do {
                            System.out.println("You need to be a Register User to access more features!");
                            System.out.println("What would you like to do?");
                            System.out.println("1. Find items");
                            System.out.println("2. Exit to login menu");
                            System.out.print("Option: ");
                            userChoice = scanner.nextInt();
                            System.out.println();

                            switch (userChoice){
                                case 1:
                                    System.out.println("R19");
                                    System.out.println("Finding and viewing items...");
                                    System.out.println();
                                    break;

                                case 2:
                                    System.out.println("Going back to Login Menu...");
                                    System.out.println();
                                    break;
                            }
                        }while (userChoice != 2);
                    }else{
                        System.out.println("R20");
                        System.out.println("Unfortunately there is no item in stock, there is nothing to find/view, come back later!");
                        System.out.println();
                    }
                case 3:
                    System.out.println("Goodbye!");
                    System.out.println();
                    break;
            }
        }while (login != 3) ;
    }
}