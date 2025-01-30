# ScrapyScrapy Application

A Spring Boot application using WebFlux, R2DBC, and PostgreSQL.


the ongoing todo list is [here](./task-checklist.md)

the app overview is [here](./overview.md)

## Prerequisites

- Java Graal 21 or later as an available toolchain
  -  check with **.\gradlew -q javaToolchains**
  -  set environment variable **GRAALJDK** if Graal jdk is in a non standard location 
- Docker and Docker Compose
- Gradle (wrapper included)

## Setup

TODO toolchainfix .\gradlew -q javaToolchains

1. Clone the repository:

```bash
git clone https://github.com/maccalsa/scrapyscrapy.git

cd scrapyscrapy
```

2. Copy the environment file:

```bash
cp .env.example .env
```

3. Configure your environment variables in `.env` file:

```
# Database
DB_USERNAME=postgres
DB_PASSWORD=postgres
DB_PORT=5432
DB_HOST=localhost
DB_NAME=scrapyscrapy

# JWT
JWT_SECRET=your-256-bit-secret
JWT_EXPIRATION=86400000

# Security
SECURITY_USERNAME=admin
SECURITY_PASSWORD=secret

# Flyway
FLYWAY_ENABLED=false

```

## Running the Application

Use the `bootstrap.sh` script to start the application:

```bash
# Make the script executable
chmod +x bootstrap.sh

# Run with default profile
./bootstrap.sh

# Run with dev profile (enables Flyway migrations)
./bootstrap.sh dev
```

Note that the Fly migrations will only work if the profile is dev and the `FLYWAY_ENABLED` property is true

The bootstrap script will:
1. Check for Docker daemon
2. Start PostgreSQL container if not running
3. Wait for database to be ready
4. Start the Spring Boot application
5. Run migrations is setup to.

## API Documentation

Once running, access the Swagger UI at:
- http://localhost:8080/swagger-ui.html

## Health Check

Access the actuator endpoints at:
- http://localhost:8080/actuator/health

## Security

The application uses Basic Auth for security:
- Default username: admin (or what you have defined)
- Default password: secret (or what you have defined)

## Database Migrations

Flyway migrations are disabled by default. To enable:
1. Set `FLYWAY_ENABLED=true` in `.env`
2. Run with dev profile: `./bootstrap.sh dev`

## Development

The project uses:
- Spring Boot 3.x
- Spring WebFlux
- R2DBC for reactive database access
- Spring Security
- Flyway for database migrations
- Spring Modulith for modular architecture
