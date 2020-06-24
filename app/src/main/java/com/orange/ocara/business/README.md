# Domain Layer
## Description
**Domain (with business rules) is the most important Layer.**

Domain is at the center of the onion which means it is the core of our program. This is one of the main reasons why it SHOULD NOT have any dependencies with other layers.

Presentation and Data Layers are less important since they are only implementations that can be easily replaced. The list of posts could be displayed in Android, iOS, Web or even Terminal if your code is properly decoupled. The same happens with a Database or any kind of Data Source, it can be easily switched.

The outer you go on the onion the most likely things are prone to change. One of the most common mistakes is to have your app driven by your data layer/specific data system. Making it hard to replace or bridge with different data sources down the line.

**Domain Layer does NOT depend on Data Layer.**

Having modules with the correct dependency rules means that our Domain doesn’t have any dependency on any other layer. Due to no dependencies to any Android Library the Domain Layer should be a Kotlin Module. This is an extra boundary that will prevent polluting our most valuable layer with framework related classes. It also promotes reusability across platforms in case we switch over the Framework as our Domain Layer is completely agnostic.

## Content
### business/binding
gives some tools to manage errors

### business/interactor
package for the implementations of all the business use-cases. Each class is named in the following way:
```
<Imperative verb><Target model> + "Task" as a suffix ,and implements the interface UseCase

ie : AcceptTermsOfUseTask, LoadRulesetTask.
```

### business/repository
package for interfaces that describe the repositories that give access to the data

### business/service
Not needed anymore. Should be removed as soon as possible

### business/BizConfig
a factory that creates instances of the use cases.
