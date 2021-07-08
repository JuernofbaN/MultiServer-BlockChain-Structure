import java.net.UnknownHostException;
import java.util.ArrayList;

public class Blockchain {

    private static Blockchain instance = null;
    ArrayList<Block> chain;
    public int difficulty;

    private Blockchain() {
        chain = new ArrayList<Block>();
        this.difficulty = 5;
        Transaction genTrans = new Transaction("0", "1", "A", 0); // NULL TRANSACTION FOR GENESIS BLOCK
        Block genesisBlock = new Block(genTrans, "0");
        chain.add(genesisBlock);
    }

    public static Blockchain getInstance(){
        if(instance == null)
            instance = new Blockchain();
        return instance;
    }

    public int getBalance(String address, String token){
        int balance = 0;
        for(int i = 0; i < chain.size(); i++){
            if(chain.get(i).getTransaction().getToken().equals(token) ) {
                if(chain.get(i).getTransaction().fromAddress.equals(address)){
                    balance -= chain.get(i).getTransaction().getAmount();
                }
                if(chain.get(i).getTransaction().toAddress.equals(address)){
                    balance += chain.get(i).getTransaction().getAmount();
                }
            }
        }
        return balance;
    }

    public void addBlock( String fromAd, String toAdd, String token, int amount) throws UnknownHostException {
        Transaction nTran = new Transaction( fromAd,  toAdd, token, amount);
        Block nBlock = new Block(nTran, chain.get(chain.size()-1).hash);
        nBlock.proofOfWork();
        chain.add(nBlock);
    }

    public void print(){
        for(int i = 0 ; i < chain.size(); i++ ){
            System.out.println("Chain No:" + i + " , Block Hash:" + chain.get(i).hash + " , Block previous Hash:" + chain.get(i).previousHash + "Block time stamp:" + chain.get(i).timestamp);
            System.out.println("Block Transaction from:" + chain.get(i).getTransaction().getFromAddress() + " , to:"+chain.get(i).getTransaction().getToAddress() + " ,Token: " + chain.get(i).getTransaction().getToken() + " ,Amount: " + chain.get(i).getTransaction().getAmount());
        }
    }

}
