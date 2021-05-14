package com.example.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
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

    public static void exportByOperacionType(List<Document> documents, Document ventasDocument, Document reparacionesDocument){
        try {
            /* Creación de xpath */
            XPath xPath = XPathFactory.newInstance().newXPath();

            /* Asignación root para ventasDocument */
            Element ventasRoot = ventasDocument.createElement("root");
            ventasDocument.appendChild(ventasRoot);

            /* Asignación root para reparacionesDocument */
            Element reparacionesRoot = reparacionesDocument.createElement("root");
            reparacionesDocument.appendChild(reparacionesRoot);

            /* Recorrer documents y sacar las operaciones según su atributo (venta o reparación)*/
            for (Document document : documents) {
                NodeList nodosOperacion = (NodeList) xPath.compile("//operacion").evaluate(document, XPathConstants.NODESET);
                for(int i=0; i < nodosOperacion.getLength(); i++){
                    Node nodoOperacion = nodosOperacion.item(i);
                    /* Recoge los atributos de operacion llamados "tipo" */
                    NamedNodeMap attributesMap = nodoOperacion.getAttributes();
                    String type = attributesMap.getNamedItem("tipo").getNodeValue();
                    if(type.equals("venta")){
                        Node finalVenta = ventasDocument.importNode(nodoOperacion, true);
                        ventasRoot.appendChild(finalVenta);
                    } else if(type.equals("reparacion")){
                        Node finalReparacion = reparacionesDocument.importNode(nodoOperacion, true);
                        reparacionesRoot.appendChild(finalReparacion);
                    }
                }
            }

            /* Escribe y exporta el contenido de ventasDocument con el sufijo venta */
            xmlUtil.writeDocument(ventasDocument, "venta");
            /* Escribe y exporta el contenido de reparacionesDocument con el sufijo reparacion */
            xmlUtil.writeDocument(reparacionesDocument, "reparacion");

        } catch (Exception e) {
            e.printStackTrace();
        }

        
    }

    public static void genConcesionarioJSON(List<Document> documents) throws XPathExpressionException {
        /* Creación de xpath */
        XPath xPath = XPathFactory.newInstance().newXPath();

        /* Creación de array operaciones */
        JSONArray operaciones = new JSONArray();

        /* Creación de JSON root y adición de operaciones*/
        JSONObject json = new JSONObject();
        json.put("operaciones", operaciones);

        /* Creación de JSON precio */
        JSONObject precio = new JSONObject();

        /* Recorre los nodos operacion */
        for (Document document : documents) {
            NodeList nodosPrecio = (NodeList) xPath.compile("//precio").evaluate(document, XPathConstants.NODESET);
            for(int i=0; i < nodosPrecio.getLength(); i++){
                Node nodoPrecio = nodosPrecio.item(i);
                String precioText = nodoPrecio.getTextContent();
                precio.put("precio", precioText);
                operaciones.put(precio);
            }
        }

        /* Escribir y pintar JSON */
        try{
            FileWriter jsonFile = new FileWriter("exports/operaciones.json");
            jsonFile.write(json.toString());
            jsonFile.flush();
            jsonFile.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        System.out.println(json.toString());
   }
}
