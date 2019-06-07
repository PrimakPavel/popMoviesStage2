# popMoviesStage2
Udacity app stage 2
I used recommendation from this site:
https://richardroseblog.wordpress.com/2016/05/29/hiding-secret-api-keys-from-git/


Before compiling:

1. Create gradle.properties file on project root dir
2. Put this to the file
# Project-wide Gradle settings.
# IDE (e.g. Android Studio) users:
# Gradle settings configured through the IDE *will override*
# any settings specified in this file.
# For more details on how to configure your build environment visit
# http://www.gradle.org/docs/current/userguide/build_environment.html
# Specifies the JVM arguments used for the daemon process.
# The setting is particularly useful for tweaking memory settings.
android.enableJetifier=true
android.useAndroidX=true
org.gradle.jvmargs=-Xmx1536m
API_KEY="PUT_YOUR_API_KEY"
# When configured, Gradle will run in incubating parallel mode.
# This option should only be used with decoupled projects. More details, visit
# http://www.gradle.org/docs/current/userguide/multi_project_builds.html#sec:decoupled_projects
# org.gradle.parallel=true

3. https://api.themoviedb.org API_KEY is saved on gradle.properties file. You need to put your API_KEY to this file for successful compilation




