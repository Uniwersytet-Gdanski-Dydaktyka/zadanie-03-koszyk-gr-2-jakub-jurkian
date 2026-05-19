1. Refinement (Analiza wymagań)

Wszystko zaczyna się na spotkaniu z zespołem i Product Ownerem [reprezentuje biznes/klienta]. W Scrumie to Backlog Refinement, ale wiele zespołów (Kanban, Shape Up) robi to mniej formalnie — krótkie sync'i albo asynchronicznie w Slacku/Linearze.
Zadanie to "Ticket" w systemie (Jira, Linear, Azure DevOps).
Rozbijanie na mniejsze części — zamiast robić cały koszyk na raz, dzieli się to na User Stories: "Jako klient chcę dodać produkt do koszyka", "Jako klient chcę otrzymać zniżkę 5% powyżej 300 zł".
Wykrywanie luk biznesowych — zanim napiszesz linijkę kodu, zadajesz pytania: Czy promocje się łączą? Co jeśli mam darmowy kubek i zniżkę 30% – naliczamy zniżkę przed czy po dodaniu kubka?

2. Architektura i Design (Planowanie)

Zanim zaczniesz kodować — kartka, tablica, Miro/Excalidraw.
Diagramy — zamiast pełnego UML coraz częściej C4 model albo Mermaid w README (docs as code). Diagramy generowane z kodu zamiast malowane ręcznie.
Wzorce projektowe — Strategy do różnych typów promocji, Command do operacji koszyka. W realiach 2026 zespoły dyskutują rzadziej w kategoriach GoF, częściej w kategoriach DDD bounded contexts, hexagonal architecture, CQRS.
ADR — krótkie notatki w Confluence/Notion albo .md w repo: "Wybieramy wzorzec Strategy, ponieważ biznes zapowiada częste zmiany promocji".

3. Setup środowiska i Branching

Pobierasz najnowszy kod z repozytorium (Git).
Tworzysz krótkożyjącą gałąź, np. feature/CART-123-discount-strategies. Klasyczny GitFlow odszedł w niepamięć — dominuje trunk-based development: małe PR-y, branch żyje 1-2 dni, niedokończone funkcje chowane są za feature flagami (LaunchDarkly, Unleash) i wdrażane stopniowo.

4. Implementacja i testy (TDD jako ideał)

W profesjonalnym IT nikt nie testuje logiki klikając w aplikacji. Kod piszesz razem z testami.
TDD w czystej formie ("test first, czerwony-zielony-refactor") stosuje konsekwentnie ok. 20-30% zespołów. Częściej spotkasz "test alongside" — testy równolegle z kodem. Twardym wymaganiem jest pokrycie testami: PR bez testów zwykle nie przejdzie review.
Przykład: tworzysz klasę testową w JUnit/AssertJ, piszesz shouldApply5PercentDiscountWhenCartValueExceeds300(), dopiero potem implementację.
AI w workflow — Copilot, Cursor, Claude Code podpowiadają szkielety testów, refaktoryzacje, commit messages. Firmy mają polityki "AI usage" (co wolno wkleić, czego nie).

5. Code Review (PR / Merge Request)

Kod działa lokalnie → push → tworzysz PR.
1-3 programistów z zespołu czyta Twój kod (przy małych branchach review jest szybki).
Sprawdzają: czy nazwy zmiennych są jasne, czy klasy nie są przeładowane, czy nie złamałeś Open/Closed Principle (mnóstwo if-else zamiast polimorfizmu).
Coraz częściej w PR-ach pomaga AI reviewer (CodeRabbit, GitHub Copilot reviews) jako pierwszy filtr — ale ostateczne "Approve" daje człowiek.

6. CI/CD + DevSecOps (shift-left security)

Kod nie idzie od razu do użytkowników. Automatyka przejmuje stery.
Serwer CI (GitHub Actions, GitLab CI, Jenkins) buduje projekt (Maven/Gradle/npm).
Uruchamiane są wszystkie testy. Jeśli Twój kod zepsuł moduł kolegi — pipeline "wybucha".
Bezpieczeństwo wpięte w CI, nie jest osobnym etapem: SonarQube (code smells), SAST (Snyk, Semgrep), SCA (skan zależności pod kątem CVE), secret scanning (gitleaks), DAST na stagingu.
W dużych firmach pipeline'y są ustandaryzowane przez Platform Engineering (Backstage, "golden path") — developer nie konfiguruje wszystkiego od zera.

7. QA, Wdrożenie i Observability (Release)

Kod ląduje na stagingu. Testerzy (QA Engineers) próbują go zepsuć: produkt 0 zł, 50 promocji naraz, edge case'y.
Wdrożenie na produkcję jest progresywne: canary release (1% → 10% → 100%), A/B testy, feature flagi pozwalają wyłączyć funkcję bez deploymentu.
Kontenery (Docker/Kubernetes) to standard.
Po wdrożeniu kod nie znika z radaru — OpenTelemetry (traces, metrics, logs), dashboardy w Grafanie/Datadogu, alerty w PagerDuty, SLO/SLI. W dojrzałych firmach developer dyżuruje na on-call dla swojego serwisu ("you build it, you run it").
Przy mikroserwisach dochodzi contract testing (Pact) — gwarancja, że Twoje API nie psuje konsumentów z innych zespołów.


Nazwa	Opis

Klasy
	
Czy klasy są poprawnie zaimplementowane (enkapsulacja, struktura, mutowalność)?

Wyszukiwanie produktów	Wyszukiwanie produktów oraz n produktów (najdroższy, najtańszy).
Opisanie zaimplementowanego algorytmu.
Sortowanie	Implementacja. Zgodność z specyfikacją z zadania.
Modyfikowalne sortowanie	Możliwość zmiany sposobu sortowania, otwarcie na modyfikacje, DIP (zgodność z SOLID).
Obsługa sytuacji brzegowych	Pokazanie, że program obsługuje sytuacje brzegowe.
Wykorzystanie wzorca projektowego	Wyjaśnienie oraz zaimplementowanie jednego ze wzorców projektowych 
(w kontekście promocji, zgodność z SOLID).
Diagram klas UML	Przedstawienie diagramu klas UML.
Testy jednostkowe	Przedstawienie odpowiednich testów jednostkowych.
Promocje	Implementacja promocji.
Poprawność wykonania programu	Czy program działa poprawnie.