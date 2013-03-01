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
 * KategorieMerkmal ist die abstrakte Oberklasse aller Klassen, die
 * Kategorien repraesentieren. Fuer jede Kategorie des Lexikons gibt es
 * eine eigene Klasse. Jede Kategorie enthaelt ein FlexMerkmal, das
 * Flexionsinformation aus dem flexive-Lexikon enthaelt. Ausserdem wird
 * waehrend der Wortbildungsanalyse Information ueber die Konstituenten
 * der analysierten Wortform gespeichert.
 * @author BL
*/

public abstract class KategorieMerkmal extends Merkmal {

	/**
	 * 
	 * @uml.property name="flex"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	protected FlexMerkmal flex = null;

	/**
	 * 
	 * @uml.property name="constInfo"
	 * @uml.associationEnd multiplicity="(0 -1)" elementType="morph.Constituent"
	 */
	protected Vector constInfo = null;

	/**
	 * 
	 * @uml.property name="baseForm" 
	 */
	// Vector von Constituents, die die morphologischen Bestandteile der
	// Wortform enthalten.
	protected String baseForm = "";


  public KategorieMerkmal() {
    flex = null;
    constInfo = null;
    baseForm = "";
  }

  public KategorieMerkmal( FlexMerkmal feat, Vector consts, String base ) {
    flex = feat;
    constInfo = consts;
    baseForm = base;
  }

  public String getRef() {
    if ( flex == null ) return "";
    else return flex.getRef();
  }

  public abstract String category();

  public boolean RefMerkmal() {
      if ( flex == null ) return false;
      else return flex.RefMerkmal();
      //    return false;
  }

  public void substRef( Vector v, int pos, Vector entries ) {
      if ( flex == null ) return;
      else flex.substRef( v, pos, entries );
  }

	/**
	 * 
	 * @uml.property name="constInfo"
	 */
	public void setConstInfo(Vector v) {
		constInfo = v;
	}

	/**
	 * 
	 * @uml.property name="constInfo"
	 */
	public Vector getConstInfo() {
		return constInfo;
	}

	/**
	 * 
	 * @uml.property name="baseForm"
	 */
	public void setBaseForm(String s) {
		baseForm = s;
	}

	/**
	 * 
	 * @uml.property name="flex"
	 */
	public FlexMerkmal getFlexInfo() {
		return flex;
	}

  // sollte Vector mit KatgeorieMerkmal-Eintr�gen zur�ckgeben
  public abstract Vector readingList( FlexiveDictionary lex );

  public void setFlexRef( FlexMerkmal f ) {
    flex = f;
  }

  public String toString() {
    String s = "";

    if( flex != null ) s += "Merkmale: " + flex.toString() + "\n";
    if( constInfo != null ) s += "Konstituenten: " + constInfo.toString() + "\n";
    if( !baseForm.equals( "" ) ) s += "Grundform: " + baseForm;

    return s;
  }
  
  // added for INTCER
  public String getBaseForm(){
  	 if( !baseForm.equals( "" ) ) return baseForm;
  	 	else return null;
  }
}
