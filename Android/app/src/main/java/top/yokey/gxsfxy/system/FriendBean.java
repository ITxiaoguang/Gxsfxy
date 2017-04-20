package top.yokey.gxsfxy.system;

public class FriendBean {

    private String idString, avatarString, nicknameString;
    private String genderString, signString, lettersString;

    public void setId(String id) {
        this.idString = id;
    }

    public void setAvatar(String avatar) {
        this.avatarString = avatar;
    }

    public void setNickname(String nickname) {
        this.nicknameString = nickname;
    }

    public void setGender(String gender) {
        this.genderString = gender;
    }

    public void setSign(String sign) {
        this.signString = sign;
    }

    public void setLetters(String letters) {
        this.lettersString = letters;
    }

    public String getId() {
        return idString;
    }

    public String getAvatar() {
        return avatarString;
    }

    public String getNickname() {
        return nicknameString;
    }

    public String getGender() {
        return genderString;
    }

    public String getSign() {
        return signString;
    }

    public String getLetters() {
        return lettersString;
    }

}