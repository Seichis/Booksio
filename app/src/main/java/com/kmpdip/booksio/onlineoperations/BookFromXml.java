package com.kmpdip.booksio.onlineoperations;


import android.graphics.Bitmap;
import android.util.Log;

import com.kmpdip.booksio.data.structure.Recommendation;

import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * Created by Ioanna on 10/31/2015.
 */
public class BookFromXml {
    private static BookFromXml bookFromXml=new BookFromXml();

    private BookFromXml() {
    }
    public static BookFromXml getInstance(){
        return bookFromXml;
    }

    public Recommendation createBookFromXMLResponse(Map<String, String> bookResponse) {
        Recommendation.RecommendationBuilder builder = new Recommendation.RecommendationBuilder();
        Bitmap imageLink=GoogleApiRequest.getInstance().getImage(bookResponse.get("isbn"));
        Recommendation book = builder.ID(bookResponse.get("id")).title(bookResponse.get("title")).author(bookResponse.get("author"))
                .description(bookResponse.get("abstract")).date(bookResponse.get("date"))
                .genre(bookResponse.get("subjects")).image(imageLink).build();
        return book;
    }

    public HashMap<String, String> consumeWebService(String bookid) {
        String DBCURL = "http://oss-services.dbc.dk/opensearch/?action=search&query={query}&agency=100200&profile=test&start=1&stepValue=5";
        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        InputStream is;
        Document dom;
        HashMap<String, String> bookValues = new HashMap<String, String>();
        String bookAuthor = "", bookAbstract = "", bookSubjects = "", bookTitle = "", bookDate = "", bookIsbn="";

        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(new SimpleXmlHttpMessageConverter());

        final String result = restTemplate.getForObject(DBCURL, String.class, bookid);

        try {
            factory = DocumentBuilderFactory.newInstance();
            is = new ByteArrayInputStream(result.getBytes("UTF-8"));
            builder = factory.newDocumentBuilder();
            dom = builder.parse(is);

            bookValues.put("id", bookid);

            bookTitle = getTagContent("dc:title", dom);
            bookValues.put("title", bookTitle);

            bookAuthor = getTagContent("dc:creator", dom);
            bookValues.put("author", bookAuthor);

            bookAbstract = getTagContent("dcterms:abstract", dom);
            bookValues.put("abstract", bookAbstract);

            bookDate = getTagContent("dc:date", dom);
            bookValues.put("date", bookDate);

            bookSubjects = getSubjects("dc:subject", dom);
            bookValues.put("subjects", bookSubjects);

            bookIsbn = getIsbn("dc:identifier", dom);
            bookValues.put("isbn", bookIsbn);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bookValues;
    }

    public String getTagContent(String tagName, Document dom) {
        NodeList bookOutput = dom.getElementsByTagName(tagName);
        String bookTag = "";
        for (int i = 0; i < bookOutput.getLength(); i++) {
            bookTag = bookOutput.item(i).getTextContent();
            break;
        }
        return bookTag;
    }

    public String getSubjects(String tagName, Document dom) {
        NodeList bookOutput = dom.getElementsByTagName(tagName);
        String subjects = "";
        for (int i = 0; i < bookOutput.getLength(); i++) {
            subjects = bookOutput.item(i).getTextContent() + ", " + subjects;
        }
        return subjects;
    }

    public String getIsbn(String tagName, Document dom){
        NodeList bookOutput = dom.getElementsByTagName(tagName);
        String bookISBN="";
        if (bookOutput != null){
            for (int i = 0; i < bookOutput.getLength(); i++) {
                Node node = bookOutput.item(i).getAttributes().getNamedItem("xsi:type");
                if (node != null){
                    if(node.getNodeValue().equals("dkdcplus:ISBN")) {
                        bookISBN = bookOutput.item(i).getTextContent();
                        Log.i("Node", bookISBN);
                    }
                }
            }
        }
        return bookISBN;
    }
}

