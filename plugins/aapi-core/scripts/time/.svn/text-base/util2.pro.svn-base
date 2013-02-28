%%%%%%%%%%%%%%%%%%%  Utilitaires  %%%%%%%%%%%%%%%%%%%


atom_to_int(Atom, Int) :- name(Atom, Codes), name(Int, Codes), integer(Int).

%% Implémentation ISO conseillée de atom_to_int: --> catcher l'exception si pas un int
%%atom_to_int(Atom, Int) :- atom_codes(Atom, Codes), number_codes(Int, Codes), integer(Int).

%% Idem
%%atom_to_int('.', _) :- !, fail.
%%atom_to_int(' ', _) :- !, fail.
%%atom_to_int(Atom, Int) :- atom_to_term(Atom, Int, _), integer(Int).

%% Counter

%% initialisiert den Zähler: setzt ihn das erste Mal auf null oder auf einen bestimmten Wert
initCounter(Key) :- flag(Key,_Old,0).
initCounter(Key,Val) :- flag(Key,_Old,Val).

%% inkrementiert den Zähler dh. zählt bei einem bestimmten Schlüssel den Wert eins nach oben
incCounter(Key) :- flag(Key,N,N+1).
incCounter(Key,Val) :- flag(Key,Val,Val+1).

%% liefert den Zähler zurück: 
getCounter(Key,Val) :- flag(Key,Val,Val).
