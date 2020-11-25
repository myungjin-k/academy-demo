package my.myungjin.academyDemo.security;

import lombok.ToString;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@ToString
public class MyAuthenticationToken extends AbstractAuthenticationToken {

    private String principal;

    private Object credentials;

    public MyAuthenticationToken(String principal, Object credentials) {
        super(null);
        super.setAuthenticated(false);

        this.principal = principal;
        this.credentials = credentials;
    }

    MyAuthenticationToken(String principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        super.setAuthenticated(true);

        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public String getPrincipal() {
        return principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        credentials = null;
    }
}
