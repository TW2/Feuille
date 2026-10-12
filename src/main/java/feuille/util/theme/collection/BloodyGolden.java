package feuille.util.theme.collection;

import com.formdev.flatlaf.FlatDarkLaf;

public class BloodyGolden
	extends FlatDarkLaf
{
	public static final String NAME = "BloodyGolden";

	public static boolean setup() {
		return setup( new BloodyGolden() );
	}

	public static void installLafInfo() {
		installLafInfo( NAME, BloodyGolden.class );
	}

	@Override
	public String getName() {
		return NAME;
	}

	public boolean isDark() {
		return super.isDark();
	}
}
