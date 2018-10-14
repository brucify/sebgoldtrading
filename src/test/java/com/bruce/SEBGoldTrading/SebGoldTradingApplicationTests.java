package com.bruce.SEBGoldTrading;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SebGoldTradingApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void depthShouldContainFields() {
        String depthUrl = "http://localhost:" + port + "/depth";
        assertThat(this.restTemplate.getForObject(depthUrl, String.class))
                .contains("bestBid")
                .contains("bestAsk")
                .contains("allBids")
                .contains("allAsks");
    }

    @Test
    public void newOrderShouldChangeVolume() throws Exception {
        /*
          Given
         */
        String depthUrl = "http://localhost:" + port + "/depth";
        String depthResponse = this.restTemplate.getForObject(depthUrl, String.class);

        assertThat(depthResponse)
                .contains("bestBid")
                .contains("bestAsk")
                .contains("allBids")
                .contains("allAsks");

        double oldBestAsk = parseBestAsk(depthResponse);
        int oldBestAskVolume = parseBestAskVolume(depthResponse);

        /*
          When
         */
        String orderUrl = "http://localhost:" + port + "/orders";
        Double price = oldBestAsk;
        Integer volume = 5;

        assertThat(postOrder(orderUrl, "SELL", price, volume).getStatusCode())
                .isEqualByComparingTo(HttpStatus.OK);

        /*
          Then
         */
        depthResponse = this.restTemplate.getForObject(depthUrl, String.class);

        assertThat(parseBestAskVolume(depthResponse))
                .isEqualTo(oldBestAskVolume + volume);
    }

    @Test
    public void newOrderShouldChangeDepth() throws Exception {
        /*
          Given
         */
        String depthUrl = "http://localhost:" + port + "/depth";
        String depthResponse = this.restTemplate.getForObject(depthUrl, String.class);
        double oldBestAsk = parseBestAsk(depthResponse);
        int oldBestAskVolume = parseBestAskVolume(depthResponse);

        /*
          When
         */
        String orderUrl = "http://localhost:" + port + "/orders";

        assertThat(postOrder(orderUrl, "BUY", oldBestAsk, oldBestAskVolume).getStatusCode())
                .isEqualByComparingTo(HttpStatus.OK);

        /*
          Then
         */
        depthResponse = this.restTemplate.getForObject(depthUrl, String.class);

        assertThat(parseBestAsk(depthResponse)).isNotEqualTo(oldBestAsk);
    }



    /*
      Private
     */

    private ResponseEntity<String> postOrder(String orderUrl, String side, Double price, Integer volume) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("side", side);
        map.add("price", price.toString());
        map.add("volume", volume.toString());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        return restTemplate.postForEntity( orderUrl, request , String.class );
    }

    private Double parseBestAsk(String depthResponse) throws IOException {
        String sBestAsk = mapper.readTree(depthResponse).get("bestAsk").toString();

        return Double.parseDouble(sBestAsk);
    }

    private Integer parseBestAskVolume(String depthResponse) throws IOException {
        String sBestAsk = mapper.readTree(depthResponse).get("bestAsk").toString();
        String sBestAskVolume = mapper.readTree(depthResponse).get("allAsks").get(sBestAsk).toString();

        return Integer.parseInt(sBestAskVolume);
    }


}
