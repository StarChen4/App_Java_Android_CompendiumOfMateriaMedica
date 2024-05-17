import json
import random
from datetime import datetime, timedelta
from faker import Faker

fake = Faker()

# Load plant data
with open('plants_with_descriptions.json', 'r') as f:
    plants_data = json.load(f)

def generate_user_data(num_users=50):
    """
    Generates simulated user data and saves it to 'users.json'.
    """
    users = [
        {
            "user_id": i + 1,
            "username": fake.user_name(),
            "email": fake.email(),
            "password": fake.password(),
            "avatar_url": fake.image_url(),
        } for i in range(num_users)
    ]

    # Save user data to JSON file
    with open('users.json', 'w') as f:
        json.dump(users, f, indent=4)

def generate_post_data(users, num_posts_per_user=19):
    """
    Generates simulated post data and saves it to 'posts.json'.
    """
    posts = []
    post_id = 1
    for user in users:
        for _ in range(num_posts_per_user):  # Assume each user posts num_posts_per_user times
            plant_id = random.choice([plant['id'] for plant in plants_data])  # Randomly select a plant ID from the loaded plant data
            posts.append({
                "post_id": post_id,
                "user_id": user["user_id"],
                "plant_id": plant_id,
                "photo_url": fake.image_url(),
                "content": fake.text(),
                "timestamp": (datetime.now() - timedelta(days=random.randint(0, 365))).isoformat()
            })
            post_id += 1

    # Save post data to JSON file
    with open('posts.json', 'w') as f:
        json.dump(posts, f, indent=4)

# Call the functions
users = generate_user_data()
generate_post_data(users)

print("Data generation completed, saved to users.json and posts.json")