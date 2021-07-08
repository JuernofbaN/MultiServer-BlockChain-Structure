public class Transaction {
    public String fromAddress;
    public String toAddress;
    public String token;
    public int amount;

    public Transaction(String fromAd, String toAdd, String token, int amount ) {
        this.fromAddress = fromAd;
        this.toAddress = toAdd;
        this.token = token;
        this.amount = amount;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
