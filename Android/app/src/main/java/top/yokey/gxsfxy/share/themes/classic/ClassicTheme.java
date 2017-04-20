package top.yokey.gxsfxy.share.themes.classic;

import android.content.Context;
import android.content.res.Configuration;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import top.yokey.gxsfxy.share.OnekeyShareThemeImpl;
import top.yokey.gxsfxy.share.themes.classic.land.EditPageLand;
import top.yokey.gxsfxy.share.themes.classic.land.PlatformPageLand;
import top.yokey.gxsfxy.share.themes.classic.port.EditPagePort;
import top.yokey.gxsfxy.share.themes.classic.port.PlatformPagePort;

@SuppressWarnings("all")
public class ClassicTheme extends OnekeyShareThemeImpl {

    protected void showPlatformPage(Context context) {
        PlatformPage page;
        int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            page = new PlatformPagePort(this);
        } else {
            page = new PlatformPageLand(this);
        }
        page.show(context, null);
    }

    protected void showEditPage(Context context, Platform platform, ShareParams sp) {
        EditPage page;
        int orientation = context.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            page = new EditPagePort(this);
        } else {
            page = new EditPageLand(this);
        }
        page.setPlatform(platform);
        page.setShareParams(sp);
        page.show(context, null);
    }

}