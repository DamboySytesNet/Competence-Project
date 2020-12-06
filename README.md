## Run Cassandra in Docker container
```
 docker run -p 9042:9042 cassandra:latest
```

## How to JavaFX
Żeby wyświetliło nam się GUI:
```
mvn clean javafx:run
```

### Dla ułatwienia w Intelliju:
1. Włączamy Run/Debug Configurations
1. Add new configuration
1. Maven
1. W `Command line:` wpisujemy `clean javafx:run`
1. Apply i OK
