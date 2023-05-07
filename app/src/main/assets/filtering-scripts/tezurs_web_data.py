import time
import requests
import json

# The rest of the data we will filter using the Tēzurs API
# API: https://api.tezaurs.lv/

# Here we use the API to check the following:
# - Double check if a word is a VERB
# - Check the words form (Singular or Not)
# - Check the words declanation (Nominatīvs)

#  API urls
API_ENDPOINT_ROOT = "api.tezaurs.lv:8182"  # Using the new API
API_ANALYZE="analyze"  # Using the word analysis endpoint

# Let's not spam them :D
DELAY_BETWEEN_REQUESTS = 0.10  # (Seconds)

def filter_with_api(wordlist:list):
    print("--- FURTHER FILTERING USING TEZURS API (WILL BE LONG) ---")
    new_list = list()

    for word in wordlist:

        print(f"- Current Word:{word[0]}")

        # Web request
        response = requests.get(f"http://{API_ENDPOINT_ROOT}/{API_ANALYZE}/{word[0]}")

        if response.status_code == 200:
            # Valid
            data = json.loads(response.content)

            if word_matches_criteria(data):
                # Add it to our list
                print("-- OK")
                new_list.append(word[0])
            else:
                # Ignore it
                print("-- BAD")

        else:
            print("Error:", response.status_code)
            print("--- Unifinished list ---")
            print(new_list)
            with open(f"./unfinished_{word[0]}", "w") as handle:
                handle.write(json.dumps(new_list, ensure_ascii=False))
                handle.close()
            return

        time.sleep(DELAY_BETWEEN_REQUESTS)

    return new_list


def word_matches_criteria(json_data):
    good = False
    for definition in json_data:

        # Break if we don't have the necessary word data in general!
        if "Vārdšķira" not in definition:
            break

        if "Skaitlis" not in definition:
            break

        if "Locījums" not in definition:
            break

        # Check the word data matches our needs!
        if definition['Vārdšķira'] == 'Lietvārds':
            if definition['Skaitlis'] == 'Vienskaitlis':
                if definition['Locījums'] == 'Nominatīvs':
                    good = True

    return good