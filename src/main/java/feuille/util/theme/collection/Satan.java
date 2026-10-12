package feuille.util.theme.collection;

import com.formdev.flatlaf.FlatDarkLaf;

public class Satan
	extends FlatDarkLaf
{
	public static final String NAME = "Satan";

	public static boolean setup() {
		return setup( new Satan() );
	}

	public static void installLafInfo() {
		installLafInfo( NAME, Satan.class );
	}

	@Override
	public String getName() {
		return NAME;
	}

	public boolean isDark() {
		return super.isDark();
	}
}
