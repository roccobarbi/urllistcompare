# urllistcompare

A simple command line tool to compare two lists of URLs tracked with different digital analytics tools (e.g. Webtrekk and Google Analytics) and see if some areas are missing on one of them.

The first official release will be an MVP and will produce three lists:
* URLs from format A missing in format B (with total format A traffic);
* URLs from format B missing in format A (with total format B traffic);
* URLs available in both formats (with total traffic per format).

Future releases will expand on the concept by giving more freedom to the user, who will be able to set up his analysis and receive more detailed reports on the results.

In general, the core of the system should not depend on the console. It should be possible (and it might become part of this project, in the future) to use the same classes with a GUI.