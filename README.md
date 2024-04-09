# Data Analisys
Projeto simples em Java 21


# Basic documentation to run  Docker-compose

### Docker

Inside folder /docker, start docker-compose
```bash
docker-compose up -d 
```

List all images running
```bash
docker ps

#output
CONTAINER ID   IMAGE                   COMMAND                  CREATED         STATUS                            PORTS                                                                     NAMES
35ff1c9304ed   postgres                "docker-entrypoint.s…"   5 seconds ago   Up 5 seconds                      5432/tcp                                                                  docker_postgres_1
c953fb85d1ba   localstack/localstack   "docker-entrypoint.sh"   6 seconds ago   Up 5 seconds (health: starting)   0.0.0.0:4566->4566/tcp, 4510-4559/tcp, 5678/tcp, 0.0.0.0:5432->5432/tcp   docker_localstack_1

```


Enter inside docker-image 
```bash
docker exec -it  (CONTAINER_ID OR NAME) /bin/bash 
```

Show logs application
```bash
docker-compose logs IMAGE
```


Delete all images from docker
```bash
docker rmi $(docker images -a -q)
```
delete all volumes
```bash
docker volume rm $(docker volume ls -q)
```

### S3



Run those commands inside image to create bucket
```bash
chmod +x /scripts/init-s3.sh && ./scripts/init-s3.sh
```

List all buckets
```bash
awslocal s3 ls
```

### Postgresql

Run those commands inside image to create a table
```bash
chmod +x /scripts/init-db.sh && ./scripts/init-db.sh
```

List all databaes
```bash
psql -h localhost -p 5432 -U postgres -l
```

Connect to database
```bash
psql -h localhost -p 5432 -U postgres -W my_company
```
 
