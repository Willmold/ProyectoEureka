package com.example.ApplicationRest;

import com.example.ApplicationRest.JsonModels.Eureka.Instance;
import com.example.ApplicationRest.JsonModels.Eureka.Application;
import com.example.ApplicationRest.JsonModels.Eureka.EurekaModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

@RestController
public class ApiController {

    @Value("${eureka.instance.hostname}")
    private String eurekaUrl;

    @RequestMapping(value = "/relacion/{apiName}", method = RequestMethod.GET)
    public ResponseEntity<String> endpoint(@PathVariable String apiName) {
        String urlEndpoint = eurekaUrl + "eureka/apps";
        StringBuilder response = new StringBuilder();
        try {
            response = requestEndpointInfo(urlEndpoint + "/" + apiName);

            ObjectMapper objMapper = new ObjectMapper();
            EurekaModel eureka = objMapper.readValue(response.toString(), EurekaModel.class);

            response.setLength(0);

            Instance instance = getOneInstanceOfApplication(eureka.getApplication());

            if (instance == null) {
                System.out.println("No se ha encontrado ninguna api con el nombre " + apiName + ".");
                response = new StringBuilder("No se ha encontrado ninguna api con el nombre " + apiName + ".");
            } else {
                System.out.println(instance.getIpAddr() + ":" + instance.getPort().get$());
                response = requestEndpointInfo("http://" + instance.getIpAddr() + ":" + instance.getPort().get$() + "/saludo/guille");
            }

            String response2 = requestEndpointInfo(urlEndpoint + "/CONFIG-SERVER-MASTER").toString();

            objMapper = new ObjectMapper();
            eureka = objMapper.readValue(response2, EurekaModel.class);

            instance = getOneInstanceOfApplication(eureka.getApplication());
            System.out.println("Mi config-server está corriendo en: " + instance.getIpAddr() + ":" + instance.getPort().get$() + " - " + instance.getStatus());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    private StringBuilder requestEndpointInfo(String url) throws IOException {
        URL urlEndpoint = new URL(url);
        StringBuilder response = new StringBuilder();
        HttpURLConnection conn = (HttpURLConnection) urlEndpoint.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            br.close();
            conn.disconnect();
        } else {
            throw new ConnectException("No se ha podido establecer conexión con '" + url + "'.");
        }

        return response;
    }

    private Instance getOneInstanceOfApplication(Application app) {
        Instance response = null;
        for (Instance instance : app.getInstance()) {
            if (instance.getStatus().equals("UP")) {
                response = instance;
                break;
            }
        }
        return response;
    }

}