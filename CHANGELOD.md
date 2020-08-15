# 1.4.2

- Fix message rendering error.
- 2020/08/15
# 1.4.1

- NBT Framework: Supported 1.16.2
- New: `BaseComponentFormatAction`
- 2020/08/15

# 1.4.0

- Hello Groovy
- Startup scripts. (Supported ~~bukkit~~ Spigot.)
    - in `plugins/KarFramework/scripts`
- Bigger Binary file.
- 2020/08/07

# 1.3.0

- Created some io utils.
- Provided following methods in NMSProvider
  - `@NotNull BaseComponent[] toComponents(@NotNull ItemStack itemStack);`
  - `@NotNull BaseComponent[] getItemName(@NotNull ItemStack itemStack);`
- 2020/08/05

# 1.2.0

- NBT Framework finished
  `ITagList.add/set/addSafely/setSafely/clear(...)` operations.
- 2020/08/05

# 1.2.0-RC

- NBT Provider support. (Pre-release) - \[NBT Framework]
- 2020/08/04

# 1.1.0

- Fix translate.conf, you need delete the old `translate.conf`
- New method `buildNMSImplement` in [KarFrameworkBukkit](spigot/src/main/java/io/github/karlatemp/karframework/bukkit/KarFrameworkBukkit.java)
- Create [NMSProvider](spigot/src/main/java/io/github/karlatemp/karframework/bukkit/NMSProvider.java)
- Fix [ConfigTranslator](common/src/main/java/io/github/karlatemp/karframework/format/Translator.java) not work.
- 2020/08/03

# 1.0.1

- Fix saving not match.

# 1.0.0

Repo start.