import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class CustomBoardParser extends DefaultHandler implements Serializable {
    private VersionedBoard board;

    private ArrayList<Coordinate> doubleScores;
    private ArrayList<Coordinate> tripleScores;
    private ArrayList<Coordinate> doubleWord;
    private ArrayList<Coordinate> tripleWord;

    private boolean doubleScoresRead = false;
    private boolean tripleScoresRead = false;
    private boolean doubleWordRead = false;
    private boolean tripleWordRead = false;


    private SAXParserFactory factory;
    private SAXParser saxParser;
    File xmlFile;

    public CustomBoardParser(String xmlFile) {
        factory = SAXParserFactory.newInstance();
        try{
            saxParser = factory.newSAXParser();
        }
        catch (ParserConfigurationException | SAXException e){
            e.printStackTrace();
        }
        this.xmlFile = new File(xmlFile);
    }

    public void startDocument(){
    }

    public void endDocument(){
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes){
        if(qName.equals("customBoard")){
            doubleScores = new ArrayList<>();
            tripleScores = new ArrayList<>();
            doubleWord = new ArrayList<>();
            tripleWord = new ArrayList<>();
        }
        else if(qName.equals("doubleScores")){
            doubleScoresRead = true;
        }
        else if(qName.equals("tripleScores")){
            tripleScoresRead = true;
        }
        else if(qName.equals("doubleWord")){
            doubleWordRead = true;
        }
        else if(qName.equals("tripleWord")){
            tripleWordRead = true;
        }
    }

    public void characters(char[] ch, int start, int length){
        String coordinateString = new String(ch, start, length);
        if(coordinateString.contains(",")){
            String[] coordinates = coordinateString.split(",");
            int row = Integer.parseInt(coordinates[0]);
            int col = Integer.parseInt(coordinates[1]);
            if(doubleScoresRead){
                this.addToDoubleScores(row, col);
            }
            else if(tripleScoresRead){
                this.addToTripleScores(row, col);
            }
            else if(doubleWordRead){
                this.addToDoubleWord(row, col);
            }
            else if(tripleWordRead){
                this.addToTripleWord(row, col);
            }
        }
    }

    public void endElement(String uri, String localName, String qName){
        if(qName.equals("doubleScores")){
            doubleScoresRead = false;
        }
        else if(qName.equals("tripleScores")){
            tripleScoresRead = false;
        }
        else if(qName.equals("doubleWord")){
            doubleWordRead = false;
        }
        else if(qName.equals("tripleWord")){
            tripleWordRead = false;
        }
    }

    public void addToDoubleScores(int row, int col){
        Coordinate newCoordinate = new Coordinate(row, col);
        doubleScores.add(newCoordinate);
    }

    public void addToTripleScores(int row, int col){
        Coordinate newCoordinate = new Coordinate(row, col);
        tripleScores.add(newCoordinate);
    }

    public void addToDoubleWord(int row, int col){
        Coordinate newCoordinate = new Coordinate(row, col);
        doubleWord.add(newCoordinate);
    }

    public void addToTripleWord(int row, int col){
        Coordinate newCoordinate = new Coordinate(row, col);
        tripleWord.add(newCoordinate);
    }

    public void importFromXML(){
        try{
            saxParser.parse(xmlFile, this);
        }
        catch (SAXException | IOException e){
            e.printStackTrace();
        }
    }

    public VersionedBoard getBoard(){
        board = new VersionedBoard(false);

        board.initializeDoublesFromArray(doubleScores);
        board.initializeTriplesFromArray(tripleScores);

        board.initializeDoubleWordsFromArray(doubleWord);
        board.initializeTripleWordsFromArray(tripleWord);

        board.printBoard();
        return board;
    }


}
