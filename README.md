# JavaSSH

Ocena: 4

### Stopień spełnienia wymagań:

1. Wykorzystanie narzędzia Spring Framework (najlepiej z wykorzystaniem SpringBoot) + Maven + git
	- Spring Boot Security, Sprint Boot Data JPA, Spring Boot Web, Thymeleaf
	- maven
	- git ( + hosting: github)
2. Kod ma być dostępny w zewnętrznym repozytorium razem z dokumentacją
	- Tak
3. min 10 poprawnych (zgodnych z REST) endpoint-ów (min 4 metody GET/POST/PUT/DELETE) Należy znać sens wykorzystywanych metod.
	- Projekt zawiera 4 serwisy/api, każdy 4-5 endpoint-ów. Metoda PUT nie została wykorzystana w projekcie ze względu na brak potrzeby aktualizacji rekordów.
4. Do wszystkich endpointów należy przygotować kolekcję requestów za pomocą programu Postman (wyeksportowana kolekcja ma znaleźć się w repozytorium razem z kodem oraz dokumentacją w oddzielnych katalogach)
	- Tak: pod nazwą: Documentation/SSH Api Documentation.postman_collection.json
5. min 5 tabel w bazie wykorzystywanej przez aplikację
	- 4 bazy MS SQL (każda z 1-2 encjami) + NoSQl w postaci Azure Table Storage oraz baza plikowa - Azure Blob Storage
6. Logika biznesowa wykorzystująca DI oraz IoC
	- Tak
7. Dokumentacja opisująca przeznaczenie oraz przypadki użycia aplikacji w postaci diagramów UML
	- Dokumentacja opisująca przeznaczenie aplikacji (Documentation/Dokumentacja)
	
### Dodatkowo:

1. Wykorzystanie narzędzia swagger.io
2. Architektura rozproszona/mikroserwisowa
	- 4 serwisy restowe
	- Proxy
	- Web Service - front dla użytkownika końcowego
3. Wykorzystanie starterów SpringBoot'a
	- Spring Boot Security, Sprint Boot Data JPA, Spring Boot Web
4. Prosty front-end do konsumpcji endpointów Rest-owych

### Service port:
  
| Service | Port |
| --- | --- |
| Proxy | 5000 |
| Books | 5001 |
| Movies | 5002 |
| Games | 5003 |
| Comments | 5004 |
| Front | 5005 |
