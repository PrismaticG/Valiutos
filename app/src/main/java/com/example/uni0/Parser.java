package com.example.uni0;

import android.util.Log;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import org.xml.sax.InputSource;

public class Parser {

    public ArrayList<String> parseXML(String xmlData) {
        ArrayList<String> currencyList = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlData)));
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("Cube");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    if (element.hasAttribute("currency") && element.hasAttribute("rate")) {
                        String currency = element.getAttribute("currency");
                        String rate = element.getAttribute("rate");
                        currencyList.add(currency + " - " + rate);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Parser", "Error parsing XML: " + e.getMessage());
        }

        return currencyList;
    }
}
