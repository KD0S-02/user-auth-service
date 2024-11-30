# KDChat.io : USER Authentication

This README outlines the available endpoints for user authentication on KDChat.io, built using Spring Boot.

The Service is connected a PostgreSQL Database thats run on port `5432`.


## Base URL 

By Default the application runs on port `8080`, this can be configured in `/src/main/resources/application.yml`

**Base URL** : `http://localhost:8080/api/v1`

## Endpoints

### 1. **Sign In**

**Endpoint:** POST `/signin`

**Description:**  
Authenticates a user with their credentials and returns two JWT tokens (accessToken and refreshToken) for session management.

**Request Body:**
```json
{
  "username": "user123",
  "password": "password123"
}
```

**Response:**
```json
{
  "accessToken": "secure-access-token",
  "refreshToken": "secure-refresh-token"
}
```
**Status Codes:**

* `200 OK`: Authentication Successful

* `401 Unauthorized`: Invalid Credentials

* `400 Bad Request`: Missing or incorrect input


### /api/v1/signup

### 2. **Sign Up**

**Endpoint:** POST `/signup`

**Description:**  
Registers a new user account in the system. The role field can either be "USER" or "GUEST", where:

    "USER": Regular authenticated user.
    "GUEST": Temporary user with limited access.

**Request Body:**
```json
{
  "username": "user123",
  "password": "password123",
  "email": "user@mail.com",
  "role": "USER"
}
```
**Status Codes:**

* `200 OK`: User registration successful

* `400 Bad Request`: Invalid input or user already exists


### /api/v1/refresh

### 3. **Refresh Token**

**Endpoint:** POST `/refresh`

**Description:**  
Refreshes the user's accessToken when it is about to expire.

**Request Body:**
```json
{
  "token": "secure-refresh-token-here",
}
```

**Response:**
```json
{
  "accessToken": "secure-access-token",
  "refreshToken": "secure-refresh-token"
}
```
**Status Codes:**

* `200 OK`: Token Refresh Successful

* `401 Unauthorized`: Invalid or expired refresh token
