package feuille.util.theme.collection;

import com.formdev.flatlaf.FlatDarkLaf;

public class Mooon
	extends FlatDarkLaf
{
	public static final String NAME = "Mooon";

	public static boolean setup() {
		return setup( new Mooon() );
	}

	public static void installLafInfo() {
		installLafInfo( NAME, Mooon.class );
	}

	@Override
	public String getName() {
		return NAME;
	}

	public boolean isDark() {
		return super.isDark();
	}
}
