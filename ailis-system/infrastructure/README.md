# AILIS Infrastructure

This directory contains infrastructure configuration for the AILIS system.

## Components

### Docker Compose Services

1. **PostgreSQL** (port 5432)
   - Database: `ailis`
   - User: `ailis`
   - Password: `ailis1234`

2. **Redis** (port 6379)
   - In-memory cache and message broker

3. **Kafka** (ports 9092, 9094)
   - Message streaming platform
   - Internal: `kafka:9092`
   - External: `localhost:9094`

4. **Zookeeper** (port 2181)
   - Kafka coordination service

5. **Prometheus** (port 9090)
   - Metrics collection and monitoring
   - Web UI: http://localhost:9090

6. **Grafana** (port 3001)
   - Metrics visualization
   - Web UI: http://localhost:3001
   - Default credentials: admin/admin1234

## Quick Start

### Start all services

```bash
cd ailis-system
docker-compose up -d
```

### Stop all services

```bash
docker-compose down
```

### Stop and remove volumes

```bash
docker-compose down -v
```

### View logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f postgres
docker-compose logs -f kafka
```

### Check service health

```bash
docker-compose ps
```

## Database Setup

After starting PostgreSQL, the Spring Boot application will automatically run Flyway migrations to create the schema and insert test data.

You can also connect to the database directly:

```bash
docker exec -it ailis-postgres psql -U ailis -d ailis
```

## Monitoring Setup

### Prometheus

1. Access Prometheus UI: http://localhost:9090
2. Check targets: http://localhost:9090/targets
3. The `cancerch-service` should appear when the backend is running

### Grafana

1. Access Grafana UI: http://localhost:3001
2. Login with `admin/admin1234`
3. Prometheus datasource is pre-configured
4. Create dashboards or import Spring Boot dashboard (ID: 12900)

## Kafka Topics

The following topics are auto-created:

- `worklist.created` - Worklist creation events
- `worklist.updated` - Worklist update events
- `worklist.status.changed` - Status change events

### Kafka Console Consumer (for testing)

```bash
docker exec -it ailis-kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic worklist.created \
  --from-beginning
```

## Troubleshooting

### PostgreSQL connection issues

```bash
# Check if PostgreSQL is ready
docker exec ailis-postgres pg_isready -U ailis

# Reset PostgreSQL data
docker-compose down -v
docker-compose up -d postgres
```

### Kafka connection issues

```bash
# Check Kafka broker status
docker exec ailis-kafka kafka-broker-api-versions --bootstrap-server localhost:9092

# List topics
docker exec ailis-kafka kafka-topics --list --bootstrap-server localhost:9092
```

### Viewing metrics

```bash
# Check if metrics endpoint is accessible
curl http://localhost:8080/actuator/prometheus
```

## Production Considerations

For production deployments:

1. **Security**
   - Change default passwords
   - Enable SSL/TLS for PostgreSQL
   - Configure Kafka authentication (SASL/SCRAM)
   - Set up proper network segmentation

2. **Performance**
   - Adjust PostgreSQL shared_buffers and work_mem
   - Configure Kafka broker settings for throughput
   - Set up Redis clustering

3. **Monitoring**
   - Set up alerting rules in Prometheus
   - Configure Grafana dashboards
   - Enable log aggregation (ELK stack)

4. **Backup**
   - Configure PostgreSQL WAL archiving
   - Set up automated backups
   - Test restore procedures
