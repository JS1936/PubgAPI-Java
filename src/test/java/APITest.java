import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
//import API;
//import API_Request;

import java.io.IOException;

class APITest {

    @Test
    void getAPIkey() throws IOException {

        API test = new API_Request("Bob");
        String keyActual = test.getAPIkey();
        String keyExpect = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJmMjdiZDY0MC05ODk5LTAxM2EtY" +
                "jVmNS0wYzc0NWVlZDY1NjQiLCJpc3MiOiJnYW1lbG9ja2VyIiwiaWF0IjoxNjQ5MzMzNTYxLCJwdWIiOiJibHVlaG9sZSIs" +
                "InRpdGxlIjoicHViZyIsImFwcCI6InB1YmdfYXBpX2xlYXJuIn0.aQZbXGdwOM8HwXLvulYN2nmUUCVgG6susMmAE6oKopY";
        assertEquals(keyActual, keyExpect);
    }

    @Test
    void getAPIplatform() throws IOException {
        API test = new API_Request("Bob");
        String platformActual = test.getAPIplatform();
        String platformExpect = "steam";

        assertEquals(platformActual, platformExpect);
    }
}