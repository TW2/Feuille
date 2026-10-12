package feuille.util.theme.collection;

import com.formdev.flatlaf.FlatLightLaf;

public class Lemon
	extends FlatLightLaf
{
	public static final String NAME = "Lemon";

	public static boolean setup() {
		return setup( new Lemon() );
	}

	public static void installLafInfo() {
		installLafInfo( NAME, Lemon.class );
	}

	@Override
	public String getName() {
		return NAME;
	}

	public boolean isDark() {
		return super.isDark();
	}
}
