package com.abhi.docman.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoIpService {
    private final RestTemplate restTemplate = new RestTemplate();

    public Location lookup(String ip) {
        try {
            if (ip == null || ip.isBlank() || isPrivateIp(ip)) {
                return null;
            }
            String url = "http://ip-api.com/json/" + ip + "?fields=status,country,city";
            IpApiResponse resp = restTemplate.getForObject(url, IpApiResponse.class);
            if (resp != null && "success".equalsIgnoreCase(resp.status)) {
                Location loc = new Location();
                loc.country = resp.country;
                loc.city = resp.city;
                return loc;
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private boolean isPrivateIp(String ip) {
        return ip.startsWith("10.") || ip.startsWith("192.168.") || ip.startsWith("127.") || ip.startsWith("::1") || ip.startsWith("172.16.") || ip.startsWith("172.17.") || ip.startsWith("172.18.") || ip.startsWith("172.19.") || ip.startsWith("172.2") || ip.startsWith("fc") || ip.startsWith("fd");
    }

    public static class Location {
        public String country;
        public String city;
    }

    public static class IpApiResponse {
        public String status;
        public String country;
        public String city;
    }
}

