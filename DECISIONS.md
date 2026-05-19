# Decyzje projektowe

## 1. Wzorzec promocji — Strategia

Każda promocja to osobna klasa implementująca interfejs `Promotion`:

```java
public interface Promotion {
    boolean apply(Cart cart);
}
```

**Dlaczego Strategia, a nie Command:**  
Wzorzec Command miałby sens, gdybyśmy potrzebowali funkcji cofania/powtarzania (undo/redo), np. usuwania wcześniej zastosowanej zniżki. Zadanie nie wymaga cofania pojedynczych promocji — jedynie dodawania lub usuwania ich z aktywnej listy przed zastosowaniem. Strategia jest prostsza: każda klasa enkapsuluje jedną regułę cenową, są wymienne w czasie wykonywania, a dodanie nowej promocji sprowadza się do dodania nowej klasy bez modyfikowania istniejących (Zasada Otwarte/Zamknięte).

**Rozszerzalność:** Nową promocję (np. "kup produkt X, otrzymaj Y gratis", "zniżka według poziomu lojalności") implementuje się jako nową klasę `Promotion` i rejestruje przez `cart.addPromotion(...)`. Nie trzeba zmieniać istniejącego kodu.

---

## 2. Niepodlegające zmianie `Product` (immutability)

`Product` jest niemutowalny. Zastosowanie zniżki nie mutuje obiektu — zwracany jest nowy obiekt:

```java
public Product withDiscountPrice(double newDiscountPrice) { ... }
```

**Dlaczego:** Mutowalne, współdzielone obiekty są częstym źródłem błędów, szczególnie gdy promocje przekazują produkty między metodami. Niemutowalność sprawia, że efekt każdej promocji jest jawny i możliwy do śledzenia. Koszt (jedna dodatkowa alokacja obiektu na zdyskontowany produkt) jest w kontekście koszyka zakupów pomijalny.

---

## 3. Dwa pola ceny: `price` i `discountPrice`

`price` zawsze zawiera cenę katalogową. `discountPrice` początkowo jest równy `price` i jest modyfikowane przez promocje.

**Dlaczego dwa pola zamiast jednego:**  
- Promocje zależne od progu (np. "zamówienie > 300 PLN → 5% zniżki") muszą sprawdzać wartość oryginalną zamówienia, a nie już zdyskontowaną. `calculateOriginalTotal()` używa `price` do tego sprawdzenia.  
- `calculateTotal()` używa `discountPrice` — to kwota, którą klient faktycznie zapłaci.  
- Posiadanie obu pól pozwala zawsze pokazać klientowi, ile zaoszczędził.

---

## 4. Nakładanie się promocji — zniżki aplikowane na `discountPrice`

`PercentageDiscountPromotion` i `ValueBasedDiscountPromotion` obliczają zniżkę w oparciu o `p.getDiscountPrice()`, a nie `p.getPrice()`. Dzięki temu promocje się prawidłowo nakładają: jeśli promocja 2+1 ustawiła `discountPrice = 0`, kolejna globalna zniżka 10% nadal pozostawi wartość 0.

`CouponPromotion` działa względem `p.getPrice()`, ponieważ kupon jednorazowy definiowany jest jako "X% z katalogowej ceny tego przedmiotu", niezależnie od innych zniżek.

---

## 5. Refaktoryzacja: Tablica → Lista (ćwiczenie Open/Closed)

Wewnętrzne przechowywanie było początkowo zaimplementowane jako `Product[]` z ręcznym powiększaniem (na wzór `ArrayList`). Następnie zrefaktoryzowano to do `List<Product>`.

**Obserwacje z refaktoryzacji:**  
- Publiczne API (`getProducts()`, `findNCheapest()`, itd.) zmieniło typy zwracane z `Product[]` na `List<Product>`. Wszyscy konsumenci (klasy promocji, testy) musieli zostać zaktualizowani — przypomnienie, że zmiany API publicznego nie są "darmowe".  
- Wewnętrzna metoda `grow()` i pole `size` zniknęły; `ArrayList` zarządza powiększaniem automatycznie.  
- Interfejs `Promotion` pozostał bez zmian, co pokazuje, że programowanie wobec interfejsów (a nie implementacji) izoluje wpływ zmian strukturalnych.

---

## 6. Domyślne porządkowanie utrzymywane przez `addProduct`

Koszyk utrzymuje elementy posortowane na bieżąco (malejąco po cenie, potem rosnąco po nazwie). `addProduct` ponownie sortuje po każdej wstawce używając aktywnego `Comparator`. `sort(Comparator)` zmienia aktywny comparator i natychmiast ponownie sortuje.

**Dlaczego sortować przy wstawianiu zamiast przy odczycie:**  
- Sortowanie w `getProducts()` zmusiłoby metodę `replaceProduct` (opartą na indeksach) do działania na świeżo posortowanym snapshotcie, który może nie zgadzać się z wewnętrznym porządkiem. Sortowanie na miejscu utrzymuje spójność indeksów między `getProducts()` a `replaceProduct`.

---

## 7. Sortowanie — Odwrócenie zależności przez `Comparator`

`Cart.sort(Comparator<Product>)` przyjmuje dowolny comparator, spełniając DIP: koszyk zależy od abstrakcji `Comparator`, a nie od konkretnej strategii sortowania. `Product` udostępnia dwie metody fabryczne (`byPrice()`, `byName()`), które można łączyć z kombinatorami `Comparator` (`thenComparing`, `reversed`).
