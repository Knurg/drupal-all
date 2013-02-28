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
import java.util.Enumeration;
import de.fau.cs.jill.feature.*;

/**
 * AdjGradKategorieMerkmal speichert neben der Kategorie- und
 * Flexionsinformation fuer Adjektive Daten ueber die
 * Komparationsstufe (POSitiv, KOMParaptiv, SUPerlativ).
 * @author BL
 */

public class AdjGradKategorieMerkmal extends AdjKategorieMerkmal {

	/**
	 * 
	 * @uml.property name="typ"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	public FeatureStructure typ;

  
  public AdjGradKategorieMerkmal() {
  }
  
  public AdjGradKategorieMerkmal( FeatureStructure feat,
				  FlexMerkmal f ) {
    typ = feat;
    flex = f;
  }
  
  public AdjGradKategorieMerkmal( FeatureStructure feat ) {
    typ = feat;
  }
  
  public String toString() {
    if ( typ != null )
      return super.toString() + "\nGrad: " + typ.toString() + "\n";
    else return super.toString();
  }

  public Vector readingList( FlexiveDictionary lex ) {
    Enumeration enum1;
    Vector features, res;
    FlexMerkmal f;

    //System.out.println( "ADJ: flex: " + flex );

    return flex.readingList( lex );
  }
 }
