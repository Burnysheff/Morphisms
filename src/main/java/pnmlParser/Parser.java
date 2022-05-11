package pnmlParser;

import net.PetriNet;
import netElements.Place;
import netElements.Transition;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import stageActs.stageActs;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

public class Parser {
    public boolean Parse(File file, Map<String, String> map, List<String> listTransitions, Map<String, String> listArcs) {
        if (file.getAbsolutePath().lastIndexOf('.') == -1) {
            return false;
        }
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            document = documentBuilderFactory.newDocumentBuilder().parse(file);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return false;
        }
        NodeList places = document.getElementsByTagName("place");
        NodeList transitions = document.getElementsByTagName("transition");
        NodeList arcs = document.getElementsByTagName("arc");
        for (int i = 0; i < places.getLength(); ++i) {
            NodeList childNodes = places.item(i).getChildNodes();
            NamedNodeMap nodes = places.item(i).getAttributes();
            String id = "";
            for (int q = 0; q < nodes.getLength(); ++q) {
                if (nodes.item(q).getNodeName().equals("id")) {
                    id = nodes.item(q).getNodeValue();
                }
            }
            for (int j = 0; j < childNodes.getLength(); ++j) {
                if (childNodes.item(j).getNodeName().equals("name")) {
                    NodeList nodeList = childNodes.item(j).getChildNodes();
                    String str = "";
                    for (int k = 0; k < nodeList.getLength(); ++k) {
                        if (nodeList.item(k).getNodeName().equals("text")) {
                            str = nodeList.item(k).getTextContent();
                            break;
                        }
                    }
                    map.put(id, str);
                    break;
                }
            }
        }
        for (int i = 0; i < transitions.getLength(); ++i) {
            String id = transitions.item(i).getAttributes().item(0).toString();
            id = id.substring(id.indexOf('=') + 1);
            listTransitions.add(id);
        }
        for (int i = 0; i < arcs.getLength(); ++i) {
            NamedNodeMap nodeList = arcs.item(i).getAttributes();
            String parent = "";
            String child = "";
            for (int j = 0; j < nodeList.getLength(); ++j) {
                switch (nodeList.item(j).getNodeName()) {
                    case "source" -> parent = nodeList.item(j).getNodeValue();
                    case "target" -> child = nodeList.item(j).getNodeValue();
                }
            }
            listArcs.put(parent, child);
        }
        return places.getLength() != 0 && transitions.getLength() != 0;
    }
}
