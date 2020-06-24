# Package *Source*
This package exposes some contracts that should be shared between the upper layer, and the sub-modules.

Each contract is suffixed with *-Source*, and may contain 1+ interface. These interfaces may be named as followed :
- *-DataStore* : when describing a data store that is exposed in the repository layer (the upper layer of the data module); each implementation is stored in this current package
- *-Cache* : when describing a repository about storing and reading data from the local database; the implementations shall be located at the root of the *cache* module
- *-Remote* : when describing a repository about storing and reading data from an external database (for instance, through webservices); the implementations shall be located at the root of the *net* module.