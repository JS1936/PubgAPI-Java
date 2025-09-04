import io.github.cdimascio.dotenv.Dotenv;

public class API {
    protected final String key = Dotenv.load().get("API_KEY");
    protected final String platform = "steam";

    public String getAPIkey()
    {
        return this.key;
    }
    public String getAPIplatform()
    {
        return this.platform;
    }

}
