import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.json.JSONObject;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jakob on 28-07-2016.
 */
@ManagedBean(name="client")
@SessionScoped
public class ClientBean implements Serializable{
    private String clientId = "7";

    private String secret = "1234567890";

    private String code;
    //private String domain = "http://graugaard.bobach.eu:8080/";
    private String domain = "http://localhost:8080";
    private String oauthLogin = domain + "/OAuth/login.html";
    private String oauthToken = domain + "/OAuth/rest/oauth";
    private String redirect = domain + "/Client/client.jsf";
    private String token = "";
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClientId() {
        return clientId;
    }

    public String getToken() {
        return token;
    }

    public boolean isValidCode() {
        return !(code == null || code.equals(""));
    }

    public String getRedirectUrl() {
        return oauthLogin + "?client_id=" + clientId
                + "&redirect_uri=" + redirect
                + "&permissions=fear,game";
    }

    public void getAuthCode() throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        context.redirect(oauthLogin + "?client_id=" + clientId
                + "&redirect_uri=" + redirect
                + "&permissions=fear,game");
    }

    public void handleCode() throws IOException {
        if (token.equals("") && isValidCode()) {
            String s = makeRequest(oauthToken +
                    "?code=" + code +
                    "&client_id=" + clientId +
                    "&client_secret=" + secret);
            JSONObject o = new JSONObject(s);

            token = o.getString("access_token");
        }
    }

    public String getInfoAboutUser() throws IOException {
        if (!(token == null || token.equals(""))) {
            return makeRequest(domain + "/OAuth/rest/resource?token=" + token);
        }
        return "";
    }

    private String makeRequest(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

        StringBuilder builder = new StringBuilder();

        String line = null;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        return builder.toString();
    }
}
