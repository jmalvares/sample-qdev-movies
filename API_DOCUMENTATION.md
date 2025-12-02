# Movie Search API Documentation 🏴‍☠️

Ahoy matey! Welcome to the comprehensive API documentation for the Movie Search functionality. This treasure map will guide ye through all the endpoints and their usage!

## Overview

The Movie Search API provides powerful search and filtering capabilities for the movie catalog. All endpoints support both JSON responses for programmatic access and HTML responses for web interface integration.

## Base URL

```
http://localhost:8080
```

## Authentication

No authentication required - this be open waters for all treasure hunters!

## Endpoints

### 1. Search Movies (JSON API)

**Endpoint:** `GET /movies/search`

**Description:** Ahoy! This be the main treasure hunting endpoint for finding movies programmatically. Returns JSON response with search results.

#### Request Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `name` | String | No | Movie name to search for (partial match, case-insensitive) |
| `id` | Long | No | Specific movie ID to find (exact match) |
| `genre` | String | No | Genre to filter by (partial match, case-insensitive) |

#### Response Format

```json
{
  "success": boolean,
  "message": "string",
  "totalResults": number,
  "movies": [
    {
      "id": number,
      "movieName": "string",
      "director": "string",
      "year": number,
      "genre": "string",
      "description": "string",
      "duration": number,
      "imdbRating": number,
      "icon": "string"
    }
  ]
}
```

#### Success Response (200 OK)

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
      "description": "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
      "duration": 142,
      "imdbRating": 5.0,
      "icon": "🎬"
    },
    {
      "id": 5,
      "movieName": "Life Journey",
      "director": "Robert Filmmaker",
      "year": 1994,
      "genre": "Drama/Romance",
      "description": "The presidencies of Kennedy and Johnson, the Vietnam War, and other historical events unfold from the perspective of an Alabama man with an IQ of 75.",
      "duration": 142,
      "imdbRating": 4.0,
      "icon": "🎬"
    }
  ]
}
```

#### Empty Results Response (200 OK)

```json
{
  "success": true,
  "message": "Arrr! No treasure found with those search criteria, me hearty! Try different terms.",
  "totalResults": 0,
  "movies": []
}
```

#### Error Response (400 Bad Request)

```json
{
  "success": false,
  "message": "Arrr! That ID be invalid, matey! Must be a positive number.",
  "movies": []
}
```

#### Server Error Response (500 Internal Server Error)

```json
{
  "success": false,
  "message": "Arrr! Something went wrong while searching for treasure. Try again later, matey!",
  "movies": []
}
```

#### Examples

**Search by movie name:**
```bash
curl "http://localhost:8080/movies/search?name=prison"
```

**Search by genre:**
```bash
curl "http://localhost:8080/movies/search?genre=drama"
```

**Search by ID:**
```bash
curl "http://localhost:8080/movies/search?id=1"
```

**Multiple criteria (AND logic):**
```bash
curl "http://localhost:8080/movies/search?name=family&genre=crime"
```

**Case-insensitive search:**
```bash
curl "http://localhost:8080/movies/search?name=PRISON&genre=DRAMA"
```

### 2. Search Movies (HTML Form)

**Endpoint:** `GET /movies/search/form`

**Description:** This be the route for landlubbers using the web interface! Returns HTML page with search results and form.

#### Request Parameters

Same parameters as JSON API above.

#### Response

Returns HTML page with:
- Search form with preserved input values
- Search results displayed as movie cards
- Success/error messages with pirate theme
- All genres available in dropdown

#### Examples

**Search via form:**
```
http://localhost:8080/movies/search/form?name=action&genre=sci-fi
```

**Direct form access:**
```
http://localhost:8080/movies/search/form
```

### 3. Get All Movies with Search Form

**Endpoint:** `GET /movies`

**Description:** Enhanced movies listing page that now includes the search form interface.

#### Response

Returns HTML page with:
- Complete movie catalog
- Interactive search form
- Genre dropdown populated with all available genres
- Responsive design for all devices

## Search Logic

### Name Search
- **Type:** Partial match
- **Case Sensitivity:** Case-insensitive
- **Examples:**
  - `name=prison` matches "The Prison Escape"
  - `name=THE` matches all movies with "The" in the title
  - `name=family` matches "The Family Boss"

### Genre Search
- **Type:** Partial match
- **Case Sensitivity:** Case-insensitive
- **Examples:**
  - `genre=drama` matches "Drama" and "Crime/Drama"
  - `genre=crime` matches "Crime/Drama" and "Action/Crime"
  - `genre=sci` matches "Action/Sci-Fi"

### ID Search
- **Type:** Exact match
- **Validation:** Must be positive integer
- **Examples:**
  - `id=1` matches movie with ID 1 exactly
  - `id=0` returns error (invalid)
  - `id=-1` returns error (invalid)

### Multiple Criteria
- **Logic:** AND operation (all criteria must match)
- **Examples:**
  - `name=family&genre=crime` finds movies containing "family" in name AND "crime" in genre
  - `id=1&name=prison` finds movie with ID 1 AND containing "prison" in name

## Error Handling

### Client Errors (4xx)

| Error | HTTP Code | Condition | Message |
|-------|-----------|-----------|---------|
| Invalid ID | 400 | ID ≤ 0 | "Arrr! That ID be invalid, matey! Must be a positive number." |

### Server Errors (5xx)

| Error | HTTP Code | Condition | Message |
|-------|-----------|-----------|---------|
| Internal Error | 500 | Unexpected exception | "Arrr! Something went wrong while searching for treasure. Try again later, matey!" |

## Rate Limiting

Currently no rate limiting is implemented. All requests are welcome aboard!

## Data Format

### Movie Object

| Field | Type | Description |
|-------|------|-------------|
| `id` | Long | Unique movie identifier (1-12) |
| `movieName` | String | Full movie title |
| `director` | String | Director's name |
| `year` | Integer | Release year |
| `genre` | String | Movie genre(s), may contain multiple separated by "/" |
| `description` | String | Movie plot description |
| `duration` | Integer | Runtime in minutes |
| `imdbRating` | Double | Rating out of 5.0 |
| `icon` | String | Movie icon emoji |

### Available Genres

The following genres are available in the current movie catalog:

- Action/Crime
- Action/Sci-Fi
- Adventure/Fantasy
- Adventure/Sci-Fi
- Crime/Drama
- Drama
- Drama/History
- Drama/Romance
- Drama/Thriller

## Performance Considerations

- **In-Memory Search:** All searches are performed on in-memory data for fast response times
- **No Pagination:** All matching results are returned in a single response
- **Case-Insensitive:** String comparisons use lowercase conversion for performance

## Testing

### Unit Tests

Comprehensive test coverage includes:
- All search parameter combinations
- Edge cases (empty strings, null values, invalid IDs)
- Error scenarios
- Case-insensitive matching
- Multiple criteria logic

### Integration Tests

End-to-end testing covers:
- API endpoint responses
- HTML form functionality
- Error handling workflows
- Response format validation

## Changelog

### Version 1.0.0 (Current)
- ✅ Added movie search by name, ID, and genre
- ✅ JSON API endpoint with comprehensive error handling
- ✅ HTML form interface with pirate theme
- ✅ Case-insensitive search functionality
- ✅ Multiple criteria support with AND logic
- ✅ Comprehensive test coverage
- ✅ Pirate-themed user messages

## Future Enhancements

Potential treasure to be added in future versions:
- 🔮 Search by rating range
- 🔮 Search by year range
- 🔮 Search by duration range
- 🔮 Pagination for large result sets
- 🔮 Sorting options (by rating, year, name)
- 🔮 Advanced search with OR logic
- 🔮 Search result highlighting
- 🔮 Search history and favorites

---

*Arrr! May yer API calls be swift and yer JSON responses be treasure-filled! 🏴‍☠️*

## Support

If ye be having trouble with the API, check the application logs or raise an issue in the repository. The crew be always ready to help fellow treasure hunters!