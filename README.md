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

---

## 3. Technology Stack

- Java
- JAX-RS (Jersey)
- Apache Tomcat
- Apache NetBeans
- Maven
- Jackson JSON
- In-memory Java collections

### Important Constraints

- **JAX-RS only**
- **No Spring Boot**
- **No database**
- **No ZIP-only submission**
- **Public GitHub repository required**

---

## 4. Project Structure

```text
smart-campus-api/
├── .gitignore
├── README.md
├── nbactions.xml
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── smartcampus/
        │           ├── ApplicationConfig.java
        │           ├── model/
        │           │   ├── Room.java
        │           │   ├── Sensor.java
        │           │   ├── SensorReading.java
        │           │   └── ApiError.java
        │           ├── store/
        │           │   └── DataStore.java
        │           ├── resource/
        │           │   ├── DiscoveryResource.java
        │           │   ├── RoomResource.java
        │           │   ├── SensorResource.java
        │           │   └── SensorReadingResource.java
        │           ├── exception/
        │           │   ├── RoomNotEmptyException.java
        │           │   ├── LinkedResourceNotFoundException.java
        │           │   └── SensorUnavailableException.java
        │           ├── mapper/
        │           │   ├── RoomNotEmptyExceptionMapper.java
        │           │   ├── LinkedResourceNotFoundExceptionMapper.java
        │           │   ├── SensorUnavailableExceptionMapper.java
        │           │   └── GlobalExceptionMapper.java
        │           └── filter/
        │               └── LoggingFilter.java
        └── webapp/
            ├── index.html
            └── WEB-INF/
                └── web.xml
```
---

## 5. How to Build and Run the Project

### Using NetBeans and Apache Tomcat

1. Open **Apache NetBeans**
2. Open the project:
   - `smart-campus-api`
3. Ensure **Apache Tomcat** is configured in the **Services** tab
4. Right-click the project and choose:

```text
Clean and Build
```
---

## 6. API Endpoints

### Discovery

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1` | Returns API metadata and resource links |

### Rooms

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/rooms` | Returns all rooms |
| POST | `/api/v1/rooms` | Creates a new room |
| GET | `/api/v1/rooms/{roomId}` | Returns one room by ID |
| DELETE | `/api/v1/rooms/{roomId}` | Deletes a room if it has no assigned sensors |

### Sensors

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/sensors` | Returns all sensors |
| GET | `/api/v1/sensors?type=Temperature` | Filters sensors by type |
| POST | `/api/v1/sensors` | Creates a new sensor |
| GET | `/api/v1/sensors/{sensorId}` | Returns one sensor by ID |

### Sensor Readings

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/sensors/{sensorId}/readings` | Returns reading history |
| POST | `/api/v1/sensors/{sensorId}/readings` | Adds a new reading for the sensor |

