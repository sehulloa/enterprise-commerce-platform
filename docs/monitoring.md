# Monitoring

## Overview

The platform uses:

- Spring Boot Actuator
- Micrometer
- Prometheus
- Grafana

---

## Endpoints

- /actuator/health
- /actuator/info
- /actuator/prometheus

---

## Prometheus

URL:
http://localhost:9090

---

## Grafana

URL:
http://localhost:3000

Default credentials:
admin / admin

---

## Metrics examples

- jvm_memory_used_bytes
- http_server_requests_seconds_count
- process_cpu_usage