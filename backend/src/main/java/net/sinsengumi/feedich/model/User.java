package net.sinsengumi.feedich.model;

import java.util.Date;
import java.util.Map;
import java.util.stream.Stream;

import lombok.Data;

@Data
public class User {
    private int id;
    private String email;
    private String name;
    private String authIdGoogle;
    private String authIdGithub;
    private String authIdFacebook;
    private String authIdYahoo;
    private String authIdLine;
    private Date createdAt;
    private Date updatedAt;

    public void setAuthId(ServiceProvider serviceProvider, String authId) {
        switch (serviceProvider) {
        case Google:
            authIdGoogle = authId;
            break;
        case Github:
            authIdGithub = authId;
            break;
        case Facebook:
            authIdFacebook = authId;
            break;
        case Yahoo:
            authIdYahoo = authId;
            break;
        case LINE:
            authIdLine = authId;
            break;
        default:
            break;
        }
    }

    public enum ServiceProvider {
        Google {
            @Override
            public String getId(Map<String, Object> attributes) {
                return (String) attributes.get("sub");
            }

            @Override
            public String getEmail(Map<String, Object> attributes) {
                return (String) attributes.get("email");
            }

            @Override
            public String getName(Map<String, Object> attributes) {
                return (String) attributes.get("name");
            }
        },
        Github {
            @Override
            public String getId(Map<String, Object> attributes) {
                Integer id = (Integer) attributes.get("id");
                return Integer.toString(id);
            }

            @Override
            public String getEmail(Map<String, Object> attributes) {
                return (String) attributes.get("email");
            }

            @Override
            public String getName(Map<String, Object> attributes) {
                return (String) attributes.get("name");
            }
        },
        Facebook {
            @Override
            public String getId(Map<String, Object> attributes) {
                return (String) attributes.get("sub");
            }

            @Override
            public String getEmail(Map<String, Object> attributes) {
                return (String) attributes.get("sub");
            }

            @Override
            public String getName(Map<String, Object> attributes) {
                return (String) attributes.get("sub");
            }
        },
        Yahoo {
            @Override
            public String getId(Map<String, Object> attributes) {
                return (String) attributes.get("sub");
            }

            @Override
            public String getEmail(Map<String, Object> attributes) {
                return (String) attributes.get("sub");
            }

            @Override
            public String getName(Map<String, Object> attributes) {
                return (String) attributes.get("sub");
            }
        },
        LINE {
            @Override
            public String getId(Map<String, Object> attributes) {
                return (String) attributes.get("sub");
            }

            @Override
            public String getEmail(Map<String, Object> attributes) {
                return (String) attributes.get("sub");
            }

            @Override
            public String getName(Map<String, Object> attributes) {
                return (String) attributes.get("sub");
            }
        };

        public abstract String getId(Map<String, Object> attributes);
        public abstract String getEmail(Map<String, Object> attributes);
        public abstract String getName(Map<String, Object> attributes);

        public static ServiceProvider of(String name) {
            return Stream.of(values())
                    .filter(sp -> name.equalsIgnoreCase(sp.toString()))
                    .findFirst().orElse(null);
        }
    }
}
