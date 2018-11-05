# Smart Cookie Jar

This project consists of 3 parts:

- [Frontend](frontend/README.md)
- [Backend](backend/README.md)
- [NodeMCU](nodemcu/README.md)

## docker-compose
In order to run the whole application using docker-compose, you first have to run the following commands:

- In ~/frontend, you need to run `docker build . --tag scj_frontend:latest`
- In ~/backend, you need to run `docker build . --tag scj_backend:latest`

After that you can run `docker-compose up -d` in the root folder.