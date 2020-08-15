# KarFramework

[简体中文](./README-zh.md)

## What's this

Here is a framework of bungee/~~bukkit~~ Spigot and standard application.

Using this framework. You can:
- Building commands with link calling.
- Loading/Saving your configuration anywhere.
- Template support!
- Regex Replacer. in `RegexUtils`

## How to use

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
    compile 'io.github.karlatemp.kar-framework:kar-framework:1.4.2'
}
```

Latest version: `1.4.2`

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

### Spigot/Bungee Command Node

In ~~bukkit~~ Spigot/bungee. We have standard frameworks. You can get they with 
`KarFrameworkBukkit.getInstance()/KarFrameworkBungee.getInstance()`.

Here is an example.
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

You can use them in ~~bukkit~~ Spigot/bungee, or other applications.
Just need implements your own [IPluginProvider](common/src/main/java/io/github/karlatemp/karframework/IPluginProvider.java)

Here is an example on ~~Bukkit~~ Spigot

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

## NBT Framework

On ~~bukkit~~ Spigot, We provided NBT Framework.
You can use our framework to access nbt
without having to visit nms by yourself.

First. You need get our nbt framework with
`IBukkitNbtProvider provider = KarFrameworkBukkit.getNbtProvider();`.

Access nbt will be very easy.

```java
public class TestNBT { public static void test() {
    ITagCompound item = provider.newCompound();
    item.setString("id", "minecraft:stone");
    ItemStack itemStack = provider.fromCompound(item);
} }
```

## NMS Framework

On ~~bukkit~~ Spigot, We provided

- `void sendPacket(Player, Object packet)`
- `void sendTitle(Player, BaseComponent, BaseComponent, int, int, int)`
- `void sendAction(Player, BaseComponent)` (sendActionBar)
- `CommandMap getCommandMap()`
- `Function<@NotNull String, @Nullable String> getSystemLocale()`
- `Object getHandle(Entity)`
- `BaseComponent[] toComponents(ItemStack)`
- `BaseComponent[] getItemName(ItemStack)`

in `KarFrameworkBukkit.getNmsProvider()`.
