package alistar.app.treasury;

import java.util.Date;

public class TreasuryReceipt {

    private int id;
    private String accountNumber;
    private String cardNumber;
    private Event event;
    private int amount;
    private int inventory;
    private Date date;
    private String receipt;

    public enum Event {
        WITHDRAWAL("WITHDRAWAL"), DEPOSIT("DEPOSIT");

        public String name;

        Event(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static Event getByName(String name) {
            for (Event e : values()) {
                if (e.name.equals(name))
                    return e;
            }
            return null;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }
}
