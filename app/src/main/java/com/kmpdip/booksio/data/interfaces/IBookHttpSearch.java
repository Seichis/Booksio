package com.kmpdip.booksio.data.interfaces;


import com.kmpdip.booksio.data.structure.Book;

import org.w3c.dom.Document;

import java.util.List;
import java.util.Timer;

/**
 * Created by Ioanna on 10/31/2015.
 */
public interface IBookHttpSearch {

    Book createBookFromXMLResponse(String bookid);
    String[] consumeWebService(String bookid);
    String getTagContent(String tagName, Document dom);
    String getSubjects(String tagName, Document dom);
}
