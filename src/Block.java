import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.time.Instant;

public class Block {



    public String hash;
    public String previousHash;
    public int nonce;
    private Transaction transaction;
    public Instant timestamp;

    public Block(Transaction transaction, String previousHash) {
        this.transaction = new Transaction(transaction.getFromAddress(), transaction.getToAddress(),
                transaction.getToken(), transaction.getAmount());
        this.previousHash = previousHash;
        this.timestamp = Instant.now();
        this.hash = calculateHash();
        this.nonce = (int)(Math.random() * Integer.MAX_VALUE);
    }
    public Block(){

    }
    public static String getSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getDifficultyString() throws IOException {
        String temp = "";
        for(int i=0; i < Blockchain.getInstance().difficulty; ++i)
            temp = temp.concat("0");

        return temp;
    }

    public void proofOfWork() throws IOException {
        boolean check = true;
        do{
            if(S.received){
                S.received = false;
                int backupNonce = this.nonce;
                this.nonce = S.receivedNonce;
                this.hash = calculateHash();
                if(this.hash.substring(0, Blockchain.getInstance().difficulty).equals(getDifficultyString())){ //TODO
                    System.out.println("Buldum nonce: " + this.nonce);
                    return;
                }
                this.nonce = backupNonce;
            }
            this.nonce++; //TODO
            this.hash = this.calculateHash();
        }while (!this.hash.substring(0,Blockchain.getInstance().difficulty).equals(getDifficultyString())); //TODO
        System.out.println("Hashi Buldum karşıya Yolluyorum");
        S.canSend = true;
        S.nonceToSend = this.nonce;
    }
    public String calculateHash() {
        String data = this.transaction.getFromAddress() + this.transaction.getToAddress() + this.transaction.getToken() + this.transaction.getAmount() + nonce ;
        String calculatedHash = getSHA256(previousHash + data);
        return calculatedHash;

    }
    public Instant getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
    public String getHash() {
        return hash;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }
    public Transaction getTransaction() {
        return transaction;
    }
    public void setTransaction(Transaction tran){
        this.transaction = new Transaction();
        this.transaction.amount = tran.getAmount();
        this.transaction.toAddress = tran.getToAddress();
        this.transaction.fromAddress = tran.getFromAddress();
        this.transaction.token = tran.getToken();
    }
    public String getPreviousHash() {
        return previousHash;
    }
    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }
}