import java.util.Scanner;

public class Main {
    private static Library library;

    public static void main(String[] args) {
        library = new Library();
        library.loadUsersFromFile("users.txt");
        library.loadBooksFromFile("books.txt");
        
        Scanner scanner = new Scanner(System.in);
        User currentUser = null;

        while (true) {
            if (currentUser == null) {
                System.out.println("Welcome to the Library System");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine(); 
                if (choice == 1) {
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    currentUser = library.loginUser(username, password);
                    if (currentUser == null) {
                        System.out.println("Invalid login or user is banned.");
                    } else {
                        System.out.println("Welcome, " + currentUser.getUsername());
                    }
                } else if (choice == 2) {
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    System.out.print("Enter role (admin/user/librarian): ");
                    String role = scanner.nextLine();
                    System.out.print("Enter age: ");
                    int age = scanner.nextInt();
                    scanner.nextLine(); 
                    System.out.print("Enter gender: ");
                    String gender = scanner.nextLine();

                    library.registerUser(username, password, role, age, gender);
                    System.out.println("Registration successful!");
                } else if (choice == 3) {
                    break;
                }
            } else {
                if (currentUser.getRole().equals("admin")) {
                    System.out.println("Admin Menu:");
                    System.out.println("1. View all users");
                    System.out.println("2. Add a new user");
                    System.out.println("3. Ban a user");
                    System.out.println("4. Unban a user");
                    System.out.println("5. Delete a user");
                    System.out.println("6. Edit a user");
                    System.out.println("7. Logout");

                    int choice = scanner.nextInt();
                    scanner.nextLine();  

                    switch (choice) {
                        case 1:
                            library.viewAllUsers();
                            break;
                        case 2:
                            break;
                        case 3:
                            System.out.print("Enter username to ban: ");
                            String banUsername = scanner.nextLine();
                            library.banUser(currentUser, banUsername);
                            break;
                        case 4:
                            System.out.print("Enter username to unban: ");
                            String unbanUsername = scanner.nextLine();
                            library.unbanUser(currentUser, unbanUsername);
                            break;
                        case 5:
                            System.out.print("Enter username to delete: ");
                            String deleteUsername = scanner.nextLine();
                            library.deleteUser(deleteUsername);
                            break;
                        case 6:
                            System.out.print("Enter username to edit: ");
                            String editUsername = scanner.nextLine();
                            System.out.print("Enter new password: ");
                            String password = scanner.nextLine();
                            System.out.print("Enter new role (admin/user/librarian): ");
                            String role = scanner.nextLine();
                            System.out.print("Enter new age: ");
                            int age = scanner.nextInt();
                            scanner.nextLine(); 
                            System.out.print("Enter new gender: ");
                            String gender = scanner.nextLine();

                            library.editUserProfile(editUsername, password, role, age, gender);
                            break;
                        case 7:
                            currentUser = null;  
                            System.out.println("You have been logged out.");
                            break;
                        default:
                            System.out.println("Invalid choice.");
                            break;
                    }
                } else if (currentUser.getRole().equals("librarian")) {
                    System.out.println("Librarian Menu:");
                    System.out.println("1. View all books");
                    System.out.println("2. Add a new book");
                    System.out.println("3. Remove a book");
                    System.out.println("4. Logout");

                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    switch (choice) {
                        case 1:
                            library.viewAllBooks();
                            break;
                        case 2:
                            System.out.print("Enter book title: ");
                            String bookTitle = scanner.nextLine();
                            System.out.print("Enter book genre: ");
                            String genre = scanner.nextLine();
                            System.out.print("Enter available copies: ");
                            int availableCopies = scanner.nextInt();
                            scanner.nextLine();  
                            library.addBook(bookTitle, genre, availableCopies);
                            System.out.println("Book added successfully!");
                            break;
                        case 3:
                            System.out.print("Enter book title to remove: ");
                            String removeBookTitle = scanner.nextLine();
                            library.removeBook(removeBookTitle);
                            System.out.println("Book removed successfully!");
                            break;
                        case 4:
                            currentUser = null;  
                            System.out.println("You have been logged out.");
                            break;
                        default:
                            System.out.println("Invalid choice.");
                            break;
                    }
                } else {
                    System.out.println("User Menu:");
                    System.out.println("1. View all books");
                    System.out.println("2. Borrow a book");
                    System.out.println("3. Return a book");
                    System.out.println("4. Edit Profile");
                    System.out.println("5. Reserve a book");
                    System.out.println("6. Check your books status");
                    System.out.println("7. Logout");

                    int choice = scanner.nextInt();
                    scanner.nextLine(); 

                    switch (choice) {
                        case 1:
                            library.viewAllBooks();
                            break;
                        case 2:
                        System.out.print("Enter book title to borrow: ");
                        String bookToBorrow = scanner.nextLine();
                        System.out.print("Enter borrowing duration (1, 2, or 3 weeks): ");
                        int weeks = scanner.nextInt();
                        scanner.nextLine();  


                        String borrowMessage = library.borrowBook(currentUser, bookToBorrow, weeks, scanner);
                        System.out.println(borrowMessage);
                      
                            break;
                        case 3:
                            System.out.print("Enter book title to return: ");
                            String bookToReturn = scanner.nextLine();
                            String returnMessage = library.returnBook(currentUser, bookToReturn);
                            System.out.println(returnMessage);
                            break;
                        case 4:
                            System.out.print("Enter new password: ");
                            String password = scanner.nextLine();
                            System.out.print("Enter new age: ");
                            int age = scanner.nextInt();
                            scanner.nextLine(); 
                            System.out.print("Enter new gender: ");
                            String gender = scanner.nextLine();

                            library.editOwnProfile(currentUser, password, age, gender);
                            break;
                        case 5:
                        System.out.print("Enter the title of the book you want to reserve: ");
                        String bookToReserve = scanner.nextLine();
                        String reserveMessage = library.reserveBook(currentUser, bookToReserve);
                        System.out.println(reserveMessage);
                                   break;
                        case 6 :
                         library.checkUserBookStatus(currentUser); 
                        break;
                        case 7:
                            currentUser = null; 
                            System.out.println("You have been logged out.");
                            break;
                        default:
                            System.out.println("Invalid choice.");
                            break;
                    }
                }
            }
        }

        scanner.close();
    }
}
