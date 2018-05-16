package net.sinsengumi.feedich.service;

import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import lombok.AllArgsConstructor;
import net.sinsengumi.feedich.exception.ApplicationException;
import net.sinsengumi.feedich.model.Feed;

@Service
@AllArgsConstructor
public class FeedOpmlService {

    public String exportOpml(List<Feed> feeds) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            // root
            Element rootElement = document.createElement("opml");
            rootElement.setAttribute("version", "1.0");
            document.appendChild(rootElement);

            // head
            Element headElement = document.createElement("head");
            Element titleElement = document.createElement("title");
            titleElement.setTextContent("Subscriptions from Feedich [https://feedich.com]");
            headElement.appendChild(titleElement);
            rootElement.appendChild(headElement);

            // body
            Element bodyElement = document.createElement("body");
            for (Feed feed : feeds) {
                Element outlineElement = document.createElement("outline");
                outlineElement.setAttribute("text", feed.getTitle());
                outlineElement.setAttribute("title", feed.getTitle());
                outlineElement.setAttribute("type", "rss");
                outlineElement.setAttribute("xmlUrl", feed.getFeedUrl());
                outlineElement.setAttribute("htmlUrl", feed.getUrl());
                bodyElement.appendChild(outlineElement);
            }
            rootElement.appendChild(bodyElement);

            return createXMLString(document);
        } catch (ParserConfigurationException | TransformerException e) {
            throw new ApplicationException(e);
        }
    }

    private String createXMLString(Document document) throws TransformerException {
        StringWriter writer = new StringWriter();
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");

        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.toString();
    }
}
