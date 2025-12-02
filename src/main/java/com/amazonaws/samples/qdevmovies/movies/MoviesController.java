package com.amazonaws.samples.qdevmovies.movies;

import com.amazonaws.samples.qdevmovies.utils.MovieIconUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class MoviesController {
    private static final Logger logger = LogManager.getLogger(MoviesController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/movies")
    public String getMovies(org.springframework.ui.Model model) {
        logger.info("Fetching movies");
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("genres", movieService.getAllGenres());
        return "movies";
    }

    @GetMapping("/movies/{id}/details")
    public String getMovieDetails(@PathVariable("id") Long movieId, org.springframework.ui.Model model) {
        logger.info("Fetching details for movie ID: {}", movieId);
        
        Optional<Movie> movieOpt = movieService.getMovieById(movieId);
        if (!movieOpt.isPresent()) {
            logger.warn("Movie with ID {} not found", movieId);
            model.addAttribute("title", "Movie Not Found");
            model.addAttribute("message", "Movie with ID " + movieId + " was not found.");
            return "error";
        }
        
        Movie movie = movieOpt.get();
        model.addAttribute("movie", movie);
        model.addAttribute("movieIcon", MovieIconUtils.getMovieIcon(movie.getMovieName()));
        model.addAttribute("allReviews", reviewService.getReviewsForMovie(movie.getId()));
        
        return "movie-details";
    }

    /**
     * REST API endpoint for movie search.
     * Ahoy matey! This be the treasure map endpoint for finding yer movies!
     * 
     * @param name Movie name to search for (optional)
     * @param id Movie ID to search for (optional)
     * @param genre Genre to filter by (optional)
     * @return JSON response with search results
     */
    @GetMapping("/movies/search")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> searchMoviesApi(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String genre) {
        
        logger.info("Ahoy! API search request - name: {}, id: {}, genre: {}", name, id, genre);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate parameters
            if (id != null && id <= 0) {
                response.put("success", false);
                response.put("message", "Arrr! That ID be invalid, matey! Must be a positive number.");
                response.put("movies", List.of());
                return ResponseEntity.badRequest().body(response);
            }
            
            List<Movie> searchResults = movieService.searchMovies(name, id, genre);
            
            response.put("success", true);
            response.put("movies", searchResults);
            response.put("totalResults", searchResults.size());
            
            if (searchResults.isEmpty()) {
                response.put("message", "Arrr! No treasure found with those search criteria, me hearty! Try different terms.");
            } else {
                response.put("message", String.format("Ahoy! Found %d movie%s matching yer search!", 
                    searchResults.size(), searchResults.size() == 1 ? "" : "s"));
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Scurvy bug in search API: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "Arrr! Something went wrong while searching for treasure. Try again later, matey!");
            response.put("movies", List.of());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * HTML form endpoint for movie search.
     * This be the route for landlubbers using the search form!
     * 
     * @param name Movie name to search for (optional)
     * @param id Movie ID to search for (optional)
     * @param genre Genre to filter by (optional)
     * @param model Spring model for template rendering
     * @return movies template with search results
     */
    @GetMapping("/movies/search/form")
    public String searchMoviesForm(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String genre,
            org.springframework.ui.Model model) {
        
        logger.info("Ahoy! Form search request - name: {}, id: {}, genre: {}", name, id, genre);
        
        try {
            // Validate parameters
            if (id != null && id <= 0) {
                model.addAttribute("movies", List.of());
                model.addAttribute("genres", movieService.getAllGenres());
                model.addAttribute("searchError", "Arrr! That ID be invalid, matey! Must be a positive number.");
                model.addAttribute("searchName", name);
                model.addAttribute("searchGenre", genre);
                return "movies";
            }
            
            List<Movie> searchResults = movieService.searchMovies(name, id, genre);
            
            model.addAttribute("movies", searchResults);
            model.addAttribute("genres", movieService.getAllGenres());
            
            // Add search parameters back to form
            model.addAttribute("searchName", name);
            model.addAttribute("searchId", id);
            model.addAttribute("searchGenre", genre);
            
            if (searchResults.isEmpty()) {
                model.addAttribute("searchMessage", "Arrr! No treasure found with those search criteria, me hearty! Try different terms.");
            } else {
                model.addAttribute("searchMessage", String.format("Ahoy! Found %d movie%s matching yer search!", 
                    searchResults.size(), searchResults.size() == 1 ? "" : "s"));
            }
            
        } catch (Exception e) {
            logger.error("Scurvy bug in search form: {}", e.getMessage(), e);
            model.addAttribute("movies", List.of());
            model.addAttribute("genres", movieService.getAllGenres());
            model.addAttribute("searchError", "Arrr! Something went wrong while searching for treasure. Try again later, matey!");
            model.addAttribute("searchName", name);
            model.addAttribute("searchId", id);
            model.addAttribute("searchGenre", genre);
        }
        
        return "movies";
    }
}