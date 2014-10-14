/**
 * 
 */
package channy.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author chanfun
 * 
 */
public class XmlOperator {
	/**
	 * @param path
	 *            Path of XML file.
	 * @param xpath
	 *            xPath expression.
	 * @return list Result attribute list filtered by xPath.
	 * @throws XPathExpressionException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static ArrayList<String> read(String path, String xpath, String attribute) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
		ArrayList<String> list = new ArrayList<String>();

		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document doc = builder.parse(new File(path));
		XPathFactory factory = XPathFactory.newInstance();
		XPath xp = factory.newXPath();
		XPathExpression expression = xp.compile(xpath);
		NodeList nodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (attribute != null && !attribute.equals("")) {
				if (node.hasAttributes()) {
					Node attr = node.getAttributes().getNamedItem(attribute);
					if (attr != null) {
						list.add(attr.getNodeValue());
					}
				}
			} else {
				String value = node.getTextContent();
				if (value != null) {
					list.add(value);
				}
			}
		}
		return list;
	}

	private static ErrorCode writeXml(Document doc, String fileName) {
		PrintWriter pw = null;
		try {
			doc.normalize();
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");

			DOMSource source = new DOMSource(doc);
			pw = new PrintWriter(new FileOutputStream(fileName));
			StreamResult result = new StreamResult(pw);
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			return ErrorCode.FILE_WRITE_ERROR;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return ErrorCode.FILE_NOT_EXISTED;
		} catch (TransformerException e) {
			e.printStackTrace();
			return ErrorCode.FILE_WRITE_ERROR;
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
		return ErrorCode.OK;
	}

	public static Number read(String path, String xpath) throws ChannyException {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder = null;
		try {
			builder = domFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
			throw new ChannyException(ErrorCode.GENERIC_ERROR);
		}
		Document doc = null;
		try {
			doc = builder.parse(new File(path));
		} catch (SAXException e1) {
			e1.printStackTrace();
			throw new ChannyException(ErrorCode.FILE_READ_ERROR);
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new ChannyException(ErrorCode.FILE_OPEN_ERROR);
		}
		XPathFactory factory = XPathFactory.newInstance();
		XPath xp = factory.newXPath();
		try {
			XPathExpression expression = xp.compile(xpath);
			Number count = (Number) expression.evaluate(doc, XPathConstants.NUMBER);
			return count;
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			throw new ChannyException(ErrorCode.GENERIC_ERROR);
		}
	}

	public static void main(String[] args) {
	}

	/**
	 * 
	 * @param predicate
	 *            :String that may contains single quotation marks
	 * @return: Escaped predicate
	 */
	public static String escape(String predicate) {
		StringBuffer sb = new StringBuffer();
		if (predicate.indexOf('\'') != -1) {
			StringTokenizer st = new StringTokenizer(predicate, "'\"", true);
			sb.append("concat(");
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				if (token.equalsIgnoreCase("'")) {
					sb.append("\"");
					sb.append(token);
					sb.append("\"");
				} else {
					sb.append("'");
					sb.append(token);
					sb.append("'");
				}
				if (st.countTokens() != 0)
					sb.append(",");
			}
			sb.append(")");
		} else {
			return "'" + predicate + "'";
		}

		return sb.toString();
	}

	/**
	 * Assemble XPath search string
	 * 
	 * @param condition
	 *            : search key words, separated by comma.
	 * @return assembled XPath expression.
	 */
	public static String assembleXpathCondition(Map<String, String> filter) {
		String result = "";
		if (filter == null || filter.size() == 0) {
			return result;
		}
		boolean flag = false;
		for (String key : filter.keySet()) {
			if (!flag) {
				result += String.format("(contains(translate(@%s, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), %s)", key, XmlOperator
						.escape(filter.get(key).trim()).toLowerCase());
				flag = true;
			} else {
				result += String.format(" or contains(translate(@%s, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), %s)", key,
						XmlOperator.escape(filter.get(key).trim()).toLowerCase());
			}
		}
		if (flag) {
			result += ")";
		}

		return result;
	}

	public static ErrorCode append(String path, String xpath, String element, String attribute, String value) {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder = null;
		try {
			builder = domFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
			return ErrorCode.GENERIC_ERROR;
		}
		Document doc = null;
		try {
			doc = builder.parse(new File(path));
		} catch (SAXException e1) {
			e1.printStackTrace();
			return ErrorCode.GENERIC_ERROR;
		} catch (IOException e1) {
			e1.printStackTrace();
			return ErrorCode.FILE_OPEN_ERROR;
		}
		XPathFactory factory = XPathFactory.newInstance();
		XPath xp = factory.newXPath();
		try {
			XPathExpression expression = xp.compile(xpath);
			NodeList nodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (element != null && !element.equals("")) {
					Element e = doc.createElement(element);
					if (attribute != null && !attribute.equals("")) {
						e.setAttribute(attribute, value);
						node.appendChild(e);
					} else {
						e.setTextContent(value);
						node.appendChild(e);
					}
				} else {
					Element e = (Element) node;
					if (attribute != null && !attribute.equals("")) {
						e.setAttribute(attribute, value);
					} else {
						e.setTextContent(value);
					}
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return ErrorCode.GENERIC_ERROR;
		}
		return writeXml(doc, path);
	}

	public static ErrorCode remove(String path, String xpath, String attribute) {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder = null;
		try {
			builder = domFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
			return ErrorCode.GENERIC_ERROR;
		}
		Document doc = null;
		try {
			doc = builder.parse(new File(path));
		} catch (SAXException e1) {
			e1.printStackTrace();
			return ErrorCode.GENERIC_ERROR;
		} catch (IOException e1) {
			e1.printStackTrace();
			return ErrorCode.FILE_OPEN_ERROR;
		}
		XPathFactory factory = XPathFactory.newInstance();
		XPath xp = factory.newXPath();
		try {
			XPathExpression expression = xp.compile(xpath);
			NodeList nodes = (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (attribute == null || attribute.isEmpty()) {
					Node parent = node.getParentNode();
					if (parent != null) {
						parent.removeChild(node);
					}
				} else {
					Element e = (Element) node;
					if (e.hasAttribute(attribute)) {
						e.removeAttribute(attribute);
					}
				}
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
			return ErrorCode.GENERIC_ERROR;
		}
		return writeXml(doc, path);
	}
}
