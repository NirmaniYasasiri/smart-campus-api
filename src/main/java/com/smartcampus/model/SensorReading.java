package com.smartcampus.model;

/**
 * Model class representing a historical reading recorded by a sensor.
 *Each reading has its own ID, a timestamp, and a measured value.
 */
public class SensorReading {

    /** Unique reading ID */
    private String id;

    /** Time when the reading was recorded */
    private long timestamp;

    /** Recorded sensor value */
    private double value;

    /** Default constructor required for JSON support */
    public SensorReading() {
    }

    /**
     * Creates a sensor reading.
     *
     * @param id reading ID
     * @param timestamp reading timestamp
     * @param value reading value
     */
    public SensorReading(String id, long timestamp, double value) {
        this.id = id;
        this.timestamp = timestamp;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}