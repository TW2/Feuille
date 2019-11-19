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
package feuille.drawing.shape;

/**
 *
 * @author util2
 */
public class FeuilleGlue {    
    private FeuillePoint glueAt = FeuillePoint.create(FeuillePoint.FeuillePointType.Unknown, -1);

    public FeuilleGlue() {
        
    }
    
    public void reset(){
        glueAt = FeuillePoint.create(FeuillePoint.FeuillePointType.Unknown, -1);
    }

    public void setGlueAt(FeuillePoint glueAt) {
        this.glueAt = glueAt;
    }

    public FeuillePoint getGlueAt() {
        return glueAt;
    }
}
