package com.epam.dao;

import com.epam.net.HttpCodes;
import com.epam.net.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import io.vavr.Tuple2;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface AbstractDAO<Value> extends DAOCrud<Value, Long> {
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                return LocalDateTime.parse(json.getAsString(), formatter);
            })
            .create();

    Class<Value> getModelClass();

    HttpResponse respondOnGET(List<Tuple2<String, Long>> tuples);

    default HttpResponse respondOnPOST(long id, String body) {
        try {
            return new HttpResponse(gson.toJson(update(id, gson.fromJson(body, getModelClass()))));
        } catch (SQLException e) {
            return new HttpResponse(HttpCodes.INTERNAL_SERVER_ERROR);
        }
    }

    default HttpResponse respondOnPUT(String body) {
        try {
            return new HttpResponse(gson.toJson(create(gson.fromJson(body, getModelClass()))));
        } catch (SQLException e) {
            return new HttpResponse(HttpCodes.INTERNAL_SERVER_ERROR);
        }
    }

    default HttpResponse respondOnDELETE(long id) {
        try {
            deleteById(id);
        } catch (SQLException e) {
            return new HttpResponse(HttpCodes.INTERNAL_SERVER_ERROR);
        }
        return new HttpResponse(HttpCodes.OK);
    }
}
