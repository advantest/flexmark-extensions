<!-- see https://shields.io/badges -->

![GitHub License](https://img.shields.io/github/license/advantest/flexmark-extensions)
[![Java CI with Maven/Tycho](https://github.com/advantest/flexmark-extensions/actions/workflows/ci.yml/badge.svg?branch=develop)](https://github.com/advantest/flexmark-extensions/actions/workflows/ci.yml)

# Extensions for flexmark-java (flexmark-extensions)

flexmark-extensions are additional parser and renderer modules for [flexmark-java](https://github.com/advantest/flexmark-java).
These include support for parsing PlantUML code and rendering PlantUML diagrams as well as some Markdown extensions for FluentMark Advantest Edition.

Extensions
* flexmark-ext-plantuml
	* Extension with support for parsing PlantUML code blocks (fenced and non-fenced code blocks) in Markdown code
	  and for rendering them as diagrams in SVG vector graphics format (for usage in HTML output).
	  Does also support special Markdown images pointing to PlantUML diagram files (*.puml), e.g. `![diagram](path/to/diagram.puml)`.
* flexmark-ext-math
	* Extension with support for LaTeX-like math formulas in Markdown code, using LaTeX formula syntax in-line (between `$`) or in display mode (between `$$`).
	  Formulas are not rendered, but prepared for rendering with JavaScript libraries MathJax.
	  In-line formulas are surrounded by `\(` and `\)` while rendering, display mode formulas are surrounded by `\[` and `\]`.
* flexmark-ext-figures
	* Extension for embedding Markdown images in HTML figure tags. Does also add a figure caption if there is an image label.
* flexmark-ext-jira-ticket-links
	* Extension for rendering Jira ticket numbers as links to that Jira tickets.
	  Allows for customizing the Jira URL and for customizing the Jira project key constraints using a regular expression.
