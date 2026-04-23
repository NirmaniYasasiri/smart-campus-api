# Smart Campus Sensor & Room Management API

- **5COSC022W Client-Server Architectures**
- **Module Leader: Mr. Caseem Farook**
- **Assignment Number: 01**
- **Assignment Type: Individual**
- **Deadline: 24th April 2026**
- **Student ID: 20231489/w2120471**
- **Student name: P.M.K.N.Nirmani Yasasiri**

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

---

## 7. Sample JSON Request Bodies

### Create Room

```json
{
  "id": "LIB-301",
  "name": "Library Quiet Study",
  "capacity": 80
}

{
  "id": "TEMP-001",
  "type": "Temperature",
  "status": "ACTIVE",
  "currentValue": 22.5,
  "roomId": "LIB-301"
}

{
  "id": "CO2-001",
  "type": "CO2",
  "status": "MAINTENANCE",
  "currentValue": 410.0,
  "roomId": "ENG-101"
}
```
---

## 8. Sample curl Commands

### 1. Discovery endpoint

```bash
curl -X GET http://localhost:8080/smart-campus-api/api/v1
```

### 2. Create a room

```bash
curl -X POST http://localhost:8080/smart-campus-api/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"LIB-301\",\"name\":\"Library Quiet Study\",\"capacity\":80}"
```

### 3. Get all rooms

```bash
curl -X GET http://localhost:8080/smart-campus-api/api/v1/rooms
```

### 4. Get a room by ID

```bash
curl -X GET http://localhost:8080/smart-campus-api/api/v1/rooms/LIB-301
```

### 5. Create a sensor

```bash
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"TEMP-001\",\"type\":\"Temperature\",\"status\":\"ACTIVE\",\"currentValue\":22.5,\"roomId\":\"LIB-301\"}"
```

### 6. Get all sensors

```bash
curl -X GET http://localhost:8080/smart-campus-api/api/v1/sensors
```

### 7. Filter sensors by type

```bash
curl -X GET "http://localhost:8080/smart-campus-api/api/v1/sensors?type=Temperature"
```

### 8. Add a reading

```bash
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d "{\"value\":24.8}"
```

### 9. Get readings

```bash
curl -X GET http://localhost:8080/smart-campus-api/api/v1/sensors/TEMP-001/readings
```

### 10. Trigger 422 invalid room error

```bash
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"OCC-001\",\"type\":\"Occupancy\",\"status\":\"ACTIVE\",\"currentValue\":12,\"roomId\":\"NO-SUCH-ROOM\"}"
```

### 11. Trigger 403 maintenance sensor error

```bash
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors/CO2-001/readings \
  -H "Content-Type: application/json" \
  -d "{\"value\":500.0}"
```

### 12. Trigger 409 room deletion error

```bash
curl -X DELETE http://localhost:8080/smart-campus-api/api/v1/rooms/LIB-301
```
---

## Part 1: Service Architecture & Setup

### 1.1 JAX-RS Resource Class Lifecycle

The default configuration of JAX-RS resource classes is a per-request lifecycle. This implies that a new resource class is instantiated for every HTTP request. Normal resource classes are not typically singletons that share their runtime, except when explicitly configured to be so.

It is important when dealing with in-memory collections. When application data was held directly within a resource instance, it might be lost between requests. It would not be safe with concurrent access either. That is why in this project, a separate shared `DataStore` class that is based on Java collections is used to store rooms, sensors, and sensor readings.

### 1.2 Discovery Endpoint and HATEOAS

The discovery endpoint is useful because it provides a clear entry point into the API. Instead of relying only on external documentation, clients can inspect the root response and discover where major resources are located.

This reflects the idea of hypermedia in RESTful design. It benefits client developers by making the API easier to explore and reducing tight coupling between the client and hardcoded URL paths.

---

## Part 2: Room Management

### 2.1 Returning IDs vs Full Room Objects

Only room IDs are returned, which decreases the size of responses and conserves bandwidth. The client would, however, require additional requests to get complete room details. Returning full room objects will make the response size larger, but this will provide all the useful room information to the client in a single response.

Full room objects are more appropriate in this project as it makes testing easier and gives full room metadata, such as capacity and assigned sensor IDs.

### 2.2 DELETE Idempotency

DELETE is considered idempotent because repeating the same DELETE request does not keep changing the final system state after the first successful deletion.

In this implementation, if a room exists and has no sensors, the first DELETE removes it. If the same request is sent again, the room is already gone, so the system state remains unchanged. If the room still has sensors, the request is blocked with a conflict response until the linked sensors are removed.

---

## Part 3: Sensor Operations & Linking

### 3.1 Effect of `@Consumes(MediaType.APPLICATION_JSON)`

This annotation tells JAX-RS that the endpoint expects JSON in the request body. If a client sends a different format, such as `text/plain` or `application/xml`, the request does not match the expected media type.

In such a case, JAX-RS can reject the request before the method body runs, typically returning `415 Unsupported Media Type`. This helps protect the API contract and ensures the request body can be parsed correctly.

### 3.2 Query Parameter Filtering vs Path Parameter Filtering

Using a query parameter such as `/sensors?type=CO` is more appropriate for optional filtering over a collection. The resource is still the same collection, but the client is requesting a filtered view of it.

This is more flexible and scalable than building a separate path structure like `/sensors/type/CO2`, especially if more filters are added later.

---

## Part 4: Deep Nesting with Sub-Resources

### 4.1 Benefits of the Sub-Resource Locator Pattern

The sub-resource locator pattern keeps the API structure clear by delegating nested logic to a dedicated resource class.

In this project, `/sensors/{sensorId}/readings` is handled by `SensorReadingResource`. This improves separation of concerns because `SensorResource` focuses on sensors, while `SensorReadingResource` focuses only on reading history and reading creation.

This makes the code cleaner, easier to maintain, and easier to extend.

---

## Part 5: Error Handling, Exception Mapping & Logging

### 5.1 Why HTTP 422 is More Accurate than 404

HTTP 422 is more semantically accurate when a JSON request is structurally valid but contains a field that refers to another resource that does not exist, such as a `roomId` in a sensor creation request.

The endpoint `/api/v1/sensors` itself exists, so the problem is not the URL. The problem is the invalid linked resources inside the payload. That is why 422 describes the issue better than a normal 404.

### 5.2 Cybersecurity Risks of Exposing Stack Traces

Internal Java stack traces are not allowed to be disclosed to external users since they may expose package names, class names, method names, line numbers, internal paths, and framework information.

That information could be used by an attacker to learn how the system is internally organized and what the potential vulnerabilities are. Because of this, the project responds to bad requests with a global exception mapper to respond with safe JSON error responses, rather than raw stack traces.

### 5.3 Why Filters Are Better for Logging

Logging is a cross-cutting issue as it is relevant to lots of endpoints of the API. Unless logging statements were included manually within every resource method, the code would be repetitive and more difficult to maintain.

With JAX-RS filters, it becomes centralised in a single location. This makes the resource methods cleaner, and request and response logging is uniformly applied throughout the API.
