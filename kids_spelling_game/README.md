# Kids Spelling Game

A fun and interactive spelling game for kids built with Java Spring Boot backend and HTML/CSS/JavaScript frontend.

## Features

- **Fill-in-the-Blanks Challenge**: Kids complete words by filling in missing letters
- **Technology Words**: Learn spelling of computer and technology terms
- **Helpful Hints**: Each word comes with a descriptive hint
- **Instant Feedback**: Immediate response with encouraging messages
- **Score Tracking**: Keep track of correct answers
- **Kid-Friendly Design**: Bright colors, large fonts, and fun animations
- **Sound Effects**: Success sounds for correct answers
- **Confetti Animation**: Celebration effects for correct answers
- **Dynamic Word Loading**: Words are loaded from a customizable file

## How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6+ (or use the included Maven wrapper)

### Running the Application

1. Navigate to the project directory:
   ```bash
   cd kids_spelling_game
   ```

2. Run the application using Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
   
   Or on Windows:
   ```bash
   mvnw.cmd spring-boot:run
   ```

3. Open your web browser and go to:
   ```
   http://localhost:8080
   ```

4. Start playing! The game will load with a scrambled word and hint.

## How to Play

1. Look at the word with missing letters (blanks shown as dashes)
2. Click "Show Hint" if you need help understanding what the word means
3. Type the complete word in the input box
4. Click "Submit" or press Enter
5. Get instant feedback!
6. Click "New Word" to get a new challenge

## Game Words

The game loads words from `src/main/resources/words.txt`. Current words include technology terms like:
- device, computer, keyboard, monitor
- software, hardware, internet, website
- database, network, printer, scanner
- and many more!

Each word comes with a helpful hint to guide young learners.

## API Endpoints

- `GET /api/word` - Get a random word with blanks and hint
- `POST /api/check` - Check if the user's answer is correct

## Project Structure

```
src/
├── main/
│   ├── java/com/example/kidsspellinggame/
│   │   ├── KidsSpellingGameApplication.java
│   │   ├── controller/SpellingGameController.java
│   │   ├── service/SpellingGameService.java
│   │   └── model/
│   │       ├── Word.java
│   │       ├── CheckRequest.java
│   │       └── CheckResponse.java
│   └── resources/
│       ├── static/
│       │   ├── index.html
│       │   ├── style.css
│       │   └── script.js
│       ├── application.properties
│       └── words.txt
└── pom.xml
```

## Technologies Used

- **Backend**: Java 17, Spring Boot 3.1.0
- **Frontend**: HTML5, CSS3, Vanilla JavaScript
- **Build Tool**: Maven
- **Server**: Embedded Tomcat (Spring Boot)

## Customization

### Adding More Words

To add more words to the game, edit the `SpellingGameService.java` file and add new entries to the `wordList`:

```java
new Word("your-word", scrambleWord("your-word"), "Your hint here")
```

### Changing Styles

Modify the `style.css` file to change colors, fonts, or layout. The current design uses:
- Bright, kid-friendly colors
- Large, readable fonts (Comic Sans MS)
- Responsive design for mobile devices

### Sound Effects

The game includes optional sound effects using the Web Audio API. These will work in modern browsers but may require user interaction to start.

## Browser Compatibility

- Chrome/Edge: Full support
- Firefox: Full support
- Safari: Full support
- Mobile browsers: Full support with responsive design

## Educational Benefits

- **Spelling Practice**: Reinforces correct spelling through active engagement
- **Problem Solving**: Kids learn to unscramble and recognize letter patterns
- **Vocabulary Building**: Each word comes with descriptive hints
- **Positive Reinforcement**: Encouraging messages build confidence
- **Hand-Eye Coordination**: Typing and clicking improve motor skills

Enjoy playing and learning! 🌟
