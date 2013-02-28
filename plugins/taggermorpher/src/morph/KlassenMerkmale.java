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

//import me.Cloneable;

/**
 * KlassenMerkmale speichert die Information eines Flexivs im
 * Flexivlexikon. Im Feld klassen sind die Namen von Flexivklassen
 * gespeichert, die dieselben KategorieMerkmale besitzen. Zu einer Liste
 * von Flexivklassen kann es verschiedene KategorieMerkmale geben,
 * deshalb sind sie in einem Vector gespeichert.
 * @author BL
*/

public class KlassenMerkmale implements Cloneable{

	/**
	 * 
	 * @uml.property name="klassen"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	private Vector klassen;

	/**
	 * 
	 * @uml.property name="merkmale"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="morph.KategorieMerkmal"
	 */
	private Vector merkmale;

    // klassen ist ein Vector mit String-Eintr�gen
    // merkmale ein Vector mit KategorieMerkmal-Eintr�gen

  public KlassenMerkmale() {
    klassen = null;
    merkmale = null;
  }

  public KlassenMerkmale( Vector v, Vector w ) {
    klassen = v;
    merkmale = w;
  }

	/**
	 * 
	 * @uml.property name="klassen"
	 */
	// MK
	public Vector getKlassen() {
		return this.klassen;
	}

	/**
	 * 
	 * @uml.property name="merkmale"
	 */
	public Vector getMerkmale() {
		return this.merkmale;
	}

  public Object clone(){
    if (klassen == null && merkmale == null) return new KlassenMerkmale();
    else {
      Enumeration enum1;
      Vector v = new Vector();
      Merkmal o;

      enum1 = merkmale.elements();

      while( enum1.hasMoreElements()){
        o = (Merkmal)enum1.nextElement();
	if (o != null) v.addElement(o.clone());
	   else v.addElement(null);
      }	  
      return new KlassenMerkmale( klassen, v );       
    }
  }

    // Vector mit KategorieMerkmal-Eintr�gen
  public Vector klassenVon( String s ) {
    Enumeration enum1, feat;
    String name;
    Vector v = new Vector();
    KategorieMerkmal fm;

    enum1 = klassen.elements();
    while( enum1.hasMoreElements() ) {
      name = ( String )enum1.nextElement();
      if( name.equals( s ) ) {
	feat = merkmale.elements();
	while( feat.hasMoreElements() ) {
	  fm = (KategorieMerkmal)feat.nextElement();
	  v.addElement( fm );
	}
      }
    }
    return v;
  }

  public String toString() {
    return "Klassen: " + klassen + "\nMerkmale: " + merkmale + "\n";
  }
}





