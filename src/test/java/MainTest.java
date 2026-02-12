import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

class MainTest {

    @Test
    @Timeout(value = 22, unit = TimeUnit.SECONDS)
    @Disabled("Тест проверяет общее время работы программы и отключен для ускорения сборки")
    void main_ShouldCompleteWithin22Seconds() throws Exception {
        // Вызываем метод main с пустым массивом аргументов
        Main.main(new String[]{});
    }
}