import javax.faces.bean.ManagedBean;
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
public class ClientBean {
    private String appId = "7";

    private String secret = "1234567890";

    private String code;

    private String oauthLogin = "http://localhost:8080/OAuth/login.html";
    private String oauthToken = "http://localhost:8080/OAuth/rest/oauth";
    private String redirect = "http://localhost:8080/Client/client.jsf";
    private String token = "";
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAppId() {
        return appId;
    }

    public String getToken() {
        return token;
    }

    public void handleCode() throws IOException {
        if (code == null || code.equals("")) {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            context.redirect(oauthLogin + "?client_id=" + appId
                    + "&redirect_uri=" + redirect
                    + "&permissions=fear,game");
        } else {
            URL url = new URL(oauthToken +
                    "?code=" + code +
                    "&client_id=" + appId +
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
