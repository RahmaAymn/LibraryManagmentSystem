import java.io.*;
import java.util.*;

public class Library {
    private Map<String, User> users;  
    private Map<String, Book> books;  
    public Library() {
        this.users = new HashMap<>();
        this.books = new HashMap<>();
    }

    public void loadUsersFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                String username = userData[0];
                String password = userData[1];
                String role = userData[2];
                boolean isBanned = Boolean.parseBoolean(userData[3]);
                int age = Integer.parseInt(userData[4]);
                String gender = userData[5];
                users.put(username, new User(username, password, role, isBanned, age, gender));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadBooksFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookData = line.split(",");
                String title = bookData[0];
                String genre = bookData[1];
                int availableCopies = Integer.parseInt(bookData[2]);
                books.put(title, new Book(title, genre, availableCopies));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveUsersToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (User user : users.values()) {
                writer.write(user.getUsername() + "," + user.getPassword() + "," + user.getRole() + ","
                        + user.isBanned() + "," + user.getAge() + "," + user.getGender() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBooksToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Book book : books.values()) {
                writer.write(book.getTitle() + "," + book.getGenre() + "," + book.getAvailableCopies() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public User loginUser(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password) && !user.isBanned()) {
            return user;
        }
        return null;
    }

    public void registerUser(String username, String password, String role, int age, String gender) {
        if (!users.containsKey(username)) {
            users.put(username, new User(username, password, role, false, age, gender));
        } else {
            System.out.println("User already exists.");
        }
    }

    public void addBook(String title, String genre, int availableCopies) {
        if (!books.containsKey(title)) {
            books.put(title, new Book(title, genre, availableCopies));
            saveBooksToFile("books.txt"); 
        } else {
            System.out.println("Book already exists.");
        }
    }

   public String borrowBook(User user, String bookTitle, int durationWeeks, Scanner scanner) {
    Book book = findBookByTitle(bookTitle); 
    
    if (book != null) {
        for (BorrowRecord record : user.getBorrowedBooks()) {
            if (record.getBookTitle().equals(bookTitle)) {
                return "You have already borrowed the book '" + bookTitle + "' and cannot borrow it again at this time.";
            }
        }
        if (book.getAvailableCopies() > 0) {
            book.setAvailableCopies(book.getAvailableCopies() - 1);  
            user.borrowBook(bookTitle, durationWeeks);
            return "You have successfully borrowed the book '" + bookTitle + "' for " + durationWeeks + " week(s).";
        } else {
           
            System.out.println("Sorry, no copies available for '" + bookTitle + "'.");
            System.out.print("Would you like to reserve it? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();
    
            if (response.equals("yes")) {
                String reserveMessage = reserveBook(user, bookTitle); 
                return reserveMessage; 
            } else {
                return "You chose not to reserve the book.";
            }
        }
    } else {
        return "The book '" + bookTitle + "' does not exist in the library.";
    }
}



    private Book findBookByTitle(String bookTitle) {
        return books.get(bookTitle); 
    }
    
    public String reserveBook(User user, String bookTitle) {
        Book book = findBookByTitle(bookTitle); 
        
        if (book != null) {
            if (book.isAvailable()) {
                return "The book '" + bookTitle + "' is available for borrowing. You cannot reserve it.";
            } else {
                if (user.getReservedBooks().contains(book)) {
                    return "The book '" + bookTitle + "' has already been reserved for you.";
                } else {
                    user.addReservedBooks(book); 
                    book.reserveBook(user);  
                    return "The book '" + bookTitle + "' has been reserved for you.";
                }
            }
        } else {
            return "The book '" + bookTitle + "' does not exist in the library.";
        }
    }
    
    

    @SuppressWarnings("resource")
    public String returnBook(User user, String bookTitle) {
        for (BorrowRecord record : user.getBorrowedBooks()) {
            if (record.getBookTitle().equals(bookTitle)) {
                user.removeBorrowRecord(record);  
    
                Book book = books.get(bookTitle);  
                System.out.println("Would you like to rate and review the book? (y/n)");
                Scanner scanner = new Scanner(System.in);
                String response = scanner.nextLine().toLowerCase();
                
                if (response.equals("y")) {
                    System.out.println("Please enter your rating (1-5):");
                    int rating = scanner.nextInt();
                    scanner.nextLine();  
                    System.out.println("Please enter your review (optional):");
                    String userReview = scanner.nextLine();
        
                    rateAndReviewBook(book, rating, userReview);
                }
                if (book != null) {
                    book.setAvailableCopies(book.getAvailableCopies() + 1);  
                    if (book.getReservedUsers() != null && !book.getReservedUsers().isEmpty()) {
                        book.notifyUsers();  
                    }
    
                    return "Book returned successfully.";
                } else {
                   
                    System.out.println("Error: Book with title '" + bookTitle + "' not found in library.");
                    return "Book not found in the library.";
                }
            }
        }
    
        return "You haven't borrowed this book.";
    }
    
     public void checkOverdueBooks(User user) {
        boolean hasBorrowedBooks = false; 

        for (BorrowRecord record : user.getBorrowedBooks()) {
            hasBorrowedBooks = true;
            long remainingDays = record.getRemainingDays();
            String bookTitle = record.getBookTitle();

            if (remainingDays > 0) {
                System.out.println("Book: '" + bookTitle + "' is due in " + remainingDays + " days.");
            } else {
                System.out.println("Book: '" + bookTitle + "' is overdue. Please return it.");
            }
        }

        if (!hasBorrowedBooks) {
            System.out.println("You have not borrowed any books.");
        }
    }

    public void rateAndReviewBook(Book book, int rating, String review) {
        if (rating < 1 || rating > 5) {
            System.out.println("Rating must be between 1 and 5.");
            return;
        }

        Rating userRating = new Rating(rating, review);

        book.addRating(userRating); 
        System.out.println("Thank you for your rating and review!");
    }





    public void banUser(User admin, String username) {
        if (admin.getRole().equals("admin")) {
            User user = users.get(username);
            if (user != null) {
                user.setBanned(true);
                saveUsersToFile("users.txt");
                System.out.println("User " + username + " has been banned.");
            } else {
                System.out.println("User not found.");
            }
        }
    }

    public void unbanUser(User admin, String username) {
        if (admin.getRole().equals("admin")) {
            User user = users.get(username);
            if (user != null) {
                user.setBanned(false);
                saveUsersToFile("users.txt");
                System.out.println("User " + username + " has been unbanned.");
            } else {
                System.out.println("User not found.");
            }
        }
    }

    public void displayBooksWithRatings() {
        for (Book book : books.values()) {
            System.out.println(book.getTitle() + " - Average Rating: " + book.getAverageRating());
        }
    }


    

    public void viewAllUsers() {
        for (User user : users.values()) {
            System.out.println(user.getUsername() + " | Role: " + user.getRole() + " | Banned: " + user.isBanned());
        }
    }
    public void viewAllBooks() {
        for (Book book : books.values()) {
            System.out.println("Title: " + book.getTitle());
            System.out.println("Genre: " + book.getGenre());
            System.out.println("Available Copies: " + book.getAvailableCopies());
    
            double averageRating = book.getAverageRating();
            if (averageRating > 0) {
                System.out.println("Average Rating: " + averageRating + "/5");
            } else {
                System.out.println("No ratings yet.");
            }
    
            System.out.println("Reviews:");
            if (book.getRatings().isEmpty()) {
                System.out.println("No reviews yet.");
            } else {
                for (Rating rating : book.getRatings()) {
                    System.out.println("Rating: " + rating.getRating() + "/5");
                    System.out.println("Review: " + rating.getReview());
                    System.out.println("---");
                }
            }
            System.out.println("________________");
        }
    }
    

    public void removeBook(String title) {
    if (books.containsKey(title)) {
        books.remove(title);
        System.out.println("Book " + title + " has been removed.");
        saveBooksToFile("books.txt"); 

    } else {
        System.out.println("Book not found.");
    }
}

public void deleteUser(String username) {
    if (users.containsKey(username)) {
        users.remove(username);
        saveUsersToFile("users.txt");
        System.out.println("User " + username + " has been deleted.");
    } else {
        System.out.println("User not found.");
    }
}

public void editUserProfile(String username, String password, String role, int age, String gender) {
    if (users.containsKey(username)) {
        User user = users.get(username);
        user.setPassword(password);
        user.setRole(role);
        user.setAge(age);
        user.setGender(gender);
        saveUsersToFile("users.txt");
        System.out.println("User " + username + "'s profile has been updated.");
    } else {
        System.out.println("User not found.");
    }
}

 public void editOwnProfile(User currentUser, String password, int age, String gender) {
        if (users.containsKey(currentUser.getUsername())) {
            User user = users.get(currentUser.getUsername());
            user.setPassword(password);
            user.setAge(age);
            user.setGender(gender);
            saveUsersToFile("users.txt");
            System.out.println("Your profile has been updated.");
        } else {
            System.out.println("User not found.");
        }
    }
    


public void checkUserBookStatus(User currentUser) {
    boolean hasOverdueBooks = false; 

    for (BorrowRecord record : currentUser.getBorrowedBooks()) {
        long remainingDays = record.getRemainingDays();  

        if (remainingDays < 0) {
            System.out.println(currentUser.getUsername() + " you have an overdue book: " + record.getBookTitle() + 
                               " (Overdue by " + Math.abs(remainingDays) + " days).");
            hasOverdueBooks = true; 
        }
    }

    if (!hasOverdueBooks) {
        System.out.println(currentUser.getUsername() + " you have no overdue books.");
    }

   if (!currentUser.getReservedBooks().isEmpty()) {
    for (Book reservedBook : currentUser.getReservedBooks()) {
        System.out.println("Reserved book(s): " + reservedBook.getTitle());
    }
} else {
    System.out.println(currentUser.getUsername() + " you have no reserved books.");
}


    if (!currentUser.getBorrowedBooks().isEmpty()) {
        System.out.println(currentUser.getUsername() + " you have borrowed the following books:");
        for (BorrowRecord record : currentUser.getBorrowedBooks()) {
            long remainingDays = record.getRemainingDays(); 

            if (remainingDays < 0) {
                System.out.println("Overdue book(s): " + record.getBookTitle() + " (Overdue by " + Math.abs(remainingDays) + " days).");
            } 
           
            else {
                System.out.println("Borrowed book(s): " + record.getBookTitle() + " with " + remainingDays + " days remaining.");
            }
        }
    } else {
        System.out.println(currentUser.getUsername() + " you have not borrowed any books.");
    }
}


}

