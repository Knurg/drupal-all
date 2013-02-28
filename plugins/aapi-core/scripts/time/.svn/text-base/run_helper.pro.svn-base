% This file contains the rules to analyse a sentence.
% The rules are taken from or inspired by the
% file linguastream.pro of the LinguaStream Platform.
%
:- consult('xml2.pro').
:- consult('gulp.pro').
:- consult('util.pro').

analyseTokens(Predicat, Tokens) :- 
	chercheDCG(Predicat, Tokens, Sem), 
	semAsXml(Sem),
	nl.

chercheDCG(Racine, Phrase, [synt(Syntagme, Sem)|SuiteSem]) :- 
    extractionDCG(Racine, Phrase, RestePhrase, Sem, Syntagme), 
    chercheDCG(Racine, RestePhrase, SuiteSem).
chercheDCG(_, [], []).


extractionDCG(Racine, Phrase, RestePhrase, Sem, Syntagme) :- 
    racineDCG(Racine, Sem, Phrase, RestePhrase), !,
    append(Syntagme, RestePhrase, Phrase).

racineDCG(Racine, Sem, Phrase, RestePhrase) :- call(Racine, Sem, Phrase, RestePhrase), !.
racineDCG(_, [], [_|R], R).

