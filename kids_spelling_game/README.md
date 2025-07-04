# 🌟 Kids Spelling Game

An interactive educational spelling game designed for children with configurable game settings, timer functionality, and audio feedback.

## ✨ Features

### 🏠 Home Screen Configuration
- **Player Name Input**: Personalized gaming experience with custom greetings
- **Question Count**: Choose from 5, 10, 15, 20, 25, or 30 questions
- **Timer Settings**: Configure time limits per question (10s - 60s) or unlimited time
- **Smart Validation**: Ensures proper name input before starting the game

### 🎮 Game Features
- **Progressive Difficulty**: Words with blanks that challenge spelling skills
- **Contextual Hints**: Meaningful hints loaded from word definitions
- **Real-time Timer**: Visual countdown with warning indicators
- **Progress Tracking**: Progress bar and question counter
- **Accuracy Calculation**: Live accuracy percentage display

### 🔊 Audio Experience
- **Text-to-Speech**: Welcome messages and final results spoken aloud
- **Success Sounds**: "Hurray" celebration melody for correct answers
- **Failure Sounds**: Different audio feedback for incorrect answers
- **Consecutive Failure Alerts**: Special sound pattern for multiple wrong answers

### 📊 Game Management
- **Session Persistence**: Game state maintained across page refreshes
- **Final Results**: Comprehensive game completion summary
- **Replay Options**: Easy restart with same or new settings
- **Quit Functionality**: Safe exit to home screen

## 🎯 How to Play

1. **Start**: Enter your name and configure game settings on the home screen
2. **Play**: Fill in missing letters to complete words
3. **Timer**: Answer within the time limit (if configured)
4. **Hints**: Use the hint button if you need help
5. **Progress**: Track your score and accuracy in real-time
6. **Finish**: Hear your final results and choose to play again

## 🚀 Technical Features

### Backend (Spring Boot)
- **RESTful API**: Clean endpoints for word retrieval and answer checking
- **Dynamic Word Loading**: Reads word-hint pairs from `words.txt`
- **Error Handling**: Robust error management with fallback options

### Frontend (Vanilla JS)
- **Responsive Design**: Works on desktop and mobile devices
- **Session Management**: Uses sessionStorage for game state
- **Audio Integration**: Web Audio API for sound effects
- **Speech Synthesis**: Text-to-speech for accessibility

### Word Database Format
```
Word:Hint description
Example: Alphabet:A set of letters used in writing a language
```

## 🎨 UI/UX Features

- **Kid-Friendly Design**: Colorful, engaging interface
- **Visual Feedback**: Animations, progress bars, and confetti effects
- **Accessibility**: Screen reader friendly with audio feedback
- **Intuitive Controls**: Easy-to-use buttons and clear instructions

## 📱 Game Modes

### Timer Modes
- **No Timer**: Unlimited time to think and learn
- **Quick Mode**: 10-15 seconds for fast-paced learning
- **Standard Mode**: 20-30 seconds for comfortable gameplay
- **Relaxed Mode**: 45-60 seconds for thorough thinking

### Question Counts
- **Quick Game**: 5-10 questions for short sessions
- **Standard Game**: 15-25 questions for regular practice
- **Extended Game**: 30+ questions for comprehensive learning

## 🎵 Audio Features

- **Welcome Message**: Personalized greeting with player name
- **Success Celebration**: Musical chord progression for correct answers
- **Failure Feedback**: Gentle audio cues for incorrect attempts
- **Final Results**: Spoken summary of performance and accuracy

## 📈 Educational Value

- **Vocabulary Building**: Learn new words with contextual hints
- **Spelling Practice**: Reinforce correct letter patterns
- **Time Management**: Learn to work within time constraints
- **Self-Assessment**: Track progress and identify areas for improvement

## 🔧 Running the Application

1. **Prerequisites**: Java 17+ and Maven 3.6+
2. **Build**: `mvn clean package`
3. **Run**: `java -jar target/kids-spelling-game-1.0.0.jar`
4. **Access**: Open browser to `http://localhost:8080`

## 🎪 Game Flow

```
Home Screen → Configure Settings → Start Game → Play Questions → View Results → Play Again/Home
```

This spelling game provides an engaging, educational experience that adapts to different learning styles and preferences, making it perfect for children to improve their spelling skills while having fun!
