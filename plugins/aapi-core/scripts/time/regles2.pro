%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  Grammatikregeln  %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%%%%%%%%% Aufzurufende Dateien %%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

:- consult('run_helper.pro').

:- consult('util2.pro').

:- consult('actual.pro').

:- consult('lexique.pro').

:- consult('erg.pro').

:- consult('gulp.pro').

:- g_features([tag, stag, lemma, xmlctx, token_position]).

:- initCounter(idExpression).


%%%%%%%%%%%%% Root-Regeln %%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

exprTemp(X) --> periode(X), !.
exprTemp(X) --> ponctuel('O', X), !.
exprTemp(X) --> ponctuel(X), !.


%Alle Ausdr�cke stellen entw eine Periode dar oder einen
%Zeitpunkt


%%%%%%%%%%% 1. PERIODEN %%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Es werden Perioden mit dem einleitenden Wort 'seit' und 'bis' in Erw�gung
% gezogen. Periode die sowohl 'von' als auch 'bis' enthalten, werden von der Grammatik 
% später verarbeitet. 

%%% 1.1 Perioden "ZWISCHEN" %%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%% 1.1.1 mit Bindestrich %%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:year..value:J1))
                            ..date:(type:ordinary..grain:year..value:J2)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2)))))
--> jahr(J1), [token('\-',_)], jahr(J2), {incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,J1)),
recorda(exp_temp2,(Nexp,J2))}.

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:year..value:N1))
                            ..date:(type:ordinary..grain:year..value:N2)))
                    ..intpr:(begin:(day:1..month:1..year:N1)..end:(day:31..month:12..year:N2)))))
--> (# [token('Zwischen',_)] ; #[token('zwischen',_)]; # ([token('in',_), token('den',_), token('Jahren',_)])) ,strich_jahre(N1,N2),
{incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,N1)), recorda(exp_temp2,(Nexp,N2))}.

%%% 1.1.2 Zwischen Jahren %%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:year..value:J1))
                            ..date:(type:ordinary..grain:year..value:J)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), jahr(J1), [token('und',_)], jahr(J),
{incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,J1)), recorda(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:year..value:J1))
                            ..date:(type:ordinary..grain:year..value:J)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), jahr(J1), [token('und',_)], recognizeEvent(T,M,J),
{incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,T)), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.



%%% 1.1.3 Zwischen Monaten %%%%%%%%%%%%%%%%%%%%%

%%%% Februar-Schaltjahr-Check %%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), monat(M1), [token('und',_)], monat(M),
{resoud(month,Nexp), recorded(exp_temp3,(Nexp,J)), leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), monat(M1), [token('und',_)], monat(M),
{monthdays(_,M,Res), NT2 is Res, resoud(month,Nexp), recorded(exp_temp3,(Nexp,J))}.

%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), monat(M1), [token('und',_)], monat(M), jahr(J),
{leapyear(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,M1)), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), monat(M1), [token('und',_)], monat(M), jahr(J),
{monthdays(_,M,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,M1)), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.

%%% Februar-Schaltjahr-Check %%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J1)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), monat(M1), jahr(J1), [token('und',_)], monat(M), jahr(J),
{leapyear(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J1)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), monat(M1), jahr(J1), [token('und',_)], monat(M), jahr(J),
{monthdays(_,M,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.

%%%% Februar-Schaltjahr-Check%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:year..value:J1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), jahr(J1), [token('und',_)], monat(M), jahr(J),
{leapyear(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:year..value:J1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), jahr(J1), [token('und',_)], monat(M), jahr(J),
{monthdays(_,M,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.


%%% 1.1.3.1 Monate und Relatife %%%%%%%%%%%%%

%%% 1.1.3.1.1 Anfang %%%%%%%%%%%%%%%%%%%%%


periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:T..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), monat(M1), [token('und',_)], relatif(debut), monat(M),
{T is 9, resoud(month,Nexp), recorded(exp_temp3,(Nexp,J))}.

%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), relatif(debut), monat(M1), [token('und',_)], monat(M),
{T1 is 1, resoud(month,Nexp), recorded(exp_temp3,(Nexp,J)), leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res}.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), relatif(debut), monat(M1), [token('und',_)], monat(M),
{monthdays(_,M,Res), NT2 is Res, T1 is 1, resoud(month,Nexp), recorded(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:T..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), monat(M1), [token('und',_)], relatif(debut), monat(M),
jahr(J), {T is 9, incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,T)), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}.

%%%% Februar-Schaltjahr-Check %%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), relatif(debut), monat(M1), [token('und',_)], monat(M),
jahr(J), {leapyear(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res, T1 is 1, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), relatif(debut), monat(M1), [token('und',_)], monat(M),
jahr(J), {T1 is 1, incCounter(monthdays(_,M,Res), NT2 is Res, idExpression,Nexp), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}.

%%% 1.1.3.1.2 Mitte %%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:T..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), monat(M1), [token('und',_)], relatif(milieu), monat(M),
{T is 19, resoud(month,Nexp), recorded(exp_temp3,(Nexp,J))}.

%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), relatif(milieu), monat(M1), [token('und',_)], monat(M),
{T1 is 11, resoud(month,Nexp), recorded(exp_temp3,(Nexp,J)), leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), relatif(milieu), monat(M1), [token('und',_)], monat(M),
{monthdays(_,M,Res), NT2 is Res, T1 is 11, resoud(month,Nexp), recorded(exp_temp3,(Nexp,J))}.


periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:T..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), monat(M1), [token('und',_)], relatif(milieu), monat(M),
jahr(J), {T is 19, incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,T)), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}.

%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), relatif(milieu), monat(M1), [token('und',_)], monat(M),
jahr(J), {leapyear(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res, T1 is 11, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), relatif(milieu), monat(M1), [token('und',_)], monat(M),
jahr(J), {monthdays(_,M,Res), NT2 is Res, T1 is 11, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}.

%%% 1.1.3.1.3 Ende %%%%%%%%%%%%%%%%%%

%%%% Februar-Schaltjahr-Check %%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), monat(M1), [token('und',_)], relatif(fin), monat(M),
{resoud(month,Nexp), recorded(exp_temp3,(Nexp,J)), leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), monat(M1), [token('und',_)], relatif(fin), monat(M),
{monthdays(_,M,Res), NT2 is Res, resoud(month,Nexp), recorded(exp_temp3,(Nexp,J))}.

%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), relatif(fin), monat(M1), [token('und',_)], monat(M),
{T1 is 25, resoud(month,Nexp), recorded(exp_temp3,(Nexp,J)), leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), relatif(fin), monat(M1), [token('und',_)], monat(M),
{T1 is 25, monthdays(_,M,Res), NT2 is Res, resoud(month,Nexp), recorded(exp_temp3,(Nexp,J))}.

%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), monat(M1), [token('und',_)], relatif(fin), monat(M),
jahr(J), {T is 25, leapyear(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,T)), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), monat(M1), [token('und',_)], relatif(fin), monat(M),
jahr(J), {T is 25, monthdays(_,M,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,T)), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}.

%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), relatif(fin), monat(M1), [token('und',_)], monat(M),
jahr(J), {T1 is 25, leapyear(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), relatif(fin), monat(M1), [token('und',_)], monat(M),
jahr(J), {T1 is 25, monthdays(_,M,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}.

%%% 1.1.4 Zwischen Tagen %%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:day..value:T1))
                            ..date:(type:ordinary..grain:day..value:T)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:T..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), # [token('dem',_)], tag(T1), [token('\.',_)], monat(M1),
[token('und',_)], tag(T), [token('\.',_)], monat(M), jahr(J),
{incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,T)), recorda(exp_temp,(Nexp,M1)), 
recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.


periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:day..value:T1))
                            ..date:(type:ordinary..grain:day..value:T)))
                    ..intpr:(begin:(day:T1..month:M1..year:J1)..end:(day:T..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), # [token('dem',_)], tag(T1), [token('\.',_)], monat(M1), jahr(J1),
[token('und',_)], # [token('dem',_)], tag(T), [token('\.',_)], monat(M), jahr(J),
{incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,T)), recorda(exp_temp,(Nexp,M1)), 
recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:day..value:T1))
                            ..date:(type:ordinary..grain:day..value:T)))
                    ..intpr:(begin:(day:T1..month:M..year:J)..end:(day:T..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), # [token('dem',_)], tag(T1), [token('\.',_)],
[token('und',_)], tag(T), [token('\.',_)], monat(M),
{resoud(day,Nexp), recorded(exp_temp3,(Nexp,J))}.

%%% 1.1.5 Zwischen Jahreszeiten %%%%%%%%%%%%%%%%%%%%%

%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..season1:S1..month1:N11..month2:N12..month3:N13..year:J))
                            ..date:(type:ordinary..grain:month..season2:S2..month1:N21..month2:N22..month3:N23..year:J)))
                    ..intpr:(begin:(day:1..month:N11..year:J)..end:(day:NT2..month:N23..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), saison(S1,N11,N12,N13), [token('und',_)],
saison(S2,N21,N22,N23), jahr(J), {leapyear(J), (monthdays1(_,N23,Res);monthdays(_,N23,Res)), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..season1:S1..month1:N11..month2:N12..month3:N13..year:J))
                            ..date:(type:ordinary..grain:month..season2:S2..month1:N21..month2:N22..month3:N23..year:J)))
                    ..intpr:(begin:(day:1..month:N11..year:J)..end:(day:NT2..month:N23..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), saison(S1,N11,N12,N13), [token('und',_)],
saison(S2,N21,N22,N23), jahr(J), {monthdays(_,N23,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp3,(Nexp,J))}.

%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..season1:S1..month1:N11..month2:N12..month3:N13..year:J1))
                            ..date:(type:ordinary..grain:month..season2:S2..month1:N21..month2:N22..month3:N23..year:J)))
                    ..intpr:(begin:(day:1..month:N11..year:J1)..end:(day:NT2..month:N23..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), saison(S1,N11,N12,N13), jahr(J1), [token('und',_)],
saison(S2,N21,N22,N23), jahr(J), {leapyear(J), (monthdays1(_,N23,Res);monthdays(_,N23,Res)), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..season1:S1..month1:N11..month2:N12..month3:N13..year:J1))
                            ..date:(type:ordinary..grain:month..season2:S2..month1:N21..month2:N22..month3:N23..year:J)))
                    ..intpr:(begin:(day:1..month:N11..year:J1)..end:(day:NT2..month:N23..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), saison(S1,N11,N12,N13), jahr(J1), [token('und',_)],
saison(S2,N21,N22,N23), jahr(J), {monthdays(_,N23,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp3,(Nexp,J))}.


%%% 1.1.6 Zwischen Ereignissen %%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:day..value:name))
                            ..date:(type:ordinary..grain:month..value:name)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:T..month:M..year:J)))))
--> ([token('Zwischen',_)] ; [token('zwischen',_)]), recognizeEvent(T1,M1), [token('und',_)],
recognizeEvent(T,M), jahr(J), {incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,T)),
recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.


%%% 1.1 Perioden "VON - BIS" %%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%% 1.1.2 Zwischen Jahren %%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:year..value:J1))
                            ..date:(type:ordinary..grain:year..value:J)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]), jahr(J1), finPeriode, jahr(J),
{incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,J1)), recorda(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:year..value:J1))
                            ..date:(type:ordinary..grain:year..value:J)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]), jahr(J1), finPeriode, recognizeEvent(T,M,J),
{incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,T)), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.



%%% 1.1.3 Zwischen Monaten %%%%%%%%%%%%%%%%%%%%%

%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)] ; [token('Vom',_)] ; [token('vom',_)]), monat(M1), finPeriode, monat(M),
{resoud(month,Nexp), recorded(exp_temp3,(Nexp,J)), leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)] ; [token('Vom',_)] ; [token('vom',_)]), monat(M1), finPeriode, monat(M),
{monthdays(_,M,Res), NT2 is Res, resoud(month,Nexp), recorded(exp_temp3,(Nexp,J))}.

%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)];[token('Vom',_)] ; [token('vom',_)]), monat(M1), finPeriode, monat(M), jahr(J),
{leapyear(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,M1)), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)];[token('Vom',_)] ; [token('vom',_)]), monat(M1), finPeriode, monat(M), jahr(J),
{monthdays(_,M,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,M1)), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.

%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J1)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]; [token('Vom',_)] ; [token('vom',_)]), monat(M1), jahr(J1), finPeriode, monat(M), jahr(J),
{leapyear(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J1)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]; [token('Vom',_)] ; [token('vom',_)]), monat(M1), jahr(J1), finPeriode, monat(M), jahr(J),
{monthdays(_,M,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.

%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:year..value:J1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]; [token('Vom',_)] ; [token('vom',_)]), jahr(J1), finPeriode, monat(M), jahr(J),
{leapyear(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:year..value:J1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]; [token('Vom',_)] ; [token('vom',_)]), jahr(J1), finPeriode, monat(M), jahr(J),
{monthdays(_,M,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.

%%% 1.1.3.1 Monate und Relatife %%%%%%%%%%%%%

%%% 1.1.3.1.1 Anfang %%%%%%%%%%%%%%%%%%%%%
periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:T..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]; [token('Vom',_)] ; [token('vom',_)]), monat(M1), finPeriode, relatif(debut), monat(M),
{T is 9, resoud(month,Nexp), recorded(exp_temp3,(Nexp,J))}.

%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]), relatif(debut), monat(M1), finPeriode, monat(M),
{T1 is 1, resoud(month,Nexp), recorded(exp_temp3,(Nexp,J)), leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]), relatif(debut), monat(M1), finPeriode, monat(M),
{T1 is 1, monthdays(_,M,Res), NT2 is Res, resoud(month,Nexp), recorded(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:T..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]; [token('Vom',_)] ; [token('vom',_)]), monat(M1), finPeriode, relatif(debut), monat(M),
jahr(J), {T is 9, incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,T)), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}.

%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]), relatif(debut), monat(M1), finPeriode, monat(M),
jahr(J), {T1 is 1, leapyear(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]), relatif(debut), monat(M1), finPeriode, monat(M),
jahr(J), {T1 is 1, monthdays(_,M,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}.

%%% 1.1.3.1.2 Mitte %%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:T..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]; [token('Vom',_)] ; [token('vom',_)]), monat(M1), finPeriode, relatif(milieu), monat(M),
{T is 19, resoud(month,Nexp), recorded(exp_temp3,(Nexp,J))}.

%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]), relatif(milieu), monat(M1), finPeriode, monat(M),
{T1 is 11, resoud(month,Nexp), recorded(exp_temp3,(Nexp,J)), leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]), relatif(milieu), monat(M1), finPeriode, monat(M),
{T1 is 11, monthdays(_,M,Res), NT2 is Res, resoud(month,Nexp), recorded(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:T..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]; [token('Vom',_)] ; [token('vom',_)]), monat(M1), finPeriode, relatif(milieu), monat(M),
jahr(J), {T is 19, incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,T)), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}.

%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]), relatif(milieu), monat(M1), finPeriode, monat(M),
jahr(J), {T1 is 11, leapyear(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]), relatif(milieu), monat(M1), finPeriode, monat(M),
jahr(J), {T1 is 11, monthdays(_,M,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}.

%%% 1.1.3.1.3 Ende %%%%%%%%%%%%%%%%%%

%%%%%%%%% Februar-Schaltjahr-Check %%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]; [token('Vom',_)] ; [token('vom',_)]), monat(M1), finPeriode, relatif(fin), monat(M),
{resoud(month,Nexp), recorded(exp_temp3,(Nexp,J)), leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]; [token('Vom',_)] ; [token('vom',_)]), monat(M1), finPeriode, relatif(fin), monat(M),
{monthdays(_,M,Res), NT2 is Res, resoud(month,Nexp), recorded(exp_temp3,(Nexp,J))}.

%%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]), relatif(fin), monat(M1), finPeriode, monat(M),
{T1 is 25, resoud(month,Nexp), recorded(exp_temp3,(Nexp,J)), leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]), relatif(fin), monat(M1), finPeriode, monat(M),
{T1 is 25, monthdays(_,M,Res), NT2 is Res, resoud(month,Nexp), recorded(exp_temp3,(Nexp,J))}.

%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]; [token('Vom',_)] ; [token('vom',_)]), monat(M1), finPeriode, relatif(fin), monat(M),
jahr(J), {leapyear(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,NT2)), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]; [token('Vom',_)] ; [token('vom',_)]), monat(M1), finPeriode, relatif(fin), monat(M),
jahr(J), {monthdays(_,M,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,NT2)), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]), relatif(fin), monat(M1), finPeriode, monat(M),
jahr(J), {T1 is 25, leapyear(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..value:M1))
                            ..date:(type:ordinary..grain:month..value:M)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:NT2..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]), relatif(fin), monat(M1), finPeriode, monat(M),
jahr(J), {T1 is 25, monthdays(_,M,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}.

%%% 1.1.4 Zwischen Tagen %%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:day..value:T1))
                            ..date:(type:ordinary..grain:day..value:T)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:T..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]; [token('Vom',_)] ; [token('vom',_)]), # [token('dem',_)], tag(T1), [token('\.',_)], monat(M1),
finPeriode, tag(T), [token('\.',_)], monat(M), jahr(J),
{incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,T)), recorda(exp_temp,(Nexp,M1)), 
recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.


periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:day..value:T1))
                            ..date:(type:ordinary..grain:day..value:T)))
                    ..intpr:(begin:(day:T1..month:M1..year:J1)..end:(day:T..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]; [token('Vom',_)] ; [token('vom',_)]), # [token('dem',_)], tag(T1), [token('\.',_)], monat(M1), jahr(J1),
finPeriode, # [token('dem',_)], tag(T), [token('\.',_)], monat(M), jahr(J),
{incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,T)), recorda(exp_temp,(Nexp,M1)), 
recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:day..value:T1))
                            ..date:(type:ordinary..grain:day..value:T)))
                    ..intpr:(begin:(day:T1..month:M..year:J)..end:(day:T..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]; [token('Vom',_)] ; [token('vom',_)]), # [token('dem',_)], tag(T1), [token('\.',_)],
finPeriode, tag(T), [token('\.',_)], monat(M),
{resoud(day,Nexp), recorded(exp_temp3,(Nexp,J))}.

%%% 1.1.5 Zwischen Jahreszeiten %%%%%%%%%%%%%%%%%%%%%

%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..season1:S1..month1:N11..month2:N12..month3:N13..year:J))
                            ..date:(type:ordinary..grain:month..season2:S2..month1:N21..month2:N22..month3:N23..year:J)))
                    ..intpr:(begin:(day:1..month:N11..year:J)..end:(day:NT2..month:N23..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]; [token('Vom',_)] ; [token('vom',_)]), saison(S1,N11,N12,N13), finPeriode,
saison(S2,N21,N22,N23), jahr(J), {leapyear(J), (monthdays1(_,N23,Res);monthdays(_,N23,Res)), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..season1:S1..month1:N11..month2:N12..month3:N13..year:J))
                            ..date:(type:ordinary..grain:month..season2:S2..month1:N21..month2:N22..month3:N23..year:J)))
                    ..intpr:(begin:(day:1..month:N11..year:J)..end:(day:NT2..month:N23..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]; [token('Vom',_)] ; [token('vom',_)]), saison(S1,N11,N12,N13), finPeriode,
saison(S2,N21,N22,N23), jahr(J), {monthdays(_,N23,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..season1:S1..month1:N11..month2:N12..month3:N13..year:J1))
                            ..date:(type:ordinary..grain:month..season2:S2..month1:N21..month2:N22..month3:N23..year:J)))
                    ..intpr:(begin:(day:1..month:N11..year:J1)..end:(day:NT2..month:N23..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]; [token('Vom',_)] ; [token('vom',_)]), saison(S1,N11,N12,N13), jahr(J1), finPeriode,
saison(S2,N21,N22,N23), jahr(J), {leapyear(J), (monthdays1(_,N23,Res);monthdays(_,N23,Res)), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:month..season1:S1..month1:N11..month2:N12..month3:N13..year:J1))
                            ..date:(type:ordinary..grain:month..season2:S2..month1:N21..month2:N22..month3:N23..year:J)))
                    ..intpr:(begin:(day:1..month:N11..year:J1)..end:(day:NT2..month:N23..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]; [token('Vom',_)] ; [token('vom',_)]), saison(S1,N11,N12,N13), jahr(J1), finPeriode,
saison(S2,N21,N22,N23), jahr(J), {monthdays(_,N23,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp3,(Nexp,J))}.

%%% 1.1.6 Zwischen Ereignissen %%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (intervall:
                            ((date:(type:ordinary..grain:day..value:name))
                            ..date:(type:ordinary..grain:month..value:name)))
                    ..intpr:(begin:(day:T1..month:M1..year:J)..end:(day:T..month:M..year:J)))))
--> ([token('Von',_)] ; [token('von',_)]; [token('Vom',_)] ; [token('vom',_)]), recognizeEvent(T1,M1), finPeriode,
recognizeEvent(T,M), jahr(J), {incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,T)),
recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.




%%% 1.2 Perioden - ANFANG EINER PERIODE %%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%% 1.2.1 Tagesangaben %%%%%%%%%%%%%%%%%%%%%%%%


periode(temporal:
                    (type:anaphoric..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:(day:T..reference:minus1)))
                        ..intpr:(begin:(day:NT..month:M..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, [token('dem',_), token('Vortag',_)], {resoud(day,Nexp), studyPeriode(_,J2),
recorded(exp_temp1,(Nexp,T)), recorded(exp_temp2,(Nexp,M)), recorded(exp_temp3,(Nexp,J)), NT is T - 1 }.

periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(day:T..month:M..year:J)))
                    ..intpr:(begin:(day:T..month:M..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, # timeofday(daytime), tag(T), [token('\.',_)], monat(M), jahr(J), {incCounter(idExpression,Nexp),
studyPeriode(_,J2), recorda(exp_temp1,(Nexp,T)), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}, !.


periode(temporal:
                (type:incomplete..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(day:T..month:M)))
                    ..intpr:(begin:(day:T..month:M..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, # timeofday(daytime), tag(T), [token('\.',_)], monat(M), {resoud(day,Nexp), studyPeriode(_,J2),
recorded(exp_temp3,(Nexp,J))}, !.


periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:nacht..value:(nacht:T..month:M..year:J)))
                    ..intpr:(begin:(day:T..month:M..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, [token('der',_), token('Nacht',_), token('zum',_)], tag(T), [token('\.',_)], monat(M),  jahr(J),
{incCounter(idExpression,Nexp), studyPeriode(_,J2), recorda(exp_temp1,(Nexp,T)), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}, !.

periode(temporal:
                (type:incomplete..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:nacht..value:(nacht:T..month:M)))
                    ..intpr:(begin:(day:T..month:M..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, [token('der',_), token('Nacht',_), token('zum',_)], tag(T), [token('\.',_)], monat(M),
{resoud(day,Nexp), studyPeriode(_,J2), recorded(exp_temp3,(Nexp,J))}, !.

%% 1.2.2 Monatsangaben %%%%%%%%%%%%%%%%%%%%


periode(temporal:
                (type:incomplete..ref_number:Nexp..periode:
                    (asem:
                            (date:(type:begin..grain:month..value:(month:M)))
                    ..intpr:(begin:(day:NT1..month:M..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, relatif(debut), monat(M), { NT1 is 5, studyPeriode(_,J2), resoud(month,Nexp), recorded(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                            (date:(type:begin..grain:month..value:(month:M..year:J)))
                    ..intpr:(begin:(day:NT1..month:M..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, relatif(debut), monat(M), jahr(J), { NT1 is 5, studyPeriode(_,J2), incCounter(idExpression,Nexp), 
recorda(exp_temp1,(Nexp,NT1)), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.


periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                            (date:(type:mitte..grain:month..value:(month:M..year:J)))
                    ..intpr:(begin:(day:T1..month:M..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, relatif(milieu), monat(M), jahr(J), { T1 is 18, studyPeriode(_,J2), incCounter(idExpression,Nexp), 
recorda(exp_temp1,(Nexp,T1)), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:incomplete..ref_number:Nexp..periode:
                    (asem:
                            (date:(type:mitte..grain:month..value:(month:M)))
                    ..intpr:(begin:(day:NT1..month:M..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, relatif(milieu), monat(M), { NT1 is 18, studyPeriode(_,J2), resoud(month,Nexp),
recorded(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                            (date:(type:end..grain:month..value:(month:M..year:J)))
                    ..intpr:(begin:(day:NT1..month:M..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, relatif(fin), monat(M), jahr(J), { NT1 is 31, studyPeriode(_,J2), incCounter(idExpression,Nexp), 
recorda(exp_temp,(Nexp,NT1)), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:incomplete..ref_number:Nexp..periode:
                    (asem:
                            (date:(type:end..grain:month..value:(month:M)))
                    ..intpr:(begin:(day:NT1..month:M..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, relatif(fin), monat(M), { NT1 is 31, studyPeriode(_,J2), resoud(month,Nexp),
recorded(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:anaphoric..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(month:M..year:reference))
                    ..intpr:(begin:(day:1..month:M..year:J)..end:(day:31..month:12..year:J2))))))
--> debutPeriode, monat(M), # [token('des',_)], ([token('gleichen',_)] ;[token('selben',_)]), [token('Jahres',_)],
{resoud(month,Nexp), studyPeriode(_,J2), recorded(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(month:M..year:J)))
                    ..intpr:(begin:(day:1..month:M..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, monat(M), jahr(J), {incCounter(idExpression,Nexp), studyPeriode(_,J2),
recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}, !.


periode(temporal:
                (type:incomplete..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(month:M)))
                    ..intpr:(begin:(day:1..month:M..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, monat(M), {resoud(month,Nexp), studyPeriode(_,J2), recorded(exp_temp3,(Nexp,J))}.


%%% 1.2.3 Angaben zur Jahreszeit %%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                            (date:(type:begin..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3..year:J)))
                    ..intpr:(begin:(day:1..month:N1..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, relatif(debut), saison(S,N1,N2,N3), jahr(J), {incCounter(idExpression,Nexp), studyPeriode(_,J2),
recorda(exp_temp2,(Nexp,N1)), recorda(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:incomplete..ref_number:Nexp..periode:
                    (asem:
                            (date:(type:begin..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3)))
                    ..intpr:(begin:(day:1..month:N1..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, relatif(debut), saison(S,N1,N2,N3), {resoud(year,Nexp), studyPeriode(_,J2),
recorded(exp_temp3,(Nexp,J))}.


periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                            (date:(type:mitte..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3..year:J)))
                    ..intpr:(begin:(day:1..month:N2..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, relatif(milieu), saison(S,N1,N2,N3), jahr(J), {incCounter(idExpression,Nexp), studyPeriode(_,J2),
recorda(exp_temp2,(Nexp,N2)),recorda(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:incomplete..ref_number:Nexp..periode:
                    (asem:
                            (date:(type:mitte..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3)))
                    ..intpr:(begin:(day:1..month:N2..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, relatif(milieu), saison(S,N1,N2,N3), {resoud(year,Nexp), studyPeriode(_,J2), 
recorded(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                            (date:(type:end..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3..year:J)))
                    ..intpr:(begin:(day:1..month:N3..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, relatif(fin), saison(S,N1,N2,N3), jahr(J), {incCounter(idExpression,Nexp), studyPeriode(_,J2),
recorda(exp_temp2,(Nexp,N3)), recorda(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:incomplete..ref_number:Nexp..periode:
                    (asem:
                            (date:(type:end..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3)))
                    ..intpr:(begin:(day:1..month:N3..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, relatif(fin), saison(S,N1,N2,N3), {resoud(year,Nexp), studyPeriode(_,J2), 
recorded(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                            (date:(type:begin..grain:season..value:(season:S..month:N1..year:J)))
                    ..intpr:(begin:(day:1..month:N1..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, saison(S,N1), jahr(J), {incCounter(idExpression,Nexp), studyPeriode(_,J2),
recorda(exp_temp2,(Nexp,N1)), recorda(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:incomplete..number:Nexp..relatif:
                    (asem:
                            (date:(type:begin..grain:season..value:(season:S..month:N1)))
                    ..intpr:(begin:(day:1..month:N1..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, saison(S,N1), {resoud(year,Nexp), studyPeriode(_,J2), 
recorded(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:relation..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3..year:J))))
                    ..intpr:(begin:(day:1..month:N1..year:J)..end:(day:31..month:12..year:J2))))
--> debutPeriode, saison(S,N1,N2,N3), jahr(J), {incCounter(idExpression,Nexp), studyPeriode(_,J2),
recorda(exp_temp,(Nexp,N1)), recorda(exp_temp3,(Nexp,J))}, !.

periode(temporal:
                (type:incomplete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3)))
                    ..intpr:(begin:(day:1..month:N1..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, saison(S,N1,N2,N3), {resoud(year,Nexp), studyPeriode(_,J2), recorded(exp_temp3,(Nexp,J))}, !.


%%% 1.2.4 Jahresangaben %%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (beginning:
                            (date:(type:ordinary..grain:year..value:(year:J))))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, relatif(debut), jahr(J), { M1 is 1, studyPeriode(_,J2), incCounter(idExpression,Nexp),
recorda(exp_temp,(Nexp,M1)), recorda(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (middle:
                            (date:(type:ordinary..grain:year..value:(year:J))))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, relatif(milieu), jahr(J), { M1 is 7, studyPeriode(_,J2), incCounter(idExpression,Nexp),
recorda(exp_temp,(Nexp,M1)), recorda(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (end:
                            (date:(type:ordinary..grain:year..value:(year:J))))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, relatif(fin), jahr(J), { M1 is 11, studyPeriode(_,J2), incCounter(idExpression,Nexp), 
recorda(exp_temp,(Nexp,M1)), recorda(exp_temp3,(Nexp,J))}.


periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(jahr1:J..season:S..monat1:N1..monat2:N2..monat3:N3))
                        ..date:(type:ordinary..grain:year..value:(jahr2:NJ2)))
                    ..intpr:(begin:(day:1..month:N1..J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, saison(S,N1,N2,N3), jahr(J), [token('\/',_)], jahrzweistellig(J3), {decode_annee(J,J3,NJ2), 
studyPeriode(_,J2), incCounter(idExpression,Nexp),recorda(exp_temp2,(Nexp,N1)), recorda(exp_temp,(Nexp,J))}.


periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:year..value:(year:J)))
                    ..intpr:(begin:(day:1..month:1..year:J)..end:(day:31..month:12..year:J2)))))

--> debutPeriode, jahr(J), { studyPeriode(_,J2), incCounter(idExpression,Nexp), recorda(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:year..value:(jahr1:J1))
                        ..date:(type:ordinary..grain:year..value:(jahr2:J)))
                    ..intpr:(begin:(day:1..month:1..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, # [token('dem',_)], # [token('zum',_)], #
[token('Jahreswechsel',_)], jahr(J1), [token('\/',_)],
jahrzweistellig(J3), {decode_annee(J1,J3,J), studyPeriode(_,J2), incCounter(idExpression,Nexp),
recorda(exp_temp3,(Nexp,J))}.

%%% 1.2.5 Angaben zu Jahrzehnten %%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (operator:
                        (begin:
                            (date:(type:ordinary..grain:jahrzehnt..value:(year:Jz1..year:Jz2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2))))))
--> debutPeriode, relatif(debut), [token('der',_)], jahrzehnt(Jz1,Jz2),
[token('Jahre',_)], {J1 is((((Jz2-Jz1)/3)-2)+Jz1), studyPeriode(_,J2), incCounter(idExpression,Nexp),
recorda(exp_temp,(Nexp,J1))}.


periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (operator:
                        (mitte:
                            (date:(type:ordinary..grain:jahrzehnt..value:(year:Jz1..year:Jz2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2))))))
--> debutPeriode, relatif(milieu), [token('der',_)], jahrzehnt(Jz1,Jz2),
[token('Jahre',_)], {J1 is(((((Jz2-Jz1)/3)*2)-2)+Jz1), studyPeriode(_,J2), incCounter(idExpression,Nexp),
recorda(exp_temp,(Nexp,J1))}.


periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (operator:(end:
                            (date:(type:ordinary..grain:jahrzehnt..value:(year:Jz1..year:Jz2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2))))))
--> debutPeriode, relatif(fin), [token('der',_)], jahrzehnt(Jz1,Jz2),
[token('Jahre',_)], {J1 is (((Jz2-Jz1)-2)+Jz1), studyPeriode(_,J2), incCounter(idExpression,Nexp),
recorda(exp_temp,(Nexp,J1))}.

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (operator:(end:
                            (date:(type:ordinary..grain:jahrzehnt..value:(year:Jz1..year:Jz2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2))))))
--> debutPeriode, [token('den',_), token('ausgehenden',_)], jahrzehnt(Jz1,Jz2),
[token('Jahren',_)], {J1 is (((Jz2-Jz1)-2)+Jz1), studyPeriode(_,J2), incCounter(idExpression,Nexp),
recorda(exp_temp,(Nexp,J1))}.


periode(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (date:
                            (date:(type:ordinary..grain:jahrzehnt..value:(year:J1..year:Jz2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, [token('den',_)], jahrzehnt(J1,Jz2), [token('Jahren',_)], {incCounter(idExpression,Nexp),
studyPeriode(_,J2), recorda(exp_temp,(Nexp,J1))}.


%%% 1.2.6 Angaben zu Jahrhunderten %%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (operator:
                        (anfang:
                            (date:(type:ordinary..grain:jahrhundert..value:(century:C..date:NJ2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2))))))
--> debutPeriode, relatif(debut), [token('des',_)], siecle(C, J1, NJ2), [token('\.'), token('Jahrhunderts',_)],
{incCounter(idExpression,Nexp), studyPeriode(_,J2), recorda(exp_temp,(Nexp,J1))}.

periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:century..value:(century:C..date:NJ2)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, siecle(C, J1, NJ2), [token('\.'), token('Jahrhundert')], {incCounter(idExpression,Nexp),
studyPeriode(_,J2), recorda(exp_temp,(Nexp,J1))}.

periode(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (operator:
                        (anfang:
                            (date:(type:ordinary..grain:jahrhundert..value:(century:C..date:NJ2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2))))))
--> debutPeriode, relatif(debut), [token('des',_)], siecle(C, J1, NJ2), [token('\.'), token('Jhds',_)],
{incCounter(idExpression,Nexp), studyPeriode(_,J2), recorda(exp_temp,(Nexp,J1))}.

periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:century..value:(century:C..date:NJ2)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, siecle(C, J1, NJ2), [token('\.'), token('Jhs')], {incCounter(idExpression,Nexp),
studyPeriode(_,J2), recorda(exp_temp,(Nexp,J1))}.

periode(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (operator:
                        (anfang:
                            (date:(type:ordinary..grain:jahrhundert..value:(century:C..date:NJ2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2))))))
--> debutPeriode, relatif(debut), [token('des',_)], siecle(C, J1, NJ2), [token('\.'), token('Jhd',_)],
{incCounter(idExpression,Nexp), studyPeriode(_,J2), recorda(exp_temp,(Nexp,J1))}.

periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:century..value:(century:C..date:NJ2)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, siecle(C, J1, NJ2), [token('\.'), token('Jh')], {incCounter(idExpression,Nexp),
studyPeriode(_,J2), recorda(exp_temp,(Nexp,J1))}.


periode(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (operator:
                        (anfang:
                            (date:(type:ordinary..grain:jahrhundert..value:(century:C..date:NJ2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2))))))
--> debutPeriode, relatif(debut), [token('des',_)], siecle(C, J1, NJ2), [token('\.'), token('Jhds.',_)],
{incCounter(idExpression,Nexp), studyPeriode(_,J2), recorda(exp_temp,(Nexp,J1))}.

periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:century..value:(century:C..date:NJ2)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, siecle(C, J1, NJ2), [token('\.'), token('Jhs.')], {incCounter(idExpression,Nexp),
studyPeriode(_,J2), recorda(exp_temp,(Nexp,J1))}.

periode(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (operator:
                        (anfang:
                            (date:(type:ordinary..grain:jahrhundert..value:(century:C..date:NJ2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2))))))
--> debutPeriode, relatif(debut), [token('des',_)], siecle(C, J1, NJ2), [token('\.'), token('Jhd.',_)],
{incCounter(idExpression,Nexp), studyPeriode(_,J2), recorda(exp_temp,(Nexp,J1))}.

periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:century..value:(century:C..date:NJ2)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, siecle(C, J1, NJ2), [token('\.'), token('Jh.')], {incCounter(idExpression,Nexp),
studyPeriode(_,J2), recorda(exp_temp,(Nexp,J1))}.


%%%%%%%% 1.2.7 Perioden und Ereignisse

periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(det:Det..adv:Adv..adj:Adj)))
                    ..intpr:(begin:(day:T..month:M..year:J)..end:(day:31..month:12..year:J2)))))
--> debutPeriode, # findDet(Det), # findAdv(Adv), # findAdj(Adj), recognizeEvent(T,M,J), { studyPeriode(_,J2), incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,T)),
recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J)) }.


%%% 1.3 Perioden - ENDE EINER PERIODE %%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%% 1.3.1 Tagesangaben %%%%%%%%%%%%%%%%%%%

%%%%%%%%% SONDERREGELN F�R DIE BERECHNUNG DES VORTAGES %%%%%%%%%%%%%%%%%%%%%%%%

%%%%%%%% 1. Regel f�r den Fall dass der Tag auf den sich "Vortag bezieht der 1. M�rz eines Schaltjahrs ist -->
%%%%%%%% --> Schaltjahr-Abfrage n�tig!!!!!!!! 

periode(temporal:
                    (type:anaphoric..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:(day:1..reference:minus1)))
                        ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:NT2..month:NM..year:J)))))
--> finPeriode, [token('zum',_), token('Vortag',_)], {resoud(day,Nexp), studyPeriode(J1,_),
recorded(exp_temp1,(Nexp,1)), recorded(exp_temp2,(Nexp,M)), recorded(exp_temp3,(Nexp,J)), NM is M - 1,
leapyear_dict(J), (monthdays1(_,NM,Res);monthdays(_,NM,Res)), NT2 is Res }.

%%%%%%% 2. Regel f�r den Fall dass der Tag auf den sich Vortag bezieht der 1. eines anderen Monats ist (NICHT
%%%%%%%M�rz und NICHT Januar). Eine Schaltjahrpr�fung ist somit nicht notwendig!!! 

periode(temporal:
                    (type:anaphoric..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:(day:1..reference:minus1)))
                        ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:NT2..month:NM..year:J)))))
--> finPeriode, [token('zum',_), token('Vortag',_)], {resoud(day,Nexp), studyPeriode(J1,_),
recorded(exp_temp1,(Nexp,1)), recorded(exp_temp2,(Nexp,M)), recorded(exp_temp3,(Nexp,J)), NM is M - 1,
monthdays(_,NM,Res), NT2 is Res }.

%%%%%%% 3. Regel f�r den Fall dass der Tag auf den sich Vortag bezieht der 1. Januar ist.
%%%%%%% Die richtige Berechnung des Vortages w�re somit der 31. 12. des vergangenen Jahres!!! 

periode(temporal:
                    (type:anaphoric..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:(day:1..reference:minus1)))
                        ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:NJ)))))
--> finPeriode, [token('zum',_), token('Vortag',_)], {resoud(day,Nexp), studyPeriode(J1,_),
recorded(exp_temp1,(Nexp,1)), recorded(exp_temp2,(Nexp,1)), recorded(exp_temp3,(Nexp,J)), NJ is J-1}.

%%%%%%% Regel f�r den Normalfall: Tag ist nicht der 1. und es kann somit ganz normal ein Tag subtrahiert
%%%%%%% werden 

periode(temporal:
                    (type:anaphoric..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:(day:T..reference:minus1)))
                        ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:NT..month:M..year:J)))))
--> finPeriode, [token('zum',_), token('Vortag',_)], {resoud(day,Nexp), studyPeriode(J1,_),
recorded(exp_temp1,(Nexp,T)), recorded(exp_temp2,(Nexp,M)), recorded(exp_temp3,(Nexp,J)), NT is T - 1 }.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(day:T..month:M..year:J)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T..month:M..year:J)))))
--> finPeriode, # timeofday(daytime), # [token('zum',_)], tag(T), [token('\.',_)], monat(M), jahr(J), {incCounter(idExpression,Nexp),
studyPeriode(J1,_), recorda(exp_temp1,(Nexp,T)), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}, !.


periode(temporal:
                (type:incomplete..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(day:T..month:M)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T..month:M..year:J)))))
--> finPeriode, # timeofday(daytime), # [token('zum',_)], tag(T), [token('\.',_)], monat(M), {resoud(day,Nexp), studyPeriode(J1,_),
recorded(exp_temp3,(Nexp,J))}, !.


periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:nacht..value:(nacht:T..month:M..year:J)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T..month:M..year:J)))))
--> finPeriode, [token('zur',_), token('Nacht',_), token('zum',_)], tag(T), [token('\.',_)], monat(M),  jahr(J),
{incCounter(idExpression,Nexp), studyPeriode(J1,_), recorda(exp_temp1,(Nexp,T)), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}, !.

periode(temporal:
                (type:incomplete..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:nacht..value:(nacht:T..month:M)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T..month:M..year:J)))))
--> finPeriode, [token('zur',_), token('Nacht',_), token('zum',_)], tag(T), [token('\.',_)], monat(M),
{resoud(day,Nexp), studyPeriode(J1,_), recorded(exp_temp3,(Nexp,J))}, !.


%% 1.2.2 Monatsangaben %%%%%%%%%%%%%%%%%%%%%%%


periode(temporal:
                (type:incomplete..ref_number:Nexp..periode:
                    (asem:
                            (date:(type:begin..grain:month..value:(month:M)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:M..year:J)))))
--> finPeriode, relatif(debut), monat(M), { T1 is 5, studyPeriode(J1,_), resoud(year,Nexp), recorded(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                            (date:(type:begin..grain:month..value:(month:M..year:J)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:M..year:J)))))
--> finPeriode, relatif(debut), monat(M), jahr(J), { T1 is 5, studyPeriode(J1,_), incCounter(idExpression,Nexp), 
recorda(exp_temp1,(Nexp,T1)), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.


periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                            (date:(type:mitte..grain:month..value:(month:M..year:J)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:M..year:J)))))
--> finPeriode, relatif(milieu), monat(M), jahr(J), { T1 is 18, studyPeriode(J1,_), incCounter(idExpression,Nexp), 
recorda(exp_temp1,(Nexp,T1)), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:incomplete..ref_number:Nexp..periode:
                    (asem:
                            (date:(type:mitte..grain:month..value:(month:M)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:M..year:J)))))
--> finPeriode, relatif(milieu), monat(M), { T1 is 18, 
studyPeriode(J1,_), resoud(year,Nexp), 
recorded(exp_temp3,(Nexp,J))}. 




 %%%%%%%%  Februar-Schaltjahr-Check %%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                            (date:(type:end..grain:month..value:(month:M..year:J)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:M..year:J)))))
--> finPeriode, relatif(fin), monat(M), jahr(J), {leapyear(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), T1 is Res,
studyPeriode(J1,_), incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,T1)),
 recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                            (date:(type:end..grain:month..value:(month:M..year:J)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:M..year:J)))))
--> finPeriode, relatif(fin), monat(M), jahr(J), {monthdays(_,M,Res), T1 is Res, studyPeriode(J1,_), incCounter(idExpression,Nexp), 
recorda(exp_temp1,(Nexp,T1)), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..ref_number:Nexp..periode:
                    (asem:
                            (date:(type:end..grain:month..value:(month:M)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:M..year:J)))))
--> finPeriode, relatif(fin), monat(M), {studyPeriode(J1,_), resoud(year,Nexp),
recorded(exp_temp3,(Nexp,J)), leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), T1 is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..ref_number:Nexp..periode:
                    (asem:
                            (date:(type:end..grain:month..value:(month:M)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:M..year:J)))))
--> finPeriode, relatif(fin), monat(M), {monthdays(_,M,Res), T1 is Res, studyPeriode(J1,_), resoud(year,Nexp),
recorded(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%

periode(temporal:
                (type:anaphoric..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(month:M..year:reference))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:M..year:J))))))
--> finPeriode, # [token('zum',_)], monat(M), # [token('des',_)], ([token('gleichen',_)] ;[token('selben',_)]), [token('Jahres',_)],
{resoud(year,Nexp), studyPeriode(J1,_), recorded(exp_temp3,(Nexp,J)), leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), T1 is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:anaphoric..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(month:M..year:reference))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:M..year:J))))))
--> finPeriode, # [token('zum',_)], monat(M), # [token('des',_)], ([token('gleichen',_)] ;[token('selben',_)]), [token('Jahres',_)],
{monthdays(_,M,Res), T1 is Res, resoud(year,Nexp), studyPeriode(J1,_), recorded(exp_temp3,(Nexp,J))}.

%%%%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(month:M..year:J)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:M..year:J)))))
--> finPeriode, # [token('zum',_)], monat(M), jahr(J), {leapyear(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), T1 is Res,
resoud(month,Nexp), incCounter(idExpression,Nexp), studyPeriode(J1,_),recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}, !.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(month:M..year:J)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:M..year:J)))))
--> finPeriode, # [token('zum',_)], monat(M), jahr(J), {monthdays(_,M,Res), T1 is Res, resoud(month,Nexp), 
incCounter(idExpression,Nexp), 
studyPeriode(J1,_),recorda(exp_temp2,(Nexp,M)), 
recorda(exp_temp3,(Nexp,J))}, !. 



%%%%%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(month:M)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:M..year:J)))))
--> finPeriode, # [token('zum',_)], monat(M), {resoud(year,Nexp), studyPeriode(J1,_), recorded(exp_temp3,(Nexp,J)),
leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), T1 is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(month:M)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:M..year:J)))))
--> finPeriode, # [token('zum',_)], monat(M), {resoud(year,Nexp), 
studyPeriode(J1,_), recorded(exp_temp3,(Nexp,J)), 
monthdays(_,M,Res), T1 is Res}. 

%%% 1.2.3 Angaben zur Jahreszeit %%%%%%%%%%%%%%%%%%%%

%%%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                            (date:(type:begin..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3..year:J)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:N1..year:J)))))
--> finPeriode, relatif(debut), saison(S,N1,N2,N3), jahr(J), {leapyear(J), (monthdays1(_,N1,Res);monthdays(_,N1,Res)),
T1 is Res, incCounter(idExpression,Nexp), studyPeriode(J1,_),
recorda(exp_temp2,(Nexp,N1)), recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                            (date:(type:begin..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3..year:J)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:N1..year:J)))))
--> finPeriode, relatif(debut), saison(S,N1,N2,N3), jahr(J), {monthdays(_,N1,Res),
T1 is Res, incCounter(idExpression,Nexp), studyPeriode(J1,_),
recorda(exp_temp2,(Nexp,N1)), recorda(exp_temp3,(Nexp,J))}.

%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..ref_number:Nexp..periode:
                    (asem:
                            (date:(type:begin..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:N1..year:J)))))
--> finPeriode, relatif(debut), saison(S,N1,N2,N3), {resoud(year,Nexp), studyPeriode(J1,_),
recorded(exp_temp3,(Nexp,J)), leapyear_dict(J), (monthdays1(_,N1,Res);monthdays(_,N1,Res)), T1 is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..ref_number:Nexp..periode:
                    (asem:
                            (date:(type:begin..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:N1..year:J)))))
--> finPeriode, relatif(debut), saison(S,N1,N2,N3), {resoud(year,Nexp), studyPeriode(J1,_),
recorded(exp_temp3,(Nexp,J)), monthdays(_,N1,Res), T1 is Res}.

%%%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                            (date:(type:mitte..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3..year:J)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:N2..year:J)))))
--> finPeriode, relatif(milieu), saison(S,N1,N2,N3), jahr(J), {leapyear(J), (monthdays1(_,N2,Res);monthdays(_,N2,Res)), T1 is Res, incCounter(idExpression,Nexp), studyPeriode(J1,_),
recorda(exp_temp2,(Nexp,N2)),recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                            (date:(type:mitte..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3..year:J)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:N2..year:J)))))
--> finPeriode, relatif(milieu), saison(S,N1,N2,N3), jahr(J), {monthdays(_,N2,Res), T1 is Res, incCounter(idExpression,Nexp), studyPeriode(J1,_),
recorda(exp_temp2,(Nexp,N2)),recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..ref_number:Nexp..periode:
                    (asem:
                            (date:(type:mitte..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:N2..year:J)))))
--> finPeriode, relatif(milieu), saison(S,N1,N2,N3), {resoud(year,Nexp), studyPeriode(J1,_), 
recorded(exp_temp3,(Nexp,J)), leapyear_dict(J), (monthdays1(_,N2,Res);monthdays(_,N2,Res)), T1 is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..ref_number:Nexp..periode:
                    (asem:
                            (date:(type:mitte..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:N2..year:J)))))
--> finPeriode, relatif(milieu), saison(S,N1,N2,N3), {resoud(year,Nexp), studyPeriode(J1,_), 
recorded(exp_temp3,(Nexp,J)), monthdays(_,N2,Res), T1 is Res}.

%%%%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                            (date:(type:end..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3..year:J)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:N3..year:J)))))
--> finPeriode, relatif(fin), saison(S,N1,N2,N3), jahr(J), {leapyear(J), (monthdays1(_,N3,Res);monthdays(_,N3,Res)), T1 is Res,
incCounter(idExpression,Nexp), studyPeriode(J1,_), recorda(exp_temp2,(Nexp,N3)), recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                            (date:(type:end..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3..year:J)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:N3..year:J)))))
--> finPeriode, relatif(fin), saison(S,N1,N2,N3), jahr(J), {monthdays(_,N3,Res), T1 is Res,
incCounter(idExpression,Nexp), studyPeriode(J1,_), recorda(exp_temp2,(Nexp,N3)), recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..ref_number:Nexp..periode:
                    (asem:
                            (date:(type:end..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:N3..year:J)))))
--> finPeriode, relatif(fin), saison(S,N1,N2,N3), {resoud(year,Nexp), studyPeriode(J1,_), 
recorded(exp_temp3,(Nexp,J)), leapyear_dict(J), (monthdays1(_,N3,Res);monthdays(_,N3,Res)), T1 is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..ref_number:Nexp..periode:
                    (asem:
                            (date:(type:end..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:N3..year:J)))))
--> finPeriode, relatif(fin), saison(S,N1,N2,N3), {resoud(year,Nexp), studyPeriode(J1,_), 
recorded(exp_temp3,(Nexp,J)), monthdays(_,N3,Res), T1 is Res}.

%%% Da nur der Juni in Frage kommt ist Schaltjahrcheck �berfl�ssig

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                            (date:(type:end..grain:season..value:(season:S..month:N1..year:J)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:N1..year:J)))))
--> finPeriode, # [token('zum',_)], saison(S,N1), jahr(J), {monthdays(_,N1,Res), T1 is Res,
incCounter(idExpression,Nexp), studyPeriode(J1,_),
recorda(exp_temp2,(Nexp,N1)), recorda(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:incomplete..number:Nexp..relatif:
                    (asem:
                            (date:(type:end..grain:season..value:(season:S..month:N1)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:N1..year:J)))))
--> finPeriode, # [token('zum',_)], saison(S,N1), {monthdays(_,N1,Res), T1 is Res, resoud(year,Nexp), studyPeriode(J1,_), 
recorded(exp_temp3,(Nexp,J))}.

%%%%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%

periode(temporal:
                (type:relation..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3..year:J))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:N3..year:J))))
--> finPeriode, # [token('zum',_)], saison(S,N1,N2,N3), jahr(J), {leapyear(J), (monthdays1(_,N3,Res);monthdays(_,N3,Res)),
T1 is Res, incCounter(idExpression,Nexp), studyPeriode(J1,_),
recorda(exp_temp,(Nexp,N1)), recorda(exp_temp3,(Nexp,J))}, !.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:relation..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3..year:J))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:N3..year:J))))
--> finPeriode, # [token('zum',_)], saison(S,N1,N2,N3), jahr(J), {monthdays(_,N3,Res),
T1 is Res, incCounter(idExpression,Nexp), studyPeriode(J1,_),
recorda(exp_temp,(Nexp,N1)), recorda(exp_temp3,(Nexp,J))}, !.

%%%%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:N3..year:J)))))
--> finPeriode, # [token('zum',_)], saison(S,N1,N2,N3), {resoud(year,Nexp), studyPeriode(J1,_), recorded(exp_temp3,(Nexp,J)),
leapyear_dict(J), (monthdays1(_,N3,Res);monthdays(_,N3,Res)), T1 is Res}, !.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:incomplete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:N3..year:J)))))
--> finPeriode, # [token('zum',_)], saison(S,N1,N2,N3), {resoud(year,Nexp), studyPeriode(J1,_), recorded(exp_temp3,(Nexp,J)),
monthdays(_,N3,Res), T1 is Res}, !.

%%%%%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(jahr1:J..season:S..monat1:N1..monat2:N2..monat3:N3))
                        ..date:(type:ordinary..grain:year..value:(jahr2:J3)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:N3..year:J)))))
--> finPeriode, saison(S,N1,N2,N3), jahr(J1), [token('\/',_)], jahrzweistellig(J3), {decode_annee(J1,J3,J), 
studyPeriode(J1,_), incCounter(idExpression,Nexp),recorda(exp_temp2,(Nexp,N1)), recorda(exp_temp,(Nexp,J)), leapyear_dict(J),
(monthdays1(_,N3,Res);monthdays(_,N3,Res)), T1 is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(jahr1:J..season:S..monat1:N1..monat2:N2..monat3:N3))
                        ..date:(type:ordinary..grain:year..value:(jahr2:J3)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:N3..year:J)))))
--> finPeriode, saison(S,N1,N2,N3), jahr(J1), [token('\/',_)], jahrzweistellig(J3), {decode_annee(J1,J3,J), 
studyPeriode(J1,_), incCounter(idExpression,Nexp),recorda(exp_temp2,(Nexp,N1)), recorda(exp_temp,(Nexp,J)),
monthdays(_,N3,Res), T1 is Res}.

periode(temporal:
                (type:incomplete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:1..month:N2..year:J)))))
--> finPeriode, [token('in',_)], [token('den',_)], saison(S,N1,N2,N3), jahr(J), [token('hinein',_)], 
{studyPeriode(J1,_), incCounter(idExpression,Nexp), recorda(exp_temp3,(Nexp,J))}, !.

%%% 1.2.4 Jahresangaben %%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (beginning:
                            (date:(type:ordinary..grain:year..value:(year:J))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:T1..month:M2..year:J)))))
--> finPeriode, relatif(debut), jahr(J), { M2 is 4, studyPeriode(J1,_), incCounter(idExpression,Nexp),
recorda(exp_temp,(Nexp,M2)), recorda(exp_temp3,(Nexp,J)), monthdays(_,M2,Res), T1 is Res}.

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (middle:
                            (date:(type:ordinary..grain:year..value:(year:J))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:M2..year:J)))))
--> finPeriode, relatif(milieu), jahr(J), { M2 is 8, studyPeriode(J1,_), incCounter(idExpression,Nexp),
recorda(exp_temp,(Nexp,M2)), recorda(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (end:
                            (date:(type:ordinary..grain:year..value:(year:J))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:M2..year:J)))))
--> finPeriode, relatif(fin), jahr(J), { M2 is 12, studyPeriode(J1,_), incCounter(idExpression,Nexp), 
recorda(exp_temp,(Nexp,M2)), recorda(exp_temp3,(Nexp,J))}.


periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:year..value:(year:J)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J)))))
--> finPeriode, jahr(J), { studyPeriode(J1,_), incCounter(idExpression,Nexp), recorda(exp_temp3,(Nexp,J))}.


periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:year..value:(jahr1:J2))
                        ..date:(type:ordinary..grain:year..value:(jahr2:J)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:1..month:1..year:J)))))
--> finPeriode, (# [token('dem',_)]), (# [token('zum',_)]), (#
[token('Jahreswechsel',_)]), jahr(J2), [token('\/',_)],
jahrzweistellig(J3), {decode_annee(J2,J3,J), studyPeriode(J1,_), incCounter(idExpression,Nexp),
recorda(exp_temp3,(Nexp,J))}.
    
%%% 1.2.5 Angaben zu Jahrzehnten %%%%%%%%%%%%%%%%%%%%%%


periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (operator:
                        (begin:
                            (date:(type:ordinary..grain:jahrzehnt..value:(year:Jz1..year:Jz2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J))))))
--> finPeriode, relatif(debut), [token('der',_)], jahrzehnt(Jz1,Jz2),
[token('Jahre',_)], {J is(((Jz2-Jz1)/3)+Jz1), studyPeriode(J1,_), incCounter(idExpression,Nexp),
recorda(exp_temp3,(Nexp,J))}.


periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (operator:
                        (mitte:
                            (date:(type:ordinary..grain:jahrzehnt..value:(year:Jz1..year:Jz2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J))))))
--> finPeriode, relatif(milieu), [token('der',_)], jahrzehnt(Jz1,Jz2),
[token('Jahre',_)], {J is((((Jz2-Jz1)/3)*2)+Jz1), studyPeriode(J1,_), incCounter(idExpression,Nexp),
recorda(exp_temp3,(Nexp,J))}.



periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (operator:(end:
                            (date:(type:ordinary..grain:jahrzehnt..value:(year:Jz1..year:Jz2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J))))))
--> finPeriode, relatif(fin), [token('der',_)], jahrzehnt(Jz1,Jz2),
[token('Jahre',_)], {J is((Jz2-Jz1)+Jz1), studyPeriode(J1,_), incCounter(idExpression,Nexp),
recorda(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:complete..number:Nexp..periode:
                    (asem:
                        (operator:(end:
                            (date:(type:ordinary..grain:jahrzehnt..value:(year:Jz1..year:Jz2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J))))))
--> finPeriode, [token('in',_), token('die',_), token('ausgehenden',_)], jahrzehnt(Jz1,Jz2),
[token('Jahre',_)], {J is((Jz2-Jz1)+Jz1), studyPeriode(J1,_), incCounter(idExpression,Nexp),
recorda(exp_temp3,(Nexp,J))}.

periode(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (date:
                            (date:(type:ordinary..grain:jahrzehnt..value:(year:J1..year:Jz2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J)))))
--> finPeriode, [token('in',_), token('die',_)], jahrzehnt(Jz1,Jz2), [token('Jahre',_)], 
{J is((((Jz2-Jz1)/3)+2)+Jz1), incCounter(idExpression,Nexp), studyPeriode(J1,_), recorda(exp_temp3,(Nexp,J))}.


%%% 1.2.6 Angaben zu Jahrhunderten %%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (operator:
                        (anfang:
                            (date:(type:ordinary..grain:jahrhundert..value:(century:C..date:J2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J))))))
--> finPeriode, [token('zum',_)], relatif(debut), [token('des',_)], siecle(C, J2, J), [token('\.'), token('Jahrhunderts',_)],
{incCounter(idExpression,Nexp), studyPeriode(J1,_), recorda(exp_temp,(Nexp,J))}.

periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:century..value:(century:C..date:J2)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J)))))
--> finPeriode, [token('zum',_)], siecle(C, J2, J), [token('\.'), token('Jahrhundert')], {incCounter(idExpression,Nexp),
studyPeriode(J1,_), recorda(exp_temp,(Nexp,J))}.



periode(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (operator:
                        (anfang:
                            (date:(type:ordinary..grain:jahrhundert..value:(century:C..date:J2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J))))))
--> finPeriode, [token('zum',_)], relatif(debut), [token('des',_)], siecle(C, J2, J), [token('\.'), token('Jhds',_)],
{incCounter(idExpression,Nexp), studyPeriode(J1,_), recorda(exp_temp,(Nexp,J))}.

periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:century..value:(century:C..date:J2)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J)))))
--> finPeriode, [token('zum',_)], siecle(C, J2, J), [token('\.'), token('Jhs')], {incCounter(idExpression,Nexp),
studyPeriode(J1,_), recorda(exp_temp,(Nexp,J))}.



periode(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (operator:
                        (anfang:
                            (date:(type:ordinary..grain:jahrhundert..value:(century:C..date:J2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J))))))
--> finPeriode, [token('zum',_)], relatif(debut), [token('des',_)], siecle(C, J2, J), [token('\.'), token('Jhd',_)],
{incCounter(idExpression,Nexp), studyPeriode(J1,_), recorda(exp_temp,(Nexp,J))}.

periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:century..value:(century:C..date:J2)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J)))))
--> finPeriode, [token('zum',_)], siecle(C, J2, J), [token('\.'), token('Jh')], {incCounter(idExpression,Nexp),
studyPeriode(J1,_), recorda(exp_temp,(Nexp,J))}.


periode(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (operator:
                        (anfang:
                            (date:(type:ordinary..grain:jahrhundert..value:(century:C..date:J2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J))))))
--> finPeriode, [token('zum',_)], relatif(debut), [token('des',_)], siecle(C, J2, J), [token('\.'), token('Jhds.',_)],
{incCounter(idExpression,Nexp), studyPeriode(J1,_), recorda(exp_temp,(Nexp,J))}.

periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:century..value:(century:C..date:J2)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J)))))
--> finPeriode, [token('zum',_)], siecle(C, J2, J), [token('\.'), token('Jhs.')], {incCounter(idExpression,Nexp),
studyPeriode(J1,_), recorda(exp_temp,(Nexp,J))}.



periode(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (operator:
                        (anfang:
                            (date:(type:ordinary..grain:jahrhundert..value:(century:C..date:J2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J))))))
--> finPeriode, [token('zum',_)], relatif(debut), [token('des',_)], siecle(C, J2, J), [token('\.'), token('Jhd.',_)],
{incCounter(idExpression,Nexp), studyPeriode(J1,_), recorda(exp_temp,(Nexp,J))}.

periode(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:century..value:(century:C..date:J2)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J)))))
--> finPeriode, [token('zum',_)], siecle(C, J2, J), [token('\.'), token('Jh.')], {incCounter(idExpression,Nexp),
studyPeriode(J1,_), recorda(exp_temp,(Nexp,J))}.


%%%%%%%%%% 1.4 ANFANG UND ENDE EINER PERIODE %%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

periode(temporal:
                    (type:incomplete..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:T1))
                                ..(date:(type:ordinary..grain:day..value:T))
                        ..intpr:(begin:(day:T1..month:M..year:J)..end:(day:T..month:M..year:J)))))
--> debutPeriode, tag(T1), [token('\.',_)], finPeriode, [token('zum',_)], tag(T), [token('\.',_)],
monat(M), {resoud(year,Nexp), recorded(exp_temp3,(Nexp,J))}.



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%% Zeitpunkte k�nnen entweder von einem kennzeichnenden Wort
%% eingeleitet werden, oder alleine stehen

ponctuel('O', R) --> [token(Mot,_)], ponctuel('N', R), { prefixePonctuel(Mot) }.
ponctuel('N', D) --> datation(D).
%ponctuel(_, contemp:/) --> contemporain.
ponctuel(D) --> datation(D).

%% Beim Zeitpunkt kann es sich entw um eine festgelegte Zeit oder
%% eine vage Angabe handeln

datation(A) --> anaphoric(A).
datation(F) --> flou(F).
datation(P) --> precis(P), !.


%% Vage Angaben k�nnen Jahre (in den 60er Jahren), Jahrhunderte
%%(Mitte des 20. Jhd) oder Monate sein (Mitte Mai)

flou(S) --> jahre(S).

flou(S) --> siecles(S).

flou(S) --> monate(S).

flou(S) --> jahreszeit(S).

flou(S) --> tage(S).

%%%%%% 2. RELATIVE DATUMSANGABEN %%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%%%%%% 2.1 Relative Tagesangaben %%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

tage(temporal:
                (type:relation..number:nexp..relatif:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(det:Det..adv:Adv..adj:Adj)))
                    ..intpr:(grain:day..operator:(vor:(value:(tage:Tage..ref:NN)))))))
                  --> [token('Wenige',_), token('Tage',_)], [token('vor',_)],
findDet(Det), # findAdv(Adv), # findAdj(Adj), findNoun(NN), {Tage is 3}.


%%%% 2.2 Relative Monatsangaben %%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

monate(temporal:
                (type:incomplete..ref_number:Nexp..relatif:
                    (asem:
                            (date:(type:begin..grain:month..value:(month:M)))
                    ..intpr:(begin:(day:NT1..month:M..year:IntJ)..end:(day:NT2..month:M..year:IntJ)))))
--> relatif(debut), monat(M), { NT1 is 1, NT2 is 5, resoud(year,Nexp), recorded(exp_temp3,(Nexp,IntJ))}.

monate(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                            (date:(type:begin..grain:month..value:(month:M..year:J)))
                    ..intpr:(begin:(day:NT1..month:M..year:J)..end:(day:NT2..month:M..year:J)))))
--> relatif(debut), monat(M), jahr(J), { NT1 is 1, NT2 is 5, incCounter(idExpression,Nexp), 
recorda(exp_temp,(Nexp,NT1)), recorda(exp_temp1,(Nexp,NT2)), recorda(exp_temp2,(Nexp,M)), 
recorda(exp_temp3,(Nexp,J))}.


monate(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                            (date:(type:mitte..grain:month..value:(month:M..year:J)))
                    ..intpr:(begin:(day:T1..month:M..year:J)..end:(day:T2..month:M..year:J)))))
--> relatif(milieu), monat(M), jahr(J), { T1 is 13, T2 is 18, incCounter(idExpression,Nexp), 
recorda(exp_temp,(Nexp,T1)), recorda(exp_temp1,(Nexp,T2)), recorda(exp_temp2,(Nexp,M)), 
recorda(exp_temp3,(Nexp,J))}.

monate(temporal:
                (type:incomplete..ref_number:Nexp..relatif:
                    (asem:
                            (date:(type:mitte..grain:month..value:(month:M)))
                    ..intpr:(begin:(day:NT1..month:M..year:J)..end:(day:NT2..month:M..year:J)))))
--> relatif(milieu), monat(M), { NT1 is 13, NT2 is 18 , resoud(year,Nexp), recorded(exp_temp3,(Nexp,J))}.


%%%% Februar Schaltjahr- Check %%%%


monate(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                            (date:(type:end..grain:month..value:(month:M..year:J)))
                    ..intpr:(begin:(day:NT1..month:M..year:J)..end:(day:NT2..month:M..year:J)))))
--> relatif(fin), monat(M), jahr(J), { NT1 is 27, leapyear(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res, incCounter(idExpression,Nexp), 
recorda(exp_temp,(Nexp,NT1)), recorda(exp_temp1,(Nexp,NT2)), recorda(exp_temp2,(Nexp,M)), 
recorda(exp_temp3,(Nexp,J))}.

monate(temporal:
                (type:incomplete..ref_number:Nexp..relatif:
                    (asem:
                            (date:(type:end..grain:month..value:(month:M)))
                    ..intpr:(begin:(day:NT1..month:M..year:J)..end:(day:NT2..month:M..year:J)))))
--> relatif(fin), monat(M), { NT1 is 27, resoud(year,Nexp), recorded(exp_temp3,(Nexp,J)), leapyear_dict(J),
 (monthdays1(_,M,Res);monthdays(_,M,Res)), NT2 is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
monate(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                            (date:(type:end..grain:month..value:(month:M..year:J)))
                    ..intpr:(begin:(day:NT1..month:M..year:J)..end:(day:NT2..month:M..year:J)))))
--> relatif(fin), monat(M), jahr(J), { NT1 is 27, monthdays(_,M,Res), NT2 is Res, incCounter(idExpression,Nexp), 
recorda(exp_temp,(Nexp,NT1)), recorda(exp_temp1,(Nexp,NT2)), recorda(exp_temp2,(Nexp,M)), 
recorda(exp_temp3,(Nexp,J))}.

monate(temporal:
                (type:incomplete..ref_number:Nexp..relatif:
                    (asem:
                            (date:(type:end..grain:month..value:(month:M)))
                    ..intpr:(begin:(day:NT1..month:M..year:J)..end:(day:NT2..month:M..year:J)))))
--> relatif(fin), monat(M), { NT1 is 27, monthdays(_,M,Res), NT2 is Res, resoud(year,Nexp), recorded(exp_temp3,(Nexp,J))}.

%%%%%%%% 2.3 Relative Jahreszeit %%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


jahreszeit(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                            (date:(type:begin..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3..year:J)))
                    ..intpr:(begin:(day:1..month:N1..year:J)..end:(day:NT2..month:N1..year:J)))))
--> relatif(debut), saison(S,N1,N2,N3), jahr(J), {monthdays(_,N1,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,N1)),
recorda(exp_temp3,(Nexp,J))}.

jahreszeit(temporal:
                (type:incomplete..ref_number:Nexp..relatif:
                    (asem:
                            (date:(type:begin..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3)))
                    ..intpr:(begin:(day:1..month:N1..year:J)..end:(day:NT2..month:N1..year:J)))))
--> relatif(debut), saison(S,N1,N2,N3), {monthdays(_,N1,Res), NT2 is Res, resoud(year,Nexp), recorded(exp_temp3,(Nexp,J))}.


jahreszeit(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                            (date:(type:mitte..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3..year:J)))
                    ..intpr:(begin:(day:1..month:N2..year:J)..end:(day:NT2..month:N2..year:J)))))
--> relatif(milieu), saison(S,N1,N2,N3), jahr(J), {monthdays(_,N2,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,N2)),
recorda(exp_temp3,(Nexp,J))}.

jahreszeit(temporal:
                (type:incomplete..ref_number:Nexp..relatif:
                    (asem:
                            (date:(type:mitte..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3)))
                    ..intpr:(begin:(day:1..month:N2..year:J)..end:(day:NT2..month:N2..year:J)))))
--> relatif(milieu), saison(S,N1,N2,N3), {monthdays(_,N2,Res), NT2 is Res, resoud(year,Nexp), recorded(exp_temp3,(Nexp,J))}.


%%% Bei Ende Winter kann das Ende des Feb eines Schaltjahres involviert sein --> Schaltjahr-Check%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
jahreszeit(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                            (date:(type:end..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3..year:J)))
                    ..intpr:(begin:(day:1..month:N3..year:J)..end:(day:NT2..month:N3..year:J)))))
--> relatif(fin), saison(S,N1,N2,N3), jahr(J), {leapyear(J), (monthdays1(_,N3,Res);monthdays(_,N3,Res)), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,N3)),
recorda(exp_temp3,(Nexp,J))}.

jahreszeit(temporal:
                (type:incomplete..ref_number:Nexp..relatif:
                    (asem:
                            (date:(type:end..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3)))
                    ..intpr:(begin:(day:1..month:N3..year:J)..end:(day:NT2..month:N3..year:J)))))
--> relatif(fin), saison(S,N1,N2,N3), {resoud(year,Nexp), recorded(exp_temp3,(Nexp,J)), leapyear_dict(J), (monthdays1(_,N3,Res);monthdays(_,N3,Res)), NT2 is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


jahreszeit(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                            (date:(type:end..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3..year:J)))
                    ..intpr:(begin:(day:1..month:N3..year:J)..end:(day:NT2..month:N3..year:J)))))
--> relatif(fin), saison(S,N1,N2,N3), jahr(J), {monthdays(_,N3,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp2,(Nexp,N3)),
recorda(exp_temp3,(Nexp,J))}.

jahreszeit(temporal:
                (type:incomplete..ref_number:Nexp..relatif:
                    (asem:
                            (date:(type:end..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3)))
                    ..intpr:(begin:(day:1..month:N3..year:J)..end:(day:NT2..month:N3..year:J)))))
--> relatif(fin), saison(S,N1,N2,N3), {monthdays(_,N3,Res), NT2 is Res, resoud(year,Nexp), recorded(exp_temp3,(Nexp,J))}.

%%% Zwei Regeln f�r Fr�h-/Sp�tsommer

jahreszeit(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                            (date:(type:ordinary..grain:season..value:(season:S..month:N1..year:J)))
                    ..intpr:(begin:(day:1..month:N1..year:J)..end:(day:T..month:N1..year:J)))))
--> saison(S,N1), jahr(J), {monthdays(_,N1,Res), T is Res, incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,T)), 
recorda(exp_temp2,(Nexp,N1)), recorda(exp_temp3,(Nexp,J))}.

jahreszeit(temporal:
                (type:incomplete..number:Nexp..relatif:
                    (asem:
                            (date:(type:ordinary..grain:season..value:(season:S..month:N1)))
                    ..intpr:(begin:(day:1..month:N1..year:J)..end:(day:NT2..month:N1..year:J)))))
--> saison(S,N1), {monthdays(_,N1,Res), NT2 is Res, resoud(year,Nexp), recorded(exp_temp3,(Nexp,J))}.


%%%% 2.4 Relative Jahresangaben %%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%% Februar-Schaltjahrcheck %%%%%%
jahre(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (beginning:
                            (date:(type:ordinary..grain:year..value:(year:J))))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:NT2..month:M2..year:J)))))
--> relatif(debut), jahr(J), { M1 is 1, M2 is 2, leapyear(J), (monthdays1(_,M2,Res);monthdays(_,M2,Res)),
NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,M1)),
recorda(exp_temp2,(Nexp,M2)), recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

jahre(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (beginning:
                            (date:(type:ordinary..grain:year..value:(year:J))))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:NT2..month:M2..year:J)))))
--> relatif(debut), jahr(J), { M1 is 1, M2 is 2, monthdays(_,M2,Res),
NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,M1)),
recorda(exp_temp2,(Nexp,M2)), recorda(exp_temp3,(Nexp,J))}.

jahre(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (middle:
                            (date:(type:ordinary..grain:year..value:(year:J))))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:NT2..month:M2..year:J)))))
--> relatif(milieu), jahr(J), { M1 is 7, M2 is 8, monthdays(_,M2,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,M1)),
recorda(exp_temp2,(Nexp,M2)), recorda(exp_temp3,(Nexp,J))}.

jahre(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (end:
                            (date:(type:ordinary..grain:year..value:(year:J))))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:NT2..month:M2..year:J)))))
--> relatif(fin), jahr(J), { M1 is 11, M2 is 12, monthdays(_,M2,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,M1)),
recorda(exp_temp2,(Nexp,M2)), recorda(exp_temp3,(Nexp,J))}.

jahre(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (end:
                            (date:(type:ordinary..grain:year..value:(year:J))))
                    ..intpr:(begin:(day:1..month:M1..year:J1)..end:(day:NT2..month:M2..year:J)))))
--> relatif(fin), jahr(J1), [token('\/',_)], relatif(debut), jahr(J), { M1 is 12, M2 is 1, 
monthdays(_,M2,Res), NT2 is Res, incCounter(idExpression,Nexp),
recorda(exp_temp2,(Nexp,M2)), recorda(exp_temp3,(Nexp,J))}.

%%%%%% 2.5 Relative Jahrzehnte %%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


jahre(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (operator:
                        (begin:
                            (date:(type:ordinary..grain:jahrzehnt..value:(year:Jz1..year:Jz2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2))))))
--> relatif(debut), [token('der',_)], jahrzehnt(Jz1,Jz2),
[token('Jahre',_)], {J1 is((((Jz2-Jz1)/3)-2)+Jz1), J2 is (((Jz2-Jz1)/3)+Jz1),
incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,J1)), recorda(exp_temp3,(Nexp,J2))}.

jahre(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (operator:
                        (mitte:
                            (date:(type:ordinary..grain:jahrzehnt..value:(year:Jz1..year:Jz2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2))))))
--> relatif(milieu), [token('der',_)], jahrzehnt(Jz1,Jz2),
[token('Jahre',_)], {J1 is(((((Jz2-Jz1)/3)*2)-2)+Jz1), J2 is ((((Jz2-Jz1)/3)*2)+Jz1), 
incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,J1)), recorda(exp_temp3,(Nexp,J2))}.


jahre(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (operator:(end:
                            (date:(type:ordinary..grain:jahrzehnt..value:(year:Jz1..year:Jz2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2))))))
--> relatif(fin), [token('der',_)], jahrzehnt(Jz1,Jz2),
[token('Jahre',_)], {J1 is (((Jz2-Jz1)-2)+Jz1), J2 is Jz2, incCounter(idExpression,Nexp),
recorda(exp_temp,(Nexp,J1)), recorda(exp_temp3,(Nexp,J2))}.

jahre(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (operator:(end:
                            (date:(type:ordinary..grain:jahrzehnt..value:(year:Jz1..year:Jz2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2))))))
--> [token('die',_), token('ausgehenden',_)], jahrzehnt(Jz1,Jz2),
[token('Jahre',_)], {J1 is (((Jz2-Jz1)-2)+Jz1), J2 is Jz2, incCounter(idExpression,Nexp),
recorda(exp_temp,(Nexp,J1)), recorda(exp_temp3,(Nexp,J2))}.


jahre(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (date:
                            (date:(type:ordinary..grain:jahrzehnt..value:(year:J1..year:J2))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2)))))
--> [token('den',_)], jahrzehnt(J1,J2), [token('Jahren',_)], {incCounter(idExpression,Nexp),
recorda(exp_temp,(Nexp,J1)), recorda(exp_temp3,(Nexp,J2))}.


jahre(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                            (date:(type:ordinary..grain:jahrzehnt..value:(year:J1..year:J2)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2)))))
--> ([token('die',_)] ; [token('Die',_)]), jahrzehnt(J1,J2), [token('Jahre',_)], {incCounter(idExpression,Nexp),
recorda(exp_temp,(Nexp,J1)), recorda(exp_temp3,(Nexp,J2))}.


%%%%%% 2.6 Relative Jahrhunderte %%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


siecles(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (operator:
                        (anfang:
                            (date:(type:ordinary..grain:jahrhundert..value:(century:C))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2))))))
--> relatif(debut), [token('des',_)], siecle(C, J1, J2), [token('\.'), token('Jahrhunderts',_)],
{incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,J1)), recorda(exp_temp3,(Nexp,J2))}.


siecles(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (operator:
                        (anfang:
                            (date:(type:ordinary..grain:jahrhundert..value:(century:C))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2))))))
--> relatif(debut), [token('des',_)], siecle(C, J1, J2), [token('\.'), token('Jhds',_)],
{incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,J1)), recorda(exp_temp3,(Nexp,J2))}.


siecles(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (operator:
                        (anfang:
                            (date:(type:ordinary..grain:jahrhundert..value:(century:C))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2))))))
--> relatif(debut), [token('des',_)], siecle(C, J1, J2), [token('\.'), token('Jhs',_)],
{incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,J1)), recorda(exp_temp3,(Nexp,J2))}.



siecles(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (operator:
                        (anfang:
                            (date:(type:ordinary..grain:jahrhundert..value:(century:C))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2))))))
--> relatif(debut), [token('des',_)], siecle(C, J1, J2), [token('\.'), token('Jhds.',_)],
{incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,J1)), recorda(exp_temp3,(Nexp,J2))}.


siecles(temporal:
                (type:complete..number:Nexp..relatif:
                    (asem:
                        (operator:
                        (anfang:
                            (date:(type:ordinary..grain:jahrhundert..value:(century:C))))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2))))))
--> relatif(debut), [token('des',_)], siecle(C, J1, J2), [token('\.'), token('Jhs.',_)],
{incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,J1)), recorda(exp_temp3,(Nexp,J2))}.




%%%%% 3. PR�ZISE DATUMSANGABEN %%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%%% 3.1 Tag und Tages�berg�nge %%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(day:T..month:M..year:J)))
                    ..intpr:(begin:(day:T..month:M..year:J)..end:(day:T..month:M..year:J)))))
--> # timeofday(daytime), tag(T), [token('\.',_)], monat(M), jahr(J), {incCounter(idExpression,Nexp),
recorda(exp_temp1,(Nexp,T)), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}, !.


precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(day1:T1..month:M..year:J))
                        ..date:(type:ordinary..grain:day..value:(day2:T2..month:M..year:J)))
                    ..intpr:(begin:(day:T1..month:M..year:J)..end:(day:T2..month:M..year:J)))))
--> tag(T1), [token('\.',_), token('und',_)], # [token('am',_)],
tag(T2), [token('\.',_)], monat(M), jahr(J), {incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,T1)),
recorda(exp_temp1,(Nexp,T2)), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}, !.

precis(temporal:
                (type:incomplete..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(day1:T1..month:M..year:J))
                        ..date:(type:ordinary..grain:day..value:(day2:T2..month:M..year:J)))
                    ..intpr:(begin:(day:T1..month:M..year:J)..end:(day:T2..month:M..year:J)))))
--> tag(T1), [token('\.',_), token('und',_)], [token('am',_)],
tag(T2), [token('\.',_)], monat(M), {resoud(year,Nexp), recorded(exp_temp3,(Nexp,J))}, !.

precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(day1:T1..month:M..year:J))
                        ..date:(type:ordinary..grain:day..value:(day2:T2..month:M..year:J)))
                    ..intpr:(begin:(day:T1..month:M..year:J)..end:(day:T2..month:M..year:J)))))
--> tag(T1), [token('\.',_), token('\/',_)], tag(T2), [token('\.',_)],  monat(M), jahr(J),
{incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,T1)), recorda(exp_temp1,(Nexp,T2)),
recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.

precis(temporal:
                (type:incomplete..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(day1:T1..month:M..year:J))
                        ..date:(type:ordinary..grain:day..value:(day2:T2..month:M..year:J)))
                    ..intpr:(begin:(day:T1..month:M..year:J)..end:(day:T2..month:M..year:J)))))
--> tag(T1), [token('\.',_), token('\/',_)], tag(T2), [token('\.',_)],  monat(M), {resoud(year,Nexp),
recorded(exp_temp3,(Nexp,J))}.

precis(temporal:
                (type:incomplete..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(day:T..month:M)))
                    ..intpr:(begin:(day:T..month:M..year:J)..end:(day:T..month:M..year:J)))))
--> # timeofday(daytime), tag(T), [token('\.',_)], monat(M), {resoud(year,Nexp), recorded(exp_temp3,(Nexp,J)), recorda(exp_temp1,(Nexp,T)),
recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}, !.

precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:nacht..value:(day1:N1..month:M..year:J))
                        ..date:(type:ordinary..grain:nacht..value:(day2:N2..month:M..year:J)))
                    ..intpr:(begin:(day:N1..month:M..year:J)..end:(day:N2..month:M..year:J)))))
--> [token('der',_), token('Nacht',_), token('vom',_)], tag(N1), [token('\.',_), token('auf',_),
token('den',_)], tag(N2), [token('\.',_)], monat(M),  jahr(J), {incCounter(idExpression,Nexp),
recorda(exp_temp,(Nexp,N1)), recorda(exp_temp1,(Nexp,N2)), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}, !.

precis(temporal:
                (type:incomplete..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(nacht1:N1..month:M))
                        ..date:(type:ordinary..grain:nacht..value:(nacht2:N2..month:M)))
                    ..intpr:(begin:(day:N1..month:M..year:J)..end:(day:N2..month:M..year:J)))))
--> [token('der',_), token('Nacht',_), token('vom',_)], tag(N1), [token('\.',_), token('auf',_),
token('den',_)], tag(N2), [token('\.',_)], monat(M),{resoud(year,Nexp),
recorded(exp_temp3,(Nexp,J))}, !.

precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(nacht:T..month:M..year:J)))
                    ..intpr:(begin:(day:T..month:M..year:J)..end:(day:T..month:M..year:J)))))
--> [token('der',_), token('Nacht',_), token('zum',_)], tag(T), [token('\.',_)], monat(M),  jahr(J),
{incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,T)), recorda(exp_temp2,(Nexp,M)),
recorda(exp_temp3,(Nexp,J))}, !.

precis(temporal:
                (type:incomplete..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(nacht:N..month:M)))
                    ..intpr:(begin:(day:N..month:M..year:J)..end:(day:N..month:M..year:J)))))
--> [token('der',_), token('Nacht',_), token('zum',_)], tag(N), [token('\.',_)], monat(M),
{resoud(year,Nexp), recorded(exp_temp3,(Nexp,J))}, !.

precis(temporal:
                (type:complete..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(year:J)))
                    ..intpr:(begin:(day:1..month:12..year:J)..end:(day:31..month:12..year:J)))))
--> [token('den',_), token('letzten',_), token('Wochen',_), token('des',_), token('Jahres',_)], jahr(J),
{incCounter(idExpression,Nexp), recorda(exp_temp3,(Nexp,J))}, !.

precis(temporal:
                (type:complete..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(year:J)))
                    ..intpr:(begin:(day:1..month:M..year:J)..end:(day:14..month:M..year:J)))))
--> [token('den',_), token('ersten',_), token('beiden',_), token('Wochen',_), token('des',_), token('Monats',_)], monat(M),
{resoud(year,Nexp), recorded(exp_temp3,(Nexp,J))}, !.


%%%%% 3.2 Angaben zur Jahreszeit %%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%% Eine Regel speziell f�r den Winter: Februar-Check und neues Jahr %%%%%

%%%%%% Februar-Schaltjahr-Check

precis(temporal:
                (type:incomplete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:season..value:(season:S..monat1:N1..monat2:1..monat3:N3)))
                    ..intpr:(begin:(day:1..month:N1..year:J)..end:(day:NT2..month:N3..year:J1)))))
--> saison(S,N1,1,N3), jahr(J),{leapyear(J), (monthdays1(_,N3,Res);monthdays(_,N3,Res)), NT2 is Res, J1 is J+1, incCounter(idExpression,Nexp),
 recorda(exp3,(Nexp,J))}, !.
 
 %%%%% kein Schaltjahr aber trotzdem Winter
 
 precis(temporal:
                (type:incomplete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:season..value:(season:S..monat1:N1..monat2:1..monat3:N3)))
                    ..intpr:(begin:(day:1..month:N1..year:J)..end:(day:NT2..month:N3..year:J1)))))
--> saison(S,N1,1,N3), jahr(J),{monthdays(_,N3,Res), NT2 is Res, J1 is J+1, incCounter(idExpression,Nexp),
 recorda(exp3,(Nexp,J))}, !.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

precis(temporal:
                (type:relation..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3..year:J))))
                    ..intpr:(begin:(day:1..month:N1..year:J)..end:(day:NT2..month:N3..year:J))))
--> saison(S,N1,N2,N3), jahr(J), {monthdays(_,N3,Res), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,N1)),
recorda(exp_temp2,(Nexp,N3)), recorda(exp_temp3,(Nexp,J))}, !.

%%%%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%

precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:season..value:(season:S..month1:N1..month2:N2..month3:N3..year1:J1..year2:J)))
                    ..intpr:(begin:(day:1..month:N1..year:J1)..end:(day:NT2..month:N3..year:J)))))
--> saison(S,N1,N2,N3), jahr(J1), [token('\/',_)], jahrzweistellig(J3), {decode_annee(J1,J3,NJ),
J is NJ+1, leapyear_dict(J), (monthdays1(_,N3,Res);monthdays(_,N3,Res)), NT2 is Res, incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,N1)), recorda(exp_temp2,(Nexp,N3)),
recorda(exp_temp,(Nexp,J1)), recorda(exp_temp3,(Nexp,J))}, !.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:season..value:(season:S..month1:N1..month2:N2..month3:N3..year1:J1..year2:J)))
                    ..intpr:(begin:(day:1..month:N1..year:J1)..end:(day:NT2..month:N3..year:J)))))
--> saison(S,N1,N2,N3), jahr(J1), [token('\/',_)], jahrzweistellig(J3), {decode_annee(J1,J3,NJ),
monthdays(_,N3,Res), NT2 is Res, J is NJ+1, incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,N1)), recorda(exp_temp2,(Nexp,N3)),
recorda(exp_temp,(Nexp,J1)), recorda(exp_temp3,(Nexp,J))}, !.

%%%%%%%% Falls Winter und Schaltjahr %%%%%%%

precis(temporal:
                (type:incomplete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:season..value:(season:S..monat1:N1..monat:1..monat3:N3)))
                    ..intpr:(begin:(day:1..month:N1..year:J)..end:(day:NT2..month:N3..year:J1)))))
--> saison(S,N1,1,N3), {resoud(year,Nexp), recorded(exp_temp3,(Nexp,J)), J1 is J+1, leapyear_dict(J1),
(monthdays1(_,N3,Res);monthdays(_,N3,Res)), NT2 is Res}, !.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

precis(temporal:
                (type:incomplete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:season..value:(season:S..monat1:N1..monat:1..monat3:N3)))
                    ..intpr:(begin:(day:1..month:N1..year:J)..end:(day:NT2..month:N3..year:J1)))))
--> saison(S,N1,1,N3), {resoud(year,Nexp), recorded(exp_temp3,(Nexp,J)), J1 is J+1,
monthdays(_,N3,Res), NT2 is Res}, !.

precis(temporal:
                (type:incomplete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:season..value:(season:S..monat1:N1..monat2:N2..monat3:N3)))
                    ..intpr:(begin:(day:1..month:N1..year:J)..end:(day:NT2..month:N3..year:J)))))
--> saison(S,N1,N2,N3), {resoud(year,Nexp), recorded(exp_temp3,(Nexp,J)), monthdays(_,N3,Res), NT2 is Res}, !.


%%%% 3.3 Monat und Monats�berg�nge %%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% Februar - Schaltjahr - Check %%%

precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(month:M..year:J)))
                    ..intpr:(begin:(day:1..month:M..year:J)..end:(day:T..month:M..year:J)))))
--> monat(M), jahr(J), {leapyear(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), T is Res, incCounter(idExpression,Nexp),
 recorda(exp_temp1,(Nexp,T)), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}, !.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(month:M..year:J)))
                    ..intpr:(begin:(day:1..month:M..year:J)..end:(day:T..month:M..year:J)))))
--> monat(M), jahr(J), {monthdays(_,M,Res), T is Res, incCounter(idExpression,Nexp), 
recorda(exp_temp1,(Nexp,T)), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}, !.

%%%%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%

precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(monat1:M1..year:J))
                        ..date:(type:ordinary..grain:month..value:(monat2:M2..year:J)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:T..month:M2..year:J)))))
--> monat(M1), [token('\/',_)], monat(M2), jahr(J), {leapyear(J), (monthdays1(_,M2,Res);monthdays(_,M2,Res)),
T is Res, incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,M1)),
recorda(exp_temp2,(Nexp,M2)), recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(monat1:M1..year:J))
                        ..date:(type:ordinary..grain:month..value:(monat2:M2..year:J)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:T..month:M2..year:J)))))
--> monat(M1), [token('\/',_)], monat(M2), jahr(J), {monthdays(_,M2,Res),
T is Res, incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,M1)),
recorda(exp_temp2,(Nexp,M2)), recorda(exp_temp3,(Nexp,J))}.

%%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%%

precis(temporal:
                (type:incomplete..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(monat1:M1))
                        ..date:(type:ordinary..grain:month..value:(monat2:M2)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:T..month:M2..year:J)))))
--> monat(M1), [token('\/',_)], monat(M2), {resoud(year,Nexp), recorded(exp_temp3,(Nexp,J)),
leapyear_dict(J), (monthdays1(_,M2,Res);monthdays(_,M2,Res)), T is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

precis(temporal:
                (type:incomplete..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(monat1:M1))
                        ..date:(type:ordinary..grain:month..value:(monat2:M2)))
                    ..intpr:(begin:(day:1..month:M1..year:J)..end:(day:T..month:M2..year:J)))))
--> monat(M1), [token('\/',_)], monat(M2), {resoud(year,Nexp), recorded(exp_temp3,(Nexp,J)),
monthdays(_,M2,Res), T is Res}.

%%%%%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%%%%

precis(temporal:
                (type:incomplete..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(month:M)))
                    ..intpr:(begin:(day:1..month:M..year:J)..end:(day:T..month:M..year:J)))))
--> monat(M), {resoud(year,Nexp), recorded(exp_temp3,(Nexp,J)), leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)),
T is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

precis(temporal:
                (type:incomplete..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(month:M)))
                    ..intpr:(begin:(day:1..month:M..year:J)..end:(day:T..month:M..year:J)))))
--> monat(M), {resoud(year,Nexp), recorded(exp_temp3,(Nexp,J)), monthdays(_,M,Res),
T is Res}.



%%%%%% 3.4 Jahresangaben und Jahres�berg�nge %%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:year..value:(jahr1:J1))
                        ..date:(type:ordinary..grain:year..value:(jahr2:J2)))
                    ..intpr:(begin:(day:31..month:12..year:J1)..end:(day:1..month:1..year:J2)))))
--> (# [token('dem',_)]), (# [token('zum',_)]), (#
[token('Jahreswechsel',_)]), jahr(J1), [token('\/',_)],
jahrzweistellig(J3), {decode_annee(J1,J3,J2), incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,J1)),
recorda(exp_temp3,(Nexp,J2))}.


precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:year..value:(year:J)))
                    ..intpr:(begin:(day:1..month:1..year:J)..end:(day:31..month:12..year:J)))))
--> jahr(J), { incCounter(idExpression,Nexp), recorda(exp_temp3,(Nexp,J))}.

precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:year..value:(year:J)))
                    ..intpr:(begin:(day:1..month:1..year:J)..end:(day:31..month:12..year:J)))))
--> ([token('Im',_)] ; [token('im',_)]), ([token('Jahr',_)] ; [token('Jahre',_)]), jahr(J),
 {incCounter(idExpression,Nexp), recorda(exp_temp3,(Nexp,J))}.


precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:year..value:(jahr1:J1))
                        ..date:(type:ordinary..grain:year..value:(jahr2:J3)))
                    ..intpr:(begin:(day:31..month:12..year:J1)..end:(day:1..month:1..year:J3)))))
--> [token(T, _)], {split_char(T, 47, J1, J2), number(J1), number(J2), decode_annee(J1, J2, J3), incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,J1)),
recorda(exp_temp3,(Nexp,J3))}.




%%%%%% 3.5 Anganben zu Jahrhunderten %%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:century..value:(century:C)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2)))))
--> siecle(C, J1, J2), [token('\.'), token('Jahrhundert')], {incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,J1)),
recorda(exp_temp3,(Nexp,J2))}.

precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:century..value:(century:C)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2)))))
--> siecle(C, J1, J2), [token('\.'), token('Jhd.')], {incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,J1)),
recorda(exp_temp3,(Nexp,J2))}.

precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:century..value:(century:C)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2)))))
--> siecle(C, J1, J2), [token('\.'), token('Jh.')], {incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,J1)),
recorda(exp_temp3,(Nexp,J2))}.


precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:century..value:(century:C)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2)))))
--> siecle(C, J1, J2), [token('\.'), token('Jhds.')], {incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,J1)),
recorda(exp_temp3,(Nexp,J2))}.


precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:century..value:(century:C)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2)))))
--> siecle(C, J1, J2), [token('\.'), token('Jhs.')], {incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,J1)),
recorda(exp_temp3,(Nexp,J2))}.


precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:century..value:(century:C)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2)))))
--> siecle(C, J1, J2), [token('\.'), token('Jhd')], {incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,J1)),
recorda(exp_temp3,(Nexp,J2))}.

precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:century..value:(century:C)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2)))))
--> siecle(C, J1, J2), [token('\.'), token('Jh')], {incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,J1)),
recorda(exp_temp3,(Nexp,J2))}.


precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:century..value:(century:C)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2)))))
--> siecle(C, J1, J2), [token('\.'), token('Jhds')], {incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,J1)),
recorda(exp_temp3,(Nexp,J2))}.


precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:century..value:(century:C)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2)))))
--> siecle(C, J1, J2), [token('\.'), token('Jhs')], {incCounter(idExpression,Nexp), recorda(exp_temp,(Nexp,J1)),
recorda(exp_temp3,(Nexp,J2))}.


%%%%%%% 3.6 Ereignisse %%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:year..value:(years:Jsz..yearold:J1..det:Det..adv:Adv..adj:Adj)))
                    ..intpr:(begin:(day:T..month:M..year:J)..end:(day:T..month:M..year:J)))))
--> jahreszahl(Jsz), # [token('Jahr',_)], # [token('Jahre',_)], [token('nach',_)], # findDet(Det),
# findAdv(Adv), # findAdj(Adj), recognizeEvent(T,M,J1),{J is (J1+Jsz), incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,T)),
recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.


precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(years:Jsz..yearold:J1..det:Det..adv:Adv..adj:Adj)))
                    ..intpr:(begin:(day:T..month:M..year:J)..end:(day:T..month:M..year:J)))))
--> jahreszahl(Jsz), # [token('Jahr',_)], # [token('Jahre',_)], [token('vor',_)], # findDet(Det),
# findAdv(Adv), # findAdj(Adj), recognizeEvent(T,M,J1),{J is (J1-Jsz), incCounter(idExpression,Nexp), recorda(exp_temp1,(Nexp,T)),
recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.

precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(det:Det..prep:Praep..adv:Adv..adj:Adj)))
                    ..intpr:(begin:(day:T..month:M..year:J)..end:(day:T..month:M..year:J)))))
--> # findPraep(Praep),# findDet(Det), # findAdv(Adv), # findAdj(Adj), recognizeEvent(T,M),
{resoud(year,Nexp), recorded(exp_temp3,(Nexp,J))}.


precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(det:Det..prep:Praep..adv:Adv..adj:Adj)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J2)))))
--> # findPraep(Praep),# findDet(Det), # findAdv(Adv), # findAdj(Adj), recognizeEvent1(J1,J2),{incCounter(idExpression,Nexp), 
recorda(exp_temp,(Nexp,J1)), recorda(exp_temp3,(Nexp,J2))}.


precis(temporal:
                (type:complete..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(det:Det..prep:Praep..adv:Adv..adj:Adj)))
                    ..intpr:(begin:(day:T..month:M..year:J)..end:(day:T..month:M..year:J)))))
--> # findPraep(Praep),# findDet(Det), # findAdv(Adv), # findAdj(Adj), recognizeEvent(T,M,J),{incCounter(idExpression,Nexp), 
recorda(exp_temp1,(Nexp,T)), recorda(exp_temp2,(Nexp,M)), recorda(exp_temp3,(Nexp,J))}.




%%%%%4. TEMPORALE ANAPHERN %%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%%%%% 4.1 Jahresangaben %%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


anaphoric(temporal:
                (type:relation..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:year..value:(year:reference)))
                    ..intpr:(begin:(day:1..month:1..year:J)..end:(day:31..month:12..year:J)))))
--> ([token('gleichen',_)] ; [token('selben',_)]), [token('Jahr',_)], {resoud(year,Nexp), recorded(exp_temp3,(Nexp,J))}.

%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%%

anaphoric(temporal:
                (type:anaphoric..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(month:M..year:reference))
                    ..intpr:(begin:(day:1..month:M..year:J)..end:(day:T..month:M..year:J))))))
--> monat(M), # [token('des',_)], ([token('gleichen',_)] ;[token('selben',_)]), [token('Jahres',_)],
{resoud(year,Nexp), recorded(exp_temp3,(Nexp,J)), leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), T is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

anaphoric(temporal:
                (type:anaphoric..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:month..value:(month:M..year:reference))
                    ..intpr:(begin:(day:1..month:M..year:J)..end:(day:T..month:M..year:J))))))
--> monat(M), # [token('des',_)], ([token('gleichen',_)] ;[token('selben',_)]), [token('Jahres',_)],
{resoud(year,Nexp), recorded(exp_temp3,(Nexp,J)), monthdays(_,M,Res), T is Res}.



anaphoric(temporal:
                (type:anaphoric..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:year..value:(years:Jsz)))
                    ..intpr:(begin:(day:1..month:1..year:J1)..end:(day:31..month:12..year:J1)))))
--> ([token('Nach',_)] ; [token('nach',_)]), jahreszahl(Jsz), [token('Jahren',_)],
{resoud(year,Nexp), recorded(exp_temp3,(Nexp,J)), J1 is (J+Jsz) }.

anaphoric(temporal:
                (type:anaphoric..number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:year..value:(years:Jsz)))
                    ..intpr:(begin:(day:1..month:1..year:J)..end:(day:31..month:12..year:J)))))
--> ([token('Vor',_)] ; [token('vor',_)]), jahreszahl(Jsz), [token('Jahren',_)],
{studyPeriode(_,J2), J is (J2-Jsz), incCounter(idExpression,Nexp), recorda(exp_temp3,(Nexp,J))}.

anaphoric(temporal:
                (type:anaphoric..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:year..value:(years:Jsz..ref_year:J)))
                    ..intpr:(begin:(day:1..month:1..year:NJ)..end:(day:31..month:12..year:NJ)))))
--> jahreszahl(Jsz), ([token('Jahre',_)]; [token('Jahr',_)]), [token('später',_)], {resoud(year,Nexp), recorded(exp_temp3,(Nexp,J)),
NJ is J + Jsz}.

%%%%%%%%%%%%%%% Februar-Schaltjahr-Check %%%%%%%%%%%%%

anaphoric(temporal:
                (type:anaphoric..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:year..value:(years:Jsz..ref_year:J)))
                    ..intpr:(begin:(day:1..month:M..year:NJ)..end:(day:T..month:M..year:NJ)))))
--> jahreszahl(Jsz), ([token('Jahre',_)] ; [token('Jahr',_)]), [token('später',_)], {resoud(month,Nexp), recorded(exp_temp3,(Nexp,J)),
recorded(exp_temp2,(Nexp,M)), NJ is J + Jsz, leapyear_dict(NJ), (monthdays1(_,M,Res);monthdays(_,M,Res)), T is Res}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

anaphoric(temporal:
                (type:anaphoric..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:year..value:(years:Jsz..ref_year:J)))
                    ..intpr:(begin:(day:1..month:M..year:NJ)..end:(day:T..month:M..year:NJ)))))
--> jahreszahl(Jsz), ([token('Jahre',_)] ; [token('Jahr',_)]), [token('später',_)], {resoud(month,Nexp), recorded(exp_temp3,(Nexp,J)),
recorded(exp_temp2,(Nexp,M)), NJ is J + Jsz, monthdays(_,M,Res), T is Res}.


%%%%%% 4.2 Wochenangaben %%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%% Februar-Schaltjahr-Check + addierte Zahl �berschreitet nicht die MOnats oder Jahresgrenze %%%%%%%%%%

anaphoric(temporal:
                    (type:anaphoric..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:(days:W..ref_day:T)))
                        ..intpr:(begin:(day:NT..month:M..year:J)..end:(day:NT..month:M..year:J)))))
--> week(W), [token('später',_)], {resoud(day,Nexp), recorded(exp_temp1,(Nexp,T)),
 recorded(exp_temp2,(Nexp,M)), recorded(exp_temp3,(Nexp,J)), NT is T + W, leapyear_dict(J),
 (monthdays1(_,M,Res);monthdays(_,M,Res)), NT =< Res}.
 
 anaphoric(temporal:
                    (type:anaphoric..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:(days:W..ref_day:T)))
                        ..intpr:(begin:(day:NT..month:M..year:J)..end:(day:NT..month:M..year:J)))))
--> ([token('Nach',_)]; [token('nach',_)]), week(W), {resoud(day,Nexp), recorded(exp_temp1,(Nexp,T)),
 recorded(exp_temp2,(Nexp,M)), recorded(exp_temp3,(Nexp,J)), NT is T + W, leapyear_dict(J),
 (monthdays1(_,M,Res);monthdays(_,M,Res)), NT =< Res}.
 
 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%% kein Schaltjahr + addierte Zahl �berschreitet nicht die MOnats oder Jahresgrenze

anaphoric(temporal:
                    (type:anaphoric..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:(days:W..ref_day:T)))
                        ..intpr:(begin:(day:NT..month:M..year:J)..end:(day:NT..month:M..year:J)))))
--> week(W), [token('später',_)], {resoud(day,Nexp), recorded(exp_temp1,(Nexp,T)),
 recorded(exp_temp2,(Nexp,M)), recorded(exp_temp3,(Nexp,J)), NT is T + W, monthdays(_,M,Res), NT =< Res}.
 
 anaphoric(temporal:
                    (type:anaphoric..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:(days:W..ref_day:T)))
                        ..intpr:(begin:(day:NT..month:M..year:J)..end:(day:NT..month:M..year:J)))))
--> [token('Nach',_)], week(W), {resoud(day,Nexp), recorded(exp_temp1,(Nexp,T)),
 recorded(exp_temp2,(Nexp,M)), recorded(exp_temp3,(Nexp,J)), NT is T + W, monthdays(_,M,Res), NT =< Res}.
 
 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
 
 %%%%% Schaltjahr + addierte Zahl �berschreitet die Monatsgrenze
 
  anaphoric(temporal:
                    (type:anaphoric..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:(days:W..ref_day:T..ref_month:M..calcul_number:NT)))
                        ..intpr:(begin:(day:T1..month:NM..year:J)..end:(day:T1..month:NM..year:J)))))
--> week(W), [token('später',_)], {resoud(day,Nexp), recorded(exp_temp1,(Nexp,T)),
 recorded(exp_temp2,(Nexp,M)), recorded(exp_temp3,(Nexp,J)), NT is T + W, leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)),
NT > Res, NM is M+1, T1 is (NT-Res)}.

  anaphoric(temporal:
                    (type:anaphoric..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:(days:W..ref_day:T..ref_month:M..calcul_number:NT)))
                        ..intpr:(begin:(day:T1..month:NM..year:J)..end:(day:T1..month:NM..year:J)))))
--> ([token('Nach',_)]; [token('nach',_)]), week(W), {resoud(day,Nexp), recorded(exp_temp1,(Nexp,T)),
 recorded(exp_temp2,(Nexp,M)), recorded(exp_temp3,(Nexp,J)), NT is T + W, leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)),
  NT > Res, NM is M+1, T1 is (NT-Res)}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
 
 %%%%% kein Schaltjahr + addierte Zahl �berschreitet die Monatsgrenze
 
 anaphoric(temporal:
                    (type:anaphoric..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:(days:W..ref_day:T..ref_month:M..calcul_number:NT)))
                        ..intpr:(begin:(day:T1..month:NM..year:J)..end:(day:T1..month:NM..year:J)))))
--> week(W), [token('später',_)], {resoud(day,Nexp), recorded(exp_temp1,(Nexp,T)),
 recorded(exp_temp2,(Nexp,M)), recorded(exp_temp3,(Nexp,J)), NT is T + W, monthdays(_,M,Res), NT > Res,
NM is M+1, T1 is (NT-Res)}.

 anaphoric(temporal:
                    (type:anaphoric..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:(days:W..ref_day:T..ref_month:M..calcul_number:NT)))
                        ..intpr:(begin:(day:T1..month:NM..year:J)..end:(day:T1..month:NM..year:J)))))
--> [token('Nach',_)], week(W), {resoud(day,Nexp), recorded(exp_temp1,(Nexp,T)),
 recorded(exp_temp2,(Nexp,M)), recorded(exp_temp3,(Nexp,J)), NT is T + W, monthdays(_,M,Res), NT > Res,
NM is M+1, T1 is (NT-Res)}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

 %%%%% Schaltjahr + addierte Zahl �berschreitet die Jahresgrenze
 
  anaphoric(temporal:
                    (type:anaphoric..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:(days:W..ref_day:T..ref_month:M..ref_year:J..calcul_number:NT)))
                        ..intpr:(begin:(day:T1..month:NM..year:NJ)..end:(day:T1..month:NM..year:NJ)))))
--> week(W), [token('später',_)], {resoud(day,Nexp), recorded(exp_temp1,(Nexp,T)),
 recorded(exp_temp2,(Nexp,12)), recorded(exp_temp3,(Nexp,J)), NT is T + W, leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)),
  NT > Res, NM is 1, T1 is (NT-Res), NJ is J+1}.

  anaphoric(temporal:
                    (type:anaphoric..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:(days:W..ref_day:T..ref_month:M..ref_year:J..calcul_number:NT)))
                        ..intpr:(begin:(day:T1..month:NM..year:NJ)..end:(day:T1..month:NM..year:NJ)))))
--> ([token('Nach',_)]; [token('nach',_)]), week(W), {resoud(day,Nexp), recorded(exp_temp1,(Nexp,T)),
 recorded(exp_temp2,(Nexp,12)), recorded(exp_temp3,(Nexp,J)), NT is T + W, leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT > Res,
NM is 1, T1 is (NT-Res), NJ is J+1}.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

 %%%%% Kein Schaltjahr + addierte Zahl �berschreitet die Jahresgrenze
 
  anaphoric(temporal:
                    (type:anaphoric..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:(days:W..ref_day:T..ref_month:M..ref_year:J..calcul_number:NT)))
                        ..intpr:(begin:(day:T1..month:NM..year:NJ)..end:(day:T1..month:NM..year:NJ)))))
--> week(W), [token('später',_)], {resoud(day,Nexp), recorded(exp_temp1,(Nexp,T)),
 recorded(exp_temp2,(Nexp,12)), recorded(exp_temp3,(Nexp,J)), NT is T + W, monthdays(_,M,Res), NT > Res,
NM is 1, T1 is (NT-Res), NJ is J+1}.

  anaphoric(temporal:
                    (type:anaphoric..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:(days:W..ref_day:T..ref_month:M..ref_year:J..calcul_number:NT)))
                        ..intpr:(begin:(day:T1..month:NM..year:NJ)..end:(day:T1..month:NM..year:NJ)))))
--> ([token('Nach',_)]; [token('nach',_)]), week(W), {resoud(day,Nexp), recorded(exp_temp1,(Nexp,T)),
 recorded(exp_temp2,(Nexp,12)), recorded(exp_temp3,(Nexp,J)), NT is T + W, monthdays(_,M,Res), NT > Res,
NM is 1, T1 is (NT-Res), NJ is J+1}.
 
%%%%%% 4.3 Tagesangaben %%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%%% Schaltjahr + Monats oder Jahresgrenze wird nicht �berschritten

anaphoric(temporal:
                (type:anaphoric..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(days:Jsz..ref_day:T)))
                    ..intpr:(begin:(day:NT..month:M..year:J)..end:(day:NT..month:M..year:J)))))
--> jahreszahl(Jsz),([token('Tag',_)];[token('Tage',_)]), [token('später',_)], {resoud(day,Nexp), 
recorded(exp_temp1,(Nexp,T)), recorded(exp_temp2,(Nexp,M)), recorded(exp_temp3,(Nexp,J)), NT is T + Jsz,
leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT =< Res}.

anaphoric(temporal:
                (type:anaphoric..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(ref_day:T)))
                    ..intpr:(begin:(day:NT..month:M..year:J)..end:(day:NT..month:M..year:J)))))
--> art_rel, [token('Tage',_)], [token('später',_)], {resoud(day,Nexp), 
recorded(exp_temp1,(Nexp,T)), recorded(exp_temp2,(Nexp,M)), recorded(exp_temp3,(Nexp,J)), NT is T + 5,
leapyear_dict(J), (monthdays(_,M,Res);monthdays(_,M,Res)), NT =< Res}.


%%%% kein Schaltjahr + addierte Zahl �berschreitet nicht die MOnats oder Jahresgrenze

anaphoric(temporal:
                    (type:anaphoric..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:(day:T..reference:minus1)))
                        ..intpr:(begin:(day:NT..month:M..year:J)..end:(day:NT..month:M..year:J)))))
--> [token('Vortag',_)], {resoud(day,Nexp), recorded(exp_temp1,(Nexp,T)), recorded(exp_temp2,(Nexp,M)),
recorded(exp_temp3,(Nexp,J)), NT is T-1, NT \= 0}.

anaphoric(temporal:
                (type:anaphoric..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(days:Jsz..ref_day:T)))
                    ..intpr:(begin:(day:NT..month:M..year:J)..end:(day:NT..month:M..year:J)))))
--> jahreszahl(Jsz),([token('Tag',_)];[token('Tage',_)]), [token('später',_)], {resoud(day,Nexp), 
recorded(exp_temp1,(Nexp,T)), recorded(exp_temp2,(Nexp,M)), recorded(exp_temp3,(Nexp,J)), NT is T + Jsz,
monthdays(_,M,Res), NT =< Res}.

anaphoric(temporal:
                (type:anaphoric..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(ref_day:T)))
                    ..intpr:(begin:(day:NT..month:M..year:J)..end:(day:NT..month:M..year:J)))))
--> art_rel, [token('Tage',_)], [token('später',_)], {resoud(day,Nexp), 
recorded(exp_temp1,(Nexp,T)), recorded(exp_temp2,(Nexp,M)), recorded(exp_temp3,(Nexp,J)), NT is T + 5,
monthdays(_,M,Res), NT =< Res}.

 %%%%% Schaltjahr + addierte Zahl �berschreitet die Monatsgrenze

anaphoric(temporal:
                    (type:anaphoric..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:(day:T..ref_month:M..reference:minus1)))
                        ..intpr:(begin:(day:NT..month:NM..year:J)..end:(day:NT..month:NM..year:J)))))
--> [token('Vortag',_)], {resoud(day,Nexp), recorded(exp_temp1,(Nexp,T)), recorded(exp_temp2,(Nexp,M)),
recorded(exp_temp3,(Nexp,J)), leapyear_dict(J), NM is M-1, (monthdays1(_,NM,Res);monthdays(_,NM,Res)), NT is Res}.

anaphoric(temporal:
                (type:anaphoric..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(days:Jsz..ref_day:T..ref_month:M..calcul_number:NT)))
                    ..intpr:(begin:(day:T1..month:NM..year:J)..end:(day:T1..month:NM..year:J)))))
--> jahreszahl(Jsz),([token('Tag',_)];[token('Tage',_)]), [token('später',_)], {resoud(day,Nexp), 
recorded(exp_temp1,(Nexp,T)), recorded(exp_temp2,(Nexp,M)), recorded(exp_temp3,(Nexp,J)), NT is T + Jsz,
leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT > Res, T1 is NT-Res, NM is M+1}.

anaphoric(temporal:
                (type:anaphoric..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(ref_day:T..ref_month:M..calcul_number:NT)))
                    ..intpr:(begin:(day:T1..month:NM..year:J)..end:(day:T1..month:NM..year:J)))))
--> art_rel, [token('Tage',_)], [token('später',_)], {resoud(day,Nexp), 
recorded(exp_temp1,(Nexp,T)), recorded(exp_temp2,(Nexp,M)), recorded(exp_temp3,(Nexp,J)), NT is T + 5,
leapyear_dict(J), (monthdays1(_,M,Res);monthdays(_,M,Res)), NT > Res, T1 is NT-Res, NM is M+1}.


 %%%%% kein Schaltjahr + addierte Zahl �berschreitet die Monatsgrenze
 
 anaphoric(temporal:
                    (type:anaphoric..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:(day:T..ref_month:M..reference:minus1)))
                        ..intpr:(begin:(day:NT..month:NM..year:J)..end:(day:NT..month:NM..year:J)))))
--> [token('Vortag',_)], {resoud(day,Nexp), recorded(exp_temp1,(Nexp,T)), recorded(exp_temp2,(Nexp,M)),
recorded(exp_temp3,(Nexp,J)), NM is M-1, monthdays(_,NM,Res), NT is Res}.

anaphoric(temporal:
                (type:anaphoric..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(days:Jsz..ref_day:T..ref_month:M..calcul_number:NT)))
                    ..intpr:(begin:(day:T1..month:NM..year:J)..end:(day:T1..month:NM..year:J)))))
--> jahreszahl(Jsz), ([token('Tag',_)];[token('Tage',_)]), [token('später',_)], {resoud(day,Nexp), 
recorded(exp_temp1,(Nexp,T)), recorded(exp_temp2,(Nexp,M)), recorded(exp_temp3,(Nexp,J)), NT is T + Jsz,
monthdays(_,M,Res), NT > Res, T1 is NT-Res, NM is M+1}.

anaphoric(temporal:
                (type:anaphoric..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(ref_day:T..ref_month:M..calcul_number:NT)))
                    ..intpr:(begin:(day:T1..month:NM..year:J)..end:(day:T1..month:NM..year:J)))))
--> art_rel, [token('Tage',_)], [token('später',_)], {resoud(day,Nexp), 
recorded(exp_temp1,(Nexp,T)), recorded(exp_temp2,(Nexp,M)), recorded(exp_temp3,(Nexp,J)), NT is T + 5,
monthdays(_,M,Res), NT > Res, T1 is NT-Res, NM is M+1}.

%%%%%% Jahresgrenze wird �berschritten

 anaphoric(temporal:
                    (type:anaphoric..ref_number:Nexp..exactdate:
                        (asem:
                                (date:(type:ordinary..grain:day..value:(reference:minus1)))
                        ..intpr:(begin:(day:31..month:12..year:NJ)..end:(day:31..month:12..year:NJ)))))
--> [token('Vortag',_)], {resoud(day,Nexp), recorded(exp_temp1,(Nexp,1)), recorded(exp_temp2,(Nexp,1)),
recorded(exp_temp3,(Nexp,J)), NJ is J-1}.

anaphoric(temporal:
                (type:anaphoric..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(days:Jsz..ref_day:T..ref_month:M..ref_year:J..calcul_number:NT)))
                    ..intpr:(begin:(day:T1..month:1..year:NJ)..end:(day:T1..month:1..year:NJ)))))
--> jahreszahl(Jsz),([token('Tag',_)];[token('Tage',_)]), [token('später',_)], {resoud(day,Nexp), 
recorded(exp_temp1,(Nexp,T)), recorded(exp_temp2,(Nexp,12)), recorded(exp_temp3,(Nexp,J)), NT is T + Jsz,
monthdays(_,M,Res), NT > Res, T1 is NT-Res, NJ is J+1}.

anaphoric(temporal:
                (type:anaphoric..ref_number:Nexp..exactdate:
                    (asem:
                            (date:(type:ordinary..grain:day..value:(ref_day:T..ref_month:M..ref_year:J..calcul_number:NT)))
                    ..intpr:(begin:(day:T1..month:1..year:NJ)..end:(day:T1..month:1..year:NJ)))))
--> art_rel, [token('Tage',_)], [token('später',_)], {resoud(day,Nexp), 
recorded(exp_temp1,(Nexp,T)), recorded(exp_temp2,(Nexp,M)), recorded(exp_temp3,(Nexp,J)), NT is T + 5,
monthdays(_,M,Res), NT > Res, T1 is NT-Res, NJ is J+1}.


%% Regeln zur Berechnung des geeigneten Antezedenten
% einfache Version:
% - finde den letzten analysierten Ausdruck
% - ohne zus�tzlichen Test auf Kompatibilit�t(grain)
        
%resoud(_,Nexp) :-
%    getCounter(idExpression, Nexp0),
%    Nexp is Nexp0 - 1.

%%	Erweiterte Regel
%
resoud(day, Nexp) :-
	recorded(exp_temp1,(Nexp,_)).
resoud(month, Nexp) :-
	recorded(exp_temp2,(Nexp,_)).
resoud(year, Nexp) :-
	recorded(exp_temp3,(Nexp,_)).




%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



tag(N) --> [token(X,_)], { atom_to_int(X, N), N>0, N=<31 }.

tag(N) --> [token(Xdot,_)], { append(X,[46], Xdot), atom_to_int(X, N), N>0, N=<31 }.	% optional dot after number

siecle(N, J1, J2) --> [token(X,_)], {atom_to_int(X, N), N>0, N=<21, nameCentury(N, J1, J2)}.

saison(X, N1, N2, N3) --> [token(X,_)], { nomSaison(X, N1, N2, N3) }.

saison(X, N1) --> [token(X,_)], { nomSaison(X, N1) }.

monat(N) --> [token(X,_)], { nomMois(X, N) }.

jahr(N) --> [token(X,_)], { atom_to_int(X, N), N>=1000, N=<2100 }.

jahrzweistellig(N) --> [token(X,_)], { atom_to_int(X, N), N>=10, N=<99 }.

anneeSouple(N) --> [token(X,_)], { atom_to_int(X, N), N>0, atom_length(X, L), member(L, [2, 4]) }.

jahreszahl(N) --> [token(X,_)], { zahl(X, N) }.

jahrzehnt(N1, N2) --> [token(X,_)], { nameJz(X, N1, N2) }.

week(N) --> [token(X,_)], [token(Y,_)], {nameWeek(X, Y, N)}.

%%%%%%%%%%%%%%% Regeln f�r die Erkennung von tempor�ren Ereignissen %%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

recognizeEvent(T,M) --> [token(X,_)], {nameEvent(X,T,M)}.

recognizeEvent1(J1,J2) --> [token(X,_)], [token(Y,_)], {nameEvent(X,Y,J1,J2)}.

recognizeEvent(T,M,J) --> [token(X,_)], {nameEvent(X,T,M,J)}.

%%%%%%%%%%%%%%% Regeln zu Dekodierung von Ausdr�cken mit Bindestrich %%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

strich_jahre(N1,N2) -->
    numerique(Texte),
    {texte2dates(Texte,N1,N2)}.


texte2dates(Texte,N1,N2) :-
    split_strich(Texte,N1,M2),
    number(N1), number(M2),
    decode_annee(N1,M2,N2).

split_strich(Texte,N1,N2) :-
    name(Texte,AsciiList),
    append(L1,[45|L2],AsciiList),
    name(N1,L1), name(N2,L2).

decode_annee(_,MM,MM) :-
    MM > 1000, !.
decode_annee(N1,MM,N2) :-
    MM < 100,
    N2 is N1- (N1 mod 100) + MM.

%%%%%%%%% Schaltjahr-Regeln %%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    
leapyear(J) :- 0 =:= J mod 400.
leapyear(J) :- 0 =:= J mod 4, \+ 0 =:= J mod 100.

split_char([C|T2], C, [], T2) :- !.
split_char([], _, [], []).
split_char([T|TR1], C, [T|TR2], T2) :- !, split_char(TR1, C, TR2, T2).


