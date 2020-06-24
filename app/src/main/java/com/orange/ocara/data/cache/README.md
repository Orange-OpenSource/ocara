# Package *cache*
## Description
This module handles the access to local data only : 
- <root> : each class should implement one interface from **data/source/-Source/-Cache**, like *AuditSource.AuditCache*
- db for database : each interface is suffixed with *-Dao*
- file for local storage : each interface is suffixed with *-Storage*
- prefs for *SharedPreferences* : each interface is suffixed with *-Preferences*

## Libraries
It uses the ORM [ActiveAndroid](http://www.activeandroid.com/). This library is actually deprecated (it has not been maintained since 2014), it should be replaced with one long-term library, such as [Room](https://developer.android.com/topic/libraries/architecture/room) or [Realm](https://realm.io/docs/java/latest/). 

Maybe, you can have a look at some benchmarks : 
- [proandroiddev](https://proandroiddev.com/android-databases-performance-crud-a963dd7bb0eb)