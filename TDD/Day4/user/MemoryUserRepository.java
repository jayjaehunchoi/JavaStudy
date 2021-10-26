package user;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserRepository implements UserRepository{

    Map<String,User> store = new HashMap<>();

    @Override
    public void save(User user) {
        store.put(user.getId(),user);
    }

    @Override
    public User findById(String id) {
        return store.get(id);
    }
}
