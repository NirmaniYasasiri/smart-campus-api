package com.smartcampus.model;

/**
 * Model class representing a sensor in the Smart Campus system.
 * A sensor belongs to a room and stores its type, current status,
 * and latest measured value.
 */
public class Sensor {

    /** Unique sensor identifier, for example TEMP-001 */
    private String id;

    /** Sensor type, for example Temperature, Occupancy, or CO2 */
    private String type;

    /** Sensor status: ACTIVE, MAINTENANCE, or OFFLINE */
    private String status;

    /** Latest recorded value for the sensor */
    private double currentValue;

    /** ID of the room this sensor is linked to */
    private String roomId;

    /** Default constructor required for JSON support */
    public Sensor() {
    }

    /**
     * Creates a sensor with all main fields.
     *
     * @param id sensor ID
     * @param type sensor type
     * @param status sensor status
     * @param currentValue latest sensor value
     * @param roomId linked room ID
     */
    public Sensor(String id, String type, String status, double currentValue, String roomId) {
        this.id = id;
        this.type = type;
        this.status = status;
        this.currentValue = currentValue;
        this.roomId = roomId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}