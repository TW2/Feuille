package feuille.util.theme.collection;

import com.formdev.flatlaf.FlatLightLaf;

public class Patriot
	extends FlatLightLaf
{
	public static final String NAME = "Patriot";

	public static boolean setup() {
		return setup( new Patriot() );
	}

	public static void installLafInfo() {
		installLafInfo( NAME, Patriot.class );
	}

	@Override
	public String getName() {
		return NAME;
	}

	public boolean isDark() {
		return super.isDark();
	}
}
