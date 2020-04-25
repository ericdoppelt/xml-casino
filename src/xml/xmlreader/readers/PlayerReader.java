package xml.xmlreader.readers;

import Utility.Pair;
import exceptions.GeneralXMLException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import xml.xmlreader.interfaces.PlayerReaderInterface;
import xml.xmlreader.interfaces.XMLGeneratorInterface;
import xml.xmlreader.interfaces.XMLParseInterface;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class PlayerReader implements PlayerReaderInterface {

    private static Document myDocument;

    private static final String PLAYER_TAG = "Player";
    private static final String NAME_TAG = "Name";
    private static final String BANKROLL_TAG = "Bankroll";

    public PlayerReader(File file) throws GeneralXMLException {
        myDocument = XMLGeneratorInterface.createDocument(file);
    }

    public PlayerReader(String file) throws GeneralXMLException {
        myDocument = XMLGeneratorInterface.createDocument(new File(file));
    }

    @Override
    public Collection<Pair> getPlayers() {
        Collection<Pair> playerCollection = new ArrayList<>();
        NodeList playersNodeList = XMLParseInterface.getNodeList(myDocument, PLAYER_TAG);
        for (int index = 0; index < playersNodeList.getLength(); index ++) {
            Node playerNode = playersNodeList.item(index);
            Element playerElement = (Element) playerNode;
            String name = XMLParseInterface.getElement(playerElement, NAME_TAG);
            double roll = Double.parseDouble(XMLParseInterface.getElement(playerElement, BANKROLL_TAG));
            playerCollection.add(new Pair(name, roll));
        }
        return playerCollection;
    }

}
