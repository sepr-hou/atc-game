# ATC Game

[![Build Status](https://travis-ci.org/sepr-hou/atc-game.png?branch=master)](https://travis-ci.org/sepr-hou/atc-game)

## Setup
### Git
If you want to commit anything to this repository, you need to use Git. The easiest way to get started is to download [GitHub for Windows](http://windows.github.com/) or [GitHub for Mac](http://mac.github.com/). If you're using Linux or you want to use the terminal, there's lots of documentation about Git on the [Git website](http://git-scm.com/documentation).

I addition, Eclipse does include a Git plugin - although you's probably have to install it manually (see below).

### Maven
This project uses Maven as its build system. If you have a copy of maven installed, you can just run

    mvn package

and Maven will download and build everything for you - producing a nice JAR at the end.
For most IDEs, you should be able to import this project as an existing Maven project.

### Eclipse on CS Machines
If you downloaded Eclipse from the internet, then it should come with the Maven plugin (m2e). Unfortunately the CS machines do not have Maven installed.

The plugin can be installed by going to Help -> Install New Software... In the "Worth with" box type http://download.eclipse.org/releases/juno/ and press enter. The m2e plugin can be found under "General Purpose Tools" -> "m2e - Maven Integration for Eclipse".

Once the plugin is installed, simply import the project into Eclipse and it should build. The first time you do this, it may take some time since all the dependencies must be downloaded from the internet.

## Bugs and Questions
If you find any bugs or have any questions about the project,, please [create a new issue](https://github.com/sepr-hou/atc-game/issues) and someone from the team will respond.
