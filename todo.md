# RoleChat Plugin Development Todo

## Project Setup
- [x] Create basic plugin structure
- [x] Set up plugin.yml with permissions
- [x] Create config.yml structure

## Core Features
- [x] Implement channel system (OOC, Shout, Talk, Whisper)
- [x] Create player data storage for preferences
- [x] Implement chat formatting with preferred colors
- [x] Add dialogue/emote color differentiation
- [x] Implement chat range limitations
- [x] Add "who heard" hover functionality
- [x] Add "No one heard you" message

## Commands
- [x] Implement /spy command
- [x] Implement /nickname command
- [x] Implement /realname command
- [x] Implement channel commands (/whisper, /shout, /talk, /ooc)
- [x] Implement /toggleooc command
- [x] Handle channel-specific message sending

## Configuration
- [x] Player-specific config (preferred colors, nickname, current channel, spy status)
- [x] Global config (default colors, restricted color codes)

## Documentation
- [x] Create README.md with usage instructions
- [x] Document permissions
- [x] Document commands
- [x] Document configuration options

## Testing
- [x] Build successful
- [x] JAR file created
- [x] Added new commands: /dialoguepref, /emotepref, /heardyou, /rolechat restrict
- [x] Added support for hex color codes (&#RRGGBB format)
- [x] Fixed dialogue formatting to use recipient's color preferences
- [x] Added spy mode for commands
- [x] Added "heardyou" global config option
- [ ] Test all commands
- [ ] Test chat formatting
- [ ] Test permissions
- [ ] Test range limitations

## Future Enhancements
- [ ] Add color command to change preferred colors
- [ ] Add private messaging system
- [ ] Add custom channel creation
- [ ] Add chat logs
- [ ] Add chat filtering