package feuille.util.theme.collection;

import com.formdev.flatlaf.FlatLightLaf;

public class Arctic
	extends FlatLightLaf
{
	public static final String NAME = "Arctic";

	public static boolean setup() {
		return setup( new Arctic() );
	}

	public static void installLafInfo() {
		installLafInfo( NAME, Arctic.class );
	}

	@Override
	public String getName() {
		return NAME;
	}

	public boolean isDark() {
		return super.isDark();
	}
}
