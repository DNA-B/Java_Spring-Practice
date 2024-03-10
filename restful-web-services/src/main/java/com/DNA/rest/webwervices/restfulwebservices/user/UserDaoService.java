package com.DNA.rest.webwervices.restfulwebservices.user;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Service
public class UserDaoService {
    private static List<User> users = new ArrayList<>();
    private static int usersCount = 0;
    
//    static {
//        users.add(new User(++usersCount, "DNA", LocalDate.now().minusYears(20)));
//        users.add(new User(++usersCount, "GOOD", LocalDate.now().minusYears(30)));
//        users.add(new User(++usersCount, "GENOM", LocalDate.now().minusYears(40)));
//    }
    
    public List<User> findAll() {
        return users;
    }
    
    public User save(User user) {
        user.setId(++usersCount);
        users.add(user);
        return user;
    }
    
    public User findOne(int id) {
        Predicate<? super User> predicate = user -> user.getId().equals(id);
        return users.stream().filter(predicate).findFirst().orElse(null);
    }
    
    public void deleteById(int id) {
        Predicate<? super User> predicate = user -> user.getId().equals(id);
        users.removeIf(predicate);
    }
}
