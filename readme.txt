imagesync
---------

A simple utility to synchronize photos from PicasaWeb to Facebook.

Run: mvn package -Prun

But first you need a settings.xml file with the following content:

<settings>
    <users accessToken="AN ACCESS TOKEN" userId="UserId used for output">
        <synchronizers feedUrl="https://picasaweb.google.com/data/feed/base/user/someuser/albumid/somealbum?alt=rss&amp;kind=photo&amp;hl=en_US"/>
    </users>
</settings>

You can have multiple users and multiple synchronizers!

See https://developers.facebook.com/docs/authentication/ for how to create an access token!
You can also use http://developers.facebook.com/tools/explorer/

