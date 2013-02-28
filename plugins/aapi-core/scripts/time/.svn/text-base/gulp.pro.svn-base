% GULP

:- consult('xml.pro').

:- op(601, xfy, '..').
:- op(600, xfy, ':').
:- op(600, xf, ':/').

:- dynamic(g_schema/3).


gulpToXml(Gulp) :- gulpToXmlAux(Gulp, [], []).
gulpToXml2(Gulp, IgnoredFeatures, Namespace) :- gulpToXmlAux(Gulp, IgnoredFeatures, Namespace).

gulpToXmlAux(X, _, _) :- var(X), !, write('?Var?').
gulpToXmlAux(X, _, _) :- atomic(X), !, encodeText(X, Y), write(Y).

gulpToXmlAux(X..Y, IF, Namespace) :- !, gulpToXmlAux(X, IF, Namespace), gulpToXmlAux(Y, IF, Namespace).
gulpToXmlAux(X:_, IF, _) :- member(X, IF), !.
gulpToXmlAux(_:Y, _, _) :- var(Y), !.
gulpToXmlAux(X:Y, IF, Namespace) :- !, write('<'), gulpTag(X, Namespace), write('>'), gulpToXmlAux(Y, IF, Namespace), write('</'), gulpTag(X, Namespace), write('>').	
gulpToXmlAux(X:/, IF, _) :- member(X, IF), !.
gulpToXmlAux(X:/, _, Namespace) :- !, write('<'), gulpTag(X, Namespace), write('/>').

gulpToXmlAux([X|R], IF, Namespace) :- !, write(X), write(' '), gulpToXmlAux(R, IF, Namespace).
gulpToXmlAux([], _, _) :- !.

gulpToXmlAux(X, IF, Namespace) :- 
	X = g_(Y), !,
	gulpToXmlAux(Y, IF, Namespace).
	
gulpToXmlAux(X, IF, Namespace) :-
	functor(X, g_, A), A > 1, !,
	bagof(F:V, g_schema(F, V, X), Bag),
	forall(member(Y, Bag), gulpToXmlAux(Y, IF, Namespace)).	
	
%gulpToXmlAux(_, _, _) :- write('?Not a GULP Structure?').
gulpToXmlAux(X, _, _) :- write('?'), write(X), write('?').

gulpTag(X, []) :- write(X), !.
gulpTag(X, Namespace) :- write(Namespace), write(':'), write(X).


g_tf(Term,Term) :-
        (var(Term) ; atom(Term) ; number(Term) ; string(Term)), !.
        

g_tf(Term,_) :-
        g_not_fs(Term),
        functor(Term,X,_),
        (X = ':' ; X = '..'), !,
        throw('Invalid GULP punctuation').
        

g_tf(Term,NewTerm) :-
        g_not_fs(Term), !,
        Term =.. [Functor|Args],
        g_tf_list(Args,NewArgs),
        NewTerm =.. [Functor|NewArgs].

g_tf(Feature:Value,ValueList) :-
        !, g_tf(Value,NewValue),
        g_tfsf(Feature,g_(NewValue),ValueList).


g_tf(FeatureStructure .. Rest,ValueList) :-
        !, g_tf(FeatureStructure,VL1),
        g_tf(Rest,VL2), g_unify_internal(VL1,VL2,ValueList).

g_tf_list([],[]).

g_tf_list([H|T],[NewH|NewT]) :-
        g_tf(H,NewH),
        g_tf_list(T,NewT).

g_tfsf(Keyword,Value,ValueList) :-
        g_schema(Keyword,Value,ValueList).

g_unify_internal(X,X,X) :- !.
g_unify_internal(X,Y,_) :- (X = Y).


g_fs(X:_) :- atom(X).
g_fs(X..Y) :- g_fs(X), g_fs(Y).

g_not_fs(X) :- g_fs(X), !, fail.
g_not_fs(_).


g_unify(FS1, FS2) :-
        g_tf(FS1, TFS1), 
        g_tf(FS2, TFS2),
        TFS1 = TFS2.


		
g_features(List) :-
    abolish(g_schema/3),
    length(List, Len),
    g_features_aux(List, 1, Len).
    	

g_features_aux([F|Reste], N, Len) :-
    functor(Schema, 'g_', Len),
    arg(N, Schema, Value),
    assert(g_schema(F, Value, Schema)),
    NewN is N+1, 
    g_features_aux(Reste, NewN, Len).

g_features_aux([],_,_).






