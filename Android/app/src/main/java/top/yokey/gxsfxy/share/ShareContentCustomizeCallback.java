package top.yokey.gxsfxy.share;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;

@SuppressWarnings("all")
public interface ShareContentCustomizeCallback {

    void onShare(Platform platform, ShareParams paramsToShare);

}