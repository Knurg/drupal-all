%%%%%%%%%%%%%%%%%%%  xml2.pro  %%%%%%%%%%%%%%%%%%%
%%
%% TRANSFORMATION EN XML : MODE "RICH TOKENS"

%% Datei übernommen aus LinguaStream. Einige Änderungen durch Martin Scholz


:- consult('gulp.pro').
:- consult('xml.pro').

% Martin Scholz: Ausgabe als XML, terminiert durch ein 0-byte als end marker (so macht es auch das originale writeXml3)
% Anders als bei writeXml3 werden nicht die Token nochmals mit ausgegeben, sondern lediglich
% referenziert (mittels Feature 'position').
% Alle Annotationen werden in einem <annotations> tag zusammengefasst. Jede Annotation ist ein <anno> tag mit folgendem Inahlt:
% - Attribut "tokens": eine Auflistung der Positionen der Token, die die Annotation bilden, durch Blanks getrennt.
% - Die Merkmalsstruktur der Annotation, codiert durch writeSemValue. Dh. in einem umschließenden <value> tag sind die Name-Wert-Paare
%   codiert, der Name jeweils als tag mit dem Wert als Inhalt
%
semAsXml(Sem) :-
	write('<annotations>'),
	semAsXml_aux(Sem),
	write('</annotations>')
	, put(0).

semAsXml_aux([]).

semAsXml_aux([synt(_, [])|R]) :-
	semAsXml_aux(R).
	
semAsXml_aux([synt(Tokens, Sem)|R]) :- 
	write('<anno'),
	write(' tokens="'), writeTokenRefs(Tokens), write('"'),
	write('>'),
	writeSemValue(Sem, [], '', []),
	write('</anno>'),
	semAsXml_aux(R).



writeTokenRefs([token(_, F)]) :-
	g_unify(F, token_position:I),
	write(I).

writeTokenRefs([token(_, F)|R]) :-
	g_unify(F, token_position:I),
	write(I), write(' '),
	writeTokenRefs(R).
	
writeTokenRefs(token(_, F)) :-
	g_unify(F, token_position:I),
	write(I).
	


ignoredTokenFeatures([xmlctx]).
ignoredSemanticsFeatures([]).



writeXml3(Type, Id, Namespace, Tokens, Blanks, Marks) :- 
	writeXmlText(Type, Id, Tokens, Blanks, Marks), 
	put(0), 
	writeXmlSem(Type, Id, Namespace, Tokens, Blanks).

writeXmlText(_, Id, [synt(Syntagme, Type, Sem)|Reste], Blanks, Marks) :-
    !, writeXmlText(Type, Id, [synt(Syntagme, Sem)|Reste], Blanks, Marks).

writeXmlText(Type, Id, [synt(Syntagme, [])|Reste], Blanks, Marks) :-
	writeSyntagme(Syntagme, Blanks, ResteBlanks, Marks, ResteMarks),
	writeXmlText(Type, Id, Reste, ResteBlanks, ResteMarks).

writeXmlText(Type, Id, [synt(Syntagme, Sem)|Reste], Blanks, Marks) :-
	Sem \== [],
	write('<analysis anchor="begin" type="'), write(Type), write('" id="'), write(Id), write('"/>'),
	writeSyntagme(Syntagme, Blanks, ResteBlanks, Marks, ResteMarks),
	write('<analysis anchor="end" type="'), write(Type), write('" id="'), write(Id), write('"/>'),
	plus(Id, 1, NewId), writeXmlText(Type, NewId, Reste, ResteBlanks, ResteMarks).

writeXmlText(_, _, [], _, _).


writeXmlSem(_, Id, Namespace, [synt(Syntagme, Type, Sem)|Reste], Blanks) :- 
	!, writeXmlSem(Type, Id, Namespace, [synt(Syntagme, Sem)|Reste], Blanks).
	
writeXmlSem(Type, Id, Namespace, [synt(Syntagme, [])|Reste], Blanks) :- 
	consommeBlanks(Syntagme, Blanks, ResteBlanks), 
	writeXmlSem(Type, Id, Namespace, Reste, ResteBlanks).

writeXmlSem(Type, Id, Namespace, [synt(Syntagme, Sem)|Reste], Blanks) :-
	sem \== [],
	write('<sem type="'), write(Type), write('" id="'), write(Id), write('">'),
	ignoredSemanticsFeatures(IF), writeSemValue(Sem, IF, Type, Namespace),	
	write('<text>'),  writeSyntagmeSem(Syntagme, Blanks, ResteBlanks), write('</text>'),
	write('</sem>\n'), plus(Id, 1, NewId),
	writeXmlSem(Type, NewId, Namespace, Reste, ResteBlanks).
	
writeXmlSem(_, Id, _, [], _):- put(0), write(Id), put(0).


consommeBlanks([_|Reste], [_|SuiteBlanks], ResteBlanks) :- consommeBlanks(Reste, SuiteBlanks, ResteBlanks).
consommeBlanks([], Blanks, Blanks).

writeSyntagme([Token|Reste], [Blanks|SuiteBlanks], ResteBlanks, [Mark|SuiteMarks], ResteMarks) :- 
        write(Mark),
        writeToken(Token, Blanks),
        writeSyntagme(Reste, SuiteBlanks, ResteBlanks, SuiteMarks, ResteMarks).
writeSyntagme([], Blanks, Blanks, Marks, Marks).


writeSyntagmeSem([Token|Reste], [Blanks|SuiteBlanks], ResteBlanks) :-         
        writeToken(Token, Blanks),
        writeSyntagmeSem(Reste, SuiteBlanks, ResteBlanks).
writeSyntagmeSem([], Blanks, Blanks).


writeToken(token(Mot,FS), Blanks) :-
        write('<token text="'), encodeAttribute(Mot, EMot), write(EMot), 
        write('" blanks="'), encodeAttribute(Blanks, EBlanks), write(EBlanks), write('">'), 
        ignoredTokenFeatures(IF), gulpToXml2(FS, IF, []), write('</token>').
    
	
writeSemValue(Sem, IF, _, []) :- 
	!, 
	write('<value>'), 
	gulpToXml2(Sem, IF, []), 
	write('</value>').
	
	
writeSemValue(Sem, IF, Type, Namespace) :-
	write('<value xmlns:'), write(Type), write('="'), write(Namespace), write('">'), 
	gulpToXml2(Sem, IF, Type),	 
	write('</value>').
                                       
