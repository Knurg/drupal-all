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

/** 
 * Ein FlexivEintrag ist ein Eintrag im Flexivlexikon, das die
 * moeglichen Endungen enthaelt (flexive.txt).
 * Ein FlexivEintrag besteht aus einem Schluessel (naemlich dem Flexiv
 * selbst) und einem Vector von KlassenMerkmale-Elementen. Jeder
 * Eintrag des Vector codiert eine Liste von fuer das Flexiv moeglichen
 * Flexivklassen und deren syntaktischer Features.
 * @author BL
*/

public class FlexivEintrag extends Eintrag {
  public FlexivEintrag() {
  }
  
  public FlexivEintrag( String flexiv, Vector daten ) {
    this.key = flexiv;
    // daten ist ein Vector mit KlassenMerkmale-Eintraegen.
    this.daten = daten;
  }
    
  public String holeFlexiv() {
    return holeEintrag();
  }
  
  public String toString() {
    return "Flexiv Eintrag fuer: " + key + "\n" + daten.toString();
  }


  public Object clone() {

      Enumeration enum1;

      if( daten != null ) {
	  Vector v = new Vector();
	  KlassenMerkmale o;

	  enum1 = daten.elements();

	  while( enum1.hasMoreElements()){
	      o = ( KlassenMerkmale )enum1.nextElement();
	      v.addElement(o.clone());
	  }	  
	  return new FlexivEintrag( key, (Vector)v ); 
      }
    else return new FlexivEintrag( key, null );
  }

  public Vector categoriesFor( String flexClass ) {
    KlassenMerkmale k;
    Enumeration enum1;
    Vector v;

    enum1 = daten.elements();

    while( enum1.hasMoreElements() ) {
      k = ( KlassenMerkmale )enum1.nextElement();
      v = k.klassenVon( flexClass );
      
      // v ist ein Vector von KategorieMerkmalen f�r flexClass
      if( v.size() > 0 ) return v;
    }

    return new Vector();
  }
}
