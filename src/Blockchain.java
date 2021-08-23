import java.io.IOException;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.ArrayList;

public class Blockchain {

    private static Blockchain instance = null;
    ArrayList<Block> chain;
    public int difficulty;

    private Blockchain() {
        chain = new ArrayList<Block>();
        this.difficulty = 6;
        Transaction genTrans = new Transaction("0", "1", "A", 0); // NULL TRANSACTION FOR GENESIS BLOCK
        Block genesisBlock = new Block(genTrans, "0");
        chain.add(genesisBlock);
    }

    public static Blockchain getInstance() throws IOException {
       /* ReadWrite rw = new ReadWrite();
        if(rw.chainFileEmpty()){
            if(instance == null)
                instance = new Blockchain();
        }else{
            instance = rw.readChain();
        }*/
        if(instance == null){
            instance = new Blockchain();
        }
        return instance;
    }

    public static boolean getChainFromTxt() throws IOException {
        ReadWrite readWrite = new ReadWrite();
        readWrite.readChain();
        return true;
    }
    public int getBalance(String address, String token){
        ReadWrite rw = new ReadWrite();
        int balance = 0;
        System.out.println("Chain size : " + chain.size());
        for(int i = 0; i < chain.size(); i++){
            System.out.println(chain.get(i).getTransaction().fromAddress + "= bu  ve bu =" + address);
            System.out.println(chain.get(i).getTransaction().toString());
                if(chain.get(i).getTransaction().fromAddress.contains(address)){
                    System.out.println("Buldum from address");
                    balance -= chain.get(i).getTransaction().getAmount();
                }
            System.out.println(chain.get(i).getTransaction().toAddress + "= şu  ve bu =" + address);
            if(chain.get(i).getTransaction().toAddress.contains(address)){
                    System.out.println("Buldum to address");
                    balance += chain.get(i).getTransaction().getAmount();
                }
        }
        return balance;
    }

    public String getTransactions(String address){
        String send = "";
        for(int i = 0; i < chain.size(); i++){
            if(chain.get(i).getTransaction().toAddress.equals(address) || chain.get(i).getTransaction().fromAddress.equals(address)) {
                    send = send + chain.get(i).getTransaction().getAmount() + " : " +
                            chain.get(i).getTransaction().getFromAddress() + " : " +
                            chain.get(i).getTransaction().getToAddress();
            }
        }
        return send;
    }

    public void addBlock( String fromAd, String toAdd, String token, int amount) throws IOException {
        Transaction nTran = new Transaction( fromAd,  toAdd, token, amount);
        Block nBlock = new Block(nTran, chain.get(chain.size()-1).hash);
        nBlock.proofOfWork();
        chain.add(nBlock);
    }
    public void addBlockTxt(String prevHash, String hash, String tstamp, String address, String toAdd, String moneyType, int amount, int nonce){
        Block nBlock = new Block();
        nBlock.setHash(hash);
        nBlock.setPreviousHash(prevHash);
        nBlock.setTimestamp(Instant.parse(tstamp));
        Transaction nTrans = new Transaction(address, toAdd, moneyType, amount);
        nBlock.setTransaction(nTrans);
        nBlock.nonce = nonce;
        chain.add(nBlock);
    }
    public void print(){
        for(int i = 0 ; i < chain.size(); i++ ){
            System.out.println("Chain No:" + i + " , Block Hash:" + chain.get(i).hash + " , Block previous Hash:" + chain.get(i).previousHash + "Block time stamp:" + chain.get(i).timestamp);
            System.out.println("Block Transaction from:" + chain.get(i).getTransaction().getFromAddress() + " , to:"+chain.get(i).getTransaction().getToAddress() + " ,Token: " + chain.get(i).getTransaction().getToken() + " ,Amount: " + chain.get(i).getTransaction().getAmount());
        }
    }

}
