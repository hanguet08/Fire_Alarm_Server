# Fire Alarm Server

Server hệ thống báo cháy cho nhà thông minh - Đồ án môn học Project 3

# Environment vars

This project uses the following environment variables:

| Name | Description          | Default Value |
| ---- | -------------------- | ------------- |
| CORS | Cors accepted values | "\*"          |

# Pre-requisites

- Install [Node.js](https://nodejs.org/en/)

# Getting started

- Clone the repository

```
git clone  <github template url> <project_name>
```

- Install dependencies

```
cd <project_name>
npm install
```

- Build and run the project

```
npm start
```

Navigate to `http://localhost:5000`

- API Document endpoints

  swagger Spec Endpoint : http://localhost:5000/api-docs

  swagger-ui Endpoint : http://localhost:5000/docs

# TypeScript + Node

The main purpose of this repository is to show a project setup and workflow for writing microservice. The Rest APIs will be using the Swagger (OpenAPI) Specification.

## Getting TypeScript

Add Typescript to project `npm`.

```
npm install -D typescript
```

# Chuẩn bị môi trường

Sử dụng Firebase cloud message

- Hướng dẫn setup :
  - https://firebase.google.com/docs/cloud-messaging/js/client
  - https://firebase.google.com/docs/admin/setup

Sử dụng MQTT :

- Thay đổi thông số của mqttClient (username, password)
- Sử dụng Extensions MQTTBox của Chrome để tương tác
