/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webapiapplication;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author bianshujun
 */
public class HotwireXml {

    public static String getHotwireURL(String cityName, String distance, String starrating, String limit, String sort) {
        final String appKey = "dhnat67g4d3pecz2anpcg5zm";
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://api.hotwire.com/v1/deal/hotel?");
        urlString.append("dest=").append(cityName);
        urlString.append("&apikey=").append(appKey);

        if (!"-1".equals(distance)) {
            urlString.append("&distance=").append(distance);
        }

        if (!"-1".equals(starrating)) {
            urlString.append("&starrating=").append(starrating);
        }

        if (!"-1".equals(limit)) {
            urlString.append("&limit=").append(limit);
        }

        if (!"-1".equals(sort)) {
            urlString.append("&sort=").append(sort);
        }

        return urlString.toString();
    }

    public static String dealWithSearchHotelXml(Document xmldoc) {
        Element root;

        root = xmldoc.getDocumentElement();

        String expression = "/Hotwire/Result";
        Element node = (Element) StaticHelper.selectSingleNode(expression, root);

        StringBuilder resultStringBuilder = new StringBuilder();
        int getItemCounter = node.getChildNodes().getLength() > 1 ? 1 : node.getChildNodes().getLength();
        for (int i = 0; i < getItemCounter; ++i) {
            if (node.getChildNodes().item(i) instanceof Element) {
                Element hotelElement = (Element) node.getChildNodes().item(i);

                resultStringBuilder.append("").append(hotelElement.getChildNodes().item(4)
                        .getTextContent());

                resultStringBuilder.append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
