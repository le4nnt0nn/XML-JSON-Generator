package com.example;

import com.example.util.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

            /* Pintar en un xml concesionarios con menos de 5 operaciones */
            //xmlUtil.exportConcesionarioAlarma(documents);

            /* Generar dos XMLs de salida, uno con todas las
            operaciones de tipo venta y otro con todas las operaciones de tipo reparación */

            /* Creación de xpath */
            XPath xPath = XPathFactory.newInstance().newXPath();

            /* Creación de ventasDocument */
            Document ventasDocument = documentBuilder.newDocument();
            Element ventasRoot = ventasDocument.createElement("root");
            ventasDocument.appendChild(ventasRoot);

            /* Creación de operacionesDocument */
            Document reparacionesDocument = documentBuilder.newDocument();
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
}
