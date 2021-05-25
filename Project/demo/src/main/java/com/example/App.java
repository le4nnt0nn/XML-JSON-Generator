package com.example;

import com.example.util.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
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
            
            /* Builder factory */
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

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

            /* Creación de ventasDocument */
            Document ventasDocument = documentBuilder.newDocument();
    
            /* Creación de operacionesDocument */
            Document reparacionesDocument = documentBuilder.newDocument();






            /* Apartados */

            /* Pintar en un xml concesionarios con menos de 5 operaciones */
            xmlUtil.exportConcesionarioAlarma(documents);

             /* Generar dos XMLs de salida, uno con todas las
            operaciones de tipo venta y otro con todas las operaciones de tipo reparación */
            xmlUtil.exportByOperacionType(documents, ventasDocument, reparacionesDocument);
            
            /* Pintar JSON con todas las operaciones y sus respectivos precios de todos los concesionarios */
            xmlUtil.genConcesionarioJSON(documents);
           
        
        } catch (Exception e) {
        e.printStackTrace();
        }
            
    }
}
