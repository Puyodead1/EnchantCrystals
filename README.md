# EnchantCrystals

Plugin that changes vanilla enchanting by adding Crystals that can contain one or more enchants. Enchants can be applied
by right-clicking on an item. Crystals can be obtained in an enchanting table, or from merchants (Both of these features
are configurable). This plugin is also fully customizable.

Enchants can be Stacked/Combined - ex: Knockback 1 + Knockback 1 = Knockback 2<br>
Enchants can be Upgraded - ex: Knockback 2 would overwrite Knockback 1

Works with all vanilla enchants!

SpigotMC listing can be found at https://www.spigotmc.org/resources/enchantcrystals.75155/

## Showcase

![Crystal Example 1](https://i.imgur.com/QAJrvak.png) <br>
![Crystal Example 2](https://i.imgur.com/PjmRzo5.png)

![Merchant Demo](https://i.imgur.com/j63FXFv.gif) <br>
![Enchanting Table Demo](https://i.imgur.com/5tD3O1F.gif) <br>
![Application Demo](https://i.imgur.com/RgQbI8P.gif) <br>
![Stacking Demo](https://i.imgur.com/naL8H43.gif) <br>
![Upgrade Demo](https://i.imgur.com/drMuA9y.gif) <br>

## Permissions and Commands

- Permissions:
    - ``enchantcrystals.enchantcrystals`` - Ability to use ``/ecr`` or ``/enchantcrystals``
- Commands:
    - ``/ecr`` or ``/enchantcrystals``
- Command Usage:
    - ``/ecr give <enchant name> [level] [amount] [player]`` - Gives a new Enchant Crystal
    - ``/ecr add <enchant name> [level]`` - Adds an enchantment to an existing Enchant Crystal
    - ``/ecr enchants`` - List available enchants

## Default Config

<details>
  <summary>Click to expand</summary>

```
# Made by Puyodead1
settings:
  enchanting_tables:
    enabled: false # Setting this to true will change enchanted books to crystals in enchantment tables
    require_lapis: true

  merchants:
    # Changing this from true to false will not affect any merchants that have already been interacted with!
    enabled: false # Setting this to true will change enchanted books to crystals in merchant trades
  item:
    material: NETHER_STAR # https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html (Don't use legacy names)
    display_name: "&6Enchantment Crystal &8[&7%ENCHANTMENT_COUNT% %ENCHANTMENT_STRING%&8]"
    enchant_lore: "&7%ENCHANTMENT_NAME% %ENCHANTMENT_LEVEL%"
    lore:
      - ""
      - "&7Right click on an item to apply."

messages:
  enchantment_plural: "Enchantments"
  enchantment_singular: "Enchantment"
  already_enchanted: "&cThat item already contains this enchantment!"
  enchantment_success: "&aSuccessfully applied &e%ENCHANTMENT_NAME% %ENCHANTMENT_LEVEL% &ato &e%ITEM_DISPLAY_NAME%&a!"
  enchantment_bounds_error: "&cEnchantment level out of bounds! Must be between %ENCHANTMENT_LEVEL_START% and %ENCHANTMENT_LEVEL_MAX%"
  enchantment_conflict: "&e%OTHER_ENCHANTMENT_NAME% &cconflicts with this enchantment&c!"
  enchantment_max_exceed: "&e%ENCHANTMENT_NAME% &ccannot be combined as it would exceed the maximum level!"
  enchantment_upgraded: "&aSuccessfully upgraded &e%ENCHANTMENT_NAME% &afrom level &e%ENCHANTMENT_LEVEL_OLD% &ato level &e%ENCHANTMENT_LEVEL_NEW%&a!"
  invalid_item: "&e%ENCHANTMENT_NAME% &ccannot be applied to &e%ITEM_DISPLAY_NAME%!"
  invalid_player: "&cInvalid player specified!"
  invalid_enchantment: "&cInvalid Enchantment! Check &e/gc enchants &cfor a list of valid enchantments."
  missing_permission: "&cYou do not have permission to run that command!"
  crystal_given: "&aAdded &ex%CRYSTAL_AMOUNT% %ENCHANTMENT_NAME% &6Enchantment Crystal &ato your inventory!"
  crystal_given_to_player: "&a%PLAYER_NAME% has been given &ex%CRYSTAL_AMOUNT% %ENCHANTMENT_NAME%&a!"
  crystal_received: "&aYou have been given &ex%CRYSTAL_AMOUNT% %ENCHANTMENT_NAME% &afrom &e%PLAYER_NAME%&a!"
  creative_error: "&cYou cannot apply crystals in creative mode!"
  add_invalid_item: "&cYou must be holding an &6Enchantment Crystal &cto apply enchants!"
  add_success: "&e%ENCHANTMENT_NAME% &ahas been added to the crystal!"
```

</details>

For support, please make an issue on [GitHub](https://github.com/Puyodead1/EnchantCrystals/issues/new) or join
the [Discord Server](https://discord.gg/tMzrSxQ)
