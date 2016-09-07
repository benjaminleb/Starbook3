
package classes;

/*
ben
 */
public class OrderLine {
    //p
    private Order order;
    private Book book;
    private int itemQty;
    private float unitPrice;
    private float taxRate;
    private float discountRate;
    private Status status;
    
    //c
    public OrderLine() {
    }

    public OrderLine(Order order, Book book, int itemQty, float unitPrice, float taxRate, float discountRate, Status status) {
        this.order = order;
        this.book = book;
        this.itemQty = itemQty;
        this.unitPrice = unitPrice;
        this.taxRate = taxRate;
        this.discountRate = discountRate;
        this.status = status;
    }
    //g&s

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getItemQty() {
        return itemQty;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public float getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(float taxRate) {
        this.taxRate = taxRate;
    }

    public float getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(float discountRate) {
        this.discountRate = discountRate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    //m
    public float calculateExclTax(){
        float lPrice;
        lPrice = itemQty * unitPrice;
        //troncate to xx,xx
        lPrice = ((float) ((int) (lPrice * 100))) / 100;
        return lPrice;
    }
    
    public float calculateInclTax(){
        float lPrice;
        lPrice = calculateExclTax()+((calculateExclTax()*taxRate)/100);
        //troncate to xx,xx
        lPrice = ((float) ((int) (lPrice * 100))) / 100;
        return lPrice;
    }
    
    public float calculateLinePrice(){
        float lPrice;
        lPrice = calculateInclTax() + (calculateInclTax() * discountRate);
        //troncate to xx,xx
        lPrice = ((float) ((int) (lPrice * 100))) / 100;
        return lPrice;
    }

}
