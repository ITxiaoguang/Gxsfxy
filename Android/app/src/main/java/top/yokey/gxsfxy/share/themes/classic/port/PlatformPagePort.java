package top.yokey.gxsfxy.share.themes.classic.port;

import java.util.ArrayList;

import top.yokey.gxsfxy.share.OnekeyShareThemeImpl;
import top.yokey.gxsfxy.share.themes.classic.PlatformPage;
import top.yokey.gxsfxy.share.themes.classic.PlatformPageAdapter;

@SuppressWarnings("all")
public class PlatformPagePort extends PlatformPage {

	public PlatformPagePort(OnekeyShareThemeImpl impl) {
		super(impl);
	}

	public void onCreate() {
		requestPortraitOrientation();
		super.onCreate();
	}

	protected PlatformPageAdapter newAdapter(ArrayList<Object> cells) {
		return new PlatformPageAdapterPort(this, cells);
	}

}