/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webapiapplication;

import cn.edu.tongji.cdi.soul.domain.Hotel;
import cn.edu.tongji.cdi.soul.domain.HotelDetailInfo;
import com.ctrip.openapi.java.utils.ConfigData;
import com.ctrip.openapi.java.utils.SignatureUtils;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;
import javax.xml.xpath.*;

/**
 *
 * @author bianshujun
 */
public class TCXml {

    public static String getTCXMLDoc(String tcServiceName, String tcDigitalSign, String tcReqTime,
            String tcCityId, String tcComeDate, String tcLeaveDate, String tcStarRatedId) {
        try {
            Document doc;
            Element request;

            Element header;
            Element version;
            Element accountID;
            Element serviceName;
            Element digitalSign;
            Element reqTime;

            Element body;
            Element cityId;
            Element comeDate;
            Element leaveDate;
            Element clientIp;
            Element starRatedId;
            Element searchFields;
            Element keyWord;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            doc = builder.newDocument();
            if (doc != null) {
                request = doc.createElement("request");

                header = doc.createElement("header");
                setXmlElement(request, header, "", doc);

                version = doc.createElement("version");
                setXmlElement(header, version, "20111128102912", doc);

                accountID = doc.createElement("accountID");
                setXmlElement(header, accountID, "4f4faac5-8fd8-4fe5-9df7-445a083afad3", doc);

                serviceName = doc.createElement("serviceName");
                setXmlElement(header, serviceName, tcServiceName, doc);

                digitalSign = doc.createElement("digitalSign");
                setXmlElement(header, digitalSign, tcDigitalSign, doc);

                reqTime = doc.createElement("reqTime");
                setXmlElement(header, reqTime, tcReqTime, doc);

                body = doc.createElement("body");
                setXmlElement(request, body, "", doc);

                cityId = doc.createElement("cityId");
                setXmlElement(body, cityId, tcCityId, doc);

                comeDate = doc.createElement("comeDate");
                setXmlElement(body, comeDate, tcComeDate, doc);

                leaveDate = doc.createElement("leaveDate");
                setXmlElement(body, leaveDate, tcLeaveDate, doc);

                clientIp = doc.createElement("clientIp");
                setXmlElement(body, clientIp, "127.0.0.1", doc);

                if (!"-1".equals(tcStarRatedId)) {
                    starRatedId = doc.createElement("starRatedId");
                    setXmlElement(body, starRatedId, tcStarRatedId, doc);
                }

                doc.appendChild(request);
                return dom2String(doc);
            } else {
                System.out.println("TC DOM XML Failed");
                return null;
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(TCXml.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static String getTCHotelDetail(String tcServiceName, String tcDigitalSign, String tcReqTime, String tchotelId) {
        try {
            Document doc;
            Element request;

            Element header;
            Element version;
            Element accountID;
            Element serviceName;
            Element digitalSign;
            Element reqTime;

            Element body;
            Element hotelId;

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            doc = builder.newDocument();
            if (doc != null) {
                request = doc.createElement("request");

                header = doc.createElement("header");
                setXmlElement(request, header, "", doc);

                version = doc.createElement("version");
                setXmlElement(header, version, "20111128102912", doc);

                accountID = doc.createElement("accountID");
                setXmlElement(header, accountID, "4f4faac5-8fd8-4fe5-9df7-445a083afad3", doc);

                serviceName = doc.createElement("serviceName");
                setXmlElement(header, serviceName, tcServiceName, doc);

                digitalSign = doc.createElement("digitalSign");
                setXmlElement(header, digitalSign, tcDigitalSign, doc);

                reqTime = doc.createElement("reqTime");
                setXmlElement(header, reqTime, tcReqTime, doc);

                body = doc.createElement("body");
                setXmlElement(request, body, "", doc);

                hotelId = doc.createElement("hotelId");
                setXmlElement(body, hotelId, tchotelId, doc);

                doc.appendChild(request);
                return dom2String(doc);
            } else {
                System.out.println("TC DOM XML Failed");
                return null;
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(TCXml.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private static void setXmlElement(Element father, Element child, String childText, Document doc) {
        if (!"".equals(childText)) {
            child.appendChild(doc.createTextNode(childText));
        }
        father.appendChild(child);
    }

    public static String dom2String(Document dom) {
        String resultString = new String();
        try {
            StringWriter stringWriter;
            stringWriter = new StringWriter();
            OutputFormat format = new OutputFormat(dom);
            format.setEncoding("utf-8");
            XMLSerializer serial = new XMLSerializer(stringWriter, format);
            serial.asDOMSerializer();
            serial.serialize(dom.getDocumentElement());
            resultString = stringWriter.toString();
            stringWriter.flush();
            stringWriter.close();
        } catch (Exception e) {
        }
        return resultString;
    }

    public static void output(Node node) {
        TransformerFactory transFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty("encoding", "utf-8");
            transformer.setOutputProperty("indent", "yes");
            DOMSource source = new DOMSource();
            source.setNode(node);
            StreamResult result = new StreamResult();
            result.setOutputStream(System.out);

            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
        } catch (TransformerException e) {
        }
    }

    public static Node selectSingleNode(String express, Object source) {
        Node result = null;
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        try {
            result = (Node) xpath.evaluate(express, source, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
        }

        return result;
    }

    public static NodeList selectNodes(String express, Object source) {
        NodeList result = null;
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        try {
            result = (NodeList) xpath.evaluate(express, source, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
        }

        return result;
    }

    public static void saveXml(String fileName, Document doc) {
        try {

            OutputStream fileoutputStream = new FileOutputStream(fileName);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(fileoutputStream);
            transformer.transform(source, result);

        } catch (TransformerConfigurationException e) {
        } catch (TransformerException e) {
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TCXml.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String tongchengSearch(String content) {
        String url = "http://tcopenapitest.17usoft.com/handlers/hotel/QueryHandler.ashx";
        try {
            return HttpHelper.readContentFromPost(url, content);
        } catch (IOException ex) {
            Logger.getLogger(HttpHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static ArrayList dealWithSearchHotelXml(Document xmldoc, String tcHotelName) {
        ArrayList<Hotel> resultList = new ArrayList();
        Element root;

        root = xmldoc.getDocumentElement();

        String expression = "/response/body/hotelList";
        Element node = (Element) StaticHelper.selectSingleNode(expression, root);

//        StringBuilder resultStringBuilder = new StringBuilder();
        int getItemCounter;
        if (!tcHotelName.equals("")) {
            if (node.hasChildNodes()) {
                getItemCounter = node.getChildNodes().getLength();
            } else {
                getItemCounter = 0;
            }
        } else {
            if (node.hasChildNodes()) {
                getItemCounter = node.getChildNodes().getLength() > 5 ? 5 : node.getChildNodes().getLength();
            } else {
                getItemCounter = 0;
            }
        }
        for (int i = 0; i < getItemCounter; ++i) {
            if (node.getChildNodes().item(i) instanceof Element) {
                Element hotelElement = (Element) node.getChildNodes().item(i);
                String hotelName = hotelElement.getElementsByTagName("hotelName").item(0).getTextContent();
                if (hotelName.contains(tcHotelName)) {
                    String hotelId = hotelElement.getElementsByTagName("hotelId").item(0).getTextContent();
                    String hotelAddress = hotelElement.getElementsByTagName("address").item(0).getTextContent();
                    String hotelCityName = hotelElement.getElementsByTagName("city").item(0).getChildNodes().item(1).getTextContent();
                    Hotel tempHotel = new Hotel(hotelName, hotelCityName, hotelAddress);
                    tempHotel.setSearchingType(3);
                    tempHotel.setHotelTCId(hotelId);

                    resultList.add(tempHotel);
//                    resultStringBuilder.append("").append(hotelName);
//                    resultStringBuilder.append("\n");
                }
            }
        }
        return resultList;
    }

    public static String getCTripCityCode(String cityName) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            Element root;
            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder db = factory.newDocumentBuilder();
            Document xmldoc = db.parse(new File("/Users/bianshujun/NetBeansProjects/HotelWebApplication/src/java/com/staticInfo/TCCityCode.xml"));

            root = xmldoc.getDocumentElement();

            String expression = "/response/cityList/city[name=" + "'" + cityName + "'" + "]";
            Element node = (Element) StaticHelper.selectSingleNode(expression, root);

            String result = node.getElementsByTagName("id").item(0).getTextContent();
            return result;

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(CTripXml.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static HotelDetailInfo dealWithHotelDetailInfoXml(Document xmldoc) {
        HotelDetailInfo resultHotelInfo = new HotelDetailInfo();

        Element root;
        root = xmldoc.getDocumentElement();

        String expression = "/Response/HotelResponse/OTA_HotelDescriptiveInfoRS/HotelDescriptiveContents";
        Element node = (Element) StaticHelper.selectSingleNode(expression, root);
        Element hotelElement = (Element) node.getChildNodes().item(0);
        String hotelName = hotelElement.getAttribute("HotelName");

        Element urlRoot = (Element) hotelElement.getElementsByTagName("MultimediaDescriptions").item(0);
        String urlExpression = "MultimediaDescription/ImageItems/ImageItem/ImageFormat/URL";
        Element urlNode = (Element) StaticHelper.selectSingleNode(urlExpression, urlRoot);
        String imageURL = urlNode.getChildNodes().item(0).getTextContent();

        Element descriptionRoot = (Element) hotelElement.getElementsByTagName("MultimediaDescriptions").item(0);
        String desExpression = "MultimediaDescription/TextItems/TextItem/Description";
        Element desNode = (Element) StaticHelper.selectSingleNode(desExpression, descriptionRoot);
        String textDescription = desNode.getChildNodes().item(0).getTextContent();

//        String imageURL = descripition.getElementsByTagName("URL").item(0).getTextContent();
//        String textDescription = descripition.getElementsByTagName("Description").item(0).getTextContent();

        resultHotelInfo.setHotelName(hotelName);
        resultHotelInfo.setPictureURL(imageURL);
        resultHotelInfo.setDescription(textDescription);

        return resultHotelInfo;
    }
}
