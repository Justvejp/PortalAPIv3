package com.example.PortalAPIv3.API;

import com.example.PortalAPIv3.API.Service.APIService;
import com.example.PortalAPIv3.Logic.EngineHeater;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class APIController {

    List<EngineHeater> engineHeaters = new ArrayList<>();

    @Autowired
    public final APIService apiService;

    public APIController(APIService apiService) {
        this.apiService = apiService;
        System.out.println(apiService.getAllEngineheaters());
        initiateEngineHeaters();
    }

    @PostMapping(value = "/engineheater/", consumes = MediaType.APPLICATION_JSON_VALUE)
    private Mono<ResponseEntity<JsonNode>> postEngineheater(@RequestBody String payload) {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.nullNode();
        try {
            jsonNode = mapper.readTree(payload);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        boolean checkPayload = apiService.checkIncomingPayload(jsonNode);
        if (checkPayload) {

            String devUI = jsonNode.get("devUi").toString();
            Mono<JsonNode> m = apiService.postEngineheater(jsonNode);

            EngineHeater ehp = new EngineHeater(devUI, jsonNode);

            if (engineHeaters.size() == 0) {
                engineHeaters.add(ehp);
            } else {
                boolean exist = false;
                for (int i = 0; i < engineHeaters.size(); i++) {
                    if (ehp.devUI.equals(engineHeaters.get(i).devUI)) {
                        engineHeaters.set(i, ehp);

                        //cancel task när man skriver över den?
                        //thread interrupt?
                        //måste cancela task när man tar bort engineheater.
                        //cancelar tasks när man gör en ny lista? eller stänger av programmet dvs rensar listan
                        exist = true;
                        break;
                    }
                }
                if (!exist) {
                    engineHeaters.add(ehp);
                }
            }

            return m.map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());

        } else {
            //TODO Returnera hur json skall se ut när det skickas in en felaktig
            return Mono.just(ResponseEntity.badRequest().build());
        }
    }
    public void initiateEngineHeaters() {
        for (int i = 0; i < apiService.getAllEngineheaters().size(); i++) {
            String devUI = apiService.getAllEngineheaters().get(i).get("devUi").toString();
            EngineHeater eh = new EngineHeater(devUI, apiService.getAllEngineheaters().get(i));
            engineHeaters.add(eh);
        }
    }

    @PostMapping(value = "/noc/", consumes = MediaType.APPLICATION_JSON_VALUE)
    private Mono<ResponseEntity<JsonNode>> postNoc(@RequestBody String payload) {
        //Ta emot cpu, minne, disk
        int ping = apiService.getPingFromWebIot();
        System.out.println(ping);
        return Mono.just(ResponseEntity.badRequest().build());
    }
}
