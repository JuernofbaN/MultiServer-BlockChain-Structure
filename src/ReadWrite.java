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

    Blockchain readChain() throws IOException{
        Blockchain bChain = Blockchain.getInstance();
        File myObj = new File(txtName);
        Scanner myReader = new Scanner(myObj);
        int ctr = 0;
        int indexer = 0;
        String hash = "";
        String prevHash = "";
        Instant tStamp = null;
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
            }else if(ctr > 7){
                if(indexer == 0){
                    hash = data;
                }else if(indexer == 1){
                    prevHash = data;
                }else if(indexer == 2){
                    tStamp = Instant.parse(data);
                }else if(indexer == 3){
                    fromAddress = data;
                }else if(indexer == 4){
                    toAddress = data;
                }else if(indexer == 5){
                    token = data;
                }else if(indexer == 6){
                    amount = Integer.parseInt(data);
                }
                else if(indexer == 7){
                    nonce = Integer.parseInt(data);
                }
                indexer++;
                if(indexer == 8){
                    indexer = 0;
                    bChain.addBlock(fromAddress, toAddress, token, amount);
                    bChain.chain.get(bChain.chain.size()-1).nonce = nonce;
                }
            }
            ctr++;
        }
        myReader.close();
        return bChain;
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
