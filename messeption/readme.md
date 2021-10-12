# Description of the app /project

## Messeption
Messeption is an app that takes inspiration from messaging apps and forums. 
These are our inspirational sources: Reddit, Jodel, Twitter.

The purpose of the app is to allow users to communicate with other people via text. The most basic functionality is to post text-posts like you would on a forum. 
Later in the development process we would like to implement the ability to comment on the post and start 'threads'. These 'threads' will be a chain of replies to the original post or to other replies.
Additionally we would like to give users the ability to like/dislike posts.

## images

These images are early prototypes and drawings of how we want the app to look.


#### Front Page

Here you will be able to see a lot of posts.

We want to create a possibility to filter the post.
For instance:
- new posts first
- old posts first
- filter by user
- filter by category

<div align="left">
    <img src="../docs/images/finished/frontPage.png" alt="Front page" width=80%/>
</div>



#### Create Post Page

Here you can create a new post
<div align="left">
    <img src="../docs/images/finished/createPostPage.png" alt="Create post page" width="80%"/>
</div>

#### Post Page

Here you will be able to look at a post in more detail, and review its comments.

You will be able to like/dislike and add comments or replies to other comments.
<div align="left">
    <img src="../docs/images/finished/postPage.png" alt="Post Page" width="80%"/ >
</div>

# Saving userdata
## Document metaphor / Autosaving?

We automaticly save the state of the app if the user changes some of the data. For instance: creates a new post or likes a post.

So the user does not manually save a file or a document. There is no metaphor to the desktop.
## File format

### Gson
We use gson to serialize and deserialize java objects to/from JSON.

JSON saves all the fields of ForumBoard.

This is a bief description of BOARD.JSON:

- The file consist of a Map with one element. (since the only field in ForumBoard is a List of ForumPosts)

- The key is: "posts" and the value is a list that represent the ForumPost objects.

- Each element in the list is a map with 6 key value pairs.
    - "title" and a string
    - "text" and a string
    - "likes" and a integer
    - "dislikes" and a integer
    - "timeStamp" and a string that is written in a format that can be read as time
    - "comments" and a list that represent the PostComment objects

- Each element in the "comments" list is a map with 4 key value pairs.
    - "text" and a string
    - "likes" and a integer
    - "dislikes" and a integer
    - "timeStamp" and a string that is written in a format that can be read as time



# User stories


## write posts (User story 1)
A private person wishes to be able to express his opinion on a certain topic anonymously, and to have that opinion saved.
Aditionally he wishes to have his opinions organized in an easy and readable manner.

### Important details for reading
- Ability to view posts

### Important details for writing
- Ability to create a post
- Save date and time for when posts are made


## Read posts (User story 2)
As a passive user, or someone who does not post a lot, a private person wishes to interact with other people's posts as a way of using the application by agreeing og disagreeing with someone.

### Important details for reading
- Ability to view likes/dislikes on posts

### Important details for interaction
- Ability to Like/dislike posts


## Comment on posts (User story 3)
As an interactive user, a private person wishes to interact with other people's posts by sharing their opinion on that spesific topic.

### Important details for reading
- Ability to view other comments on posts
- Ability to view likes/dislikes on comments

### Important details for interaction
- Ability to comment on posts
- Ability to like/dislike comments
