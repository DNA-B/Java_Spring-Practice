package com.DNA.rest.webwervices.restfulwebservices.user;

import com.DNA.rest.webwervices.restfulwebservices.jpa.UserRepository;
import com.DNA.rest.webwervices.restfulwebservices.post.Post;
import com.DNA.rest.webwervices.restfulwebservices.post.PostRepository;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UserJpaResource {
    private UserRepository userRepository;
    private PostRepository postRepository;
    
    public UserJpaResource(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }
    
    @GetMapping("/jpa/users")
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }
    
    
    @GetMapping("/jpa/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) {
        User user = userRepository.findById(id).get();
        
        if (user == null)
            throw new UserNotFoundExcepttion("id: " + id);
        
        EntityModel<User> entityModel = EntityModel.of(user);
        WebMvcLinkBuilder link = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                        this.getClass()).retrieveAllUsers()
        );
        entityModel.add(link.withRel("all-users"));
        
        return entityModel;
    }
    
    @DeleteMapping("/jpa/users/{id}")
    public void deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
    }
    
    @PostMapping("/jpa/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
    
    @GetMapping("/jpa/users/{id}/posts")
    public List<Post> retrievePostsForUser(@PathVariable int id) {
        User user = userRepository.findById(id).get();
        
        if (user == null)
            throw new UserNotFoundExcepttion("id: " + id);
        
        return user.getPosts();
    }
    
    @GetMapping("/jpa/users/{id}/posts/{post_id}")
    public Post retrievePostsByIdForUser(@PathVariable int id, @PathVariable int post_id) {
        return postRepository.findById(post_id).get();
    }
    
    @PostMapping("/jpa/users/{id}/posts")
    public ResponseEntity<Object> createPostsForUser(@PathVariable int id, @Valid @RequestBody Post post) {
        User user = userRepository.findById(id).get();
        
        if (user == null)
            throw new UserNotFoundExcepttion("id: " + id);
        
        post.setUser(user);
        Post savedPost = postRepository.save(post);
        
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPost.getId())
                .toUri();
        
        return ResponseEntity.created(location).build();
    }
}
