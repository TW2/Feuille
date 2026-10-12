package feuille.util.theme.collection;

import com.formdev.flatlaf.FlatDarkLaf;

public class Military
	extends FlatDarkLaf
{
	public static final String NAME = "Military";

	public static boolean setup() {
		return setup( new Military() );
	}

	public static void installLafInfo() {
		installLafInfo( NAME, Military.class );
	}

	@Override
	public String getName() {
		return NAME;
	}

	public boolean isDark() {
		return super.isDark();
	}
}
