package feuille.util.theme.collection;

import com.formdev.flatlaf.FlatDarkLaf;

public class Ranch
	extends FlatDarkLaf
{
	public static final String NAME = "Ranch";

	public static boolean setup() {
		return setup( new Ranch() );
	}

	public static void installLafInfo() {
		installLafInfo( NAME, Ranch.class );
	}

	@Override
	public String getName() {
		return NAME;
	}

	public boolean isDark() {
		return super.isDark();
	}
}
