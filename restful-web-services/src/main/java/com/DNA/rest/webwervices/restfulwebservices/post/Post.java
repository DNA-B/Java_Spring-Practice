package com.DNA.rest.webwervices.restfulwebservices.post;

import com.DNA.rest.webwervices.restfulwebservices.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
public class Post {
    @Id
    @GeneratedValue
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;
    
    @Size(min = 2)
    private String description;
    
    public Post(Integer id, String description) {
        this.id = id;
        this.description = description;
    }
    
    public Post() {
        
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", user=" + user +
                ", description='" + description + '\'' +
                '}';
    }
}
