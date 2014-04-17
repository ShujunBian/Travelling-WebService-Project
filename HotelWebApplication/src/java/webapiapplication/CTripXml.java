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
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author bianshujun
 */
public class CTripXml {

    public static String getcTripXMLDoc() throws ParserConfigurationException {
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

//        Element root;
        Element Request;
        Element Header;
        Element HotelRequest;
        Element RequestBody;
        Element OTA_PingRQ;
        Element EchoData;

        try {
//            factory.setIgnoringElementContentWhitespace(true);
//            factory.setNamespaceAware(true);

//            DocumentBuilder db = factory.newDocumentBuilder();
//            Document xmldoc = db.parse(new File("/Users/bianshujun/NetBeansProjects/WebAPIApplication/src/com/ctrip/openapi/java/base/RequestSOAPTemplate.xml"));
//            root = xmldoc.getDocumentElement();
//            ((XmlDocument) xmldoc).write(System.out);
//            Document xmldoc = db.parse(new File("/Users/bianshujun/NetBeansProjects/WebAPIApplication/src/com/ctrip/openapi/java/base/RequestSOAPTemplate.txt"));

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document xmldoc = builder.newDocument();
//            NodeList requestXmlNL = xmldoc.getElementsByTagName("requestXML");
//            Element node = (Element) requestXmlNL.item(0);
//            node.getFirstChild().setTextContent("");
            Request = xmldoc.createElement("Request");
            xmldoc.appendChild(Request);

            Header = xmldoc.createElement("Header");
            Header.setAttribute("AllianceID", ConfigData.AllianceId);
            Header.setAttribute("SID", ConfigData.SId);
            long timestamp = SignatureUtils.GetTimeStamp();
            Header.setAttribute("TimeStamp", String.valueOf(timestamp));
            Header.setAttribute("Signature", SignatureUtils.CalculationSignature(timestamp
                    + "", ConfigData.AllianceId, ConfigData.SecretKey, ConfigData.SId, ConfigData.RequestType));
            Header.setAttribute("RequestType", ConfigData.RequestType);
            Header.setAttribute("AsyncRequest", "false");
            Header.setAttribute("Timeout", "0");
            Header.setAttribute("MessagePriority", "3");
            Request.appendChild(Header);

            HotelRequest = xmldoc.createElement("HotelRequest");
            Request.appendChild(HotelRequest);

            RequestBody = xmldoc.createElement("RequestBody");
            Attr attrns = xmldoc.createAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:ns");
            attrns.setValue("http://www.opentravel.org/OTA/2003/05");
            attrns.setPrefix("xmlns");
            RequestBody.setAttributeNode(attrns);

            Attr attrxsi = xmldoc.createAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi");
            attrxsi.setValue("http://www.w3.org/2001/XMLSchema-instance");
            attrxsi.setPrefix("xmlns");
            RequestBody.setAttributeNode(attrxsi);

            Attr attrxsd = xmldoc.createAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsd");
            attrxsd.setValue("http://www.w3.org/2001/XMLSchema");
            attrxsd.setPrefix("xmlns");
            RequestBody.setAttributeNode(attrxsd);
            HotelRequest.appendChild(RequestBody);

            OTA_PingRQ = xmldoc.createElementNS("http://", "ns:OTA_PingRQ");
            OTA_PingRQ.setPrefix("ns");
            RequestBody.appendChild(OTA_PingRQ);

            EchoData = xmldoc.createElementNS("http://", "ns:EchoData");
            EchoData.setPrefix("ns");
            OTA_PingRQ.appendChild(EchoData);
            EchoData.setTextContent("联通测试");

            return removeXMLHeader(xmldoc);

//            requestXML = (Element) selectSingleNode("requestXML", root);
//            output(requestXML);

//            requestXML.setAttribute("id", "B01");
//            output(requestXML);

//            saveXml("/Users/bianshujun/NetBeansProjects/WebAPIApplication/src/com/ctrip/openapi/java/base/RequestSOAPTemplate.xml", xmldoc);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return null;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(TCXml.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(TCXml.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static String getcTripHotelXMLDoc(int hotelCityCode, int AreaID, String hotelName,
            boolean isHotelStarRate, int rating) throws ParserConfigurationException {

        Element Request;
        Element Header;
        Element HotelRequest;
        Element RequestBody;
        Element HotelSearchRQ;
        Element Criteria;
        Element Criterion;
        Element HotelRef;
        Element Award;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document xmldoc = builder.newDocument();

            Request = xmldoc.createElement("Request");
            xmldoc.appendChild(Request);

            Header = xmldoc.createElement("Header");
            Header.setAttribute("AllianceID", ConfigData.AllianceId);
            Header.setAttribute("SID", ConfigData.SId);
            long timestamp = SignatureUtils.GetTimeStamp();
            Header.setAttribute("TimeStamp", String.valueOf(timestamp));
            Header.setAttribute("Signature", SignatureUtils.CalculationSignature(timestamp
                    + "", ConfigData.AllianceId, ConfigData.SecretKey, ConfigData.SId, ConfigData.hotelRequestType));
            Header.setAttribute("RequestType", ConfigData.hotelRequestType);
            Header.setAttribute("AsyncRequest", "false");
            Header.setAttribute("Timeout", "0");
            Header.setAttribute("MessagePriority", "3");
            Request.appendChild(Header);

            HotelRequest = xmldoc.createElement("HotelRequest");
            Request.appendChild(HotelRequest);

            RequestBody = xmldoc.createElement("RequestBody");
            Attr attrns = xmldoc.createAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:ns");
            attrns.setValue("http://www.opentravel.org/OTA/2003/05");
            attrns.setPrefix("xmlns");
            RequestBody.setAttributeNode(attrns);

            Attr attrxsi = xmldoc.createAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi");
            attrxsi.setValue("http://www.w3.org/2001/XMLSchema-instance");
            attrxsi.setPrefix("xmlns");
            RequestBody.setAttributeNode(attrxsi);

            Attr attrxsd = xmldoc.createAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsd");
            attrxsd.setValue("http://www.w3.org/2001/XMLSchema");
            attrxsd.setPrefix("xmlns");
            RequestBody.setAttributeNode(attrxsd);
            HotelRequest.appendChild(RequestBody);

            HotelSearchRQ = xmldoc.createElementNS("http://", "ns:OTA_HotelSearchRQ");
            HotelSearchRQ.setPrefix("ns");

            Attr attrVersion = xmldoc.createAttributeNS("", "Version");
            attrVersion.setValue("0.0");
            HotelSearchRQ.setAttributeNode(attrVersion);

            Attr attrPriL = xmldoc.createAttributeNS("", "PrimaryLangID");
            attrPriL.setValue("zh");
            HotelSearchRQ.setAttributeNode(attrPriL);

            Attr attrschemaL = xmldoc.createAttributeNS("http://", "xsi:schemaLocation");
            attrschemaL.setValue("http://www.opentravel.org/OTA/2003/05 OTA_HotelSearchRQ.xsd");
            attrschemaL.setPrefix("xsi");
            HotelSearchRQ.setAttributeNode(attrschemaL);

            HotelSearchRQ.setAttribute("xmlns", "http://www.opentravel.org/OTA/2003/05");

            RequestBody.appendChild(HotelSearchRQ);

            Criteria = xmldoc.createElementNS("http://", "ns:Criteria");
            Criteria.setPrefix("ns");
            HotelSearchRQ.appendChild(Criteria);

            Criterion = xmldoc.createElementNS("http://", "ns:Criterion");
            Criterion.setPrefix("ns");
            Criteria.appendChild(Criterion);

            HotelRef = xmldoc.createElementNS("http://", "ns:HotelRef");
            HotelRef.setPrefix("ns");
            HotelRef.setAttribute("HotelCityCode", String.valueOf(hotelCityCode));
            if (AreaID != -1) {
                HotelRef.setAttribute("AreaID", String.valueOf(AreaID));
            }
            HotelRef.setAttribute("HotelName", hotelName);
            Criterion.appendChild(HotelRef);

            if (rating != -1) {
                Award = xmldoc.createElementNS("http://", "ns:Award");
                Award.setPrefix("ns");
                if (isHotelStarRate) {
                    Award.setAttribute("Provider", "HotelStarRate");
                } else {
                    Award.setAttribute("Provider", "CtripStarRate");
                }
                Award.setAttribute("Rating", String.valueOf(rating));
                Criterion.appendChild(Award);
            }

            return removeXMLHeader(xmldoc);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            return null;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(TCXml.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(TCXml.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static String getcTripHotelInfoXMLDoc(String hotelCode) throws ParserConfigurationException {

        Element Request;
        Element Header;
        Element HotelRequest;
        Element RequestBody;
        Element HotelDescription;
        Element HotelDescriptiveInfos;
        Element HotelDescriptiveInfo;
        Element HotelInfo;
        Element FacilityInfo;
        Element AreaInfo;
        Element MultimediaObjects;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document xmldoc = builder.newDocument();

            Request = xmldoc.createElement("Request");
            xmldoc.appendChild(Request);

            Header = xmldoc.createElement("Header");
            Header.setAttribute("AllianceID", ConfigData.AllianceId);
            Header.setAttribute("SID", ConfigData.SId);
            long timestamp = SignatureUtils.GetTimeStamp();
            Header.setAttribute("TimeStamp", String.valueOf(timestamp));
            Header.setAttribute("Signature", SignatureUtils.CalculationSignature(timestamp
                    + "", ConfigData.AllianceId, ConfigData.SecretKey, ConfigData.SId, ConfigData.hotelInfoRequestType));
            Header.setAttribute("RequestType", ConfigData.hotelInfoRequestType);
            Header.setAttribute("AsyncRequest", "false");
            Header.setAttribute("Timeout", "0");
            Header.setAttribute("MessagePriority", "3");
            Request.appendChild(Header);

            HotelRequest = xmldoc.createElement("HotelRequest");
            Request.appendChild(HotelRequest);

            RequestBody = xmldoc.createElement("RequestBody");
            Attr attrns = xmldoc.createAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:ns");
            attrns.setValue("http://www.opentravel.org/OTA/2003/05");
            attrns.setPrefix("xmlns");
            RequestBody.setAttributeNode(attrns);

            Attr attrxsi = xmldoc.createAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi");
            attrxsi.setValue("http://www.w3.org/2001/XMLSchema-instance");
            attrxsi.setPrefix("xmlns");
            RequestBody.setAttributeNode(attrxsi);

            Attr attrxsd = xmldoc.createAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsd");
            attrxsd.setValue("http://www.w3.org/2001/XMLSchema");
            attrxsd.setPrefix("xmlns");
            RequestBody.setAttributeNode(attrxsd);
            HotelRequest.appendChild(RequestBody);

            HotelDescription = xmldoc.createElement("OTA_HotelDescriptiveInfoRQ");
            HotelDescription.setAttribute("Version", "1.0");

            Attr attrSL = xmldoc.createAttributeNS("http://", "xsi:schemaLocation");
            attrSL.setPrefix("xsi");
            attrSL.setValue("http://www.opentravel.org/OTA/2003/05 OTA_HotelDescriptiveInfoRQ.xsd");
            HotelDescription.setAttributeNode(attrSL);

            HotelDescription.setAttribute("xmlns", "http://www.opentravel.org/OTA/2003/05");
            Attr attrXIS = xmldoc.createAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi");
            attrXIS.setPrefix("xmlns");
            attrXIS.setValue("http://www.w3.org/2001/XMLSchema-instance");
            HotelDescription.setAttributeNode(attrXIS);
            RequestBody.appendChild(HotelDescription);

            HotelDescriptiveInfos = xmldoc.createElement("HotelDescriptiveInfos");
            HotelDescription.appendChild(HotelDescriptiveInfos);

            HotelDescriptiveInfo = xmldoc.createElement("HotelDescriptiveInfo");
            HotelDescriptiveInfo.setAttribute("HotelCode", hotelCode);
            HotelDescriptiveInfos.appendChild(HotelDescriptiveInfo);

            HotelInfo = xmldoc.createElement("HotelInfo");
            HotelInfo.setAttribute("SendData", "true");
            HotelDescriptiveInfo.appendChild(HotelInfo);

            FacilityInfo = xmldoc.createElement("FacilityInfo");
            FacilityInfo.setAttribute("SendGuestRooms", "true");
            HotelDescriptiveInfo.appendChild(FacilityInfo);

            AreaInfo = xmldoc.createElement("AreaInfo");
            AreaInfo.setAttribute("SendAttractions", "true");
            AreaInfo.setAttribute("SendRecreations", "true");
            HotelDescriptiveInfo.appendChild(AreaInfo);

            MultimediaObjects = xmldoc.createElement("MultimediaObjects");
            MultimediaObjects.setAttribute("SendData", "true");
            HotelDescriptiveInfo.appendChild(MultimediaObjects);

            return removeXMLHeader(xmldoc);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            return null;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(TCXml.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(TCXml.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static String removeXMLHeader(Document dom) {
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

        return resultString.substring(resultString.indexOf('>') + 2, resultString.length());
    }

    public static String getCTripCityCode(String cityName) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            Element root;
            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder db = factory.newDocumentBuilder();
//            Document xmldoc = db.parse(new File("/Users/bianshujun/NetBeansProjects/WebAPIApplication/src/com/ctrip/openapi/java/base/RequestSOAPTemplate.xml"));
            Document xmldoc = db.parse(new File("/Users/bianshujun/Downloads/Projects/Travelling-WebService-Project/HotelWebApplication/src/java/com/staticInfo/CTripCityCode.xml"));

            root = xmldoc.getDocumentElement();

            String expression = "/CityDetails/CityDetail[CityName=" + "'" + cityName + "'" + "]";
            Element node = (Element) StaticHelper.selectSingleNode(expression, root);
            String result = node.getChildNodes().item(3).getTextContent();
            return result;

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(CTripXml.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String getCTripRegionName(String regionName) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            Element root;
            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder db = factory.newDocumentBuilder();
            Document xmldoc = db.parse(new File("/Users/bianshujun/Downloads/Projects/Travelling-WebService-Project/HotelWebApplication/src/java/com/staticInfo/CTripRegion.xml"));

            root = xmldoc.getDocumentElement();

            String expression = "/LocationDetails/LocationDetail[LocationName=" + "'" + regionName + "'" + "]";
            Element node = (Element) StaticHelper.selectSingleNode(expression, root);
            String result = node.getChildNodes().item(1).getTextContent();
            return result;

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(CTripXml.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static ArrayList dealWithSearchHotelXml(Document xmldoc) {
        ArrayList<Hotel> tempHotelList = new ArrayList();

        Element root;
        root = xmldoc.getDocumentElement();

        String expression = "/Response/HotelResponse/OTA_HotelSearchRS/Properties";
        Element node = (Element) StaticHelper.selectSingleNode(expression, root);

//        StringBuilder resultStringBuilder = new StringBuilder();
        int getItemCounter = node.getChildNodes().getLength() > 5 ? 5 : node.getChildNodes().getLength();
        for (int i = 0; i < getItemCounter; ++i) {
            if (node.getChildNodes().item(i) instanceof Element) {
                Element hotelElement = (Element) node.getChildNodes().item(i);

                String hotelName = hotelElement.getAttribute("HotelName");
                String hotelAddress = hotelElement.getElementsByTagName("AddressLine").item(0).getTextContent();
                String hotelCity = hotelElement.getElementsByTagName("CityName").item(0).getTextContent();
                String hotelId = hotelElement.getAttribute("HotelId");
                Hotel tempHotel = new Hotel(hotelName, hotelCity, hotelAddress);
                tempHotel.setSearchingType(0);
                tempHotel.setHotelCtripId(hotelId);
                tempHotelList.add(tempHotel);
            }
        }
        return tempHotelList;
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
