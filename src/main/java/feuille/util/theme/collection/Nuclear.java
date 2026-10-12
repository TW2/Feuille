package feuille.util.theme.collection;

import com.formdev.flatlaf.FlatDarkLaf;

public class Nuclear
	extends FlatDarkLaf
{
	public static final String NAME = "Nuclear";

	public static boolean setup() {
		return setup( new Nuclear() );
	}

	public static void installLafInfo() {
		installLafInfo( NAME, Nuclear.class );
	}

	@Override
	public String getName() {
		return NAME;
	}

	public boolean isDark() {
		return super.isDark();
	}
}
