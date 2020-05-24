package com.mountblue.canduitRestAPI.controller;

import com.mountblue.canduitRestAPI.DAO.JwtUtil;
import com.mountblue.canduitRestAPI.DAO.PostRepository;
import com.mountblue.canduitRestAPI.DAO.UserRepository;
import com.mountblue.canduitRestAPI.config.UserDetailsServiceImpl;
import com.mountblue.canduitRestAPI.entity.AuthenticationRequest;
import com.mountblue.canduitRestAPI.entity.AuthenticationResponse;
import com.mountblue.canduitRestAPI.entity.Post;

import com.mountblue.canduitRestAPI.entity.User;
import com.mountblue.canduitRestAPI.services.PostService;
import com.mountblue.canduitRestAPI.services.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostRestController {

    private final Logger logger = LogManager.getLogger(PostRestController.class);

    private final PostRepository postRepository;

    private final PostService postService;

    private final UserService userService;

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtUtil jwtUtilToken;

    public PostRestController(PostRepository postRepository, PostService postService, UserService userService,
                              UserRepository userRepository, AuthenticationManager authenticationManager,
                              UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtilToken) {
        this.postRepository = postRepository;
        this.postService = postService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtilToken = jwtUtilToken;
    }

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {

        return "Hello World";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (Exception e) {
            throw new Exception("Username and Password is incorrect " + e);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtUtilToken.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }


    @ApiOperation(value = "Find all the posts",
            notes = "provide an id to look up the specific post")
    @GetMapping("/posts")
    public List<Post> getPosts() {
        return postRepository.findAllByOrderByIdAsc();
    }

    @ApiOperation(value = "Find post by id",
            notes = "provide an id to look up the specific post")
    @GetMapping("/posts/{postId}")
    public Post getPost(@ApiParam(value = "Retrieve the post by given id", required = true)
                            @PathVariable int postId) {

        Post thePost = postService.findById(postId);

        if (thePost == null) {
            throw new PostNotFoundException("Post id not found : " + postId);
        }

        return thePost;
    }

    @PostMapping("/posts")
    public Post addPost(@RequestBody Post thePost) {

        // If not set to zero it will update the post
        logger.info("Inside addPost method");
        thePost.setId(0);

        User user = userRepository.findAllByName(thePost.getAuthor());

        thePost.setUser(user);
        postService.save(thePost);
        return thePost;
    }

    @PutMapping("/posts")
    public Post updatePost(@RequestBody Post thePost) {

        User user = userRepository.findAllByName(thePost.getAuthor());

        thePost.setUser(user);
        postService.save(thePost);

        return thePost;
    }

    @DeleteMapping("/posts/{postId}")
    public String deletePost(@PathVariable int postId, Principal principal) {
        Post post = postService.findById(postId);

        if (post == null) {
            throw new PostNotFoundException("Post id not found : " + postId);
        }
        String response = postService.deleteById(postId, principal);

        return response;
    }
}