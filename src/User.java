import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String role;
    private boolean isBanned;
    private int age;
    private String gender;
    private ArrayList<Rating> ratings;  
    private ArrayList<BorrowRecord> borrowedBooks; 
    private List<Book> reservedBooks; 
    


    public User(String username, String password, String role, boolean isBanned, int age, String gender) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.isBanned = isBanned;
        this.age = age;
        this.gender = gender;
        this.ratings = new ArrayList<>();
        this.borrowedBooks = new ArrayList<>(); 
        this.reservedBooks = new ArrayList<>();  

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean isBanned) {
        this.isBanned = isBanned;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    public void editProfile(String newPassword) {
        this.password = newPassword;
    }

    public void addRating(Rating rating) {
        this.ratings.add(rating);
    }
    public ArrayList<BorrowRecord> getBorrowedBooks() {
        return borrowedBooks;

    }

    public void addReservedBooks(Book book) {
        if (!reservedBooks.contains(book)) {
            reservedBooks.add(book);
            book.reserveBook(this); 
        } else {
        }
    }
    
    public List<Book> getReservedBooks() {
        return reservedBooks;  
    }
    

    public void addBorrowRecord(BorrowRecord borrowRecord) {
        this.borrowedBooks.add(borrowRecord); 
    }

    public void removeBorrowRecord(BorrowRecord borrowRecord) {
        this.borrowedBooks.remove(borrowRecord);  
    }

    public void borrowBook(String bookTitle, int durationWeeks) {
        BorrowRecord borrowRecord = new BorrowRecord(bookTitle, durationWeeks);
        borrowedBooks.add(borrowRecord);
    }

    public void rateAndReviewBook(Book book, int rating, String review) {
        if (rating < 1 || rating > 5) {
            System.out.println("Rating must be between 1 and 5.");
            return;
        }

        if (review == null || review.trim().isEmpty()) {
            review = "No review provided";  
        }

        Rating userRating = new Rating(rating, review);

        book.addRating(userRating);
        System.out.println("Thank you for your rating and review!");
    }
}