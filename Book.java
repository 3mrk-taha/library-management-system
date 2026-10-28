public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private int copiesTotal;
    private int copiesAvailable;

    public Book(int id, String title, String author, String isbn, int copiesTotal, int copiesAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.copiesTotal = copiesTotal;
        this.copiesAvailable = copiesAvailable;
    }

    public Book(String title, String author, String isbn, int copies) {
        this(0, title, author, isbn, copies, copies);
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public int getCopiesTotal() { return copiesTotal; }
    public int getCopiesAvailable() { return copiesAvailable; }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s | ISBN: %s | Available: %d/%d",
                id, title, author, isbn, copiesAvailable, copiesTotal);
    }
}
