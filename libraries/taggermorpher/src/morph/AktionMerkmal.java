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

/** 
 * AktionMerkmal ist die Klasse, in der die Beschreibungen der
 * Endungshypothesen gespeichert werden, die im Endungslexikon
 * endung.txt zu finden sind. Zu einem solchen Merkmal gehoert ein
 * KategorieMerkmal, das die Kategorie- und Flexionsinformation fuer
 * eine Hypothese enthaelt und eine Aktion (cut oder concat) fuer die
 * Verarbeitung der Oberflaechenwortform.
 * @author BL
*/

public class AktionMerkmal {

	/**
	 * 
	 * @uml.property name="katMerkmal"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private KategorieMerkmal katMerkmal;

	/**
	 * 
	 * @uml.property name="aktion"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private EndungAktion aktion;


  public AktionMerkmal() {
    katMerkmal = null;
    aktion = null;
  }

  public AktionMerkmal( EndungAktion e, KategorieMerkmal k ) {
    katMerkmal = k;
    aktion = e;
  }

	/**
	 * 
	 * @uml.property name="katMerkmal"
	 */
	public KategorieMerkmal getKategorieMerkmal() {
		return katMerkmal;
	}

	/**
	 * 
	 * @uml.property name="aktion"
	 */
	public EndungAktion getAction() {
		return aktion;
	}

  public String toString() {
    return "Aktion: " + aktion + "\nMerkmal: " + katMerkmal;
  }
}
