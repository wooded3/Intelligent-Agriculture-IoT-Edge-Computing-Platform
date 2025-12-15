package com.iaiotecp.backend.maintenance.model;

public class Classroom {
    private String id;
    private String name;
    private String location;
    private int deviceCount;

    public Classroom() {}

    public Classroom(String id, String name, String location, int deviceCount) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.deviceCount = deviceCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(int deviceCount) {
        this.deviceCount = deviceCount;
    }
}



