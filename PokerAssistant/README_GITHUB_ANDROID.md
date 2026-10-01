# Poker Assistant — build from Android phone

Ovaj projekt je pripremljen za GitHub Actions. Ne treba ti računalo za build: GitHub će sam napraviti APK.

## 1. Napravi GitHub repository
Na mobitelu otvori GitHub u pregledniku, prijavi se i napravi novi repository, npr. `PokerAssistant`.

## 2. Prenesi sadržaj projekta
Raspakiraj ZIP na telefonu. U GitHub repositoryju koristi **Add file → Upload files** i prenesi cijelu strukturu projekta tako da u rootu repozitorija budu `settings.gradle.kts`, `build.gradle.kts`, `app/` i `.github/`.

Ako mobilni preglednik ne dopušta upload mapa, koristi GitHub Codespaces ili GitHub web editor za dodavanje datoteka i mapa.

## 3. Pokreni build
Nakon commita otvori karticu **Actions** → **Build Android APK**. Workflow se automatski pokreće na `main`, a možeš ga pokrenuti i ručno preko **Run workflow**.

## 4. Preuzmi APK
Kad workflow završi zeleno, otvori završeni run → **Artifacts** → `PokerAssistant-debug-apk`. Preuzmi ZIP artefakt i iz njega izvuci `app-debug.apk`.

Na telefonu otvori APK i dopusti instalaciju iz tog izvora ako Android to zatraži.

## Napomena
Ovo je debug APK. Aplikacija ne klika i ne igra umjesto korisnika. Screen capture/HUD koriste Androidove dozvole i korisnik ih mora eksplicitno odobriti.
