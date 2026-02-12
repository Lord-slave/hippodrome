import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.CsvSource;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;

import static org.junit.jupiter.api.Assertions.*;

class HorseTest {

    // Тест 1: Проверка, что при передаче null в name выбрасывается исключение
    @Test
    void constructor_ShouldThrowException_WhenNameIsNull() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Horse(null, 10.0, 100.0)
        );


        assertEquals("Name cannot be null.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "\t", "\n", "   \t\n"})
    void constructor_ShouldThrowException_WhenNameIsBlank(String name) {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Horse(name, 10.0, 100.0)
        );

        assertEquals("Name cannot be blank.", exception.getMessage());
    }

    @Test
    void constructor_ShouldThrowException_WhenSpeedIsNegative() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Horse("Bucephalus", -5.0, 100.0)
        );

        assertEquals("Speed cannot be negative.", exception.getMessage());
    }

    @Test
    void constructor_ShouldThrowException_WhenDistanceIsNegative() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Horse("Bucephalus", 10.0, -100.0)
        );

        assertEquals("Distance cannot be negative.", exception.getMessage());
    }

    @Test
    void getName_ShouldReturnNamePassedToConstructor() {
        // Arrange
        String expectedName = "Bucephalus";
        Horse horse = new Horse(expectedName, 5.0, 10.0);

        // Act
        String actualName = horse.getName();

        // Assert
        assertEquals(expectedName, actualName);
    }

    @Test
    void getSpeed_ShouldReturnSpeedPassedToConstructor() {
        // Arrange
        double expectedSpeed = 7.5;
        Horse horse = new Horse("Bucephalus", expectedSpeed, 10.0);

        // Act
        double actualSpeed = horse.getSpeed();

        // Assert
        assertEquals(expectedSpeed, actualSpeed);
    }

    @Test
    void getDistance_ShouldReturnDistancePassedToThreeParamConstructor() {
        // Arrange
        double expectedDistance = 15.0;
        Horse horse = new Horse("Bucephalus", 5.0, expectedDistance);

        // Act
        double actualDistance = horse.getDistance();

        // Assert
        assertEquals(expectedDistance, actualDistance);
    }

    @Test
    void getDistance_ShouldReturnZero_WhenCreatedWithTwoParamConstructor() {
        // Arrange
        Horse horse = new Horse("Bucephalus", 5.0);

        // Act
        double actualDistance = horse.getDistance();

        // Assert
        assertEquals(0.0, actualDistance);
    }

    @Test
    void move_ShouldCallGetRandomDoubleWithCorrectParameters() {

        try (MockedStatic<Horse> mockedStatic = Mockito.mockStatic(Horse.class)) {
            // Arrange
            Horse horse = new Horse("Bucephalus", 5.0, 10.0);

            // Настраиваем мок: при вызове getRandomDouble с любыми double возвращаем 0.5
            mockedStatic.when(() -> Horse.getRandomDouble(Mockito.anyDouble(), Mockito.anyDouble()))
                    .thenReturn(0.5);

            // Act
            horse.move();

            // Assert
            mockedStatic.verify(
                    () -> Horse.getRandomDouble(0.2, 0.9),
                    times(1)
            );
        }
    }

    @ParameterizedTest
    @CsvSource({
            "10.0, 2.0, 0.2, 10.4",   // distance=10, speed=2, random=0.2 -> 10 + 2*0.2 = 10.4
            "10.0, 2.0, 0.9, 11.8",   // 10 + 2*0.9 = 11.8
            "0.0, 5.0, 0.5, 2.5",     // 0 + 5*0.5 = 2.5
            "7.5, 1.5, 0.7, 8.55"     // 7.5 + 1.5*0.7 = 8.55
    })
    void move_ShouldIncreaseDistanceBySpeedTimesRandom(
            double initialDistance,
            double speed,
            double randomValue,
            double expectedDistance) {

        // Мокируем статический метод
        try (MockedStatic<Horse> mockedStatic = Mockito.mockStatic(Horse.class)) {
            // Arrange
            Horse horse = new Horse("Bucephalus", speed, initialDistance);

            // Подменяем getRandomDouble: при любых аргументах возвращаем randomValue
            mockedStatic.when(() -> Horse.getRandomDouble(Mockito.anyDouble(), Mockito.anyDouble()))
                    .thenReturn(randomValue);

            // Act
            horse.move();

            // Assert
            assertEquals(expectedDistance, horse.getDistance(), 0.0001);
        }
    }
}
