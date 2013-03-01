package morph;

/**
 *
 * Copyright (C) 2000 Bernd Ludwig, Forwiss, Erlangen
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation; either version 1, or (at your option)
 *   any later version.
 *    
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 * @author <A HREF="mailto:bdludwig@forwiss.de">
 */

import java.util.Vector;

/**
 * AdjKategorieMerkmal speichert Information ueber Adjektive (Kategorie und
 * Flexionsdaten).
 * @author BL
*/

public abstract class AdjKategorieMerkmal extends KategorieMerkmal {
  public AdjKategorieMerkmal() {
    super();
  }

  public AdjKategorieMerkmal( FlexMerkmal feat, Vector consts, String base ) {
    super( feat, consts, base );
  }
  
  public void setFlexRef( AdjFlexRefMerkmal f ) {
    flex = f;
  }
  
  public String category() {
    return "ADJ";
  }
  
  public String toString() {
    return "Kategorie: ADJ\n" + super.toString();
  }
}
