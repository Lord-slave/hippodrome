import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource; // или другие
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HippodromeTest {

    private static final Logger logger = LoggerFactory.getLogger(HippodromeTest.class);

    // Здесь будут тесты
    @Test
    void constructor_ShouldThrowException_WhenHorsesIsNull() {
        logger.info("Тест: конструктор с null");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Hippodrome(null)
        );

        assertEquals("Horses cannot be null.", exception.getMessage());
    }

    @Test
    void constructor_ShouldThrowException_WhenHorsesIsEmpty() {
        logger.info("Тест: конструктор с пустым списком");

        List<Horse> emptyList = List.of(); // пустой список

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Hippodrome(emptyList)
        );

        assertEquals("Horses cannot be empty.", exception.getMessage());
    }

    @Test
    void getHorses_ShouldReturnSameListInSameOrder() {
        logger.info("Тест: getHorses() возвращает тот же список и порядок");

        // Arrange
        List<Horse> originalHorses = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            originalHorses.add(new Horse("Horse " + i, i + 1, i * 10));
        }
        Hippodrome hippodrome = new Hippodrome(originalHorses);

        // Act
        List<Horse> returnedHorses = hippodrome.getHorses();

        // Assert
        assertEquals(originalHorses.size(), returnedHorses.size());
        for (int i = 0; i < originalHorses.size(); i++) {
            assertSame(originalHorses.get(i), returnedHorses.get(i),
                    "Объект на позиции " + i + " не совпадает");
        }
    }

    @Test
    void move_ShouldCallMoveOnAllHorses() {
        logger.info("Тест: move() вызывает move() у всех лошадей");

        // Arrange
        List<Horse> mockHorses = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Horse mockHorse = mock(Horse.class); // создаём мок
            mockHorses.add(mockHorse);
        }
        Hippodrome hippodrome = new Hippodrome(mockHorses);

        // Act
        hippodrome.move();

        // Assert
        for (Horse horse : mockHorses) {
            verify(horse, times(1)).move();
        }
    }

    @Test
    void getWinner_ShouldReturnHorseWithMaxDistance() {
        logger.info("Тест: getWinner() возвращает лошадь с наибольшей дистанцией");

        // Arrange
        Horse horse1 = new Horse("Horse A", 10, 50);
        Horse horse2 = new Horse("Horse B", 12, 70); // наибольшая
        Horse horse3 = new Horse("Horse C", 11, 65);
        Horse horse4 = new Horse("Horse D", 9, 40);

        List<Horse> horses = List.of(horse1, horse3, horse2, horse4); // порядок любой
        Hippodrome hippodrome = new Hippodrome(horses);

        // Act
        Horse winner = hippodrome.getWinner();

        // Assert
        assertSame(horse2, winner);
    }
}