# Messeption: release 3

## Release 3 gives Messeption these new features:
- Ability to create an account
    - A user can now create an account, and log in to the application with this account
- Ability to post and comment anonymously
    - A user can now post and comment anonymously by checking a checkbox
- Ability to delete own posts and comments
    - A user can now delete posts and comments connected to their account by pressing a delete button related to a given post or comment
- Ability to sort posts
    - A user can now sort posts by spesific criteria, such as:
        - Descending post date
        - author
        - title
        - Descending text length
        - Descending number of comments



## Integrationtests
- TODO

## Restserver
- TODO

<br/>

## Screenshots of the finished application
<div align="left">
    <img src="../images/release3/LoginPage.png" alt="Login page" width=80%/>
</div>
<div align="left">
    <img src="../images/release3/FrontPage_r3.png" alt="Front page" width=80%/>
</div>

<div align="left">
    <img src="../images/release3/CreatePostPage_r3.png" alt="CreatePostPage" width=80%/>
</div>
<div align="left">
    <img src="../images/release3/PostPage_r3.png" alt="PostPage" width=80%/>
</div>

<br/>

# User stories


## Create user (User story 4)
An individual wishes to be able to create an account to keep track of their posts, comments and likes

### Important details for reading
- Ability to see own posts, comments and likes
    - Indicated by "author" label 
    - For anonymous posts and comments -> (more info in user story 6 (*) )

### Important details for interaction
- Ability to create posts connected to the logged-in account
- Ability to create comments connected to the logged-in account
- Ability to change likes and dislikes on posts and comments for the logged-in account

<br/>

## Anonymous posts and comments (User story 5)
An individual wishes to be able to express their opinion on a topic without showing their name or information

### Important details for reading
- Ability for a user to see which posts and comments they have posted anonymously and not anonymously 
    - (more info in user story 6 (*) )

### Important details for interaction
- Ability to create posts anonymously
- Ability to comment on posts anonymously

<br/>

## Delete posts and comments (User story 6)
An individual wishes to be able to delete posts and comments they have created because they changed their mind

### Important details for reading
- Deleted posts and comments are removed for all users
- **(*) The delete button works as an indicator to which posts and comments are owned by the logged-in user**

### Important details for interaction
- Ability to delete own posts
- Ability to delete own comments

<br/>

## Sort posts (User story 7)
An individual wishes to be able to sort posts based on different criteria

### Important details for reading
- Ability to read posts in a sorted order

### Important details for interaction
- Ability to choose by which criteria to sort the posts
