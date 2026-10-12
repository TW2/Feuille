package feuille.util.theme.collection;

import com.formdev.flatlaf.FlatLightLaf;

public class Nihon
	extends FlatLightLaf
{
	public static final String NAME = "Nihon";

	public static boolean setup() {
		return setup( new Nihon() );
	}

	public static void installLafInfo() {
		installLafInfo( NAME, Nihon.class );
	}

	@Override
	public String getName() {
		return NAME;
	}

	public boolean isDark() {
		return super.isDark();
	}
}
