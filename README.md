# KarFramework

## What's this

First, add our repository (https://raw.githubusercontent.com/Karlatemp/karlatemp-repo/master/)
into your repositories 

Gradle:
```groovy
repositories {
    maven {
        url = 'https://raw.githubusercontent.com/Karlatemp/karlatemp-repo/master/'
        name = 'Karlatemp Repo'
    }
}
```

Now, you can use our framework now!

```groovy
dependencies {
    compile 'io.github.karlatemp.kar-framework:kar-framework:1.0.1'
}
```

Latest version: `1.0.1`

## Command Node

### Common Command Node

You can create command node with link calling.

First. You need a command-framework.

```java
class TestFramework
    // extends AbstractCommandFramework<PrintStream>
    implements ICommandFramework<PrintStream> {}
```

You can found example [here](common/src/test/java/io/github/karframwork/common/test/TestCommand.java)

Then, we can register our commands now.

```java
class TestFramework implements ICommandFramework<PrintStream> {
    public static void register() {
        ICommandFramework<PrintStream> framework = new TestFramework();
        final ICommandNode<PrintStream> command = new CommandTree<>(framework)
                .registerSubCommand(framework.newSingleCommand()
                        .setName("test")
                        .setDescription("Description")
                        .setExecutor((sender, arguments, sourceArguments) -> {
                            sender.println("Hello!");
                            sender.println(arguments);
                            sender.println(sourceArguments);
                        })
                        .build())
                .registerSubCommand(framework.newSingleCommand()
                        .setName("text")
                        .setDescription("D2")
                        .setExecutor((sender, arguments, sourceArguments) -> {
                            sender.println("TEXT HERE");
                        })
                        .build());
        command.execute(System.out, Arrays.asList("test"));
    }
}
```

### Bukkit/Bungee Command Node

In bukkit/bungee. We have standard frameworks. You can get they with 
`KarFrameworkBukkit.getInstance()/KarFrameworkBungee.getInstance()` 

Here is a example.
```java
public class CommandRegister {
    public static void register() {
        KarFrameworkBukkit framework = KarFrameworkBukkit.getInstance();
        final ICommandNode<CommandSender> command = new CommandTree<>(framework)
                        .registerSubCommand(framework.newSingleCommand()
                                .setName("test")
                                .setDescription("Description")
                                .setExecutor((sender, arguments, sourceArguments) -> {
                                    sender.sendMessage("Hello!");
                                })
                                .build())
                        .registerSubCommand(framework.newSingleCommand()
                                .setName("text")
                                .setDescription("D2")
                                .setExecutor((sender, arguments, sourceArguments) -> {
                                    sender.sendMessage("TEXT HERE");
                                })
                                .build());
        framework.provide(MyPlugin.getInstance()).provideCommand("my-command", command);
    }
}
```

## Configuration with SpongePowered

We have integrated 3 configurations format support(HOCON, JSON, YAML),
Powered by [SpongePowered/Configurate](https://github.com/SpongePowered/Configurate)

You can use them in bukkit/bungee, or other applications.
Just need implements your own [IPluginProvider](common/src/main/java/io/github/karlatemp/karframework/IPluginProvider.java)

Here is a example on Bukkit

```java
public class MyPlugin extends JavaPlugin {
    private static IPluginProvider provider;
    private ConfigurationNode config;
    public void onLoad() {
        provider = KarFrameworkBukkit.getInstance().provide(this);
    }

    public void reloadConfig() {
        ConfigurationLoader<? extends ConfigurationNode> loader = provider.loadConfiguration("config.conf");
        assert loader != null;
        config = provider.loadConfiguration(loader);
    }

    public org.bukkit.configuration.file.FileConfiguration getConfig() {
        return new YamlConfiguration();
    }
}
```
