package com.example.ApiRest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

//    @Value("${server.port}")
//    String url;

    @RequestMapping(value = "/saludo/{nombre}", method = RequestMethod.GET)
    public ResponseEntity<String> endpoint(@PathVariable String nombre) {
        return new ResponseEntity<>("HOLA " + nombre, HttpStatus.OK);
    }

}