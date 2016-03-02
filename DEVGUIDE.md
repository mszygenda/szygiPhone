szygiPhone - Dev Wiki
--------

* [Message Format](#message-format)
* Command list
 * [SpotifyService](#spotifyservice)
 * [SayService](#sayservice-aka-special-feature)
 * [FacebookService](#facebookservice)
* [Show me the code](#show-me-the-code)
 * [SmsServer - Starting point](#smsserver-starting-point)
 * [ServerActor - Request Router](#serveractor-request-router)
 * [Services](#services)
 * [Request](#request)
 * [Command objects](#command-objects)
 * [ServiceContext](#servicecontext)
 * [tools - tons of scripts](#tools-tons-of-scripts)
* [Can I use it?](#can-i-use-it)

### Message Format
Each message is in the following format

    SERVICE_ID.COMMAND.[ARGUMENT0].[ARGUMENT1].[ARGUMENTN],[CONTENT]

What is service id? It's the identifier of functionality you want to act on (`spotify`, `fb`, `say`). 

Once the server finds service able to process given message it passes the Command Identifier and Arguments to it. `COMMAND` is the identifier of an action you want to perform within the Service (eg. `post` = "Create new facebook post").

Responsibility of the service instance is to properly map arguments and execute right command (`GetPosts`, `GetNotifications`, `PlaySong`)

Each command is given an option to send a response. `GetPosts` for example retrieves latest posts from a wall and writes all of them in the response (which is later transformed into series of SMS messages sent back by the server).

Here are sample messages you can send to the server

    # Play some song
    spotify.play,Breaking the law

    # Add a comment to object with local id = 1
    fb.comment.1,Taki tam komentarz do posta

    # Get 10 latest post from a wall
    fb.wall.0.10,

SpotifyService
-----------
### Commands

####**spotify.play,SONG_NAME**
Let you stream any song over phone call :-)

**Example request**

    spotify.play,Hold the line

**Example response**

https://vimeo.com/120244422

SayService AKA "Special feature"
-------------
### Commands
#### **say.to.PHONE_NUMBER.[DELAY].[REPETITIONS].[VOICE],MESSAGE**
Calls given number and reads a text message processed by IVONA voice synthesiser (https://www.ivona.com/pl/). 

PHONE_NUMBER can be either phone number or "alias" from predefined 'AddressBook'

**Example requests**

    say.to.Andrzej,Co tam jak tam?

    say.to.999888777.0.1.pl_jan,Wygrał Pan toster

### Implementation details
It's very similar to spotify except that it doesn't download any songs :-) Instead it calls script tools/sayTo which uses Ivona API to generate audio from given phrase. Later on this file is streamed over phone call.

FacebookService
------------
It's the simplest and most boring functionalities of it.

It lets you do usual FB stuff like: browsing wall, posting messages, commenting, sending likes, check notifications and even send messages to people.

It's the only service which is context based. Basically in order make functionalities like: "commenting specific post" or "sending likes" we have to keep track of "objects" we browse (groups, posts, comments etc.). So each time the service the responds with new content it assigns it unique REFERENCE_ID which is just a number you can use later on to act on that content (comment, give like etc.)

### Commands

#### **fb.post[.GROUP_REF],MESSAGE**

This command lets you post a message on a wall or within a group.

**Examples:**

    fb.post,Ja nie napisze posta na fb z telefonu? ;-)

**Example response:**

    Created P1. Ja nie napisze posta na fb z telefonu? ;-)

----------


#### **fb.groups,**
Lists all facebook groups you belong to

Where P1 is the reference you can later use to act on it (like posting comments etc.)

**Example response:**

    G0.KS Paraolimpiada
    G1.Grupa Fajnych Ludzi


----------


#### **fb.notifications.0|1,**
Turns off/on sending FB notifications to the phone

**Example Request:**

    fb.notifications.1,

**Example Response**

    Janusz likes your comment;
    Don't forget you have Blah event tomorrow;


----------


#### **fb.messages,**
Gets your latest inbox messages

**Example response:**
 
    John 12:40 : How's it going?
    Janusz 15:20 : Ale umowa.


----------


#### **fb.like.CONTENT_REF,**
Lets you like things stored within **context**. Everything within context gets unique reference id (starting from 0) as you use the app.

**Example Request:**

    fb.like,P1

----------


#### **fb.posts.[GROUP_REF].[OFFSET].[AMOUNT],**
#### **fb.wall.[OFFSET].[AMOUNT],**
Get latest posts from selected group (use: fb.groups to find out GROUP_REF) or a wall

**Example request**

    fb.posts.1,
    fb.wall.0.10,
    
**Example response:**

    P1. Jules: I double dare you.
    P2. Hoff9000: Did anyone ever tell you not to hassle the HOFF-9000?


----------


#### **fb.comments.POST_REF.[OFFSET].[AMOUNT],**
Get comments for given post (to find out POST_REF use fb.wall, fb.posts)

**Example request**

    # 10 latest comments for a post with id = 1
    fb.comments.1.0.10,

    # All comments on a post with id = 1
    fb.comments.1,
    
----------


#### **fb.comment.POST_REF,MESSAGE**
Add a comment to post (to find out POST_REF use fb.wall, fb.posts)

**Example request**

    fb.comments.1,Soo lame!


Show me the code
-------

Go ahead and clone it, share it, fork it.

Before you do that, let me guide you through the structure

### SmsServer - Starting point
`org.szygi.phoneserver.SmsServer` - It's Akka actor which is effectively starting point for application

It's responsibility is to:

- Check for SMS messages
- Send SMS messages

It passes parsed requests messages so ServerActor

### ServerActor - Request Router
`org.szygi.phoneserver.ServerActor`

It's responsibility is to route the requests to the appropriate services.

### Services
`org.szygi.phoneserver.services.facebook.FacebookService `

`org.szygi.phoneserver.services.spotify.SpotifyService`

`org.szygi.phoneserver.services.say.SayService`

All of these transforms Requests into executable Command objects

### Request

`org.szygi.phoneserver.services.Request`

Request is an abstract interface which lets you access properties like:

    Source phone number
    Service Id
    Action Name
    Parameters
    Content

It also let you create response object.

If it reminds you HTTP Stack implementation, yeah. You're right. That's the inspiration.

### Command objects
Command objects implement specific actions (posting on Facebook, playing song etc.)

Just a few of them:

`org.szygi.phoneserver.services.facebook.commands.CommentPost`

`org.szygi.phoneserver.services.facebook.commands.GetPosts`

`org.szygi.phoneserver.services.spotify.PlaySongCommand`

Let's have a look at one of them

    case class DeleteObject(objectRef: Int) extends FacebookCommand {
      def execute(ctx: FacebookServiceContext) {
        val obj = ctx.watchedObjects(objectRef)

        ctx.api.removeObject(obj.id, obj.objectType)
      }
    }

It's used to remove facebook post or a comment. It takes only one parameter which is reference id of the content you want to remove. The actual Facebook Identifier is stored within "Service Context" which is a storage for content you browse while you use the application. This is done so that you don't have to use facebook ids which are at least 10 characters long! ;-)

### ServiceContext

`org.szygi.phoneserver.services.ServiceContext`

`org.szygi.phoneserver.services.facebook.FacebookServiceContext`

It's the place to persist data between different requests. You can think of it as HTTP Session.

The idea came as I was implementing `FacebookService`. I needed a way to assign unique and simple identifiers for the content we present to the user. So that the user can later on reference it (add a comment on a post, remove it or give a like)

### tools - tons of scripts

This the folder where all useful scripts sits in 

#### `sendSms`

Sends SMS message using scmxx (Siemens CX65 tool)

------

#### `sendSmsSamsung.sh`

Sends SMS using Android Phone (you'll have to have app installed).

------

#### `fetchSms`

Reads unread messages from Siemens phone

------

#### `fetchSmsSamsung.sh`

Same as above, but for Android phones

------

#### `call`

Initiates skype call

------

#### `ivonaSay`

Reads given text using IVONA

------

#### `hangUp`

Ends skype call

------

#### `muteApp`

Mutes application by its name

------

#### `playMusic.sh`

Plays music and streams it to given number, Spotify

------

#### `prepareSamsung.sh`

Prepares Android SMS Gateway

------

#### `sayIt`

Says given phrase (IVONA) to given phone number

------

#### `sayTo`

Wrapper for the above

------

#### `setDefaultInput`

Set default microphone (to fake one)

------

#### `setVolume`

sets system volume to given value

------

#### `setVolumeDelay`

Same, but with a delay

------

#### `stopSounds`

Stops all music, ivona 

------

#### `switchEqualiser`

Switches equaliser preset

------

#### `unlockPhone/waitForRelease`

Lock mechanism for Siemens (so that we don't send SMS/read at the same time)

Can I use it?
--------------
Potentially. Yes.

But it may not be that easy, as it would require some basic dev skills since there are few things you have to set up manually.

#### Basic requirements

 1. Old mobile phone as *szygiPhone*
 1. Mobile phone connected to the server (to receive/send SMS)
 2. Possibly different implementation of `tools/fetchSms.sh`,  `tools/sendSms` (unless you have old Siemens phone plugged in eg. CX65)
 3. Basic *nix tools: bash etc.
 4. Scala 2.11, sbt
 5. Dev skills
 6. Spare time ;-)

#### Facebook requirements

1. Facebook dev account
2. Update `APP_ID`, `SECRET_TOKEN` in `FacebookApi` object.

**src/main/scala/org/szygi/phoneserver/services/facebook/api/FacebookApi.scala**

    class FacebookApi {
        val facebook = new FacebookFactory().getInstance();
        facebook.setOAuthAppId("FACEBOOK_APP_ID", "FACEBOOK_APP_SECRET")
        facebook.setOAuthAccessToken(new AccessToken("FACEBOOK_AUTH_TOKEN"))
        
        ...
        
    }
      
#### Spotify requirements

1. Skype
2. PulseAudio
3. Audio output redirected to fake microphone (you'll find some guidance in one of txt files within the project)

####  Say To requirements

1. Same requirements as Spotify
2. Ivona API account
3. Update `IVONA_USER_EMAIL`, `IVONA_USER_TOKEN` in python scripts


     **tools/python/create.py**
     
         c = Client("IVONA_SERVICE_USER_EMAIL", "IVONA_SERVICE_USER_SECRET_TOKEN")


