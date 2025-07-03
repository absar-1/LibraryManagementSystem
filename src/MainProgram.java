import java.io.*;
import java.util.*;



public class MainProgram {

    private static String currentUser = null;
    private static String availableBooks = "src\\available_books.csv";
    private static String borrowedBooks = "src\\borrowed_books.csv";
    private static String librarians = "src\\librarians.csv";
    private static String members = "src\\members.csv";

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);

        System.out.println("\n********** Welcome to the Library Management System **********\n");


        int choice;

        do {
            System.out.println("\nLibrary Management System");
            System.out.println("1. Login as Librarian");
            System.out.println("2. Login as Member");
            System.out.println("3. Exit");
//            System.out.print("Enter your choice: ");

            while(true){
                System.out.print("Enter your choice: ");
                if(sc.hasNextInt()){
                    choice = sc.nextInt();
                    sc.nextLine();
                    break;
                } else {
                    System.out.println("\nPlease enter a valid choice");
                    sc.nextLine();
                }
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter Username: ");
                    String username = sc.nextLine();
                    while(!isValidUsername(username)){
                        System.out.println("\nInvalid Username");
                        System.out.print("Enter Username Again: ");
                        username = sc.nextLine();
                    }


                    System.out.print("Enter Password: ");
                    String password = sc.nextLine();

                    while (!isValidPassword(password)){
                        System.out.println("\nInvalid Password");
                        System.out.print("Enter Password Again: ");
                        password = sc.nextLine();
                    }

                    login(username, password);
                    break;
                case 2:
                    System.out.print("Enter Username: ");
                    String un = sc.nextLine();
                    while(!isValidUsername(un)){
                        System.out.println("\nInvalid Username");
                        System.out.print("Enter Username Again: ");
                        un = sc.nextLine();
                    }
                    System.out.print("Enter Password: ");
                    String pwd = sc.nextLine();
                    while (!isValidPassword(pwd)){
                        System.out.println("\nInvalid Password");
                        System.out.print("Enter Password Again: ");
                        pwd = sc.nextLine();
                    }
                    memberLogin(un,pwd);
                    break;
                case 3:
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);




    }

    //Login

    public static void login(String username, String password){
//        String file = "src\\librarians.csv";
        String line;
        boolean found = false;

        try(BufferedReader br = new BufferedReader(new FileReader(librarians))) {
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials[0].equals(username) && credentials[1].equals(password)) {
                    System.out.println("Login Successful");
                    currentUser = credentials[0];
                    br.close();
                    found = true;

                    displayMenu();

                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("Login Failed");
            System.out.println(e.getMessage());
        }

        if (!found) {
            System.out.println("Invalid Username or Password!!!");
        }
    }

    // Logout

    public static void logout(){
        if(currentUser != null){
            System.out.println("Goodbye " + currentUser);
            System.out.println("Logout Successful");
        } else {
            System.out.println("No user is currently logged in");
        }
    }

    //Register Librarian

    public static void librarianRegister(String username, String password){
//        String file = "src\\librarians.csv";
        String line;
        boolean userExists = false;
        try(BufferedReader br = new BufferedReader(new FileReader(librarians))){
            while ((line = br.readLine()) != null){
                String[] data = line.split(",");
                if (username.equals(data[0])){
                    System.out.println("Username already exists");
                    userExists = true;
                    break;
                }
            }
        } catch (IOException e){
            System.out.println("Problem reading file");
            System.out.println(e.getMessage());
        }

        if(!userExists){

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(librarians,true))) {

                bw.write(username + "," + password);
                bw.newLine();
                System.out.println("User successfully created");

            } catch (IOException e) {
                System.out.println("Problem writing file");
                System.out.println(e.getMessage());
            }
        }
    }


    // -----------   Book Management  --------------


    // Available books

    public static void displayAvailableBooks() throws IOException {
//        String file = "C:\\Users\\Dell\\Documents\\KSBL\\Library Management System\\LibraryManagementSystem\\src\\available_books.csv";
        BufferedReader br = new BufferedReader(new FileReader(availableBooks));
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");

            displayRow(data);
        }
        br.close();
    }
    // Formatting output as Table:
    public static void displayRow(String[] data) throws IOException {
        for (String s : data) {
            System.out.printf("| %-25s |", s);
        }
        System.out.println();
    }


    // Edit available books


    public static void editAvailableBooks(String id, String[] arr) throws IOException {
        Scanner sc = new Scanner(System.in);
//        String file = "C:\\Users\\Dell\\Documents\\KSBL\\Library Management System\\LibraryManagementSystem\\src\\available_books.csv";
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(availableBooks))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(availableBooks))) {
            for (String line : lines) {
                String[] details = line.split(",");

                if (details[0].equals(id)) {
                    bw.write(String.join(",", arr));
                    found = true;
                } else {
                    bw.write(line);
                }
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (found) {
            System.out.println("Book details updated successfully");
        } else {
            System.out.println("Book not found");
        }
    }



    // Add a book

    public static void addBook(String id, String bookName, String author, String Available){
//        String file = "C:\\Users\\Dell\\Documents\\KSBL\\Library Management System\\LibraryManagementSystem\\src\\available_books.csv";
        String line;
        boolean bookExists = false;

        try(BufferedReader br = new BufferedReader(new FileReader(availableBooks))){
            while ((line = br.readLine()) != null){
                String[] data = line.split(",");
                if (id.equals(data[0]) || bookName.equals(data[1])){
                    System.out.println("Book already exists");
                    bookExists = true;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Problem reading file");
            System.out.println(e.getMessage());
        }
        if(!bookExists){
            try(BufferedWriter bw  = new BufferedWriter(new FileWriter(availableBooks,true))) {
                bw.write(id + "," + bookName + "," + author + "," + Available);
                bw.newLine();
                System.out.println("Book successfully added");
            } catch (Exception e) {
                System.out.println("Problem writing file");
                System.out.println(e.getMessage());
            }
        }
    }

    // Delete a Book

    public static void deleteBook(String id){
        List<String> lines = new ArrayList<>();
//        String file = "C:\\Users\\Dell\\Documents\\KSBL\\Library Management System\\LibraryManagementSystem\\src\\available_books.csv";
        String line;
        boolean isDeleted = false;

        try(BufferedReader br = new BufferedReader(new FileReader(availableBooks))){
            while ((line = br.readLine()) != null){
                String[] data = line.split(",");
                if (!id.equals(data[0])) {
                    lines.add(line);
                } else {
                    isDeleted = true;
                }
            }
        } catch (Exception e) {
            System.out.println("Problem reading file");
            System.out.println(e.getMessage());
            return;
        }


        try(BufferedWriter bw = new BufferedWriter(new FileWriter(availableBooks))) {
            for (String s : lines) {
                bw.write(s);
                bw.newLine();
            }
            if(isDeleted){
                System.out.println("Book deleted successfully");
            } else{
                System.out.println("Book not found");
            }
        } catch (Exception e) {
            System.out.println("Problem writing file");
            System.out.println(e.getMessage());
        }


    }


    // ----------------- User Management -------------------------

    // User Login

    public static void memberLogin(String username, String password){
        boolean found = false;
//        String file = "C:\\Users\\Dell\\Documents\\KSBL\\Library Management System\\LibraryManagementSystem\\src\\members.csv";
        try(BufferedReader br = new BufferedReader(new FileReader(members))){
            String line;
            while ((line = br.readLine()) != null){
                String[] data = line.split(",");
                if (username.equals(data[0]) && password.equals(data[1])){
                    currentUser = data[0];

                    memberDisplayMenu();

                    found = true;
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println("Login Failed");
            System.out.println(e.getMessage());
        }

        if(!found){
            System.out.println("Invalid username or password");
        }
    }


    // Register Members

    public static void registerMembers(String username,String pwd){
//        String file = "C:\\Users\\Dell\\Documents\\KSBL\\Library Management System\\LibraryManagementSystem\\src\\members.csv";
        boolean found = false;
        try(BufferedReader br = new BufferedReader(new FileReader(members))){
            String line;
            while((line = br.readLine()) != null){
                String[] data = line.split(",");
                if (data[0].equals(username)){
                    System.out.println("Username already exists");
                    found = true;
                    return;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if(!found){
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(members, true))){
                bw.newLine();
                bw.write(username + "," + pwd);
                System.out.println("Member Registered Successfully");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // Remove Members

    public static void removeMembers(String username){
//        String file = "C:\\Users\\Dell\\Documents\\KSBL\\Library Management System\\LibraryManagementSystem\\src\\members.csv";
        List<String> lines = new ArrayList<>();
        boolean found = false;
        try(BufferedReader br = new BufferedReader(new FileReader(members))){
            String line;
            while((line = br.readLine()) != null){
                String[] data = line.split(",");
                if (!data[0].equals(username)){
                    lines.add(line);
                } else{
                    found = true;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (found) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(members))) {
                for (String line : lines) {
                    bw.write(line);
                    bw.newLine();
                }
                System.out.println("Member removed successfully");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Member not found");
        }
    }


    // Update member details


    public static void updateMemberDetails(String username, String[] newDetails){
        boolean found = false;
        List<String> lines = new ArrayList<>();
//        String file = "C:\\Users\\Dell\\Documents\\KSBL\\Library Management System\\LibraryManagementSystem\\src\\members.csv";

        try(BufferedReader br = new BufferedReader(new FileReader(members))){
            String line;
            while((line = br.readLine()) != null){
                lines.add(line);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(members))){
            for (String s : lines) {
                String[] data = s.split(",");
                if (!data[0].equals(username)){
                    bw.write(s);
                } else{
                    bw.write(String.join(",", newDetails));
                    found = true;
                }
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (found) {
            System.out.println("Member details updated successfully");
        } else {
            System.out.println("Member not found");
        }
    }


    // -------------------- Issue and Return Books -----------------------------


    // Issue Books

    public static void issueBooks(String username, String bookName){
//        String memberFile = "C:\\Users\\Dell\\Documents\\KSBL\\Library Management System\\LibraryManagementSystem\\src\\members.csv";
        boolean isMember = false;
        try(BufferedReader br = new BufferedReader(new FileReader(members))){
            String line;
            while((line = br.readLine()) != null){
                String[] data = line.split(",");
                if (data[0].equals(username)){
                    isMember = true;
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

//        String bookFile = "C:\\Users\\Dell\\Documents\\KSBL\\Library Management System\\LibraryManagementSystem\\src\\available_books.csv";
        boolean isAvailable = false;
        try (BufferedReader br = new BufferedReader(new FileReader(availableBooks))){
            String line;
            while((line = br.readLine()) != null){
                String[] data = line.split(",");
                if (data[1].toLowerCase().equals(bookName.toLowerCase()) && data[3].toLowerCase().equals("yes")){
                    isAvailable = true;
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        if (isAvailable && isMember) {
//            String borrowFile = "C:\\Users\\Dell\\Documents\\KSBL\\Library Management System\\LibraryManagementSystem\\src\\borrowed_books.csv";
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(borrowedBooks,true))){
                bw.newLine();
                bw.write(username + "," + bookName);
                System.out.println("Book issued successfully");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else if (!isAvailable) {
            System.out.println("Book is not available");
        } else {
            System.out.println("Member not found");
        }
    }

    // Return Books

    public static void returnBook(String username, String bookname){
//        String file = "C:\\Users\\Dell\\Documents\\KSBL\\Library Management System\\LibraryManagementSystem\\src\\borrowed_books.csv";
        List<String> lines =  new ArrayList<>();
        boolean found = false;
        try(BufferedReader br = new BufferedReader(new FileReader(borrowedBooks))){
            String line;
            while((line = br.readLine()) != null){
                String[] data = line.split(",");
                if (data[0].equals(username) && data[1].equalsIgnoreCase(bookname)){
                    found = true;
                } else{
                    lines.add(line);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(borrowedBooks))){
            for (String s : lines) {
                bw.write(s);
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (!found) {
            System.out.println("Book or User not found");
        } else {
            System.out.println("Book returned successfully");
        }
    }

    // Show issued books

    public static void showIssuedBooks(String username){
//        String file = "C:\\Users\\Dell\\Documents\\KSBL\\Library Management System\\LibraryManagementSystem\\src\\borrowed_books.csv";
        List<String> lines =  new ArrayList<>();
        boolean found = false;

        try(BufferedReader br = new BufferedReader(new FileReader(borrowedBooks))){
            String line;
            while ((line = br.readLine()) != null){
                String[] data = line.split(",");
                if (data[0].equals(username)){
                    lines.add(line);
                    found = true;
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        if (found) {
            for (String s : lines) {
                String[] data = s.split(",");
                System.out.println("Your issued book(s) are: " + data[1]);
            }
        } else {
            System.out.println("You have not borrowed any book");
        }
    }


    // Displays after login

    // For Librarian

    public static void displayMenu() {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nChoose an option:");
            System.out.println("1. Display Available Books");
            System.out.println("2. Add a Book");
            System.out.println("3. Edit a Book");
            System.out.println("4. Delete a Book");
            System.out.println("5. Issue a Book");
            System.out.println("6. Return a Book");
            System.out.println("7. Register a Librarian");
            System.out.println("8. Register a member");
            System.out.println("9. Remove a Member");
            System.out.println("10. Logout");
//            System.out.print("Enter your choice: ");

//            choice = sc.nextInt();
//            sc.nextLine();


            while(true){
                System.out.print("Enter your choice: ");
                if(sc.hasNextInt()){
                    choice = sc.nextInt();
                    sc.nextLine();
                    break;
                } else {
                    System.out.println("\nPlease enter a valid choice");
                    sc.nextLine();
                }
            }



            switch (choice) {
                case 1:
                    try {
                        displayAvailableBooks();
                    } catch (IOException e) {
                        System.out.println("Error displaying books: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.print("Enter Book ID: ");
                    String id = sc.nextLine();
                    System.out.print("Enter Book Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Author: ");
                    String author = sc.nextLine();
                    System.out.print("Is it Available (Yes/No): ");
                    String available = sc.nextLine();
                    addBook(id, name, author, available);
                    break;
                case 3:
                    System.out.print("Enter Book ID to Edit: ");
                    id = sc.nextLine();
                    System.out.print("Enter new ID: ");
                    String newId = sc.nextLine();
                    System.out.print("Enter new book name: ");
                    String newName = sc.nextLine();
                    System.out.print("Enter new Author: ");
                    String newAuthor = sc.nextLine();
                    System.out.print("Is it Available (Yes/No): ");
                    String newAva = sc.nextLine();
                    String newDetails = String.join(",", newId, newName, newAuthor, newAva);
                    String[] details = newDetails.split(",");
                    try {
                        editAvailableBooks(id, details);
                    } catch (IOException e) {
                        System.out.println("Error editing book: " + e.getMessage());
                    }
                    break;
                case 4:
                    System.out.print("Enter Book ID to Delete: ");
                    id = sc.nextLine();
                    deleteBook(id);
                    break;
                case 5:
                    System.out.print("Enter Username: ");
                    String username = sc.nextLine();
                    while(!isValidUsername(username)){
                        System.out.println("\nInvalid Username");
                        System.out.print("Enter Username Again: ");
                        username = sc.nextLine();
                    }
                    System.out.print("Enter Book Name to Issue: ");
                    String bookName = sc.nextLine();
                    issueBooks(username, bookName);
                    break;
                case 6:
                    System.out.print("Enter Username: ");
                    String username2 = sc.nextLine();
                    while(!isValidUsername(username2)){
                        System.out.println("\nInvalid Username");
                        System.out.print("Enter Username Again: ");
                        username2 = sc.nextLine();
                    }
                    System.out.print("Enter Book Name to Return: ");
                    bookName = sc.nextLine();
                    returnBook(username2, bookName);
                    break;
                case 7:
                    System.out.print("Enter Username: ");
                    String un = sc.nextLine();
                    while(!isValidUsername(un)){
                        System.out.println("\nInvalid Username");
                        System.out.print("Enter Username Again: ");
                        un = sc.nextLine();
                    }
                    System.out.print("Enter Password: ");
                    String pwd = sc.nextLine();
                    while (!isValidPassword(pwd)){
                        System.out.println("\nInvalid Password");
                        System.out.print("Enter Password Again: ");
                        pwd = sc.nextLine();
                    }
                    librarianRegister(un, pwd);
                    break;
                case 8:
                    System.out.print("Enter Username: ");
                    String uName = sc.nextLine();
                    while(!isValidUsername(uName)){
                        System.out.println("\nInvalid Username");
                        System.out.print("Enter Username Again: ");
                        uName = sc.nextLine();
                    }
                    System.out.print("Enter Password: ");
                    String pwd2 = sc.nextLine();
                    while (!isValidPassword(pwd2)){
                        System.out.println("\nInvalid Password");
                        System.out.print("Enter Password Again: ");
                        pwd2 = sc.nextLine();
                    }
                    registerMembers(uName, pwd2);
                    break;
                case 9:
                    System.out.print("Enter Username: ");
                    String un3 = sc.nextLine();
                    while(!isValidUsername(un3)){
                        System.out.println("\nInvalid Username");
                        System.out.print("Enter Username Again: ");
                        un3 = sc.nextLine();
                    }
                    removeMembers(un3);
                    break;
                case 10:
                    logout();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 10);
    }

    // For Member


    public static void memberDisplayMenu() {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nChoose an option:");
            System.out.println("1. Display Available Books");
            System.out.println("2. Show issued books");
            System.out.println("3. Update details");
            System.out.println("4. Logout");
//            System.out.print("Enter your choice: ");
//            choice = sc.nextInt();


            while(true){
                System.out.print("Enter your choice: ");
                if(sc.hasNextInt()){
                    choice = sc.nextInt();
                    sc.nextLine();
                    break;
                } else {
                    System.out.println("\nPlease enter a valid choice");
                    sc.nextLine();
                }
            }


            switch (choice){
                case 1:
                    try{
                        displayAvailableBooks();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    showIssuedBooks(currentUser);
                    break;
                case 3:
//                    sc.nextLine();
                    System.out.print("Enter your current username: ");
                    String un = sc.nextLine();
                    System.out.print("Enter new username: ");
                    String newUn = sc.nextLine();
                    while(!isValidUsername(newUn)){
                        System.out.println("\nInvalid Username");
                        System.out.print("Enter Username Again: ");
                        newUn = sc.nextLine();
                    }
                    System.out.print("Enter new password: ");
                    String pwd = sc.nextLine();
                    while (!isValidPassword(pwd)){
                        System.out.println("\nInvalid Password");
                        System.out.print("Enter Password Again: ");
                        pwd = sc.nextLine();
                    }
                    String newDetails = String.join(",",newUn,pwd);
                    String[] data = newDetails.split(",");
                    updateMemberDetails(un,data);
                    break;
                case 4:
                    logout();
                    break;
            }
        } while (choice != 4);
    }


    public static boolean isValidUsername(String username){
        if (username.length()<5){
            return false;
        }
        char firstChar = username.charAt(0);
        return Character.isLetter(firstChar);
    }

    public static boolean isValidPassword(String pwd){
        if (pwd.length()<4){
            return false;
        }
        char firstChar = pwd.charAt(0);
        return Character.isLetterOrDigit(firstChar);
    }




}