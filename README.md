# RoleChat

A comprehensive roleplay chat plugin for Spigot servers with channels, nicknames, and spy functionality.

## Features

- **Multiple Chat Channels**: OOC (global), Shout (60 blocks), Talk (30 blocks), Whisper (3 blocks)
- **Customizable Nicknames**: Set your roleplay nickname with color support
- **Color Preferences**: Set preferred colors for dialogue and emotes
- **Spy Mode**: Admins can see all messages across all channels
- **Message Formatting**: Different formats for each channel
- **Dialogue Detection**: Automatically colors text in quotation marks differently
- **Hover Information**: See who heard your message by hovering over it
- **OOC Toggle**: Ability to hide OOC chat

## Commands

- `/spy` - Toggle spy mode (requires `rolechat.spy` permission)
- `/nickname <nickname>` - Set your roleplay nickname
- `/realname <nickname>` - Get a player's real name from their nickname
- `/whisper [message]` - Set whisper as your channel or send a whisper message
- `/talk [message]` - Set talk as your channel or send a talk message
- `/shout [message]` - Set shout as your channel or send a shout message
- `/ooc [message]` - Set OOC as your channel or send an OOC message
- `/toggleooc` - Toggle visibility of OOC messages
- `/rolechat reload` - Reload the plugin configuration
- `/rolechat help` - Show help message

## Permissions

- `rolechat.*` - Gives access to all RoleChat commands
- `rolechat.spy` - Allows player to use spy mode
- `rolechat.nickname` - Allows player to set a nickname
- `rolechat.realname` - Allows player to see real names
- `rolechat.channel.*` - Allows access to all chat channels
  - `rolechat.channel.whisper` - Allows access to whisper channel
  - `rolechat.channel.talk` - Allows access to talk channel
  - `rolechat.channel.shout` - Allows access to shout channel
  - `rolechat.channel.ooc` - Allows access to OOC channel
- `rolechat.toggleooc` - Allows toggling OOC visibility
- `rolechat.admin` - Allows access to admin commands
- `rolechat.allcolors` - Allows use of restricted color codes

## Configuration

### config.yml

```yaml
# Default colors for new players
defaults:
  dialogue-color: '&f'  # Default color for dialogue (text in quotes)
  emote-color: '&7'     # Default color for emotes (text outside quotes)

# List of color codes that require the rolechat.allcolors permission
restricted-colors:
  - '&k'  # Obfuscated
  - '&l'  # Bold
  - '&m'  # Strikethrough
  - '&n'  # Underline
  - '&o'  # Italic
  - '&r'  # Reset

# Chat format settings
formats:
  ooc: '&8[OOC] &f{displayname}: &7{message}'
  shout: '&4[Shout] {nickname}: {message}'
  talk: '{nickname}: {message}'
  whisper: '&7[Whisper] {nickname}: {message}'
  spy: '&6[Spy] {format}'

# Chat range settings (in blocks)
ranges:
  shout: 60
  talk: 30
  whisper: 3
```

## Usage

1. Install the plugin by placing the JAR file in your server's plugins folder
2. Start or reload your server
3. Configure the plugin using the config.yml file
4. Set your preferred channel using the channel commands
5. Start chatting! Messages will automatically be sent to your current channel

## Player Data

Player preferences are stored in individual files in the `plugins/RoleChat/playerdata/` directory. These include:

- Preferred dialogue color
- Preferred emote color
- Nickname
- Current channel
- Spy mode status
- OOC visibility status

## Examples

### Chat Formats

- **OOC**: `[OOC] Username: This is an OOC message`
- **Shout**: `[Shout] Nickname: "This is dialogue" This is an emote`
- **Talk**: `Nickname: "This is dialogue" This is an emote`
- **Whisper**: `[Whisper] Nickname: "This is dialogue" This is an emote`

### Color Codes

You can use Minecraft color codes in your nickname and messages:

- `&0` - Black
- `&1` - Dark Blue
- `&2` - Dark Green
- `&3` - Dark Aqua
- `&4` - Dark Red
- `&5` - Dark Purple
- `&6` - Gold
- `&7` - Gray
- `&8` - Dark Gray
- `&9` - Blue
- `&a` - Green
- `&b` - Aqua
- `&c` - Red
- `&d` - Light Purple
- `&e` - Yellow
- `&f` - White

Formatting codes (require `rolechat.allcolors` permission):
- `&k` - Obfuscated
- `&l` - Bold
- `&m` - Strikethrough
- `&n` - Underline
- `&o` - Italic
- `&r` - Reset