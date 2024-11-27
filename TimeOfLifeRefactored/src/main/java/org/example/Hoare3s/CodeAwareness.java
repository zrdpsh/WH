package org.example.Hoare3s;/*
1.
 в случае с изменением метода в суперклассе без Override в классе-родителе появляется самостоятельный метод
 makeGenericSound, ничего не знающий о существовании наследника, а makeSound в классе-наследнике остаётся
 сам по себе. При вызове myCat.makeSound() вызывается самостоятельный метод makeSound().
 Обе функции отвязаны друг от друга, и ошибок не будет
 */


class Animal {
    // Изменен метод в суперклассе
    public void makeGenericSound() {
        System.out.println("Some generic animal sound");
    }
}

/*
2.
В этом случае мы прямо заявляем осведомлённость класса-наследника о классе-родителе (Override), код скомпилируется
и выведет четыре Meow.
 */


class Cat extends Animal {
    @Override
    public void makeSound(int numberOfSounds) {
        for (int i = 0; i < numberOfSounds; i++) {
            System.out.println("Meow");
        }
    }

    @Override
    public void makeSound() {
        System.out.println("Meow");
    }
}

/*
3.
Здесь явно выделены:
- методы readValue, writerWithDefaultPrettyPrinter(), writeValueAsString() у objectMapper и get у Map.
Если что-то пойдёт не так, компилятор не пустит дальше. Также проверяться будет, что writerWithDefaultPrettyPrinter
возвращает тот тип объекта, который и получил.
Незримой остаётся логическая связь между objectMapper и jsonString - метод знает о существовании строки,
строка при этом может оказаться любой, без форматирования в т.ч. и отработать непредсказуемо.
 */

public class Main {
    public static void main(String[] args) {
        // Создаем объект ObjectMapper для парсинга JSON
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonString = "{\"name\":\"John\", \"age\":30}";

        try {
            // Парсим JSON-строку в HashMap
            Map<String, Object> result = objectMapper.readValue(jsonString, HashMap.class);

            System.out.println("Name: " + result.get("name"));
        } catch (IOException e) {
            // Обработка ошибки парсинга
            e.printStackTrace();
        }

        try {
            String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
            System.out.println("Pretty JSON: " + prettyJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}