package edu.cmu.cs.cs214.hw5b.dataplugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class APIData {

	public static void main(String[] args) throws IOException {

		Display disp = new Display();

		Document doc = generateXML("1940345");
		disp.getConditions(doc);

	}

	public static Document generateXML(String code) throws IOException {

		String url = null;
		String XmlData = null;

		// creating the URL
		url = "http://weather.yahooapis.com/forecastrss?w=" + code;
		URL xmlUrl = new URL(url);
		InputStream in = xmlUrl.openStream();

		// parsing the XmlUrl
		Document doc = parse(in);

		return doc;

	}

	public static Document parse(InputStream is) {
		Document doc = null;
		DocumentBuilderFactory domFactory;
		DocumentBuilder builder;

		try {
			domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setValidating(false);
			domFactory.setNamespaceAware(false);
			builder = domFactory.newDocumentBuilder();

			doc = builder.parse(is);
		} catch (Exception ex) {
			System.err.println("unable to load XML: " + ex);
		}
		return doc;
	}
}

class Display {

	static void getConditions(Document doc) {

		String city = null;
		String unit = null;

		try {

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("rss");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					NodeList nl = eElement.getElementsByTagName("yweather:location");

					for (int tempr = 0; tempr < nl.getLength(); tempr++) {

						Node n = nl.item(tempr);

						if (nNode.getNodeType() == Node.ELEMENT_NODE) {

							Element e = (Element) n;
							city = e.getAttribute("city");
							System.out.println("The City Is : " + city);

						}
					}

					NodeList nl2 = eElement.getElementsByTagName("yweather:units");

					for (int tempr = 0; tempr < nl2.getLength(); tempr++) {

						Node n2 = nl2.item(tempr);

						if (nNode.getNodeType() == Node.ELEMENT_NODE) {

							Element e2 = (Element) n2;
							unit = e2.getAttribute("temperature");

						}
					}

					NodeList nl3 = eElement.getElementsByTagName("yweather:condition");

					for (int tempr = 0; tempr < nl3.getLength(); tempr++) {

						Node n3 = nl3.item(tempr);

						if (nNode.getNodeType() == Node.ELEMENT_NODE) {

							Element e3 = (Element) n3;
							System.out.println("The Temperature In " + city + " Is : "
									+ e3.getAttribute("temp") + " " + unit);
						}
					}

					NodeList nl4 = eElement.getElementsByTagName("yweather:atmosphere");

					for (int tempr = 0; tempr < nl4.getLength(); tempr++) {

						Node n4 = nl4.item(tempr);

						if (nNode.getNodeType() == Node.ELEMENT_NODE) {

							Element e4 = (Element) n4;
							System.out.println("The Humidity In " + city + " Is : "
									+ e4.getAttribute("humidity"));
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}