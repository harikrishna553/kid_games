package com.example.quizgame.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public class GameUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(GameUtils.class);
    private static final Random random = new Random();

    /**
     * Generates a random session ID
     */
    public static String generateSessionId() {
        return "session-" + System.currentTimeMillis() + "-" + random.nextInt(1000);
    }

    /**
     * Shuffles a list in place
     */
    public static <T> void shuffleList(List<T> list) {
        for (int i = list.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            T temp = list.get(i);
            list.set(i, list.get(j));
            list.set(j, temp);
        }
    }

    /**
     * Validates if a string is not null and not empty
     */
    public static boolean isValidString(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * Validates if a list is not null and not empty
     */
    public static boolean isValidList(List<?> list) {
        return list != null && !list.isEmpty();
    }

    /**
     * Safely converts a string to integer with default value
     */
    public static int safeParseInt(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            logger.warn("Failed to parse integer: {}, using default: {}", str, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Formats accuracy percentage for display
     */
    public static String formatAccuracy(double accuracy) {
        return String.format("%.1f%%", accuracy);
    }

    /**
     * Gets a performance message based on accuracy
     */
    public static String getPerformanceMessage(double accuracy) {
        if (accuracy >= 90) {
            return "Outstanding! You're a quiz master!";
        } else if (accuracy >= 80) {
            return "Excellent work! You really know your stuff!";
        } else if (accuracy >= 70) {
            return "Good job! You're getting there!";
        } else if (accuracy >= 60) {
            return "Not bad! Keep practicing!";
        } else {
            return "Keep trying! Practice makes perfect!";
        }
    }

    /**
     * Gets encouragement message based on performance
     */
    public static String getEncouragementMessage(String username, int correct, int total) {
        double accuracy = (double) correct / total * 100;
        
        if (accuracy >= 90) {
            return String.format("Incredible, %s! You got %d out of %d correct. You're a true champion!", 
                                username, correct, total);
        } else if (accuracy >= 80) {
            return String.format("Great job, %s! You answered %d out of %d correctly. That's amazing!", 
                                username, correct, total);
        } else if (accuracy >= 70) {
            return String.format("Well done, %s! You got %d out of %d right. You're doing great!", 
                                username, correct, total);
        } else if (accuracy >= 60) {
            return String.format("Good effort, %s! You answered %d out of %d correctly. Keep it up!", 
                                username, correct, total);
        } else {
            return String.format("Nice try, %s! You got %d out of %d. Don't give up - you're learning!", 
                                username, correct, total);
        }
    }

    /**
     * Determines if confetti should be shown based on performance
     */
    public static boolean shouldShowConfetti(double accuracy) {
        return accuracy >= 80.0;
    }

    /**
     * Gets the trophy emoji based on performance
     */
    public static String getTrophyEmoji(double accuracy) {
        if (accuracy >= 90) {
            return "🏆"; // Gold trophy
        } else if (accuracy >= 80) {
            return "🥇"; // Gold medal
        } else if (accuracy >= 70) {
            return "🥈"; // Silver medal
        } else if (accuracy >= 60) {
            return "🥉"; // Bronze medal
        } else {
            return "🎯"; // Target (participation)
        }
    }

    /**
     * Cleans and validates quiz title
     */
    public static String cleanQuizTitle(String title) {
        if (title == null) {
            return "Unknown Quiz";
        }
        return title.trim().replaceAll("\\s+", " ");
    }

    /**
     * Validates time per question is within acceptable range
     */
    public static int validateTimePerQuestion(int timePerQuestion) {
        if (timePerQuestion < 10) {
            return 30; // Minimum 30 seconds
        } else if (timePerQuestion > 300) {
            return 300; // Maximum 5 minutes
        }
        return timePerQuestion;
    }

    /**
     * Validates number of questions is within acceptable range
     */
    public static int validateNumberOfQuestions(int numberOfQuestions) {
        if (numberOfQuestions < 1) {
            return 5; // Minimum 5 questions
        } else if (numberOfQuestions > 100) {
            return 100; // Maximum 100 questions
        }
        return numberOfQuestions;
    }
}
