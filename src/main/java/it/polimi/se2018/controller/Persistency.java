package it.polimi.se2018.controller;

import it.polimi.se2018.utils.XMLFileFinder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
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
                globalRankings.add(new RankingRecord(playerID, cumulativePoints, gamesWon, gamesLost, timePlayed));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
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
        //TODO: convert ranking into new globalRanking.xml
       /* DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse("server.xml");
        Element root = document.getDocumentElement();

        Collection<Server> servers = new ArrayList<Server>();
        servers.add(new Server());

        for (Server server : servers) {
            // server elements
            Element newServer = document.createElement("server");

            Element name = document.createElement("name");
            name.appendChild(document.createTextNode(server.getName()));
            newServer.appendChild(name);

            Element port = document.createElement("port");
            port.appendChild(document.createTextNode(Integer.toString(server.getPort())));
            newServer.appendChild(port);

            root.appendChild(newServer);
        }

        DOMSource source = new DOMSource(document);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StreamResult result = new StreamResult("server.xml");
        transformer.transform(source, result);*/
    }

    public List<RankingRecord> getGlobalRankings() { return globalRankings; }
}
