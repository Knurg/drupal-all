%%%%%%%%%%%%%%%%%%%  Lexikon  %%%%%%%%%%%%%%%%%%%

art_rel --> [token('Wenige',_)].
art_rel --> [token('wenige',_)].
art_rel --> [token('Einige',_)].
art_rel --> [token('einige',_)].
art_rel --> [token('Ein',_), token('paar',_)].
art_rel --> [token('ein',_), token('paar',_)].

leapyear_dict(2004).
leapyear_dict(2000).
leapyear_dict(1996).
leapyear_dict(1992).
leapyear_dict(1988).
leapyear_dict(1984).
leapyear_dict(1980).
leapyear_dict(1976).
leapyear_dict(1972).
leapyear_dict(1968).
leapyear_dict(1964).
leapyear_dict(1960).
leapyear_dict(1956).
leapyear_dict(1952).
leapyear_dict(1948).
leapyear_dict(1944).
leapyear_dict(1940).
leapyear_dict(1936).
leapyear_dict(1932).
leapyear_dict(1928).
leapyear_dict(1924).
leapyear_dict(1920).
leapyear_dict(1916).
leapyear_dict(1912).
leapyear_dict(1908).
leapyear_dict(1904).


monthdays('Januar', 1, 31).
monthdays('Februar', 2, 28).
monthdays('März', 3, 31).
monthdays('April', 4, 30).
monthdays('Mai', 5, 31).
monthdays('Juni', 6, 30).
monthdays('Juli', 7, 31).
monthdays('August', 8, 31).
monthdays('September', 9, 30).
monthdays('Oktober', 10, 31).
monthdays('November', 11, 30).
monthdays('Dezember', 12, 31).
monthdays1('Februar', 2, 29).

nameJz('zwanziger', 1920, 1929).
nameJz('20er', 1920, 1929).
nameJz('dreißiger', 1930, 1939).
nameJz('30er', 1930, 1939).
nameJz('vierziger', 1940, 1949).
nameJz('40er', 1940, 1949).
nameJz('fünfziger', 1950, 1959).
nameJz('50er', 1950, 1959).
nameJz('sechziger', 1960, 1969).
nameJz('60er', 1960, 1969).
nameJz('siebziger', 1970, 1979).
nameJz('70er', 1970, 1979).
nameJz('achtziger', 1980, 1989).
nameJz('80er', 1980, 1989).
nameJz('neunziger', 1990, 1999).
nameJz('90er', 1990, 1999).

nameCentury(11, 1000, 1099).
nameCentury(12, 1100, 1199).
nameCentury(13, 1200, 1299).
nameCentury(14, 1300, 1399).
nameCentury(15, 1400, 1499).
nameCentury(16, 1500, 1599).
nameCentury(17, 1600, 1699).
nameCentury(18, 1700, 1799).
nameCentury(19, 1800, 1899).
nameCentury(20, 1900, 1999).
nameCentury(21, 2000, 2099).


nameWeek('Eine', 'Woche', 7).
nameWeek('Einer', 'Woche', 7).
nameWeek('eine', 'Woche', 7).
nameWeek('einer', 'Woche', 7).
nameWeek('Zwei', 'Wochen', 14).
nameWeek('Drei', 'Wochen', 21).
nameWeek('Vier', 'Wochen', 28).
nameWeek('Fünf', 'Wochen', 35).
nameWeek('Sechs', 'Wochen', 42).
nameWeek('Sieben', 'Wochen', 49).
nameWeek('Acht', 'Wochen', 56).
nameWeek('Neun', 'Wochen', 63).
nameWeek('Zehn', 'Wochen', 70).
nameWeek('zwei', 'Wochen', 14).
nameWeek('drei', 'Wochen', 21).
nameWeek('vier', 'Wochen', 28).
nameWeek('fünf', 'Wochen', 35).
nameWeek('sechs', 'Wochen', 42).
nameWeek('sieben', 'Wochen', 49).
nameWeek('acht', 'Wochen', 56).
nameWeek('neun', 'Wochen', 63).
nameWeek('zehn', 'Wochen', 70).

zahl('ein', 1).
zahl('einen',1).
zahl('eine',1).
zahl('zwei', 2).
zahl('drei', 3).
zahl('vier', 4).
zahl('fünf', 5).
zahl('sechs', 6).
zahl('sieben', 7).
zahl('acht', 8).
zahl('neun', 9).
zahl('zehn', 10).
zahl('Ein', 1).
zahl('Einen',1).
zahl('Eine',1).
zahl('Zwei', 2).
zahl('Drei', 3).
zahl('Vier', 4).
zahl('Fünf', 5).
zahl('Sechs', 6).
zahl('Sieben', 7).
zahl('Acht', 8).
zahl('Neun', 9).
zahl('Zehn', 10).
zahl('zwanzig', 20).
zahl('Zwanzig', 20).
zahl('dreißig', 30).
zahl('Dreißig', 30).
zahl('vierzig', 40).
zahl('Vierzig', 40).
zahl('fünfzig', 50).
zahl('Fünfzig', 50).
zahl('sechzig', 60).
zahl('Sechzig', 60).
zahl('siebzig', 70).
zahl('Siebzig', 70).
zahl('achtzig', 80).
zahl('Achtzig', 80).
zahl('neunzig', 90).
zahl('Neunzig', 90).
zahl('hundert', 100).
zahl('Hundert', 100).
zahl('zweihundert', 200).
zahl('Zweihundert', 200).
zahl('dreihundert', 300).
zahl('Dreihundert', 300).
zahl('Vierhundert', 400).
zahl('vierhundert', 400).
zahl('fünfhundert', 500).
zahl('Fünfhundert', 500).
zahl('sechshundert', 600).
zahl('Sechshundert', 600).
zahl('siebenhundert', 700).
zahl('Siebenhundert', 700).
zahl('achthundert', 800).
zahl('Achthundert', 800).
zahl('neunhundert', 900).
zahl('Neunhundert', 900).
zahl('tausend', 1000).
zahl('Tausend', 1000).

nomMois('Januar', 1).
nomMois('Februar', 2).
nomMois('März', 3).
nomMois('April', 4).
nomMois('Mai', 5).
nomMois('Juni', 6).
nomMois('Juli', 7).
nomMois('August', 8).
nomMois('September', 9).
nomMois('Oktober', 10).
nomMois('November', 11).
nomMois('Dezember', 12).
nomMois('Januars', 1).
nomMois('Februars', 2).
nomMois('Märzes', 3).
nomMois('Aprils', 4).
nomMois('Mais', 5).
nomMois('Junis', 6).
nomMois('Julis', 7).
nomMois('Augusts', 8).
nomMois('Septembers', 9).
nomMois('Oktobers', 10).
nomMois('Novembers', 11).
nomMois('Dezembers', 12).
nomMois('JANUAR', 1).
nomMois('FEBRUAR', 2).
nomMois('MÄRZ', 3).
nomMois('APRIL', 4).
nomMois('MAI', 5).
nomMois('JUNI', 6).
nomMois('JULI', 7).
nomMois('AUGUST', 8).
nomMois('SEPTEMBER', 9).
nomMois('OKTOBER', 10).
nomMois('NOVEMBER', 11).
nomMois('DEZEMBER', 12).

nomSaison('Frühling',3,4,5).
nomSaison('Frühjahr',3,4,5).
nomSaison('Frühlings',3,4,5).
nomSaison('Frühjahrs',3,4,5).
nomSaison('Sommer',6,7,8).
nomSaison('Sommers',6,7,8).
nomSaison('Herbst',9,10,11).
nomSaison('Herbstes',9,10,11).
nomSaison('Winter',12,1,2).
nomSaison('Winters',12,1,2).
nomSaison('Frühsommer',6).
nomSaison('Spätsommer',8).


relatif(fin) --> [token('Ende',_)].
relatif(milieu) --> [token('Mitte',_)].
relatif(debut) --> [token('Anfang',_)].
relatif(debut) --> [token('zu',_), token('Beginn',_)].
relatif(global) --> [token('gegen',_)], relatif(fin).
relatif(global) --> [token('gegen',_)], relatif(milieu).
relatif(global) --> [token('gegen',_)], relatif(debut).
relatif(global) --> [token('Gegen',_)], relatif(fin).
relatif(global) --> [token('Gegen',_)], relatif(milieu).
relatif(global) --> [token('Gegen',_)], relatif(debut).

timeofday(daytime) --> ([token('Am',_)];[token('am',_)]), [token('Morgen',_)], [token('des',_)].
timeofday(daytime) --> ([token('Am',_)];[token('am',_)]), [token('Vormittag',_)], [token('des',_)].
timeofday(daytime) --> ([token('Am',_)];[token('am',_)]), [token('Mittag',_)], [token('des',_)].
timeofday(daytime) --> ([token('Am',_)];[token('am',_)]), [token('Nachmittag',_)], [token('des',_)].
timeofday(daytime) --> ([token('Am',_)];[token('am',_)]), [token('Abend',_)], [token('des',_)].

debutPeriode --> [token('seit',_)].
debutPeriode --> [token('Seit',_)].
debutPeriode --> [token('ab',_)].
debutPeriode --> [token('Ab',_)].
debutPeriode --> [token('nach',_)].
debutPeriode --> [token('Nach',_)].
debutPeriode --> [token('binnen',_)].
debutPeriode --> [token('Binnen',_)].

finPeriode --> [token('bis',_)].
finPeriode --> [token('Bis',_)].
finPeriode --> [token('vor',_)].
finPeriode --> [token('Vor',_)].


delimiteursPeriode('zwischen', 'und').
delimiteursPeriode('Zwischen', 'und').
delimiteursPeriode('von', 'bis').
delimiteursPeriode('Von', 'bis').
delimiteursPeriode('Periode', '-').

prefixePonctuel('im').
prefixePonctuel('Im').
prefixePonctuel('von').
prefixePonctuel('Von').
prefixePonctuel('am').
prefixePonctuel('Am').
prefixePonctuel('in').
prefixePonctuel('In').
%prefixePonctuel('der').
%prefixePonctuel('Der').
prefixePonctuel('dem').
prefixePonctuel('Dem').
prefixePonctuel('den').
prefixePonctuel('Den').
/*
%% Regel für deiktische Artikel

det_anaphoric(Deta) -->
    ls_token(Deta, tag: 'PDS').
det_anaphoric(Deta) -->
    ls_token(Deta, tag: 'PDAT').
*/

numerique(Texte) -->
    ls_token(Texte, lemma : '@card@').

findDet(Det) -->
ls_token(Det, tag: 'ART').

findAdj(Adj) -->
ls_token(Adj, tag: 'ADJA').

findNoun(NN) -->
ls_token(NN, tag: 'NN').

findAdv(Adv) -->
ls_token(Adv, tag: 'ADJD').

findPraep(Praep) -->
ls_token(Praep, tag: 'APPR').
