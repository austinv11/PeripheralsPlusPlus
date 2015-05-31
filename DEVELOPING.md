Developing For Peripherals++
============================

**Prerequisites**: Experience with ComputerCraft, Java and the Minecraft Forge Library

## The Very Basics (this *should* apply to any mod)

### 1. Forking the repo

First, make sure you have a Github account and have installed git and/or a Github client.
If you need help learning how to use git and Github, read [these articles](https://help.github.com/categories/bootcamp/)
Then, navigate to the top right of the page, where you will find a [button called 'Fork'](http://puu.sh/hjzjc/86344fcf3c.png)
Click it to fork the repo, then clone your fork to your computer by using [this](http://puu.sh/hjzx4/95c15c59e1.png) on the 
bottom right.

### 2. Setting up the workspace

*Note*: When you see any `gradlew` command, the input will have to change base on your OS.

**Windows**: Ignore this, you're good to just copy and paste commands!

**Mac**: Prefix each command with `sh `, so `gradlew clean` becomes `sh gradlew clean`

**Linux/Other**: Prefix each command with `./`, so `gradlew clean` becomes `./gradlew clean`

Now, just run these following commands (in order!) before doing anything to start developing

* `gradlew clean --refresh-dependencies`
* `gradlew setupDecompWorkspace`
* If you use Intellij: `gradlew idea` or if you use Eclipse: `gradlew eclipse`

Finally, add the follow VM option to your run configuration for Minecraft: `-Dfml.coreMods.load=com.austinv11.collectiveframework.minecraft.asm.CollectiveFrameworkEarlyTransformerPlugin`

### 3. Developing

You should know what to do

### 4. Building

Simply run `gradlew build` and navigate to the build/libs directory relative to the project directory

### 5. Making the pull request

So, you followed everything and you believe that your contribution is good enough to be included? If so, make the Pull Request!
Please refer to your preferred method of using git's documentation if you don't know how.

## Peripherals++ Specific Information

### 1. Coding Conventions

If you wish to have your pull request approved, you **must** follow my conventions.

#### Tabs or spaces?

Tabs, it must be tabs. No, I don't care why you think spaces are better.

#### Brackets

Please use [1tbs](http://en.wikipedia.org/wiki/Indent_style#Variant:_1TBS) with the exception that one line conditional
statements do not need brackets. Yes, I know, your style is better, blah blah blah.

#### Naming

I use standard java naming conventions and try to follow Mojang's naming conventions for classes

### 2. Dependencies

Yes, I know, I am a horrible person for making Peripherals++ require a dependency. Get over it. [Collective Framework](https://github.com/austinv11/CollectiveFramework)
(the dependency) is really cool and contains a lot of helpful things for both mod development and general Java development.
Please have a look at it before you decide that you hate it. Plus, it has a maven repository (which is linked in Peripheral++'s
build.gradle)! So you don't need to worry about dependency management when developing.

### 3. Anything else?

Contact me. [Tweet me](https://twitter.com/amanv111) or message me on [CurseForge](http://minecraft.curseforge.com/members/austinv11)
or [Reddit](https://www.reddit.com/user/austinv11)
