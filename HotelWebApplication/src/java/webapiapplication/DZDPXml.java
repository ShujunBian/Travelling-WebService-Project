/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webapiapplication;

import cn.edu.tongji.cdi.soul.domain.Hotel;
import cn.edu.tongji.cdi.soul.domain.HotelDetailInfo;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author bianshujun
 */
public class DZDPXml {

    /**
     * getDZDPSign() 酒店查询
     *
     * @param cityName 城市名字
     * @param AreaID 行政区ID
     * @param hotelName 酒店名称
     * @param isHotelStarRate 选择酒店评级标准,true为星级,false为携程星级,
     * @param rating 星级等级
     * @return
     */
    public static String getDZDPURL(String cityName, String latitude, String longitude, String category,
            String region, String keyword, int radius, int sortType, boolean has_deal, boolean has_coupon) throws IOException {

        String appkey = "575076720";
        String secret = "7cd49bf301764a6bb191e9a972cf482c";
        String apiUrl = "http://api.dianping.com/v1/business/find_businesses";

        String utfCityName = URLEncoder.encode(cityName, "utf-8");
        String utfRegionName = URLEncoder.encode(region, "utf-8");
        String utfKeyword = URLEncoder.encode(keyword, "utf-8");
        String utfCategory = URLEncoder.encode(category, "utf-8");

        Map<String, String> paramMap = new HashMap<>();

        if (!"-1".equals(cityName)) {
            paramMap.put("city", cityName);
        }
        if (!"-1".equals(latitude) || !"-1".equals(longitude)) {
            paramMap.put("latitude", latitude);
            paramMap.put("longitude", longitude);
        }
        if (!"-1".equals(category)) {
            paramMap.put("category", category);
        }
        if (!"-1".equals(region)) {
            paramMap.put("region", region);
        }
        if (!"-1".equals(keyword)) {
            paramMap.put("keyword", keyword);
        }
        if (radius != -1) {
            paramMap.put("radius", String.valueOf(radius));
        }
        paramMap.put("limit", "20");
        if (has_deal) {
            paramMap.put("has_deal", "1");

        }
        if (has_coupon) {
            paramMap.put("has_coupon", "1");
        }
        paramMap.put("sort", String.valueOf(sortType));
        paramMap.put("format", "xml");


        String[] keyArray = paramMap.keySet().toArray(new String[0]);
        Arrays.sort(keyArray);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(appkey);
        for (String key : keyArray) {
            stringBuilder.append(key).append(paramMap.get(key));
        }

        stringBuilder.append(secret);
        String codes = stringBuilder.toString();

        String sign = org.apache.commons.codec.digest.DigestUtils.shaHex(codes).toUpperCase();

        StringBuilder urlString = new StringBuilder();
        urlString.append(apiUrl).append("?appkey=").append(appkey)
                .append("&sign=").append(sign);

        if (!"-1".equals(category)) {
            urlString.append("&category=").append(utfCategory);
        }

        if (!"-1".equals(cityName)) {
            urlString.append("&city=").append(utfCityName);
        }

        if (!"-1".equals(region)) {
            urlString.append("&region=").append(utfRegionName);
        }

        if (!"-1".equals(latitude) || !"-1".equals(longitude)) {
            urlString.append("&latitude=").append(latitude);
            urlString.append("&longitude=").append(longitude);
        }

        if (!"-1".equals(keyword)) {
            urlString.append("&keyword=").append(utfKeyword);
        }

        if (radius != -1) {
            urlString.append("&radius=").append(String.valueOf(radius));
        }

        urlString.append("&sort=").append(String.valueOf(sortType))
                .append("&limit=20&format=xml");
        if (has_deal) {
            urlString.append("&has_deal=1");
        }
        if (has_coupon) {
            urlString.append("&has_coupon=1");

        }

        return urlString.toString();
    }

    public static String getDetailInfo(String business_id) {

        String appkey = "575076720";
        String secret = "7cd49bf301764a6bb191e9a972cf482c";
        String apiUrl = "http://api.dianping.com/v1/business/get_single_business";

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("business_id", business_id);
        paramMap.put("format", "xml");

        String[] keyArray = paramMap.keySet().toArray(new String[0]);
        Arrays.sort(keyArray);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(appkey);
        for (String key : keyArray) {
            stringBuilder.append(key).append(paramMap.get(key));
        }
        stringBuilder.append(secret);
        String codes = stringBuilder.toString();
        String sign = org.apache.commons.codec.digest.DigestUtils.shaHex(codes).toUpperCase();


        StringBuilder urlString = new StringBuilder();
        urlString.append(apiUrl).append("?appkey=").append(appkey)
                .append("&sign=").append(sign);

        urlString.append("&business_id=").append(business_id);
        urlString.append("&format=xml");
        return urlString.toString();
    }

    public static String getDZDPRegionName(String cityName) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            Element root;
            factory.setIgnoringElementContentWhitespace(true);

            DocumentBuilder db = factory.newDocumentBuilder();
            Document xmldoc = db.parse(new File("/Users/bianshujun/NetBeansProjects/HotelWebApplication/src/java/com/staticInfo/DZDPRegion.xml"));

            root = xmldoc.getDocumentElement();

            String expression = "/result/cities/city[name=" + "'" + cityName + "'" + "]";
            Element node = (Element) StaticHelper.selectSingleNode(expression, root);
            String result = node.getChildNodes().item(1).getTextContent();
            return result;

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(CTripXml.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static ArrayList dealWithSearchHotelXml(Document xmldoc) {
        ArrayList<Hotel> resultList = new ArrayList();
        Element root;

        root = xmldoc.getDocumentElement();

        String expression = "/result/businesses";
        Element node = (Element) StaticHelper.selectSingleNode(expression, root);

//        StringBuilder resultStringBuilder = new StringBuilder();
        int getItemCounter = node.getChildNodes().getLength() > 10 ? 10 : node.getChildNodes().getLength();
        for (int i = 0; i < getItemCounter; ++i) {
            if (node.getChildNodes().item(i) instanceof Element) {
                Element hotelElement = (Element) node.getChildNodes().item(i);
                String hotelName = (hotelElement.getElementsByTagName("name").item(0).getTextContent());
                String hotelId = hotelElement.getElementsByTagName("business_id").item(0).getTextContent();
                String hotelAddress = hotelElement.getElementsByTagName("address").item(0).getTextContent();
                String hotelCityName = hotelElement.getElementsByTagName("city").item(0).getTextContent();

                Hotel tempHotel = new Hotel(hotelName.substring(0, hotelName.indexOf('(')), hotelCityName, hotelAddress);
                tempHotel.setSearchingType(2);
                tempHotel.setHotelDZDPId(hotelId);

                resultList.add(tempHotel);
//                resultStringBuilder.append("").append(hotelName.substring(0, hotelName.indexOf('('))).append("   ").append(hotelAddress);
//                resultStringBuilder.append("\n");
            }
        }
        return resultList;
    }

    public static HotelDetailInfo dealWithHotelDetailInfoXml(Document xmldoc) {
        HotelDetailInfo resultHotelInfo = new HotelDetailInfo();

        Element root;
        root = xmldoc.getDocumentElement();

        String expression = "/result/businesses";

        Element node = (Element) StaticHelper.selectSingleNode(expression, root);
        int getItemCounter = node.getChildNodes().getLength();
        for (int i = 0; i < getItemCounter; ++i) {
            if (node.getChildNodes().item(i) instanceof Element) {
                Element hotelElement = (Element) node.getChildNodes().item(i);

                String tempHotelName = (hotelElement.getElementsByTagName("name").item(0).getTextContent());
                String hotelName = tempHotelName.substring(0, tempHotelName.indexOf('('));

                String imageURL = hotelElement.getElementsByTagName("photo_url").item(0).getTextContent();

                String desExpression = "deals/deal/description";
                Element desNode = (Element) StaticHelper.selectSingleNode(desExpression, hotelElement);
                String textDescription = desNode.getChildNodes().item(0).getTextContent();

//        String imageURL = descripition.getElementsByTagName("URL").item(0).getTextContent();
//        String textDescription = descripition.getElementsByTagName("Description").item(0).getTextContent();

                resultHotelInfo.setHotelName(hotelName);
                resultHotelInfo.setPictureURL(imageURL);
                resultHotelInfo.setDescription(textDescription);
            }
        }
        return resultHotelInfo;
    }
}
