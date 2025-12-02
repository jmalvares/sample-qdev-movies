# Movie Service - Spring Boot Demo Application

A simple movie catalog web application built with Spring Boot, demonstrating Java application development best practices with a fun pirate theme! 🏴‍☠️

## Features

- **Movie Catalog**: Browse 12 classic movies with detailed information
- **Movie Details**: View comprehensive information including director, year, genre, duration, and description
- **🆕 Movie Search & Filtering**: Search for treasure (movies) by name, ID, or genre with pirate-themed interface
- **🆕 REST API**: JSON API endpoints for programmatic access to movie search functionality
- **Customer Reviews**: Each movie includes authentic customer reviews with ratings and avatars
- **Responsive Design**: Mobile-first design that works on all devices
- **Modern UI**: Dark theme with gradient backgrounds and smooth animations

## Technology Stack

- **Java 8**
- **Spring Boot 2.0.5**
- **Maven** for dependency management
- **Log4j 2.20.0**
- **JUnit 5.8.2**
- **Thymeleaf** for templating

## Quick Start

### Prerequisites

- Java 8 or higher
- Maven 3.6+

### Run the Application

```bash
git clone https://github.com/<youruser>/sample-qdev-movies.git
cd sample-qdev-movies
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access the Application

- **Movie List**: http://localhost:8080/movies
- **Movie Details**: http://localhost:8080/movies/{id}/details (where {id} is 1-12)
- **🆕 Movie Search**: Use the search form on the movies page or API endpoints below

## Building for Production

```bash
mvn clean package
java -jar target/sample-qdev-movies-0.1.0.jar
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/amazonaws/samples/qdevmovies/
│   │       ├── movies/
│   │       │   ├── MoviesApplication.java    # Main Spring Boot application
│   │       │   ├── MoviesController.java     # REST controller for movie endpoints
│   │       │   ├── MovieService.java         # Business logic for movie operations
│   │       │   ├── Movie.java                # Movie data model
│   │       │   ├── Review.java               # Review data model
│   │       │   └── ReviewService.java        # Review business logic
│   │       └── utils/
│   │           ├── MovieIconUtils.java       # Movie icon utilities
│   │           └── MovieUtils.java           # Movie validation utilities
│   └── resources/
│       ├── application.yml                   # Application configuration
│       ├── movies.json                       # Movie catalog data
│       ├── mock-reviews.json                 # Mock review data
│       ├── log4j2.xml                        # Logging configuration
│       ├── templates/                        # Thymeleaf HTML templates
│       │   ├── movies.html                   # Movie list with search form
│       │   └── movie-details.html            # Movie details page
│       └── static/css/                       # Stylesheets
│           ├── movies.css                    # Main movie page styles
│           └── movie-details.css             # Movie details styles
└── test/                                     # Unit tests
    └── java/
        └── com/amazonaws/samples/qdevmovies/movies/
            ├── MovieServiceTest.java         # Service layer tests
            ├── MoviesControllerTest.java     # Controller tests
            └── MovieTest.java                # Model tests
```

## API Endpoints

### Get All Movies (HTML)
```
GET /movies
```
Returns an HTML page displaying all movies with ratings, basic information, and a search form.

### Get Movie Details (HTML)
```
GET /movies/{id}/details
```
Returns an HTML page with detailed movie information and customer reviews.

**Parameters:**
- `id` (path parameter): Movie ID (1-12)

**Example:**
```
http://localhost:8080/movies/1/details
```

### 🆕 Search Movies (JSON API)
```
GET /movies/search
```
Ahoy matey! This be the treasure hunting endpoint for finding yer movies programmatically!

**Query Parameters:**
- `name` (optional): Movie name to search for (partial match, case-insensitive)
- `id` (optional): Specific movie ID to find
- `genre` (optional): Genre to filter by (case-insensitive)

**Response Format:**
```json
{
  "success": true,
  "message": "Ahoy! Found 2 movies matching yer search!",
  "totalResults": 2,
  "movies": [
    {
      "id": 1,
      "movieName": "The Prison Escape",
      "director": "John Director",
      "year": 1994,
      "genre": "Drama",
      "description": "Two imprisoned men bond over a number of years...",
      "duration": 142,
      "imdbRating": 5.0,
      "icon": "🎬"
    }
  ]
}
```

**Examples:**
```bash
# Search by movie name
curl "http://localhost:8080/movies/search?name=prison"

# Search by genre
curl "http://localhost:8080/movies/search?genre=drama"

# Search by ID
curl "http://localhost:8080/movies/search?id=1"

# Multiple criteria (AND logic)
curl "http://localhost:8080/movies/search?name=family&genre=crime"
```

### 🆕 Search Movies (HTML Form)
```
GET /movies/search/form
```
This be the route for landlubbers using the search form! Returns HTML with search results.

**Query Parameters:** Same as JSON API above

**Example:**
```
http://localhost:8080/movies/search/form?name=action&genre=sci-fi
```

## Search Features

### 🔍 Search Capabilities
- **Name Search**: Partial, case-insensitive matching on movie titles
- **ID Search**: Exact match on movie ID
- **Genre Search**: Partial, case-insensitive matching on genres
- **Multi-criteria**: Combine multiple search parameters (AND logic)
- **Pirate Language**: Fun pirate-themed messages and interface elements

### 🎯 Search Examples
- Search for "The" to find all movies with "The" in the title
- Search for "Drama" to find all drama movies
- Search for "Crime" to find movies with crime elements
- Use ID search to find a specific movie quickly

### 🚫 Error Handling
- Invalid IDs return appropriate error messages
- Empty search results show helpful pirate-themed messages
- Malformed requests are handled gracefully
- All errors include pirate language for consistency

## Testing

Run the comprehensive test suite:

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=MovieServiceTest

# Run with coverage
mvn test jacoco:report
```

### Test Coverage
- **MovieService**: Search functionality, edge cases, validation
- **MoviesController**: API endpoints, form handling, error scenarios
- **Integration Tests**: End-to-end search workflows

## Troubleshooting

### Port 8080 already in use

Run on a different port:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Build failures

Clean and rebuild:
```bash
mvn clean compile
```

### Search not working

1. Check that movies.json is properly loaded
2. Verify search parameters are correctly formatted
3. Check application logs for detailed error messages

## Contributing

This project is designed as a demonstration application. Feel free to:
- Add more movies to the catalog
- Enhance the UI/UX with more pirate elements
- Add new search features (rating range, year range, etc.)
- Improve the responsive design
- Add more comprehensive error handling
- Extend the API with additional endpoints

### Development Guidelines
- Follow existing pirate theme in user-facing messages
- Maintain comprehensive test coverage
- Use proper JavaDoc comments with pirate flair
- Follow Spring Boot best practices
- Ensure responsive design for all new features

## License

This sample code is licensed under the MIT-0 License. See the LICENSE file.

---

*Arrr! May yer code be bug-free and yer deployments smooth sailing! 🏴‍☠️*
