# Two-Player Board Game for Mobile Phones
A multiplayer board game that uses two android clients and one java server to communicate. Currently android clients are able to connect and play chess against
one another when they connect to the server.

## Getting Started (Software Based)
The Java Server Should be run remotely.
Android devices simply require the app to be opened and the play button pressed for the game to start working.

### Prerequisites
* Android Phone or Emulator

### Installing
In order to install the android application you can
*  Run the code from Android Studio and install it on a phone or emulator
*  Install the provided APK on your phone.  

Upon opening the android application you can type a username that will be displayed to the other player and press play!

### Technical Achievements

*  The Java server validates each individual move is correct
*  Android clients only show valid moves to the client.
*  Checkmate and checks are able to be correctly achieved by either player.
*  All methods written to determine check/checkmate are execulsively designed by myself
*  All chess rules implemented including the special rules, En Passant, Castling and Pawn Promotion