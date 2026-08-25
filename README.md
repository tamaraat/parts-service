# AutoCare Parts Service

AutoCare Parts Service is a REST microservice responsible for managing spare parts and inventory for the AutoCare application.

The service runs independently from the main AutoCare application and exposes a REST API that is consumed by the main application.

## Technology Stack

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Validation
- Spring Cache
- Spring Scheduling
- MySQL
- Maven
- JUnit 5
- Mockito
- MockMvc

## Database

The microservice uses a separate MySQL database:

`autocare_parts_db`

All spare parts use UUID identifiers.

## Domain Entity

### SparePart

A spare part contains:

- UUID id
- name
- part number
- quantity
- price

## REST API

Base endpoint:

`/api/parts`

### Get all spare parts

`GET /api/parts`

Returns all available spare parts.

### Create spare part

`POST /api/parts`

Creates a new spare part.

### Update spare part

`PUT /api/parts/{id}`

Updates the information of an existing spare part.

### Update stock

`PUT /api/parts/{id}/stock`

Changes the available quantity of a spare part.

### Delete spare part

`DELETE /api/parts/{id}`

Deletes a spare part.

## Functionalities

The microservice supports the following domain functionalities:

1. Creating a spare part
2. Editing a spare part
3. Updating spare part stock
4. Deleting a spare part
5. Retrieving all spare parts

The modifying REST endpoints are invoked by the AutoCare main application through a Feign Client.

## Validation

Incoming requests are validated before they are processed.

Validation includes:

- required part name
- valid part number
- non-negative quantity
- positive price
- duplicate part number prevention

## Error Handling

The service provides centralized REST exception handling.

It handles:

- missing spare parts
- invalid operations
- invalid request data

Meaningful HTTP responses are returned instead of default error pages.

## Caching

Spring Cache is enabled for spare part retrieval.

The spare parts cache is invalidated whenever a spare part is:

- created
- updated
- deleted
- restocked

## Scheduling

The service contains two scheduled jobs.

### Automatic inventory restocking

A cron-based scheduled task runs every day at 03:00.

Spare parts with very low stock are automatically restocked.

### Cache refresh

A fixed-delay scheduled task periodically clears the spare parts cache.

## Logging

Domain operations contain logging, including:

- spare part creation
- spare part update
- stock update
- spare part deletion
- automatic inventory restocking
- scheduled cache refresh

## Testing

The project contains:

- Unit tests with JUnit and Mockito
- Integration tests with Spring Boot
- REST API tests with MockMvc

Tests can be executed with:

```bash
./mvnw test