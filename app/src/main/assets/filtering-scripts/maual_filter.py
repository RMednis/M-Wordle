# Some words are extremely obscure
# We want to make sure they don't get picked for the main Wordle word...
# They can still be picked after though!
import json

def WordPick(word, wordnum):
    print(f"--- VĀRDS ---")
    print(word)
    print(f"--- {wordnum} ---")
    option = input("Y/N:")

    if option == "y":
        return True
    else:
        return False


def ManualSort(wordlist):
    new_list = list()
    wordnum = 0
    with open(f"manuallist-unfinished.json", "w") as handle:
        for word in wordlist:
            if WordPick(word, wordnum):
                wordnum += 1
                new_list.append(word)
            handle.write(json.dumps(new_list, indent=True, ensure_ascii=False))
        handle.close()

    return new_list
