package project.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.text.ParseException;
import java.util.*;

@Service
public class UserService extends MainService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Map<String, String> hashMap = connection.getUser(s);
        String role;
        if(!hashMap.isEmpty()) {
            if(hashMap.get("status").equals("PRO") || hashMap.get("status").equals("ASSISTANT")) {
                role = "USER";
            } else {
                role = "ADMIN";
            }
            authorities.add(new SimpleGrantedAuthority(role));
            User user = new User(s, "{noop}" + hashMap.get("password"), authorities);
            return user;
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public List<HashMap<String, String>> getSchedule(String username, String startDate, String endDate) throws ParseException {
        String id = connection.getEmplId(username);
        return super.getSchedule(id, startDate, endDate);
    }

    public Map<String, String> getCourseByGroup (String groupId) {
        return connection.getCourseByGroup(Integer.parseInt(groupId));
    }
}
