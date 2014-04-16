package com.ctrip.openapi.java.base;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import com.ctrip.openapi.java.utils.ConfigData;
import com.ctrip.openapi.java.utils.SignatureUtils;
import java.io.ByteArrayOutputStream;

// Http访问器
public class HttpAccessAdapter {
    // Http请求静态信息

    private static ArrayList<HttpRequestProperty> StaticHttpRequestProperty = LoadStaticHttpRequestProperties();

    // 发送指定的请求到指定的URL上
    public static String SendRequestToUrl(String requestContent,
            String urlString, String paraName) {
        InputStream errorStream = null;
        HttpURLConnection httpCon = null;
        try {
            URL url = new URL(urlString);
            String content = XMLToString(requestContent);
            String soapMessage = AddSoapShell(content);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestMethod("POST");
            httpCon.setDoOutput(true);
            httpCon.setDoInput(true);

            for (int i = 0; i < StaticHttpRequestProperty.size(); i++) {
                httpCon
                        .setRequestProperty(StaticHttpRequestProperty.get(i)
                        .getName(), StaticHttpRequestProperty.get(i)
                        .getValue());
            }

            httpCon.setRequestProperty("Content-Length", String
                    .valueOf(soapMessage.length()));

            OutputStream outputStream = httpCon.getOutputStream();
            outputStream.write(soapMessage.getBytes("UTF-8"));
            outputStream.close();

            InputStream inputStream = httpCon.getInputStream();
            // String encoding = httpCon.getRequestProperty("Accept-Encoding");
            BufferedReader br = null;
            // httpCon.getResponseMessage();
            if (httpCon.getRequestProperty("Accept-Encoding") != null) {
                try {
                    GZIPInputStream gzipStream = new GZIPInputStream(
                            inputStream);
                    br = new BufferedReader(new InputStreamReader(gzipStream,
                            "UTF-8"));
                } catch (IOException e) {
                    br = new BufferedReader(new InputStreamReader(inputStream,
                            "UTF-8"));
                }
            } else {
                br = new BufferedReader(new InputStreamReader(inputStream,
                        "UTF-8"));
            }
            StringBuffer result = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            return StringToXML(RemoveSoapShell(result.toString()));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            errorStream = httpCon.getErrorStream();
            if (errorStream != null) {
                String errorMessage = null;
                String line = null;
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        errorStream));
                try {
                    while ((line = br.readLine()) != null) {
                        errorMessage += line;
                    }
                    return errorMessage;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
            try {
                errorStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpCon != null) {
                httpCon.disconnect();
            }
        }

        return "";
    }

    // 将Soap外壳添加到请求体上
    private static String AddSoapShell(
            /* String parameterName, */String patameterValue) throws Exception {
        BufferedReader bufferedReader = null;
        try {
            InputStream in = HttpAccessAdapter.class
                    .getResourceAsStream("RequestSOAPTemplate.xml");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(in));
            String text = bufferedReader.readLine();
            StringBuilder soapShellStringBuilder = new StringBuilder();
            while (text != null) {
                soapShellStringBuilder.append(text);
                text = bufferedReader.readLine();
            }
            String result = soapShellStringBuilder.toString();
            return result.replaceAll("string", patameterValue);
        } catch (FileNotFoundException e) {
            throw new SdkSystemException("无法找到请求模板文件");
        } catch (IOException e) {
            throw new SdkSystemException("无法读取请求模板文件");
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                }
            }
        }
    }

    // 删除Soap外壳信息
    private static String RemoveSoapShell(String source) {
        String result = "";
        int indexElementBegin = source.indexOf("<RequestResult>");
        int indexElementEnd = source.indexOf("</RequestResult>");
        if (indexElementBegin > 0 && indexElementEnd > 0) {
            result = source.substring(indexElementBegin
                    + "<RequestResult>".length(), indexElementEnd);
        }
        return result;
    }

    // 将xml文档转换为可传输的字符串
    private static String XMLToString(String source) {
        String result = source.replaceAll("<", "&lt;");
        result = result.replaceAll(">", "&gt;");
        return result;
    }

    // 将返回的字符串转换为可反序列化XML文本
    private static String StringToXML(String source) {
        String result = source.replaceAll("&lt;", "<");
        result = result.replaceAll("&gt;", ">");
        return result;
    }

    // 加载静态信息
    private static ArrayList<HttpRequestProperty> LoadStaticHttpRequestProperties() {
        ArrayList<HttpRequestProperty> staticHttpRequestProperty = new ArrayList<HttpRequestProperty>();
        staticHttpRequestProperty.add(new HttpRequestProperty("Host",
                "crmint.dev.sh.ctriptravel.com"));
        staticHttpRequestProperty.add(new HttpRequestProperty("Content-Type",
                "text/xml; charset=UTF-8"));
        staticHttpRequestProperty.add(new HttpRequestProperty("SOAPAction",
                "http://ctrip.com/Request"));
        staticHttpRequestProperty.add(new HttpRequestProperty(
                "Accept-Encoding", "gzip, deflate"));

        return staticHttpRequestProperty;
    }
    
    public static String createRequestXml() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<Request><Header AllianceID=\"");
            sb.append(ConfigData.AllianceId);
            sb.append("\" SID=\"");
            sb.append(ConfigData.SId);
            sb.append("\" TimeStamp=\"");
            long timestamp = SignatureUtils.GetTimeStamp();
            sb.append(timestamp);
            sb.append("\" Signature=\"");

            String signature = SignatureUtils.CalculationSignature(timestamp
                    + "", ConfigData.AllianceId, ConfigData.SecretKey, ConfigData.SId, ConfigData.RequestType);
            sb.append(signature);
            sb.append("\" RequestType=\"");
            sb.append(ConfigData.RequestType);
            sb.append("\" AsyncRequest=\"false\" "
                    + "Timeout=\"0\" MessagePriority=\"3\"/><HotelRequest>"
                    + "<RequestBody xmlns:ns=\"http://www.opentravel.org/OTA/2003/05\" "
                    + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                    + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
                    + "<ns:OTA_PingRQ><ns:EchoData>联通测试</ns:EchoData></ns:OTA_PingRQ></RequestBody></HotelRequest></Request>");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return sb.toString();
    }

    /**
     * 酒店查询
     *
     * @param hotelCityCode 城市ID
     * @param AreaID 行政区ID
     * @param hotelName 酒店名称
     * @param isHotelStarRate 选择酒店评级标准,true为星级,false为携程星级,
     * @param rating 星级等级
     * @return
     */
    public static String createHotelRequestXml(int hotelCityCode, int AreaID, String hotelName,
            boolean isHotelStarRate, int rating) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<Request><Header AllianceID=\"");
            sb.append(ConfigData.AllianceId);
            sb.append("\" SID=\"");
            sb.append(ConfigData.SId);
            sb.append("\" TimeStamp=\"");
            long timestamp = SignatureUtils.GetTimeStamp();
            sb.append(timestamp);
            sb.append("\" Signature=\"");

            String signature = SignatureUtils.CalculationSignature(timestamp
                    + "", ConfigData.AllianceId, ConfigData.SecretKey, ConfigData.SId, ConfigData.hotelRequestType);
            sb.append(signature);
            sb.append("\" RequestType=\"");
            sb.append(ConfigData.hotelRequestType);
            sb.append("\" AsyncRequest=\"false\" "
                    + "Timeout=\"0\" MessagePriority=\"3\"/><HotelRequest>"
                    + "<RequestBody xmlns:ns=\"http://www.opentravel.org/OTA/2003/05\" "
                    + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                    + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
                    + "<ns:OTA_HotelSearchRQ Version=\"0.0\" PrimaryLangID=\"zh\" xsi:schemaLocation=\"http://www.opentravel.org/OTA/2003/05 OTA_HotelSearchRQ.xsd\" xmlns=\"http://www.opentravel.org/OTA/2003/05\">"
                    + "  <ns:Criteria>"
                    + "    <ns:Criterion>"
                    + "      <ns:HotelRef ");
            if (AreaID != 0) {
                sb.append("AreaID=\"").append(AreaID).append("\"");
            }
            sb.append("            HotelCityCode=\"").append(hotelCityCode).append("\""
                    + "            HotelName=\"").append(hotelName).append("\"/>\n");
            if (isHotelStarRate) {
                sb.append("<ns:Award Provider=\"HotelStarRate\" ");
            } else {
                sb.append("<ns:Award Provider=\"CtripStarRate\" ");
            }
            sb.append("Rating=\"").append(rating).append("\"/>"
                    + "    </ns:Criterion>\n"
                    + "  </ns:Criteria>\n"
                    + "</ns:OTA_HotelSearchRQ>"
                    + "</RequestBody></HotelRequest></Request>");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return sb.toString();
    }

    public static String createHotelInfoRequestXml(String hotelCode) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<Request><Header AllianceID=\"");
            sb.append(ConfigData.AllianceId);
            sb.append("\" SID=\"");
            sb.append(ConfigData.SId);
            sb.append("\" TimeStamp=\"");
            long timestamp = SignatureUtils.GetTimeStamp();
            sb.append(timestamp);
            sb.append("\" Signature=\"");

            String signature = SignatureUtils.CalculationSignature(timestamp
                    + "", ConfigData.AllianceId, ConfigData.SecretKey, ConfigData.SId, ConfigData.hotelInfoRequestType);
            sb.append(signature);
            sb.append("\" RequestType=\"");
            sb.append(ConfigData.hotelInfoRequestType);
            sb.append("\" AsyncRequest=\"false\" "
                    + "Timeout=\"0\" MessagePriority=\"3\"/><HotelRequest>"
                    + "<RequestBody xmlns:ns=\"http://www.opentravel.org/OTA/2003/05\" "
                    + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                    + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">"
                    + "<OTA_HotelDescriptiveInfoRQ Version=\"1.0\" xsi:schemaLocation=\"http://www.opentravel.org/OTA/2003/05 OTA_HotelDescriptiveInfoRQ.xsd\" xmlns=\"http://www.opentravel.org/OTA/2003/05\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
                    + "  <HotelDescriptiveInfos>\n"
                    + "    <HotelDescriptiveInfo HotelCode=\"");
            sb.append(hotelCode).append("\">"
                    + "      <HotelInfo SendData=\"true\"/>"
                    + "      <FacilityInfo SendGuestRooms=\"true\"/>"
                    + "      <AreaInfo SendAttractions=\"true\" SendRecreations=\"true\"/>"
                    + "      <MultimediaObjects SendData=\"true\"/>"
                    + "    </HotelDescriptiveInfo>"
                    + "  </HotelDescriptiveInfos>\n"
                    + "</OTA_HotelDescriptiveInfoRQ>\n"
                    + "</RequestBody></HotelRequest></Request>");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return sb.toString();
    }
}
