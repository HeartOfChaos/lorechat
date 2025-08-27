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
- [ ] Test all commands
- [ ] Test chat formatting
- [ ] Test permissions
- [ ] Test range limitations

## Debugging
- Make hex codes work in regular text channels
- Add the /rolechat restrict command
- Make dialogue, or text inside of quotation marks, appear as the dialoguepref colorcode.
- Make it so that the dialogue/emote pref colors affect ALL MESSAGES that the PERSON WHO SET THE COLOR sees. If Player1 has the pref for orange, all the emotes they see should be orange, and if Player2 has a preference for pink, all the emotes they see should be pink.

## Future Enhancements
- [ ] Add color command to change preferred colors
- [ ] Add private messaging system
- [ ] Add custom channel creation
- [ ] Add chat logs
- [ ] Add chat filtering