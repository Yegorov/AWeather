[![Build Status](https://travis-ci.org/Yegorov/AWeather.svg)](https://travis-ci.org/Yegorov/AWeather)

## AWeather

This is android application that allows you to share the weather forecast in Gorlovka with your friends via VK or FB.

### Interface

Here are some screenshots of the interface Android app:

![Sample screenshot 1](/wiki/AWeatherMain.png)
![Sample screenshot 2](/wiki/AWeatherVK.png)
![Sample screenshot 3](/wiki/AWeatherFB.png)

Here are some screenshots of the result:

![Sample screenshot 4](/wiki/resultVK.png)
![Sample screenshot 5](/wiki/resultFB.png)

### Usage

First of all, take a look at **src/main/java/com/yegorov/app/ProjectVars.java** and fill it with correct values:

1. `VK_APP_ID` and `VK_WALL_OWNER_ID`
2. `FB_APP_ID`

Import the root folder into your IDE (IntelliJ IDEA), then run project.

More information how to get [`VK_APP_ID`](/wiki/AppGuide/CreateAppVK.md) and [`FB_APP_ID`](/wiki/AppGuide/CreateAppFB.md).

### Provider weather forecast

Weather information is taken from the [this](http://inmart.ua/show_weather) site.

### Changelog
+ Support fragments
+ Added sunrise and sunset times.
+ Added moon phases.
