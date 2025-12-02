package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for MoviesController.
 * Arrr! These tests be making sure our movie treasure hunting endpoints work like a charm!
 */
public class MoviesControllerTest {

    private MoviesController moviesController;
    private Model model;
    private MockMovieService mockMovieService;
    private MockReviewService mockReviewService;

    // Test data - Ahoy! Our treasure chest of test movies!
    private static final Movie TEST_MOVIE_1 = new Movie(1L, "Test Movie", "Test Director", 2023, "Drama", "Test description", 120, 4.5);
    private static final Movie TEST_MOVIE_2 = new Movie(2L, "Action Movie", "Action Director", 2022, "Action", "Action description", 110, 4.0);
    private static final Movie TEST_MOVIE_3 = new Movie(3L, "Comedy Movie", "Comedy Director", 2021, "Comedy", "Comedy description", 95, 3.5);

    @BeforeEach
    public void setUp() {
        moviesController = new MoviesController();
        model = new ExtendedModelMap();
        
        // Create mock services with test data
        mockMovieService = new MockMovieService();
        mockReviewService = new MockReviewService();
        
        // Inject mocks using reflection
        try {
            java.lang.reflect.Field movieServiceField = MoviesController.class.getDeclaredField("movieService");
            movieServiceField.setAccessible(true);
            movieServiceField.set(moviesController, mockMovieService);
            
            java.lang.reflect.Field reviewServiceField = MoviesController.class.getDeclaredField("reviewService");
            reviewServiceField.setAccessible(true);
            reviewServiceField.set(moviesController, mockReviewService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock services", e);
        }
    }

    @Test
    @DisplayName("Should get all movies and genres")
    public void testGetMovies() {
        String result = moviesController.getMovies(model);
        
        assertNotNull(result, "Result should not be null");
        assertEquals("movies", result, "Should return movies template");
        
        // Verify model attributes
        assertTrue(model.containsAttribute("movies"), "Model should contain movies");
        assertTrue(model.containsAttribute("genres"), "Model should contain genres");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertEquals(3, movies.size(), "Should have 3 test movies");
    }

    @Test
    @DisplayName("Should get movie details for valid ID")
    public void testGetMovieDetails() {
        String result = moviesController.getMovieDetails(1L, model);
        
        assertNotNull(result, "Result should not be null");
        assertEquals("movie-details", result, "Should return movie-details template");
        
        assertTrue(model.containsAttribute("movie"), "Model should contain movie");
        assertTrue(model.containsAttribute("movieIcon"), "Model should contain movieIcon");
        assertTrue(model.containsAttribute("allReviews"), "Model should contain allReviews");
    }

    @Test
    @DisplayName("Should return error for non-existent movie ID")
    public void testGetMovieDetailsNotFound() {
        String result = moviesController.getMovieDetails(999L, model);
        
        assertNotNull(result, "Result should not be null");
        assertEquals("error", result, "Should return error template");
        
        assertTrue(model.containsAttribute("title"), "Model should contain error title");
        assertTrue(model.containsAttribute("message"), "Model should contain error message");
    }

    // API Search Tests - Arrr! Testing our treasure hunting API!

    @Test
    @DisplayName("API: Should search movies by name")
    public void testSearchMoviesApiByName() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi("Test", null, null);
        
        assertEquals(200, response.getStatusCodeValue(), "Should return OK status");
        
        Map<String, Object> body = response.getBody();
        assertNotNull(body, "Response body should not be null");
        assertTrue((Boolean) body.get("success"), "Response should be successful");
        assertEquals(1, body.get("totalResults"), "Should find 1 movie");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) body.get("movies");
        assertEquals(1, movies.size(), "Should return 1 movie");
        assertEquals("Test Movie", movies.get(0).getMovieName(), "Should return correct movie");
    }

    @Test
    @DisplayName("API: Should search movies by ID")
    public void testSearchMoviesApiById() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi(null, 2L, null);
        
        assertEquals(200, response.getStatusCodeValue(), "Should return OK status");
        
        Map<String, Object> body = response.getBody();
        assertTrue((Boolean) body.get("success"), "Response should be successful");
        assertEquals(1, body.get("totalResults"), "Should find 1 movie");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) body.get("movies");
        assertEquals("Action Movie", movies.get(0).getMovieName(), "Should return correct movie");
    }

    @Test
    @DisplayName("API: Should search movies by genre")
    public void testSearchMoviesApiByGenre() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi(null, null, "Comedy");
        
        assertEquals(200, response.getStatusCodeValue(), "Should return OK status");
        
        Map<String, Object> body = response.getBody();
        assertTrue((Boolean) body.get("success"), "Response should be successful");
        assertEquals(1, body.get("totalResults"), "Should find 1 movie");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) body.get("movies");
        assertEquals("Comedy Movie", movies.get(0).getMovieName(), "Should return correct movie");
    }

    @Test
    @DisplayName("API: Should return empty results with pirate message")
    public void testSearchMoviesApiNoResults() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi("NonExistent", null, null);
        
        assertEquals(200, response.getStatusCodeValue(), "Should return OK status");
        
        Map<String, Object> body = response.getBody();
        assertTrue((Boolean) body.get("success"), "Response should be successful");
        assertEquals(0, body.get("totalResults"), "Should find 0 movies");
        
        String message = (String) body.get("message");
        assertTrue(message.contains("Arrr!"), "Should contain pirate language");
        assertTrue(message.contains("treasure"), "Should contain treasure reference");
    }

    @Test
    @DisplayName("API: Should return error for invalid ID")
    public void testSearchMoviesApiInvalidId() {
        ResponseEntity<Map<String, Object>> response = moviesController.searchMoviesApi(null, -1L, null);
        
        assertEquals(400, response.getStatusCodeValue(), "Should return Bad Request status");
        
        Map<String, Object> body = response.getBody();
        assertFalse((Boolean) body.get("success"), "Response should not be successful");
        
        String message = (String) body.get("message");
        assertTrue(message.contains("Arrr!"), "Should contain pirate language");
        assertTrue(message.contains("invalid"), "Should mention invalid ID");
    }

    @Test
    @DisplayName("Form: Should search movies by name")
    public void testSearchMoviesFormByName() {
        String result = moviesController.searchMoviesForm("Test", null, null, model);
        
        assertEquals("movies", result, "Should return movies template");
        
        assertTrue(model.containsAttribute("movies"), "Model should contain movies");
        assertTrue(model.containsAttribute("genres"), "Model should contain genres");
        assertTrue(model.containsAttribute("searchName"), "Model should contain search name");
        assertTrue(model.containsAttribute("searchMessage"), "Model should contain search message");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertEquals(1, movies.size(), "Should find 1 movie");
        
        String message = (String) model.getAttribute("searchMessage");
        assertTrue(message.contains("Ahoy!"), "Should contain pirate greeting");
        assertTrue(message.contains("Found 1 movie"), "Should mention found count");
    }

    // Mock Services - Arrr! Our trusty crew members for testing!

    private static class MockMovieService extends MovieService {
        private List<Movie> searchResults = null;

        @Override
        public List<Movie> getAllMovies() {
            return Arrays.asList(TEST_MOVIE_1, TEST_MOVIE_2, TEST_MOVIE_3);
        }

        @Override
        public Optional<Movie> getMovieById(Long id) {
            if (id == null || id <= 0) return Optional.empty();
            return getAllMovies().stream()
                    .filter(movie -> movie.getId().equals(id))
                    .findFirst();
        }

        @Override
        public List<Movie> searchMovies(String name, Long id, String genre) {
            return getAllMovies().stream()
                    .filter(movie -> {
                        if (id != null && !movie.getId().equals(id)) return false;
                        if (name != null && !name.trim().isEmpty() && 
                            !movie.getMovieName().toLowerCase().contains(name.toLowerCase())) return false;
                        if (genre != null && !genre.trim().isEmpty() && 
                            !movie.getGenre().toLowerCase().contains(genre.toLowerCase())) return false;
                        return true;
                    })
                    .collect(java.util.stream.Collectors.toList());
        }

        @Override
        public List<String> getAllGenres() {
            return Arrays.asList("Action", "Comedy", "Drama");
        }
    }

    private static class MockReviewService extends ReviewService {
        @Override
        public List<Review> getReviewsForMovie(long movieId) {
            return new ArrayList<>();
        }
    }
}
