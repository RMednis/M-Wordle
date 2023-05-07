# Dictionary
The word ditionary was gathered by scraping through the following word list:
```
https://raw.githubusercontent.com/LUMII-AILab/Tezaurs/master/wordlists/entries.txt
```

The word list was then filtered to remove duplicates and to only include words that are exactly 5
letters long, as well as to make sure all words are in the Nominative form, and they are singular.

All of this was done with a simple python script, which can be found in the scripts folder.

This was then dumped into a Giant JSON array and is used in the app to check if a word is valid,
as well as to generate the random word to guess.

The 'word-dictionary.json' file contains all the scraped words, filtered

The 'word-choices.json' file contains a manually sorted subset (about 50% of the total list) that
the game uses to generate the random word to guess. This makes the guessing a lot more fun, as it
makes the words more common and easier to guess.

## About

This word list comes from the Latvian University's AI Lab's Tezaurs project.
``https://github.com/LUMII-AILab/Tezaurs``
``https://tezaurs.lv/``

It was partially filtered using calls to the Tēzurs API, which is available at:
``https://api.tezaurs.lv/``

It's licenesed under the Creative Commons Attribution-ShareAlike 4.0 International License.
``https://creativecommons.org/licenses/by-sa/4.0/``
