module LibraryManagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;
    
    exports ChapterOne to javafx.graphics;
    exports LBMS to javafx.graphics;
    exports LibraryManagement;  // Export the entire package for general use (optional).
    
    opens LibraryManagement to javafx.base; // Open for JavaFX reflection
    opens LBMS to javafx.base;
}
