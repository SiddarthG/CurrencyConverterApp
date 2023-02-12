# CurrencyConverterApp
CurrencyConverter App implemented using MVVM, Hilt, Coroutines + Flow, Room db offline first, Unit Test using Mockk

The app uses openExchange - openexchangerates.org APIs to get the list of available currencies and latest values of all currencies for 1USD as base.

The app caches all the data from API in Room Db and uses DB as single source of truth.

Repository layer serves data to UI always from DB.

Network call is made after checking if the previous call was made within 30 minutes. and only allowed once in 30 minutes. latest values are inserted in DB.

Flow is used to return data from DB. repository observes for any insertions and emits transformed data to Viewmodel

LiveData is used in view layer with State.

Network layer has Network Repository interface and impl, interface is injected to Use case repository.

Network repository is injected with Network Service(consists retrofit calls)

Hilt is used for Dependency Injection

Mockk is used for Mocking and for unit tests.

Interface and concrete class are used for sharedPrefernce Helper, Network Repo, and use case Repo. interface is injected everywhere to maintain scalability and unit testing.


