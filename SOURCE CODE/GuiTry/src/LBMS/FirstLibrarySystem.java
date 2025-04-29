package LBMS;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.Scanner;

// Abstract Book class implementing polymorphism 
abstract class Book {
    private String title;
    private int copies;

    public Book(String title, int copies) {
        this.title = title;
        this.copies = copies;
    }

    public String getTitle() {
        return title;
    }

    public abstract String getCategory();

    public boolean isAvailable() {
        return copies > 0;
    }

    public int getCopies() {
        return copies;
    }

    public boolean borrow() {
        if (copies > 0) {
            copies--;
            return true;
        }
        return false;
    }

    public void returnBook() {
        copies++;
    }
}

// Specific Book types
class Fiction extends Book {
    public Fiction(String title, int copies) {
        super(title, copies);
    }

    public String getCategory() {
        return "Fiction";
    }
}

class NonFiction extends Book {
    public NonFiction(String title, int copies) {
        super(title, copies);
    }

    public String getCategory() {
        return "Non-Fiction";
    }
}

class Biography extends Book {
    public Biography(String title, int copies) {
        super(title, copies);
    }

    public String getCategory() {
        return "Biography";
    }
}

// Member class
class Member {
    private String name;
    private ArrayList<Book> borrowedBooks = new ArrayList<>();

    public Member(String name) {
        this.name = name;
    }

    public boolean borrowBook(Book book) {
        if (book.borrow()) {
            borrowedBooks.add(book);
            return true;
        }
        return false;
    }

    public boolean returnBook(Book book) {
        if (borrowedBooks.contains(book)) {
            book.returnBook();
            borrowedBooks.remove(book);
            return true;
        }
        return false;
    }
}

// Library class
class Library {
    private ObservableList<Book> books = FXCollections.observableArrayList();

    public Library() {
        books.addAll(new Fiction("The Great Gatsby", 3),
                     new NonFiction("Sapiens", 2),
                     new Biography("The Diary of Anne Frank", 4));
    }

    public ObservableList<Book> getBooks() {
        return books;
    }
}

// JavaFX Application
public class FirstLibrarySystem extends Application {
    private Library library = new Library();
    private Member member = new Member("User");
    private ListView<String> bookListView = new ListView<>();

    public static void main(String[] args) {
        launch(args);  // Calls the JavaFX Application launch method
    }

    @Override
    public void start(Stage primaryStage) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("-----------[CHOOSE MODE]-----------");
        System.out.println("\n 1.] GUI ");
        System.out.println("\n 2.] Console");
        System.out.println("-----------------------------------");
        System.out.println("\n Enter Choice:");
        int choice = scanner.nextInt();
        
        if (choice == 1) {
            showDashboard(primaryStage);
        } else {
            consoleMode();
        }
    }

    private void showDashboard(Stage primaryStage) {
        Label titleLabel = new Label("Library Management System");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #4B0082;");

        Button manageBooksButton = new Button("Continue");
        manageBooksButton.setStyle("-fx-background-color: #8A2BE2; -fx-text-fill: white;");
        manageBooksButton.setOnAction(e -> showBookManagement(primaryStage));

        VBox layout = new VBox(20, titleLabel, manageBooksButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 30px; -fx-background-color: #E6E6FA;");

        Scene scene = new Scene(layout, 500, 400);
        primaryStage.setTitle("Library Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showBookManagement(Stage primaryStage) {
        updateBookList();
        Label titleLabel = new Label("Book Management");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        bookListView.setStyle("-fx-control-inner-background: white; -fx-selection-bar: #E0E0E0;");

        Button borrowButton = new Button("Borrow");
        Button returnButton = new Button("Return");
        Button backButton = new Button("Back");

        borrowButton.setOnAction(e -> borrowBook());
        returnButton.setOnAction(e -> returnBook());
        backButton.setOnAction(e -> showDashboard(primaryStage));

        HBox buttonLayout = new HBox(10, borrowButton, returnButton, backButton);
        buttonLayout.setAlignment(Pos.CENTER);

        VBox layout = new VBox(20, titleLabel, bookListView, buttonLayout);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 30px; -fx-background-color: #E6E6FA;");

        Scene scene = new Scene(layout, 500, 600);
        primaryStage.setScene(scene);
    }

    private void updateBookList() {
        ObservableList<String> bookTitles = FXCollections.observableArrayList();
        for (Book book : library.getBooks()) {
            bookTitles.add(book.getTitle() + " [" + book.getCategory() + "] (Available Copies: " + book.getCopies() + ")");
        }
        bookListView.setItems(bookTitles);
    }

    private void borrowBook() {
        int selectedIndex = bookListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Book book = library.getBooks().get(selectedIndex);
            if (member.borrowBook(book)) {
                updateBookList();
            }
        }
    }

    private void returnBook() {
        int selectedIndex = bookListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Book book = library.getBooks().get(selectedIndex);
            if (member.returnBook(book)) {
                updateBookList();
            }
        }
    }

    public static void consoleMode() {
        System.out.println("Console mode running...");
    }
}
