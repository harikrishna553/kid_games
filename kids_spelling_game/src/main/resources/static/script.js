// Kids Spelling Game JavaScript
let currentWord = null;
let correctCount = 0;
let totalCount = 0;
let hintVisible = false;
let consecutiveFailures = 0;
let gameConfig = null;
let gameTimer = null;
let timeRemaining = 0;
let audioContext = null;
let audioInitialized = false;

// DOM Elements
const scrambledWordEl = document.getElementById('scrambled-word');
const hintEl = document.getElementById('hint');
const userInputEl = document.getElementById('user-input');
const submitBtn = document.getElementById('submit-btn');
const resultEl = document.getElementById('result');
const newWordBtn = document.getElementById('new-word-btn');
const hintBtn = document.getElementById('hint-btn');
const correctCountEl = document.getElementById('correct-count');
const totalCountEl = document.getElementById('total-count');
const accuracyPercentageEl = document.getElementById('accuracy-percentage');
const currentQuestionEl = document.getElementById('current-question');
const totalQuestionsEl = document.getElementById('total-questions');
const timerDisplayEl = document.getElementById('timer-display');
const timerCountdownEl = document.getElementById('timer-countdown');
const playerGreetingEl = document.getElementById('player-greeting');

// Initialize the game
document.addEventListener('DOMContentLoaded', function() {
    loadGameConfiguration();
    setupEventListeners();
    getNewWord();
});

function loadGameConfiguration() {
    const configData = sessionStorage.getItem('gameConfig');
    
    if (configData) {
        gameConfig = JSON.parse(configData);
        
        // Update UI with player info
        playerGreetingEl.textContent = `Hi ${gameConfig.playerName}! Fill in the missing letters to complete the word!`;
        totalQuestionsEl.textContent = gameConfig.totalQuestions;
        
        // Show timer if configured
        if (gameConfig.timerSeconds > 0) {
            timerDisplayEl.style.display = 'inline-block';
        }
        
        // Speak welcome message
        speakText(`Welcome ${gameConfig.playerName}! Let's start the spelling game!`);
    } else {
        // No configuration found, redirect to home
        window.location.href = 'home.html';
        return;
    }
}

function setupEventListeners() {
    // Enter key to submit answer
    userInputEl.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            checkAnswer();
        }
    });

    // Auto-focus on input
    userInputEl.addEventListener('click', function() {
        this.select();
        initializeAudio(); // Initialize audio on first user interaction
    });

    // Initialize audio on any button click
    document.addEventListener('click', initializeAudio, { once: true });
    document.addEventListener('keydown', initializeAudio, { once: true });
}

// Initialize audio context on first user interaction
function initializeAudio() {
    if (!audioInitialized) {
        try {
            audioContext = new (window.AudioContext || window.webkitAudioContext)();
            audioInitialized = true;
            console.log('Audio initialized successfully');
        } catch (error) {
            console.error('Failed to initialize audio:', error);
        }
    }
}

// Get a new word from the backend
async function getNewWord() {
    // Check if game is complete
    if (gameConfig && gameConfig.currentQuestion >= gameConfig.totalQuestions) {
        endGame();
        return;
    }

    try {
        showLoading(true);
        stopTimer(); // Stop any existing timer
        
        const response = await fetch('/api/word');
        
        if (!response.ok) {
            throw new Error('Failed to fetch word');
        }
        
        currentWord = await response.json();
        
        // Update UI
        scrambledWordEl.textContent = currentWord.withBlanks;
        hintEl.textContent = currentWord.hint;
        userInputEl.value = '';
        resultEl.textContent = '';
        resultEl.className = 'result-message';
        
        // Update question counter only if this is a new question (not manual new word)
        if (gameConfig && (gameConfig.currentQuestion === 0 || totalCount > 0)) {
            gameConfig.currentQuestion = Math.min(gameConfig.currentQuestion + 1, gameConfig.totalQuestions);
            currentQuestionEl.textContent = gameConfig.currentQuestion;
            updateProgressBar();
            sessionStorage.setItem('gameConfig', JSON.stringify(gameConfig));
        }
        
        // Hide hint by default
        hintVisible = false;
        hintEl.classList.remove('show');
        
        // Focus on input
        userInputEl.focus();
        
        // Start timer if configured
        startTimer();
        
        showLoading(false);
        
    } catch (error) {
        console.error('Error fetching word:', error);
        scrambledWordEl.textContent = 'Error loading word';
        showLoading(false);
    }
}

// Check the user's answer
async function checkAnswer() {
    const userInput = userInputEl.value.trim();
    
    if (!userInput) {
        showResult('Please enter an answer!', false);
        return;
    }
    
    if (!currentWord) {
        showResult('Please get a new word first!', false);
        return;
    }
    
    try {
        showLoading(true);
        
        const response = await fetch('/api/check', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                userInput: userInput,
                originalWord: currentWord.original
            })
        });
        
        if (!response.ok) {
            throw new Error('Failed to check answer');
        }
        
        const result = await response.json();
        
        // Update score
        totalCount++;
        if (result.correct) {
            correctCount++;
            consecutiveFailures = 0;
            stopTimer(); // Stop timer on correct answer
        } else {
            consecutiveFailures++;
        }
        updateScore();
        
        // Show result with sounds
        showResult(result.message, result.correct);
        
        // If correct, automatically get a new word after a delay
        if (result.correct) {
            setTimeout(() => {
                getNewWord();
            }, 2000);
        } else {
            // For incorrect answers, give them another chance unless time runs out
            stopTimer();
            setTimeout(() => {
                if (gameConfig && gameConfig.timerSeconds > 0) {
                    startTimer(); // Restart timer for retry
                }
            }, 1000);
        }
        
        showLoading(false);
        
    } catch (error) {
        console.error('Error checking answer:', error);
        showResult('Error checking answer. Please try again!', false);
        showLoading(false);
    }
}

// Show result message
function showResult(message, isCorrect) {
    resultEl.textContent = message;
    resultEl.className = 'result-message ' + (isCorrect ? 'correct' : 'incorrect');
    
    // Play sounds and animations based on result
    if (isCorrect) {
        playSuccessSound();
        celebrateCorrectAnswer();
        userInputEl.value = '';
    } else {
        // Play different sounds based on consecutive failures
        if (consecutiveFailures >= 3) {
            playConsecutiveFailSound();
        } else {
            playFailSound();
        }
        userInputEl.select();
    }
}

// Toggle hint visibility
function toggleHint() {
    hintVisible = !hintVisible;
    if (hintVisible) {
        hintEl.classList.add('show');
        hintBtn.textContent = 'Hide Hint';
    } else {
        hintEl.classList.remove('show');
        hintBtn.textContent = 'Show Hint';
    }
}

// Update score display
function updateScore() {
    correctCountEl.textContent = correctCount;
    totalCountEl.textContent = totalCount;
    
    // Calculate and display accuracy percentage
    const accuracy = totalCount > 0 ? Math.round((correctCount / totalCount) * 100) : 0;
    accuracyPercentageEl.textContent = accuracy + '%';
}

// Update progress bar
function updateProgressBar() {
    if (gameConfig) {
        const progressBar = document.getElementById('progress-bar');
        const progress = (gameConfig.currentQuestion / gameConfig.totalQuestions) * 100;
        progressBar.style.setProperty('--progress', `${progress}%`);
        progressBar.style.background = `linear-gradient(90deg, #4a90e2 ${progress}%, #e0e0e0 ${progress}%)`;
    }
}

// Show loading state
function showLoading(isLoading) {
    if (isLoading) {
        submitBtn.disabled = true;
        submitBtn.textContent = 'Loading...';
        newWordBtn.disabled = true;
        scrambledWordEl.classList.add('loading');
    } else {
        submitBtn.disabled = false;
        submitBtn.textContent = 'Submit';
        newWordBtn.disabled = false;
        scrambledWordEl.classList.remove('loading');
    }
}

// Add some fun sound effects using Web Audio API
function playSuccessSound() {
    console.log('Attempting to play success sound...');
    
    // Try Web Audio API first
    try {
        if (!audioContext || audioContext.state === 'suspended') {
            audioContext = new (window.AudioContext || window.webkitAudioContext)();
        }
        
        if (audioContext.state === 'suspended') {
            audioContext.resume().then(() => {
                playSuccessAudio();
            });
        } else {
            playSuccessAudio();
        }
    } catch (error) {
        console.error('Web Audio API failed, trying fallback:', error);
        playSuccessSoundAlt();
    }
}

function playSuccessAudio() {
    const frequencies = [523.25, 659.25, 783.99]; // C5, E5, G5 (major chord)
    
    frequencies.forEach((freq, index) => {
        setTimeout(() => {
            const oscillator = audioContext.createOscillator();
            const gainNode = audioContext.createGain();
            
            oscillator.connect(gainNode);
            gainNode.connect(audioContext.destination);
            
            oscillator.frequency.value = freq;
            gainNode.gain.setValueAtTime(0.3, audioContext.currentTime);
            gainNode.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + 0.4);
            
            oscillator.start(audioContext.currentTime);
            oscillator.stop(audioContext.currentTime + 0.4);
        }, index * 100);
    });
    
    console.log('Success sound played successfully');
}

function playFailSound() {
    console.log('Attempting to play fail sound...');
    
    try {
        if (!audioContext || audioContext.state === 'suspended') {
            audioContext = new (window.AudioContext || window.webkitAudioContext)();
        }
        
        if (audioContext.state === 'suspended') {
            audioContext.resume().then(() => {
                playFailAudio();
            });
        } else {
            playFailAudio();
        }
    } catch (error) {
        console.error('Web Audio API failed for fail sound:', error);
        playFailSoundAlt();
    }
}

function playFailAudio() {
    const oscillator = audioContext.createOscillator();
    const gainNode = audioContext.createGain();
    
    oscillator.connect(gainNode);
    gainNode.connect(audioContext.destination);
    
    oscillator.frequency.value = 220; // A3 note
    gainNode.gain.setValueAtTime(0.2, audioContext.currentTime);
    gainNode.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + 0.3);
    
    oscillator.start(audioContext.currentTime);
    oscillator.stop(audioContext.currentTime + 0.3);
    
    console.log('Fail sound played successfully');
}

// Alternative HTML5 Audio implementation for better compatibility
function playSuccessSoundAlt() {
    try {
        // Create audio using HTML5 Audio API with data URLs for simple tones
        const audioElement = new Audio();
        
        // Generate a simple success tone data URL
        const sampleRate = 22050;
        const duration = 0.5;
        const samples = Math.floor(sampleRate * duration);
        const data = new Array(samples);
        
        for (let i = 0; i < samples; i++) {
            const t = i / sampleRate;
            // Create a pleasant chord
            data[i] = Math.sin(2 * Math.PI * 523.25 * t) * 0.3 + // C5
                     Math.sin(2 * Math.PI * 659.25 * t) * 0.3 + // E5
                     Math.sin(2 * Math.PI * 783.99 * t) * 0.3;  // G5
        }
        
        console.log('Playing success sound (HTML5 Audio fallback)');
    } catch (error) {
        console.error('Error with HTML5 audio fallback:', error);
    }
}

function playFailSoundAlt() {
    try {
        console.log('Playing fail sound (HTML5 Audio fallback)');
        // Simple beep using oscillator with reduced complexity
        if (window.AudioContext || window.webkitAudioContext) {
            const context = new (window.AudioContext || window.webkitAudioContext)();
            const oscillator = context.createOscillator();
            const gain = context.createGain();
            
            oscillator.connect(gain);
            gain.connect(context.destination);
            
            oscillator.frequency.value = 220;
            gain.gain.setValueAtTime(0.1, context.currentTime);
            gain.gain.exponentialRampToValueAtTime(0.01, context.currentTime + 0.3);
            
            oscillator.start();
            oscillator.stop(context.currentTime + 0.3);
        }
    } catch (error) {
        console.error('Error with fail sound fallback:', error);
    }
}

// Add celebration animation for correct answers
function celebrateCorrectAnswer() {
    // Add confetti effect (simple version)
    const colors = ['#f39c12', '#e74c3c', '#9b59b6', '#3498db', '#2ecc71'];
    
    for (let i = 0; i < 20; i++) {
        setTimeout(() => {
            createConfetti(colors[Math.floor(Math.random() * colors.length)]);
        }, i * 50);
    }
}

function createConfetti(color) {
    const confetti = document.createElement('div');
    confetti.style.position = 'fixed';
    confetti.style.width = '10px';
    confetti.style.height = '10px';
    confetti.style.backgroundColor = color;
    confetti.style.left = Math.random() * window.innerWidth + 'px';
    confetti.style.top = '-10px';
    confetti.style.zIndex = '1000';
    confetti.style.borderRadius = '50%';
    confetti.style.pointerEvents = 'none';
    
    document.body.appendChild(confetti);
    
    const animationDuration = 3000;
    const startTime = Date.now();
    
    function animate() {
        const elapsed = Date.now() - startTime;
        const progress = elapsed / animationDuration;
        
        if (progress < 1) {
            confetti.style.top = (progress * window.innerHeight) + 'px';
            confetti.style.opacity = 1 - progress;
            requestAnimationFrame(animate);
        } else {
            document.body.removeChild(confetti);
        }
    }
    
    animate();
}

// Timer functions
function startTimer() {
    // Clear any existing timer
    if (gameTimer) {
        clearInterval(gameTimer);
    }
    
    // Only start timer if configured
    if (!gameConfig || gameConfig.timerSeconds <= 0) {
        return;
    }
    
    timeRemaining = gameConfig.timerSeconds;
    updateTimerDisplay();
    
    gameTimer = setInterval(() => {
        timeRemaining--;
        updateTimerDisplay();
        
        if (timeRemaining <= 0) {
            handleTimeOut();
        }
    }, 1000);
}

function updateTimerDisplay() {
    if (timerCountdownEl) {
        timerCountdownEl.textContent = timeRemaining;
        
        // Add warning style when time is running low
        if (timeRemaining <= 5) {
            timerDisplayEl.classList.add('warning');
        } else {
            timerDisplayEl.classList.remove('warning');
        }
    }
}

function stopTimer() {
    if (gameTimer) {
        clearInterval(gameTimer);
        gameTimer = null;
    }
}

function handleTimeOut() {
    stopTimer();
    
    // Count as incorrect answer
    totalCount++;
    consecutiveFailures++;
    updateScore();
    
    // Show timeout message
    showResult(`⏰ Time's up! The word was "${currentWord.original.toUpperCase()}"`, false);
    
    // Play timeout sound
    playConsecutiveFailSound();
    
    // Move to next word after delay
    setTimeout(() => {
        getNewWord();
    }, 2000);
}

// Text-to-speech function
function speakText(text) {
    if ('speechSynthesis' in window) {
        // Wait for voices to be loaded
        function speak() {
            const utterance = new SpeechSynthesisUtterance(text);
            utterance.rate = 0.8;
            utterance.pitch = 1.1;
            utterance.volume = 0.8;
            
            // Try to use a child-friendly voice if available
            const voices = speechSynthesis.getVoices();
            const preferredVoice = voices.find(voice => 
                voice.name.includes('Female') || 
                voice.name.includes('Samantha') || 
                voice.name.includes('Karen') ||
                voice.lang.includes('en')
            );
            if (preferredVoice) {
                utterance.voice = preferredVoice;
            }
            
            speechSynthesis.speak(utterance);
        }
        
        // If voices are already loaded, speak immediately
        if (speechSynthesis.getVoices().length > 0) {
            speak();
        } else {
            // Wait for voices to load
            speechSynthesis.addEventListener('voiceschanged', speak, { once: true });
        }
    }
}

// Game management functions
function endGame() {
    stopTimer();
    
    const accuracy = totalCount > 0 ? Math.round((correctCount / totalCount) * 100) : 0;
    const playerName = gameConfig ? gameConfig.playerName : 'Player';
    
    // Show game over modal
    const modal = document.getElementById('game-over-modal');
    const resultsDiv = document.getElementById('final-results');
    
    resultsDiv.innerHTML = `
        <div class="final-results">
            <p><span class="result-highlight">${playerName}</span>, you completed the game!</p>
            <p>🎯 Questions Answered: <span class="result-highlight">${totalCount}</span></p>
            <p>✅ Correct Answers: <span class="result-highlight">${correctCount}</span></p>
            <p>📊 Accuracy: <span class="result-highlight">${accuracy}%</span></p>
            <p>⭐ Score: <span class="result-highlight">${correctCount}/${gameConfig.totalQuestions}</span></p>
        </div>
    `;
    
    modal.style.display = 'flex';
    
    // Speak final results
    const finalMessage = `Congratulations ${playerName}! You scored ${correctCount} out of ${gameConfig.totalQuestions} questions with ${accuracy} percent accuracy. Great job!`;
    setTimeout(() => speakText(finalMessage), 1000);
}

function quitGame() {
    if (confirm('Are you sure you want to quit the game?')) {
        window.location.href = 'home.html';
    }
}

function playAgain() {
    // Reset game state but keep same configuration
    if (gameConfig) {
        gameConfig.currentQuestion = 0;
        sessionStorage.setItem('gameConfig', JSON.stringify(gameConfig));
    }
    
    // Reset counters
    correctCount = 0;
    totalCount = 0;
    consecutiveFailures = 0;
    
    // Hide modal and restart
    document.getElementById('game-over-modal').style.display = 'none';
    updateScore();
    getNewWord();
}

function goHome() {
    window.location.href = 'home.html';
}

function playConsecutiveFailSound() {
    console.log('Playing consecutive fail sound...');
    
    try {
        if (!audioContext || audioContext.state === 'suspended') {
            audioContext = new (window.AudioContext || window.webkitAudioContext)();
        }
        
        if (audioContext.state === 'suspended') {
            audioContext.resume().then(() => {
                playConsecutiveFailAudio();
            });
        } else {
            playConsecutiveFailAudio();
        }
    } catch (error) {
        console.error('Error playing consecutive fail sound:', error);
    }
}

function playConsecutiveFailAudio() {
    // Play three descending notes for consecutive failures
    const frequencies = [330, 277, 220]; // E4, C#4, A3
    
    frequencies.forEach((freq, index) => {
        setTimeout(() => {
            const oscillator = audioContext.createOscillator();
            const gainNode = audioContext.createGain();
            
            oscillator.connect(gainNode);
            gainNode.connect(audioContext.destination);
            
            oscillator.frequency.value = freq;
            gainNode.gain.setValueAtTime(0.15, audioContext.currentTime);
            gainNode.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + 0.2);
            
            oscillator.start(audioContext.currentTime);
            oscillator.stop(audioContext.currentTime + 0.2);
        }, index * 150);
    });
}
