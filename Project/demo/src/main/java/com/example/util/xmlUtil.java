package com.example.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class xmlUtil {

    public static void writeDocument(Document document, String suffix) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(
                    new File("exports/" + String.valueOf(new Date().getTime()) + " - "+suffix+".xml"));
            transformer.transform(domSource, streamResult);
        } catch (Exception e) {
            System.out.println(e.getCause().getMessage());
        }
    }

    /* Obtener documento XML dada una ruta relativa */
    public static Document getXMLDocument(String path) {
        Document doc = null;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Path donde se ubica el archivo
            doc = builder.parse(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return doc;
    }

    public static void exportConcesionarioAlarma(List<Document> documents) throws ParserConfigurationException, XPathExpressionException{
        /* Creación de xpath */
        XPath xPath = XPathFactory.newInstance().newXPath();
            
        /* Creación de Lista alarmas (recoge todos los docs < 5 operaciones) */

        /**
        * El único concesionario con más de 5 operaciones es QueMazda (concesionario2.xml) 
        *
        */
        List<Document> alarmas = new ArrayList<Document>();
        /* Recoger todos los documentos con menos de 5 operaciones */
        for (Document document : documents) {
            NodeList nodosOperacion = (NodeList) xPath.compile("//operacion").evaluate(document, XPathConstants.NODESET);
            if(nodosOperacion.getLength() < 5){
                alarmas.add(document);
            }
        }

        /* Creación de finalDocument */
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document finalDocument = documentBuilder.newDocument();
        /* Root implementado en el documentoFinal */
        Element root = finalDocument.createElement("root");
        finalDocument.appendChild(root);

        /* Recorro los documentos de alarmas, devuelven sus elementos, los importo y 
        los paso a un nodo final (finalConcesionario) */
        for (Document document : alarmas) {
            Element concesionario = document.getDocumentElement();
            Node finalConcesionario = finalDocument.importNode(concesionario, true);
            root.appendChild(finalConcesionario);
        }
        /* Escribe y exporta el contenido de finalDocument con el sufijo alarma */
        xmlUtil.writeDocument(finalDocument, "alarma");

    }
    
}
