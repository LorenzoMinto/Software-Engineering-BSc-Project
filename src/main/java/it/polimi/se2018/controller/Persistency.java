package it.polimi.se2018.controller;

import it.polimi.se2018.utils.XMLFileFinder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Persistency {

    /**
     * Instance of the class in order to achieve the Singleton Pattern.
     */
    private static Persistency instance = null;

    /**
     * The file system path to find GlobalRankings.xml.xml
     */
    private static final String PATH = "assets/persistency/GlobalRankings.xml";

    private List<RankingRecord> globalRankings;

    private Persistency() { }

    public static Persistency getInstance(){
        if (instance == null){
            instance = new Persistency();
        }
        return instance;
    }

    public void loadRankings() {
        List<RankingRecord> newGlobalRankings = new ArrayList<>();

        try {
            Document document = XMLFileFinder.getFileDocument(PATH);

            NodeList rankings = document.getElementsByTagName("ranking");

            NodeList gamesWonNodes = document.getElementsByTagName("gamesWon");
            NodeList gamesLostNodes = document.getElementsByTagName("gamesLost");
            NodeList timePlayedNodes = document.getElementsByTagName("timePlayed");
            NodeList cumulativePointsNodes = document.getElementsByTagName("cumulativePoints");

            for (int i = 0; i < rankings.getLength(); i++) {
                String playerID = rankings.item(i).getAttributes().getNamedItem("id").getNodeValue();
                int gamesWon = Integer.parseInt(gamesWonNodes.item(i).getTextContent());
                int gamesLost = Integer.parseInt(gamesLostNodes.item(i).getTextContent());
                int timePlayed = Integer.parseInt(timePlayedNodes.item(i).getTextContent());
                int cumulativePoints = Integer.parseInt(cumulativePointsNodes.item(i).getTextContent());
                newGlobalRankings.add(new RankingRecord(playerID, cumulativePoints, gamesWon, gamesLost, timePlayed));
            }
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

        this.globalRankings = newGlobalRankings;
    }

    public RankingRecord getRankingForPlayerID(String playerID) {
        for (RankingRecord r: globalRankings) {
            if (r.getPlayerID().equals(playerID)) {
                return r;
            }
        }
        return null;
    }

    public void updateRankingForPlayerID(String playerID, int points, boolean hasWon, int timePlayed) {
        RankingRecord updated = null;
        RankingRecord toRemove = null;
        for (RankingRecord r: globalRankings) {
            if (r.getPlayerID().equals(playerID)) {
                updated = new RankingRecord(r.getPlayerID(), r.getCumulativePoints()+points,
                        r.getGamesWon()+ (hasWon ? 1 : 0), r.getGamesLost()+ (hasWon ? 0 : 1),
                        r.getTimePlayed()+timePlayed);
                toRemove = r;
            }
        }
        //if the value has been update the old ranking record needs to be removed
        if (updated != null) {
            globalRankings.remove(toRemove);
        } else {
            //the ranking record is for a new player
            updated = new RankingRecord(playerID, points, hasWon ? 1 : 0, hasWon ? 0 : 1, timePlayed);
        }
        globalRankings.add(updated);
    }

    public void persist() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = documentBuilder != null ? documentBuilder.newDocument() : null;

        Element root = document.createElement("GlobalRanking");
        document.appendChild(root);


        for (RankingRecord record : globalRankings) {

            // server elements
            Element newRecord = document.createElement("ranking");
            newRecord.setAttribute("id", record.getPlayerID());

            Element gamesWon = document.createElement("gamesWon");
            gamesWon.appendChild(document.createTextNode(Integer.toString(record.getGamesWon())));
            newRecord.appendChild(gamesWon);

            Element gamesLost = document.createElement("gamesLost");
            gamesLost.appendChild(document.createTextNode(Integer.toString(record.getGamesLost())));
            newRecord.appendChild(gamesLost);

            Element cumulativePoints = document.createElement("cumulativePoints");
            cumulativePoints.appendChild(document.createTextNode(Integer.toString(record.getCumulativePoints())));
            newRecord.appendChild(cumulativePoints);

            Element timePlayed = document.createElement("timePlayed");
            timePlayed.appendChild(document.createTextNode(Integer.toString(record.getTimePlayed())));
            newRecord.appendChild(timePlayed);

            root.appendChild(newRecord);
        }

        DOMSource source = new DOMSource(document);


        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        StreamResult result = new StreamResult(new File(PATH));
        try {
            if (transformer != null) {
                transformer.transform(source, result);
            }
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public List<RankingRecord> getGlobalRankings() { return globalRankings; }
}
