#### General Requirements

- All new plugin features must include an entry in plugin.yml
- All commands must have proper permission definitions in plugin.yml
- Use Maven or Gradle for dependency management

#### Dependencies

For Bukkit/Spigot plugins, include in pom.xml or build.gradle:
```xml
<!-- Maven -->
<dependency>
    <groupId>org.spigotmc</groupId>
    <artifactId>spigot-api</artifactId>
    <version>1.21.4-R0.1-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

#### Plugin Structure

Every plugin must:
1. Extend JavaPlugin in main class
2. Implement onEnable() for initialization
3. Implement onDisable() for cleanup
4. Register listeners in onEnable()
5. Register commands in plugin.yml and code

#### Data Storage

- Use YAML for configuration
- Use SQLite/MySQL for player data
- Use PersistentDataContainer for small per-player data

#### Thread Safety

- Never block the main thread
- Use runTaskAsynchronously() for database/network operations
- Sync back to main thread with runTask() for Bukkit API calls from async

#### Deliverables for Every Feature

For every request, output:
- The feature implementation
- Tests if applicable
- Documentation updates
- Any plugin.yml changes

#### Code Style

- Follow Java naming conventions
- Use meaningful variable/method names
- Keep methods under 30 lines
- Extract constants for magic strings
