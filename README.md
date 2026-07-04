<div align="center">

# Nhut Reader

**English** | [Tiếng Việt](README_VI.md)

Nhut Reader is a lightweight, distraction-free Japanese EPUB reader for Android. Built specifically for language immersion, it features Yomitan dictionary integration, direct Anki card creation, read-along audiobook synchronization, AI-powered translations, and e-ink display optimizations.

<table>
  <tr>
    <td><img src="images/bookshelf.jpg" alt="Bookshelf" width="100%"></td>
    <td><img src="images/reader-lookup-popup.jpg" alt="Reader lookup popup" width="100%"></td>
    <td><img src="images/reader-dark-theme.jpg" alt="Dark reader" width="100%"></td>
    <td><img src="images/reader-eink-mode.jpg" alt="E-ink reader" width="100%"></td>
  </tr>
  <tr>
    <td><img src="images/sasayaki-audiobook.jpg" alt="Sasayaki audiobook" width="100%"></td>
    <td><img src="images/reader-statistics.jpg" alt="Reader statistics" width="100%"></td>
    <td><img src="images/reader-highlights.jpg" alt="Reader highlights" width="100%"></td>
    <td><img src="images/dictionary-recursive-lookup.jpg" alt="Dictionary recursive lookup" width="100%"></td>
  </tr>
  <tr>
    <td><img src="images/reader-appearance-settings.jpg" alt="Reader appearance settings" width="100%"></td>
    <td><img src="images/dictionary-management.jpg" alt="Dictionary management" width="100%"></td>
    <td><img src="images/anki-card-settings.jpg" alt="Anki card settings" width="100%"></td>
    <td><img src="images/sync-settings.jpg" alt="Sync settings" width="100%"></td>
  </tr>
  <tr>
    <td><img src="images/ai-translation-pop-up.png" alt="AI translation pop up" width="100%"></td>
    <td><img src="images/ai-translation-settings.png" alt="AI translation settings" width="100%"></td>
    <td><img src="images/anki-mining-history.png" alt="Anki mining history" width="100%"></td>
    <td><img src="images/firebase-cloud.png" alt="Firebase cloud integration" width="100%"></td>
  </tr>
</table>

</div>

## Features

### Bookshelf & Library Management
- **Flexible Imports**: Import EPUBs individually, in batches, or recursively scan entire directories.
- **Reading Progress**: Track and display progress directly on the bookshelf cover.
- **Shelves**: Organize your library using custom bookshelves.
- **Cloud Sync**: Export EPUBs and sync books seamlessly from remote servers.

### Immersive Reading Experience
- **Layout Options**: Read Japanese text in vertical (traditional) or horizontal layouts with continuous scroll or paginated modes.
- **Visual Customization**: Fully adjust spacing, margins, line heights, system or custom fonts, and custom themes (light, dark, sepia).
- **Physical Controls**: Volume-key page turning and e-ink display options.
- **Image Viewer**: Open in-book images in fullscreen with zoom, save, copy, and share options.

### Advanced Dictionary Lookup
- **Yomitan Integration**: Import, download, and manage Yomitan dictionaries directly on device.
- **Text Search & System Lookup**: Lookup words via reader tap, manually search from the Dictionary tab, or share selected text from other Android apps to Nhut Reader.
- **Recursive Definitions**: Tap on unknown words inside dictionary definitions for nested lookups.
- **Personalization**: Inject custom CSS styles for popups, choose online or local audio sources.

### AI-Powered Translation
- **Gemini Models**: Translate vocabulary or full sentences using state-of-the-art models (such as `gemini-2.5-flash`, `gemini-2.5-pro`, or `gemini-3-flash-preview`).
- **Auto-Translate**: Trigger translation automatically when tapping a word.
- **Anki Integration**: Formats sentence translations automatically when mining to highlight keyword occurrences (e.g., `<span class="group">...</span>` and `<span class="highlight">...</span>`).
- **History Logs**: Automatically saves all translation history to Firebase Firestore.

### Highlights & Reading Stats
- **Multi-color Highlights**: Highlight texts in five colors with simple jump-to navigation.
- **Reading Statistics**: Live display of character count, time spent, and reading speed while reading.

### Anki Flashcard Mining
- **Seamless Integrations**: Create cards instantly via local AnkiDroid API or remote AnkiConnect.
- **Custom Templates**: Map fields dynamically (compatible with Lapis format).
- **Smart Duplicate Checks**: Avoid duplicate notes by verifying checksums across the entire database or deck.
- **Mining History**: View and manage all previously mined terms with timestamps inside a dedicated history log.

### Audiobook Read-Along (Sasayaki)
- **Time-aligned Highlighting**: Synchronize audiobook subtitle files (SRT/WebVTT) with EPUB text to automatically highlight the spoken sentence.
- **Autoplay & Autoscroll**: Turn pages automatically to keep up with the audio.
- **Rich Controls**: Adjust playback speed, skip cues forward/backward, and control playback via system media session notifications.

## Privacy & Data
Nhut Reader stores your books, dictionaries, fonts, reading progress, highlights, statistics, and settings locally on your device.

- **Google Drive**: Uses Google Cloud OAuth device-code flow to sync files.
- **Anki Mining**: Communicates directly with local AnkiDroid database or AnkiConnect endpoints.
- **Firebase**: Utilized solely for crashes, remote debugging, and translation history synchronization.

## Attribution
Nhut Reader is built on top of these amazing projects:
- [hoshidicts](https://github.com/Manhhao/hoshidicts) and [hoshidicts-kotlin-bridge](https://github.com/Manhhao/hoshidicts-kotlin-bridge) for Yomitan dictionary support.
- [Yomitan](https://github.com/yomidevs/yomitan) for dictionary format and lookup inspiration.
- [AnkiDroid](https://github.com/ankidroid/Anki-Android) for Android card creation integration.
- [Ankiconnect Android](https://github.com/KamWithK/AnkiconnectAndroid) for local audio behavior and AnkiDroid duplicate scope/checksum query references.
- [ッツ Ebook Reader](https://github.com/ttu-ttu/ebook-reader) for reader, statistics, and sync compatibility references.

## License
Distributed under the GNU General Public License v3.0. See [LICENSE](LICENSE) for details.
