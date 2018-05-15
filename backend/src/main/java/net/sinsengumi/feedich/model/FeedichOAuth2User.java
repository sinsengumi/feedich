package net.sinsengumi.feedich.model;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.ToString;

@ToString
public class FeedichOAuth2User implements OAuth2User, OidcUser {

    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;
    private final String name;
    private final OidcIdToken idToken;

    private final int id;
    private final String email;
    private final String username;

    public FeedichOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String name,
            int id, String email, String username) {
        this.authorities = authorities;
        this.attributes = attributes;
        this.name = name;
        this.idToken = null;

        this.id = id;
        this.email = email;
        this.username = username;
    }

    public FeedichOAuth2User(OidcIdToken idToken, Map<String, Object> attributes, int id, String email, String username) {
        this.authorities = null;
        this.attributes = attributes;
        this.name = null;
        this.idToken = idToken;

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

    @Override
    public Map<String, Object> getClaims() {
        return attributes;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return new OidcUserInfo(attributes);
    }

    @Override
    public OidcIdToken getIdToken() {
        return idToken;
    }
}
