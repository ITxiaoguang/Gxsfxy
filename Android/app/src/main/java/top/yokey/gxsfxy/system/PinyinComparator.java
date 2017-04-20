package top.yokey.gxsfxy.system;

import java.util.Comparator;

public class PinyinComparator implements Comparator<FriendBean> {

    public int compare(FriendBean o1, FriendBean o2) {
        if (o1.getLetters().equals("@") || o2.getLetters().equals("#")) {
            return -1;
        } else if (o1.getLetters().equals("#") || o2.getLetters().equals("@")) {
            return 1;
        } else {
            return o1.getLetters().compareTo(o2.getLetters());
        }
    }

}