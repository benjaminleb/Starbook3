package classes;

import java.util.*;

/*
 ben
 */
public class Review {

    //p
    private int id;
    private Book book;
    private Customer customer;
    private OrderLine orderLine;
    private String comment;
    private int rating;
    private Date date;

    //c
    public Review() {
    }

    public Review(int id, Book book, Customer customer, OrderLine orderLine, int rating, Date date) {
        this.id = id;
        this.book = book;
        this.customer = customer;
        this.orderLine = orderLine;
        this.rating = rating;
        this.date = date;
    }

    public Review(int id, Book book, Customer customer, OrderLine orderLine, String comment, int rating, Date date) {
        this(id, book, customer, orderLine, rating, date);
        this.comment = comment;
    }

    //g&s

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public OrderLine getOrderLine() {
        return orderLine;
    }

    public void setOrderLine(OrderLine orderLine) {
        this.orderLine = orderLine;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        if ((rating >= 0) && (rating <= 10)) {
            this.rating = rating;
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
