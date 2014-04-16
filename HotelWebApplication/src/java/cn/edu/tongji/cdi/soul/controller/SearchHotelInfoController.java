package cn.edu.tongji.cdi.soul.controller;

import cn.edu.tongji.cdi.soul.domain.HotelDetailInfo;
import com.ctrip.openapi.java.base.HttpAccessAdapter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import webapiapplication.CTripXml;
import webapiapplication.DZDPXml;
import webapiapplication.HttpHelper;
import webapiapplication.StaticHelper;
import webapiapplication.TCXml;
import webapiapplication.TongChengDigitalSign;

@Controller
@RequestMapping("webService/getHotelInfo")
public class SearchHotelInfoController {

    @RequestMapping("/test")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("message",
                "Hello,This is a example of Spring3 RESTful!");
        return "test";
    }

    @RequestMapping(value = "CTrip/{hotelCode}", method = RequestMethod.GET)
    @ResponseBody
    public HotelDetailInfo CTrip(@PathVariable String hotelCode, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        HotelDetailInfo resultHotelDetail = new HotelDetailInfo();
        try {
            String hotelInfoRequest = CTripXml.getcTripHotelInfoXMLDoc(hotelCode);
            String hotelInfoUrl = "http://openapi.ctrip.com/Hotel/OTA_HotelDescriptiveInfo.asmx?wsdl";
            String hotelInfoParaName = "requestXML";

            String hotelInfoResponse = HttpAccessAdapter.SendRequestToUrl(hotelInfoRequest,
                    hotelInfoUrl, hotelInfoParaName);

            resultHotelDetail = CTripXml.dealWithHotelDetailInfoXml(StaticHelper.getDocumentByString(hotelInfoResponse));

//            response.setContentType("text/xml;charset=utf-8");
//            response.getWriter().write(hotelInfoResponse);

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(SearchHotelInfoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultHotelDetail;
    }

    @RequestMapping(value = "TC/{tchotelId}", method = RequestMethod.GET)
    public String TC(@PathVariable String tchotelId, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String currentTime = new java.sql.Timestamp(System.currentTimeMillis()).toString();
        String digitalSign = TongChengDigitalSign.getContent(currentTime, "GetHotelDetail");
        String postContent = TCXml.getTCHotelDetail("GetHotelDetail", digitalSign, currentTime, tchotelId);

        String result = TCXml.tongchengSearch(postContent);
        response.setContentType("text/xml;charset=utf-8");
        response.getWriter().write(postContent);

        return null;
    }

    @RequestMapping(value = "DZDP/{business_id}", method = RequestMethod.GET)
    @ResponseBody
    public HotelDetailInfo DZDP(@PathVariable String business_id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HotelDetailInfo resultHotelDetail = new HotelDetailInfo();

        String url = DZDPXml.getDetailInfo(business_id);
        String result = HttpHelper.readContentFromGet(url);
        resultHotelDetail = DZDPXml.dealWithHotelDetailInfoXml(StaticHelper.getDocumentByString(result));

//        response.setContentType("text/xml;charset=utf-8");
//        response.getWriter().write(result);

        return resultHotelDetail;
//        return null;
    }
}
