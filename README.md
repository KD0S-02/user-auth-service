# KDChat.io : USER Authentication

This README outlines the available endpoints for user authentication on KDChat.io, built using Spring Boot.

The Service is connected to a PostgreSQL Database thats run on port `5432`.


## Base URL 

By Default the application runs on port `8080`, this can be configured in `/src/main/resources/application.yml`

**Base URL** : `http://localhost:8080/api/v1/auth`

## Endpoints

### 1. **Sign In**

**Endpoint:** POST `/signin`

**Description:**  
Authenticates a user with their credentials. The **accessToken** and **refreshToken** (JWT Tokens) are sent as **HttpOnly cookies**, and the response body includes only the authenticated user's username.

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
  "username": "user-here"
}
```

**HttpOnly Cookies:**

* `accessToken`: Used for session management.

* `refreshToken`: Used to obtain a new access token when the current one expires.


**Status Codes:**

* `200 OK`: Authentication Successful

* `401 Unauthorized`: Invalid Credentials

* `400 Bad Request`: Missing or incorrect input


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


### 3. **Refresh Token**

**Endpoint:** POST `/refresh`

**Description:**  
Generates a new **accessToken**, which is sent as an **HttpOnly cookie**. The **refreshToken** is sent automatically as **HttpOnly cookie** in the request header.

**Request Body:**
None.

**Response:**
```json
{
  "username": "user-here"
}
```

**HttpOnly Cookies:**

* `accessToken:` Refreshed and sent back to the client.

**Status Codes:**

* `200 OK`: Token Refresh Successful

* `401 Unauthorized`: Invalid or expired refresh token
