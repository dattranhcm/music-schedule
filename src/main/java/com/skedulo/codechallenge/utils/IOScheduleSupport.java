package com.skedulo.codechallenge.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.skedulo.codechallenge.dto.MusicBandSchedule;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class IOScheduleSupport {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").
            setPrettyPrinting().create();

    public static ArrayList<MusicBandSchedule> readInputFile(String inputFilePath) throws FileNotFoundException {
        JsonReader reader = new JsonReader(new FileReader(inputFilePath));
        Type listOfMyClassObject = new TypeToken<ArrayList<MusicBandSchedule>>() {}.getType();
        return gson.fromJson(reader, listOfMyClassObject);
    }

    public static String getNameWithoutExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    public static void writeResultFileActsScheduleTest(List<MusicBandSchedule> musicBandSchedules, String outputFilePath) {
        gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
        try (FileWriter writer = new FileWriter(outputFilePath)) {
            gson.toJson(musicBandSchedules, writer);
        } catch (IOException e) {
            System.out.println("An Error Occur when write output: " + e.getMessage());
        }
    }
}
