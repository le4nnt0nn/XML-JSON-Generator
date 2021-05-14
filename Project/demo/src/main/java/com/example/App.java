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

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Hello world!
 *
 */
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

            /* Creación de xpath */
            XPath xPath = XPathFactory.newInstance().newXPath();
            
            /* Creación de Lista alarmas (recoge todos los docs < 5 operaciones) */
            List<Document> alarmas = new ArrayList<Document>();
            /* Recoger todos los documentos con menos de 5 operaciones */
            for (Document document : documents) {
                NodeList nodosOperacion = (NodeList) xPath.compile("//operacion").evaluate(document, XPathConstants.NODESET);
                if(nodosOperacion.getLength() < 5){
                    alarmas.add(document);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
