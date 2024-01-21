package com.codigo.msexamenexp.util;

import com.codigo.msexamenexp.aggregates.response.ResponseSunat;
import com.codigo.msexamenexp.entity.EnterprisesEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class Util {
    public static String convertToJson(ResponseSunat responseSunat) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(responseSunat);
        } catch (Exception e) {
            // Manejar la excepción (puede ser JsonProcessingException)
            e.printStackTrace();
            return null;
        }
    }
    public static String convertToJsonEntity(EnterprisesEntity enterprisesEntity) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(enterprisesEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static <T> T convertFromJson(String json, Class<T> valueType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, valueType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
