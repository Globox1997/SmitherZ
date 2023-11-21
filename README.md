# SmitherZ
SmitherZ is a minecraft mod which adds the ability to upgrade armor, tools and weapons with gems.

### Installation
SmitherZ is a mod built for the [Fabric Loader](https://fabricmc.net/). It requires [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) and [Cloth Config API](https://www.curseforge.com/minecraft/mc-mods/cloth-config) to be installed separately; all other dependencies are installed with the mod.

### License
SmitherZ is licensed under MIT.

### Datapack
{
    "rarity_0": {
        "items": [
            "smitherz:strength_1_gem",
            "smitherz:strength_2_gem"
        ],
        "drop_chance": 0.01, // chance for droping a random item of the item list
        "mob_types": [
            "minecraft:spider" // only mobentity types
        ],
        "rarity_group": 0, // for general rarity
        "difficulty_multiplier": 1.1 // rpg difficulty compat
    }