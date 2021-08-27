package com.example.thebookspot;

public class Book {
private final String bookTitle,author;
private final int bookImage;
private final String bookCost;
private final String webLink;
private final double avgRating;
public Book(String bookTitle, int bookImage, String author, String bookCost,String webLink,double avgRating){
    this.author=author;
    this.bookCost=bookCost;
    this.bookTitle=bookTitle;
    this.bookImage=bookImage;
    this.webLink=webLink;
this.avgRating=avgRating;
}

    public String getBookCost() {
        return bookCost;
    }

    public int  getBookImage() {
        return bookImage;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getAuthor() {
        return author;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public String getWebLink() {
        return webLink;
    }
}
