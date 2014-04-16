/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webapiapplication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author bianshujun
 */
public class HttpHelper {

    public static String readContentFromGet(String url) throws IOException {

        URL getUrl = new URL(url);

        HttpURLConnection connection = (HttpURLConnection) getUrl
                .openConnection();

        connection.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        String lines;
        StringBuilder result = new StringBuilder();
        while ((lines = reader.readLine()) != null) {
            result.append(lines);
        }
        reader.close();

        connection.disconnect();
        return result.toString();
    }

    public static String readContentFromPost(String url, String postContent) throws IOException {

        URL postUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) postUrl
                .openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);

        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Content-Type",
                "application/xml");
        connection.connect();
        DataOutputStream out = new DataOutputStream(connection
                .getOutputStream());

        out.writeBytes(postContent);
        out.flush();
        out.close(); // flush and close
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        String ss = "";
        StringBuilder line = new StringBuilder();
        while ((ss = reader.readLine()) != null) {
            line.append(ss);
        }
        reader.close();
        connection.disconnect();
        return line.toString();
    }

}