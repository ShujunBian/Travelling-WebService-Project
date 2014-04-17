package cn.edu.tongji.cdi.soul.controller;

import cn.edu.tongji.cdi.soul.domain.Hotel;
import com.ctrip.openapi.java.base.HttpAccessAdapter;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import webapiapplication.HotwireXml;
import webapiapplication.HttpHelper;
import webapiapplication.StaticHelper;
import webapiapplication.TCXml;
import webapiapplication.TongChengDigitalSign;

@Controller
@RequestMapping("webService/getHotel")
public class SearchHotelController {

    @RequestMapping("/test")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("message",
                "Hello,This is a example of Spring3 RESTful!");
        return "test";
    }

    @RequestMapping(value = "CTrip/{hotelCityCode}/{AreaID}/{hotelName}/{isHotelStarRate}/{rating}", method = RequestMethod.GET)
    public String CTrip(@PathVariable int hotelCityCode, @PathVariable int AreaID, @PathVariable String hotelName,
            @PathVariable int isHotelStarRate, @PathVariable int rating, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            boolean tIsHotelStarRate = isHotelStarRate == 0 ? true : false;
            String hotelRequest = CTripXml.getcTripHotelXMLDoc(hotelCityCode, AreaID, hotelName, tIsHotelStarRate, rating);
            String hotelUrl = "http://openapi.ctrip.com/Hotel/OTA_HotelSearch.asmx?wsdl";
            String hotelParaName = "requestXML";
            String hotelResponse = HttpAccessAdapter.SendRequestToUrl(hotelRequest, hotelUrl, hotelParaName);

            response.setContentType("text/xml;charset=utf-8");
            response.getWriter().write(hotelResponse);

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(SearchHotelController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @RequestMapping(value = "TC/{tcCityId}/{tcComeDate}/{tcLeaveDate}/{tcStarRatedId}/{tcKeyword}", method = RequestMethod.GET)
    public String TC(@PathVariable String tcCityId, @PathVariable String tcComeDate,
            @PathVariable String tcLeaveDate, @PathVariable String tcStarRatedId, @PathVariable String tcKeyword,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        String currentTime = new java.sql.Timestamp(System.currentTimeMillis()).toString();
        String digitalSign = TongChengDigitalSign.getContent(currentTime, "GetHotelList");
        String postContent = TCXml.getTCXMLDoc("GetHotelList", digitalSign, currentTime, tcCityId,
                tcComeDate, tcLeaveDate, tcStarRatedId);

        response.setContentType("text/xml;charset=utf-8");
        response.getWriter().write(TCXml.tongchengSearch(postContent));

        return null;
    }

    @RequestMapping(value = "DZDP/{cityName}/{latitude}/{longitude}/{category}/{region}/{keyword}/{radius}/{sortType}", method = RequestMethod.GET)
    @ResponseBody
    public ArrayList<Hotel> DZDP(@PathVariable String cityName, @PathVariable String latitude, @PathVariable String longitude,
            @PathVariable String category, @PathVariable String region, @PathVariable String keyword, @PathVariable int radius, @PathVariable int sortType,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = DZDPXml.getDZDPURL(cityName, latitude, longitude, category, region, keyword, radius, sortType, true, false);

        String result = HttpHelper.readContentFromGet(url);
        ArrayList<Hotel> DZDPList = DZDPXml.dealWithSearchHotelXml(StaticHelper.getDocumentByString(result));

//        response.setContentType("text/xml;charset=utf-8");
//        response.getWriter().write(result);

        return DZDPList;
    }

    @RequestMapping(value = "HotwireURL/{cityName}/{distance}/{starrating}/{limit}/{sort}", method = RequestMethod.GET)
    public String HotwireURL(@PathVariable String cityName, @PathVariable String distance,
            @PathVariable String starrating, @PathVariable String limit, @PathVariable String sort,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        String url = HotwireXml.getHotwireURL(cityName, distance, starrating, limit, sort);

        String result = HttpHelper.readContentFromGet(url);
        response.setContentType("text/xml;charset=utf-8");
        response.getWriter().write(result);

        return null;
    }

    @RequestMapping(value = "All/{cityName}/{AreaName}/{hotelName}/{rating}/{keyword}/{isSpecialHotel}", method = RequestMethod.GET)
    @ResponseBody
    public ArrayList All(@PathVariable String cityName, @PathVariable String AreaName, @PathVariable String hotelName,
            @PathVariable int rating, @PathVariable String keyword, @PathVariable String isSpecialHotel,
            HttpServletRequest request, HttpServletResponse response) throws IOException {

        ArrayList<Hotel> responseList = new ArrayList();


//        StringBuilder resultString = new StringBuilder();
        String utfCityName = URLEncoder.encode(cityName, "utf-8");
        String chineseCityName = URLDecoder.decode(utfCityName, "utf-8");

        response.setContentType("text;charset=utf-8");
        //携程

        try {
            String cityCode = CTripXml.getCTripCityCode(chineseCityName);
            String utfRegionName;
            String chineseRegionName;
            String regionCode = AreaName;
            if (!"-1".equals(AreaName)) {
                utfRegionName = URLEncoder.encode(AreaName, "utf-8");
                chineseRegionName = URLDecoder.decode(utfRegionName, "utf-8");
                regionCode = CTripXml.getCTripRegionName(chineseRegionName);
            }

            String cTripHotelRequest = CTripXml.getcTripHotelXMLDoc(Integer.valueOf(cityCode).intValue(),
                    Integer.valueOf(regionCode).intValue(), hotelName, true, rating);
            String cTripHotelUrl = "http://openapi.ctrip.com/Hotel/OTA_HotelSearch.asmx?wsdl";
            String cTripHotelParaName = "requestXML";
            String cTripHotelResponse = HttpAccessAdapter.SendRequestToUrl(cTripHotelRequest, cTripHotelUrl, cTripHotelParaName);

            ArrayList<Hotel> CTripList = CTripXml.dealWithSearchHotelXml(StaticHelper.getDocumentByString(cTripHotelResponse));
            for (int i = 0; i < CTripList.size(); i++) {
                responseList.add(CTripList.get(i));
            }

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(SearchHotelController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //hotWire

//        String hotWireurl = HotwireXml.getHotwireURL("Shanghai", "-1", String.valueOf(rating), "5", "-1");
//
//        String hotWireResponse = HttpHelper.readContentFromGet(hotWireurl);
//        String hotWireresult = HotwireXml.dealWithSearchHotelXml(StaticHelper.getDocumentByString(hotWireResponse));
//        resultString.append("\n").append(hotWireresult);

        //大众点评
        if (rating == -1) {
            String dzdpUrl = DZDPXml.getDZDPURL(chineseCityName, "-1", "-1", "酒店", AreaName, hotelName, -1, 1, true, false);
            String dzdpresponse = HttpHelper.readContentFromGet(dzdpUrl);

//        String dzdpResult = DZDPXml.dealWithSearchHotelXml(StaticHelper.getDocumentByString(dzdpresponse));
            ArrayList<Hotel> DZDPList = DZDPXml.dealWithSearchHotelXml(StaticHelper.getDocumentByString(dzdpresponse));
            for (int i = 0; i < DZDPList.size(); i++) {
                responseList.add(DZDPList.get(i));
            }
//        resultString.append("\n").append(dzdpResult);

            //ͬ同程
            if ("-1".equals(hotelName)) {
                Date date = new Date();
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);
                calendar.add(Calendar.DATE, 1);
                date = calendar.getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String nextDateString = formatter.format(date);

                calendar.add(Calendar.DATE, -1);
                date = calendar.getTime();
                String currentDateString = formatter.format(date);

                String tcCityId = TCXml.getCTripCityCode(chineseCityName);
                String currentTime = new java.sql.Timestamp(System.currentTimeMillis()).toString();
                String digitalSign = TongChengDigitalSign.getContent(currentTime, "GetHotelList");
                String postContent = TCXml.getTCXMLDoc("GetHotelList", digitalSign, currentTime, tcCityId,
                        currentDateString, nextDateString, String.valueOf(rating));
                String specialHotelName;
                if ("1".equals(isSpecialHotel)) {
                    specialHotelName = hotelName;
                } else {
                    specialHotelName = "-1";
                }
                ArrayList<Hotel> TCList = TCXml.dealWithSearchHotelXml(StaticHelper.getDocumentByString(
                        HttpHelper.readContentFromPost("http://tcopenapitest.17usoft.com/handlers/hotel/QueryHandler.ashx", postContent)),
                        specialHotelName);
//            response.getWriter().write(
//                    HttpHelper.readContentFromPost("http://tcopenapitest.17usoft.com/handlers/hotel/QueryHandler.ashx", postContent));
                for (int i = 0; i < TCList.size(); i++) {
                    responseList.add(TCList.get(i));
                }
            }
        }
//        return null;
        return responseList;

    }
}
