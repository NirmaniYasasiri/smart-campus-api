package com.smartcampus.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Model class representing a room in the Smart Campus system.
 *Each room has an ID, a human-readable name, a maximum capacity,
 * and a list of sensor IDs assigned to that room.
 */
public class Room {

    /** Unique room identifier, for example LIB-301 */
    private String id;

    /** Human-readable room name */
    private String name;

    /** Maximum room capacity */
    private int capacity;

    /** List of sensor IDs currently assigned to this room */
    private List<String> sensorIds = new CopyOnWriteArrayList<>();

    /** Default constructor required for JSON serialization/deserialization */
    public Room() {
    }

    /**
     * Creates a room with the main fields.
     *
     * @param id room ID
     * @param name room name
     * @param capacity room capacity
     */
    public Room(String id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.sensorIds = new CopyOnWriteArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getSensorIds() {
        return sensorIds;
    }

    public void setSensorIds(List<String> sensorIds) {
        this.sensorIds = sensorIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}