import csv
import json
import os
import time
import datetime
import urllib.request

import maual_filter
import tezurs_web_data

# Automatically pre sort a word list from Tēzurs
# The criteria are the following:
# 1. Must be 5 letters long
# 2. Must be a NOUN
# 3. Must be in the "Nominatīvs" declaration
# 4. Must be in a singular form

# The first and second steps are done here
# The last 2 are done in the tezurs_web_data.py file


WORDLIST_URL = "https://github.com/LUMII-AILab/Tezaurs/raw/master/wordlists/entries.txt"


def download_wordlist(url, filename):
    print(f"- Downloading new wordlist file from: \n {url} \nSaving to: {filename}")
    with urllib.request.urlopen(url) as f:
        wordlist = f.read().decode('utf-8')

        with open(f"./{filename}", "w") as handle:
            handle.write(wordlist)
            handle.close()


def filter_duplicates(wordlist):
    print("Filtering duplicates!")
    new_wordlist = list()
    for word in wordlist:
        if word not in new_wordlist:
            new_wordlist.append(word)
    return new_wordlist


def filter_wordlenght(wordlist, max_word_length):
    print(f"- Filtering to only {max_word_length} long words!")
    new_wordlist = list()
    for word in wordlist:
        if len(word[0]) == max_word_length:
            new_wordlist.append(word)

    return new_wordlist


def filter_nouns(wordlist):
    print(f"- Filtering to only NOUNS!")
    new_wordlist = list()
    for word in wordlist:
        if word[2] == "NOUN":
            new_wordlist.append(word)

    return new_wordlist


def filter_ip_words(wordlist):
    print("- Removing some people names, etc :D")
    new_wordlist = list()
    for word in wordlist:
        if not word[0][0].isupper():
            new_wordlist.append(word)
    return new_wordlist


def add_preamble(jsondata):
    json_formatted = {
        "version": VERSION,
        "compiled-by": "Reinis Gunārs Mednis",
        "about": "This dictionary of words was taken from Tēzurs - The open lexical database for latvian by "
                 "LUMII-AILab.",
        "info": "They are Singular Verbs in the Nominative form",
        "author": "https://github.com/LUMII-AILab/Tezaurs",
        "license": "CC BY-SA 4.0",
        "total-words": len(jsondata),
        "words": jsondata
    }

    return json_formatted


# Downloading the wordlist

filename = "wordlist_unprocessed.csv"

start_time = datetime.datetime.now()

VERSION = start_time.isoformat(timespec='minutes', sep='-')

if not os.path.exists(filename):
    download_wordlist(WORDLIST_URL, filename)

# Convert the data into an easily readable format (list of lists)

word_list = list(csv.reader(open(filename, 'r'), delimiter='\t'))
print(f"Original Wordlist length: {len(word_list)}")

word_list = filter_wordlenght(word_list, 5)
print(f"Wordlist length: {len(word_list)}")

word_list = filter_nouns(word_list)
print(f"Wordlist length: {len(word_list)}")

word_list = filter_ip_words(word_list)
print(f"Wordlist length: {len(word_list)}")

print("--- LOCAL PROCESSING DONE ---")
print(f"Final Wordlist length: {len(word_list)}")

if not os.path.exists("finished_list.json"):
    word_list = tezurs_web_data.filter_with_api(word_list)

    with open(f"./finished_list.json", "w") as handle:
        handle.write(json.dumps(word_list, indent=True, ensure_ascii=False))
        handle.close()

print("Adding the preamble and licensing information to the output json!")
with open(f"./finished_list.json", "r") as handle:
    jsondata = json.load(handle)
    jsondata = filter_duplicates(jsondata)

    with open(f"fulllist-{VERSION}.json", "w") as handle2:
        autodata = add_preamble(jsondata)
        handle2.write(json.dumps(autodata, indent=True, ensure_ascii=False))
        handle2.close()


    with open(f"manuallist-{VERSION}.json", "w") as handle2:
        if not os.path.exists("./manuallist.json"):
            manualdata = maual_filter.ManualSort(jsondata)
        else:
            manualdata = json.load(open("manuallist.json", "r"))

        manualdata = add_preamble(manualdata)
        handle2.write(json.dumps(manualdata, indent=True, ensure_ascii=False))
        handle2.close()

    handle.close()

print("--- WORD LISTING FINISHED ---")
print(f"Total words: {len(word_list)}")
print(f"Time taken: {datetime.datetime.now() - start_time}")
