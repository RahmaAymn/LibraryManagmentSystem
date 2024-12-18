import java.util.ArrayList;
import java.util.List;

public class Book {
    private String title;
    private String genre;
    private int availableCopies;
    private ArrayList<Rating> ratings;  
    private List<User> reservedUsers; 
    

    public Book(String title, String genre, int availableCopies) {
        this.title = title;
        this.genre = genre;
        this.availableCopies = availableCopies;
        this.ratings = new ArrayList<>();
        this.reservedUsers = new ArrayList<>();  
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public ArrayList<Rating> getRatings() {
        return ratings;
    }

    public void addRating(Rating rating) {
        this.ratings.add(rating);
    }
    

    public double getAverageRating() {
        if (ratings.isEmpty()) {
            return 0.0;
        }

        int totalRating = 0;
        for (Rating rating : ratings) {
            totalRating += rating.getRating();
        }

        return (double) totalRating / ratings.size();
    }

    public void viewReviews() {
        if (ratings.isEmpty()) {
            System.out.println("No reviews yet.");
        } else {
            System.out.println("Reviews for " + title + ":");
            for (Rating rating : ratings) {
                System.out.println("Rating: " + rating.getRating() + "/5");
                System.out.println("Review: " + rating.getReview());
                System.out.println("---");
            }
        }
    }

    public boolean isAvailable() {
        return availableCopies > 0;
    }

    public void notifyUsers() {
        if (reservedUsers != null && !reservedUsers.isEmpty()) {
            for (User user : reservedUsers) {
                System.out.println("Notification: The book '" + this.title + "' is now available for you, " + user.getUsername() + ".");
            }
            reservedUsers.clear();  
        }
    }
    public void reserveBook(User user) {
        if (!reservedUsers.contains(user)) {
            reservedUsers.add(user);
        } 
    }
    

    public List<User> getReservedUsers() {
        return reservedUsers;
    }
    
    public boolean borrowBook() {
        if (availableCopies > 0) {
            availableCopies--;
            return true;
        }
        return false;
    }

    public void returnBook() {
        availableCopies++;
        notifyUsers();
    }

    

}
