# Nil125Fix

Nilmod aiming to fix some bugs in Minecraft 1.2.5 mods.

Every fix can be disabled/enabled individually in the config.

## Current fixes

- [General] Remove/skip broken update checkers
- [General] Remove/skip debug logs from the console
- [Vanilla] Fix many rendering bugs on some Intel graphics driver versions (credits to [TheMasterCaver](https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/1294926-themastercavers-world?comment=294))
- [Vanilla] Apply Lights Out and Antifreeze patches to fix lighting bugs (credits to [last_username](https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/1282658-1-2-5-lights-out-antifreeze-two-mods-to-fix))
- [Forge/ML] Add warnings if two entities share the same ID
- [Forge] Tidy up unresolved ore recipes (important when NEI is also installed as these lead to crashes)
- [Better Dungeons] Add annoying warning when the mod is not properly installed
- [Buildcraft 3] Fix oil dupe using refinery
- [Factorization] Fix item rendering on barrels
- [NEI] Fix pruning item stacks to only a single remaining item when clicking "Show Usages" on them
- [NEI] Made search field better (<kbd>Ctrl</kbd>+<kbd>A</kbd>, arrow keys etc. are now working!)
- [RedPower 2] Fix crashes in certain Mystcraft dimensions due to faultry Marble generation
- [Rei's Minimap] Fix sporadic internal crash
- [Tropicraft] Change hardcoded turtle ID to avoid chunk resets with Mo' Creatures

## FAQ

**Q**: What the hell is a Nilmod? How do I install this? AAAAAAAAAAA??!?!?!!  
**A**: First of all: you can install NilLoader alongside any other loader (including Forge, ModLoader etc.)! A Nilmod is a mod that is loaded by [NilLoader](https://git.sleeping.town/Nil/NilLoader). You should [install NilLoader](https://git.sleeping.town/Nil/NilLoader#using-nilloader) first and then put NilFix in either the usual `mods` folder or in the dedicated `nilmods` folder (recommended!) in your Minecraft instance.

**Q**: I have found a bug in 1.2.5 itself or some mod. Can you please fix it?  
**A**: Well, I will try my best! Please report it over at the [mod's issues](https://github.com/ThexXTURBOXx/Nil125Fix/issues).

**Q**: I have found a bug in NilFix itself. Can you please fix it?  
**A**: That's unfortunate! Please report it over at the [mod's issues](https://github.com/ThexXTURBOXx/Nil125Fix/issues) and I will fix it, of course.

**Q**: I don't have some of these mods installed. Will this mod still work?  
**A**: Yes, NilLoader only applies my fixes if the corresponding mods are present.

**Q**: I want to disable certain fixes. Is this possible?  
**A**: Yes! In your Minecraft instance folder, go to the `config` directory and open the `Nil125Fix.cfg` file. There, you can disable unwanted patches.

**Q**: Is there a server version of this?  
**A**: Not yet. The server uses very different mappings and I guess (even though, I have not tested this so far) that it is simply incompatible for now. Also, targeting server mappings requires changes in NilGradle - which would need to be done first.

**Q**: Are there any plans for 1.4.7/1.5.2/[any other version]?  
**A**: If enough people are interested in this: yes, of course!
