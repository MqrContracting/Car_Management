package com.carwash.CarWash.Responses;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;


@AllArgsConstructor
public class ResponseModel<T> {
    private HttpStatus statusCode;
    private String message;
    private T data;
    // arg for optional params for paginations
    public ResponseModel(HttpStatus statusCode, String message) {
        this.message = message;
        this.statusCode = statusCode;
    }

    public static <T> ResponseEntity<HashMap<String, Object>> success(String message, T data) {
        HashMap<String, Object> res = new HashMap<>();
        res.put("code", HttpStatus.OK);
        res.put("message", message);
        res.put("data", data);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    public static ResponseEntity<HashMap<String, String>> error(String message, HttpStatus statusCode) {
        HashMap<String, String> res = new HashMap<>();
        res.put("code", statusCode.toString());
        res.put("message", message);

        return ResponseEntity.status(statusCode).body(res);
    }
}
