# SmitherZ
SmitherZ is a minecraft mod which adds the ability to upgrade armor, tools and weapons with gems.

### Installation
SmitherZ is a mod built for the [Fabric Loader](https://fabricmc.net/). It requires [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) and [Cloth Config API](https://www.curseforge.com/minecraft/mc-mods/cloth-config) to be installed separately; all other dependencies are installed with the mod.

### License
SmitherZ is licensed under MIT.

### Datapack
Gems can be created, gem drops can be set (mainly for RpgDifficulty compat) and item socket count can be set (mainly for TieredZ compat).  
If you don't know how to create a datapack check out [Data Pack Wiki](https://minecraft.fandom.com/wiki/Data_Pack) website and try to create your first one for the vanilla game.  
If you know how to create one, the folder path has to be ```data\modid\FOLDER\YOURFILE.json```.  
The `FOLDER` is `gems` for new gems, `gem_drops` for adding gem drops and `gem_upgrades` for item socket count.  
For creating new gems you have to put the datapack under ```.minecraft\global_packs\required_datapacks\YOURDATAPACK```.  

Example for a new gem json:
```json
{
    "new_gem_name": {
        "link_chance": 0.1, // chance to link gem to item
        "break_chance": 0.1, // chance to break whole item on link fail
        "unlink_chance": 0.1, // chance to unlink gem
        "attributes": [
            {
                "type": "generic.max_health",
                "modifier": {
                    "operation": "ADDITION",
                    "amount": 2
                }
            }
        ],
        "tag": "minecraft:swords"
    }
}
```

Example for a gem drop json:
```json
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
}
```

Example for a socket count json:
```json
{
    "2": [ // socket count
        "tiered:common_armor_1", // nbt string of an item
        "tiered:common_armor_2",
    ]
}
```

