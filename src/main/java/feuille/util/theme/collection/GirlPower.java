package feuille.util.theme.collection;

import com.formdev.flatlaf.FlatLightLaf;

public class GirlPower
	extends FlatLightLaf
{
	public static final String NAME = "GirlPower";

	public static boolean setup() {
		return setup( new GirlPower() );
	}

	public static void installLafInfo() {
		installLafInfo( NAME, GirlPower.class );
	}

	@Override
	public String getName() {
		return NAME;
	}

	public boolean isDark() {
		return super.isDark();
	}
}
