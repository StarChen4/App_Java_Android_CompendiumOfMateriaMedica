import requests
import json
import time

# API settings
API_KEY = 'VX1XjgJVcqrTWf5VDsLGUB98JP8XfP98LvQQFfOZ6Nk'
BASE_URL = 'https://trefle.io/api/v1/species/'
WIKI_URL = 'https://en.wikipedia.org/api/rest_v1/page/summary/'
HEADERS = {'Authorization': f'Bearer {API_KEY}'}

def fetch_plant_description(scientific_name):
    """Fetch description for a plant using its scientific name from Wikipedia API"""
    try:
        # Send a GET request to the Wikipedia API
        response = requests.get(WIKI_URL + scientific_name)
        if response.status_code == 200:
            data = response.json()
            # Return the extract from the response, or a default message if not available
            return data.get('extract', 'No description available.')
        else:
            return 'No description available.'
    except:
        return 'Failed to fetch description.'

def fetch_plants(page):
    """Fetch plant information for a specific page from Trefle API"""
    # Send a GET request to the Trefle API with the page number and API key
    response = requests.get(f"{BASE_URL}?page={page}", headers=HEADERS)
    if response.status_code == 200:
        # Return the JSON response if the request was successful
        return response.json()
    else:
        return None

def save_to_json(data, filename='plants_with_descriptions.json'):
    """Save data to a JSON file"""
    with open(filename, 'w') as file:
        # Dump the data to the file in JSON format
        json.dump(data, file, indent=4)
        file.write("\n")

def main():
    all_plants = []
    batch = []
    page = 1
    total_plants = 1500
    batch_size = 20 

    # Fetch plant data until we reach the desired total
    try:
        while len(all_plants) < total_plants:
            data = fetch_plants(page)
            if data and 'data' in data:
                for plant in data['data']:
                    if len(batch) < batch_size:
                        # Fetch the description for the plant
                        description = fetch_plant_description(plant['scientific_name'])
                        # Filter the plant data
                        filtered_plant = {
                            'id': plant['id'],
                            'common_name': plant['common_name'],
                            'slug': plant['slug'],
                            'scientific_name': plant['scientific_name'],
                            'image_url': plant['image_url'],
                            'genus': plant['genus'],
                            'family': plant['family'],
                            'description': description
                        }
                        batch.append(filtered_plant)
                    else:
                        break
                    if len(all_plants)+ len(batch) >= total_plants:
                        break
                # Add the batch to the list of all plants
                all_plants.extend(batch)
                print(f"Successfully fetched {len(all_plants)} records.")
            else:
                break
            page += 1
            time.sleep(1)  # To prevent rate limit issues
    except Exception as e:
        print("An error occurred:", e)
        time.sleep(5)  # Wait before retrying
    
    # Truncate to the desired total and save to a JSON file
    all_plants = all_plants[:total_plants]
    save_to_json(all_plants)

if __name__ == '__main__':
    main()