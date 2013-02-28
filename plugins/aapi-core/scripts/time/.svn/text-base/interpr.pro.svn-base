%%%%%%%%%%%%%%%%%%%  Interpretationsregeln  %%%%%%%%%%%%%%%%%%%


%%%%%%%% Root-Regeln %%%%%%%%%%


interp0(Grain, (type_synt : X .. periode : P),
    (type_synt : X .. periode : ( epft : P .. Int))) :-
    interp(Grain, P, Int), !.
interp0(_Grain, X,X).


% 1. Dates

interp(an,
    date : (type : ordinaire .. grain : _Grain .. valeur : (jour : _J .. mois : _M .. annee : A)),
    istd : (grain : an .. deb : A .. fin : A)).
interp(an,
    date : (type : ordinaire .. grain : _Grain .. valeur : (mois : _M .. annee : A)),
    istd : (grain : an .. deb : A .. fin : A)).
interp(an,
    date : (type : ordinaire .. grain : _Grain .. valeur : (annee : A)),
    istd : (grain : an .. deb : A .. fin : A)).
interp(an,
    date : (type : scolaire .. grain : an .. valeur : (annee :A)),
    istd : (grain : an .. deb : A .. fin : B)):-
    B is A+1.
interp(an,
    date : (type : scolaire .. grain : periode_scolaire .. valeur : (nom : _ .. annee : A)),
    istd : (grain : an .. deb : A .. fin : A)).
interp(siecle,
    date : (type : ordinaire .. grain : siecle .. valeur : S),
    istd : (grain : siecle .. deb : S .. fin : S)).

% 2. Opérateurs
interp(G, intervalle(Inf,Sup), istd : (grain : G .. deb : D .. fin : F)) :-
    interp(G, Inf, istd : (grain : _ .. deb : D .. fin : _F)),
    interp(G, Sup, istd : (grain : _ .. deb : _D .. fin : F)).
interp(G, approx(Per), Int) :-
    interp(G,Per,Int0),
    approxime(G,Int0,Int).
% Interprétation "semi-ponctuelle" de "Durée avant/apres Période"
% = approximation autour d'une translation de la borne (initiale ou finale) adéquate
interp(G, ante(duree : (grain : G .. valeur : X), Per), istd : (grain : G .. deb : D .. fin : F)) :-
    interp(G, Per, istd : (grain : G .. deb : D1 .. fin : _F1)),
    defaut(approx, G, N), D is D1 - X - N, F is D1 - X + N.
interp(G, post(duree : (grain : G .. valeur : X), Per), istd : (grain : G .. deb : D .. fin : F)) :-
    interp(G, Per, istd : (grain : G .. deb : _D1 .. fin : F1)),
    defaut(approx, G, N), D is F1 + X - N, F is F1 + X + N.
interp(an, annees(A), istd : (grain : an .. deb : A .. fin : F)) :-
    defaut(annees, A, F).
interp(an, debut(Per), Int) :-
    interp(an, Per, Int0),
    modif(debut,Int0, Int).
interp(an, milieu(Per), Int) :-
    interp(an , Per, Int0),
    modif(milieu,Int0, Int).
interp(an, fin(Per), Int) :-
    interp(an, Per, Int0),
    modif(fin,Int0, Int).
interp(an, ref_temp_contexte, Int) :-
    periode_ref_contexte(Int).
interp(an, ref_temp_contexte(debut), istd : (grain : an .. deb : D .. fin : D)) :-
    periode_ref_contexte(istd : (grain : an .. deb : D .. fin : _F)).
interp(an, ref_temp_contexte(fin), istd : (grain : an .. deb : F .. fin : F)) :-
    periode_ref_contexte(istd : (grain : an .. deb : _D .. fin : F)).


% Utilitaires
approxime(G, istd : (grain : G .. deb : D .. fin : F), istd : (grain : G .. deb : ND .. fin : NF)) :-
    defaut(approx,G,N),
    ND is D-N, NF is F+N.

modif(debut, istd : (grain : G .. deb : D .. fin : F), istd : (grain : G .. deb : D .. fin : NF)) :-
    defaut(modif,Taux),
    Delta is 1 + integer( Taux * (F-D)),
    NF is D + Delta.
modif(milieu, istd : (grain : G .. deb : D .. fin : F), istd : (grain : G .. deb : ND .. fin : NF)) :-
    defaut(modif,Taux),
    Delta is integer( Taux * (F-D) * 0.5),
    ND is D + Delta, NF is F - Delta.
modif(fin, istd : (grain : G .. deb : D .. fin : F), istd : (grain : G .. deb : ND .. fin : F)) :-
    defaut(modif,Taux),
    Delta is 1 + integer( Taux * (F-D)),
    ND is F - Delta.

% Valeurs par défaut
defaut(approx,an,1).        % vers 1980-1985 = 1984-1986
defaut(annees, X, Y) :- Y is X+10.
                % les années 1980. Revoir pour "les années 1985"
defaut(modif, 0.5).     % Pour début/milieu/fin

periode_ref_contexte(istd : (grain : an .. deb : 1945 .. fin : 1994)).
