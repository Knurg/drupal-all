% Outils xml communs


encodeAttribute(Str, Res) :- atom_chars(Str, X), encodeChars(X, Y, 'Attribute'), atom_chars(Res, Y).
encodeText(Str, Res) :- atom_chars(Str, X), encodeChars(X, Y, 'Text'), atom_chars(Res, Y).

encodeChars([C|Reste], Result, Mode) :- 
        encodeChar(C, ResultC, Mode),
        encodeChars(Reste, ResultR, Mode),
        append(ResultC, ResultR, Result).
encodeChars([], [], _).


encodeChar('&', ['&', 'a', 'm', 'p', ';'], _) :- !.
encodeChar('<', ['&', 'l', 't', ';'], _) :- !.
encodeChar('>', ['&', 'g', 't', ';'], _) :- !.
encodeChar('>', ['&', 'g', 't', ';'], _) :- !.
encodeChar('"', ['&', 'q', 'u', 'o', 't', ';'], 'Attribute') :- !.
encodeChar('\n', ['&', '#', '1', '0', ';'], 'Attribute') :- !.
encodeChar(C, [C], _).                                       