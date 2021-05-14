package com.example;

import com.example.util.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


public class App 
{
    public static void main( String[] args )
    {
        try {
            
            /* Apunte a ficheros xml */
            File file = new File("./src/xmls");
            File[] xmls = file.listFiles();

            /* Recolección de ficheros xml en Lista */
            List<Document> documents = new ArrayList<Document>();
            for(int i=0; i<xmls.length; i++){
                File xml = xmls[i];
                Document document = xmlUtil.getXMLDocument(xml.getAbsolutePath());
                documents.add(document);
            }

            /* Pintar en un xml concesionarios con menos de 5 operaciones */
            xmlUtil.exportConcesionarioAlarma(documents);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
