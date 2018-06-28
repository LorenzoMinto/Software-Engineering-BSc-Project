package it.polimi.se2018.controller;

import it.polimi.se2018.utils.BadBehaviourRuntimeException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Persistency {

    /**
     * The file system path to find GlobalRankings.xml
     */
    private final String path;

    /**
     * List of Ranking Records representing the Global Rankings
     */
    private List<RankingRecord> globalRankings;

    /**
     * Constructor
     */
    Persistency(String path) {
        this.path = path;
    }

    /**
     * Loads to Scorer#globalRankings the rankings stored in the xml file GlobalRankings.xml
     */
    public void loadRankings() {
        List<RankingRecord> newGlobalRankings = new ArrayList<>();

        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(path);

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
        } catch (Exception e) {
            //no global ranking was found, global ranking starts out as empty
        }

        this.globalRankings = newGlobalRankings;
    }

    /**
     * Returns, if it exists, the global ranking record for the specified player
     * @param playerID the player's username
     * @return a global {@link RankingRecord}
     */
    public RankingRecord getRankingForPlayerID(String playerID) {
        for (RankingRecord r: globalRankings) {
            if (r.getPlayerID().equals(playerID)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Updates, if it already exists, the {@link RankingRecord} for the specified player. If it doesn't exist yet,
     * it is created and added to Scorer#globalRankings
     *
     * @param playerID the player's username
     * @param points the player's points
     * @param hasWon whether the player has won
     * @param timePlayed the time elapsed in the game
     */
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

    /**
     * Stores and persists to GlobalRanking.xml the updated globalRankings
     */
    public void persist() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new BadBehaviourRuntimeException();
        }
        Document document = documentBuilder != null ? documentBuilder.newDocument() : null;

        if (document == null) {
            throw new BadBehaviourRuntimeException();
        }
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
            throw new BadBehaviourRuntimeException();
        }


        StreamResult result = null;
        try {
            result = new StreamResult(new BufferedWriter(new FileWriter(path)));
        } catch (IOException e) {
            throw new BadBehaviourRuntimeException();
        }
        try {
            if (transformer != null) {
                transformer.transform(source, result);
            }
        } catch (TransformerException e) {
            throw new BadBehaviourRuntimeException();
        }
    }

    /**
     * Returns the global rankings
     * @return the global rankings
     */
    public List<RankingRecord> getGlobalRankings() { return globalRankings; }
}
