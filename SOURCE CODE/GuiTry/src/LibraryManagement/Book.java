package LibraryManagement;

public class Book {
    private String title;
    private String author;
    private int copies;
    private String status;

    private int bookId; // Add this

    public Book (int bookId, String title, String author, int copies, String status) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.copies = copies;
        this.status = status;
    }

   

	public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
