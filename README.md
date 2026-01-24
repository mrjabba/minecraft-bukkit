# MyPlugin - Minecraft Bukkit Server Plugin

A feature-rich Bukkit/Spigot plugin for Minecraft 1.20 that provides building utilities, mob spawning, and player management commands.

## Features

### Building Commands
- **`/buildhouse`** - Constructs wooden houses with customizable dimensions
- **`/buildtower`** - Builds vertical towers with specified height and block type
- **`/buildtowersand`** - Specialized tower building with sand blocks
- **`/generatetree`** - Generates natural-looking trees
- **`/generatecherrytree`** - Generates cherry tree variants
- **`/fillarea`** - Fills flat areas with specified block type and radius
- **`/cleararea`** - Clears dirt blocks in a square area

### Player & World Management
- **`/additem`** - Add items to player inventory with custom amounts
- **`/move`** - Moves player forward
- **`/setlevel`** - Set player experience level
- **`/spawnmob`** - Spawn mobs near the player
- **`/explodezone`** - Set up TNT with redstone fuse and lever control

### Menu & Navigation
- **`/buildmenu`** - Opens interactive build menu
- **`/setwarp`** - Create warp points at current location
- **`/warp`** - Teleport to saved warp points

## Technical Details

- **Platform:** Bukkit/Spigot
- **Minecraft Version:** 1.20.1
- **Build System:** Gradle
- **Language:** Java

## Building

```bash
gradle build
```

### Deployment

To deploy the plugin to a local Spigot server:

```bash
gradle deployPlugin
```

This task compiles the plugin and copies the JAR to your Spigot server's plugins directory.

## Project Structure

```
src/main/java/com/minecraftplay/
├── MyPlugin.java        # Main plugin class with command handling
```

## Dependencies

- **spigot-api:1.20.1-R0.1-SNAPSHOT** - Official Spigot API for Minecraft 1.20.1

## License

See [LICENSE](LICENSE) file for details.
