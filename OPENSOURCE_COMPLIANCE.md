# Opensource Compliance
## Why ?
Ocara aims to be shared with people and organizations. So it has to be published on [GitHub](https://github.com/Orange-OpenSource/ocara).

## How ?
### Create a ticket
Please contact Nicolas Toussaint <nicolas1.toussaint@orange.com>. He is an expert with Opensource compliance and he knows how to proceed.

### Current tickets
ConfOCARA : https://www.forge.orange-labs.fr/plugins/tracker/?aid=647148
OCARA : https://www.forge.orange-labs.fr/plugins/tracker/?aid=647147 

### Previous tickets
OCARA : https://www.forge.orange-labs.fr/plugins/tracker/?aid=224095

### Create delivery
OCARA is an old project that has a deprecated architecture and that uses deprecated libraries. Therefore it is not easy to unify both opensource and proprietary versions of the same application, into a multi-modules multi-flavors project.
For instance, AndroidAnnotations is a painful library that makes refactoring very difficult.

Therefore, this document presents how to produce a package that can be delivered as compliant for opensource.

- create a new feature on Git
- remove flavors dedicated to Orange :
  - app/src/orange
  - app/src/orangewithoutsmtk
  - fix app/build.gradle (especially, remove the content of the variables *OCARA_DOMAIN_NAME* and *OCARA_SERVEUR*)
- remove files that are dedicated to Orange :
  - gradle/ci.gradle
  - gradle/sonar.gradle
  - Jenkinsfile
  - OPENSOURCE_COMPLIANCE.md
- launch the opensource app and check that it is still working

### Update Copyright
Each file shall be headed with a comment about copyright :
```
Software Name: OCARA

SPDX-FileCopyrightText: Copyright (c) 2015-2023 Orange
SPDX-License-Identifier: MPL v2.0

This software is distributed under the Mozilla Public License v. 2.0, 
the text of which is available at http://mozilla.org/MPL/2.0/ or 
see the "license.txt" file for more details.
```

If it's not the case, your IDE may help you to do so.

#### IntelliJ IDEA
- Go to File > Settings ... > Copyright
- Define a new Copyright Profile that contains the licence texte
- Define that profile as the new Default Copyright Profile
- Click right on a folder and choose *Update Copyright...*
- Verify that files have been updated

### Check delivery is compliant
- Upload a .tar.gz of the project to [Fossology](https://fossology.bigdata.intraorange/repo/). The archive can be downloaded from the [Gitlab](https://gitlab.forge.orange-labs.fr/OCARASoftwareSolution/Ocara).
- Then make the results be analyzed
- Make some fixes if necessary. Then check the compliance again.

### Publish delivery on Github
[Home of the project](https://github.com/Orange-OpenSource/ocara)

*please add some detail here*