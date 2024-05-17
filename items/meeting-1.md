# [G053]

## Team Meeting [1] - Week [7] - [2024-04-20] (18:00-22:00)
**Absent: None**
<br>
**Lead/scribe:Yusi Zhong**

## Agreed Procedure
For Stand up Procedure, as this is our first meeting, we've decided the following:
- Roll Call: Start each meeting by confirming attendees. 
- Progress Updates: Members succinctly report on their progress.
- Discussion: Address agenda items in order, focusing on decision-making.
- Action Review: Conclude by assigning action items.
- Documentation: The scribe captures key points and updates for reference.


## Agenda Items
| Number |                      Item |
|:-------|--------------------------:|
| [1]    | [Escape Room Information] |
| [2]    |           [Project Topic] |
| [3]    |       [Project Framework] |
| [4]    |        [Project Database] |
| [5]    |         [Task Allocation] |

## Meeting Minutes
### [Project Topic Brainstorming]
After considering various options, including job hunting app, fundraising app, income inequality statistics app, carbon emissions calculator and social apps for minority groups, the team decided on an app to support SDG 15 'Life on Land',emphasizing the **identification of plants** to foster environmental engagement and education.<br>

### [Project Framework]
The team agreed upon the app's probably features, which will include:
- **Login**: to personalize user experience and secure user data.<br>
- **Plant Identification**: using photo recognition technology.<br>
- **Fuzzy Search**: for efficient and user-friendly searching within the app.<br>
- **Geolocation Tagging**: for mapping plant discoveries.<br>
- **Social Community Sharing**: for users to share experiences and knowledge. (subscription option).<br>

The skeleton diagram:
![skeleton.png](media/Design/skeleton.png)

### [Project Database]
The database is structured to support the primary functions of the app with the following tables:
- **Users Table**<br>
Stores user account and profile information.<br>

| Field           | Type         | Description                    |
|-----------------|--------------|--------------------------------|
| UserID          | INT          | Primary key                    |
| Username        | VARCHAR(255) | User's chosen username         |
| PasswordHash    | VARCHAR(255) | Hashed password for security   |
| AvatarURL       | VARCHAR(255) | URL to the user's avatar image |

- **Plants Table**<br>
Contains data for plant identification, including information fetched from an API.<br>

| Field           | Type         | Description                   |
|-----------------|--------------|-------------------------------|
| PlantID         | INT          | Primary key                   |
| Name            | VARCHAR(255) | Name of the plant             |
| APIInformation  | JSON         | Data fetched from a plant API |

- **Posts Table**<br>
Holds records of community posts.<br>

| Field       | Type         | Description                              |
|-------------|--------------|------------------------------------------|
| PostID      | INT          | Primary key, auto-increment              |
| UserID      | INT          | Foreign key from Users table             |
| PlantID     | INT          | Foreign key from Plants table (optional) |
| GPSLocation | VARCHAR(255) | Geolocation data of the post             |
| Date        | DATETIME     | Date and time when the post was made     |
| Content     | TEXT         | Body of the post                         |
| ImageURL    | VARCHAR(255) | URL to the user-uploaded image           |

It can be scaled or modified depending on future needs.
![database.png](media/Design/database.png)

### [Project Timeline]
#### Week 8: Checkpoint 1
- **Completed**: Basic features of the app are developed

#### Week 9: Custom Features
- **Development**: Focus on adding custom features to the app.

#### Week 10: Checkpoint 2
- **Integration and Debugging**: All app components are integrated.
- **Report Preparation**: Begin preparing the report.

#### Week 11: Project Submission and Presentation Preparation
- **Thursday Deadline**: Final adjustments and completion.
- **Friday Deadline**: Slides must be uploaded to the specified link (to be announced).

### Week 12: Lecture Group Presentation (Minute-Madness)
- **Presentation**


## Action Items
| Task           |  Assigned To   |    Due Date    |
|:---------------|:--------------:|:--------------:|
| [Login]        | [Tianhao Shan] |  [2024-04-27]  |
| [DataFiles]    |  [Yusi Zhong]  |  [2024-04-27]  |
| [LoadShowData] | [Haochen Gong] |  [2024-04-27]  |
| [DataStream]   |  [Xing Chen]   |  [2024-04-27]  |
| [Search]       |  [Hongjun Xu]  |  [2024-04-27]  |



## Scribe Rotation
The following dictates who will scribe in this and the next meeting.

|      Name      |
|:--------------:|
| [Haochen Gong] |
|  [Xing Chen]   |
| [Hongjun Xu]   |
| [Tianhao Shan] |