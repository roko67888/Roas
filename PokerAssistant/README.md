# Poker Assistant Android — prototype 0.1

Android prototip za pomoć pri pokeru. Aplikacija ne klikće po poker aplikaciji i ne donosi/izvršava odluke umjesto korisnika.

## Što radi
- 6-max i 9-max način rada
- unos hole cards i boarda
- Monte Carlo equity protiv slučajne ruke (prototip)
- osnovna preporuka prema equityju
- MediaProjection čitanje ekrana
- ML Kit OCR nad snimkom ekrana
- mali HUD preko druge aplikacije, ako korisnik ručno dozvoli overlay
- lokalno spremanje osnovnih statistika igrača kroz `StatsStore`

## Pokretanje
1. Otvori projekt u Android Studiju.
2. Pusti Android Studio da sinkronizira Gradle dependencyje.
3. Pokreni na Androidu 8.0+ (API 26+).
4. Za čitanje ekrana odaberi **Pokreni čitanje ekrana** i potvrdi Androidov MediaProjection dijalog.
5. Ako želiš HUD, ručno omogući **Display over other apps**.

## Važna napomena
OCR u ovoj prvoj verziji čita tekst sa ekrana, ali još nije specijaliziran za svaki poker klijent. Za produkcijsku verziju treba dodati detekciju poker tablice, prepoznavanje karata po vizualnim predlošcima i parser akcija/stackova prilagođen konkretnom klijentu.
