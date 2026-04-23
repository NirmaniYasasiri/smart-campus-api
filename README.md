# Smart Campus Sensor & Room Management API
## 1. Project Overview

This project is a RESTful API developed for the **Client-Server Architectures** coursework.

The API manages three main resource types:

- **Rooms**
- **Sensors**
- **Sensor Readings**

The system is built using **JAX-RS (Jersey)** and deployed using **Apache Tomcat** in **NetBeans**. It uses **in-memory Java collections** such as maps and lists instead of a database.

The base API path is:

```text
/api/v1
```
---

## 2. API Design Overview

The API follows a resource-based RESTful structure.

### Main Resources

#### Room
A room represents a physical room in the smart campus environment.

Fields:
- `id`
- `name`
- `capacity`
- `sensorIds`

#### Sensor
A sensor represents a device connected to a room.

Fields:
- `id`
- `type`
- `status`
- `currentValue`
- `roomId`

#### SensorReading
A sensor reading represents a historical measurement captured by a sensor.

Fields:
- `id`
- `timestamp`
- `value`

### Resource Hierarchy

- `/api/v1` → discovery endpoint
- `/api/v1/rooms` → room collection
- `/api/v1/sensors` → sensor collection
- `/api/v1/sensors/{sensorId}/readings` → nested sensor reading history

This structure keeps the API clear and resource-oriented. The reading history is modelled as a nested sub-resource because readings belong to a specific sensor.
