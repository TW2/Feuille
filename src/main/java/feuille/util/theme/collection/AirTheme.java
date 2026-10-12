package feuille.util.theme.collection;

import com.formdev.flatlaf.FlatLightLaf;

public class AirTheme extends FlatLightLaf {

	public static final String NAME = "Air";

	public static boolean setup() {
		return setup( new AirTheme() );
	}

	public static void installLafInfo() {
		installLafInfo( NAME, AirTheme.class );
	}

	@Override
	public String getName() {
		return NAME;
	}

	public boolean isDark() {
		return super.isDark();
	}
}
