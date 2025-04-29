package LibraryManagement;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class LibraryManagementFX extends Application {
    private TableView<Book> tableView;
    private ObservableList<Book> bookList;

    @Override
    public void start(Stage primaryStage) {
        showLandingPage(primaryStage); // First show the landing page
    }

    private void showLandingPage(Stage stage) {
        // Load background image
    	Image bgImage = new Image("file:/C:/Users/zyra/OneDrive/SOURCE CODE/Images/Niones_Page1GUI.png");

        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, false)
        );

        // Root pane
        StackPane root = new StackPane();
        root.setBackground(new Background(backgroundImage));

        // Button
        Button getStartedButton = new Button("Get Started");
        getStartedButton.setStyle(
                "-fx-background-color: #f8e6e8;" +
                "-fx-font-size: 16px;" +
                "-fx-padding: 10 20 10 20;" +
                "-fx-background-radius: 20;" +
                "-fx-font-family: 'Arial';" + 
                "-fx-font-weight: bold;"  +
                "-fx-text-fill: #b64747;"
        );
        getStartedButton.setOnAction(_ -> showLibraryManagement(stage));

        // VBox to align button at bottom center
        VBox buttonBox = new VBox(getStartedButton);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
        buttonBox.setPadding(new Insets(1, 205, 210, 1)); // Adjust this value to move the button up/down
                            //top, right, bottom, left

        root.getChildren().add(buttonBox);

        Scene scene = new Scene(root, 1174, 768);
        stage.setTitle("Welcome to ZYBRAY");
        stage.setScene(scene);
        stage.show();
    }

    private void showLibraryManagement(Stage stage) {
        initializeComponents();
        setupLayout(stage);
        loadBooksFromDatabase();
    }

    private void initializeComponents() {
        bookList = FXCollections.observableArrayList();
        tableView = new TableView<>();
        setupTableColumns();
    }

    @SuppressWarnings("unchecked")
    private void setupTableColumns() {
        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, Integer> copiesCol = new TableColumn<>("Copies");
        copiesCol.setCellValueFactory(new PropertyValueFactory<>("copies"));

        TableColumn<Book, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.getColumns().addAll(titleCol, authorCol, copiesCol, statusCol);
    }

    private void setupLayout(Stage primaryStage) {
        Label headerLabel = new Label("Library Management System");
        headerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button addButton = new Button("Add Book");
        Button borrowButton = new Button("Borrow Book");
        Button returnButton = new Button("Return Book");
        Button refreshButton = new Button("Refresh");

        addButton.setOnAction(_ -> showAddBookDialog());
        borrowButton.setOnAction(_ -> borrowBook());
        returnButton.setOnAction(_ -> returnBook());
        refreshButton.setOnAction(_ -> loadBooksFromDatabase());

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(addButton, borrowButton, returnButton, refreshButton);

        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().addAll(headerLabel, tableView, buttonBox);

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setTitle("Library Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadBooksFromDatabase() {
        String sql = "SELECT * FROM Books";
        bookList.clear();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Book book = new Book(
                        rs.getInt("BookID"),
                        rs.getString("Title"),
                        rs.getString("Author"),
                        rs.getInt("Copies"),
                        rs.getBoolean("IsBorrowed") ? "Borrowed" : "Available"
                );
                bookList.add(book);
            }

            tableView.setItems(bookList);
        } catch (SQLException e) {
            showAlert("Database Error", "Error loading books: " + e.getMessage());
        }
    }

    private void showAddBookDialog() {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Add New Book");

        TextField titleField = new TextField();
        TextField authorField = new TextField();
        TextField copiesField = new TextField();

        VBox dialogContent = new VBox(10);
        dialogContent.getChildren().addAll(
                new Label("Title:"), titleField,
                new Label("Author:"), authorField,
                new Label("Copies:"), copiesField
        );

        dialog.getDialogPane().setContent(dialogContent);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    String title = titleField.getText().trim();
                    String author = authorField.getText().trim();
                    int copies = Integer.parseInt(copiesField.getText().trim());

                    if (title.isEmpty() || author.isEmpty()) {
                        showAlert("Validation Error", "All fields must be filled out.");
                        return null;
                    }

                    addBookToDatabase(title, author, copies);
                    return new Book(copies, title, author, copies, "Available");
                } catch (NumberFormatException e) {
                    showAlert("Input Error", "Copies must be a valid number.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void addBookToDatabase(String title, String author, int copies) {
        String sql = "INSERT INTO Books (Title, Author, Copies, IsBorrowed) VALUES (?, ?, ?, 0)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setInt(3, copies);
            pstmt.executeUpdate();

            loadBooksFromDatabase();
        } catch (SQLException e) {
            showAlert("Database Error", "Error adding book: " + e.getMessage());
        }
    }

    private void borrowBook() {
        updateBookQuantity(-1);
    }

    private void returnBook() {
        updateBookQuantity(1);
    }

    private void updateBookQuantity(int change) {
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert("Selection Error", "Please select a book first.");
            return;
        }

        int newCopies = selectedBook.getCopies() + change;

        if (newCopies < 0) {
            showAlert("Error", "No copies available for borrowing.");
            return;
        }

        String sql = "UPDATE Books SET Copies = ?, IsBorrowed = ? WHERE BookID = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newCopies);
            pstmt.setBoolean(2, newCopies == 0);
            pstmt.setInt(3, selectedBook.getBookId());
            pstmt.executeUpdate();

            loadBooksFromDatabase();
        } catch (SQLException e) {
            showAlert("Database Error", "Error updating book: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
