/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webapiapplication;

import com.ctrip.openapi.java.base.HttpAccessAdapter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;

/**
 *
 * @author bianshujun
 */
public class WebAPIApplication {

    /**
     * @param args the command line arguments
     */

    public static void WebAPITest(String[] args) throws IOException {
//        String postContent = DomXmlHelper.getTCXMLDoc("GetHotelList", "ce95f1275db2f3a63debf979b2c00f03", "2014-04-08 18:24:31.912", "45", "2014-04-08", "2014-04-09", "1");
//        HttpHelper.tongchengSearch(postContent);
        try {
            
            /*
            try {
                HttpHelper.readContentFromGet();
            } catch (IOException ex) {
                Logger.getLogger(WebAPIApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
            */
            
            HttpAccessAdapter httpAccessAdapter = new HttpAccessAdapter();
                    
            String request = CTripXml.getcTripXMLDoc();            
            String url = "http://openapi.ctrip.com/Hotel/OTA_Ping.asmx?wsdl";
            String paraName = "requestXML";

//            String hotelRequest = httpAccessAdapter.createHotelRequestXml(2, 0, "如家", true, 2);
            String hotelRequest = CTripXml.getcTripHotelXMLDoc(2, 0, "上海", true, 2);
            String hotelUrl = "http://openapi.ctrip.com/Hotel/OTA_HotelSearch.asmx?wsdl";
            String hotelParaName = "requestXML";

            String hotelInfoRequest = httpAccessAdapter.createHotelInfoRequestXml("625");
            String hotelInfoUrl = "http://openapi.ctrip.com/Hotel/OTA_HotelDescriptiveInfo.asmx?wsdl";
            String hotelInfoParaName = "requestXML";


            String response = httpAccessAdapter.SendRequestToUrl(request, url,
                    paraName);
            
            String hotelResponse = httpAccessAdapter.SendRequestToUrl(hotelRequest,
                    hotelUrl, hotelParaName);
            
            String hotelInfoResponse = httpAccessAdapter.SendRequestToUrl(hotelInfoRequest,
                    hotelInfoUrl, hotelInfoParaName);
            
            System.out.println(hotelResponse);
            
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(WebAPIApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
