# popMoviesStage2
Udacity app stage 2
I used recommendation from this site:
https://richardroseblog.wordpress.com/2016/05/29/hiding-secret-api-keys-from-git/


Before compiling:

1. Create gradle.properties file on project root dir
2. Put this to the file

android.enableJetifier=true
android.useAndroidX=true
org.gradle.jvmargs=-Xmx1536m
API_KEY="PUT_YOUR_API_KEY"

3. https://api.themoviedb.org API_KEY is saved on gradle.properties file. You need to put your API_KEY to this file for successful compilation

## Libraries
- Livecycle(LiveData,ViewModel)
- Room
- Navigation
- Paging
- Retrofit2
- OkHttp3
- Stetho
- DataBinding
- Picasso




