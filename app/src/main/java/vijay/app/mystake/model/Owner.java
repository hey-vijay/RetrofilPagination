package vijay.app.mystake.model;

public class Owner {
    public int reputation;
    public int user_id;
    public String user_type;
    public String profile_image;
    public String display_name;
    public String link;

    public Owner(int reputation, int user_id, String user_type, String profile_image, String display_name, String link) {
        this.reputation = reputation;
        this.user_id = user_id;
        this.user_type = user_type;
        this.profile_image = profile_image;
        this.display_name = display_name;
        this.link = link;
    }

    public int getReputation() {
        return reputation;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "reputation=" + reputation +
                ", user_id=" + user_id +
                ", user_type='" + user_type + '\'' +
                ", profile_image='" + profile_image + '\'' +
                ", display_name='" + display_name + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
