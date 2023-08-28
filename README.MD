# OCARA [![Build Status](https://travis-ci.org/Orange-OpenSource/ocara.svg?branch=internationalisation)](https://travis-ci.org/Orange-OpenSource/ocara)

OCARA, available for mobile devices, is primarily an application for performing a self-diagnosis of buildings accessibility. You can use expert, but you do not need to be an expert!
Typically, you describe and audit the path from a given starting point to a destination in the building (for instance from the entrance to a specific meeting room).
Each object you encounter is referenced up the path as you go. Objects are, for example, a path, a door, a gate, a sidewalk...
 
Associated with scalable and customizable set of rules, it allows to audit any type of object or service area, according to a multi-disability approach.


## For who ?
 * Indirectly for employees with disabilities
 * For all corporate actors in the field of disability: disability correspondents, site managers, safety managers ...


## Employees  Benefits

For disability stakeholders
   * Being able to independently assess the accessibility level of workplaces for a given collaborator, without particular expertise
   * Quickly and easily produce an audit report
   * Integrating new rules quickly

For employees with disabilities
   * The possibility of a recurring audit easy to perform promotes the quality of life at work
   * Employees may also express their specific needs or even ask for the creation of new rules

## Business Benefits
   * Allows to better integrate accessibility approach into current practices: evaluating the accessibility of workplaces can be done simply
   * Facilitates the opportunity to engage or accomodate a disabled person and solves situations in maintaining the employment of a disabled person
   * Beyond Disability such a tool help to improve movements and safety for all employees 


**Orange** decided to open-source Ocara so that all companies, associations and individuals concerned about accessibility, can use this tool and contribute to its development.

Accessibility is a common concern. It is by pooling and sharing our accessibility resources we will go further together.
[watch the video](http://vod.com.ftgroup/content/encoded/t81HxrJyewTuNLcLgfph.asx)


## Build the application

A prebuilt apk is provided for your convenience (click on "Raw" to save file) [here](https://github.com/Orange-OpenSource/ocara/blob/internationalisation/demo/ocara-opensource-debug.apk)

To build the application, you need to have a proper installation of the [Android SDK](https://developer.android.com/sdk/index.html).
Then the application can be built with Android Studio, or directly on the command line through:

```shell
gradlew clean assembleOpensourceDebug
```

When launching the application, you arrive on the page *Create audit*

## Documentation

How to use Ocara is describe [here](https://github.com/Orange-OpenSource/ocara/blob/internationalisation/documentation/Guide de formation V16.2.pdf)

The description of the json ruleset is [here](https://github.com/Orange-OpenSource/ocara/blob/internationalisation/documentation/Fichier JSON des règles.md)


## License

Copyright (C) 2015 Orange

[Mozilla Public License Version 2.0](https://www.mozilla.org/MPL/2.0)


## Authors
Denis Boisset

Alexis Chemin

Ludovic Lelion

Manuel Sanchez

## Support
<ocara.support@orange.com>
