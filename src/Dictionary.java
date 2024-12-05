import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Dictionary {

    private final ArrayList<String> validWords;

    public Dictionary() throws IOException{
        validWords = new ArrayList<>();
        this.readFromWeb();
    }

    public void readFromWeb() throws IOException {
        URL url = new URL("https://www.mit.edu/~ecprice/wordlist.10000");
        Scanner reader = new Scanner(url.openStream());
        StringBuffer buffer = new StringBuffer();
        while(reader.hasNextLine()){
            String word = reader.nextLine();
            validWords.add(word);
        }
    }

    private void printDictionary(){
        for(String word : validWords){
            System.out.println(word);
        }
    }

    public boolean inDictionary(String word){
        return validWords.contains(word);
    }

}
