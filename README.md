# Smart Campus Sensor And Room Management 

## Overview

This ResftulAPI was created for the smart campus initiative at the INformatics Institute of Technology(IIT), affliated with the University of Westminster.
Campus rooms and IoT sensors installed across the Spencer(SP), GP, and Java(JB) buildings are managed using the API.

**The system allows to**:
* Establish and oversee campus spaces in certain rooms.
* Register and keep an eye on IoT sensors.
* Record and Retrieve sensor readings over time.

Built using JAX-RS with Jersey 3.1 as the implmentation and GRizzly as the embedded HTTP server. ConcurrentHashMap is used to store all data in-memory; no database is utilised.


&nbsp;




## Technology Stack Used


| Component             | Technology                            |
|:----------------------|:--------------------------------------|
| Language              | Java 23                               |
| JAX-RS Implementation | Jersey 3.1.3                          |
| Embedded Server       | Grizzly HTTP Server                   |
| JSON Support          | Jackson via jersey-media-json-jackson |
| Build Tool            | Maven                                 |
| Data Storage          | ConcurrentHashMap (in-memory)         |
| IDE                   | Apache NetBeans 25   



&nbsp;
## How to build and Run?

### Preequistites
* Java 23 or higher.
* Apache Netbeans 25.
* Maven [bundled with NetBeans].

### Steps to Run
1. Clone the repository from GitHub:
git clone https://github.com/WaseemK-ops/5COSC022W-smart-campus.git
2. Open Apache NetBeans
3. Click **File** → **Open Project**
4. Navigate to the cloned folder and select it
5. Right click the project → **Run**
6. The server will start at:
http://localhost:8080/api/v1

### You should see this in the output:
INFO: Smart Campus API - Mohamed Waseem
INFO: Running at http://localhost:8080/api/v1



&nbsp;

## API - Endpoints

| Method  |  Endpoint | Description |
|:-------|:---------|:------------|
| GET | /api/v1 | Discovery - returns API metadata and resource links |
| GET | /api/v1/rooms | Gets all campus rooms |
| POST | /api/v1/rooms | Creates a new room |
| GET | /api/v1/rooms/{roomId} | Gets a specific room by ID |
| DELETE | /api/v1/rooms/{roomId} | Deletes a room (fails if sensors exist) |
| GET | /api/v1/sensors | Gets all sensors (optional ?type= filter) |
| POST | /api/v1/sensors | Registers a new sensor |
| GET | /api/v1/sensors/{sensorId} | Gets a specific sensor by ID |
| GET | /api/v1/sensors/{sensorId}/readings | Gets reading history for a sensor |
| POST | /api/v1/sensors/{sensorId}/readings | Adds a new reading for a sensor |



&nbsp;
&nbsp;
## Sample curl Commands

These commands can be used to test the API once the server is running on localhost:8080.

### 1. Check the API is running

curl -X GET http://localhost:8080/api/v1

**This hits the discovery endpoint and returns basic info about the API including available resource links.**



### 2. Get all campus rooms

curl -X GET http://localhost:8080/api/v1/rooms

**Returns the full list of rooms across SP, GP and JB buildings.**


### 3. Create a new room

curl -X POST http://localhost:8080/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\":\"SP-8LA\",\"name\":\"Spencer Building Floor 8\",\"capacity\":45}"

**Creates a new room. Returns 201 Created with the room details and a Location header.**

### 4. Filter sensors by type

curl -X GET "http://localhost:8080/api/v1/sensors?type=CO2"

Returns only CO2 sensors. You can change the type to Temperature, Occupancy, Light or Humidity.

### 5. Try to delete a room that has sensors

curl -X DELETE http://localhost:8080/api/v1/rooms/SP-1LA

This should return 409 Conflict because SP-1LA still has sensors assigned to it.

### 6. Register a sensor with an invalid roomId

curl -X POST http://localhost:8080/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"TEMP-NEW\",\"type\":\"Temperature\",\"status\":\"ACTIVE\",\"currentValue\":0.0,\"assignedRoomId\":\"FAKE-ROOM\"}"

Returns 422 Unprocessable Entity because FAKE-ROOM does not exist in the system.

### 7. Add a reading to a sensor

curl -X POST http://localhost:8080/api/v1/sensors/TEMP-SP-1/readings -H "Content-Type: application/json" -d "{\"measuredValue\":24.5}"

Records a new reading. Also updates the parent sensor's currentValue automatically.

### 8. Get reading history for a sensor

curl -X GET http://localhost:8080/api/v1/sensors/TEMP-SP-1/readings

Returns the full list of historical readings for that sensor.

### 9. Try to post a reading to a maintenance sensor

curl -X POST http://localhost:8080/api/v1/sensors/TEMP-MAR-01/readings -H "Content-Type: application/json" -d "{\"measuredValue\":20.0}"

Returns 403 Forbidden because TEMP-MAR-01 is currently under maintenance.

&nbsp;


## Report Questions and Answers


**Question: Explain the default lifecycle of a JAX-RS Resource class. Is a new instance 
instantiated for every incoming request, or does the runtime treat it as a singleton? 
How does this impact the way you manage your in-memory data structures?**

- The default lifecycle of a JAX-RS resource class is request-scoped. Yes, a new instance is created for every incoming HTTP requests — It is not treated as a  singleton;  When the request is received, the instance is created, and after the response is given, it is destroyed.
- This directly impacts how I manage data. If I stored room or sensor data as instance fields inside the resource class, they would be lost after every single request because the instance would get discarded. To prevent this, I store all of the data as static ConCurrentHashMap fields in DataStore.java. When data is static, it is present at class level and survives across all requests.
-  Becuase JAS-RS can handle several requests at once on seperate threads, I chose ConcurrentHashMap over a regular HashMap. A regular HashMap is not thread safe and could cause data corruption. ConCurrentHashMap handles concurrent read and writes safely without the need of synchronized blocks.



**Question: Why is the provision of Hypermedia (HATEOAS) considered a hallmark of 
advanced RESTful design? How does this benefit client developers compared to 
static documentation?**

- HATEOAS is considered advanced because it makes the API self descirbing. The API itself inludes those links in the response, saving the client from having to read other documentations to know what URLs exist or are available. This is exactly how the web works - you visit a page and follow links rather than memorising every URL.
- Links to /api/v1/rooms, /api/v1/sensors, and /api/v1/sensors/{sensorId}/readings are returned by GET /api/v1 in my API. Without any documentation, a client developer can begin at the discovery endpoint and work their way through the complete API.
- Compared to static documentation, HATEOAS is better because if a URL changes the server simply updates the link in the response. Every client that depended on static documentation would break it when it became outdated. HATEOAS keeps clients decoupled from hard-coded URLs.




**Question: When returning a list of rooms, what are the implications of returning 
only IDs versus returning the full room objects? Consider network bandwidth and 
client side processing.**


- Returning only IDs results in a quick and minimal response. However the client then has to make a seperate GET request for every single to get its details. The N+1 problem, which results 101 requests overall if there are 100 rooms, actually uses more bandwidth overall. Returning full objects means one requests gets everything. The response is bigger but the client has all the data it needs instantly with no more processing or queries.

- In my implementation I return complete full objects. This is more useful for a campus-scale system since facilities managers require quick access to room specifics, including name,capactity, and sensor list, not just the ID.


**Question: Is the DELETE operation idempotent in your implementation? Provide 
a detailed justification by describing what happens if a client sends the same 
DELETE request multiple times.**

Yes, Delete is idempotent in my implementation. Idempotency is the ability of the server to remain in the same state regardless of how many times you send the same request.

Here is what happens with repeated DELETE requests on the same room:
- First call: room exists, it gets removed, server state = room is absent, 
  response = 204 No Content
- Second call: room is already gone, server state = room is still absent, 
  response = 404 Not Found
- Third call: same as second call

After each call, the server is in the same state: the room does not exist. The response code changes from 204 to 404 but idempotency is defined by server state not the response code. So yes, my DELETE implementation is completely idempotent.

**Question: Explain the technical consequences if a client sends data in a 
different format such as text/plain or application/xml to an endpoint annotated 
with @Consumes(MediaType.APPLICATION_JSON).**


The Content-Type that JAX-RS will accept is specified by the @Consumes annotation. If a client sends a request with Content-Type: text/plain or application/xml instead of application/json, JAX-RS compares the incoming Content-Type header against the @Consumes constraint during request matching.

If there is no match , JAX-RS instantly rejects the request and returns HTTP 415 Unsupported Media Type. This happens at the framework level before my method code even runs. No data is process and no business logic is executed. This prevents the endpoint from receiving data in format it cannot dserialize.



**Question: Contrast @QueryParam with using the type as part of the URL path. 
Why is the query parameter approach generally considered superior for filtering?**

- Query parameter approach: GET /api/v1/sensors?type=CO2
- Path segment approach: GET /api/v1/sensors/type/CO2

The query parameter approach is superior for filtering since  query parameters 
are designed exactly for this purpose - narrowing or searching a collection. 
The resource being checked or accessed is still /api/v1/sensors regardless of the filter. 
The identity of the resource does not change .


 The path segment approach is incorrect in this instance because it suggest that CO2 is a named sub-resource that is present at that path, which is untrue. Additionally, a totally different path would be needed for the unfiltered scenario alone.


**Question: Discuss the architectural benefits of the Sub-Resource Locator 
pattern. How does delegating logic to separate classes help manage complexity 
compared to defining every nested path in one massive controller?**

Without the sub-resource locator pattern, every path including nested ones like 
/sensors/{id}/readings and /sensors/{id}/readings/{rid} would have to be defined 
in one controller class. As the API grows this becomes a massive unmanageable 
file with hundreds of methods all mixed together.

 
This is resolved by the sub - resource locator pattern, which has a method in SensorResource that only returns an instance of SensorReadingResource. All requests for readings are then assigned to that specific class using JAX-RS.

The benefits are:

- SensorReadingResource is a single class with a single responsiblity that only handles reading logic - one class, one responsibility.
 
- It does not require the entire SensorResource to be checked independently. Adding additional sub-resources such as alerts only requires one locator method and a new class.
 
- The primary SensorResource remains tidy and focuses on sensor operations only.


**Question: Why is HTTP 422 often considered more semantically accurate than 
404 when the issue is a missing reference inside a valid JSON payload?**

404 Not Found means the  URL that was requested is not present on the server. 
In this case the URL /api/v1/sensors is perfectly correct and does exist - so 
returning 404 would be misleading and technically incorrect.

The real issue is that the JSON  body's roomId value refers to an invalid room. The URL is fine , the JSON structure is fine but the content of the payload cannot be processed because of a broken reference.

HTTP 422 Unprocessable Entity is specifically designed for this situation - the request was received and understood but the payload's semantice errors prevented it from being handled. It provides the customer with a more accurate signal about exactly what went wrong and where to fix it .



**Question: From a cybersecurity standpoint, explain the risks of exposing 
internal Java stack traces to external API consumers.**

Exposing raw stack traces is dangerous because they reveal internal details 
that attackers can use to compromise the system:

 - Package structure and Class names reveal the internal architecture making it easier to plan targetted attacks.
 - The deployement directory structure is revealed via file system paths, which can help path traversal attacks.
 - By exposing business logic flow , the method call chain enables attackers to create malicious payloads that take advantage of particular code paths.

 In my API the GlobalExceptionMapper catches every unexpected exception  and returns only  a clean generic 500 message to the client. The full stack tace is logged server-side only where only developers can access it. This makes sure zero internal information / data ever reaches an attacker. 


 ## Project Structure

smart-campus-api/
├── pom.xml
└── src/main/java/com/smartcampus/
    ├── Main.java
    ├── application/
    │   └── SmartCampusApplication.java
    ├── exception/
    │   ├── ExceptionMappers.java
    │   ├── LinkedResourceNotFoundException.java
    │   ├── RoomNotEmptyException.java
    │   └── SensorUnavailableException.java
    ├── filter/
    │   └── LoggingFilter.java
    ├── model/
    │   ├── CampusSensor.java
    │   ├── Room.java
    │   └── SensorReading.java
    ├── resource/
    │   ├── DiscoveryResources.java
    │   ├── RoomResources.java
    │   ├── SensorReadingResource.java
    │   └── SensorResources.java
    └── store/
        └── DataStore.java
