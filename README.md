<!-- see https://shields.io/badges -->

![GitHub License](https://img.shields.io/github/license/advantest/flexmark-extensions)
[![Java CI with Maven/Tycho][ci-badge]][ci-workflow]

[ci-badge]: https://github.com/advantest/flexmark-extensions/actions/workflows/ci-docker.yml/badge.svg?branch=main
[ci-workflow]: https://github.com/advantest/flexmark-extensions/actions/workflows/ci-docker.yml

# Extensions for flexmark-java (flexmark-extensions)

flexmark-extensions are additional parser and renderer modules for
[flexmark-java](https://github.com/advantest/flexmark-java).
These include support for parsing PlantUML code and rendering PlantUML diagrams as well as some Markdown extensions
for FluentMark Advantest Edition.

Extensions
* flexmark-ext-plantuml
	* Extension with support for parsing PlantUML code blocks (fenced and non-fenced code blocks) in Markdown code
	  and for rendering them as diagrams in SVG vector graphics format (for usage in HTML output).
	  Does also support special Markdown images pointing to PlantUML diagram files (*.puml),
	  e.g. `![diagram](path/to/diagram.puml)`.
* flexmark-ext-math
	* Extension with support for LaTeX-like math formulas in Markdown code, using LaTeX formula syntax in-line
	  (between `$`) or in display mode (between `$$`).
	  Formulas are not rendered, but prepared for rendering with JavaScript libraries MathJax.
	  In-line formulas are surrounded by `\(` and `\)` while rendering,
	  display mode formulas are surrounded by `\[` and `\]`.
* flexmark-ext-figures
	* Extension for embedding Markdown images in HTML figure tags.
	  Does also add a figure caption if there is an image label.
* flexmark-ext-jira-ticket-links
	* Extension for rendering Jira ticket numbers as links to that Jira tickets.
	  Allows for customizing the Jira URL and for customizing the Jira project key constraints using a regular expression.
* flexmark-ext-sourcetracking
	* Extension adding the Markdown source code position (attributes `source-offset` and `source-length`)
	  to the rendered HTML elements.
	  Enables navigation from Markdown source code to the corresponding element in an HTML preview and vice versa.

## PlantUML edition

flexmark-ext-plantuml renders diagrams with PlantUML's EPL edition, `net.sourceforge.plantuml:plantuml-epl`, published
under the Eclipse Public License. PlantUML's GPL edition, `net.sourceforge.plantuml:plantuml`, is not an option: the
GPL is incompatible with the BSD 3-Clause License and would put its terms on everything distributed together with it.
The EPL fits as well because ELK, the Eclipse Layout Kernel that PlantUML can lay out diagrams with, is under the EPL
too. The EPL's copyleft covers PlantUML's own files only, so the extensions keep their licence.

What follows for an application embedding the extension:

* Do not add the GPL edition. To use another PlantUML version, override the version of `plantuml-epl`.
* The PlantUML JAR bundles ELK, EMF, JLaTeXMath and Guava. The extension therefore excludes PlantUML's transitive
  dependencies, and the extensions bring no Guava of their own.
* The EPL edition renders no Ditaa, JCCKit or Sudoku diagrams and writes no WebP images.

## Licence

The extensions are available under the [BSD 3-Clause License](LICENSE) (`BSD-3-Clause`).

## Copyright

Copyright (c) 2022-2026, Advantest Europe GmbH.
All rights reserved.
