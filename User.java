public class User {


private String gmailPhone;
private String passwordHash;

public User(String gmailPhone, String passwordHash) {
    this.gmailPhone = gmailPhone;
    this.passwordHash = passwordHash;
}

public String getGmailPhone() {
    return gmailPhone;
}

public String getPasswordHash() {
    return passwordHash;
}

public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
}

public String toFile() {
    return gmailPhone + ";" + passwordHash;
}

}
