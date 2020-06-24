# Data Layer 
This layer contains *Repository Implementations* and *1 or multiple Data Sources*. Repositories are responsible to coordinate data from the different Data Sources. 

**Data Layer depends on Domain Layer.**

The Data layer implements the [Repository Pattern](https://martinfowler.com/eaaCatalog/repository.html).

## data/
implementations of repositories from business/repository

## data/common
package for utilities

## data/cache
gives access to local data, which can be stored in a database, in folders (resources, assets, cache, ...) or in preferences

## data/net
connectors to webservices

## data/source
package for classes that merge data from cache and net packages.