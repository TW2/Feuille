/*
 * Copyright (C) 2019 util2
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package feuille.util.effect.VsFilterMod;

import feuille.util.effect.AFx;
import feuille.util.effect.Parameter;

/**
 * Shaking
 * \jitter(left,right,up,down,period[,seed])
 * @author util2
 */
public class Shaking extends AFx {
    
    Parameter p_left = new Parameter(0, in.getTranslated("FxListShakingLeft", iso, "Left"));
    Parameter p_right = new Parameter(0, in.getTranslated("FxListShakingRight", iso, "Right"));
    Parameter p_up = new Parameter(0, in.getTranslated("FxListShakingUp", iso, "Up"));
    Parameter p_down = new Parameter(0, in.getTranslated("FxListShakingDown", iso, "Down"));
    Parameter p_period = new Parameter(0, in.getTranslated("FxListShakingPeriod", iso, "Period"));
    Parameter p_seed = new Parameter(10, in.getTranslated("FxListShakingSeed", iso, "Seed"));
    Parameter p_seeded = new Parameter(false, in.getTranslated("FxListShakingSeeded", iso, "Seeded"));

    public Shaking() {
        name = in.getTranslated("FxListShaking", iso, "Shaking");
        uniqueID = -1;
        params.add(p_left);
        params.add(p_right);
        params.add(p_up);
        params.add(p_down);
        params.add(p_period);
        params.add(p_seed);
        params.add(p_seeded);
    }

    @Override
    public String getTag() {
        return "\\jitter(" + getLeft() + "," + getRight() + "," + getUp() + "," + getDown() + "," + getPeriod() + (isSeeded() == true ? "," + getSeed() : "") + ")";
    }

    public void setLeft(int left) {
        p_left.setParam(left);
        params.set(0, p_left);
    }

    public int getLeft() {
        return (int)params.get(0).getParam();
    }

    public void setRight(int right) {
        p_right.setParam(right);
        params.set(1, p_right);
    }

    public int getRight() {
        return (int)params.get(1).getParam();
    }

    public void setUp(int up) {
        p_up.setParam(up);
        params.set(2, p_up);
    }

    public int getUp() {
        return (int)params.get(2).getParam();
    }

    public void setDown(int down) {
        p_down.setParam(down);
        params.set(3, p_down);
    }

    public int getDown() {
        return (int)params.get(3).getParam();
    }

    public void setPeriod(int period) {
        p_period.setParam(period);
        params.set(4, p_period);
    }

    public int getPeriod() {
        return (int)params.get(4).getParam();
    }

    public void setSeed(int seed) {
        p_seed.setParam(seed);
        params.set(5, p_seed);
    }

    public int getSeed() {
        return (int)params.get(5).getParam();
    }

    public void setSeeded(boolean seeded) {
        p_seeded.setParam(seeded);
        params.set(6, p_seeded);
    }

    public boolean isSeeded() {
        return (boolean)params.get(6).getParam();
    }
    
}
