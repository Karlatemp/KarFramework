# KarFramework

[ENGLISH](./README.md)

## 这是什么

这是一个 bungee/~~bukkit~~ Spigot 和标准java程序的一个框架,

使用此框架，你可以:
- 使用链式构建命令系统
- 快速加载你的系统配置
- 字符串模板支撑
- 正则替换器, 就在 `RegexUtils` 里面

## 如何使用

首先, 你需要添加我们的mvn仓库地址 (https://raw.githubusercontent.com/Karlatemp/karlatemp-repo/master/)

Gradle:
```groovy
repositories {
    maven {
        url = 'https://raw.githubusercontent.com/Karlatemp/karlatemp-repo/master/'
        name = 'Karlatemp Repo'
    }
}
```

然后你就可以开始使用我们的框架了!


```groovy
dependencies {
    compile 'io.github.karlatemp.kar-framework:kar-framework:1.3.0'
}
```

最后一个版本为: `1.3.0`

## 命令树

### 标准命令树

你可以使用链式创建命令树.

首先, 你需要一个 `CommandFramework`

```java
class TestFramework
    // extends AbstractCommandFramework<PrintStream>
    implements ICommandFramework<PrintStream> {}
```

你可以在 [这里](common/src/test/java/io/github/karframwork/common/test/TestCommand.java)
找到我们的示例文件

然后, 开始注册我们的命令吧

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

### Bukkit/Bungee 命令树

在 Bukkit/BungeeCord, 我们已经提供了对应平台的标准 `CommandFramework`.
你可以使用以下方法获取:
`KarFrameworkBukkit.getInstance()/KarFrameworkBungee.getInstance()` 

这是一个示例代码
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

## 快速加载你的配置文件 (Powered by SpongePowered)

我们提供了三种配置格式支持, HOCON, JSON, YAML

Powered by [SpongePowered/Configurate](https://github.com/SpongePowered/Configurate)

你可以在 ~~bukkit~~ Spigot/bugee, 或者你自己的应用程序使用他们.
只需要实现自己的 [IPluginProvider](common/src/main/java/io/github/karlatemp/karframework/IPluginProvider.java)

下面是一段运行在 ~~bukkit~~ Spigot 的示例代码


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

----

## NBT Framework

在 ~~bukkit~~ Spigot 平台上, 我们提供了 NBT 操作框架, 你可以使用我们的操作框架,
而不需要自己访问 nms (实验性)

首先你需要获取我们的 NBT Provider.
`IBukkitNbtProvider provider = KarFrameworkBukkit.getNbtProvider();`

这能让你操作 nbt 变得十分简单

```java
public class TestNBT { public static void test() {
    ITagCompound item = provider.newCompound();
    item.setString("id", "minecraft:stone");
    ItemStack itemStack = provider.fromCompound(item);
} }
```

## NMS Framework

在 ~~bukkit~~ Spigot 上, 我们提供了一些快速框架, 包括
- `void sendPacket(Player, Object packet)`
- `void sendTitle(Player, BaseComponent, BaseComponent, int, int, int)`
- `void sendAction(Player, BaseComponent)` (sendActionBar)
- `CommandMap getCommandMap()`
- `Function<@NotNull String, @Nullable String> getSystemLocale()`
- `Object getHandle(Entity)`
- `BaseComponent[] toComponents(ItemStack)`
- `BaseComponent[] getItemName(ItemStack)`

你所需要做的就是 `KarFrameworkBukkit.getNmsProvider().....`
