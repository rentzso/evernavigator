a twitter like api service with the following basic functionality:

1. a call to read the tweets for a given user (include self-tweets and people being followed by user)
    an extra “search=” argument can be used to further filter tweets based on keyword
2. a call to get the list of people a user is following as well as the followers of the user
3. a call to start following a user
4. a call to unfollow a user

All calls are guarded with an API token in the query string that will serve to authenticate the calls (this can be a 16-character alphanumeric sequence); if the wrong API token is given, a 401 error is thrown (unauthorized). The output is in json. 
A test script can be found in src/test folder.
