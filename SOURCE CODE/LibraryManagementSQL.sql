-- Create the database
CREATE DATABASE LibraryManagement;
GO

-- Use the LibraryManagement database
USE LibraryManagement;
GO

-- Drop the table if it already exists
IF OBJECT_ID('dbo.Books', 'U') IS NOT NULL
    DROP TABLE dbo.Books;
GO

-- Create the Books table
CREATE TABLE dbo.Books (
    BookID INT IDENTITY(1,1) PRIMARY KEY, -- Auto-incrementing ID
    Title NVARCHAR(255) NOT NULL,         -- Book title
    Author NVARCHAR(255) NOT NULL,        -- Book author
    Copies INT NOT NULL,                  -- Number of available copies
    IsBorrowed BIT NOT NULL DEFAULT 0     -- 1 if no copies left; otherwise 0
);
GO

-- Insert sample books (all available initially)
INSERT INTO Books (Title, Author, Copies, IsBorrowed)
VALUES 
    ('The Great Gatsby', 'F. Scott Fitzgerald', 3, 0),
    ('To Kill a Mockingbird', 'Harper Lee', 2, 0),
    ('1984', 'George Orwell', 4, 0),
    ('Pride and Prejudice', 'Jane Austen', 3, 0);
GO

-- View table
SELECT * FROM Books;

SELECT *, 
       CASE WHEN Copies = 0 THEN 'Borrowed' ELSE 'Available' END AS Status
FROM Books;




