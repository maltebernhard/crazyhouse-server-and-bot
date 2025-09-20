-- module (NICHT ÄNDERN!)
module CrazyhouseBot
    ( getMove
    , listMoves
    ) 
    where

import Data.Char
-- Weitere Modulen können hier importiert werden

import Util

--- external signatures (NICHT ÄNDERN!)
getMove :: String -> String
getMove [] = []
getMove a = a


listMoves :: String -> String
listMoves xs = xs

-- YOUR IMPLEMENTATION FOLLOWS HERE
allMoves :: String -> Int -> Int -> String
allMoves xs i j = allMovesA xs i j 1 1

allMovesA :: String -> Int -> Int -> Int -> Int -> String
allMovesA xs i j 8 8 = if iCanMove xs i j 8 8 then printMove i j 8 8 else []
allMovesA xs i j a 8 = if iCanMove xs i j a 8 then printMove i j a 8 ++ [','] ++ allMovesA xs i j (a+1) 1 else allMovesA xs i j (a+1) 1
allMovesA xs i j a b = if iCanMove xs i j a b then printMove i j a b ++ [','] ++ allMovesA xs i j a (b+1) else allMovesA xs i j a (b+1)

--ändert Eingabestring um
--z.B. "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR/" -> "rnbqkbnr/pppppppp/00000000/00000000/00000000/00000000/PPPPPPPP/RNBQKBNR/ w"
changeString :: String -> String
changeString [] = []
changeString (c:cs)
  | not (cS1 (c:cs)) = []
  | otherwise = if c `elem` ['1','2','3','4','5','6','7','8'] then printNulles (ord c - 48) ++ changeString cs else c : changeString cs

--überprüft ob alle Eingabesymbole richtig sind
cS1 :: String -> Bool
cS1 [c] = c `elem` "rnbqkpPRNBQK12345678/"
cS1 (c:cs) = c `elem` "rnbqkpPRNBQK12345678/" && cS1 cs

-- druckt Nullen (nur in changeString gebräuchtlich)
printNulles :: Int -> String
printNulles 0 = []
printNulles a = '0' : printNulles (a-1)

--gibt an ob der Spieler/Bot weiß ist
isWhite :: String -> Bool
isWhite xs = last xs == 'w'

--macht aus 2 Koordinaten einen wohl-formatierten Move z.B. 1 1 2 2 -> "(a1-b2)""
printMove :: Int -> Int -> Int -> Int -> String
printMove is js iz jz = '(' : chr (96+js) : chr (48+is) : '-' : chr (96+jz) : chr (48+iz) : [')']

-- Findet Figur oder 0 auf gegebenem Feld
atPosition :: String -> Int -> Int -> Char
atPosition [] _ _ = '-'
atPosition xs i j
     | (i-1)*9+(j-1) < length xs = xs !! ((i-1)*9+(j-1))
     | otherwise = '-'

--gibt aus ob die Figur auf dem gegebenen Feld eine Figur meines Gegners ist
isEnemy :: String -> Int -> Int -> Bool
isEnemy [] _ _ = False
isEnemy xs i j = (atPosition xs i j /= '0') && (isLower (atPosition xs i j) == isWhite xs)

--gibt aus ob die Figur auf dem gegebenen Feld eine Figur von mir ist
isMe :: String -> Int -> Int -> Bool
isMe [] _ _ = False
isMe xs i j = (atPosition xs i j /= '0') && (isUpper (atPosition xs i j) == isWhite xs)

--überpruft ob sich gegebene Koordinaten im Feld befinden
outOfField :: Int -> Int -> Bool
outOfField i j
    | i `notElem` [1..8] = True
    | j `notElem` [1..8] = True 
    | otherwise = False

--allgemeine Funktion, die überprüft ob ein Zug vom Startfeld (is,js) zum Zielfeld (iz,jz) gemacht werden kann (beliebiger Spieler)
canMove :: String -> Int -> Int -> Int -> Int -> Bool
canMove [] _ _ _ _ = False
canMove xs is js iz jz
  | outOfField is js || outOfField iz jz = False
  -- sollte das Zielfeld nicht-leer sein und die Figuren sind beide in Groß-/Kleinbuchstaben -> 
  | atPosition xs iz jz /= '0' && isLower (atPosition xs is js) == isLower (atPosition xs iz jz) = False
  --ansonsten wird nach Figur eine eigene Funktion aufgerufen
  | otherwise = case toLower (atPosition xs is js) of {
      'k' -> moveKk xs is js iz jz;
      'q' -> moveQq xs is js iz jz;
      'b' -> moveBb xs is js iz jz;
      'n' -> moveNn xs is js iz jz;
      'r' -> moveRr xs is js iz jz;
      -- bei Bauern müssen wir unterscheiden, da diese je nach Farbe unterschiedlich laufen
      'p' -> if atPosition xs is js == 'P' then movePpWhite xs is js iz jz else movePpBlack xs is js iz jz;
      -- wenn auf dem Feld 0 oder eine invalide Figur ist, dann kann man sich auch nicht bewegen
      _ -> False
  }

--Funktion, die überprüft ob ich einen Zug machen kann (ob ich auf dem Startfeld bin, ob ich nicht auf dem Zielfeld bin, ob mein König nach dem Zug safe bleibt & ob ich moven kann)
iCanMove :: String -> Int -> Int -> Int -> Int -> Bool
iCanMove xs is js iz jz = isMe xs is js && not (isMe xs iz jz) && isKingSafe xs is js iz jz && canMove xs is js iz jz

--kann der König laufen (beliebiger Spieler)
moveKk :: String -> Int -> Int -> Int -> Int -> Bool
moveKk xs is js iz jz
  -- wenn Zeile gleich, dann darf das Zielfeld nur 1 Spalte entfernt sein, sonst: ungültig
  | is == iz = abs(js-jz)==1
  -- wenn Spalte gleich, dann darf das Zielfeld nur 1 Zeile entfernt sein, sonst: ungültig
  | js == jz = abs(iz-is)==1
  -- wenn beide Cases falsch : sollte das Zielfeld 1 Zeile entfernt sein, darf es auch nur 1 Spalte entfernt sein, sonst: ungültig
  | abs(is-iz)==1 = abs(jz-js)==1
  -- wenn alle Cases falsch sein sollten, dann ist der Zug ungültig
  | otherwise = False

--kann die Königin laufen (beliebiger Spieler)
moveQq :: String -> Int -> Int -> Int -> Int -> Bool
  -- Königin kann sich genauso bewegen wie ein Läufer und ein Turm
moveQq xs is js iz jz = moveNn xs is js iz jz || moveRr xs is js iz jz

--kann der Springer laufen (beliebiger Spieler)
moveBb :: String -> Int -> Int -> Int -> Int -> Bool
moveBb xs is js iz jz
  -- wenn 1 Zeile entfernt, dann muss 2 Spalten entfernt sein, sonst: ungültig
  | abs(is-iz)==1 = abs(jz-js)==2
  -- wenn 2 Zeilen entfernt, dann muss 1 Spalte entfernt sein, sonst: ungültig
  | abs(is-iz)==2 = abs(jz-js)==1
  -- wenn alles scheitert -> ungültiger Zug
  | otherwise = False

--kann der Läufer laufen (beliebiger Spieler)
moveNn :: String -> Int -> Int -> Int -> Int -> Bool
moveNn xs is js iz jz
  -- wenn Ziel über und rechts von Start, dann müssen die jeweiligen Entfernungen gleich sein und auf dem Weg dürfen keine Figuren sein, sonst: ungültig
  | iz > is && jz > js = abs(is-iz) == abs(js-jz) && freeQuer xs is js '+' '+' (abs(js-jz)-1)               --(Differenz-1), da auf dem Zielfeld ein Gegner sein könnte
  -- wenn Ziel über und links von Start, dann müssen die jeweiligen Entfernungen gleich sein und auf dem Weg dürfen keine Figuren sein, sonst: ungültig
  | iz > is && jz < js = abs(is-iz) == abs(js-jz) && freeQuer xs is js '+' '-' (abs(js-jz)-1)  
  -- wenn Ziel unter und rechts von Start, dann müssen die jeweiligen Entfernungen gleich sein und auf dem Weg dürfen keine Figuren sein, sonst: ungültig
  | iz < is && jz > js = abs(is-iz) == abs(js-jz) && freeQuer xs is js '-' '+' (abs(js-jz)-1)
  -- wenn Ziel unter und links von Start, dann müssen die jeweiligen Entfernungen gleich sein und auf dem Weg dürfen keine Figuren sein, sonst: ungültig
  | iz < is && jz < js = abs(is-iz) == abs(js-jz) && freeQuer xs is js '-' '-' (abs(js-jz)-1)
  -- sollte kein Fall zutreffen -> ungültiger Zug
  | otherwise = False

--kann der Turm laufen (beliebiger Spieler)
moveRr :: String -> Int -> Int -> Int -> Int -> Bool
moveRr xs is js iz jz
  -- wenn Zeilen gleich, dann darf nichts auf dem Weg sein, sonst: ungültig
  | is==iz = if jz<js then freeLang xs is js (jz+1) else freeLang xs is js (jz-1)
  -- wenn Spalten gleich, dann darf nichts auf dem Weg sein, sonst: ungültig
  | js==jz = if iz<is then freeHoch xs is (iz+1) js else freeHoch xs is (iz-1) js 
  -- wenn beides falsch -> ungültiger Zug
  | otherwise = False


--kann der Bauer laufen (schwarz)
movePpBlack :: String -> Int -> Int -> Int -> Int -> Bool
movePpBlack xs is js iz jz
  -- Doppelschritt: wenn Startfeld in 2. Zeile und Ziel in 4. Zeile, dann müssen Spalten gleich und auf dem Weg darf niemand, sonst: ungültig
  --Achtung: hier wird freeHoch xs 2 js !!3!! genommen, weil auf dem Zielfeld keiner sein darf
  | is == 2 && iz==4 = js == jz && freeHoch xs 2 4 js
  -- wenn ich quer nach links oder rechts will, muss an der Stelle ein weißer Spieler sein 
  | iz == is+1 && abs(js-jz) == 1 = isUpper (atPosition xs iz jz)
  -- einen Schritt vor gehen wenn dort leer ist
  | iz == is+1 && js==jz = atPosition xs iz jz == '0'
  --sonst -> ungültiger Zug
  | otherwise = False

--kann der Bauer laufen (schwarz)
movePpWhite :: String -> Int -> Int -> Int -> Int -> Bool
movePpWhite xs is js iz jz
  -- Doppelschritt: wenn Startfeld in 7. Zeile und Ziel in 5. Zeile, dann müssen Spalten gleich und auf dem Weg darf niemand, sonst: ungültig
  | is == 7 && iz==5 = js == jz && freeHoch xs 7 5 js
  -- wenn quer nach links oder rechts will, muss an Stelle weißer Spieler sein 
  | iz == is-1 && abs(js-jz) == 1 = isLower (atPosition xs iz jz)
  -- einen Schritt runter gehen wenn dort leer ist
  | iz == is-1 && js==jz = atPosition xs iz jz == '0'
  -- sonst -> ungültig
  | otherwise = False

--simuliert einen Move und ändert dementsprechend das Spielfeld
doMove :: String -> Int -> Int -> Int -> Int -> String
doMove xs is js iz jz = let a = (is-1)*9+(js-1) in
                        let b = (iz-1)*9+(jz-1) in
                        let c = min a b in
                        let d = max a b in
  take c xs ++ (if c==a then ['0'] else [atPosition xs is js]) ++ take (d-c-1) (drop (c+1) xs) ++ (if c==a then [atPosition xs is js] else ['0']) ++ take (length xs -d-2) (drop d xs) ++ [atPosition xs iz jz] ++ drop (length xs -2) xs

--findet den eigenen König (findKing ruft findkingA mit i=0 und j=0 auf)
findKing :: String -> [Int]
findKing xs = findKingA xs 0 0
findKingA :: String -> Int -> Int -> [Int]
findKingA (x:xs) i j = case x of{
    --wenn wir weiß sind und wir 'K' gefunden haben, geben wir die Position aus, sonst: weiter in Zeile suchen
    'K' -> if isWhite xs then i : [j] else findKingA xs i (j+1);
    --wenn wir nicht weiß sind und wir 'k' gefunden haben, geben wir die Position aus, sonst: weiter in Zeile suchen
    'k' -> if isWhite xs then findKingA xs i (j+1) else i:[j];
    --wenn wir auf ein / treffen, ändern wir die Spalte
    '/' -> findKingA xs (i+1) 0;
    --wenn nichts zutrifft -> weiter in Zeile suchen
    _ -> findKingA xs i (j+1)
  }

--überprüft ob der eigene König noch safe ist, wenn man einen bestimmten Zug macht (isKingSafe ruft isKingSafe2 mit i=0 und j=0 auf)
isKingSafe :: String -> Int -> Int -> Int -> Int -> Bool
isKingSafe xs is js iz jz = isKingSafe2 xs is js iz jz 1 1

isKingSafe2 :: String -> Int -> Int -> Int -> Int -> Int -> Int -> Bool
--allgemeine Erklärung: wir sind sicher, wenn auf allen Feldern des Brettes kein Zug gemacht werden kann, sodass unser König geschlagen wird
--canMove verwendet statt das aktuelle Brett das neue Brett wenn wir den Zug machen würden und berechnet ob wir von i j zum König kommen
--falls Zug unmöglich -> Rekursion    falls Zug möglich -> Abbruch

--wenn i=8 und j=8 und keine Figur unseren König schlagen kann, dann ist unser König sicher
isKingSafe2 xs is js iz jz 8 8 = not (canMove (doMove xs is js iz jz) 8 8 (head (findKing xs)) (last (findKing xs))) || isMe xs 8 8
-- bei j=8 -> Zeile ändern
isKingSafe2 xs is js iz jz i 8 = not (canMove (doMove xs is js iz jz) i 8 (head (findKing xs)) (last (findKing xs))) &&
                                  isKingSafe2 xs is js iz jz (i+1) 1
-- Normalfall
isKingSafe2 xs is js iz jz i j = not (canMove (doMove xs is js iz jz) i j (head (findKing xs)) (last (findKing xs))) &&
                                  isKingSafe2 xs is js iz jz i (j+1)

--prüft ob in einer Querreihe keine Figuren im Weg stehen
-- bekommt Board, Startkoordinaten, 2 Zeichen (+ und -) und die Anzahl an Felder, die es zu beobachten gilt
freeQuer :: String -> Int -> Int -> Char -> Char -> Int -> Bool
freeQuer xs is js a b d
    --Rekursionsanker - wenn wir auf dem selben Feld sind und keine Figur im Weg stand, dann True
    | d == 0 = True
    --"+" "+" -> Querreihe nach oben rechts; falls das Feld d-mal-rechts-über Start ist frei -> Rekursion
    | a == b && b == '+' = atPosition xs (is+d) (js+d) == '0' && freeQuer xs is js a b (d-1)
    -- "+" "-" -> Querreihe nach oben links
    | a=='+' && b=='-' = atPosition xs (is+d) (js-d) == '0' && freeQuer xs is js a b (d-1)
    -- "-" "+" -> Querreihe nach unten rechts
    | a=='-' && b=='+' =atPosition xs (is-d) (js+d) == '0' && freeQuer xs is js a b (d-1)
    -- "-" "-" -> Querreihe unten links
    | a==b && b == '-' = atPosition xs (is-d) (js-d) == '0' && freeQuer xs is js a b (d-1)

--prüft ob in einer Spalte keine Figuren im Weg stehen
--bekommt Board, Spalte, Startzeile und Anzahl an Felder, die es zu beobachten gilt
freeHoch :: String -> Int -> Int -> Int -> Bool
freeHoch xs is iz j
  | is==iz = True
  | is>iz = atPosition xs iz j == '0' && freeHoch xs is (iz+1) j
  | is<iz = atPosition xs iz j == '0' && freeHoch xs is (iz-1) j

--prüft ob in einer Zeile keine Figuren im Weg stehen
--bekommt Board, Spalte, Startzeile und Anzahl an Felder, die es zu beobachten gilt
freeLang :: String -> Int -> Int -> Int -> Bool
freeLang xs i js jz
  | js == jz = True
  | js>jz = atPosition xs i jz == '0' && freeHoch xs i js (jz+1)
  | js<jz = atPosition xs i jz == '0' && freeHoch xs i js (jz-1)


