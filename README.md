# skynet.SkyNET project template

This is the starter project for skynet.SkyNET, a command-line chatbot. The sections below explain how to set up and run it.

## Setting up in IntelliJ

Prerequisites: JDK 25. Update IntelliJ to the most recent version.

1. Open IntelliJ. If you are not on the welcome screen, click `File` > `Close Project` first.
1. Open the project:
   1. Click `Open`.
   1. Select the project directory, then click `OK`.
   1. Accept the defaults for any further prompts.
1. Configure the project to use **JDK 25** as explained [in IntelliJ’s SDK setup guide](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk). In the same dialog, set **Project language level** to `SDK default`.
1. Locate `src/main/java/skynet.SkyNET.java`, right-click it, and select `Run skynet.SkyNET.main()`.

   ```text
    ____  _          _   _      _
   / ___|| | ___   _| \ | | ___| |_
   \___ \| |/ / | | |  \| |/ _ \ __|
    ___) |   <| |_| | |\  |  __/ |_
   |____/|_|\_\\__, |_| \_|\___|\__|
                |___/
   ```

**Warning:** Keep `src\main\java` as the root folder for Java source files. Tools such as Gradle expect this default location.