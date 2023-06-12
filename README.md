# Drones Management API

This project is a RESTful API for managing drones and their payload in a delivery system. The API enables the creation and management of drones, loading them with medication items, querying the status of drones and the loads they are carrying, and checking their battery levels.

## System Requirements

1. Java 17
2. Maven
3. Spring Boot

## Getting Started

1. Clone the repository to your local machine.
2. Navigate to the project directory in your terminal.
3. Run the project with the following command: `mvn spring-boot:run`.

## Endpoints

- `POST /drone`: Register a new drone.

  Request body:
    ```
    {
        "serialNumber": "<drone_serial_number>",
        "model": "<drone_model>",
        "weightLimit": <drone_weight_limit>,
        "batteryCapacity": <drone_battery>
    }
    ```

- `POST /drone/load/{drone_id}`: Load a payload to a drone.

  Path variable:
    ```
    drone_id: ID of the drone to load the payload
    ```

  Form-Data:
    ```
        "name": "<medication_name>",
        "payloadType": <payload_type>,
        "weight": <medication_weight>,
        "code": "<medication_code>",
        "uploadImage": <image>
    ```

- `GET /drone/payloads/{id}`: Check loaded medication items for a given drone.

  Path variable:
    ```
    id: ID of the drone
    ```

- `GET /drone/available`: Check available drones for loading.

- `GET /drone/battery/{id}`: Check drone battery level for a given drone.

  Path variable:
    ```
    id: ID of the drone
    ```

## Built With

- Java 17
- Spring Boot
- Hibernate
- H2 Database

## Testing

You can run the unit tests for this project by running the following command in the project's root directory:

`mvn test`

## API Postman Collection

`Drones.postman_collection.json` is included in the project directory

## Author

- Umesh Giri
