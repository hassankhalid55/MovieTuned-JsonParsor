package Model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Hassan Khalid on 27/05/2016.
 */
public class HTTPURLConnectionHelper {
    private final String USER_AGENT = "Mozilla/5.0";

    public String makeServiceCall(String urlParameters){
        try {

            URL obj = new URL(urlParameters);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
//            con.setRequestMethod(method);

            //add request header
//            con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = con.getResponseCode();
//            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        }catch (Exception e){
            return null;
        }
    }
}
