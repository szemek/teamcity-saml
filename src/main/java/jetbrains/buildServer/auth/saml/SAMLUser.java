package jetbrains.buildServer.auth.saml;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class SAMLUser {
    private final String id;
    private final String name;
    private final String email;
    private final String userGroupKey;

    public SAMLUser(String id) {
        this(id, null, null, null);
    }

    public SAMLUser(String id, String name, String email, String userGroupKey) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.userGroupKey = userGroupKey;
    }

    public String getId() {
        return Optional.ofNullable(id).orElse(email);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUserGroupKey() {
        return userGroupKey;
    }

    @Override
    public String toString() {
        return "SAMLUser{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", userGroupKey='" + userGroupKey + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SAMLUser samlUser = (SAMLUser) o;
        return Objects.equals(id, samlUser.id) &&
                Objects.equals(name, samlUser.name) &&
                Objects.equals(email, samlUser.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }
}
