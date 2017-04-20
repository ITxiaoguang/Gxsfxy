package top.yokey.gxsfxy.system;

import net.tsz.afinal.http.AjaxParams;

import top.yokey.gxsfxy.activity.MyApplication;
import top.yokey.gxsfxy.utility.TimeUtil;

public class UserAjaxParams extends AjaxParams {

    public UserAjaxParams(MyApplication application, String control, String action) {

        this.put("model", "mobile");
        this.put("control", control);
        this.put("action", action);
        this.put("token", application.userTokenString);
        this.put("client", application.getAndroidVersion());
        this.put("version", application.getVersion());
        this.put("device", application.getDeviceName());
        this.put("time", TimeUtil.getAll());

    }

}