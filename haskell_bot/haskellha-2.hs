import Util

teil :: [Char] -> [[Char]]
teil x = Util.splitOn '/' x  

getplayer :: [Char] -> Char 
getplayer x = last (last ((teil x))) 

getreserve :: [Char] -> [Char]
getreserve x = init (init (last (teil x)))

board ::[Char] -> [Char]
board x = boardString (oneString( init (teil x)))

oneString :: [[Char]] -> [Char]
oneString [] = []
oneString (x:xs) = x ++ oneString (xs)

nul :: Char -> [Char]
nul x 
    | x == '1' = "0"
    | x == '2' = "00"
    | x == '3' = "000"
    | x == '4' = "0000"
    | x == '5' = "00000"
    | x == '6' = "000000"
    | x == '7' = "0000000"
    | x == '8' = "00000000"

boardString :: [Char] -> [Char]
boardString [] = []
boardString (x:xs) 
    | x `elem` ['1','2','3','4','5','6','7','8'] = nul x ++ boardString xs 
    | otherwise = x : boardString xs 

boardKonstante :: Int -> [Char]
boardKonstante x = "a8b8c8d8e8f8g8h8a7b7c7d7e7f7g7h7a6b6c6d6e6f6g6h6a5b5c5d5e5f5g5h5a4b4c4d4e4f4g4h4a3b3c3d3e3f3g3h3a2b2c2d2e2f2g2h2a1b1c1d1e1f1g1h1"

zipTupel :: [Char] -> [Char] -> [([Char], Char)]
zipTupel [] _ = []
zipTupel _ [] = []
zipTupel x (y:ys) = (take 2 x,y) : zipTupel (drop 2 x) ys 

-- Position und Figur in Tupeln z.B. ("a8"-'r'),("a7"-'n'),("a6"-'0')...
tupelP :: [Char] -> [([Char], Char)]
tupelP x = zipTupel (boardKonstante 0) (board x)

-- Gibt 0 zurück, wenn das Feld leer ist, 1 für den Gegner und 2 für Spieler
isEnemy :: [([Char], Char)] -> [Char] -> [Char] -> Int 
isEnemy string position eigeneposition
    | getFigur string (position) == '0' = 0
    | (getFigur string (position) `elem` ['k','q','b','n','r','p']) == (getFigur string (eigeneposition) `elem` ['k','q','b','n','r','p']) = 2 
    | (getFigur string (position) `elem` ['K','Q','B','N','R','P']) == (getFigur string (eigeneposition) `elem` ['K','Q','B','N','R','P']) = 2
    | otherwise = 1

-- gibt Figur an Position zurück z.B. an "g2" ist der Rückgabewert 'Q' 
getFigur :: [([Char], Char)] -> [Char] -> Char
getFigur [] _ = 'A'
getFigur ((x,y):xs) position 
    | x == position = y
    | otherwise = getFigur xs position

-- verändert Position um einen Schritt in gegebener Richtung, z.B. "a2" nach unten ("u") gibt "a1" zurück
schritt :: [Char] -> [Char] -> [Char]
schritt position richtung
    | richtung == "o" = (take 1 position) ++ intzuChar(charzuInt(last position) + 1)
    | richtung == "u" = (take 1 position) ++ intzuChar(charzuInt(last position) - 1) 
    | richtung == "r" = intzuChar(charzuInt(head position)+1) ++ (drop 1 position)
    | richtung == "l" = intzuChar(charzuInt(head position)-1) ++ (drop 1 position)
    | richtung == "or" = intzuChar(charzuInt(head position)+1) ++ intzuChar(charzuInt(last position) + 1)
    | richtung == "ol" =  intzuChar(charzuInt(head position)-1) ++ intzuChar(charzuInt(last position) + 1)
    | richtung == "ur" = intzuChar(charzuInt(head position)+1) ++ intzuChar(charzuInt(last position) - 1)
    | richtung == "ul" = intzuChar(charzuInt(head position)-1) ++ intzuChar(charzuInt(last position) - 1)

charzuInt :: Char -> Int 
charzuInt c = fromEnum c - 48 

intzuChar :: Int -> [Char] 
intzuChar i = [toEnum (i + 48)] 

-- Wie viele Schritte noch maximal bis Boardende von gegebener Position und Richtung möglich sind
maxschritte :: [Char] -> [Char] -> Int 
maxschritte position richtung
    | richtung == "o" = 8 - charzuInt(last position) 
    | richtung == "u" = charzuInt(last position) - 1
    | richtung == "r" = charzuInt('h') - charzuInt(head position) 
    | richtung == "l" = charzuInt(head position) - 49 
    | richtung == "or" = mi (maxschritte position "o") (maxschritte position "r") 
    | richtung == "ol" = mi (maxschritte position "o") (maxschritte position "l") 
    | richtung == "ur" = mi (maxschritte position "u") (maxschritte position "r") 
    | richtung == "ul" = mi (maxschritte position "u") (maxschritte position "l") 

-- laufe eine gegebene Anzahl von Schritten in gegebene Richtung und gebe jeden move in einer Liste aus
-- z.B. laufe 3 Schritte von "b2" nach rechts --> "b2-c3","b2-d2","b2-e2"
laufenBis :: [([Char], Char)] -> [Char] -> [Char] -> [Char] -> Int -> Int -> [[Char]]
laufenBis _ _ _ _ _ 0 = []
laufenBis _ _ _ _ 2 _ = []
laufenBis string alt position richtung 1 _ = laufenBis string alt position richtung 0 1
laufenBis string alt position richtung frei max = (alt ++ "-" ++ schritt position richtung ) 
    : (laufenBis string alt (schritt position richtung) richtung (isEnemy string (schritt (schritt position richtung)richtung) alt) (max-1))

-- ist auf dem angrenzenden Feld Spieler (2), Gegenspieler (1) oder ist es frei (0)?
neighbor :: [([Char], Char)] -> [Char] -> [Char] -> Int
neighbor string position richtung = isEnemy string (schritt position richtung) position

-- so wie laufenBis nur ohne Schlagen (für Bauern)
laufenBisOhneSchlagen :: [([Char], Char)] -> [Char] -> [Char] -> [Char] -> Int -> Int -> [[Char]]
laufenBisOhneSchlagen _ _ _ _ _ 0 = []
laufenBisOhneSchlagen _ _ _ _ 2 _ = []
laufenBisOhneSchlagen string alt position richtung 1 _ = laufenBisOhneSchlagen string alt position richtung 0 1
laufenBisOhneSchlagen string alt position richtung frei max = (alt ++ "-" ++ schritt position richtung ) 
    : (laufenBisOhneSchlagen string alt (schritt position richtung) richtung  (nichtSchlagen string (schritt position richtung) richtung) (max-1))

bauerSchlagen :: [([Char], Char)] -> [Char] -> [[Char]]
bauerSchlagen s p
    | (getFigur s (p) `elem` ['K','Q','B','N','R','P']) && neighbor s p "or" == 1 = laufenBis s p p "or" 1 1
    | (getFigur s (p) `elem` ['K','Q','B','N','R','P']) && neighbor s p "ol" == 1 = laufenBis s p p "ol" 1 1
    | (getFigur s (p) `elem` ['k','q','b','n','r','p']) && neighbor s p "ul" == 1 = laufenBis s p p "ul" 1 1
    | (getFigur s (p) `elem` ['k','q','b','n','r','p']) && neighbor s p "ur" == 1 = laufenBis s p p "ur" 1 1
    | otherwise = []

-- Hilfsfunktion für Bauern
nichtSchlagen :: [([Char], Char)] -> [Char] -> [Char] -> Int
nichtSchlagen string position richtung
    | isEnemy string (schritt position richtung) position == 0 = 0
    | otherwise = 2 

bauerLaufen :: [([Char], Char)] -> [Char] -> [[Char]]
bauerLaufen s p
    | drop 1 p == "2" && (getFigur s (p) `elem` ['K','Q','B','N','R','P'])  
             = laufenBisOhneSchlagen s p p "o" (nichtSchlagen s p "o") 2
    | drop 1 p == "7" && (getFigur s (p) `elem` ['k','q','b','n','r','p'])
             = laufenBisOhneSchlagen s p p "u" (nichtSchlagen s p "u") 2
    | drop 1 p /= "2" && (getFigur s (p) `elem` ['K','Q','B','N','R','P']) 
              = laufenBisOhneSchlagen s p p "o" (nichtSchlagen s p "o") 1
    | drop 1 p /= "7" && (getFigur s (p) `elem` ['k','q','b','n','r','p'])
              = laufenBisOhneSchlagen s p p "u" (nichtSchlagen s p "u") 1

-- Kombinieren von bauerLaufen und bauerSchlagen
bauer :: [([Char], Char)]-> [Char] -> [[Char]]
bauer s p = bauerSchlagen s p ++ bauerLaufen s p 

queen :: [([Char], Char)]-> [Char] -> [[Char]]
queen s p = turm s p ++ bishop s p

turm :: [([Char], Char)] -> [Char] -> [[Char]]
turm s p = (laufenBis s p p "o" (neighbor s p "o") (maxschritte p "o")) ++ 
            (laufenBis s p p "u" (neighbor s p "u") (maxschritte p "u")) ++
            (laufenBis s p p "r" (neighbor s p "r") (maxschritte p "r")) ++
            (laufenBis s p p "l" (neighbor s p "l") (maxschritte p "l")) 

bishop :: [([Char], Char)] -> [Char] -> [[Char]]
bishop s p = (laufenBis s p p "or" (neighbor s p "or") (maxschritte p "or")) ++
            (laufenBis s p p "ol" (neighbor s p "ol") (maxschritte p "ol")) ++
            (laufenBis s p p "ur" (neighbor s p "ur") (maxschritte p "ur")) ++
            (laufenBis s p p "ul" (neighbor s p "ul") (maxschritte p "ul"))

mi :: Int -> Int -> Int 
mi a b = if a < b then a else b 

king ::[([Char], Char)] -> [Char] -> [[Char]]
king s p = (laufenBis s p p "o" (neighbor s p "o") (mi (maxschritte p "o") 1))  ++ 
            (laufenBis s p p "u" (neighbor s p "u") (mi(maxschritte p "u") 1)) ++
            (laufenBis s p p "r" (neighbor s p "r") (mi(maxschritte p "r") 1)) ++
            (laufenBis s p p "l" (neighbor s p "l") (mi(maxschritte p "l") 1)) ++
            (laufenBis s p p "or" (neighbor s p "or") (mi(maxschritte p "or") 1)) ++
            (laufenBis s p p "ol" (neighbor s p "ol") (mi(maxschritte p "ol") 1)) ++
            (laufenBis s p p "ur" (neighbor s p "ur") (mi(maxschritte p "ur") 1)) ++
            (laufenBis s p p "ul" (neighbor s p "ul") (mi(maxschritte p "ul") 1))

-- welche Felder kann ein Springer theoretisch erreichen
springertheo :: [Char] -> [Char]
springertheo p = intzuChar(charzuInt(head p)+1)++intzuChar(charzuInt(last p)+2) ++ 
                    intzuChar(charzuInt(head p)+1)++intzuChar(charzuInt(last p)-2) ++
                    intzuChar(charzuInt(head p)+2)++intzuChar(charzuInt(last p)+1) ++
                    intzuChar(charzuInt(head p)+2)++intzuChar(charzuInt(last p)-1) ++
                    intzuChar(charzuInt(head p)-1)++intzuChar(charzuInt(last p)+2) ++
                    intzuChar(charzuInt(head p)-1)++intzuChar(charzuInt(last p)-2) ++
                    intzuChar(charzuInt(head p)-2)++intzuChar(charzuInt(last p)+1) ++
                    intzuChar(charzuInt(head p)-2)++intzuChar(charzuInt(last p)-1) 

-- existiert Position
existPosition :: [Char] -> Bool 
existPosition c = (between 'a' 'h' (head c)) && (between '1' '8' (last c))

-- Feld muss existieren und entweder frei sein, oder vom Gegner besetzt
springer :: [([Char], Char)] -> [Char] -> [Char] -> [[Char]]
springer s [] p = []
springer s st p 
    | existPosition(take 2 st) && (isEnemy s (take 2 st) p) /= 2 = (p ++ "-" ++ (take 2 st) ): springer s (drop 2 st) p 
    | otherwise = springer s (drop 2 st) p   

-- Kombinieren von beiden Springer-Funktionen
knight ::[([Char], Char)] -> [Char] -> [[Char]]
knight s p = springer s (springertheo p) p 

between :: Char -> Char -> Char -> Bool
between low high x = low <= x && x <= high

-- alle Züge, die weiß theoretisch machen kann
movesWhite ::  [([Char], Char)] -> [([Char], Char)] -> String -> [[Char]]
movesWhite [] _ _ = []
movesWhite ((x,y):xs) string eingabe
    | y == 'P' = bauer string x ++ movesWhite xs string eingabe
    | y == 'R' = turm string x ++ movesWhite xs string eingabe
    | y == 'N' = knight string x ++ movesWhite xs string eingabe
    | y == 'B' = bishop string x ++ movesWhite xs string eingabe
    | y == 'Q' = queen string x ++ movesWhite xs string eingabe
    | y == 'K' = king string x ++ movesWhite xs string eingabe
    | y == '0' = (setReserve (getreserve eingabe) x 'w') ++ movesWhite xs string eingabe
    |otherwise = movesWhite xs string eingabe

-- alle Züge, die schwarz theoretisch machen kann
movesBlack ::  [([Char], Char)] -> [([Char], Char)] -> String -> [[Char]]
movesBlack [] _ _ = []
movesBlack ((x,y):xs) string eingabe
    | y == 'p'= bauer string x ++ movesBlack xs string eingabe
    | y =='r' = turm string x ++ movesBlack xs string eingabe
    | y == 'n' = knight string x ++ movesBlack xs string eingabe
    | y == 'b' = bishop string x ++ movesBlack xs string eingabe
    | y == 'q' = queen string x ++ movesBlack xs string eingabe
    | y == 'k' = king string x ++ movesBlack xs string eingabe
    | y == '0' = (setReserve (getreserve eingabe) x 'b') ++ movesBlack xs string eingabe
    | otherwise = movesBlack xs string eingabe

-- für alle Figuren der Reserve wird ein Zug auf das gegebene Feld in die Liste aufgenommen
setReserve :: [Char] -> [Char] -> Char -> [[Char]]
setReserve [] _ _ = []
setReserve reserve position player
    | (head reserve `elem` ['k','q','b','n','r']) && (player == 'b') && (head reserve /= endeReserve reserve)= ([head reserve] ++ "-" ++ position)
                : setReserve (tail reserve) position player   
    | (head reserve `elem` ['K','Q','B','N','R']) && (player == 'w')&& (head reserve /= endeReserve reserve) = ([head reserve] ++ "-" ++ position)
                : setReserve (tail reserve) position player 
    | head reserve == 'p' && player == 'b' && (last position `elem` ['2','3','4','5','6','7']) && (head reserve /= endeReserve reserve)=  ([head reserve] ++ "-" ++ position)
                : setReserve (tail reserve) position player   
    | head reserve == 'P' && player == 'w' && (last position `elem` ['2','3','4','5','6','7']) && (head reserve /= endeReserve reserve)=  ([head reserve] ++ "-" ++ position)
                : setReserve (tail reserve) position player   
    | otherwise = setReserve (tail reserve) position player

endeReserve :: [Char] -> Char 
endeReserve reserve = if length reserve > 1 then head (tail reserve) else '0' 

-- setzte Feld, wo die Figur her kam auf 0
setFigurVon :: [([Char], Char)] -> [([Char], Char)]-> [Char] -> Int -> [([Char], Char)]
setFigurVon [] _ _ _ = [("a0", 'A')]
setFigurVon ((x,y):xs) liste von zaehler
    | x == von = take zaehler liste ++ (von, '0') : xs 
    | otherwise = setFigurVon xs liste von (zaehler+1) 

-- setze die Figur auf die Zielposition
setFigurNach :: [([Char], Char)] -> [([Char], Char)]-> [Char] -> Char -> Int -> [([Char], Char)]
setFigurNach [] _ _ _ _ = [("a0", 'B')]
setFigurNach ((x,y):xs) liste nach figur zaehler
    | x == nach = take zaehler liste ++ (nach, figur) : xs 
    | otherwise = setFigurNach xs liste nach figur (zaehler+1)

-- Verbindung von setFigurVon und setFigurNach
doMove :: [([Char], Char)] -> [Char] ->[([Char], Char)]
doMove string move 
    | length move == 5 = setFigurNach (setFigurVon string  string (take 2 move) 0)
     (setFigurVon string string (take 2 move) 0) (drop 3 move) (getFigur string (take 2 move)) 0
    | otherwise = setFigurNach string string (drop 2 move) (head move) 0   

-- gibt Position des Königs zurück
whereIsKing :: [([Char], Char)] -> Char -> [Char]
whereIsKing [] _ = "A"
whereIsKing ((x,y):xs) player
    | y == 'K' && player == 'w' = x
    | y == 'k' && player == 'b' = x
    | otherwise =  whereIsKing xs player 

-- König im Schach
check :: [[Char]] -> Char -> [([Char], Char)] -> Bool 
check [] _ _ = False
check (x:xs) player string
    | drop ((length x)-2) x == whereIsKing string player = True
    | otherwise = check xs player string

-- alle theoretisch möglichen Züge vom gegebenen Player (ohne auf König zu achten)
possibleMoves :: [([Char], Char)] ->[([Char], Char)] -> Char -> String -> [[Char]]
possibleMoves string tupel player eingabe = if player == 'w' then (movesWhite tupel string eingabe) else (movesBlack tupel string eingabe) 

-- ist der König von gegebenem Player im Schach
kinginCheck :: [([Char], Char)] -> [([Char], Char)] -> String -> Char -> Bool 
kinginCheck string tupel eingabe player
    | player == 'w' = check (possibleMoves string tupel 'b' eingabe) 'w' string
    | otherwise = check (possibleMoves string tupel 'w' eingabe) 'b' string 

-- nur alle Züge, an deren Ende der eigene König nicht im Schach steht
correctMoves :: [[Char]] -> [([Char], Char)] -> String -> [Char]
correctMoves [] _ _ = []
correctMoves (x:xs) string eingabe
    | kinginCheck (doMove string x) (doMove string x) eingabe (getplayer eingabe) == False = x ++ "," ++  correctMoves xs string eingabe
    | otherwise = correctMoves xs string eingabe

-- ######## fertige ListMoves Funktion ########
listMoves :: String -> String 
listMoves string = correctMoves (possibleMoves (tupelP string) (tupelP string)(getplayer string) string) (tupelP string) string

-- nur alle Züge, an deren Ende der eigene König nicht im Schach steht --> in Listenform (Hilfsfunktion)
correctMovesArray :: [[Char]] -> [([Char], Char)] -> String -> [[Char]]
correctMovesArray [] _ _ = []
correctMovesArray (x:xs) string eingabe
    | kinginCheck (doMove string x) (doMove string x) eingabe (getplayer eingabe) == False = x : correctMovesArray xs string eingabe
    | otherwise = correctMovesArray xs string eingabe

-- listMoves in Listenform (Hilfsfunktion)
getMovesArray :: String -> [[Char]]
getMovesArray string = correctMovesArray (possibleMoves (tupelP string) (tupelP string) (getplayer string) string) (tupelP string) string

-- Gegner
notme :: Char -> Char 
notme a = if a == 'w' then 'b' else 'w' 

-- alle Züge, mit denen der Spieler den Gegner Schach setzen kann
movesEnemyCheck :: [[Char]] ->[([Char], Char)] -> String -> Char -> [[Char]]
movesEnemyCheck [] _ _ _ = []
movesEnemyCheck (x:xs) string eingabe player
    | kinginCheck (doMove string x) (doMove string x) eingabe player == True = 
        x : movesEnemyCheck xs string eingabe player
    | otherwise = movesEnemyCheck xs string eingabe player

-- gebe allen weißen Figuren auf dem Feld einen Wert und berechne den Gesamtwert
berechneFigurenWertW :: [([Char], Char)] -> Int -> Int 
berechneFigurenWertW [] x = x
berechneFigurenWertW ((x,y):xs) anfang 
    | y == 'K' = berechneFigurenWertW xs (anfang+5)
    | y == 'Q' = berechneFigurenWertW xs (anfang+4)
    | y == 'R' = berechneFigurenWertW xs (anfang+3)
    | y == 'B' = berechneFigurenWertW xs (anfang+2)
    | y == 'N' = berechneFigurenWertW xs (anfang+2)
    | y == 'P' = berechneFigurenWertW xs (anfang+1)
    | otherwise = berechneFigurenWertW xs anfang 

-- gebe allen schwarzen Figuren auf dem Feld einen Wert und berechne den Gesamtwert
berechneFigurenWertS :: [([Char], Char)] -> Int -> Int 
berechneFigurenWertS [] x = x
berechneFigurenWertS ((x,y):xs) anfang 
    | y == 'k' = berechneFigurenWertS xs (anfang+5)
    | y == 'q' = berechneFigurenWertS xs (anfang+4)
    | y == 'r' = berechneFigurenWertS xs (anfang+3)
    | y == 'b' = berechneFigurenWertS xs (anfang+2)
    | y == 'n' = berechneFigurenWertS xs (anfang+2)
    | y == 'p' = berechneFigurenWertS xs (anfang+1)
    | otherwise = berechneFigurenWertS xs anfang 

-- alle Züge, für die der Wert der weißen Figuren auf dem Feld größer wird 
movesWertGroesserW :: [[Char]] ->[([Char], Char)] -> Int -> [[Char]]
movesWertGroesserW [] _ _ = []
movesWertGroesserW (x:xs) string anfang
    | (berechneFigurenWertW (doMove string x) 0) > anfang = x : movesWertGroesserW xs string anfang
    | otherwise = movesWertGroesserW xs string anfang 

-- alle Züge, für die der Wert der schwarzen Figuren auf dem Feld größer wird 
movesWertGroesserS :: [[Char]] ->[([Char], Char)] -> Int -> [[Char]]
movesWertGroesserS [] _ _ = []
movesWertGroesserS (x:xs) string anfang 
    | (berechneFigurenWertS (doMove string x) 0) > anfang = x : movesWertGroesserS xs string anfang
    | otherwise = movesWertGroesserS xs string anfang 

-- alle Züge, für die der Wert der weißen Figuren auf dem Feld kleiner wird 
movesWertKleinerW :: [[Char]] ->[([Char], Char)] -> Int -> [[Char]]
movesWertKleinerW [] _ _ = []
movesWertKleinerW (x:xs) string anfang
    | (berechneFigurenWertW (doMove string x) 0) < anfang = x : movesWertKleinerW xs string anfang
    | otherwise = movesWertKleinerW xs string anfang 

-- alle Züge, für die der Wert der schwarzen Figuren auf dem Feld kleiner wird 
movesWertKleinerS :: [[Char]] ->[([Char], Char)] -> Int -> [[Char]]
movesWertKleinerS [] _ _ = []
movesWertKleinerS (x:xs) string anfang 
    | (berechneFigurenWertS (doMove string x) 0) > anfang = x : movesWertKleinerS xs string anfang
    | otherwise = movesWertKleinerS xs string anfang 

-- zuerst Züge, die den Gegner Schach setzen 
-- dann Züge, die den gegnerischen Figurenwert verringern 
-- dann Züge, die den eigenen Figurenwert erhöhen und dann alle möglichen Züge 
-- doppelte Züge noch drin 
moveFuerSchwarz ::[[Char]] -> String -> [[Char]]
moveFuerSchwarz moves string = movesEnemyCheck moves (tupelP string ) string 'w'  
             ++ movesWertKleinerW moves (tupelP string) (berechneFigurenWertW (tupelP string) 0)
            ++ movesWertGroesserS moves (tupelP string) (berechneFigurenWertS (tupelP string) 0)
             ++ moves 


moveFuerWeiss ::[[Char]] -> String -> [[Char]]
moveFuerWeiss moves string = movesEnemyCheck moves (tupelP string ) string 'b'
        ++ movesWertKleinerS moves (tupelP string) (berechneFigurenWertS (tupelP string) 0)
        ++ movesWertGroesserW moves (tupelP string) (berechneFigurenWertW (tupelP string) 0) 
        ++ moves 

-- erster Move in Liste von moveFuerWeiss
bestmoveWeiss :: String -> String
bestmoveWeiss string =  head (moveFuerWeiss (getMovesArray string) string)

-- erster Move in Liste von moveFuerSchwarz
bestmoveSchwarz :: String -> String
bestmoveSchwarz string =  head (moveFuerSchwarz (getMovesArray string) string)

-- ######### fertige getMove Funktion ############
getMove :: String -> String 
getMove string = if getplayer string == 'w' then bestmoveWeiss string else bestmoveSchwarz string 
