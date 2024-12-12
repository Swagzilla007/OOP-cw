# Ticketing Software System

A full-stack ticketing system with a Spring Boot backend and React frontend.

## Project Structure

- `oop backend/cw_20234071/` - Spring Boot backend application
- `ticketing-frontend/` - React.js frontend application

## Prerequisites

- Java JDK 17 or higher
- Node.js 16.x or higher
- Maven 3.x
- MySQL 8.x

## Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd "oop backend/cw_20234071"
   ```

2. Install Maven dependencies:
   ```bash
   mvn clean install
   ```

3. Configure your MySQL database connection in `src/main/resources/application.properties`

4. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

The backend server will start on `http://localhost:8080`

## Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd ticketing-frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

The frontend application will be available at `http://localhost:3002`

## API Endpoints

### Customer Endpoints
- `GET /api/customers` - Get all customers
- `POST /api/customers` - Create a new customer
- `GET /api/customers/{id}` - Get customer by ID
- `PUT /api/customers/{id}` - Update customer
- `DELETE /api/customers/{id}` - Delete customer

### Vendor Endpoints
- `GET /api/vendors` - Get all vendors
- `POST /api/vendors` - Create a new vendor
- `GET /api/vendors/{id}` - Get vendor by ID
- `PUT /api/vendors/{id}` - Update vendor
- `DELETE /api/vendors/{id}` - Delete vendor

## Architecture and Design Decisions

### Backend Architecture (`Cw20234071Application.java`)

The backend follows a layered architecture with clear separation of concerns:

1. **Spring Boot Application Structure**
   - Uses `@SpringBootApplication` for auto-configuration and component scanning
   - Implements dependency injection for loose coupling between components
   - Services are designed to be stateless for better scalability

2. **Repository Pattern**
   - Uses Spring Data JPA for data access
   - Abstracts database operations behind repository interfaces
   - Enables easy switching between different database implementations

3. **Service Layer**
   - Contains business logic
   - Implements transaction management
   - Handles data validation and transformation

4. **Controller Layer**
   - REST controllers for HTTP endpoint handling
   - DTO pattern for data transfer
   - Clear separation between API contracts and implementations

### Frontend Architecture (`App.jsx`)

The React frontend is built with modern practices:

1. **Component Structure**
   - Uses functional components with hooks
   - Implements React Router for client-side routing
   - Material-UI (MUI) for consistent design system

2. **Routing Design**
   - Centralized routing in `App.jsx`
   - Lazy loading for route components
   - Default route redirects to Customers page

3. **State Management**
   - Uses React's built-in state management
   - Implements prop drilling for shallow component trees
   - API calls handled in component-specific hooks

4. **UI/UX Decisions**
   - Responsive layout using MUI's Box and Container
   - Consistent navigation through AppBar
   - Clear visual hierarchy with Typography components

## Technologies Used

### Backend
- Spring Boot
- Spring Data JPA
- MySQL
- Maven

### Frontend
- React.js
- Vite
- React Router
- Axios

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
