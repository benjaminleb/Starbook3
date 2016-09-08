package classes;

import java.util.*;

/*
 ben
 */
public class Book {

    //p
    private String isbn;
    private Publisher publisher;
    private String title;
    private String subtitle;
    private Date date;
    private String picture;
    private String summary;
    private String idiom;
    private float price;
    private Tax tax;
    private int quantity;
    private String pages;
    private String print;
    private int weight;

    //c
    public Book() {
    }

    public Book(String isbn, Publisher publisher, String title, Date date, float price, Tax tax, int quantity) {
        this.isbn = isbn;
        this.publisher = publisher;
        this.title = title;
        this.date = date;
        this.price = price;
        this.tax = tax;
        this.quantity = quantity;
    }

    public Book(String isbn, Publisher publisher, Author author, String title, String subtitle, Date date, String picture, String summary, String idiom, float price, Tax tax, int quantity, String pages, String print, int weight) {
        this.isbn = isbn;
        this.publisher = publisher;
        this.title = title;
        this.subtitle = subtitle;
        this.date = date;
        this.picture = picture;
        this.summary = summary;
        this.idiom = idiom;
        this.price = price;
        this.tax = tax;
        this.quantity = quantity;
        this.pages = pages;
        this.print = print;
        this.weight = weight;
    }
    //g&s

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {     
        this.date = date;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIdiom() {
        return idiom;
    }

    public void setIdiom(String idiom) {
        this.idiom = idiom;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
        }
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        if (Integer.valueOf(pages) > 0) {
            this.pages = pages;
        }
    }

    public String getPrint() {
        return print;
    }

    public void setPrint(String print) {
        this.print = print;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }


    //m
    public float calculateInclTax() {
        float tPrice;
        tPrice = price + ((price) * (tax.getRate()) / 100);
        //troncate to xx,xx
        tPrice = ((float) ((int) (tPrice * 100))) / 100;
        return tPrice;
    }
}
