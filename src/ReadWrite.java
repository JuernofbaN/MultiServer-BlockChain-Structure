import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.Scanner;

public class ReadWrite {
    String txtName = "chains.txt";
    void createFile() throws IOException {
        File myObj = new File(txtName);
        if (myObj.createNewFile()) {
            System.out.println("File created: " + myObj.getName());
        }
    }

    void writeTxt(String data) throws IOException {
        createFile();
        FileWriter myWriter = new FileWriter(txtName);
        myWriter.write(data);
        myWriter.close();
        System.out.println("Successfully wrote to the file.");
    }

    boolean chainFileEmpty(){
        File file = new File(txtName);
        if (file.length() == 0)
            return true;
        else
           return false;
    }

    public void readChain() throws IOException{
        Blockchain bChain = Blockchain.getInstance();
        File myObj = new File(txtName);
        Scanner myReader = new Scanner(myObj);
        int ctr = 0;
        int indexer = 0;
        String hash = "";
        String prevHash = "";
        String tStamp = "";
        String fromAddress = "";
        String toAddress = "";
        String token = "";
        int amount = 0;
        int nonce = 0;
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            if(ctr == 0){
                // DATA == First block's gen blocks hash key. So we need it.
                bChain.chain.get(0).hash = data;
            }else if(ctr > 8){
                if(indexer == 0){
                    System.out.println("hash = " + data);
                    hash = data;
                }else if(indexer == 1){
                    System.out.println("prevhash = " + data);
                    prevHash = data;
                }else if(indexer == 2){
                    System.out.println("tstamph = " + data);
                    tStamp = data;
                }else if(indexer == 3){
                    System.out.println("fromaddress = " + data);
                    fromAddress = data;
                }else if(indexer == 4){
                    System.out.println("toaddress = " + data);
                    toAddress = data;
                }else if(indexer == 5){
                    System.out.println("token = " + data);
                    token = data;
                }else if(indexer == 6){
                    System.out.println("amount = " + data);
                    amount = Integer.parseInt(data);
                }
                else if(indexer == 7){
                    System.out.println("nonce = " + data);
                    nonce = Integer.parseInt(data);
                }
                indexer++;
                if(indexer == 8){
                    indexer = -1;
                    bChain.addBlockTxt(prevHash, hash, tStamp, fromAddress, toAddress, token,amount,nonce);
                }
            }
            ctr++;
        }
        myReader.close();
        return;
    }

    void writeBlockChain(Blockchain bChain) throws IOException {
        String data = "";
        for( int i = 0; i < bChain.chain.size() ; i++){
            data += bChain.chain.get(i).hash + "\n";
            data += bChain.chain.get(i).previousHash + "\n";
            data += bChain.chain.get(i).timestamp + "\n";
            data += bChain.chain.get(i).getTransaction().getFromAddress() + "\n";
            data += bChain.chain.get(i).getTransaction().getToAddress() + "\n";
            data += bChain.chain.get(i).getTransaction().getToken() + "\n";
            data += bChain.chain.get(i).getTransaction().getAmount() + "\n";
            data += bChain.chain.get(i).nonce + "\n";
            data += "\n";
        }
        writeTxt(data);
    }

}
