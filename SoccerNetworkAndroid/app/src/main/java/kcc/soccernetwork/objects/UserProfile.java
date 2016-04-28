package kcc.soccernetwork.objects;

/**
 * Created by Administrator on 4/12/2016.
 */
public class UserProfile {
    private String user_id;
    private String username;
    private String password;
    private String full_name;
    private String email;
    private String phone_number;
    private String status;
    private String district_id;
    private String user_type;
    private String last_login;
    private String is_verified;
    private String verification_code;
    private String created;
    private String updated;
    private String deleted;
    private String avatar_path;
    private String gcm_reg_id;

    public String getGcm_reg_id() {
        return gcm_reg_id;
    }

    public void setGcm_reg_id(String gcm_reg_id) {
        this.gcm_reg_id = gcm_reg_id;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getVerification_code() {
        return verification_code;
    }

    public void setVerification_code(String verification_code) {
        this.verification_code = verification_code;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getIs_verified() {
        return is_verified;
    }

    public void setIs_verified(String is_verified) {
        this.is_verified = is_verified;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAvatar_path() {
        return avatar_path;
    }

    public void setAvatar_path(String avatar_path) {
        this.avatar_path = avatar_path;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "user_id='" + user_id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", full_name='" + full_name + '\'' +
                ", email='" + email + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", status='" + status + '\'' +
                ", district_id='" + district_id + '\'' +
                ", user_type='" + user_type + '\'' +
                ", last_login='" + last_login + '\'' +
                ", is_verified='" + is_verified + '\'' +
                ", verification_code='" + verification_code + '\'' +
                ", created='" + created + '\'' +
                ", updated='" + updated + '\'' +
                ", deleted='" + deleted + '\'' +
                ", avatar_path='" + avatar_path + '\'' +
                '}';
    }
}
