package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for MovieService search functionality.
 * Arrr! These tests be making sure our treasure hunting methods work ship shape!
 */
public class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        movieService = new MovieService();
    }

    @Test
    @DisplayName("Should load movies from JSON file")
    public void testLoadMoviesFromJson() {
        List<Movie> movies = movieService.getAllMovies();
        assertNotNull(movies, "Movies list should not be null");
        assertFalse(movies.isEmpty(), "Movies list should not be empty");
        assertTrue(movies.size() > 0, "Should load at least one movie");
    }

    @Test
    @DisplayName("Should get movie by valid ID")
    public void testGetMovieByValidId() {
        Optional<Movie> movie = movieService.getMovieById(1L);
        assertTrue(movie.isPresent(), "Should find movie with ID 1");
        assertEquals(1L, movie.get().getId(), "Movie ID should match");
        assertEquals("The Prison Escape", movie.get().getMovieName(), "Movie name should match");
    }

    @Test
    @DisplayName("Should return empty for invalid ID")
    public void testGetMovieByInvalidId() {
        Optional<Movie> movie = movieService.getMovieById(999L);
        assertFalse(movie.isPresent(), "Should not find movie with invalid ID");
    }

    @Test
    @DisplayName("Should return empty for null ID")
    public void testGetMovieByNullId() {
        Optional<Movie> movie = movieService.getMovieById(null);
        assertFalse(movie.isPresent(), "Should not find movie with null ID");
    }

    @Test
    @DisplayName("Should return empty for zero or negative ID")
    public void testGetMovieByZeroOrNegativeId() {
        Optional<Movie> movieZero = movieService.getMovieById(0L);
        assertFalse(movieZero.isPresent(), "Should not find movie with ID 0");
        
        Optional<Movie> movieNegative = movieService.getMovieById(-1L);
        assertFalse(movieNegative.isPresent(), "Should not find movie with negative ID");
    }

    @Test
    @DisplayName("Should search movies by name (case-insensitive)")
    public void testSearchMoviesByName() {
        // Test exact match
        List<Movie> results = movieService.searchMovies("The Prison Escape", null, null);
        assertEquals(1, results.size(), "Should find exactly one movie");
        assertEquals("The Prison Escape", results.get(0).getMovieName());

        // Test partial match
        results = movieService.searchMovies("prison", null, null);
        assertEquals(1, results.size(), "Should find movie with partial name match");

        // Test case-insensitive
        results = movieService.searchMovies("PRISON", null, null);
        assertEquals(1, results.size(), "Should find movie with case-insensitive match");

        // Test multiple matches
        results = movieService.searchMovies("The", null, null);
        assertTrue(results.size() > 1, "Should find multiple movies containing 'The'");
    }

    @Test
    @DisplayName("Should search movies by ID")
    public void testSearchMoviesById() {
        List<Movie> results = movieService.searchMovies(null, 1L, null);
        assertEquals(1, results.size(), "Should find exactly one movie with ID 1");
        assertEquals(1L, results.get(0).getId());

        // Test non-existent ID
        results = movieService.searchMovies(null, 999L, null);
        assertEquals(0, results.size(), "Should find no movies with non-existent ID");
    }

    @Test
    @DisplayName("Should search movies by genre (case-insensitive)")
    public void testSearchMoviesByGenre() {
        // Test exact genre match
        List<Movie> results = movieService.searchMovies(null, null, "Drama");
        assertTrue(results.size() > 0, "Should find movies with Drama genre");

        // Test case-insensitive genre match
        results = movieService.searchMovies(null, null, "drama");
        assertTrue(results.size() > 0, "Should find movies with case-insensitive genre match");

        // Test partial genre match (e.g., "Crime/Drama" contains "Crime")
        results = movieService.searchMovies(null, null, "Crime");
        assertTrue(results.size() > 0, "Should find movies with Crime in genre");

        // Test non-existent genre
        results = movieService.searchMovies(null, null, "NonExistentGenre");
        assertEquals(0, results.size(), "Should find no movies with non-existent genre");
    }

    @Test
    @DisplayName("Should search movies with multiple criteria (AND logic)")
    public void testSearchMoviesWithMultipleCriteria() {
        // Test name + genre
        List<Movie> results = movieService.searchMovies("Family", null, "Crime");
        assertTrue(results.size() > 0, "Should find movies matching both name and genre");
        
        // Verify all results match both criteria
        for (Movie movie : results) {
            assertTrue(movie.getMovieName().toLowerCase().contains("family"), 
                "Movie name should contain 'family'");
            assertTrue(movie.getGenre().toLowerCase().contains("crime"), 
                "Movie genre should contain 'crime'");
        }

        // Test conflicting criteria (should return empty)
        results = movieService.searchMovies("Prison", null, "Comedy");
        assertEquals(0, results.size(), "Should find no movies with conflicting criteria");
    }

    @Test
    @DisplayName("Should handle empty and null search parameters")
    public void testSearchMoviesWithEmptyParameters() {
        // Test all null parameters (should return all movies)
        List<Movie> allMovies = movieService.getAllMovies();
        List<Movie> results = movieService.searchMovies(null, null, null);
        assertEquals(allMovies.size(), results.size(), "Should return all movies when all parameters are null");

        // Test empty string parameters
        results = movieService.searchMovies("", null, "");
        assertEquals(allMovies.size(), results.size(), "Should return all movies when string parameters are empty");

        // Test whitespace-only parameters
        results = movieService.searchMovies("   ", null, "   ");
        assertEquals(allMovies.size(), results.size(), "Should return all movies when string parameters are whitespace");
    }

    @Test
    @DisplayName("Should get all unique genres")
    public void testGetAllGenres() {
        List<String> genres = movieService.getAllGenres();
        assertNotNull(genres, "Genres list should not be null");
        assertFalse(genres.isEmpty(), "Genres list should not be empty");
        
        // Check for expected genres from the test data
        assertTrue(genres.contains("Drama"), "Should contain Drama genre");
        assertTrue(genres.contains("Crime/Drama"), "Should contain Crime/Drama genre");
        assertTrue(genres.contains("Action/Crime"), "Should contain Action/Crime genre");
        
        // Verify uniqueness (no duplicates)
        long uniqueCount = genres.stream().distinct().count();
        assertEquals(genres.size(), uniqueCount, "All genres should be unique");
        
        // Verify sorted order
        List<String> sortedGenres = genres.stream().sorted().collect(java.util.stream.Collectors.toList());
        assertEquals(sortedGenres, genres, "Genres should be sorted alphabetically");
    }

    @Test
    @DisplayName("Should handle special characters in search")
    public void testSearchMoviesWithSpecialCharacters() {
        // Test searching with special characters that might exist in movie names
        List<Movie> results = movieService.searchMovies(":", null, null);
        // Should not throw exception and should handle gracefully
        assertNotNull(results, "Results should not be null even with special characters");
        
        // Test searching with forward slash (exists in genres like "Crime/Drama")
        results = movieService.searchMovies(null, null, "/");
        assertTrue(results.size() > 0, "Should find movies with '/' in genre");
    }

    @Test
    @DisplayName("Should be case-insensitive for all string searches")
    public void testCaseInsensitiveSearch() {
        // Test various case combinations
        String[] nameCases = {"prison", "PRISON", "Prison", "pRiSoN"};
        String[] genreCases = {"drama", "DRAMA", "Drama", "dRaMa"};
        
        int expectedNameResults = movieService.searchMovies("prison", null, null).size();
        int expectedGenreResults = movieService.searchMovies(null, null, "drama").size();
        
        for (String nameCase : nameCases) {
            List<Movie> results = movieService.searchMovies(nameCase, null, null);
            assertEquals(expectedNameResults, results.size(), 
                "Case variation '" + nameCase + "' should return same results");
        }
        
        for (String genreCase : genreCases) {
            List<Movie> results = movieService.searchMovies(null, null, genreCase);
            assertEquals(expectedGenreResults, results.size(), 
                "Case variation '" + genreCase + "' should return same results");
        }
    }
}