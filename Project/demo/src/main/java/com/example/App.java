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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
