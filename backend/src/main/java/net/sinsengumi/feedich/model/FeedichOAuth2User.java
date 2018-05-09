package net.sinsengumi.feedich.model;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.ToString;

@ToString
public class FeedichOAuth2User implements OAuth2User {

    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;
    private final String name;

    private final int id;
    private final String email;
    private final String username;

    public FeedichOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String name,
            int id, String email, String username) {
        this.authorities = authorities;
        this.attributes = attributes;
        this.name = name;

        this.id = id;
        this.email = email;
        this.username = username;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
