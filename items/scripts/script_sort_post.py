import json
import random
from datetime import datetime, timedelta

def sort_posts():
    """
    Sorts posts by 'post_id', generates increasing timestamps, assigns random 'user_id's, and updates the JSON file.
    """
    # Open and load the JSON file
    with open('posts.json', 'r') as file:
        data = json.load(file)

    # Sort the posts by 'post_id'
    data.sort(key=lambda x: x['post_id'])

    # Generate random but increasing timestamps
    start_time = datetime.now() - timedelta(days=365)  # Start from one year ago
    end_time = datetime.now()

    # Generate a random timestamp for each post, ensuring the timestamps are increasing
    timestamps = sorted([start_time + (end_time - start_time) * random.random() for _ in data])

    # Generate random hours and minutes for each timestamp
    randomized_timestamps = []
    for timestamp in timestamps:
        hour = random.randint(0, 23)
        minute = random.randint(0, 59)
        second = random.randint(0, 59)
        randomized_timestamp = timestamp.replace(hour=hour, minute=minute, second=second)
        randomized_timestamps.append(randomized_timestamp)

    # Randomly assign 'user_id', assuming the range of 'user_id' is from 1 to 52
    user_ids = [random.randint(1, 52) for _ in data]

    # Update the data
    for i, item in enumerate(data):
        item['timestamp'] = timestamps[i].isoformat()
        item['user_id'] = user_ids[i]

    # Convert back to JSON string, if needed to save to a file or for other purposes
    # Save post data to JSON file
    with open('posts.json', 'w') as f:
        json.dump(data, f, indent=4)

    print("Data sorting and updating completed, saved to posts.json")

sort_posts()