#!/bin/bash

# Function to log messages with timestamp
log() {
    echo "[$(date +'%Y-%m-%d %H:%M:%S')] $1"
}

# Check if Docker is running
log "Checking Docker daemon..."
if ! docker info > /dev/null 2>&1; then
    log "Error: Docker daemon is not running"
    exit 1
fi

# Default profile if none supplied
PROFILE=${1:-default}
log "Using profile: $PROFILE"

# Check if .env exists
if [ ! -f .env ]; then
    log "Error: .env file not found"
    exit 1
fi

# Check if PostgreSQL container is already running
if docker compose ps postgres_scrapyscrapy | grep -q "running"; then
    log "PostgreSQL container is already running"
else
    # Start Docker containers
    log "Starting Docker containers..."
    docker compose up -d
    if [ $? -ne 0 ]; then
        log "Error: Failed to start Docker containers"
        exit 1
    fi
fi

# Wait for PostgreSQL to be ready
log "Checking PostgreSQL connection..."
for i in {1..30}; do
    if docker compose exec postgres_scrapyscrapy pg_isready -U ${DB_USERNAME:-postgres} > /dev/null 2>&1; then
        log "PostgreSQL is ready"
        break
    fi
    if [ $i -eq 30 ]; then
        log "Error: PostgreSQL failed to become ready"
        exit 1
    fi
    log "Waiting for PostgreSQL... ($i/30)"
    sleep 1
done

# Add after PostgreSQL ready check and before starting the app
log "Checking Flyway migrations..."
if [ ! -d "src/main/resources/db/migration" ]; then
    log "Creating Flyway migrations directory"
    mkdir -p src/main/resources/db/migration
fi

# Start the application
log "Starting application with profile(s): $PROFILE (Flyway enabled: ${PROFILE} == dev)"
if [ "$PROFILE" = "default" ]; then
    ./gradlew bootRun
else
    echo "Starting application with profile(s): $PROFILE"
    ./gradlew bootRun --args="--spring.profiles.active=$PROFILE --spring.flyway.clean-on-validation-error=true"
fi

log "Bootstrap complete" 