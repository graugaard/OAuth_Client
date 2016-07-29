import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jakob on 28-07-2016.
 */
@ManagedBean(name="client")
@SessionScoped
public class ClientBean {
    private String clientId = "7";

    private String secret = "1234567890";

    private String code;
    private String domain = "http://graugaard.bobach.eu:8080/";
    private String oauthLogin = domain + "OAuth/login.html";
    private String oauthToken = domain + "OAuth/rest/oauth";
    private String redirect = domain + "Client/client.jsf";
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

    public void handleCode() throws IOException {
        if (token.equals("") && (code == null || code.equals(""))) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect(oauthLogin + "?client_id=" + clientId
                    + "&redirect_uri=" + redirect
                    + "&permissions=fear,game");
        } else {
            URL url = new URL(oauthToken +
                    "?code=" + code +
                    "&client_id=" + clientId +
                    "&client_secret=" + secret);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            StringBuilder builder = new StringBuilder();

            String line = null;
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }

            token = builder.toString();
        }
    }
}
