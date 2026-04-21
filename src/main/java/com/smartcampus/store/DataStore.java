package com.smartcampus.store;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Shared in-memory storage for the Smart Campus API.
 * This class replaces the need for a database and stores all rooms,
 * sensors, and sensor readings using Java collections.
 */
public final class DataStore {

    /** Stores all rooms by room ID */
    private static final Map<String, Room> rooms = new ConcurrentHashMap<>();

    /** Stores all sensors by sensor ID */
    private static final Map<String, Sensor> sensors = new ConcurrentHashMap<>();

    /** Stores sensor reading history grouped by sensor ID */
    private static final Map<String, List<SensorReading>> readingsBySensor = new ConcurrentHashMap<>();

    /** Private constructor prevents creating objects from this utility class */
    private DataStore() {
    }

    /**
     * Returns the shared map of rooms.
     *
     * @return rooms map
     */
    public static Map<String, Room> getRooms() {
        return rooms;
    }

    /**
     * Returns the shared map of sensors.
     *
     * @return sensors map
     */
    public static Map<String, Sensor> getSensors() {
        return sensors;
    }

    /**
     * Returns the shared map of reading history.
     *
     * @return readings-by-sensor map
     */
    public static Map<String, List<SensorReading>> getReadingsBySensor() {
        return readingsBySensor;
    }

    /**
     * Returns the reading list for a given sensor.
     * If the sensor has no reading list yet, a new one is created.
     *
     * @param sensorId sensor ID
     * @return list of sensor readings
     */
    public static List<SensorReading> getOrCreateReadingsList(String sensorId) {
        return readingsBySensor.computeIfAbsent(sensorId, id -> new CopyOnWriteArrayList<>());
    }
}