// Kids Spelling Game JavaScript
let currentWord = null;
let correctCount = 0;
let totalCount = 0;
let hintVisible = false;

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

// Initialize the game
document.addEventListener('DOMContentLoaded', function() {
    getNewWord();
    setupEventListeners();
});

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
    });
}

// Get a new word from the backend
async function getNewWord() {
    try {
        showLoading(true);
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
        
        // Hide hint by default
        hintVisible = false;
        hintEl.classList.remove('show');
        
        // Focus on input
        userInputEl.focus();
        
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
        }
        updateScore();
        
        // Show result
        showResult(result.message, result.correct);
        
        // If correct, automatically get a new word after a delay
        if (result.correct) {
            setTimeout(() => {
                getNewWord();
            }, 2000);
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
    
    // Clear input if correct
    if (isCorrect) {
        userInputEl.value = '';
    } else {
        // Select text for easy correction
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

// Add some fun sound effects (optional - using Web Audio API)
function playSuccessSound() {
    try {
        const audioContext = new (window.AudioContext || window.webkitAudioContext)();
        const oscillator = audioContext.createOscillator();
        const gainNode = audioContext.createGain();
        
        oscillator.connect(gainNode);
        gainNode.connect(audioContext.destination);
        
        oscillator.frequency.value = 523.25; // C5 note
        gainNode.gain.setValueAtTime(0.3, audioContext.currentTime);
        gainNode.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + 0.5);
        
        oscillator.start(audioContext.currentTime);
        oscillator.stop(audioContext.currentTime + 0.5);
    } catch (error) {
        // Ignore audio errors
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

// Enhanced result showing with animations
function showResult(message, isCorrect) {
    resultEl.textContent = message;
    resultEl.className = 'result-message ' + (isCorrect ? 'correct' : 'incorrect');
    
    if (isCorrect) {
        playSuccessSound();
        celebrateCorrectAnswer();
        userInputEl.value = '';
    } else {
        userInputEl.select();
    }
}
