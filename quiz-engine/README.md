# Quiz Game Engine

A dynamic and educational multiple-choice quiz game engine built with Spring Boot. This application supports loading question sets from configurable JSON files and offers per-question timers, sound effects, voice feedback, scoring, and comprehensive game summaries.

## Features

- **Dynamic Quiz Loading**: Load multiple quiz sets from JSON files
- **Flexible Question Types**: Support for single-choice and multiple-choice questions
- **Interactive UI**: Modern, responsive web interface with sound effects
- **Timer System**: Configurable per-question time limits
- **Text-to-Speech**: Voice feedback and greetings for enhanced user experience
- **Real-time Scoring**: Live tracking of correct/incorrect answers and accuracy
- **Game Summary**: Detailed final statistics with voice narration
- **Educational Focus**: Perfect for exam preparation and kids learning games

## Project Structure

```
src/
 └── main/
     ├── java/com/example/quizgame/
     │    ├── QuizGameApplication.java
     │    ├── controller/
     │    │    └── QuizGameController.java
     │    ├── service/
     │    │    ├── GameService.java
     │    │    └── QuizService.java
     │    ├── model/
     │    │    ├── Quiz.java
     │    │    ├── Question.java
     │    │    └── GameSession.java
     │    └── util/
     │         └── GameUtils.java
     ├── resources/
     │    ├── application.properties
     │    ├── qa/
     │    │    ├── math.json
     │    │    ├── science.json
     │    │    ├── history.json
     │    │    └── geography.json
     │    ├── static/sounds/
     │    └── templates/
     │         ├── index.html
     │         ├── game.html
     │         └── game-summary.html
     └── test/
```

## Quiz JSON Format

Each quiz file follows this structure:

```json
{
  "title": "Math Quiz",
  "subject": "Mathematics", 
  "difficulty": "easy",
  "questions": [
    {
      "id": "M1",
      "question": "What is 2 + 2?",
      "hint": "It's the first even number after 3",
      "answerType": "single",
      "choices": ["3", "4", "5", "6"],
      "answerPositions": [1],
      "explanations": ["Two plus two equals four. This is basic addition."],
      "image": null,
      "tags": ["addition", "math", "easy"]
    }
  ]
}
```

### Key Fields

- **answerType**: "single" for single-choice, "multi" for multiple-choice
- **answerPositions**: Array of correct answer indices (0-based)
- **explanations**: Detailed explanations for learning
- **hint**: Optional hint text to help users
- **image**: Optional image URL or file path
- **tags**: Keywords for categorization

## Game Configuration

Users can customize their quiz experience:

- **Username**: Personal greeting and TTS narration
- **Quiz Selection**: Choose from available quiz topics
- **Number of Questions**: 5, 10, 25, 50, or 100 questions
- **Time Per Question**: 30s, 60s, 90s, or 2 minutes

## Game Flow

1. **Welcome Screen**: User enters name and selects preferences
2. **Voice Greeting**: TTS welcomes user by name
3. **Question Display**: Shows question with choices and timer
4. **Answer Feedback**: Immediate audio/visual feedback
5. **Progress Tracking**: Real-time statistics display
6. **Game Summary**: Final scores with voice narration

## API Endpoints

- `GET /` - Main game interface
- `POST /start-game` - Initialize new game session
- `GET /api/question/{sessionId}` - Get current question
- `POST /api/submit-answer/{sessionId}` - Submit answer
- `POST /api/skip-question/{sessionId}` - Skip current question
- `GET /api/summary/{sessionId}` - Get final game summary
- `GET /game/{sessionId}` - Game play interface

## Technologies Used

- **Spring Boot 3.2.0** - Core framework
- **Thymeleaf** - Template engine for UI
- **Jackson** - JSON processing
- **Web Speech API** - Text-to-speech functionality
- **HTML5 Audio** - Sound effects
- **JavaScript** - Client-side interactivity
- **Maven** - Build and dependency management

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. Clone the repository
2. Navigate to project directory
3. Run the application:

```bash
mvn spring-boot:run
```

4. Open browser and go to `http://localhost:8080`

### Running Tests

```bash
mvn test
```

### Building for Production

```bash
mvn clean package
java -jar target/quiz-engine-0.0.1-SNAPSHOT.jar
```

## Adding New Quizzes

1. Create a new JSON file in `src/main/resources/qa/`
2. Follow the quiz JSON format
3. Update `QuizService.java` to include the new file path
4. Restart the application

## Sound Effects

The application includes built-in sound generation using Web Audio API:

- **Welcome Sound**: Musical greeting when starting
- **Correct Answer**: Success audio feedback
- **Wrong Answer**: Error audio feedback
- **Background Music**: Optional ambient sounds

## Accessibility Features

- **Voice Feedback**: TTS for all major interactions
- **Visual Feedback**: Clear success/error indicators
- **Keyboard Navigation**: Full keyboard support
- **Responsive Design**: Works on desktop and mobile devices

## Educational Benefits

- **Immediate Feedback**: Learn from mistakes instantly
- **Explanations**: Detailed answers for better understanding
- **Progress Tracking**: Motivation through visible improvement
- **Adaptive Learning**: Hints and multiple difficulty levels
- **Engagement**: Audio-visual elements keep users interested

## Customization

The application is highly customizable:

- **Quiz Content**: Easy to add new subjects and questions
- **UI Themes**: Modify CSS for different visual styles
- **Sound Effects**: Replace or add new audio files
- **Time Limits**: Adjust per-question timing
- **Scoring System**: Modify point calculations

## Future Enhancements

- User accounts and progress saving
- Multiplayer quiz competitions
- Advanced analytics and reporting
- Integration with learning management systems
- Mobile app version
- Advanced question types (drag-drop, fill-in-blank)

## Support

For questions or issues, please refer to the application logs or check the API endpoints for debugging information.
