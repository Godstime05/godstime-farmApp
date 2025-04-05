# Godstime Farms E-commerce Application

A full-featured e-commerce application built with Spring Boot, featuring JWT authentication, product management, cart functionality, and order processing.

## Features

- 🔐 **Secure Authentication**
  - JWT-based authentication
  - Role-based access control (USER, ADMIN)
  - Secure password hashing with BCrypt

- 🛍️ **Product Management**
  - Product listing and search
  - Product categories
  - Product details and images

- 🛒 **Shopping Cart**
  - Add/remove products
  - Update quantities
  - Cart persistence

- 📦 **Order Processing**
  - Order creation and management
  - Order history
  - Order status tracking

- 👤 **User Management**
  - User registration and login
  - Profile management
  - Role-based permissions

## Tech Stack

- **Backend**
  - Java 21
  - Spring Boot
  - Spring Security
  - JWT Authentication
  - Spring Data JPA
  - Maven

- **Database**
  - MySQL (configurable)

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven
- MySQL (or your preferred database)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Godstime05/godstime-farmApp.git
   ```

2. Navigate to the project directory:
   ```bash
   cd godstime-farmApp
   ```

3. Configure your database settings in `application.properties`

4. Build the project:
   ```bash
   mvn clean install
   ```

5. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### API Endpoints

#### Authentication
- `POST /api/v1/auth/register` - Register a new user
- `POST /api/v1/auth/login` - Login and get JWT token
- `POST /api/v1/auth/refresh` - Refresh JWT token

#### Products
- `GET /api/v1/products` - Get all products
- `GET /api/v1/products/{id}` - Get product by ID
- `POST /api/v1/products` - Create new product (ADMIN only)
- `PUT /api/v1/products/{id}` - Update product (ADMIN only)
- `DELETE /api/v1/products/{id}` - Delete product (ADMIN only)

#### Cart
- `GET /api/v1/cart` - Get user's cart
- `POST /api/v1/cart/add` - Add item to cart
- `PUT /api/v1/cart/update` - Update cart item
- `DELETE /api/v1/cart/remove/{id}` - Remove item from cart

#### Orders
- `GET /api/v1/orders` - Get user's orders
- `POST /api/v1/orders` - Create new order
- `GET /api/v1/orders/{id}` - Get order details

## Security

The application implements JWT-based authentication with the following security features:
- Token-based authentication
- Role-based authorization
- Secure password storage
- CSRF protection
- CORS configuration

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contact

Godstime - [@Godstime05](https://github.com/Godstime05)