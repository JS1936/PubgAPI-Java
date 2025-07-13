public class API {
    protected final String key = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmMjdiZDY0MC05ODk5LTAxM2EtY" +
            "jVmNS0wYzc0NWVlZDY1NjQiLCJpc3MiOiJnYW1lbG9ja2VyIiwiaWF0IjoxNjQ5MzMzNTYxLCJwdWIiOiJibHVlaG9sZSIs" +
            "InRpdGxlIjoicHViZyIsImFwcCI6InB1YmdfYXBpX2xlYXJuIn0.aQZbXGdwOM8HwXLvulYN2nmUUCVgG6susMmAE6oKopY";
    protected final String platform = "steam";

    protected String getAPIkey()
    {
        return this.key;
    }
    protected String getAPIplatform()
    {
        return this.platform;
    }

}