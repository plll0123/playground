package org.playground.nginxdemo.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class NginxApi {

    @Value("${SERVER_NAME:Unknown-Server}")
    private String serverName;

    @GetMapping
    public Map<String, Object> getServerInfo(@RequestHeader Map<String, String> headers) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("serverName", serverName);
        response.put("headers", headers);
        return response;
    }

}
