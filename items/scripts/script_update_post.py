import json
import random

def replace_photo_urls(json_file_path):
    """
    Replaces 'photo_url' in the JSON file with a new URL from Picsum Photos.
    """
    # Read the JSON file
    with open(json_file_path, 'r') as file:
        data = json.load(file)
    
    id = 50
    # Iterate over each item and replace the photo_url
    for item in data:
        width = 640  # Set the width of the image
        height = 480  # Set the height of the image
        new_url = f"https://picsum.photos/id/{id}/{width}/{height}"
        item['photo_url'] = new_url
        id += 1
    
    # Write the modified data back to the original JSON file
    with open(json_file_path, 'w') as file:
        json.dump(data, file, indent=4)

def replace_avater_urls(json_file_path):
    """
    Replaces 'avatar_url' in the JSON file with a new URL from Robohash.
    """
    # Read the JSON file
    with open(json_file_path, 'r') as file:
        data = json.load(file)
    
    # Iterate over each item and replace the avatar_url
    for item in data:
        item['avatar_url'] = f"https://robohash.org/{item['username']}"
    
    # Write the modified data back to the original JSON file
    with open(json_file_path, 'w') as file:
        json.dump(data, file, indent=4)

# Call the functions
#replace_photo_urls('update/posts.json')
replace_avater_urls('update/users.json')